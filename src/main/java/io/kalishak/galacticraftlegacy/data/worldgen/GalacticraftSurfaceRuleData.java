/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.worldgen;

import com.google.common.collect.ImmutableList;
import io.kalishak.galacticraftlegacy.world.level.biome.MoonBiomes;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import io.kalishak.galacticraftlegacy.world.level.levelgen.GalacticraftNoises;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;

public class GalacticraftSurfaceRuleData {
    public static final SurfaceRules.RuleSource BEDROCK = SurfaceRules.state(Blocks.BEDROCK.defaultBlockState());
    public static final SurfaceRules.RuleSource MOON_TURF = SurfaceRules.state(GalacticraftBlocks.MOON_TURF.get().defaultBlockState());
    public static final SurfaceRules.RuleSource MOON_ROCK = SurfaceRules.state(GalacticraftBlocks.MOON_ROCK.get().defaultBlockState());
    public static final SurfaceRules.RuleSource MOON_DIRT = SurfaceRules.state(GalacticraftBlocks.MOON_DIRT.get().defaultBlockState());

    public static SurfaceRules.RuleSource moon() {
        SurfaceRules.ConditionSource steeps = SurfaceRules.steep();
        SurfaceRules.ConditionSource nearFloor = SurfaceRules.yStartCheck(VerticalAnchor.aboveBottom(10), 6);
        SurfaceRules.RuleSource commonSurface = SurfaceRules.sequence(
                SurfaceRules.ifTrue(
                        SurfaceRules.isBiome(MoonBiomes.MOON_PLAINS),
                        SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.noiseCondition(GalacticraftNoises.OPENSPACE, -0.12, 0.12), MOON_TURF),
                                MOON_DIRT
                        )
                ),
                SurfaceRules.sequence(
                        SurfaceRules.ifTrue(SurfaceRules.noiseCondition(GalacticraftNoises.OPENSPACE_VEG, -0.4, 0.4), MOON_TURF),
                        SurfaceRules.ifTrue(nearFloor, MOON_ROCK),
                        SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, MOON_DIRT)
                ),
                SurfaceRules.ifTrue(
                        SurfaceRules.ON_FLOOR,
                        SurfaceRules.sequence(
                                SurfaceRules.ifTrue(nearFloor, MOON_DIRT)
                        )
                ),
                MOON_ROCK
        );

        return SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), BEDROCK),
                SurfaceRules.ifTrue(steeps, commonSurface)
        );
    }

    public static SurfaceRules.RuleSource empty() {
        return SurfaceRules.state(Blocks.AIR.defaultBlockState());
    }
}
