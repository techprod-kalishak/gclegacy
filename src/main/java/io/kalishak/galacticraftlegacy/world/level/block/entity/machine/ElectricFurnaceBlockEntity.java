package io.kalishak.galacticraftlegacy.world.level.block.entity.machine;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.world.inventory.ElectricFurnaceMenu;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.DelegatingResourceHandler;
import net.neoforged.neoforge.transfer.RangedResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.energy.VoidingEnergyHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class ElectricFurnaceBlockEntity extends AbstractMachineBlockEntity implements RecipeCraftingHolder, StackedContentsCompatible {
    public static final int SLOT_INPUT = 0;
    public static final int SLOT_BATTERY = 1;
    public static final int SLOT_RESULT = 2;
    private static final Codec<Map<ResourceKey<Recipe<?>>, Integer>> RECIPES_USED_CODEC = Codec.unboundedMap(Recipe.KEY_CODEC, Codec.INT);
    int cookingTimer;
    int cookingTotalTime;
    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> ElectricFurnaceBlockEntity.this.cookingTimer;
                case 1 -> ElectricFurnaceBlockEntity.this.cookingTotalTime;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> ElectricFurnaceBlockEntity.this.cookingTimer = value;
                case 1 -> ElectricFurnaceBlockEntity.this.cookingTotalTime = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };
    private final Reference2IntOpenHashMap<ResourceKey<Recipe<?>>> recipesUsed = new Reference2IntOpenHashMap<>();
    private final RecipeManager.CachedCheck<SingleRecipeInput, SmeltingRecipe> quickCheck;

    public ElectricFurnaceBlockEntity(BlockPos pos, BlockState blockState) {
        super(GalacticraftBlockEntityType.ELECTRIC_FURNACE.get(), pos, blockState);
        this.quickCheck = RecipeManager.createCheck(RecipeType.SMELTING);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.Item.BLOCK,
                GalacticraftBlockEntityType.ELECTRIC_FURNACE.get(),
                (blockEntity, cxt) -> {
                    if (cxt != null) {
                        if (cxt.getAxis().isVertical()) {
                            return cxt == Direction.UP ? RangedResourceHandler.ofSingleIndex(() -> blockEntity.innerResourceHandler, 0) : RangedResourceHandler.of(() -> blockEntity.innerResourceHandler, 1, 3);
                        }

                        return RangedResourceHandler.ofSingleIndex(() -> blockEntity.innerResourceHandler, 1);
                    }

                    return new DelegatingResourceHandler<>(blockEntity.innerResourceHandler);
                }
        );
        event.registerBlockEntity(
                Capabilities.Energy.BLOCK,
                GalacticraftBlockEntityType.ELECTRIC_FURNACE.get(),
                (blockEntity, cxt) -> {
                    if (cxt == null || cxt == Direction.EAST) {
                        return blockEntity.energyHandler;
                    }

                    return VoidingEnergyHandler.INSTANCE;
                }
        );
    }

    private boolean isLit() {
        return this.energyHandler.getAmountAsInt() > 0 && this.cookingTotalTime > 0;
    }

    @Override
    protected void loadAdditional(ValueInput valueInput) {
        super.loadAdditional(valueInput);
        this.cookingTimer = valueInput.getIntOr("CookingTimeSpent", (short) 0);
        this.cookingTotalTime = valueInput.getIntOr("CookingTotalTime", (short) 0);
        this.recipesUsed.clear();
        this.recipesUsed.putAll(valueInput.read("RecipesUsed", RECIPES_USED_CODEC).orElse(Map.of()));
    }

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);
        valueOutput.putInt("CookingTimeSpent", this.cookingTimer);
        valueOutput.putInt("CookingTotalTime", this.cookingTotalTime);
        valueOutput.store("RecipesUsed", RECIPES_USED_CODEC, this.recipesUsed);
    }

    @Override
    protected int size() {
        return 3;
    }

    @Override
    protected int getBatterySlotIndex() {
        return SLOT_BATTERY;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.galacticraftlegacy.electric_furnace");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ElectricFurnaceMenu(containerId, playerInventory, this, this.dataAccess);
    }

    @Override
    public void set(int index, ItemResource resource, int amount) {

        if (index == SLOT_INPUT) {
            ItemStack stack = ItemUtil.getStack(this.innerResourceHandler, index);

            if ((stack.isEmpty() || resource.matches(stack)) ) {
                if (this.level instanceof ServerLevel serverLevel) {
                    this.cookingTotalTime = getTotalSmeltingTime(serverLevel, this);
                    this.cookingTimer = 0;
                    setChanged();
                }
            }
        }

        super.set(index, resource, amount);
    }

    public static void serverTick(ServerLevel level, BlockPos pos, BlockState state, ElectricFurnaceBlockEntity furnace) {
        boolean isLit = furnace.isLit();
        boolean hadChanged = false;

        energyTransferTick(furnace, furnace.cookingTimer > 0, false, BASIC_MACHINE_MAX_TRANSFER_RATE, null);

        ItemStack input = ItemUtil.getStack(furnace.innerResourceHandler, SLOT_INPUT);
        boolean haveEnergy = furnace.hasEnergyToOperate();

        if (furnace.hasEnergyToOperate() || !input.isEmpty() && haveEnergy) {
            SingleRecipeInput recipeInput = new SingleRecipeInput(input);
            RecipeHolder<SmeltingRecipe> recipeHolder = null;

            if (!input.isEmpty()) {
                recipeHolder = furnace.quickCheck.getRecipeFor(recipeInput, level).orElse(null);
            }

            if (furnace.hasEnergyToOperate() && canHeat(recipeHolder, recipeInput, furnace.innerResourceHandler, furnace.energyHandler)) {
                furnace.cookingTimer++;

                if (furnace.cookingTimer == furnace.cookingTotalTime) {
                    furnace.cookingTimer = 0;
                    furnace.cookingTotalTime = getTotalSmeltingTime(level, furnace);

                    if (heat(recipeHolder, recipeInput, furnace.innerResourceHandler, furnace.energyHandler)) {
                        furnace.setRecipeUsed(recipeHolder);
                    }

                    hadChanged = true;
                }
            } else {
                furnace.cookingTimer = 0;
            }
        } else if (!furnace.hasEnergyToOperate() && furnace.cookingTimer > 0) {
            furnace.cookingTimer = Mth.clamp(furnace.cookingTimer - 2, 0, furnace.cookingTotalTime);
        }

        if (isLit != furnace.isLit()) {
            hadChanged = true;
            state = state.setValue(AbstractFurnaceBlock.LIT, furnace.isLit());
            level.setBlock(pos, state, AbstractFurnaceBlock.UPDATE_ALL);
        }

        if (hadChanged) {
            BlockEntity.setChanged(level, pos, state);
        }
    }

    private static int getTotalSmeltingTime(ServerLevel level, ElectricFurnaceBlockEntity furnace) {
        SingleRecipeInput recipeInput = new SingleRecipeInput(ItemUtil.getStack(furnace.innerResourceHandler, SLOT_INPUT));
        return furnace.quickCheck.getRecipeFor(recipeInput, level).map(recipeHolder -> recipeHolder.value().cookingTime()).orElse(100);
    }

    private static boolean canHeat(@Nullable RecipeHolder<SmeltingRecipe> recipe, SingleRecipeInput recipeInput, ResourceHandler<ItemResource> items, EnergyHandler energyHandler) {
        if (!items.getResource(SLOT_INPUT).isEmpty() && recipe != null && energyHandler.getAmountAsInt() > BASIC_MACHINE_MAX_TRANSFER_RATE) {
            ItemStack recipeResult = recipe.value().assemble(recipeInput);

            if (recipeResult.isEmpty()) {
                return false;
            } else {
                ItemResource resourceInResultSlot = items.getResource(SLOT_RESULT);

                if (resourceInResultSlot.isEmpty()) {
                    return true;
                } else if (!resourceInResultSlot.matches(recipeResult)) {
                    return false;
                } else {
                    int maxStackSize = items.getCapacityAsInt(SLOT_RESULT, resourceInResultSlot);
                    int resourceInSlotSize = items.getAmountAsInt(SLOT_RESULT);

                    return resourceInSlotSize + recipeResult.getCount() <= maxStackSize && resourceInSlotSize + recipeResult.getCount() <= resourceInSlotSize || resourceInSlotSize + recipeResult.getCount() <= recipeResult.getMaxStackSize();
                }
            }
        }

        return false;
    }

    private static boolean heat(@Nullable RecipeHolder<SmeltingRecipe> recipe, SingleRecipeInput recipeInput, ResourceHandler<ItemResource> items, EnergyHandler energyHandler) {
        if (recipe != null && canHeat(recipe, recipeInput, items, energyHandler)) {
            try (Transaction tx = Transaction.open(null)) {
                ItemStack result = recipe.value().assemble(recipeInput);

                if (!result.isEmpty() && items.insert(SLOT_RESULT, ItemResource.of(result), result.getCount(), tx) > 0) {
                    if (items.extract(ItemResource.of(recipeInput.item()), 1, tx) > 0) {
                        energyHandler.extract(BASIC_MACHINE_MAX_TRANSFER_RATE, tx);
                        tx.commit();

                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public @Nullable RecipeHolder<?> getRecipeUsed() {
        return null;
    }

    @Override
    public void setRecipeUsed(@Nullable RecipeHolder<?> recipe) {
        if (recipe != null) {
            ResourceKey<Recipe<?>> recipeKey = recipe.id();
            this.recipesUsed.addTo(recipeKey, 1);
        }
    }

    @Override
    public void awardUsedRecipes(Player player, List<ItemStack> items) {

    }

    public void awardUsedRecipes(ServerPlayer player) {
        List<RecipeHolder<?>> list = this.getRecipesToAward(player.level(), player.position());
        player.awardRecipes(list);

        for (RecipeHolder<?> recipeholder : list) {
            player.triggerRecipeCrafted(recipeholder, this.items);
        }

        this.recipesUsed.clear();
    }

    public List<RecipeHolder<?>> getRecipesToAward(ServerLevel level, Vec3 popVec) {
        List<RecipeHolder<?>> list = Lists.newArrayList();

        for (Reference2IntMap.Entry<ResourceKey<Recipe<?>>> entry : this.recipesUsed.reference2IntEntrySet()) {
            level.recipeAccess().byKey(entry.getKey()).ifPresent(list::add);
        }

        return list;
    }

    @Override
    public void fillStackedContents(StackedItemContents stackedContents) {
        for (int i = 0; i < size(); i++) {
            stackedContents.accountStack(ItemUtil.getStack(this.innerResourceHandler, i));
        }
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);

        if (this.level instanceof ServerLevel serverlevel) {
            getRecipesToAward(serverlevel, Vec3.atCenterOf(pos));
        }
    }
}
