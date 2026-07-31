/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.crafting.recipe.rocket;

import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;

import java.util.function.Function;

import static io.kalishak.galacticraftlegacy.world.item.crafting.recipe.rocket.VehicleCraftingSlotTypes.*;

public class VehicleCraftingDataRecipes {
    public static final ResourceKey<VehicleCraftingDataRecipe> TIER_1_ROCKET = key("tier_1_rocket");
    public static final ResourceKey<VehicleCraftingDataRecipe> MOON_BUGGY = key("moon_buggy");
    public static final ResourceKey<VehicleCraftingDataRecipe> TIER_2_ROCKET = key("tier_2_rocket");
    public static final ResourceKey<VehicleCraftingDataRecipe> CARGO_ROCKET = key("cargo_rocket");
    public static final ResourceKey<VehicleCraftingDataRecipe> TIER_3_ROCKET = key("tier_3_rocket");
    public static final ResourceKey<VehicleCraftingDataRecipe> ASTRO_MINER = key("astro_miner");

    public static void bootstrap(BootstrapContext<VehicleCraftingDataRecipe> cxt) {
        HolderGetter<VehicleCraftingSlotType> slotTypes = cxt.lookup(GalacticraftRegistries.Keys.VEHICLE_CRAFTING_SLOT_TYPE);
        HolderGetter<Item> items = cxt.lookup(Registries.ITEM);
        HolderSet<Item> chests = items.getOrThrow(Tags.Items.CHESTS);
        HolderSet<Item> heavyDutyPlates = items.getOrThrow(GalacticraftTags.Items.PLATE_HEAVY_DUTY);
        HolderSet<Item> heavyDutyPlatesT2 = items.getOrThrow(GalacticraftTags.Items.PLATE_HEAVY_DUTY_2);
        HolderSet<Item> heavyDutyPlatesT3 = items.getOrThrow(GalacticraftTags.Items.PLATE_HEAVY_DUTY_3);

        cxt.register(
                TIER_1_ROCKET,
                createDefault(slotTypes, items)
        );
        cxt.register(
                MOON_BUGGY,
                VehicleCraftingDataRecipe.Builder.builder(slotTypes)
                        .inputSlot(PLATING, 39, 41, Ingredient.of(heavyDutyPlates))
                        .inputSlot(PLATING, 57, 41, Ingredient.of(heavyDutyPlates))
                        .inputSlot(PLATING, 75, 41, Ingredient.of(heavyDutyPlates))
                        .inputSlot(PLATING, 39, 59, Ingredient.of(heavyDutyPlates))
                        .inputSlot(PLATING, 57, 59, Ingredient.of(heavyDutyPlates))
                        .inputSlot(BUGGY_SEAT, 75, 59, GalacticraftItems.BUGGY_SEAT)
                        .inputSlot(PLATING, 39, 77, Ingredient.of(heavyDutyPlates))
                        .inputSlot(PLATING, 57, 77, Ingredient.of(heavyDutyPlates))
                        .inputSlot(PLATING, 75, 77, Ingredient.of(heavyDutyPlates))
                        .inputSlot(PLATING, 39, 95, Ingredient.of(heavyDutyPlates))
                        .inputSlot(PLATING, 57, 95, Ingredient.of(heavyDutyPlates))
                        .inputSlot(PLATING, 75, 95, Ingredient.of(heavyDutyPlates))
                        .inputSlot(BUGGY_WHEEL, 21, 41, GalacticraftItems.BUGGY_WHEEL)
                        .inputSlot(BUGGY_WHEEL, 93, 41, GalacticraftItems.BUGGY_WHEEL)
                        .inputSlot(BUGGY_WHEEL, 21, 95, GalacticraftItems.BUGGY_WHEEL)
                        .inputSlot(BUGGY_WHEEL, 93, 95, GalacticraftItems.BUGGY_WHEEL)
                        .inputSlot(STORAGE, 93, 12, GalacticraftItems.BUGGY_STORAGE_BOX)
                        .inputSlot(STORAGE, 119, 12, GalacticraftItems.BUGGY_STORAGE_BOX)
                        .inputSlot(STORAGE, 145, 12, GalacticraftItems.BUGGY_STORAGE_BOX)
                        .outputSlot(142, 108, GalacticraftItems.BUGGY)
                        .build()
        );
        cxt.register(
                TIER_2_ROCKET,
                VehicleCraftingDataRecipe.Builder.builder(slotTypes)
                        .inputSlot(ROCKET_NOSE_CONE, 48, 19, GalacticraftItems.ROCKET_NOSE_CONE)
                        .inputSlot(PLATING, 39, 37, Ingredient.of(heavyDutyPlatesT2))
                        .inputSlot(PLATING, 39, 55, Ingredient.of(heavyDutyPlatesT2))
                        .inputSlot(PLATING, 39, 73, Ingredient.of(heavyDutyPlatesT2))
                        .inputSlot(PLATING, 39, 91, Ingredient.of(heavyDutyPlatesT2))
                        .inputSlot(PLATING, 39, 109, Ingredient.of(heavyDutyPlatesT2))
                        .inputSlot(PLATING, 57, 37, Ingredient.of(heavyDutyPlatesT2))
                        .inputSlot(PLATING, 57, 55, Ingredient.of(heavyDutyPlatesT2))
                        .inputSlot(PLATING, 57, 73, Ingredient.of(heavyDutyPlatesT2))
                        .inputSlot(PLATING, 57, 91, Ingredient.of(heavyDutyPlatesT2))
                        .inputSlot(PLATING, 57, 109, Ingredient.of(heavyDutyPlatesT2))
                        .inputSlot(ROCKET_BOOSTER, 21, 91, GalacticraftItems.ROCKET_BOOSTER)
                        .inputSlot(ROCKET_FIN, 21, 109, GalacticraftItems.ROCKET_FIN)
                        .inputSlot(ROCKET_FIN, 21, 127, GalacticraftItems.ROCKET_FIN)
                        .inputSlot(ROCKET_ENGINE, 48, 127, GalacticraftItems.ROCKET_ENGINE)
                        .inputSlot(ROCKET_BOOSTER, 75, 91, GalacticraftItems.ROCKET_BOOSTER)
                        .inputSlot(ROCKET_FIN, 75, 109, GalacticraftItems.ROCKET_FIN)
                        .inputSlot(ROCKET_FIN, 75, 127, GalacticraftItems.ROCKET_FIN)
                        .inputSlot(STORAGE, 93, 12, Ingredient.of(chests))
                        .inputSlot(STORAGE, 119, 12, Ingredient.of(chests))
                        .inputSlot(STORAGE, 145, 12, Ingredient.of(chests))
                        .outputSlot(142, 114, GalacticraftItems.TIER_2_ROCKET)
                        .build()
        );
        cxt.register(
                CARGO_ROCKET,
                VehicleCraftingDataRecipe.Builder.builder(slotTypes)
                        .inputSlot(ROCKET_NOSE_CONE, 48, 16, GalacticraftItems.ROCKET_NOSE_CONE)
                        .inputSlot(PLATING, 48, 34, Ingredient.of(heavyDutyPlates))
                        .inputSlot(PLATING, 39, 54, Ingredient.of(heavyDutyPlates))
                        .inputSlot(PLATING, 39, 72, Ingredient.of(heavyDutyPlates))
                        .inputSlot(PLATING, 39, 80, Ingredient.of(heavyDutyPlates))
                        .inputSlot(PLATING, 39, 108, Ingredient.of(heavyDutyPlates))
                        .inputSlot(PLATING, 39, 126, Ingredient.of(heavyDutyPlates))
                        .inputSlot(PLATING, 57, 54, Ingredient.of(heavyDutyPlates))
                        .inputSlot(PLATING, 57, 72, Ingredient.of(heavyDutyPlates))
                        .inputSlot(PLATING, 57, 80, Ingredient.of(heavyDutyPlates))
                        .inputSlot(PLATING, 57, 108, Ingredient.of(heavyDutyPlates))
                        .inputSlot(PLATING, 57, 126, Ingredient.of(heavyDutyPlates))
                        .inputSlot(ROCKET_FIN, 21, 90, GalacticraftItems.ROCKET_FIN)
                        .inputSlot(ROCKET_FIN, 21, 108, GalacticraftItems.ROCKET_FIN)
                        .inputSlot(ROCKET_ENGINE, 48, 108, GalacticraftItems.ROCKET_ENGINE)
                        .inputSlot(ROCKET_FIN, 75, 90, GalacticraftItems.ROCKET_FIN)
                        .inputSlot(ROCKET_FIN, 75, 108, GalacticraftItems.ROCKET_FIN)
                        .outputSlot(142, 96, GalacticraftItems.CARGO_ROCKET)
                        .build()
        );
        cxt.register(
                TIER_3_ROCKET,
                VehicleCraftingDataRecipe.Builder.builder(slotTypes)
                        .inputSlot(ROCKET_NOSE_CONE, 48, 19, GalacticraftItems.HEAVY_NOSE_CONE)
                        .inputSlot(PLATING, 39, 37, Ingredient.of(heavyDutyPlatesT3))
                        .inputSlot(PLATING, 39, 55, Ingredient.of(heavyDutyPlatesT3))
                        .inputSlot(PLATING, 39, 73, Ingredient.of(heavyDutyPlatesT3))
                        .inputSlot(PLATING, 39, 91, Ingredient.of(heavyDutyPlatesT3))
                        .inputSlot(PLATING, 39, 109, Ingredient.of(heavyDutyPlatesT3))
                        .inputSlot(PLATING, 57, 37, Ingredient.of(heavyDutyPlatesT3))
                        .inputSlot(PLATING, 57, 55, Ingredient.of(heavyDutyPlatesT3))
                        .inputSlot(PLATING, 57, 73, Ingredient.of(heavyDutyPlatesT3))
                        .inputSlot(PLATING, 57, 91, Ingredient.of(heavyDutyPlatesT3))
                        .inputSlot(PLATING, 57, 109, Ingredient.of(heavyDutyPlatesT3))
                        .inputSlot(ROCKET_BOOSTER, 21, 91, GalacticraftItems.ROCKET_BOOSTER)
                        .inputSlot(ROCKET_FIN, 21, 109, GalacticraftItems.HEAVY_FIN)
                        .inputSlot(ROCKET_FIN, 21, 127, GalacticraftItems.HEAVY_FIN)
                        .inputSlot(ROCKET_ENGINE, 48, 127, GalacticraftItems.HEAVY_ROCKET_ENGINE)
                        .inputSlot(ROCKET_BOOSTER, 75, 91, GalacticraftItems.ROCKET_BOOSTER)
                        .inputSlot(ROCKET_FIN, 75, 109, GalacticraftItems.HEAVY_FIN)
                        .inputSlot(ROCKET_FIN, 75, 127, GalacticraftItems.HEAVY_FIN)
                        .inputSlot(STORAGE, 93, 12, Ingredient.of(chests))
                        .inputSlot(STORAGE, 119, 12, Ingredient.of(chests))
                        .inputSlot(STORAGE, 145, 12, Ingredient.of(chests))
                        .outputSlot(142, 114, GalacticraftItems.TIER_3_ROCKET)
                        .build()
        );
        cxt.register(
                ASTRO_MINER,
                VehicleCraftingDataRecipe.Builder.builder(slotTypes)
                        .inputSlot(PLATING, 27, 35, Ingredient.of(heavyDutyPlates))
                        .inputSlot(ORION_DRIVE, 45, 35, GalacticraftItems.ORION_DRIVE)
                        .inputSlot(PLATING, 63, 35, Ingredient.of(heavyDutyPlates))
                        .inputSlot(ORION_DRIVE, 92, 35, GalacticraftItems.ORION_DRIVE)
                        .inputSlot(PLATING, 16, 35, Ingredient.of(heavyDutyPlates))
                        .inputSlot(ORION_DRIVE, 34, 53, GalacticraftItems.ORION_DRIVE)
                        .inputSlot(MISC, 52, 53, GalacticraftItems.ADVANCED_WAFER)
                        .inputSlot(STORAGE, 70, 53, Ingredient.of(chests))
                        .inputSlot(STORAGE, 88, 53, Ingredient.of(chests))
                        .inputSlot(ORION_DRIVE, 106, 53, GalacticraftItems.ORION_DRIVE)
                        .inputSlot(ORION_DRIVE, 44, 71, GalacticraftItems.ORION_DRIVE)
                        .inputSlot(STORAGE, 62, 71, Ingredient.of(heavyDutyPlates))
                        .inputSlot(ORION_DRIVE, 80, 71, GalacticraftItems.ORION_DRIVE)
                        .inputSlot(MISC, 8, 77, GalacticraftItems.COMPRESSED_ALUMINUM)
                        .inputSlot(MISC, 26, 77, GalacticraftItems.STEEL_POLE)
                        .outputSlot(142, 70, GalacticraftItems.ASTRO_MINER)
                        .build()
        );
    }

