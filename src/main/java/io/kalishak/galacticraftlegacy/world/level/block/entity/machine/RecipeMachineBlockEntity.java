/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.machine;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;

public abstract class RecipeMachineBlockEntity<I extends RecipeInput, R extends Recipe<I>> extends AbstractMachineBlockEntity implements RecipeCraftingHolder, StackedContentsCompatible {
    public static final Codec<Map<ResourceKey<Recipe<?>>, Integer>> RECIPES_USED_CODEC = Codec.unboundedMap(Recipe.KEY_CODEC, Codec.INT);
    protected final Reference2IntOpenHashMap<ResourceKey<Recipe<?>>> recipesUsed = new Reference2IntOpenHashMap<>();
    protected final RecipeManager.CachedCheck<I, R> quickCheck;

    protected RecipeMachineBlockEntity(BlockEntityType<?> entityType, BlockPos pos, BlockState state, RecipeType<R> recipeType) {
        super(entityType, pos, state);
        this.quickCheck = RecipeManager.createCheck(recipeType);
    }

    public static void createExperience(ServerLevel level, Vec3 position, int amount, float value) {
        int xpReward = Mth.floor(amount * value);
        float xpFraction = Mth.frac(amount * value);
        if (xpFraction != 0.0F && level.getRandom().nextFloat() < xpFraction) {
            xpReward++;
        }

        ExperienceOrb.award(level, position, xpReward);
    }

    public static boolean canProcess(RecipeMachineBlockEntity<?, ?> entity, int maxStackSize, ItemStack result, EnergyHandler energyHandler, int energyPerTick, int outputSlot) {
        if (energyHandler.getAmountAsInt() < energyPerTick) return false;

        ItemStack resultItemStack = entity.getItem(outputSlot);

        if (resultItemStack.isEmpty()) {
            return true;
        } else if (!ItemStack.isSameItemSameComponents(resultItemStack, result)) {
            return false;
        } else {
            int resultCount = resultItemStack.getCount() + result.count();
            int maxResultCount = Math.min(maxStackSize, result.getMaxStackSize());
            return resultCount <= maxResultCount;
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.recipesUsed.clear();
        this.recipesUsed.putAll(input.read("RecipesUsed", RECIPES_USED_CODEC).orElse(Map.of()));
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.store("RecipesUsed", RECIPES_USED_CODEC, this.recipesUsed);
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
            player.triggerRecipeCrafted(recipeholder, this.items.copyToList());
        }

        this.recipesUsed.clear();
    }

    public List<RecipeHolder<?>> getRecipesToAward(ServerLevel level, Vec3 ignored) {
        List<RecipeHolder<?>> list = Lists.newArrayList();

        for (Reference2IntMap.Entry<ResourceKey<Recipe<?>>> entry : this.recipesUsed.reference2IntEntrySet()) {
            level.recipeAccess().byKey(entry.getKey()).ifPresent(list::add);
        }

        return list;
    }

    @Override
    public void fillStackedContents(StackedItemContents stackedContents) {
        forEachResource(((resource, count) -> stackedContents.accountStack(resource.toStack(count))));
    }
}
