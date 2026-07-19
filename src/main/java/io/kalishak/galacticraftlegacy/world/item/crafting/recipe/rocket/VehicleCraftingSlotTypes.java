package io.kalishak.galacticraftlegacy.world.item.crafting.recipe.rocket;

import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.function.BiConsumer;

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
    ResourceKey<VehicleCraftingSlotType> ORION_DRIVE =  key("orion_drive");
    ResourceKey<VehicleCraftingSlotType> MISC =  key("circuit");
    //Common
    ResourceKey<VehicleCraftingSlotType> PLATING =  key("plating");
    ResourceKey<VehicleCraftingSlotType> STORAGE = key("storage");
    ResourceKey<VehicleCraftingSlotType> RESULT = key("result");

    static void bootstrap(BootstrapContext<VehicleCraftingSlotType> context) {
        context.register(ROCKET_NOSE_CONE, new VehicleCraftingSlotType("nose_cone"));
        context.register(ROCKET_FIN, new VehicleCraftingSlotType("fin"));
        context.register(ROCKET_ENGINE, new VehicleCraftingSlotType("engine"));
        context.register(ROCKET_BOOSTER, new VehicleCraftingSlotType("booster"));
        context.register(BUGGY_WHEEL, new VehicleCraftingSlotType("wheel"));
        context.register(BUGGY_SEAT, new VehicleCraftingSlotType("seat"));
        context.register(ORION_DRIVE, new VehicleCraftingSlotType("drive"));
        context.register(MISC, new VehicleCraftingSlotType("misc"));
        context.register(PLATING, new VehicleCraftingSlotType("plating"));
        context.register(STORAGE, new VehicleCraftingSlotType("storage"));
        context.register(RESULT, new VehicleCraftingSlotType("result"));
    }

    private static ResourceKey<VehicleCraftingSlotType> key(String name) {
        return Constants.key(GalacticraftRegistries.Keys.VEHICLE_CRAFTING_SLOT_TYPE, name);
    }
}
