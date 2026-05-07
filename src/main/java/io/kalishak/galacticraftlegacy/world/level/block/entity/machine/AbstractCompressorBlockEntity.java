/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.machine;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.CompressingRecipe;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.input.CompressingRecipeInput;
import io.kalishak.galacticraftlegacy.world.level.block.entity.NamedBlockEntity;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

import java.util.Map;

public abstract class AbstractCompressorBlockEntity extends NamedBlockEntity implements StackedContentsCompatible, RecipeCraftingHolder {
    public static final int CRAFTING_SLOT_START = 0;
    public static final int CRAFTING_SLOT_END = 8;
    public static final int RESULT_SLOT_START = 9;
    public static final int RESULT_SLOT_END = 10;
    public static final int FUEL_SLOT = 10;
    public static final int DATA_SLOT_COMPRESSING_TIMER = 0;
    public static final int DATA_SLOT_COMPRESSING_TIME_TOTAL = 1;
    public static final int DATA_SLOT_LIT_TIMER = 2;
    public static final int DATA_SLOT_LIT_TIME_TOTAL = 3;
    protected static final Codec<Map<ResourceKey<Recipe<?>>, Integer>> RECIPES_USED_CODEC = Codec.unboundedMap(Recipe.KEY_CODEC, Codec.INT);
    protected final NonNullList<ItemStack> items = NonNullList.withSize(11, ItemStack.EMPTY);
    protected final ItemStacksResourceHandler innerResourceHandler = new ItemStacksResourceHandler(this.items);
    protected int compressingTimer;
    protected int compressingTotalTime;
    protected final Reference2IntOpenHashMap<ResourceKey<Recipe<?>>> recipesUsed = new Reference2IntOpenHashMap<>();
    protected final RecipeManager.CachedCheck<CompressingRecipeInput, ? extends CompressingRecipe> quickCheck;
    protected final RecipeType<? extends CompressingRecipe> recipeType;

    public AbstractCompressorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, RecipeType<? extends CompressingRecipe> recipeType) {
        super(type, pos, blockState);
        this.quickCheck = RecipeManager.createCheck(recipeType);
        this.recipeType = recipeType;
    }

    public void set(int index, ItemResource resource, int amount) {
        this.innerResourceHandler.set(index, resource, amount);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.innerResourceHandler.deserialize(input);
        this.compressingTimer = input.getIntOr("CompressingTimer", (short) 0);
        this.compressingTotalTime = input.getIntOr("CompressingTotalTime", (short) 0);
        this.recipesUsed.clear();
        this.recipesUsed.putAll(input.read("RecipesUsed", RECIPES_USED_CODEC).orElse(Map.of()));
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        this.innerResourceHandler.serialize(output);
        output.putInt("CompressingTimer", this.compressingTimer);
        output.putInt("CompressingTotalTime", this.compressingTotalTime);
        output.store("RecipesUsed", RECIPES_USED_CODEC, this.recipesUsed);
    }

    protected static boolean canCompress(ResourceHandler<ItemResource> items, int maxStackSize, ItemStack recipeResult) {
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

    protected static void compress(ResourceHandler<ItemResource> items, NonNullList<ItemStack> ingredients, ItemStack result) {
        try (Transaction transaction = Transaction.open(null)) {
            boolean produce = true;

            for (ItemStack itemStack : ingredients) {
                if (!itemStack.isEmpty() && items.extract(ItemResource.of(itemStack), 1, transaction) == 0) {
                    produce = false;
                    break;
                }
            }

            if (produce && items.insert(ItemResource.of(result), 1, transaction) > 0) {
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
    public void fillStackedContents(StackedItemContents stackedItemContents) {
        this.items.forEach(stackedItemContents::accountStack);
    }
}
