/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.client.data.GalacticraftBlockFamilies;
import io.kalishak.galacticraftlegacy.data.recipes.builder.CompressingRecipeBuilder;
import io.kalishak.galacticraftlegacy.data.recipes.builder.FabricatingRecipeBuilder;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.item.crafting.FabricatingBookCategory;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class GalacticraftRecipeProvider extends RecipeProvider {
    private static final List<ItemLike> ALUMINUM_SMELTABLE = List.of(
            GalacticraftItems.RAW_ALUMINUM,
            GalacticraftItems.ALUMINUM_ORE,
            GalacticraftItems.DEEPSLATE_ALUMINUM_ORE
    );
    private static final List<ItemLike> COPPER_SMELTABLE = List.of(
            GalacticraftItems.MOON_COPPER_ORE
    );
    private static final List<ItemLike> DESH_SMELTABLE = List.of(
            GalacticraftItems.RAW_DESH
    );
    private static final List<ItemLike> LEAD_SMELTABLE = List.of(
            GalacticraftItems.RAW_LEAD
    );
    private static final List<ItemLike> SILICON_SMELTABLE = List.of(
            GalacticraftItems.SILICON_ORE,
            GalacticraftItems.DEEPSLATE_SILICON_ORE
    );
    private static final List<ItemLike> STEEL_SMELTABLE = List.of(
            GalacticraftItems.RAW_STEEL
    );
    private static final List<ItemLike> TIN_SMELTABLE = List.of(
            GalacticraftItems.TIN_ORE,
            GalacticraftItems.DEEPSLATE_TIN_ORE,
            GalacticraftItems.MOON_TIN_ORE,
            GalacticraftItems.RAW_TIN
    );
    private static final List<ItemLike> TITANIUM_SMELTABLE = List.of(
            GalacticraftItems.RAW_TITANIUM
    );

    GalacticraftRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        toolSet(GalacticraftTags.Items.PLATE_STEEL,
                GalacticraftItems.STEEL_SWORD,
                GalacticraftItems.STEEL_SPEAR,
                GalacticraftItems.STEEL_SHOVEL,
                GalacticraftItems.STEEL_PICKAXE,
                GalacticraftItems.STEEL_AXE,
                GalacticraftItems.STEEL_HOE
        );
        armorSet(GalacticraftTags.Items.PLATE_STEEL,
                GalacticraftItems.STEEL_HELMET,
                GalacticraftItems.STEEL_CHESTPLATE,
                GalacticraftItems.STEEL_LEGGINGS,
                GalacticraftItems.STEEL_BOOTS
        );
        nineBlockStorageRecipes(
                RecipeCategory.MISC,
                GalacticraftItems.STEEL_NUGGET,
                RecipeCategory.MISC,
                GalacticraftItems.STEEL_INGOT,
                getItemId(GalacticraftItems.STEEL_NUGGET) + "_from_steel_ingot",
                null,
                getItemId(GalacticraftItems.STEEL_INGOT) + "_from_steel_nugget",
                null
        );
        SimpleCookingRecipeBuilder.smelting(
                Ingredient.of(GalacticraftItems.STEEL_SWORD,
                        GalacticraftItems.STEEL_SPEAR,
                        GalacticraftItems.STEEL_SHOVEL,
                        GalacticraftItems.STEEL_PICKAXE,
                        GalacticraftItems.STEEL_AXE,
                        GalacticraftItems.STEEL_HOE,
                        GalacticraftItems.STEEL_HELMET,
                        GalacticraftItems.STEEL_CHESTPLATE,
                        GalacticraftItems.STEEL_LEGGINGS,
                        GalacticraftItems.STEEL_BOOTS,
                        GalacticraftItems.STEEL_HORSE_ARMOR,
                        GalacticraftItems.STEEL_NAUTILUS_ARMOR
                ), RecipeCategory.TOOLS, CookingBookCategory.MISC, GalacticraftItems.STEEL_NUGGET, 0.1F, 200)
                .unlockedBy(getHasName(GalacticraftItems.STEEL_SWORD), has(GalacticraftItems.STEEL_SWORD))
                .unlockedBy(getHasName(GalacticraftItems.STEEL_SHOVEL), has(GalacticraftItems.STEEL_SHOVEL))
                .unlockedBy(getHasName(GalacticraftItems.STEEL_PICKAXE), has(GalacticraftItems.STEEL_PICKAXE))
                .unlockedBy(getHasName(GalacticraftItems.STEEL_AXE), has(GalacticraftItems.STEEL_AXE))
                .unlockedBy(getHasName(GalacticraftItems.STEEL_HOE), has(GalacticraftItems.STEEL_HOE))
                .unlockedBy(getHasName(GalacticraftItems.STEEL_HELMET), has(GalacticraftItems.STEEL_HELMET))
                .unlockedBy(getHasName(GalacticraftItems.STEEL_CHESTPLATE), has(GalacticraftItems.STEEL_CHESTPLATE))
                .unlockedBy(getHasName(GalacticraftItems.STEEL_LEGGINGS), has(GalacticraftItems.STEEL_LEGGINGS))
                .unlockedBy(getHasName(GalacticraftItems.STEEL_BOOTS), has(GalacticraftItems.STEEL_BOOTS))
                .unlockedBy(getHasName(GalacticraftItems.STEEL_HORSE_ARMOR), has(GalacticraftItems.STEEL_HORSE_ARMOR))
                .unlockedBy(getHasName(GalacticraftItems.STEEL_NAUTILUS_ARMOR), has(GalacticraftItems.STEEL_NAUTILUS_ARMOR))
                .group("steel")
                .save(this.output, Constants.key(Registries.RECIPE, getSmeltingRecipeName(GalacticraftItems.STEEL_NUGGET)));
        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(GalacticraftItems.STEEL_SWORD,
                                GalacticraftItems.STEEL_SPEAR,
                                GalacticraftItems.STEEL_SHOVEL,
                                GalacticraftItems.STEEL_PICKAXE,
                                GalacticraftItems.STEEL_AXE,
                                GalacticraftItems.STEEL_HOE,
                                GalacticraftItems.STEEL_HELMET,
                                GalacticraftItems.STEEL_CHESTPLATE,
                                GalacticraftItems.STEEL_LEGGINGS,
                                GalacticraftItems.STEEL_BOOTS,
                                GalacticraftItems.STEEL_HORSE_ARMOR,
                                GalacticraftItems.STEEL_NAUTILUS_ARMOR
                        ), RecipeCategory.TOOLS, CookingBookCategory.MISC, GalacticraftItems.STEEL_NUGGET, 0.1F, 100)
                .unlockedBy(getHasName(GalacticraftItems.STEEL_SWORD), has(GalacticraftItems.STEEL_SWORD))
                .unlockedBy(getHasName(GalacticraftItems.STEEL_SHOVEL), has(GalacticraftItems.STEEL_SHOVEL))
                .unlockedBy(getHasName(GalacticraftItems.STEEL_PICKAXE), has(GalacticraftItems.STEEL_PICKAXE))
                .unlockedBy(getHasName(GalacticraftItems.STEEL_AXE), has(GalacticraftItems.STEEL_AXE))
                .unlockedBy(getHasName(GalacticraftItems.STEEL_HOE), has(GalacticraftItems.STEEL_HOE))
                .unlockedBy(getHasName(GalacticraftItems.STEEL_HELMET), has(GalacticraftItems.STEEL_HELMET))
                .unlockedBy(getHasName(GalacticraftItems.STEEL_CHESTPLATE), has(GalacticraftItems.STEEL_CHESTPLATE))
                .unlockedBy(getHasName(GalacticraftItems.STEEL_LEGGINGS), has(GalacticraftItems.STEEL_LEGGINGS))
                .unlockedBy(getHasName(GalacticraftItems.STEEL_BOOTS), has(GalacticraftItems.STEEL_BOOTS))
                .unlockedBy(getHasName(GalacticraftItems.STEEL_HORSE_ARMOR), has(GalacticraftItems.STEEL_HORSE_ARMOR))
                .unlockedBy(getHasName(GalacticraftItems.STEEL_NAUTILUS_ARMOR), has(GalacticraftItems.STEEL_NAUTILUS_ARMOR))
                .group("steel")
                .save(this.output, Constants.key(Registries.RECIPE, getBlastingRecipeName(GalacticraftItems.STEEL_NUGGET)));

        toolSet(GalacticraftTags.Items.INGOTS_DESH,
                GalacticraftItems.DESH_SWORD,
                GalacticraftItems.DESH_SPEAR,
                GalacticraftItems.DESH_SHOVEL,
                GalacticraftItems.DESH_PICKAXE,
                GalacticraftItems.DESH_AXE,
                GalacticraftItems.DESH_HOE
        );
        armorSet(GalacticraftTags.Items.INGOTS_DESH,
                GalacticraftItems.DESH_HELMET,
                GalacticraftItems.DESH_CHESTPLATE,
                GalacticraftItems.DESH_LEGGINGS,
                GalacticraftItems.DESH_BOOTS
        );
        nineBlockStorageRecipes(
                RecipeCategory.MISC,
                GalacticraftItems.DESH_NUGGET,
                RecipeCategory.MISC,
                GalacticraftItems.DESH_INGOT,
                getItemId(GalacticraftItems.DESH_NUGGET) + "_from_desh_ingot",
                null,
                getItemId(GalacticraftItems.DESH_INGOT) + "_from_desh_nugget",
                null
        );
        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(GalacticraftItems.DESH_SWORD,
                                GalacticraftItems.DESH_SPEAR,
                                GalacticraftItems.DESH_SHOVEL,
                                GalacticraftItems.DESH_PICKAXE,
                                GalacticraftItems.DESH_AXE,
                                GalacticraftItems.DESH_HOE,
                                GalacticraftItems.DESH_HELMET,
                                GalacticraftItems.DESH_CHESTPLATE,
                                GalacticraftItems.DESH_LEGGINGS,
                                GalacticraftItems.DESH_BOOTS
                        ), RecipeCategory.TOOLS, CookingBookCategory.MISC, GalacticraftItems.DESH_NUGGET, 0.1F, 200)
                .unlockedBy(getHasName(GalacticraftItems.DESH_SWORD), has(GalacticraftItems.DESH_SWORD))
                .unlockedBy(getHasName(GalacticraftItems.DESH_SHOVEL), has(GalacticraftItems.DESH_SHOVEL))
                .unlockedBy(getHasName(GalacticraftItems.DESH_PICKAXE), has(GalacticraftItems.DESH_PICKAXE))
                .unlockedBy(getHasName(GalacticraftItems.DESH_AXE), has(GalacticraftItems.DESH_AXE))
                .unlockedBy(getHasName(GalacticraftItems.DESH_HOE), has(GalacticraftItems.DESH_HOE))
                .unlockedBy(getHasName(GalacticraftItems.DESH_HELMET), has(GalacticraftItems.DESH_HELMET))
                .unlockedBy(getHasName(GalacticraftItems.DESH_CHESTPLATE), has(GalacticraftItems.DESH_CHESTPLATE))
                .unlockedBy(getHasName(GalacticraftItems.DESH_LEGGINGS), has(GalacticraftItems.DESH_LEGGINGS))
                .unlockedBy(getHasName(GalacticraftItems.DESH_BOOTS), has(GalacticraftItems.DESH_BOOTS))
                .group("desh")
                .save(this.output, Constants.key(Registries.RECIPE, getSmeltingRecipeName(GalacticraftItems.DESH_NUGGET)));
        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(GalacticraftItems.DESH_SWORD,
                                GalacticraftItems.DESH_SPEAR,
                                GalacticraftItems.DESH_SHOVEL,
                                GalacticraftItems.DESH_PICKAXE,
                                GalacticraftItems.DESH_AXE,
                                GalacticraftItems.DESH_HOE,
                                GalacticraftItems.DESH_HELMET,
                                GalacticraftItems.DESH_CHESTPLATE,
                                GalacticraftItems.DESH_LEGGINGS,
                                GalacticraftItems.DESH_BOOTS
                        ), RecipeCategory.TOOLS, CookingBookCategory.MISC, GalacticraftItems.DESH_NUGGET, 0.1F, 100)
                .unlockedBy(getHasName(GalacticraftItems.DESH_SWORD), has(GalacticraftItems.DESH_SWORD))
                .unlockedBy(getHasName(GalacticraftItems.DESH_SHOVEL), has(GalacticraftItems.DESH_SHOVEL))
                .unlockedBy(getHasName(GalacticraftItems.DESH_PICKAXE), has(GalacticraftItems.DESH_PICKAXE))
                .unlockedBy(getHasName(GalacticraftItems.DESH_AXE), has(GalacticraftItems.DESH_AXE))
                .unlockedBy(getHasName(GalacticraftItems.DESH_HOE), has(GalacticraftItems.DESH_HOE))
                .unlockedBy(getHasName(GalacticraftItems.DESH_HELMET), has(GalacticraftItems.DESH_HELMET))
                .unlockedBy(getHasName(GalacticraftItems.DESH_CHESTPLATE), has(GalacticraftItems.DESH_CHESTPLATE))
                .unlockedBy(getHasName(GalacticraftItems.DESH_LEGGINGS), has(GalacticraftItems.DESH_LEGGINGS))
                .unlockedBy(getHasName(GalacticraftItems.DESH_BOOTS), has(GalacticraftItems.DESH_BOOTS))
                .group("desh")
                .save(this.output, Constants.key(Registries.RECIPE, getBlastingRecipeName(GalacticraftItems.DESH_NUGGET)));

        toolSet(GalacticraftTags.Items.INGOTS_TITANIUM,
                GalacticraftItems.TITANIUM_SWORD,
                GalacticraftItems.TITANIUM_SPEAR,
                GalacticraftItems.TITANIUM_SHOVEL,
                GalacticraftItems.TITANIUM_PICKAXE,
                GalacticraftItems.TITANIUM_AXE,
                GalacticraftItems.TITANIUM_HOE
        );
        armorSet(GalacticraftTags.Items.INGOTS_TITANIUM,
                GalacticraftItems.TITANIUM_HELMET,
                GalacticraftItems.TITANIUM_CHESTPLATE,
                GalacticraftItems.TITANIUM_LEGGINGS,
                GalacticraftItems.TITANIUM_BOOTS
        );
        nineBlockStorageRecipes(
                RecipeCategory.MISC,
                GalacticraftItems.TITANIUM_NUGGET,
                RecipeCategory.MISC,
                GalacticraftItems.TITANIUM_INGOT,
                getItemId(GalacticraftItems.TITANIUM_NUGGET) + "_from_titanium_ingot",
                null,
                getItemId(GalacticraftItems.TITANIUM_INGOT) + "_from_titanium_nugget",
                null
        );
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.DECORATIONS, GalacticraftItems.MAGNETIC_CRAFTING_TABLE)
                .define('#', Items.CRAFTING_TABLE)
                .define('I', GalacticraftTags.Items.PLATE_IRON)
                .pattern("I")
                .pattern("#")
                .unlockedBy(getHasName(Items.CRAFTING_TABLE), has(Items.CRAFTING_TABLE))
                .unlockedBy(getHasName(GalacticraftItems.COMPRESSED_IRON), has(GalacticraftTags.Items.PLATE_IRON))
                .save(this.output, Constants.key(Registries.RECIPE, getSimpleRecipeName(GalacticraftItems.MAGNETIC_CRAFTING_TABLE)));
        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(GalacticraftItems.TITANIUM_SWORD,
                                GalacticraftItems.TITANIUM_SPEAR,
                                GalacticraftItems.TITANIUM_SHOVEL,
                                GalacticraftItems.TITANIUM_PICKAXE,
                                GalacticraftItems.TITANIUM_AXE,
                                GalacticraftItems.TITANIUM_HOE,
                                GalacticraftItems.TITANIUM_HELMET,
                                GalacticraftItems.TITANIUM_CHESTPLATE,
                                GalacticraftItems.TITANIUM_LEGGINGS,
                                GalacticraftItems.TITANIUM_BOOTS
                        ), RecipeCategory.TOOLS, CookingBookCategory.MISC, GalacticraftItems.TITANIUM_NUGGET, 0.1F, 200)
                .unlockedBy(getHasName(GalacticraftItems.TITANIUM_SWORD), has(GalacticraftItems.TITANIUM_SWORD))
                .unlockedBy(getHasName(GalacticraftItems.TITANIUM_SHOVEL), has(GalacticraftItems.TITANIUM_SHOVEL))
                .unlockedBy(getHasName(GalacticraftItems.TITANIUM_PICKAXE), has(GalacticraftItems.TITANIUM_PICKAXE))
                .unlockedBy(getHasName(GalacticraftItems.TITANIUM_AXE), has(GalacticraftItems.TITANIUM_AXE))
                .unlockedBy(getHasName(GalacticraftItems.TITANIUM_HOE), has(GalacticraftItems.TITANIUM_HOE))
                .unlockedBy(getHasName(GalacticraftItems.TITANIUM_HELMET), has(GalacticraftItems.TITANIUM_HELMET))
                .unlockedBy(getHasName(GalacticraftItems.TITANIUM_CHESTPLATE), has(GalacticraftItems.TITANIUM_CHESTPLATE))
                .unlockedBy(getHasName(GalacticraftItems.TITANIUM_LEGGINGS), has(GalacticraftItems.TITANIUM_LEGGINGS))
                .unlockedBy(getHasName(GalacticraftItems.TITANIUM_BOOTS), has(GalacticraftItems.TITANIUM_BOOTS))
                .group("steel")
                .save(this.output, Constants.key(Registries.RECIPE, getSmeltingRecipeName(GalacticraftItems.TITANIUM_NUGGET)));
        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(GalacticraftItems.TITANIUM_SWORD,
                                GalacticraftItems.TITANIUM_SPEAR,
                                GalacticraftItems.TITANIUM_SHOVEL,
                                GalacticraftItems.TITANIUM_PICKAXE,
                                GalacticraftItems.TITANIUM_AXE,
                                GalacticraftItems.TITANIUM_HOE,
                                GalacticraftItems.TITANIUM_HELMET,
                                GalacticraftItems.TITANIUM_CHESTPLATE,
                                GalacticraftItems.TITANIUM_LEGGINGS,
                                GalacticraftItems.TITANIUM_BOOTS
                        ), RecipeCategory.TOOLS, CookingBookCategory.MISC, GalacticraftItems.TITANIUM_NUGGET, 0.1F, 100)
                .unlockedBy(getHasName(GalacticraftItems.TITANIUM_SWORD), has(GalacticraftItems.TITANIUM_SWORD))
                .unlockedBy(getHasName(GalacticraftItems.TITANIUM_SHOVEL), has(GalacticraftItems.TITANIUM_SHOVEL))
                .unlockedBy(getHasName(GalacticraftItems.TITANIUM_PICKAXE), has(GalacticraftItems.TITANIUM_PICKAXE))
                .unlockedBy(getHasName(GalacticraftItems.TITANIUM_AXE), has(GalacticraftItems.TITANIUM_AXE))
                .unlockedBy(getHasName(GalacticraftItems.TITANIUM_HOE), has(GalacticraftItems.TITANIUM_HOE))
                .unlockedBy(getHasName(GalacticraftItems.TITANIUM_HELMET), has(GalacticraftItems.TITANIUM_HELMET))
                .unlockedBy(getHasName(GalacticraftItems.TITANIUM_CHESTPLATE), has(GalacticraftItems.TITANIUM_CHESTPLATE))
                .unlockedBy(getHasName(GalacticraftItems.TITANIUM_LEGGINGS), has(GalacticraftItems.TITANIUM_LEGGINGS))
                .unlockedBy(getHasName(GalacticraftItems.TITANIUM_BOOTS), has(GalacticraftItems.TITANIUM_BOOTS))
                .group("steel")
                .save(this.output, Constants.key(Registries.RECIPE, getBlastingRecipeName(GalacticraftItems.TITANIUM_NUGGET)));
        oreSmelting(ALUMINUM_SMELTABLE, RecipeCategory.MISC, CookingBookCategory.MISC, GalacticraftItems.ALUMINUM_INGOT, 0.1F, 200, "aluminum_ingot");
        oreBlasting(ALUMINUM_SMELTABLE, RecipeCategory.MISC, CookingBookCategory.MISC, GalacticraftItems.ALUMINUM_INGOT, 0.1F, 100, "aluminum_ingot");
        oreSmelting(DESH_SMELTABLE, RecipeCategory.MISC, CookingBookCategory.MISC, GalacticraftItems.DESH_INGOT, 0.1F, 200, "desh_ingot");
        oreBlasting(DESH_SMELTABLE, RecipeCategory.MISC, CookingBookCategory.MISC, GalacticraftItems.DESH_INGOT, 0.1F, 100, "desh_ingot");
        oreSmelting(LEAD_SMELTABLE, RecipeCategory.MISC, CookingBookCategory.MISC, GalacticraftItems.LEAD_INGOT, 0.2F, 200, "lead_ingot");
        oreBlasting(LEAD_SMELTABLE, RecipeCategory.MISC, CookingBookCategory.MISC, GalacticraftItems.LEAD_INGOT, 0.2F, 100, "lead_ingot");
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(GalacticraftItems.RAW_STEEL), RecipeCategory.MISC, CookingBookCategory.MISC, GalacticraftItems.STEEL_INGOT, 0.1F, 200)
                .unlockedBy(getHasName(GalacticraftItems.RAW_STEEL), has(GalacticraftItems.RAW_STEEL))
                .save(this.output, Constants.key(Registries.RECIPE, getSmeltingRecipeName(GalacticraftItems.STEEL_INGOT)));
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(GalacticraftItems.RAW_STEEL), RecipeCategory.MISC, CookingBookCategory.MISC, GalacticraftItems.STEEL_INGOT, 0.1F, 100)
                .unlockedBy(getHasName(GalacticraftItems.RAW_STEEL), has(GalacticraftItems.RAW_STEEL))
                .save(this.output, Constants.key(Registries.RECIPE, getBlastingRecipeName(GalacticraftItems.STEEL_INGOT)));
        oreSmelting(SILICON_SMELTABLE, RecipeCategory.MISC, CookingBookCategory.MISC, GalacticraftItems.RAW_SILICON, 0.1F, 200, "raw_silicon");
        oreBlasting(SILICON_SMELTABLE, RecipeCategory.MISC, CookingBookCategory.MISC, GalacticraftItems.RAW_SILICON, 0.1F, 100, "raw_silicon");
        oreSmelting(TIN_SMELTABLE, RecipeCategory.MISC, CookingBookCategory.MISC, GalacticraftItems.TIN_INGOT, 0.1F, 200, "tin_ingot");
        oreBlasting(TIN_SMELTABLE, RecipeCategory.MISC, CookingBookCategory.MISC, GalacticraftItems.TIN_INGOT, 0.1F, 100, "tin_ingot");
        oreSmelting(TITANIUM_SMELTABLE, RecipeCategory.MISC, CookingBookCategory.MISC, GalacticraftItems.TITANIUM_INGOT, 0.2F, 200, "titanium_ingot");
        oreBlasting(TITANIUM_SMELTABLE, RecipeCategory.MISC, CookingBookCategory.MISC, GalacticraftItems.TITANIUM_INGOT, 0.2F, 100, "titanium_ingot");
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(GalacticraftItems.MOON_SAPPHIRE_ORE), RecipeCategory.MISC, CookingBookCategory.MISC, GalacticraftItems.SAPPHIRE, 0.1F, 200)
                .unlockedBy(getHasName(GalacticraftItems.MOON_SAPPHIRE_ORE), has(GalacticraftItems.MOON_SAPPHIRE_ORE))
                .save(this.output, Constants.key(Registries.RECIPE, getSmeltingRecipeName(GalacticraftItems.SAPPHIRE)));
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(GalacticraftItems.MOON_SAPPHIRE_ORE), RecipeCategory.MISC, CookingBookCategory.MISC, GalacticraftItems.SAPPHIRE, 0.1F, 100)
                .unlockedBy(getHasName(GalacticraftItems.MOON_SAPPHIRE_ORE), has(GalacticraftItems.MOON_SAPPHIRE_ORE))
                .save(this.output, Constants.key(Registries.RECIPE, getBlastingRecipeName(GalacticraftItems.SAPPHIRE)));
        oreSmelting(COPPER_SMELTABLE, RecipeCategory.MISC, CookingBookCategory.MISC, Items.COPPER_INGOT, 0.2F, 200, "copper_ingot");
        oreBlasting(COPPER_SMELTABLE, RecipeCategory.MISC, CookingBookCategory.MISC, Items.COPPER_INGOT, 0.2F, 100, "copper_ingot");

        parachute(GalacticraftItems.BLACK_PARACHUTE, Items.BLACK_WOOL);
        parachute(GalacticraftItems.BLUE_PARACHUTE, Items.BLUE_WOOL);
        parachute(GalacticraftItems.BROWN_PARACHUTE, Items.BROWN_WOOL);
        parachute(GalacticraftItems.CYAN_PARACHUTE, Items.CYAN_WOOL);
        parachute(GalacticraftItems.GRAY_PARACHUTE, Items.GRAY_WOOL);
        parachute(GalacticraftItems.GREEN_PARACHUTE, Items.GREEN_WOOL);
        parachute(GalacticraftItems.LIGHT_BLUE_PARACHUTE, Items.LIGHT_BLUE_WOOL);
        parachute(GalacticraftItems.LIGHT_GRAY_PARACHUTE, Items.LIGHT_GRAY_WOOL);
        parachute(GalacticraftItems.LIME_PARACHUTE, Items.LIME_WOOL);
        parachute(GalacticraftItems.MAGENTA_PARACHUTE, Items.MAGENTA_WOOL);
        parachute(GalacticraftItems.ORANGE_PARACHUTE, Items.ORANGE_WOOL);
        parachute(GalacticraftItems.PINK_PARACHUTE, Items.PINK_WOOL);
        parachute(GalacticraftItems.PURPLE_PARACHUTE, Items.PURPLE_WOOL);
        parachute(GalacticraftItems.RED_PARACHUTE, Items.RED_WOOL);
        parachute(GalacticraftItems.WHITE_PARACHUTE, Items.WHITE_WOOL);
        parachute(GalacticraftItems.YELLOW_PARACHUTE, Items.YELLOW_WOOL);

        List<Item> dyes = List.of(
                Items.BLACK_DYE,
                Items.BLUE_DYE,
                Items.BROWN_DYE,
                Items.CYAN_DYE,
                Items.GRAY_DYE,
                Items.GREEN_DYE,
                Items.LIGHT_BLUE_DYE,
                Items.LIGHT_GRAY_DYE,
                Items.LIME_DYE,
                Items.MAGENTA_DYE,
                Items.ORANGE_DYE,
                Items.PINK_DYE,
                Items.PURPLE_DYE,
                Items.RED_DYE,
                Items.YELLOW_DYE,
                Items.WHITE_DYE
        );
        List<Item> parachutes = List.of(
                GalacticraftItems.BLACK_PARACHUTE.get(),
                GalacticraftItems.BLUE_PARACHUTE.get(),
                GalacticraftItems.BROWN_PARACHUTE.get(),
                GalacticraftItems.CYAN_PARACHUTE.get(),
                GalacticraftItems.GRAY_PARACHUTE.get(),
                GalacticraftItems.GREEN_PARACHUTE.get(),
                GalacticraftItems.LIGHT_BLUE_PARACHUTE.get(),
                GalacticraftItems.LIGHT_GRAY_PARACHUTE.get(),
                GalacticraftItems.LIME_PARACHUTE.get(),
                GalacticraftItems.MAGENTA_PARACHUTE.get(),
                GalacticraftItems.ORANGE_PARACHUTE.get(),
                GalacticraftItems.PINK_PARACHUTE.get(),
                GalacticraftItems.PURPLE_PARACHUTE.get(),
                GalacticraftItems.RED_PARACHUTE.get(),
                GalacticraftItems.YELLOW_PARACHUTE.get(),
                GalacticraftItems.WHITE_PARACHUTE.get()
        );
        colorItemWithDye(dyes, parachutes, "parachute_dye", RecipeCategory.TOOLS);
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, GalacticraftItems.THERMAL_CLOTH)
                .define('W', ItemTags.WOOL)
                .define('R', Items.REDSTONE)
                .pattern(" W ")
                .pattern("WRW")
                .pattern(" W ")
                .unlockedBy("has_wool", has(ItemTags.WOOL))
                .unlockedBy(getHasName(Items.REDSTONE), has(Items.REDSTONE))
                .save(this.output, Constants.key(Registries.RECIPE, getItemName(GalacticraftItems.THERMAL_CLOTH)));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, GalacticraftItems.THERMAL_PADDING_HELM)
                .define('#', GalacticraftItems.THERMAL_CLOTH)
                .pattern("###")
                .pattern("# #")
                .unlockedBy(getHasName(GalacticraftItems.THERMAL_CLOTH), has(GalacticraftItems.THERMAL_CLOTH))
                .save(this.output, Constants.key(Registries.RECIPE, getItemName(GalacticraftItems.THERMAL_PADDING_HELM)));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, GalacticraftItems.THERMAL_PADDING_CHESTPIECE)
                .define('#', GalacticraftItems.THERMAL_CLOTH)
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .unlockedBy(getHasName(GalacticraftItems.THERMAL_CLOTH), has(GalacticraftItems.THERMAL_CLOTH))
                .save(this.output, Constants.key(Registries.RECIPE, getItemName(GalacticraftItems.THERMAL_PADDING_CHESTPIECE)));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, GalacticraftItems.THERMAL_PADDING_LEGGINGS)
                .define('#', GalacticraftItems.THERMAL_CLOTH)
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .unlockedBy(getHasName(GalacticraftItems.THERMAL_CLOTH), has(GalacticraftItems.THERMAL_CLOTH))
                .save(this.output, Constants.key(Registries.RECIPE, getItemName(GalacticraftItems.THERMAL_PADDING_LEGGINGS)));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, GalacticraftItems.THERMAL_PADDING_BOOTS)
                .define('#', GalacticraftItems.THERMAL_CLOTH)
                .pattern("# #")
                .pattern("# #")
                .unlockedBy(getHasName(GalacticraftItems.THERMAL_CLOTH), has(GalacticraftItems.THERMAL_CLOTH))
                .save(this.output, Constants.key(Registries.RECIPE, getItemName(GalacticraftItems.THERMAL_PADDING_BOOTS)));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, GalacticraftItems.TIN_CANISTER, 2)
                .define('#', GalacticraftTags.Items.INGOTS_TIN)
                .pattern("# #")
                .pattern("# #")
                .pattern("###")
                .unlockedBy(getHasName(GalacticraftItems.TIN_INGOT), has(GalacticraftTags.Items.INGOTS_TIN))
                .save(this.output, Constants.key(Registries.RECIPE, getItemName(GalacticraftItems.TIN_CANISTER)));


        FabricatingRecipeBuilder.classic(GalacticraftItems.BASIC_WAFER, 1, Ingredient.of(Items.REDSTONE_TORCH), FabricatingBookCategory.BASIC)
                .unlockedBy(getHasName(GalacticraftItems.RAW_SILICON), has(GalacticraftTags.Items.RAW_MATERIALS_SILICON))
                .save(output, Constants.key(Registries.RECIPE, "basic_wafer"));
        FabricatingRecipeBuilder.classic(GalacticraftItems.ADVANCED_WAFER, 1, Ingredient.of(Items.REPEATER), FabricatingBookCategory.ADVANCED)
                .unlockedBy(getHasName(GalacticraftItems.RAW_SILICON), has(GalacticraftTags.Items.RAW_MATERIALS_SILICON))
                .save(output, Constants.key(Registries.RECIPE, "advanced_wafer"));
        FabricatingRecipeBuilder.classic(GalacticraftItems.SOLAR_WAFER, 9, Ingredient.of(Items.LAPIS_LAZULI), FabricatingBookCategory.BASIC)
                .unlockedBy(getHasName(GalacticraftItems.RAW_SILICON), has(GalacticraftTags.Items.RAW_MATERIALS_SILICON))
                .save(output, Constants.key(Registries.RECIPE, "solar_panel"));
        compressing(GalacticraftItems.COMPRESSED_ALUMINUM, 0.1F, builder -> builder
                .define('#', GalacticraftTags.Items.INGOTS_ALUMINUM)
                .pattern("##")
                .unlockedBy(getHasName(GalacticraftItems.ALUMINUM_INGOT), has(GalacticraftTags.Items.INGOTS_ALUMINUM))
        );
        compressing(GalacticraftItems.COMPRESSED_BRONZE, 0.1F, builder -> builder
                .define('#', GalacticraftTags.Items.INGOTS_BRONZE)
                .pattern("##")
                .unlockedBy("has_bronze_ingot", has(GalacticraftTags.Items.INGOTS_BRONZE))
        );
        compressing(GalacticraftItems.COMPRESSED_BRONZE, 2, 0.1F, builder -> builder
                .define('#', GalacticraftTags.Items.PLATE_COPPER)
                .define('X', GalacticraftTags.Items.PLATE_TIN)
                .pattern("#X")
                .unlockedBy(getHasName(GalacticraftItems.COMPRESSED_COPPER), has(GalacticraftTags.Items.PLATE_COPPER))
                .unlockedBy(getHasName(GalacticraftItems.COMPRESSED_TIN), has(GalacticraftTags.Items.PLATE_TIN)),
                "_from_alloying"
        );
        compressing(GalacticraftItems.COMPRESSED_COPPER, 0.1F, builder -> builder
                .define('#', Tags.Items.INGOTS_COPPER)
                .pattern("##")
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(Tags.Items.INGOTS_COPPER))
        );
        compressing(GalacticraftItems.COMPRESSED_DESH, 0.1F, builder -> builder
                .define('#', GalacticraftTags.Items.INGOTS_DESH)
                .pattern("##")
                .unlockedBy(getHasName(GalacticraftItems.DESH_INGOT), has(GalacticraftTags.Items.INGOTS_DESH))
        );
        compressing(GalacticraftItems.COMPRESSED_IRON, 0.2F, builder -> builder
                .define('#', Tags.Items.INGOTS_IRON)
                .pattern("##")
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Tags.Items.INGOTS_IRON))
        );
        compressing(GalacticraftItems.COMPRESSED_METEORIC_IRON, 0.3F, builder -> builder
                .define('#', GalacticraftTags.Items.RAW_MATERIALS_METEORIC_IRON)
                .pattern("##")
                .unlockedBy(getHasName(GalacticraftItems.RAW_METEORIC_IRON), has(GalacticraftTags.Items.RAW_MATERIALS_METEORIC_IRON))
        );
        compressing(GalacticraftItems.COMPRESSED_TIN, 0.1F, builder -> builder
                .define('#', GalacticraftTags.Items.INGOTS_TIN)
                .pattern("##")
                .unlockedBy(getHasName(GalacticraftItems.TIN_INGOT), has(GalacticraftTags.Items.INGOTS_TIN))
        );
        compressing(GalacticraftItems.COMPRESSED_TITANIUM, 0.3F, builder -> builder
                .define('#', GalacticraftTags.Items.INGOTS_TITANIUM)
                .pattern("##")
                .unlockedBy(getHasName(GalacticraftItems.TITANIUM_INGOT), has(GalacticraftTags.Items.INGOTS_TITANIUM))
        );
        compressing(GalacticraftItems.COMPRESSED_STEEL, 0.1F, builder -> builder
                .define('#', GalacticraftTags.Items.INGOTS_STEEL)
                .pattern("##")
                .unlockedBy(getHasName(GalacticraftItems.STEEL_INGOT), has(GalacticraftTags.Items.INGOTS_STEEL))
        );
        compressing(GalacticraftItems.COMPRESSED_STEEL, 0.1F, builder -> builder
                .define('#', GalacticraftTags.Items.PLATE_IRON)
                .define('X', Items.COAL)
                .pattern("#X")
                .unlockedBy(getHasName(GalacticraftItems.COMPRESSED_IRON), has(GalacticraftTags.Items.PLATE_IRON))
                .unlockedBy(getHasName(Items.COAL), has(Items.COAL)),
                "_from_alloying"
        );
        compressing(GalacticraftItems.HEAVY_DUTY_PLATE, 2, 0.3F, builder -> builder
                .define('A', GalacticraftTags.Items.PLATE_ALUMINUM)
                .define('B', GalacticraftTags.Items.PLATE_BRONZE)
                .define('S', GalacticraftTags.Items.PLATE_STEEL)
                .pattern("SAB")
                .pattern("SAB")
                .unlockedBy(getHasName(GalacticraftItems.COMPRESSED_ALUMINUM), has(GalacticraftTags.Items.PLATE_ALUMINUM))
                .unlockedBy(getHasName(GalacticraftItems.COMPRESSED_BRONZE), has(GalacticraftTags.Items.PLATE_BRONZE))
                .unlockedBy(getHasName(GalacticraftItems.COMPRESSED_STEEL), has(GalacticraftTags.Items.PLATE_STEEL))
        );
        compressing(GalacticraftItems.HEAVY_DUTY_PLATE_TIER_2, 1, 0.3F, builder -> builder
                .define('#', GalacticraftTags.Items.PLATE_HEAVY_DUTY)
                .define('M', GalacticraftTags.Items.PLATE_METEORIC_IRON)
                .pattern("#M")
                .unlockedBy(getHasName(GalacticraftItems.HEAVY_DUTY_PLATE), has(GalacticraftTags.Items.PLATE_HEAVY_DUTY))
                .unlockedBy(getHasName(GalacticraftItems.COMPRESSED_METEORIC_IRON), has(GalacticraftTags.Items.PLATE_METEORIC_IRON))
        );
        compressing(GalacticraftItems.HEAVY_DUTY_PLATE_TIER_3, 1, 0.3F, builder -> builder
                .define('#', GalacticraftTags.Items.PLATE_HEAVY_DUTY)
                .define('D', GalacticraftTags.Items.PLATE_DESH)
                .pattern("#D")
                .unlockedBy(getHasName(GalacticraftItems.HEAVY_DUTY_PLATE_TIER_2), has(GalacticraftTags.Items.PLATE_HEAVY_DUTY_2))
                .unlockedBy(getHasName(GalacticraftItems.COMPRESSED_DESH), has(GalacticraftTags.Items.PLATE_DESH))
        );
        GalacticraftBlockFamilies.getFamilies().forEach(blockFamily -> generateRecipes(blockFamily, FeatureFlags.DEFAULT_FLAGS));
    }

    protected void compressing(ItemLike result, int count, float experience, UnaryOperator<CompressingRecipeBuilder> commonRecipeBuilder) {
        compressing(result, count, experience, commonRecipeBuilder, "");
    }

    protected void compressing(ItemLike result, float experience, UnaryOperator<CompressingRecipeBuilder> commonRecipeBuilder) {
        compressing(result, 1, experience, commonRecipeBuilder, "");
    }

    protected void compressing(ItemLike result, float experience, UnaryOperator<CompressingRecipeBuilder> commonRecipeBuilder, String suffix) {
        compressing(result, 1, experience, commonRecipeBuilder, suffix);
    }

    protected void compressing(ItemLike result, int count, float experience, UnaryOperator<CompressingRecipeBuilder> commonRecipeBuilder, String suffix) {
        commonRecipeBuilder
                .apply(CompressingRecipeBuilder.classic(this.items, RecipeCategory.MISC, result, count, 200, experience))
                .save(this.output, Constants.key(Registries.RECIPE, getItemName(result) + suffix));
        commonRecipeBuilder
                .apply(CompressingRecipeBuilder.electric(this.items, RecipeCategory.MISC, result, count, 100))
                .save(this.output, Constants.key(Registries.RECIPE, "electric_compressing_" + getItemName(result) + suffix));
    }

    @Override
    protected <T extends AbstractCookingRecipe> void oreCooking(AbstractCookingRecipe.Factory<T> recipeFactory, List<ItemLike> smeltables, RecipeCategory craftingCategory, CookingBookCategory cookingCategory, ItemLike result, float experience, int cookingTime, String group, String fromDesc) {
        for(ItemLike itemlike : smeltables) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), craftingCategory, cookingCategory, result, experience, cookingTime, recipeFactory).group(group).unlockedBy(getHasName(itemlike), this.has(itemlike)).save(this.output, Constants.key(Registries.RECIPE, getItemName(result) + fromDesc + "_" + getItemName(itemlike)));
        }
    }

    public void toolSet(TagKey<Item> ingredient, ItemLike sword, ItemLike spear, ItemLike shovel, ItemLike pickaxe, ItemLike axe, ItemLike hoe) {
        shaped(RecipeCategory.COMBAT, sword)
                .define('S', Items.STICK)
                .define('I', ingredient)
                .pattern("I")
                .pattern("I")
                .pattern("S")
                .unlockedBy(getHasName(GalacticraftItems.STEEL_INGOT), has(ingredient))
                .save(this.output, Constants.key(Registries.RECIPE, getItemName(sword)));
        shaped(RecipeCategory.COMBAT, spear)
                .define('S', Items.STICK)
                .define('I', ingredient)
                .pattern("  I")
                .pattern(" I ")
                .pattern("S  ")
                .unlockedBy(getHasName(GalacticraftItems.STEEL_INGOT), has(ingredient))
                .save(this.output, Constants.key(Registries.RECIPE, getItemName(spear)));
        shaped(RecipeCategory.TOOLS, shovel)
                .define('S', Items.STICK)
                .define('I', ingredient)
                .pattern("I")
                .pattern("S")
                .pattern("S")
                .unlockedBy(getHasName(GalacticraftItems.STEEL_INGOT), has(ingredient))
                .save(this.output, Constants.key(Registries.RECIPE, getItemName(shovel)));
        shaped(RecipeCategory.TOOLS, pickaxe)
                .define('S', Items.STICK)
                .define('I', ingredient)
                .pattern("III")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy(getHasName(GalacticraftItems.STEEL_INGOT), has(ingredient))
                .save(this.output, Constants.key(Registries.RECIPE, getItemName(pickaxe)));
        shaped(RecipeCategory.TOOLS, axe)
                .define('S', Items.STICK)
                .define('I', ingredient)
                .pattern("II")
                .pattern("IS")
                .pattern(" S")
                .unlockedBy(getHasName(GalacticraftItems.STEEL_INGOT), has(ingredient))
                .save(this.output, Constants.key(Registries.RECIPE, getItemName(axe)));
        shaped(RecipeCategory.TOOLS, hoe)
                .define('S', Items.STICK)
                .define('I', ingredient)
                .pattern("II")
                .pattern(" S")
                .pattern(" S")
                .unlockedBy(getHasName(GalacticraftItems.STEEL_INGOT), has(ingredient))
                .save(this.output, Constants.key(Registries.RECIPE, getItemName(hoe)));
    }
    public void armorSet(TagKey<Item> ingredient, ItemLike helmet, ItemLike chestplate, ItemLike leggings, ItemLike boots) {
        shaped(RecipeCategory.COMBAT, helmet)
                .define('I', ingredient)
                .pattern("III")
                .pattern("I I")
                .unlockedBy(getHasName(GalacticraftItems.STEEL_INGOT), has(ingredient))
                .save(this.output, Constants.key(Registries.RECIPE, getItemName(helmet)));
        shaped(RecipeCategory.COMBAT, chestplate)
                .define('I', ingredient)
                .pattern("I I")
                .pattern("III")
                .pattern("III")
                .unlockedBy(getHasName(GalacticraftItems.STEEL_INGOT), has(ingredient))
                .save(this.output, Constants.key(Registries.RECIPE, getItemName(chestplate)));
        shaped(RecipeCategory.TOOLS, leggings)
                .define('I', ingredient)
                .pattern("III")
                .pattern("I I")
                .pattern("I I")
                .unlockedBy(getHasName(GalacticraftItems.STEEL_INGOT), has(ingredient))
                .save(this.output, Constants.key(Registries.RECIPE, getItemName(leggings)));
        shaped(RecipeCategory.TOOLS, boots)
                .define('I', ingredient)
                .pattern("I I")
                .pattern("I I")
                .unlockedBy(getHasName(GalacticraftItems.STEEL_INGOT), has(ingredient))
                .save(this.output, Constants.key(Registries.RECIPE, getItemName(boots)));
    }

    private String conversionName(ItemLike result, String conversionName, ItemLike ingredient) {
        return getItemName(result) + "_from_" + conversionName + "_" + getItemName(ingredient);
    }

    private static String getHeatingRecipeName(ItemLike result, ItemLike ingredient) {
        return getItemName(result) + "heating" + getItemName(ingredient);
    }

    private static String getArcHeatingRecipeName(ItemLike result, ItemLike ingredient) {
        return getItemName(result) + "arc_heating" + getItemName(ingredient);
    }

    @Override
    protected void colorWithDye(List<Item> dyes, List<Item> dyedItems, @Nullable Item uncoloredItem, String groupName, RecipeCategory category) {
        for (int i = 0; i < dyes.size(); i++) {
            Item item = dyes.get(i);
            Item item1 = dyedItems.get(i);
            Stream<Item> stream = dyedItems.stream().filter(p_288265_ -> !p_288265_.equals(item1));
            if (uncoloredItem != null) {
                stream = Stream.concat(stream, Stream.of(uncoloredItem));
            }

            this.shapeless(category, item1)
                    .requires(item)
                    .requires(Ingredient.of(stream))
                    .group(groupName)
                    .unlockedBy("has_needed_dye", this.has(item))
                    .save(this.output, Constants.key(Registries.RECIPE, "dye_" + getItemName(item1)));
        }
    }

    private void parachute(ItemLike parachute, ItemLike woolItem) {
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, parachute)
                .group("parachute")
                .define('#', woolItem)
                .define('S', Items.STRING)
                .pattern("###")
                .pattern("S S")
                .pattern(" S ")
                .unlockedBy("discovered_gravity", has(Items.WHITE_WOOL))
                .save(this.output, Constants.key(Registries.RECIPE, getItemName(parachute)));
    }

    @Override
    protected void nineBlockStorageRecipes(RecipeCategory unpackedFormCategory, ItemLike unpackedForm, RecipeCategory packedFormCategory, ItemLike packedForm) {
        nineBlockStorageRecipes(unpackedFormCategory, unpackedForm, packedFormCategory, packedForm, getItemName(packedForm), null, getSimpleRecipeName(unpackedForm), null);
    }

    private static String getItemId(ItemLike item) {
        return BuiltInRegistries.ITEM.getKey(item.asItem()).toString();
    }

    public static class Runner extends RecipeProvider.Runner {
        Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new  GalacticraftRecipeProvider(registries, output);
        }

        @Override
        public String getName() {
            return "Recipe Provider for Galacticraft Legacy";
        }
    }
}
