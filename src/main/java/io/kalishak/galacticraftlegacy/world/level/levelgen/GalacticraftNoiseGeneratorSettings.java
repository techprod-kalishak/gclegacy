package io.kalishak.galacticraftlegacy.world.level.levelgen;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.data.worldgen.GalacticraftSurfaceRuleData;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.*;

import java.util.List;

public final class GalacticraftNoiseGeneratorSettings {
    public static final ResourceKey<NoiseGeneratorSettings> MOON = Constants.key(Registries.NOISE_SETTINGS, "moon");
    public static final ResourceKey<NoiseGeneratorSettings> OPEN_SPACE = Constants.key(Registries.NOISE_SETTINGS, "open_space");
    public static final ResourceKey<NoiseGeneratorSettings> MARS = Constants.key(Registries.NOISE_SETTINGS, "mars");
    public static final ResourceKey<NoiseGeneratorSettings> ASTEROIDS = Constants.key(Registries.NOISE_SETTINGS, "asteroids");
    public static final ResourceKey<NoiseGeneratorSettings> VENUS = Constants.key(Registries.NOISE_SETTINGS, "venus");

    public static void bootstrap(BootstrapContext<NoiseGeneratorSettings> cxt) {
        cxt.register(
                MOON,
                new NoiseGeneratorSettings(
                        NoiseSettings.create(-64, 256, 1, 2),
                        GalacticraftBlocks.MOON_ROCK.get().defaultBlockState(),
                        GalacticraftBlocks.EMPTY_AIR.get().defaultBlockState(),
                        GalacticraftNoiseRouterData.moon(cxt.lookup(Registries.DENSITY_FUNCTION), cxt.lookup(Registries.NOISE)),
                        GalacticraftSurfaceRuleData.moon(),
                        List.of(),
                        0,
                        false,
                        false,
                        true,
                        false
                )
        );
        cxt.register(
                OPEN_SPACE,
                new NoiseGeneratorSettings(
                        NoiseSettings.create(0, 256, 2, 1),
                        GalacticraftBlocks.EMPTY_AIR.get().defaultBlockState(),
                        GalacticraftBlocks.EMPTY_AIR.get().defaultBlockState(),
                        GalacticraftNoiseRouterData.empty(),
                        GalacticraftSurfaceRuleData.empty(),
                        List.of(),
                        0,
                        false,
                        false,
                        true,
                        false
                )
        );
    }
}
