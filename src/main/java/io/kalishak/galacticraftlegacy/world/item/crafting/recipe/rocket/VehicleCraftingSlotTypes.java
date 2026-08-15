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
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public interface VehicleCraftingSlotTypes {
    // Rocket
    ResourceKey<VehicleCraftingSlotType> ROCKET_NOSE_CONE = key("nose_cone");
    ResourceKey<VehicleCraftingSlotType> ROCKET_FIN = key("fin");
    ResourceKey<VehicleCraftingSlotType> ROCKET_ENGINE = key("engine");
    ResourceKey<VehicleCraftingSlotType> ROCKET_BOOSTER = key("booster");
    //Buggy
    ResourceKey<VehicleCraftingSlotType> BUGGY_WHEEL = key("wheel");
    ResourceKey<VehicleCraftingSlotType> BUGGY_SEAT = key("seat");
    //Astro Miner
    ResourceKey<VehicleCraftingSlotType> ORION_DRIVE = key("orion_drive");
    ResourceKey<VehicleCraftingSlotType> MISC = key("circuit");
    //Common
    ResourceKey<VehicleCraftingSlotType> PLATING = key("plating");
    ResourceKey<VehicleCraftingSlotType> STORAGE = key("storage");
    ResourceKey<VehicleCraftingSlotType> RESULT = key("result");

    static void bootstrap(BootstrapContext<VehicleCraftingSlotType> context) {
        HolderGetter<Item> items = context.lookup(Registries.ITEM);

        named(context, ROCKET_NOSE_CONE, items, GalacticraftTags.Items.VEHICLE_INGREDIENT_CONE);
        named(context, ROCKET_FIN, items, GalacticraftTags.Items.VEHICLE_INGREDIENT_FIN);
        named(context, ROCKET_ENGINE, items, GalacticraftTags.Items.VEHICLE_INGREDIENT_ENGINE);
        named(context, ROCKET_BOOSTER, items, GalacticraftTags.Items.VEHICLE_INGREDIENT_BOOSTER);
        named(context, BUGGY_WHEEL, items, GalacticraftTags.Items.VEHICLE_INGREDIENT_WHEEL);
        named(context, BUGGY_SEAT, items, GalacticraftTags.Items.VEHICLE_INGREDIENT_SEAT);
        direct(context, ORION_DRIVE, GalacticraftItems.ORION_DRIVE);
        named(context, MISC, items, GalacticraftTags.Items.VEHICLE_INGREDIENT_MISC);
        named(context, PLATING, items, GalacticraftTags.Items.VEHICLE_INGREDIENT_PLATING);
        named(context, STORAGE, items, GalacticraftTags.Items.VEHICLE_INGREDIENT_STORAGE);
        context.register(RESULT, new VehicleCraftingSlotType(HolderSet.empty()));
    }

    private static ResourceKey<VehicleCraftingSlotType> key(String name) {
        return Constants.key(GalacticraftRegistries.Keys.VEHICLE_CRAFTING_SLOT_TYPE, name);
    }

    @SafeVarargs
    private static void direct(BootstrapContext<VehicleCraftingSlotType> cxt, ResourceKey<VehicleCraftingSlotType> key, Holder<Item>... items) {
        cxt.register(key, new VehicleCraftingSlotType(HolderSet.direct(items)));
    }

    private static void named(BootstrapContext<VehicleCraftingSlotType> cxt, ResourceKey<VehicleCraftingSlotType> key, HolderGetter<Item> items, TagKey<Item> tag) {
        cxt.register(key, new VehicleCraftingSlotType(items.getOrThrow(tag)));
    }
}
