/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.config;

import java.util.List;
import java.util.Objects;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.neoforged.neoforge.common.ModConfigSpec;

public class CommonConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue DEBUG_MODE = BUILDER
            .push("general")
            .comment("If this is enabled, debug messages will appear in the console. This is useful for finding bugs in the mod. (this is still overridden in IDE)")
            .translation("galacticraftlegacy.configgui.common.debug_mode")
            .define("debugMode", false);

    //TODO tag key?
    public static final ModConfigSpec.ConfigValue<List<? extends String>> DIMENSIONS_WITH_DISABLED_ROCKETS = BUILDER
            .comment("Keys of dimensions where rockets should not launch - this should always include The Nether.")
            .translation("galacticraftlegacy.configgui.common.dimensions_with_disabled_rockets")
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
            .translation("galacticraftlegacy.configgui.common.disable_returning_rockets")
            .define("disableReturningRockets", false);
    public static final ModConfigSpec.BooleanValue FORCE_OVERWORLD_RESPAWN = BUILDER
            .comment("By default, you will respawn on Galacticraft dimensions if you die. If you are dying over and over on a planet, set this to true, and you will respawn back on the Overworld.")
            .translation("galacticraftlegacy.configgui.common.force_overworld_respawn")
            .define("forceOverworldRespawn", false);
    public static final ModConfigSpec.BooleanValue DISABLE_LANDERS = BUILDER
            .comment("If this is true, the Player will parachute onto celestial bodies instead - use only in debug situations.")
            .translation("galacticraftlegacy.configgui.common.disable_landers")
            .define("disableLanders", false);

    public static final ModConfigSpec SPEC = BUILDER.pop().build();

    private static boolean validateResourceKeys(final Object obj) {
        return obj instanceof String key && Identifier.tryParse(key) != null;
    }

    public static List<ResourceKey<Level>> getDimensions() {
        return DIMENSIONS_WITH_DISABLED_ROCKETS.get().stream().map(Identifier::tryParse).filter(Objects::nonNull).map(key -> ResourceKey.create(Registries.DIMENSION, key)).toList();
    }
}
