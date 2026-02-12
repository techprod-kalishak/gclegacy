package io.kalishak.galacticraftlegacy.world.timeline;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.world.attribute.GalacticraftEnvironmentAttributes;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.EasingType;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.attribute.modifier.BooleanModifier;
import net.minecraft.world.attribute.modifier.FloatModifier;
import net.minecraft.world.timeline.Timeline;

public class GalacticraftTimelines {
    public static final ResourceKey<Timeline> MOON_DAY = Constants.key(Registries.TIMELINE, "moon_day");
    public static final ResourceKey<Timeline> EARTH = Constants.key(Registries.TIMELINE, "earth");

    public static void bootstrap(BootstrapContext<Timeline> cxt) {
        EasingType easingType = EasingType.symmetricCubicBezier(0.362F, 0.241F);
        cxt.register(
                MOON_DAY,
                Timeline.builder()
                        .setPeriodTicks(24000 * 14) // 14 Minecraft days
                        .addTrack(EnvironmentAttributes.SUN_ANGLE, builder -> builder.setEasing(easingType).addKeyframe(6000, 360.0F).addKeyframe(6000, 0.0F))
                        .addTrack(GalacticraftEnvironmentAttributes.EARTH_ANGLE.get(), builder -> builder.setEasing(easingType).addKeyframe(6000, 540.0F).addKeyframe(6000, 180.0F))
                        .addTrack(EnvironmentAttributes.STAR_ANGLE, builder -> builder.setEasing(easingType).addKeyframe(6000, 360.0F).addKeyframe(6000, 0.0F))
                        .addTrack(
                                EnvironmentAttributes.SUNRISE_SUNSET_COLOR,
                                builder -> builder.addKeyframe(71, 1609540403)
                                        .addKeyframe(310, 703969843)
                                        .addKeyframe(565, 117167155)
                                        .addKeyframe(730, 16770355)
                                        .addKeyframe(11270, 16770355)
                                        .addKeyframe(11397, 83679283)
                                        .addKeyframe(11522, 268028723)
                                        .addKeyframe(11690, 703969843)
                                        .addKeyframe(11929, 1609540403)
                                        .addKeyframe(12243, -1310226637)
                                        .addKeyframe(12358, -857440717)
                                        .addKeyframe(12512, -371166669)
                                        .addKeyframe(12613, -153261261)
                                        .addKeyframe(12732, -19242189)
                                        .addKeyframe(12841, -19440589)
                                        .addKeyframe(13035, -321760973)
                                        .addKeyframe(13252, -1043577037)
                                        .addKeyframe(13775, 918435635)
                                        .addKeyframe(13888, 532362547)
                                        .addKeyframe(14039, 163001139)
                                        .addKeyframe(14192, 11744051)
                                        .addKeyframe(21807, 11678515)
                                        .addKeyframe(21961, 163001139)
                                        .addKeyframe(22112, 532362547)
                                        .addKeyframe(22225, 918435635)
                                        .addKeyframe(22748, -1043577037)
                                        .addKeyframe(22965, -321760973)
                                        .addKeyframe(23159, -19440589)
                                        .addKeyframe(23272, -19242189)
                                        .addKeyframe(23488, -371166669)
                                        .addKeyframe(23642, -857440717)
                                        .addKeyframe(23757, -1310226637)
                        )
                        .addModifierTrack(
                                EnvironmentAttributes.STAR_BRIGHTNESS,
                                FloatModifier.MAXIMUM,
                                builder -> builder.addKeyframe(92, 0.037F)
                                        .addKeyframe(627, 0.0F)
                                        .addKeyframe(11373, 0.0F)
                                        .addKeyframe(11732, 0.016F)
                                        .addKeyframe(11959, 0.044F)
                                        .addKeyframe(12399, 0.143F)
                                        .addKeyframe(12729, 0.258F)
                                        .addKeyframe(13228, 0.5F)
                                        .addKeyframe(22772, 0.5F)
                                        .addKeyframe(23032, 0.364F)
                                        .addKeyframe(23356, 0.225F)
                                        .addKeyframe(23758, 0.101F)
                        )
                        .addModifierTrack(
                                EnvironmentAttributes.MONSTERS_BURN, BooleanModifier.OR, builder -> builder.addKeyframe(12542 * 14, false)
                                        .addKeyframe(23460 * 14, true)
                        )
                        .build()
        );
//        cxt.register(
//                EARTH,
//                Timeline.builder()
//                        .setPeriodTicks(24000 * 14 * MoonPhase.COUNT)
//                        .addTrack(GalacticraftEnvironmentAttributes.EARTH_PHASE.get(), builder -> {
//                            for (EarthPhase phase : EarthPhase.values()) {
//                                builder.addKeyframe(phase.startTick(), phase);
//                            }
//                        }).build()
//        );
    }
}
