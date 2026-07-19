package io.kalishak.galacticraftlegacy.world.inventory.workbench;

import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.registry.SchematicVariant;
import io.kalishak.galacticraftlegacy.registry.SchematicVariants;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.rocket.VehicleCraftingDataRecipe;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

public class VehicleCraftingPages {
    public static final ResourceKey<VehicleCraftingPage> TIER_2_ROCKET = fromSchematic(SchematicVariants.TIER_2_ROCKET);
    public static final ResourceKey<VehicleCraftingPage> CARGO_ROCKET = fromSchematic(SchematicVariants.CARGO_ROCKET);
    public static final ResourceKey<VehicleCraftingPage> TIER_3_ROCKET = fromSchematic(SchematicVariants.TIER_3_ROCKET);
    public static final ResourceKey<VehicleCraftingPage> MOON_BUGGY = fromSchematic(SchematicVariants.MOON_BUGGY);
    public static final ResourceKey<VehicleCraftingPage> ASTRO_MINER = fromSchematic(SchematicVariants.ASTRO_MINER);

    public static void bootstrap(BootstrapContext<VehicleCraftingPage> cxt) {
        HolderGetter<SchematicVariant> schematics = cxt.lookup(GalacticraftRegistries.Keys.SCHEMATIC);
        HolderGetter<VehicleCraftingDataRecipe> vehicleRecipes = cxt.lookup(GalacticraftRegistries.Keys.VEHICLE_CRAFTING_RECIPE_DATA);

        register(cxt, schematics, vehicleRecipes, TIER_2_ROCKET, 156);
        register(cxt, schematics, vehicleRecipes, CARGO_ROCKET, 138);
        register(cxt, schematics, vehicleRecipes, TIER_3_ROCKET, 156);
        register(cxt, schematics, vehicleRecipes, MOON_BUGGY, 138);
        register(cxt, schematics, vehicleRecipes, ASTRO_MINER, 140);
    }

    private static ResourceKey<VehicleCraftingPage> fromSchematic(ResourceKey<SchematicVariant> schematic) {
        return Constants.castKey(schematic, GalacticraftRegistries.Keys.VEHICLE_CRAFTING_PAGE);
    }

    private static void register(BootstrapContext<VehicleCraftingPage> cxt, HolderGetter<SchematicVariant> schematics, HolderGetter<VehicleCraftingDataRecipe> vehicleRecipes, ResourceKey<VehicleCraftingPage> identifier, int height) {
        cxt.register(
                identifier,
                new VehicleCraftingPage(
                        schematics.getOrThrow(Constants.castKey(identifier, GalacticraftRegistries.Keys.SCHEMATIC)),
                        vehicleRecipes.getOrThrow(Constants.castKey(identifier, GalacticraftRegistries.Keys.VEHICLE_CRAFTING_RECIPE_DATA)),
                        height,
                        identifier.identifier().withPath(path -> "gui/container/workbench_page/" + path + ".png")
                )
        );
    }
}
