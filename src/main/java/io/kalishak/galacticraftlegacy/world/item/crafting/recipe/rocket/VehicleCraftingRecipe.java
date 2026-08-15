/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.crafting.recipe.rocket;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.transfer.ResourcefulHelper;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.item.crafting.GalacticraftRecipeBookCategories;
import io.kalishak.galacticraftlegacy.world.item.crafting.VehicleCraftingBookCategory;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.GalacticraftRecipeSerializer;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.GalacticraftRecipeType;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.input.ResourceHandlerInput;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class VehicleCraftingRecipe implements Recipe<ResourceHandlerInput> {
    public static final MapCodec<VehicleCraftingRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            CommonInfo.MAP_CODEC.forGetter(recipe -> recipe.commonInfo),
            VehicleCraftingDataRecipe.KEY_CODEC.fieldOf("recipe_holder").forGetter(recipe -> recipe.dataRecipeKey),
            VehicleCraftingBookInfo.MAP_CODEC.forGetter(recipe -> recipe.bookInfo),
            Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(recipe -> recipe.ingredients),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
    ).apply(instance, VehicleCraftingRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, VehicleCraftingRecipe> STREAM_CODEC = StreamCodec.composite(
            CommonInfo.STREAM_CODEC, recipe -> recipe.commonInfo,
            ResourceKey.streamCodec(GalacticraftRegistries.Keys.VEHICLE_CRAFTING_RECIPE_DATA), recipe -> recipe.dataRecipeKey,
            VehicleCraftingBookInfo.STREAM_CODEC, recipe -> recipe.bookInfo,
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), recipe -> recipe.ingredients,
            ItemStackTemplate.STREAM_CODEC, recipe -> recipe.result,
            VehicleCraftingRecipe::new
    );

    private final Recipe.CommonInfo commonInfo;
    private final ResourceKey<VehicleCraftingDataRecipe> dataRecipeKey;
    private final VehicleCraftingBookInfo bookInfo;
    private final List<Ingredient> ingredients;
    private final ItemStackTemplate result;
    private @Nullable PlacementInfo placementInfo;
    private @Nullable VehicleCraftingDataRecipe dataRecipe;

    public VehicleCraftingRecipe(Recipe.CommonInfo commonInfo, ResourceKey<VehicleCraftingDataRecipe> dataRecipeKey, VehicleCraftingBookInfo bookInfo, List<Ingredient> ingredients, ItemStackTemplate result) {
        this.commonInfo = commonInfo;
        this.dataRecipeKey = dataRecipeKey;
        this.bookInfo = bookInfo;
        this.ingredients = ingredients;
        this.result = result;
    }

    private VehicleCraftingDataRecipe getDataRecipe(Level level) {
        if (this.dataRecipe == null) {
            this.dataRecipe = level.registryAccess().lookupOrThrow(GalacticraftRegistries.Keys.VEHICLE_CRAFTING_RECIPE_DATA).getValueOrThrow(this.dataRecipeKey);
        }

        return this.dataRecipe;
    }

    @Override
    public boolean matches(ResourceHandlerInput resourceHandlerInput, Level level) {
        for (int i = 0; i < resourceHandlerInput.size(); i++) {
            VehicleCraftingEntry entry = getDataRecipe(level).inputSlots().get(i);
            ItemStack stack = resourceHandlerInput.getItem(i);

            if (!entry.slotType().value().acceptedItems().contains(stack.typeHolder()) || !this.ingredients.get(i).test(stack)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack assemble(ResourceHandlerInput resourceHandlerInput) {
        int storage = ResourcefulHelper.asList(resourceHandlerInput.delegate(), ItemUtil::getStack).stream()
                .filter(stack -> stack.is(GalacticraftTags.Items.VEHICLE_INGREDIENT_STORAGE))
                .mapToInt(_ -> 1)
                .sum();
        DataComponentPatch.Builder builder = DataComponentPatch.builder();

        if (storage > 0) {
            builder = builder.set(GalacticraftDataComponents.VEHICLE_STORAGE.get(), storage);
        }

        return this.result.apply(builder.build());
    }

    @Override
    public boolean showNotification() {
        return this.commonInfo.showNotification();
    }

    @Override
    public String group() {
        return this.bookInfo.group;
    }

    @Override
    public RecipeSerializer<VehicleCraftingRecipe> getSerializer() {
        return GalacticraftRecipeSerializer.VEHICLE_CRAFTING.get();
    }

    @Override
    public RecipeType<VehicleCraftingRecipe> getType() {
        return GalacticraftRecipeType.VEHICLE_CRAFTING.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        if (this.placementInfo == null) {
            this.placementInfo = PlacementInfo.create(this.ingredients);
        }

        return this.placementInfo;
    }

    @Override
    public List<RecipeDisplay> display() {
        return Recipe.super.display();
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return switch (this.bookInfo.category) {
            case MISC -> GalacticraftRecipeBookCategories.CRAFTING_MISC_VEHICLES.get();
            case ROCKET -> GalacticraftRecipeBookCategories.CRAFTING_ROCKET.get();
            case LAND_VEHICLE -> GalacticraftRecipeBookCategories.CRAFTING_LAND_VEHICLE.get();
            case FLOATING_VEHICLE -> GalacticraftRecipeBookCategories.CRAFTING_FLOATING_VEHICLES.get();
        };
    }

    public record VehicleCraftingBookInfo(VehicleCraftingBookCategory category, String group) implements Recipe.BookInfo<VehicleCraftingBookCategory> {
        public static final MapCodec<VehicleCraftingBookInfo> MAP_CODEC = BookInfo.mapCodec(VehicleCraftingBookCategory.CODEC, VehicleCraftingBookCategory.MISC, VehicleCraftingBookInfo::new);
        public static final StreamCodec<RegistryFriendlyByteBuf, VehicleCraftingBookInfo> STREAM_CODEC = BookInfo.streamCodec(VehicleCraftingBookCategory.STREAM_CODEC, VehicleCraftingBookInfo::new);
    }
}
