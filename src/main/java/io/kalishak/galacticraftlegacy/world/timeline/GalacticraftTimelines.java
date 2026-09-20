/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.timeline;

import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.world.attribute.GalacticraftEnvironmentAttributes;
import io.kalishak.galacticraftlegacy.world.level.EarthPhase;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ARGB;
import net.minecraft.util.EasingType;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.attribute.modifier.BooleanModifier;
import net.minecraft.world.attribute.modifier.ColorModifier;
import net.minecraft.world.attribute.modifier.FloatModifier;
import net.minecraft.world.clock.ClockTimeMarkers;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.timeline.Timeline;

import static net.minecraft.world.timeline.Timelines.*;

public class GalacticraftTimelines {
    public static final ResourceKey<Timeline> MOON_DAY = Constants.key(Registries.TIMELINE, "moon_day");
    public static final ResourceKey<Timeline> MARS_DAY = Constants.key(Registries.TIMELINE, "mars_day");
    public static final ResourceKey<Timeline> EARTH = Constants.key(Registries.TIMELINE, "earth");
    public static final ResourceKey<Timeline> ORBIT = Constants.key(Registries.TIMELINE, "orbit");

    public static void bootstrap(BootstrapContext<Timeline> cxt) {
        HolderGetter<WorldClock> worldClocks = cxt.lookup(Registries.WORLD_CLOCK);
        EasingType skyAngleEase = EasingType.symmetricCubicBezier(0.362F, 0.241F);
        Holder<WorldClock> moonClock = worldClocks.getOrThrow(GalacticraftWorldClocks.MOON);
        cxt.register(
                MOON_DAY,
                Timeline.builder(moonClock)
                        .setPeriodTicks(354000)
                        .addTimeMarker(ClockTimeMarkers.DAY, 14750, true)
                        .addTimeMarker(ClockTimeMarkers.NOON, 88500, true)
                        .addTimeMarker(ClockTimeMarkers.NIGHT, 191750, true)
                        .addTimeMarker(ClockTimeMarkers.MIDNIGHT, 265500, true)
                        .addTimeMarker(ClockTimeMarkers.WAKE_UP_FROM_SLEEP, 0)
                        .addTrack(
                                EnvironmentAttributes.SUN_ANGLE,
                                (track) -> track.setEasing(skyAngleEase)
                                        .addKeyframe(88500, 360.0F)
                                        .addKeyframe(88500, 0.0F)
                        ).addTrack(
                                GalacticraftEnvironmentAttributes.EARTH_ANGLE.get(),
                                (track) -> track.setEasing(skyAngleEase)
                                        .addKeyframe(88500, 540.0F)
                                        .addKeyframe(88500, 180.0F)
                        ).addTrack(
                                EnvironmentAttributes.STAR_ANGLE,
                                (track) -> track.setEasing(skyAngleEase)
                                        .addKeyframe(88500, 360.0F)
                                        .addKeyframe(88500, 0.0F)
                        ).addModifierTrack(
                                EnvironmentAttributes.SKY_COLOR,
                                ColorModifier.MULTIPLY_RGB,
                                (track) -> track.addKeyframe(133, ARGB.vector3fFromRGB24(-1))
                                        .addKeyframe(11867, ARGB.vector3fFromRGB24(-1))
                                        .addKeyframe(13670, ARGB.vector3fFromRGB24(-16777216))
                                        .addKeyframe(22330, ARGB.vector3fFromRGB24(-16777216))
                        ).addModifierTrack(
                                EnvironmentAttributes.SKY_LIGHT_COLOR,
                                ColorModifier.MULTIPLY_RGB,
                                (track) -> track.addKeyframe(730, ARGB.vector3fFromRGB24(-1))
                                        .addKeyframe(11270, ARGB.vector3fFromRGB24(-1))
                                        .addKeyframe(13140, ARGB.vector3fFromRGB24(NIGHT_SKY_LIGHT_COLOR))
                                        .addKeyframe(22860, ARGB.vector3fFromRGB24(NIGHT_SKY_LIGHT_COLOR))
                        ).addModifierTrack(
                                EnvironmentAttributes.SKY_LIGHT_FACTOR,
                                FloatModifier.MULTIPLY,
                                (track) -> track.addKeyframe(730, 1.0F)
                                        .addKeyframe(11270, 1.0F)
                                        .addKeyframe(13140, 0.24F)
                                        .addKeyframe(22860, 0.24F)
                        ).addModifierTrack(
                                EnvironmentAttributes.SKY_LIGHT_LEVEL,
                                FloatModifier.MULTIPLY,
                                (track) -> track.addKeyframe(133, 1.0F)
                                        .addKeyframe(11867, 1.0F)
                                        .addKeyframe(13670, 0.26666668F)
                                        .addKeyframe(22330, 0.26666668F)
                        ).addTrack(
                                EnvironmentAttributes.SUNRISE_SUNSET_COLOR,
                                (track) -> track.addKeyframe(71, ARGB.vector4fFromARGB32(1609540403))
                                        .addKeyframe(310, ARGB.vector4fFromARGB32(703969843))
                                        .addKeyframe(565, ARGB.vector4fFromARGB32(117167155))
                                        .addKeyframe(730, ARGB.vector4fFromARGB32(16770355))
                                        .addKeyframe(11270, ARGB.vector4fFromARGB32(16770355))
                                        .addKeyframe(11397, ARGB.vector4fFromARGB32(83679283))
                                        .addKeyframe(11522, ARGB.vector4fFromARGB32(268028723))
                                        .addKeyframe(11690, ARGB.vector4fFromARGB32(703969843))
                                        .addKeyframe(11929, ARGB.vector4fFromARGB32(1609540403))
                                        .addKeyframe(12243, ARGB.vector4fFromARGB32(-1310226637))
                                        .addKeyframe(12358, ARGB.vector4fFromARGB32(-857440717))
                                        .addKeyframe(12512, ARGB.vector4fFromARGB32(-371166669))
                                        .addKeyframe(12613, ARGB.vector4fFromARGB32(-153261261))
                                        .addKeyframe(12732, ARGB.vector4fFromARGB32(-19242189))
                                        .addKeyframe(12841, ARGB.vector4fFromARGB32(-19440589))
                                        .addKeyframe(13035, ARGB.vector4fFromARGB32(-321760973))
                                        .addKeyframe(13252, ARGB.vector4fFromARGB32(-1043577037))
                                        .addKeyframe(13775, ARGB.vector4fFromARGB32(918435635))
                                        .addKeyframe(13888, ARGB.vector4fFromARGB32(532362547))
                                        .addKeyframe(14039, ARGB.vector4fFromARGB32(163001139))
                                        .addKeyframe(14192, ARGB.vector4fFromARGB32(11744051))
                                        .addKeyframe(21807, ARGB.vector4fFromARGB32(11678515))
                                        .addKeyframe(21961, ARGB.vector4fFromARGB32(163001139))
                                        .addKeyframe(22112, ARGB.vector4fFromARGB32(532362547))
                                        .addKeyframe(22225, ARGB.vector4fFromARGB32(918435635))
                                        .addKeyframe(22748, ARGB.vector4fFromARGB32(-1043577037))
                                        .addKeyframe(22965, ARGB.vector4fFromARGB32(-321760973))
                                        .addKeyframe(23159, ARGB.vector4fFromARGB32(-19440589))
                                        .addKeyframe(23272, ARGB.vector4fFromARGB32(-19242189))
                                        .addKeyframe(23488, ARGB.vector4fFromARGB32(-371166669))
                                        .addKeyframe(23642, ARGB.vector4fFromARGB32(-857440717))
                                        .addKeyframe(23757, ARGB.vector4fFromARGB32(-1310226637))
                        ).addModifierTrack(
                                EnvironmentAttributes.STAR_BRIGHTNESS,
                                FloatModifier.MAXIMUM,
                                (track) -> track.addKeyframe(92, 0.037F)
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
                        ).addModifierTrack(
                                EnvironmentAttributes.MONSTERS_BURN,
                                BooleanModifier.OR,
                                (track) -> track.addKeyframe(12542, false)
                                        .addKeyframe(23460, true)
                        ).build()
        );
        cxt.register(
                MARS_DAY,
                Timeline.builder(worldClocks.getOrThrow(GalacticraftWorldClocks.MARS))
                        .setPeriodTicks(24624)
                        .addTimeMarker(ClockTimeMarkers.DAY, 1026, true)
                        .addTimeMarker(ClockTimeMarkers.NOON, 6156, true)
                        .addTimeMarker(ClockTimeMarkers.NIGHT, 13338, true)
                        .addTimeMarker(ClockTimeMarkers.MIDNIGHT, 18468, true)
                        .addTimeMarker(ClockTimeMarkers.WAKE_UP_FROM_SLEEP, 0)
                        .addTrack(
                                EnvironmentAttributes.SUN_ANGLE,
                                (track) -> track.setEasing(skyAngleEase)
                                        .addKeyframe(6156, 360.0F)
                                        .addKeyframe(6156, 0.0F)
                        ).addTrack(
                                GalacticraftEnvironmentAttributes.EARTH_ANGLE.get(),
                                (track) -> track.setEasing(skyAngleEase)
                                        .addKeyframe(6156, 540.0F)
                                        .addKeyframe(6156, 180.0F)
                        ).addTrack(
                                EnvironmentAttributes.STAR_ANGLE,
                                (track) -> track.setEasing(skyAngleEase)
                                        .addKeyframe(6156, 360.0F)
                                        .addKeyframe(6156, 0.0F)
                        ).addModifierTrack(
                                EnvironmentAttributes.FOG_COLOR,
                                ColorModifier.MULTIPLY_RGB,
                                (track) -> track.addKeyframe(133, ARGB.vector3fFromRGB24(-1))
                                        .addKeyframe(11867, ARGB.vector3fFromRGB24(-1))
                                        .addKeyframe(13670, ARGB.vector3fFromRGB24(NIGHT_SKY_COLOR_MULTIPLIER))
                                        .addKeyframe(22330, ARGB.vector3fFromRGB24(NIGHT_FOG_COLOR_MULTIPLIER))
                        ).addModifierTrack(
                                EnvironmentAttributes.SKY_COLOR,
                                ColorModifier.MULTIPLY_RGB,
                                (track) -> track.addKeyframe(133, ARGB.vector3fFromRGB24(-1))
                                        .addKeyframe(11867, ARGB.vector3fFromRGB24(-1))
                                        .addKeyframe(13670, ARGB.vector3fFromRGB24(-16777216))
                                        .addKeyframe(22330, ARGB.vector3fFromRGB24(-16777216))
                        ).addModifierTrack(
                                EnvironmentAttributes.SKY_LIGHT_COLOR,
                                ColorModifier.MULTIPLY_RGB,
                                (track) -> track.addKeyframe(730, ARGB.vector3fFromRGB24(-1))
                                        .addKeyframe(11270, ARGB.vector3fFromRGB24(-1))
                                        .addKeyframe(13140, ARGB.vector3fFromRGB24(NIGHT_SKY_LIGHT_COLOR))
                                        .addKeyframe(22860, ARGB.vector3fFromRGB24(NIGHT_SKY_LIGHT_COLOR))
                        ).addModifierTrack(
                                EnvironmentAttributes.SKY_LIGHT_FACTOR,
                                FloatModifier.MULTIPLY,
                                (track) -> track.addKeyframe(730, 1.0F)
                                        .addKeyframe(11270, 1.0F)
                                        .addKeyframe(13140, 0.24F)
                                        .addKeyframe(22860, 0.24F)
                        ).addModifierTrack(
                                EnvironmentAttributes.SKY_LIGHT_LEVEL,
                                FloatModifier.MULTIPLY,
                                (track) -> track.addKeyframe(133, 1.0F)
                                        .addKeyframe(11867, 1.0F)
                                        .addKeyframe(13670, 0.26666668F)
                                        .addKeyframe(22330, 0.26666668F)
                        ).addTrack(
                                EnvironmentAttributes.SUNRISE_SUNSET_COLOR,
                                (track) -> track.addKeyframe(71, ARGB.vector4fFromARGB32(1609540403))
                                        .addKeyframe(310, ARGB.vector4fFromARGB32(703969843))
                                        .addKeyframe(565, ARGB.vector4fFromARGB32(117167155))
                                        .addKeyframe(730, ARGB.vector4fFromARGB32(16770355))
                                        .addKeyframe(11270, ARGB.vector4fFromARGB32(16770355))
                                        .addKeyframe(11397, ARGB.vector4fFromARGB32(83679283))
                                        .addKeyframe(11522, ARGB.vector4fFromARGB32(268028723))
                                        .addKeyframe(11690, ARGB.vector4fFromARGB32(703969843))
                                        .addKeyframe(11929, ARGB.vector4fFromARGB32(1609540403))
                                        .addKeyframe(12243, ARGB.vector4fFromARGB32(-1310226637))
                                        .addKeyframe(12358, ARGB.vector4fFromARGB32(-857440717))
                                        .addKeyframe(12512, ARGB.vector4fFromARGB32(-371166669))
                                        .addKeyframe(12613, ARGB.vector4fFromARGB32(-153261261))
                                        .addKeyframe(12732, ARGB.vector4fFromARGB32(-19242189))
                                        .addKeyframe(12841, ARGB.vector4fFromARGB32(-19440589))
                                        .addKeyframe(13035, ARGB.vector4fFromARGB32(-321760973))
                                        .addKeyframe(13252, ARGB.vector4fFromARGB32(-1043577037))
                                        .addKeyframe(13775, ARGB.vector4fFromARGB32(918435635))
                                        .addKeyframe(13888, ARGB.vector4fFromARGB32(532362547))
                                        .addKeyframe(14039, ARGB.vector4fFromARGB32(163001139))
                                        .addKeyframe(14192, ARGB.vector4fFromARGB32(11744051))
                                        .addKeyframe(21807, ARGB.vector4fFromARGB32(11678515))
                                        .addKeyframe(21961, ARGB.vector4fFromARGB32(163001139))
                                        .addKeyframe(22112, ARGB.vector4fFromARGB32(532362547))
                                        .addKeyframe(22225, ARGB.vector4fFromARGB32(918435635))
                                        .addKeyframe(22748, ARGB.vector4fFromARGB32(-1043577037))
                                        .addKeyframe(22965, ARGB.vector4fFromARGB32(-321760973))
                                        .addKeyframe(23159, ARGB.vector4fFromARGB32(-19440589))
                                        .addKeyframe(23272, ARGB.vector4fFromARGB32(-19242189))
                                        .addKeyframe(23488, ARGB.vector4fFromARGB32(-371166669))
                                        .addKeyframe(23642, ARGB.vector4fFromARGB32(-857440717))
                                        .addKeyframe(23757, ARGB.vector4fFromARGB32(-1310226637))
                        ).addModifierTrack(
                                EnvironmentAttributes.STAR_BRIGHTNESS,
                                FloatModifier.MAXIMUM,
                                (track) -> track.addKeyframe(92, 0.037F)
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
                        ).addModifierTrack(
                                EnvironmentAttributes.CLOUD_COLOR,
                                ColorModifier.MULTIPLY_ARGB,
                                (track) -> track.addKeyframe(133, ARGB.vector4fFromARGB32(-1))
                                        .addKeyframe(11867, ARGB.vector4fFromARGB32(-1))
                                        .addKeyframe(13670, ARGB.vector4fFromARGB32(NIGHT_CLOUD_COLOR_MULTIPLIER))
                                        .addKeyframe(22330, ARGB.vector4fFromARGB32(NIGHT_CLOUD_COLOR_MULTIPLIER))
                        ).addModifierTrack(
                                EnvironmentAttributes.MONSTERS_BURN,
                                BooleanModifier.OR,
                                (track) -> track.addKeyframe(12542, false)
                                        .addKeyframe(23460, true)
                        ).build()
        );
        Timeline.Builder earthPhases = Timeline.builder(moonClock).setPeriodTicks(24000 * 29).addTrack(GalacticraftEnvironmentAttributes.EARTH_PHASE.get(), (track) -> {
            for(EarthPhase phase : EarthPhase.values()) {
                track.addKeyframe(phase.startTick(), phase);
            }
        });
        cxt.register(EARTH, earthPhases.build());
        cxt.register(
                ORBIT,
                Timeline.builder(worldClocks.getOrThrow(GalacticraftWorldClocks.ORBIT))
                        .addModifierTrack(
                                EnvironmentAttributes.STAR_BRIGHTNESS,
                                FloatModifier.MAXIMUM,
                                (track) -> track.addKeyframe(1, 1.0F)
                        )
                        .build()
        );
    }
}
