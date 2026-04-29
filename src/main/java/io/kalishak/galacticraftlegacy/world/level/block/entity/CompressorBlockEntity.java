/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.CompressingRecipe;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.GalacticraftRecipeType;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.input.CompressingRecipeInput;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.core.BlockPos;
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
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class CompressorBlockEntity extends NamedBlockEntity implements StackedContentsCompatible, RecipeCraftingHolder {
    protected final NonNullList<ItemStack> items = NonNullList.withSize(11, ItemStack.EMPTY);
    protected final ItemStacksResourceHandler innerResourceHandler = new ItemStacksResourceHandler(this.items);
    protected static final Codec<Map<ResourceKey<Recipe<?>>, Integer>> RECIPES_USED_CODEC = Codec.unboundedMap(Recipe.KEY_CODEC, Codec.INT);
    protected int litTimeRemaining;
    protected int litTotalTime;
    protected int cookingTimer;
    protected int cookingTotalTime;
    protected final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int dataId) {
            return switch (dataId) {
                case 0 -> {
                    if (litTotalTime > Short.MAX_VALUE) {
                        yield Mth.floor(((double) litTimeRemaining / litTotalTime) * Short.MAX_VALUE);
                    }

                    yield CompressorBlockEntity.this.litTimeRemaining;
                }
                case 1 -> Math.min(CompressorBlockEntity.this.litTotalTime, Short.MAX_VALUE);
                case 2 -> CompressorBlockEntity.this.cookingTimer;
                case 3 -> CompressorBlockEntity.this.cookingTotalTime;
                default -> 0;
            };
        }

        @Override
        public void set(int dataId, int value) {
            switch (dataId) {
                case 0 -> CompressorBlockEntity.this.litTimeRemaining = value;
                case 1 -> CompressorBlockEntity.this.litTotalTime = value;
                case 2 -> CompressorBlockEntity.this.cookingTimer = value;
                case 3 -> CompressorBlockEntity.this.cookingTotalTime = value;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };
    protected final Reference2IntOpenHashMap<ResourceKey<Recipe<?>>> recipesUsed = new Reference2IntOpenHashMap<>();
    protected final RecipeManager.CachedCheck<CompressingRecipeInput, ? extends CompressingRecipe> quickCheck;
    protected final RecipeType<? extends CompressingRecipe> recipeType;

    public CompressorBlockEntity(BlockPos blockPos, BlockState blockState, RecipeType<? extends CompressingRecipe> recipeType) {
        super(GalacticraftBlockEntityType.COMPRESSOR.get(), blockPos, blockState);
        this.quickCheck = RecipeManager.createCheck(recipeType);
        this.recipeType = recipeType;
    }

    public CompressorBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(blockPos, blockState, GalacticraftRecipeType.COMPRESSING.get());
    }

    public void set(int index, ItemResource resource, int amount) {
        this.innerResourceHandler.set(index, resource, amount);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.innerResourceHandler.deserialize(input);
        this.cookingTimer = input.getIntOr("cooking_time_spent", (short) 0);
        this.cookingTotalTime = input.getIntOr("cooking_total_time", (short) 0);
        this.litTimeRemaining = input.getIntOr("lit_time_remaining", (short) 0);
        this.litTotalTime = input.getIntOr("lit_total_time", (short) 0);
        this.recipesUsed.clear();
        this.recipesUsed.putAll(input.read("RecipesUsed", RECIPES_USED_CODEC).orElse(Map.of()));
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        this.innerResourceHandler.serialize(output);
        output.putInt("cooking_time_spent", this.cookingTimer);
        output.putInt("cooking_total_time", this.cookingTotalTime);
        output.putInt("lit_time_remaining", this.litTimeRemaining);
        output.putInt("lit_total_time", this.litTotalTime);
        output.store("RecipesUsed", RECIPES_USED_CODEC, this.recipesUsed);
    }

    public static void serverTick(ServerLevel level, BlockPos pos, BlockState state, CompressorBlockEntity compressor) {
        boolean changed = false;
        boolean isLit;
        boolean wasLit;
        if (compressor.litTimeRemaining > 0) {
            wasLit = true;
            compressor.litTimeRemaining--;
            isLit = compressor.litTimeRemaining > 0;
        } else {
            wasLit = false;
            isLit = false;
        }

        ItemStack fuel = compressor.items.get(9);
        ItemStack ingredient = compressor.items.get(0);
        NonNullList<ItemStack> ingredients = NonNullList.copyOf(compressor.items.subList(0, 9));

        boolean hasIngredients = !ingredients.isEmpty();
        boolean hasFuel = !fuel.isEmpty();

        if (isLit || hasFuel && hasIngredients) {
            if (hasIngredients) {
                ResourceHandler<ItemResource> ingredientsHandler = new ItemStacksResourceHandler(ingredients);
                CompressingRecipeInput input = new CompressingRecipeInput(3, 3, () -> ingredientsHandler);
                RecipeHolder<? extends CompressingRecipe> recipe = compressor.quickCheck.getRecipeFor(input, level).orElse(null);

                if (recipe != null) {
                    ItemStack recipeResult = recipe.value().assemble(input);
                    ItemResource resourceInResultSlot = compressor.innerResourceHandler.getResource(10);
                    int maxStackSize = compressor.innerResourceHandler.getCapacityAsInt(10, resourceInResultSlot);

                    if (!recipeResult.isEmpty() && canBurn(compressor.innerResourceHandler, maxStackSize, recipeResult)) {
                        if (!isLit) {
                            int newLitTime = compressor.getBurnDuration(level, fuel);
                            compressor.litTimeRemaining = newLitTime;
                            compressor.litTotalTime = newLitTime;

                            if (newLitTime > 0) {
                                consumeFuel(compressor.innerResourceHandler, fuel);
                                isLit = true;
                                changed = true;
                            }
                        }

                        if (isLit) {
                            compressor.cookingTimer++;

                            if (compressor.cookingTimer == compressor.cookingTotalTime) {
                                compressor.cookingTimer = 0;
                                compressor.cookingTotalTime = recipe.value().compressingTime();
                                burn(compressor.innerResourceHandler, ingredient, recipeResult);
                                compressor.setRecipeUsed(recipe);
                                changed = true;
                            }
                        } else {
                            compressor.cookingTimer = 0;
                        }
                    } else {
                        compressor.cookingTimer = 0;
                    }
                }
            } else {
                compressor.cookingTimer = 0;
            }
        } else if (compressor.cookingTimer > 0) {
            compressor.cookingTimer = Mth.clamp(compressor.cookingTimer - 2, 0, compressor.cookingTotalTime);
        }

        if (wasLit != isLit) {
            changed = true;
        }

        if (changed) {
            setChanged(level, pos, state);
        }
    }

    protected int getBurnDuration(Level level, ItemStack itemStack) {
        FurnaceFuel fuel = level.registryAccess()
                .lookupOrThrow(Registries.ITEM)
                .getData(NeoForgeDataMaps.FURNACE_FUELS, itemStack.typeHolder().unwrapKey().orElseThrow());

        return fuel == null ? 0 : fuel.burnTime();
    }

    private static void consumeFuel(ResourceHandler<ItemResource> items, ItemStack fuel) {
        try (Transaction transaction = Transaction.open(null)) {
            int extractFuel = items.extract(ItemResource.of(fuel), 1, transaction);

            if (extractFuel > 0) {
                ItemStackTemplate remainder = fuel.getItem().getCraftingRemainder(fuel);

                if (remainder != null) {
                    if (items.insert(ItemResource.of(remainder), remainder.count(), transaction) > 0) {
                        transaction.commit();
                    }
                }
            }
        }
    }

    private static boolean canBurn(ResourceHandler<ItemResource> items, int maxStackSize, ItemStack recipeResult) {
        ItemStack resultItemStack = ItemUtil.getStack(items, 10);

        if (resultItemStack.isEmpty()) {
            return true;
        } else if (!ItemStack.isSameItemSameComponents(resultItemStack, recipeResult)) {
            return false;
        } else {
            int resultCount = resultItemStack.getCount() + recipeResult.count();
            int maxResultCount = Math.min(maxStackSize, recipeResult.getMaxStackSize());
            return resultCount <= maxResultCount;
        }
    }

    private static void burn(ResourceHandler<ItemResource> items, ItemStack inputItemStack, ItemStack result) {
        try (Transaction transaction = Transaction.open(null)) {
            int extract = items.insert(ItemResource.of(result), 1, transaction);

            if (extract > 0 && items.extract(ItemResource.of(inputItemStack), 1, transaction) > 0) {
                transaction.commit();
            }
        }
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

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);

        if (this.level != null) {
            Containers.dropContents(this.level, pos, this.items);
        }
    }

    @Override
    public void setRecipeUsed(@Nullable RecipeHolder<?> recipeHolder) {

    }

    @Override
    public @Nullable RecipeHolder<?> getRecipeUsed() {
        return null;
    }

    @Override
    public void awardUsedRecipes(Player player, List<ItemStack> itemStacks) {
        RecipeCraftingHolder.super.awardUsedRecipes(player, itemStacks);
    }

    @Override
    public void fillStackedContents(StackedItemContents stackedItemContents) {

    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return null;
    }

    @Override
    protected Component getDefaultName() {
        return null;
    }
}
