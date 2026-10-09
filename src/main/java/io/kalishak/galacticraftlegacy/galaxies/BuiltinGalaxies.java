package io.kalishak.galacticraftlegacy.galaxies;

import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.galaxies.celestial.Celestial;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.registry.ChecklistEntry;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import io.kalishak.galacticraftlegacy.world.level.dimension.GalacticraftDimensionTypes;
import io.kalishak.galacticraftlegacy.world.level.dimension.SpaceStationRecipe;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.function.UnaryOperator;

public class BuiltinGalaxies {
    public static final ResourceKey<Celestial> SOL = key("sol");
    public static final ResourceKey<Celestial> SUN = key("sun");
    public static final ResourceKey<Celestial> MERCURY = key("mercury");
    public static final ResourceKey<Celestial> VENUS = key("venus");
    public static final ResourceKey<Celestial> EARTH = key("earth");
    public static final ResourceKey<Celestial> MOON = key("moon");
    public static final ResourceKey<Celestial> SATELLITE = key("satellite");
    public static final ResourceKey<Celestial> MARS = key("mars");
    public static final ResourceKey<Celestial> ASTEROIDS = key("asteroids");
    public static final ResourceKey<Celestial> JUPITER = key("jupiter");
    public static final ResourceKey<Celestial> SATURN = key("saturn");
    public static final ResourceKey<Celestial> URANUS = key("uranus");
    public static final ResourceKey<Celestial> NEPTUNE = key("neptune");

