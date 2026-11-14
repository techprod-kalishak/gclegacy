/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.config;

import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import net.minecraft.SharedConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CommonConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue DEBUG_MODE = BUILDER
            .push("general")
            .comment("If this is enabled, debug messages will appear in the console. This is useful for finding bugs in the mod. (this is still overridden in IDE)")
            .translation(GalacticraftComponents.CONFIG_DEBUG_MODE)
            .define("debugMode", false);

    //TODO tag key?
    public static final ModConfigSpec.ConfigValue<List<? extends String>> DIMENSIONS_WITH_DISABLED_ROCKETS = BUILDER
            .comment("Keys of dimensions where rockets should not launch - this should always include The Nether.")
            .translation(GalacticraftComponents.CONFIG_DISABLED_ROCKETS_DIMENSIONS)
            .defineListAllowEmpty(
                    "dimensionsWithDisabledRockets",
                    List.of(
                            BuiltinDimensionTypes.NETHER.identifier().toString(),
                            BuiltinDimensionTypes.END.identifier().toString()
                    ),
                    () -> "",
                    CommonConfig::validateResourceKeys);
    public static final ModConfigSpec.BooleanValue DISABLE_RETURNING_ROCKETS = BUILDER
            .comment("If true, rockets will be unable to reach the Overworld (only use this in special modpacks!)")
            .translation(GalacticraftComponents.CONFIG_DISABLE_RETURNING_ROCKETS)
            .define("disableReturningRockets", false);
    public static final ModConfigSpec.BooleanValue FORCE_OVERWORLD_RESPAWN = BUILDER
            .comment("By default, you will respawn on Galacticraft dimensions if you die. If you are dying over and over on a planet, set this to true, and you will respawn back on the Overworld.")
            .translation(GalacticraftComponents.CONFIG_FORCE_OVERWORLD_RESPAWN)
            .define("forceOverworldRespawn", false);
    public static final ModConfigSpec.BooleanValue DISABLE_LANDERS = BUILDER
            .comment("If this is true, the Player will parachute onto celestial bodies instead - use only in debug situations.")
            .translation(GalacticraftComponents.CONFIG_DISABLE_LANDERS)
            .define("disableLanders", false);
    public static final ModConfigSpec.BooleanValue DISABLE_ROCKET_PARTICLES = BUILDER
            .comment("If you have FPS problems, setting this to true will help if rocket particles are in your sights")
            .translation(GalacticraftComponents.CONFIG_DISABLE_PARTICLES)
            .define("disableRocketParticles", false);

    public static final ModConfigSpec SPEC = BUILDER.pop().build();

    private static boolean validateResourceKeys(final Object obj) {
        return obj instanceof String key && Identifier.tryParse(key) != null;
    }

    public static List<ResourceKey<Level>> getDimensions() {
        return DIMENSIONS_WITH_DISABLED_ROCKETS.get().stream().map(Identifier::tryParse).filter(Objects::nonNull).map(key -> ResourceKey.create(Registries.DIMENSION, key)).toList();
    }

    public static boolean showDebug() {
        return SharedConstants.DEBUG_ENABLED && DEBUG_MODE.get();
    }

    public static void debug(Consumer<String> messageGetter, String message) {
        if (showDebug()) messageGetter.accept(message);
    }

    public static void debug(BiConsumer<String, Object> messageGetter, String message, Object o) {
        if (showDebug()) messageGetter.accept(message, o);
    }

    public static void debug(BiConsumer<String, Object[]> messageGetter, String message, Object... o) {
        if (showDebug()) messageGetter.accept(message, o);
    }
}
