/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.config;

import io.kalishak.galacticraftlegacy.config.values.GameDifficulty;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ServerConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue WORLD_BORDERS = BUILDER
            .push("general")
            .comment("Set this to 0 for no borders (default).  If set to e.g. 2000, players will land on the Moon inside the x,z range -2000 to 2000.)")
            .translation(GalacticraftComponents.CONFIG_WORLD_BORDERS)
            .worldRestart()
            .defineInRange("worldBorders", 0, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.BooleanValue SPACE_STATIONS_PERMISSIONS = BUILDER
            .comment("While true, space stations require you to invite other Players using /spacerace invite <playername>")
            .translation(GalacticraftComponents.CONFIG_STATION_PERMISSIONS)
            .define("spaceStationsPermissions", true);
    public static final ModConfigSpec.BooleanValue DISABLE_SPACE_STATIONS = BUILDER
            .comment("If set to true on a server, players will be completely unable to create space stations.")
            .translation(GalacticraftComponents.CONFIG_DISABLE_SPACE_STATIONS)
            .worldRestart()
            .define("disableSpaceStationCreation", false);
    public static final ModConfigSpec.BooleanValue OVERRIDE_DONOR_CAPES = BUILDER
            .comment("By default, Galacticraft will override capes with the mod's donor cape. Set to false to disable.")
            .translation(GalacticraftComponents.CONFIG_OVERRIDE_CAPES)
            .worldRestart()
            .define("overrideDonorCapes", true);
    public static final ModConfigSpec.BooleanValue SEAL_EDGE_CHECK = BUILDER
            .comment("If this is enabled, areas sealed by Oxygen Sealers will run a seal check when the playerName breaks or places a block (or on block updates).", "This should be enabled for a 100% accurate sealed status, but can be disabled on servers for performance reasons.")
            .translation(GalacticraftComponents.CONFIG_SEAL_EDGE_CHECK)
            .define("sealEdgeCheck", true);

    public static final ModConfigSpec.BooleanValue DISABLE_SPACESHIP_EXPLOSION = BUILDER
            .pop()
            .push("difficulty")
            .comment("Spaceships will not explode on contact if set to true.")
            .translation(GalacticraftComponents.CONFIG_DISABLE_ROCKET_EXPLOSION)
            .define("disableSpaceshipExplosion", false);
    public static final ModConfigSpec.BooleanValue DISABLE_METEOR_EXPLOSION = BUILDER
            .comment("Set to false to stop meteors from breaking blocks on contact.")
            .translation(GalacticraftComponents.CONFIG_DISABLE_METEOR_GRIEFING)
            .define("disableMeteorBlockBreaking", false);
    public static final ModConfigSpec.DoubleValue METEOR_SPAWN_MULTIPLIER = BUILDER
            .comment("Set to a value between 0.0 and 1.0 to decrease meteor spawn chance (all dimensions).")
            .translation(GalacticraftComponents.CONFIG_METEOR_SPAWN_MULTIPLIER)
            .worldRestart()
            .defineInRange("meteorSpawnMultiplier", 1.0D, 0.0D, 1.0D);
    public static final ModConfigSpec.DoubleValue ENERGY_MULTIPLIER = BUILDER
            .comment("Solar panels will work (default 2x) more effective on space stations.")
            .translation(GalacticraftComponents.CONFIG_SOLAR_ENERGY_MULTIPLIER)
            .worldRestart()
            .defineInRange("spaceStationSolarEnergyMultiplier", 2.0D, 1.0D, Double.MAX_VALUE);
    public static final ModConfigSpec.DoubleValue FUEL_USAGE_MULTIPLIER = BUILDER
            .comment("The normal factor is 1. Increase this to 2 - 5 if other mods with a lot of oil (e.g. BuildCraft) are installed to increase GC rocket fuel requirement.")
            .translation(GalacticraftComponents.CONFIG_FUEL_USAGE_MULTIPLIER)
            .worldRestart()
            .defineInRange("fuelUsageMultiplier", 1.0D, 0.1D, 10.0D);
    public static final ModConfigSpec.EnumValue<GameDifficulty> GAME_DIFFICULTY = BUILDER
            .comment(
                    "Set this to QUICK for less metal use in Galacticraft recipes (makes the game easier!)",
                    "Set this to HARD for increased difficulty in modpacks.",
                    "Set this to ADVENTURE for a challenging adventure where the playerName starts the game stranded in the Asteroids dimension with low resources"
            )
            .translation(GalacticraftComponents.CONFIG_GAMEMODE)
            .worldRestart()
            .defineEnum("gamemode", GameDifficulty.NORMAL);
    public static final ModConfigSpec.IntValue ADVENTURE_MODE_FLAGS = BUILDER
            .comment(
                    "Additional options for the adventure gamemode, these settings will be only applied once the config above will be set to ADVENTURE",
                    "Add together flags 8, 4, 2, 1 to enable the four elements of adventure game mode. The default is 15",
                    "1 = extended compressor recipes.",
                    "2 = mob drops and spawning.",
                    "4 = more trees in hollow asteroids.",
                    "8 = start stranded in Asteroids."
            ).translation(GalacticraftComponents.CONFIG_ADVENTURE_MODE_FLAGS)
            .worldRestart()
            .defineInRange("adventureModeFlags", 15, 1, 15);
    public static final ModConfigSpec.IntValue SUFFOCATION_COOLDOWN = BUILDER
            .pop()
            .push("entities")
            .comment("Lower/Raise this value to change time between suffocation damage ticks.")
            .translation(GalacticraftComponents.CONFIG_SUFFOCATION_COOLDOWN)
            .defineInRange("suffocationCooldown", 100, 50, 250);
    public static final ModConfigSpec.IntValue SUFFOCATION_DAMAGE = BUILDER
            .comment("Change this value to modify the damage taken per suffocation tick")
            .translation(GalacticraftComponents.CONFIG_SUFFOCATION_DAMAGE)
            .defineInRange("suffocationCooldown", 2, 1, 20);
    public static final ModConfigSpec.DoubleValue BOSS_HEALTH_MODIFIER = BUILDER
            .comment("Change this if you wish to balance the mod (if you have more powerful weapon mods).")
            .translation(GalacticraftComponents.CONFIG_BOSS_HEALTH_MODIFIER)
            .defineInRange("bossHealthModifier", 1.0D, 0.1D, 10.0D);

    public static final ModConfigSpec SPEC = BUILDER.pop().build();
}