    public static void bootstrap(BootstrapContext<Celestial> context) {
        HolderGetter<Celestial> self = context.lookup(GalacticraftRegistries.Keys.CELESTIAL);
        HolderGetter<DimensionType> dimensions = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<ChecklistEntry> checklists = context.lookup(GalacticraftRegistries.Keys.CHECKLIST);

        context.register(
                SUN,
                Celestial.Builder.of()
                        .phaseShift(1.45F)
                        .relativeDistanceFromCenter(0.5F)
                        .relativeOrbitTime(0.24096385542168674698795180722892F)
                        .createStar(SUN)
        );
        context.register(
                SOL,
                Celestial.Builder.of()
                        .phaseShift(1.45F)
                        .relativeDistanceFromCenter(0.5F)
                        .relativeOrbitTime(0.24096385542168674698795180722892F)
                        .createSolarSystem(SOL, self.getOrThrow(SUN))
        );
        registerCelestial(
                context,
                MERCURY,
                builder -> builder
                        .phaseShift(1.45F)
                        .relativeDistanceFromCenter(0.5F)
                        .relativeOrbitTime(0.24096385542168674698795180722892F)
                        .parent(self.getOrThrow(SUN))
        );
        registerCelestial(
                context,
                VENUS,
                builder -> builder
                        .phaseShift(2.0F)
                        .relativeDistanceFromCenter(0.75F)
                        .relativeOrbitTime(0.61527929901423877327491785323111F)
                        .atmosphereInfo(atmosphere -> atmosphere
                                .corrosive()
                                .rainy()
                                .gas(ResourceKey.create(Registries.FLUID, Constants.id("carbon_dioxide")), 96.5)
                                .gas(ResourceKey.create(Registries.FLUID, Constants.id("nitrogen")), 3.5)

                        )
                        .transferableTo(dimensions.getOrThrow(GalacticraftDimensionTypes.VENUS), FeatureTier.TIER_3)
                        .parent(self.getOrThrow(SUN))
                        .ringColors(0.1F, 0.9F, 0.6F)
                        //.biome(GalacticraftTags.Biomes.IS_VENUS)
                        .checklist(checklists.getOrThrow(GalacticraftTags.Checklist.VENUS_CHECKLIST))
        );
        registerCelestial(
                context,
                EARTH,
                builder -> builder
                        .atmosphereInfo(atmosphere -> atmosphere
                                .rainy()
                                .gas(ResourceKey.create(Registries.FLUID, Constants.id("nitrogen")), 78.08F)
                                .gas(ResourceKey.create(Registries.FLUID, Constants.id("oxygen")), 20.95F)
                                .gas(ResourceKey.create(Registries.FLUID, Constants.id("argon")), 0.93F)
                                .gas(ResourceKey.create(Registries.FLUID, Constants.id("carbon_dioxide")), 0.04F)
                        )
                        .parent(self.getOrThrow(SUN))
                        //.biome(BiomeTags.IS_OVERWORLD)
                        .checklist(checklists.getOrThrow(GalacticraftTags.Checklist.OVERWORLD_CHECKLIST))
        );
        registerCelestial(
                context,
                MARS,
                builder -> builder
                        .relativeSize(0.5319F)
                        .phaseShift(0.1667F)
                        .relativeDistanceFromCenter(1.25F)
                        .relativeOrbitTime(1.8811610076670317634173055859803F)
                        .atmosphereInfo(atmosphere -> atmosphere
                                .gas(ResourceKey.create(Registries.FLUID, Constants.id("carbon_dioxide")), 95.3F)
                                .gas(ResourceKey.create(Registries.FLUID, Constants.id("argon")), 2.7F)
                                .gas(ResourceKey.create(Registries.FLUID, Constants.id("nitrogen")), 2.0F)
                        )
                        .transferableTo(dimensions.getOrThrow(GalacticraftDimensionTypes.MARS), FeatureTier.TIER_2)
                        .parent(self.getOrThrow(SUN))
                        .ringColors(0.67F, 0.1F, 0.1F)
                        //.biome(GalacticraftTags.Biomes.IS_MARS)
                        .checklist(checklists.getOrThrow(GalacticraftTags.Checklist.MARS_CHECKLIST))
        );
        registerCelestial(
                context,
                JUPITER,
                builder -> builder
                        .phaseShift((float) Math.PI)
                        .relativeDistanceFromCenter(1.5F)
                        .relativeOrbitTime(11.861993428258488499452354874042F)
                        .parent(self.getOrThrow(SUN))
        );
        registerCelestial(
                context,
                SATURN,
                builder -> builder
                        .phaseShift(5.45F)
                        .relativeDistanceFromCenter(1.75F)
                        .relativeOrbitTime(29.463307776560788608981380065717F)
                        .parent(self.getOrThrow(SUN))
                        .withRings(Constants.texture("gui/celestial_body/saturn_rings"))
        );
        registerCelestial(
                context,
                URANUS,
                builder -> builder
                        .phaseShift(1.38F)
                        .relativeDistanceFromCenter(2.0F)
                        .relativeOrbitTime(84.063526834611171960569550930997F)
                        .parent(self.getOrThrow(SUN))
                        .withRings(Constants.texture("gui/celestial_body/uranus_rings"))
        );
        registerCelestial(
                context,
                NEPTUNE,
                builder -> builder
                        .phaseShift(1.0F)
                        .relativeDistanceFromCenter(2.25F)
                        .relativeOrbitTime(164.84118291347207009857612267251F)
                        .parent(self.getOrThrow(SUN))
                        .withRings(Constants.texture("gui/celestial_body/neptune_rings"))
        );
        registerCelestial(
                context,
                MOON,
                builder -> builder
                        .relativeSize(0.2667F)
                        .relativeDistanceFromCenter(13.0F)
                        .relativeOrbitTime(1.0F / 0.1F)
                        .parent(self.getOrThrow(EARTH))
                        .transferableTo(dimensions.getOrThrow(GalacticraftDimensionTypes.MOON), FeatureTier.TIER_1)
                        //.biome(GalacticraftTags.Biomes.IS_MOON)
                        .checklist(checklists.getOrThrow(GalacticraftTags.Checklist.MOON_CHECKLIST))
        );
        registerCelestial(
                context,
                ASTEROIDS,
                builder -> builder
                        .phaseShift((float) (Math.random() * (2 * Math.PI)))
                        .relativeDistanceFromCenter(1.375F)
                        .relativeOrbitTime(45.0F)
                        .parent(self.getOrThrow(SUN))
                        //.biome(GalacticraftTags.Biomes.IS_ASTEROIDS)
                        .transferableTo(dimensions.getOrThrow(GalacticraftDimensionTypes.ASTEROIDS), FeatureTier.TIER_3)
                        .checklist(checklists.getOrThrow(GalacticraftTags.Checklist.ASTEROIDS_CHECKLIST))
        );

        context.register(
                SATELLITE,
                Celestial.Builder.of()
                        .relativeSize(0.2667F)
                        .relativeDistanceFromCenter(9.0F)
                        .relativeOrbitTime(1.0F / 0.05F)
                        .parent(self.getOrThrow(EARTH))
                        //.biome(GalacticraftTags.Biomes.IS_OPEN_SPACE)
                        .transferableTo(dimensions.getOrThrow(GalacticraftDimensionTypes.SATELLITE), FeatureTier.TIER_1)
                        .checklist(checklists.getOrThrow(GalacticraftTags.Checklist.SATELLITE_CHECKLIST))
                        .withRecipe(context.lookup(GalacticraftRegistries.Keys.SPACE_STATION_RECIPE).getOrThrow(SpaceStationRecipe.EARTH_SPACE_STATION_RECIPE))
                        .createSatellite(SATELLITE)
        );
    }

    public static void registerCelestial(BootstrapContext<Celestial> context, ResourceKey<Celestial> key, UnaryOperator<Celestial.Builder> builder) {
        context.register(key, builder.apply(Celestial.Builder.of()).create(key));
    }

    private static ResourceKey<Celestial> key(String name) {
        return Constants.key(GalacticraftRegistries.Keys.CELESTIAL, name);
    }
}
