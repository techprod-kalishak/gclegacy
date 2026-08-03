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
import io.kalishak.galacticraftlegacy.world.level.block.entity.BaseItemStorageBlockEntity;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
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
import net.neoforged.neoforge.transfer.RangedResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class CompressorBlockEntity extends BaseItemStorageBlockEntity implements AlloyCompressor {
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
                    if (CompressorBlockEntity.this.fuelTotalTime > Short.MAX_VALUE) {
                        yield Mth.floor(((double) CompressorBlockEntity.this.fuelTimeRemaining / CompressorBlockEntity.this.fuelTotalTime) * Short.MAX_VALUE);
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
    protected final RecipeManager.CachedCheck<CraftingInput, AnvilCompressingRecipe> quickCheck = RecipeManager.createCheck(GalacticraftRecipeType.COMPRESSING.get());

    public CompressorBlockEntity(BlockPos pos, BlockState blockState) {
        super(GalacticraftBlockEntityType.COMPRESSOR.get(), pos, blockState);
    }

    public static void registerItemCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.Item.BLOCK,
                GalacticraftBlockEntityType.COMPRESSOR.get(),
                (entity, side) -> switch (side) {
                    case UP -> RangedResourceHandler.of(() -> entity.items, 0, 9); // Crafting grid
                    case DOWN -> RangedResourceHandler.ofSingleIndex(() -> entity.items, 10); // Output
                    case null -> entity.items;
                    default -> RangedResourceHandler.ofSingleIndex(() -> entity.items, 9); // Fuel
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

        ItemStack fuel = compressor.getItem(AlloyCompressor.FUEL_SLOT);

        boolean hasFuel = !fuel.isEmpty();

        if (isLit || hasFuel) {
            CraftingInput input = CraftingInput.of(3, 3, compressor.items.copyToList().subList(CRAFTING_SLOT_START, FUEL_SLOT));
            RecipeHolder<AnvilCompressingRecipe> recipe = compressor.quickCheck.getRecipeFor(input, level).orElse(null);
            boolean hasIngredients = recipe != null;

            if (hasIngredients) {
                ItemStack recipeResult = recipe.value().assemble(input);
                ItemStack inResultSlot = compressor.getItem(AlloyCompressor.RESULT_SLOT_START);
                int maxStackSize = compressor.items.getCapacityAsInt(RESULT_SLOT_START, ItemResource.of(inResultSlot));

                if (!recipeResult.isEmpty() && AlloyCompressor.canCompress(compressor.items, maxStackSize, recipeResult)) {
                    if (!isLit) {
                        int newLitTime = fuelDuration(level, fuel);
                        compressor.fuelTimeRemaining = newLitTime;
                        compressor.fuelTotalTime = newLitTime;

                        if (newLitTime > 0) {
                            consumeFuel(compressor.items, fuel);
                            isLit = true;
                            changed = true;
                        }
                    }

                    if (isLit) {
                        compressor.compressingTimer++;

                        if (compressor.compressingTimer % 40 == 0) {
                            level.playSound(null, pos, SoundEvents.ANVIL_FALL, SoundSource.BLOCKS, 0.3F, level.getRandom().nextFloat() * 0.1F + 0.9F);
                        }

                        if (compressor.compressingTimer == compressor.compressingTotalTime) {
                            compressor.compressingTimer = 0;
                            compressor.compressingTotalTime = recipe.value().compressingTime();
                            AlloyCompressor.compress(compressor.items, recipeResult);
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

    public static int signalFromCompression(CompressorBlockEntity compressor) {
        return compressor.compressingTimer == compressor.compressingTotalTime ? 13 : 0;
    }

    protected static int fuelDuration(Level level, ItemStack stack) {
        FurnaceFuel fuel = level.registryAccess()
                .lookupOrThrow(Registries.ITEM)
                .getData(NeoForgeDataMaps.FURNACE_FUELS, stack.typeHolder().unwrapKey().orElseThrow());
        return fuel == null ? 0 : fuel.burnTime();
    }

    protected static void consumeFuel(ResourceHandler<ItemResource> items, ItemStack fuel) {
        ItemStackTemplate remainder = fuel.getCraftingRemainder();

        try (Transaction tx = Transaction.open(null)) {
            if (items.extract(ItemResource.of(fuel), 1, tx) <= 0) {
                return;
            }

            if (remainder != null && items.getResource(FUEL_SLOT).isEmpty()) {
                if (items.insert(ItemResource.of(remainder), remainder.count(), tx) > 0) {
                    tx.commit();
                }
            }
        }
    }

    @Override
    public int getSize() {
        return AlloyCompressor.INVENTORY_SIZE_BASIC;
    }

    @Override
    protected Component getDefaultName() {
        return getBlockState().getBlock().getName();
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        ItemStack currentStack = this.getItem(slot);
        boolean same = !itemStack.isEmpty() && ItemStack.isSameItemSameComponents(currentStack, itemStack);
        super.setItem(slot, itemStack);

        if (slot == AlloyCompressor.FUEL_SLOT && !same && this.level instanceof ServerLevel serverLevel) {
            this.fuelTotalTime = fuelDuration(serverLevel, itemStack);
            this.fuelTimeRemaining = 0;
            this.setChanged();
        }
    }

    @Override
    public List<ItemStack> getCraftingItems() {
        return this.items.copyToList().subList(CRAFTING_SLOT_START, FUEL_SLOT);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
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
        output.putInt("FuelTimeRemaining", this.fuelTimeRemaining);
        output.putInt("FuelTotalTime", this.fuelTotalTime);
        output.putInt("CompressingTimer", this.compressingTimer);
        output.putInt("CompressingTotalTime", this.compressingTotalTime);
        output.store("RecipesUsed", RecipeMachineBlockEntity.RECIPES_USED_CODEC, this.recipesUsed);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void setRecipeUsed(@Nullable RecipeHolder<?> recipeUsed) {
        if (recipeUsed != null) {
            ResourceKey<Recipe<?>> id = recipeUsed.id();
            this.recipesUsed.addTo(id, 1);
        }
    }

    public void awardUsedRecipes(ServerPlayer player) {
        List<RecipeHolder<?>> list = getRecipesToAward(player.level(), player.position());
        player.awardRecipes(list);

        for (RecipeHolder<?> recipeholder : list) {
            player.triggerRecipeCrafted(recipeholder, this.items.copyToList());
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
    public void fillStackedContents(StackedItemContents contents) {
        forEachResource((resource, count) -> contents.accountStack(resource.toStack(count)));
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new CompressorMenu(containerId, inventory, this, this.dataAccess);
    }

    @Override
    public int[] getSlotsForOutput() {
        return new int[] { AlloyCompressor.RESULT_SLOT_START };
    }
}
