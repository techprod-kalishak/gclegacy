package io.kalishak.galacticraftlegacy.data.worldgen;

import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import net.minecraft.world.level.levelgen.SurfaceRules;

public class GalacticraftSurfaceRuleData {
    public static SurfaceRules.RuleSource moon() {
        return SurfaceRules.state(GalacticraftBlocks.MOON_TURF.get().defaultBlockState());
    }

    public static SurfaceRules.RuleSource empty() {
        return SurfaceRules.state(GalacticraftBlocks.EMPTY_AIR.get().defaultBlockState());
    }
}
