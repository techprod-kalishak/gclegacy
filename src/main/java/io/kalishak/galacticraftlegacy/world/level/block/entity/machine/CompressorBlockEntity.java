/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.machine;

import com.google.common.collect.Lists;
import io.kalishak.galacticraftlegacy.world.inventory.machine.CompressorMenu;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.AnvilCompressingRecipe;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.GalacticraftRecipeType;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.input.CompressingRecipeInput;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import io.kalishak.galacticraftlegacy.world.level.block.entity.NamedBlockEntity;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.transfer.DelegatingResourceHandler;
import net.neoforged.neoforge.transfer.RangedResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class CompressorBlockEntity extends NamedBlockEntity implements AlloyCompressor {
    protected final NonNullList<ItemStack> items = NonNullList.withSize(12, ItemStack.EMPTY);
    protected final ItemStacksResourceHandler innerResourceHandler = new ItemStacksResourceHandler(this.items);
    protected int compressingTimer;
    protected int compressingTotalTime;
    protected int fuelTimeRemaining;
    protected int fuelTotalTime;
    protected final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int dataId) {
            return switch (dataId) {
                case AlloyCompressor.DATA_SLOT_COMPRESSING_TIMER -> CompressorBlockEntity.this.compressingTimer;
                case AlloyCompressor.DATA_SLOT_COMPRESSING_TIME_TOTAL -> CompressorBlockEntity.this.compressingTotalTime;
                case AlloyCompressor.DATA_SLOT_LIT_TIMER -> {
                    if (fuelTotalTime > Short.MAX_VALUE) {
                        yield Mth.floor(((double) fuelTimeRemaining / fuelTotalTime) * Short.MAX_VALUE);
                    }

                    yield CompressorBlockEntity.this.fuelTimeRemaining;
                }
                case AlloyCompressor.DATA_SLOT_LIT_TIME_TOTAL -> Math.min(CompressorBlockEntity.this.fuelTotalTime, Short.MAX_VALUE);
                default -> 0;
            };
        }

        @Override
        public void set(int dataId, int value) {
            switch (dataId) {
                case AlloyCompressor.DATA_SLOT_COMPRESSING_TIMER -> CompressorBlockEntity.this.compressingTimer = value;
                case AlloyCompressor.DATA_SLOT_COMPRESSING_TIME_TOTAL -> CompressorBlockEntity.this.compressingTotalTime = value;
                case AlloyCompressor.DATA_SLOT_LIT_TIMER -> CompressorBlockEntity.this.fuelTimeRemaining = value;
                case AlloyCompressor.DATA_SLOT_LIT_TIME_TOTAL -> CompressorBlockEntity.this.fuelTotalTime = value;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };
    protected final Reference2IntOpenHashMap<ResourceKey<Recipe<?>>> recipesUsed = new Reference2IntOpenHashMap<>();
    protected final RecipeManager.CachedCheck<CompressingRecipeInput, AnvilCompressingRecipe> quickCheck = RecipeManager.createCheck(GalacticraftRecipeType.COMPRESSING.get());

    public CompressorBlockEntity(BlockPos pos, BlockState blockState) {
        super(GalacticraftBlockEntityType.COMPRESSOR.get(), pos, blockState);
    }

    public static void registerItemCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.Item.BLOCK,
                GalacticraftBlockEntityType.COMPRESSOR.get(),
                (blockEntity, cxt) -> {
                    if (cxt != null && cxt.getAxis().isVertical()) {
                        return cxt == Direction.UP
                                ? RangedResourceHandler.of(() -> blockEntity.innerResourceHandler, CRAFTING_SLOT_START, FUEL_SLOT)
                                : RangedResourceHandler.of(() -> blockEntity.innerResourceHandler, RESULT_SLOT_START, RESULT_SLOT_END);
                    }

                    return new DelegatingResourceHandler<>(() -> blockEntity.innerResourceHandler);
                }
        );
    }

    public static void serverTick(ServerLevel level, BlockPos pos, BlockState state, CompressorBlockEntity compressor) {
        boolean changed = false;
        boolean isLit;
        boolean wasLit;
        if (compressor.fuelTimeRemaining > 0) {
            wasLit = true;
            compressor.fuelTimeRemaining--;
            isLit = compressor.fuelTimeRemaining > 0;
        } else {
            wasLit = false;
            isLit = false;
        }

        ItemResource fuel = compressor.innerResourceHandler.getResource(AlloyCompressor.FUEL_SLOT);
        ResourceHandler<ItemResource> ingredients = RangedResourceHandler.of(() -> compressor.innerResourceHandler, AlloyCompressor.CRAFTING_SLOT_START, AlloyCompressor.CRAFTING_SLOT_END);

        boolean hasFuel = !fuel.isEmpty();

        if (isLit || hasFuel) {
            CompressingRecipeInput input = new CompressingRecipeInput(3, 3, ingredients);
            RecipeHolder<AnvilCompressingRecipe> recipe = compressor.quickCheck.getRecipeFor(input, level).orElse(null);
            boolean hasIngredients = recipe != null;

            if (hasIngredients) {
                ItemStack recipeResult = recipe.value().assemble(input);
                ItemResource resourceInResultSlot = compressor.innerResourceHandler.getResource(AlloyCompressor.RESULT_SLOT_START);
                int maxStackSize = compressor.innerResourceHandler.getCapacityAsInt(AlloyCompressor.RESULT_SLOT_START, resourceInResultSlot);

                if (!recipeResult.isEmpty() && AlloyCompressor.canCompress(compressor.innerResourceHandler, maxStackSize, recipeResult)) {
                    if (!isLit) {
                        int newLitTime = fuelDuration(level, fuel);
                        compressor.fuelTimeRemaining = newLitTime;
                        compressor.fuelTotalTime = newLitTime;

                        if (newLitTime > 0) {
                            consumeFuel(compressor.innerResourceHandler, fuel);
                            isLit = true;
                            changed = true;
                        }
                    }

                    if (isLit) {
                        compressor.compressingTimer++;

                        if (compressor.compressingTimer == compressor.compressingTotalTime) {
                            compressor.compressingTimer = 0;
                            compressor.compressingTotalTime = recipe.value().compressingTime();
                            AlloyCompressor.compress(compressor.innerResourceHandler, ingredients, recipeResult);
                            compressor.setRecipeUsed(recipe);
                            changed = true;
                        }
                    } else {
                        compressor.compressingTimer = 0;
                    }
                } else {
                    compressor.compressingTimer = 0;
                }
            } else {
                compressor.compressingTimer = 0;
            }
        } else if (compressor.compressingTimer > 0) {
            compressor.compressingTimer = Mth.clamp(compressor.compressingTimer - 2, 0, compressor.compressingTotalTime);
        }

        if (wasLit != isLit) {
            changed = true;
        }

        if (changed) {
            setChanged(level, pos, state);
        }
    }

    protected static int fuelDuration(Level level, ItemResource resource) {
        FurnaceFuel fuel = level.registryAccess()
                .lookupOrThrow(Registries.ITEM)
                .getData(NeoForgeDataMaps.FURNACE_FUELS, resource.typeHolder().unwrapKey().orElseThrow());
        return fuel == null ? 0 : fuel.burnTime();
    }

    protected static void consumeFuel(ResourceHandler<ItemResource> items, ItemResource fuel) {
        try (Transaction transaction = Transaction.open(null)) {
            int extractFuel = items.extract(fuel, 1, transaction);

            boolean doCommit = extractFuel > 0;

            if (doCommit) {
                ItemStackTemplate remainder = fuel.toStack().getCraftingRemainder();

                if (remainder != null) {
                    if (items.insert(ItemResource.of(remainder), remainder.count(), transaction) == 0) {
                        doCommit = false;
                    }
                }
            }

            if (doCommit) {
                transaction.commit();
            }
        }
    }

    public void set(int index, ItemResource resource, int amount) {
        ItemStack oldStack = this.items.get(index);
        ItemStack newStack = resource.toStack(amount);
        boolean same = !resource.isEmpty() && ItemStack.isSameItemSameComponents(oldStack, newStack);
        this.innerResourceHandler.set(index, resource, amount);

        if (index == AlloyCompressor.FUEL_SLOT && !same && this.level instanceof ServerLevel serverLevel) {
            this.fuelTotalTime = fuelDuration(serverLevel, resource);
            this.fuelTimeRemaining = 0;
            this.setChanged();
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.innerResourceHandler.deserialize(input);
        this.fuelTimeRemaining = input.getIntOr("FuelTimeRemaining", (short) 0);
        this.fuelTotalTime = input.getIntOr("FuelTotalTime", (short) 0);
        this.compressingTimer = input.getIntOr("CompressingTimer", (short) 0);
        this.compressingTotalTime = input.getIntOr("CompressingTotalTime", (short) 0);
        this.recipesUsed.clear();
        this.recipesUsed.putAll(input.read("RecipesUsed", RecipeMachineBlockEntity.RECIPES_USED_CODEC).orElse(Map.of()));
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        this.innerResourceHandler.serialize(output);
        output.putInt("FuelTimeRemaining", this.fuelTimeRemaining);
        output.putInt("FuelTotalTime", this.fuelTotalTime);
        output.putInt("CompressingTimer", this.compressingTimer);
        output.putInt("CompressingTotalTime", this.compressingTotalTime);
        output.store("RecipesUsed", RecipeMachineBlockEntity.RECIPES_USED_CODEC, this.recipesUsed);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter componentGetter) {
        super.applyImplicitComponents(componentGetter);
        componentGetter.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(this.items);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        components.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(this.items));
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void awardUsedRecipes(ServerPlayer player) {
        List<RecipeHolder<?>> list = getRecipesToAward(player.level(), player.position());
        player.awardRecipes(list);

        for (RecipeHolder<?> recipeholder : list) {
            player.triggerRecipeCrafted(recipeholder, this.items);
        }

        this.recipesUsed.clear();
    }

    public List<RecipeHolder<?>> getRecipesToAward(ServerLevel level, Vec3 popVec) {
        List<RecipeHolder<?>> list = Lists.newArrayList();

        for (Reference2IntMap.Entry<ResourceKey<Recipe<?>>> entry : this.recipesUsed.reference2IntEntrySet()) {
            level.recipeAccess().byKey(entry.getKey()).ifPresent(recipe -> {
                list.add(recipe);
                RecipeMachineBlockEntity.createExperience(level, popVec, entry.getIntValue(), ((AnvilCompressingRecipe) recipe.value()).experience());
            });
        }

        return list;
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);

        if (this.level != null) {
            Containers.dropContents(this.level, pos, this.items);
        }
    }

    @Override
    public void fillStackedContents(StackedItemContents stackedItemContents) {
        this.items.forEach(stackedItemContents::accountStack);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new CompressorMenu(containerId, inventory, this, this.dataAccess);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("galacticraftlegacy.block.compressor");
    }
}