    public static VehicleCraftingDataRecipe getDefault(Level level) {
        return createDefault(
                level.registryAccess().lookupOrThrow(GalacticraftRegistries.Keys.VEHICLE_CRAFTING_SLOT_TYPE),
                level.registryAccess().lookupOrThrow(Registries.ITEM)
        );
    }

    private static VehicleCraftingDataRecipe createDefault(HolderGetter<VehicleCraftingSlotType> slotTypes, HolderGetter<Item> items) {
        HolderSet<Item> heavyDutyPlates = items.getOrThrow(GalacticraftTags.Items.PLATE_HEAVY_DUTY);
        HolderSet<Item> chests = items.getOrThrow(Tags.Items.CHESTS);

        return VehicleCraftingDataRecipe.Builder.builder(slotTypes)
                .inputSlot(ROCKET_NOSE_CONE, 48, 19, GalacticraftItems.ROCKET_NOSE_CONE)
                .inputSlot(PLATING, 39, 37, Ingredient.of(heavyDutyPlates))
                .inputSlot(PLATING, 39, 55, Ingredient.of(heavyDutyPlates))
                .inputSlot(PLATING, 39, 73, Ingredient.of(heavyDutyPlates))
                .inputSlot(PLATING, 39, 91, Ingredient.of(heavyDutyPlates))
                .inputSlot(PLATING, 57, 37, Ingredient.of(heavyDutyPlates))
                .inputSlot(PLATING, 57, 55, Ingredient.of(heavyDutyPlates))
                .inputSlot(PLATING, 57, 73, Ingredient.of(heavyDutyPlates))
                .inputSlot(PLATING, 57, 91, Ingredient.of(heavyDutyPlates))
                .inputSlot(ROCKET_FIN, 21, 91, GalacticraftItems.ROCKET_FIN)
                .inputSlot(ROCKET_FIN, 21, 109, GalacticraftItems.ROCKET_FIN)
                .inputSlot(ROCKET_ENGINE, 48, 109, GalacticraftItems.ROCKET_ENGINE)
                .inputSlot(ROCKET_FIN, 75, 91, GalacticraftItems.ROCKET_FIN)
                .inputSlot(ROCKET_FIN, 75, 109, GalacticraftItems.ROCKET_FIN)
                .inputSlot(STORAGE, 93, 12, Ingredient.of(chests))
                .inputSlot(STORAGE, 119, 12, Ingredient.of(chests))
                .inputSlot(STORAGE, 145, 12, Ingredient.of(chests))
                .outputSlot(142, 96, GalacticraftItems.TIER_1_ROCKET)
                .build();
    }

    private static ResourceKey<VehicleCraftingDataRecipe> key(String name) {
        return Constants.key(GalacticraftRegistries.Keys.VEHICLE_CRAFTING_RECIPE_DATA, name);
    }
}
