/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.recipes;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.advancements.MissingGearTrigger;
import io.kalishak.galacticraftlegacy.advancements.SchematicUnlockedTrigger;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.data.recipes.builder.ElectricCookingRecipeBuilder;
import io.kalishak.galacticraftlegacy.data.recipes.builder.VehicleCraftingRecipeBuilder;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.client.data.GalacticraftBlockFamilies;
import io.kalishak.galacticraftlegacy.data.recipes.builder.CompressingRecipeBuilder;
import io.kalishak.galacticraftlegacy.data.recipes.builder.FabricatingRecipeBuilder;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.registry.SchematicVariant;
import io.kalishak.galacticraftlegacy.registry.SchematicVariants;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.item.crafting.FabricatingBookCategory;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.rocket.VehicleCraftingDataRecipe;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.rocket.VehicleCraftingDataRecipes;
import net.minecraft.advancements.predicates.DistancePredicate;
import net.minecraft.advancements.predicates.GameTypePredicate;
import net.minecraft.advancements.predicates.LocationPredicate;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.predicates.entity.PlayerPredicate;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.DistanceTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

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
    private static final List<ItemLike> METEORIC_IRON_SMELTABLE = List.of(
            GalacticraftItems.RAW_METEORIC_IRON
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
        buildCraftingRecipes();
        buildCookingRecipes();
        buildFabricatorRecipes();
        buildCompressorRecipes();
        buildHeatingOnlyRecipes();
        buildVehicleCrafting();
    }

    private void buildVehicleCrafting() {
        HolderGetter<VehicleCraftingDataRecipe> vehicleData = this.registries.lookupOrThrow(GalacticraftRegistries.Keys.VEHICLE_CRAFTING_RECIPE_DATA);
        HolderSet<Item> heavyDutyPlates = this.items.getOrThrow(GalacticraftTags.Items.PLATE_HEAVY_DUTY);

        VehicleCraftingRecipeBuilder.rocket(vehicleData, VehicleCraftingDataRecipes.TIER_1_ROCKET, GalacticraftItems.TIER_1_ROCKET)
                .unlockedBy(getHasName(GalacticraftItems.NASA_WORKBENCH), has(GalacticraftItems.NASA_WORKBENCH))
                .withIngredient(GalacticraftItems.ROCKET_NOSE_CONE)
                .withIngredients(heavyDutyPlates, 8)
                .withIngredients(GalacticraftItems.ROCKET_FIN, 2)
                .withIngredient(GalacticraftItems.ROCKET_ENGINE)
                .withIngredients(GalacticraftItems.ROCKET_FIN, 2)
                .save(this.output);
        VehicleCraftingRecipeBuilder.rocket(vehicleData, VehicleCraftingDataRecipes.MOON_BUGGY, GalacticraftItems.BUGGY)
                .hasSchematic(SchematicVariants.MOON_BUGGY)
                .withIngredients(heavyDutyPlates, 5)
                .withIngredient(GalacticraftItems.BUGGY_SEAT)
                .withIngredients(heavyDutyPlates, 6)
                .withIngredients(GalacticraftItems.BUGGY_WHEEL, 4)
                .save(this.output);
        VehicleCraftingRecipeBuilder.rocket(vehicleData, VehicleCraftingDataRecipes.TIER_2_ROCKET, GalacticraftItems.TIER_2_ROCKET)
                .hasSchematic(SchematicVariants.TIER_2_ROCKET)
                .withIngredient(GalacticraftItems.ROCKET_NOSE_CONE)
                .withIngredients(this.items.getOrThrow(GalacticraftTags.Items.PLATE_HEAVY_DUTY_2), 10)
                .withIngredient(GalacticraftItems.ROCKET_BOOSTER)
                .withIngredients(GalacticraftItems.ROCKET_FIN, 2)
                .withIngredient(GalacticraftItems.ROCKET_ENGINE)
                .withIngredient(GalacticraftItems.ROCKET_BOOSTER)
                .withIngredients(GalacticraftItems.ROCKET_FIN, 2)
                .save(this.output);
        VehicleCraftingRecipeBuilder.rocket(vehicleData, VehicleCraftingDataRecipes.CARGO_ROCKET, GalacticraftItems.CARGO_ROCKET)
                .hasSchematic(SchematicVariants.CARGO_ROCKET)
                .withIngredient(GalacticraftItems.ROCKET_NOSE_CONE)
                .withIngredients(heavyDutyPlates, 11)
                .withIngredients(GalacticraftItems.ROCKET_FIN, 2)
                .withIngredient(GalacticraftItems.ROCKET_ENGINE)
                .withIngredients(GalacticraftItems.ROCKET_FIN, 2)
                .save(this.output);
        VehicleCraftingRecipeBuilder.rocket(vehicleData, VehicleCraftingDataRecipes.TIER_3_ROCKET, GalacticraftItems.TIER_3_ROCKET)
                .hasSchematic(SchematicVariants.TIER_3_ROCKET)
                .withIngredient(GalacticraftItems.HEAVY_NOSE_CONE)
                .withIngredients(this.items.getOrThrow(GalacticraftTags.Items.PLATE_HEAVY_DUTY_3), 10)
                .withIngredient(GalacticraftItems.ROCKET_BOOSTER)
                .withIngredients(GalacticraftItems.HEAVY_FIN, 2)
                .withIngredient(GalacticraftItems.HEAVY_ROCKET_ENGINE)
                .withIngredient(GalacticraftItems.ROCKET_BOOSTER)
                .withIngredients(GalacticraftItems.HEAVY_FIN, 2)
                .save(this.output);
        VehicleCraftingRecipeBuilder.rocket(vehicleData, VehicleCraftingDataRecipes.ASTRO_MINER, GalacticraftItems.ASTRO_MINER)
                .hasSchematic(SchematicVariants.ASTRO_MINER)
                .withIngredient(heavyDutyPlates)
                .withIngredient(GalacticraftItems.ORION_DRIVE)
                .withIngredient(heavyDutyPlates)
                .withIngredient(GalacticraftItems.ORION_DRIVE)
                .withIngredient(heavyDutyPlates)
                .withIngredient(GalacticraftItems.ORION_DRIVE)
                .withIngredient(GalacticraftItems.ADVANCED_WAFER)
                .withIngredients(this.items.getOrThrow(Tags.Items.CHESTS), 2)
                .withIngredient(GalacticraftItems.ORION_DRIVE)
                .withIngredient(GalacticraftItems.ORION_DRIVE)
                .withIngredient(heavyDutyPlates)
                .withIngredient(GalacticraftItems.ORION_DRIVE)
                .withIngredient(this.items.getOrThrow(GalacticraftTags.Items.PLATE_ALUMINUM))
                .withIngredient(GalacticraftItems.STEEL_POLE)
                .withIngredient(GalacticraftItems.ADVANCED_WAFER)
                .save(this.output);
    }

    private void buildCraftingRecipes() {
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, GalacticraftItems.ROCKET_FIN)
                .define('S', GalacticraftTags.Items.PLATE_STEEL)
                .define('H', GalacticraftTags.Items.PLATE_HEAVY_DUTY)
                .pattern(" S ")
                .pattern("HSH")
                .pattern("H H")
                .unlockedBy(getHasName(GalacticraftItems.NASA_WORKBENCH), has(GalacticraftItems.NASA_WORKBENCH))
                .save(this.output);
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, GalacticraftItems.NASA_WORKBENCH)
                .define('L', Items.LEVER)
                .define('T', Items.REDSTONE_TORCH)
                .define('A', GalacticraftItems.ADVANCED_WAFER)
                .define('S', GalacticraftTags.Items.PLATE_STEEL)
                .define('C', Items.CRAFTING_TABLE)
                .pattern("SCS")
                .pattern("LAL")
                .pattern("STS")
                .unlockedBy(getHasName(GalacticraftItems.ADVANCED_WAFER), has(GalacticraftItems.ADVANCED_WAFER))
                .save(this.output);
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, GalacticraftItems.WRENCH)
                .define('S', GalacticraftTags.Items.PLATE_STEEL)
                .define('B', GalacticraftTags.Items.PLATE_BRONZE)
                .pattern("  S")
                .pattern(" B ")
                .pattern("B  ")
                .unlockedBy(getHasName(GalacticraftItems.COMPRESSOR), has(GalacticraftItems.COMPRESSOR))
                .save(this.output);

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, GalacticraftItems.SINGLE_SOLAR_MODULE, 2)
                .define('G', Tags.Items.GLASS_BLOCKS)
                .define('S', GalacticraftItems.SOLAR_WAFER)
                .define('A', GalacticraftItems.ALUMINUM_WIRE)
                .pattern("GGG")
                .pattern("SSS")
                .pattern("AAA")
                .unlockedBy(getHasName(GalacticraftItems.SOLAR_WAFER), has(GalacticraftItems.SOLAR_WAFER))
                .save(this.output);
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, GalacticraftItems.FULL_SOLAR_PANEL)
                .define('G', Tags.Items.GLASS_BLOCKS)
                .define('S', GalacticraftItems.SINGLE_SOLAR_MODULE)
                .define('A', GalacticraftItems.ALUMINUM_WIRE)
                .pattern("GGG")
                .pattern("SSS")
                .pattern("AAA")
                .unlockedBy(getHasName(GalacticraftItems.SINGLE_SOLAR_MODULE), has(GalacticraftItems.SINGLE_SOLAR_MODULE))
                .save(this.output);
        foodCanister(GalacticraftItems.CANNED_BEEF, Items.BEEF);
        foodCanister(GalacticraftItems.DEHYDRATED_APPLE, Items.APPLE);
        foodCanister(GalacticraftItems.DEHYDRATED_BEETROOT, Items.BEETROOT);
        foodCanister(GalacticraftItems.DEHYDRATED_CARROT, Items.CARROT);
        foodCanister(GalacticraftItems.DEHYDRATED_MELON, Items.MELON_SLICE);
        foodCanister(GalacticraftItems.DEHYDRATED_POTATO, Items.POTATO);
        foodCanister(GalacticraftItems.DEHYDRATED_PUMPKIN, Items.PUMPKIN_SEEDS);
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, GalacticraftItems.FREQUENCY_MODULE)
                .define('P', Items.REPEATER)
                .define('W', GalacticraftItems.BASIC_WAFER)
                .define('A', GalacticraftTags.Items.PLATE_ALUMINUM)
                .define('I', GalacticraftTags.Items.PLATE_IRON)
                .define('R', Items.REDSTONE)
                .pattern(" A ")
                .pattern("IPI")
                .pattern("RWR")
                .unlockedBy("was_in_space", MissingGearTrigger.TriggerInstance.cannotHear(this.registries.lookupOrThrow(Registries.BIOME)))
                .save(this.output);
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, GalacticraftItems.THERMAL_CONTROLLER)
                .define('V', GalacticraftItems.OXYGEN_VENT)
                .define('R', Items.REDSTONE)
                .define('S', GalacticraftTags.Items.PLATE_STEEL)
                .define('B', GalacticraftTags.Items.PLATE_BRONZE)
                .define('W', GalacticraftItems.BASIC_WAFER)
                .pattern("RVR")
                .pattern("BSB")
                .pattern("BWB")
                .unlockedBy(getHasName(GalacticraftItems.BASIC_WAFER), has(GalacticraftItems.BASIC_WAFER))
                .unlockedBy(getHasName(GalacticraftItems.OXYGEN_VENT), has(GalacticraftItems.OXYGEN_VENT))
                .save(this.output);
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, GalacticraftItems.BUGGY_WHEEL)
                .define('L', Items.LEATHER)
                .define('S', GalacticraftTags.Items.PLATE_STEEL)
                .pattern(" L ")
                .pattern("LSL")
                .pattern(" L ")
                .unlockedBy("has_schematic", hasUnlockedSchematic(SchematicVariants.MOON_BUGGY))
                .save(this.output);
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, GalacticraftItems.BUGGY_SEAT)
                .define('I', GalacticraftTags.Items.PLATE_IRON)
                .define('S', GalacticraftTags.Items.PLATE_STEEL)
                .pattern("  S")
                .pattern(" IS")
                .pattern("SSS")
                .unlockedBy("has_schematic", hasUnlockedSchematic(SchematicVariants.MOON_BUGGY))
                .save(this.output);
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, GalacticraftItems.BUGGY_STORAGE_BOX)
                .define('I', GalacticraftTags.Items.PLATE_IRON)
                .define('S', GalacticraftTags.Items.PLATE_STEEL)
                .define('C', Tags.Items.CHESTS)
                .pattern("SSS")
                .pattern("ICI")
                .pattern("SSS")
                .unlockedBy("has_schematic", hasUnlockedSchematic(SchematicVariants.MOON_BUGGY))
                .save(this.output);
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, GalacticraftItems.CANVAS)
                .define('S', Items.STICK)
                .define('T', Items.STRING)
                .pattern(" TS")
                .pattern("TTT")
                .pattern("ST ")
                .unlockedBy(getHasName(Items.STRING), has(Items.STRING))
                .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                .save(this.output);
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.DECORATIONS, GalacticraftItems.COMPACT_NASA_WORKBENCH)
                .define('#', GalacticraftItems.NASA_WORKBENCH)
                .pattern("#")
                .unlockedBy(getHasName(GalacticraftItems.NASA_WORKBENCH), has(GalacticraftItems.NASA_WORKBENCH))
                .save(this.output);

        toolSet(Items.STICK,
                GalacticraftItems.COMPRESSED_STEEL,
                GalacticraftTags.Items.PLATE_STEEL,
                GalacticraftItems.STEEL_SWORD,
                GalacticraftItems.STEEL_SPEAR,
                GalacticraftItems.STEEL_SHOVEL,
                GalacticraftItems.STEEL_PICKAXE,
                GalacticraftItems.STEEL_AXE,
                GalacticraftItems.STEEL_HOE
        );
        armorSet(GalacticraftItems.COMPRESSED_STEEL,
                GalacticraftTags.Items.PLATE_STEEL,
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

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, GalacticraftItems.DESH_STICK)
                .define('#', GalacticraftItems.DESH_INGOT)
                .pattern("#")
                .pattern("#")
                .unlockedBy(getHasName(GalacticraftItems.DESH_INGOT), has(GalacticraftItems.DESH_INGOT))
                .save(this.output);
        toolSet(GalacticraftItems.DESH_STICK,
                GalacticraftItems.DESH_INGOT,
                GalacticraftTags.Items.INGOTS_DESH,
                GalacticraftItems.DESH_SWORD,
                GalacticraftItems.DESH_SPEAR,
                GalacticraftItems.DESH_SHOVEL,
                GalacticraftItems.DESH_PICKAXE,
                GalacticraftItems.DESH_AXE,
                GalacticraftItems.DESH_HOE
        );
        armorSet(GalacticraftItems.DESH_INGOT,
                GalacticraftTags.Items.INGOTS_DESH,
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
        toolSet(Items.IRON_INGOT,
                GalacticraftItems.TITANIUM_INGOT,
                GalacticraftTags.Items.INGOTS_TITANIUM,
                GalacticraftItems.TITANIUM_SWORD,
                GalacticraftItems.TITANIUM_SPEAR,
                GalacticraftItems.TITANIUM_SHOVEL,
                GalacticraftItems.TITANIUM_PICKAXE,
                GalacticraftItems.TITANIUM_AXE,
                GalacticraftItems.TITANIUM_HOE
        );
        armorSet(GalacticraftItems.TITANIUM_INGOT,
                GalacticraftTags.Items.INGOTS_TITANIUM,
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
                .save(this.output);
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, GalacticraftItems.THERMAL_CLOTH)
                .define('W', ItemTags.WOOL)
                .define('R', Items.REDSTONE)
                .pattern(" W ")
                .pattern("WRW")
                .pattern(" W ")
                .unlockedBy("has_wool", has(ItemTags.WOOL))
                .unlockedBy(getHasName(Items.REDSTONE), has(Items.REDSTONE))
                .save(this.output);
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, GalacticraftItems.THERMAL_PADDING_HELM)
                .define('#', GalacticraftItems.THERMAL_CLOTH)
                .pattern("###")
                .pattern("# #")
                .unlockedBy(getHasName(GalacticraftItems.THERMAL_CLOTH), has(GalacticraftItems.THERMAL_CLOTH))
                .save(this.output);
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, GalacticraftItems.THERMAL_PADDING_CHESTPIECE)
                .define('#', GalacticraftItems.THERMAL_CLOTH)
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .unlockedBy(getHasName(GalacticraftItems.THERMAL_CLOTH), has(GalacticraftItems.THERMAL_CLOTH))
                .save(this.output);
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, GalacticraftItems.THERMAL_PADDING_LEGGINGS)
                .define('#', GalacticraftItems.THERMAL_CLOTH)
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .unlockedBy(getHasName(GalacticraftItems.THERMAL_CLOTH), has(GalacticraftItems.THERMAL_CLOTH))
                .save(this.output);
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, GalacticraftItems.THERMAL_PADDING_BOOTS)
                .define('#', GalacticraftItems.THERMAL_CLOTH)
                .pattern("# #")
                .pattern("# #")
                .unlockedBy(getHasName(GalacticraftItems.THERMAL_CLOTH), has(GalacticraftItems.THERMAL_CLOTH))
                .save(this.output);
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, GalacticraftItems.TIN_CANISTER, 2)
                .define('#', GalacticraftTags.Items.INGOTS_TIN)
                .pattern("# #")
                .pattern("# #")
                .pattern("###")
                .unlockedBy(getHasName(GalacticraftItems.TIN_INGOT), has(GalacticraftTags.Items.INGOTS_TIN))
                .save(this.output);
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, GalacticraftItems.STEEL_POLE, 3)
                .define('#', GalacticraftTags.Items.PLATE_STEEL)
                .pattern("#")
                .pattern("#")
                .pattern("#")
                .unlockedBy(getHasName(GalacticraftItems.COMPRESSED_STEEL), has(GalacticraftTags.Items.PLATE_STEEL))
                .save(this.output);
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, GalacticraftItems.PARACHUTE.white())
                .define('C', GalacticraftItems.CANVAS)
                .define('S', Items.STRING)
                .pattern("CCC")
                .pattern("S S")
                .pattern(" S ")
                .unlockedBy("discovered_gravity", discoveredGravityTrigger())
                .unlockedBy("has_rocket", has(GalacticraftItems.TIER_1_ROCKET))
                .save(this.output);
        colorItemWithDye(Items.DYE.asList(), GalacticraftItems.PARACHUTE.map(DeferredItem::asItem).asList(), "dye_parachute", RecipeCategory.TOOLS);
        GalacticraftBlockFamilies.getFamilies().forEach(blockFamily -> generateRecipes(blockFamily, FeatureFlags.DEFAULT_FLAGS));
    }

    private void buildCookingRecipes() {
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
        oreSmelting(METEORIC_IRON_SMELTABLE, RecipeCategory.MISC, CookingBookCategory.MISC, GalacticraftItems.METEORIC_IRON_INGOT, 0.2F, 200, "meteoric_iron_ingot");
        oreBlasting(METEORIC_IRON_SMELTABLE, RecipeCategory.MISC, CookingBookCategory.MISC, GalacticraftItems.METEORIC_IRON_INGOT, 0.2F, 100, "meteoric_iron_ingot");

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
    }

    private void buildFabricatorRecipes() {
        FabricatingRecipeBuilder.classic(GalacticraftItems.BASIC_WAFER, 1, Ingredient.of(Items.REDSTONE_TORCH), FabricatingBookCategory.BASIC)
                .unlockedBy(getHasName(GalacticraftItems.RAW_SILICON), has(GalacticraftTags.Items.RAW_MATERIALS_SILICON))
                .save(output, Constants.key(Registries.RECIPE, "basic_wafer"));
        FabricatingRecipeBuilder.classic(GalacticraftItems.ADVANCED_WAFER, 1, Ingredient.of(Items.REPEATER), FabricatingBookCategory.ADVANCED)
                .unlockedBy(getHasName(GalacticraftItems.RAW_SILICON), has(GalacticraftTags.Items.RAW_MATERIALS_SILICON))
                .save(output, Constants.key(Registries.RECIPE, "advanced_wafer"));
        FabricatingRecipeBuilder.classic(GalacticraftItems.SOLAR_WAFER, 9, Ingredient.of(Items.LAPIS_LAZULI), FabricatingBookCategory.BASIC)
                .unlockedBy(getHasName(GalacticraftItems.RAW_SILICON), has(GalacticraftTags.Items.RAW_MATERIALS_SILICON))
                .save(output, Constants.key(Registries.RECIPE, "solar_panel"));
    }

    private void buildCompressorRecipes() {
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
                .define('#', GalacticraftTags.Items.RAW_MATERIALS_IRIDIUM)
                .pattern("##")
                .unlockedBy(getHasName(GalacticraftItems.RAW_METEORIC_IRON), has(GalacticraftTags.Items.RAW_MATERIALS_IRIDIUM))
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
    }

    private void buildHeatingOnlyRecipes() {
        heating(
                Items.RAW_IRON_BLOCK,
                RecipeCategory.BUILDING_BLOCKS,
                CookingBookCategory.BLOCKS,
                Items.IRON_BLOCK,
                200,
                null
        );
        heating(
                Items.RAW_COPPER_BLOCK,
                RecipeCategory.BUILDING_BLOCKS,
                CookingBookCategory.BLOCKS,
                Items.COPPER_BLOCK.weathering().unaffected(),
                200,
                null
        );
        heating(
                Items.RAW_GOLD_BLOCK,
                RecipeCategory.BUILDING_BLOCKS,
                CookingBookCategory.BLOCKS,
                Items.GOLD_BLOCK,
                200,
                null
        );

        heating(
                GalacticraftItems.RAW_ALUMINUM_BLOCK,
                RecipeCategory.BUILDING_BLOCKS,
                CookingBookCategory.BLOCKS,
                GalacticraftItems.ALUMINUM_BLOCK,
                200,
                null
        );
        heating(
                GalacticraftItems.RAW_TIN_BLOCK,
                RecipeCategory.BUILDING_BLOCKS,
                CookingBookCategory.BLOCKS,
                GalacticraftItems.TIN_BLOCK,
                200,
                null
        );
    }

    protected void foodCanister(ItemLike cannedFood, ItemLike ingredient) {
        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.FOOD, cannedFood)
                .requires(ingredient)
                .requires(ingredient)
                .requires(GalacticraftItems.TIN_CANISTER)
                .unlockedBy(getHasName(GalacticraftItems.TIN_CANISTER), has(GalacticraftItems.TIN_CANISTER))
                .save(this.output);
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

    protected void heating(ItemLike input, RecipeCategory recipeCategory, CookingBookCategory cookingBookCategory, ItemLike result, int heatingTime, @Nullable String group) {
        UnaryOperator<ElectricCookingRecipeBuilder> toHeat = builder -> builder
                .group(group)
                .unlockedBy(getHasName(input), has(input));
        toHeat.apply(
                ElectricCookingRecipeBuilder.heating(
                        Ingredient.of(input),
                        recipeCategory,
                        cookingBookCategory,
                        result,
                        heatingTime
                )
        ).save(this.output, getHeatingRecipeName(result, input));
        toHeat.apply(
                ElectricCookingRecipeBuilder.arcHeating(
                        Ingredient.of(input),
                        recipeCategory,
                        cookingBookCategory,
                        result,
                        heatingTime
                )
        ).save(this.output, getArcHeatingRecipeName(result, input));
    }

    protected void heating(Ingredient input, RecipeCategory recipeCategory, CookingBookCategory cookingBookCategory, ItemLike result, int heatingTime, @Nullable String group) {
        input.getValues().forEach(itemHolder -> heating(itemHolder.value(), recipeCategory, cookingBookCategory, result, heatingTime, group));
    }

    protected void heating(TagKey<Item> input, RecipeCategory recipeCategory, CookingBookCategory cookingBookCategory, ItemLike result, int heatingTime, @Nullable String group) {
        heating(Ingredient.of(this.items.getOrThrow(input)), recipeCategory, cookingBookCategory, result, heatingTime, group);
    }

    protected void heating(List<ItemLike> input, RecipeCategory recipeCategory, CookingBookCategory cookingBookCategory, ItemLike result, int heatingTime, @Nullable String group) {
        input.forEach(itemHolder -> heating(itemHolder, recipeCategory, cookingBookCategory, result, heatingTime, group));
    }

    public void toolSet(ItemLike rodItem, ItemLike baseItem, TagKey<Item> ingredient, ItemLike sword, ItemLike spear, ItemLike shovel, ItemLike pickaxe, ItemLike axe, ItemLike hoe) {
        shaped(RecipeCategory.COMBAT, sword)
                .define('S', rodItem)
                .define('I', ingredient)
                .pattern("I")
                .pattern("I")
                .pattern("S")
                .unlockedBy(getHasName(baseItem), has(ingredient))
                .save(this.output);
        shaped(RecipeCategory.COMBAT, spear)
                .define('S', rodItem)
                .define('I', ingredient)
                .pattern("  I")
                .pattern(" I ")
                .pattern("S  ")
                .unlockedBy(getHasName(baseItem), has(ingredient))
                .save(this.output);
        shaped(RecipeCategory.TOOLS, shovel)
                .define('S', rodItem)
                .define('I', ingredient)
                .pattern("I")
                .pattern("S")
                .pattern("S")
                .unlockedBy(getHasName(baseItem), has(ingredient))
                .save(this.output);
        shaped(RecipeCategory.TOOLS, pickaxe)
                .define('S', rodItem)
                .define('I', ingredient)
                .pattern("III")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy(getHasName(baseItem), has(ingredient))
                .save(this.output);
        shaped(RecipeCategory.TOOLS, axe)
                .define('S', rodItem)
                .define('I', ingredient)
                .pattern("II")
                .pattern("IS")
                .pattern(" S")
                .unlockedBy(getHasName(baseItem), has(ingredient))
                .save(this.output);
        shaped(RecipeCategory.TOOLS, hoe)
                .define('S', rodItem)
                .define('I', ingredient)
                .pattern("II")
                .pattern(" S")
                .pattern(" S")
                .unlockedBy(getHasName(baseItem), has(ingredient))
                .save(this.output);
    }

    public void armorSet(ItemLike baseItem, TagKey<Item> ingredient, ItemLike helmet, ItemLike chestplate, ItemLike leggings, ItemLike boots) {
        shaped(RecipeCategory.COMBAT, helmet)
                .define('I', ingredient)
                .pattern("III")
                .pattern("I I")
                .unlockedBy(getHasName(baseItem), has(ingredient))
                .save(this.output);
        shaped(RecipeCategory.COMBAT, chestplate)
                .define('I', ingredient)
                .pattern("I I")
                .pattern("III")
                .pattern("III")
                .unlockedBy(getHasName(baseItem), has(ingredient))
                .save(this.output);
        shaped(RecipeCategory.TOOLS, leggings)
                .define('I', ingredient)
                .pattern("III")
                .pattern("I I")
                .pattern("I I")
                .unlockedBy(getHasName(baseItem), has(ingredient))
                .save(this.output);
        shaped(RecipeCategory.TOOLS, boots)
                .define('I', ingredient)
                .pattern("I I")
                .pattern("I I")
                .unlockedBy(getHasName(baseItem), has(ingredient))
                .save(this.output);
    }

    private static String conversionName(ItemLike result, String conversionName, ItemLike ingredient) {
        return getItemName(result) + "_from_" + conversionName + "_" + getItemName(ingredient);
    }

    private static ResourceKey<Recipe<?>> getHeatingRecipeName(ItemLike result, ItemLike ingredient) {
        return Constants.key(Registries.RECIPE, conversionName(result, "heating", ingredient));
    }

    private static ResourceKey<Recipe<?>> getArcHeatingRecipeName(ItemLike result, ItemLike ingredient) {
        return Constants.key(Registries.RECIPE, conversionName(result, "arc_heating", ingredient));
    }

    private Criterion<?> discoveredGravityTrigger() {
        return DistanceTrigger.TriggerInstance.fallFromHeight(
                EntityPredicate.Builder.entity().player(PlayerPredicate.Builder.player().setGameType(GameTypePredicate.SURVIVAL_LIKE).build()),
                DistancePredicate.horizontal(MinMaxBounds.Doubles.atLeast(10.0D)),
                LocationPredicate.Builder.inDimension(Level.OVERWORLD)
        );
    }

    private Criterion<?> hasUnlockedSchematic(ResourceKey<SchematicVariant> schematicVariant) {
        return SchematicUnlockedTrigger.TriggerInstance.playerUnlockedSchematic(schematicVariant);
    }

    @Override
    protected void nineBlockStorageRecipes(RecipeCategory unpackedFormCategory, ItemLike unpackedForm, RecipeCategory packedFormCategory, ItemLike packedForm) {
        nineBlockStorageRecipes(unpackedFormCategory, unpackedForm, packedFormCategory, packedForm, getItemName(packedForm), null, getSimpleRecipeName(unpackedForm), null);
    }

    private static String getItemId(ItemLike item) {
        return BuiltInRegistries.ITEM.getKey(item.asItem()).toString();
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new GalacticraftRecipeProvider(registries, output);
        }

        @Override
        public String getName() {
            return "Recipe Provider for Galacticraft Legacy";
        }
    }
}
