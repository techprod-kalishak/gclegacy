/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ServerConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue WORLD_BORDERS = BUILDER
            .push("general")
            .comment("Set this to 0 for no borders (default).  If set to e.g. 2000, players will land on the Moon inside the x,z range -2000 to 2000.)")
            .translation("galacticraftlegacy.configgui.server.world_borders")
            .worldRestart()
            .defineInRange("worldBorders", 0, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.BooleanValue SPACE_STATIONS_PERMISSIONS = BUILDER
            .comment("While true, space stations require you to invite other Players using /spacerace invite <playername>")
            .translation("galacticraftlegacy.configgui.server.space_stations_permissions")
            .define("spaceStationsPermissions", true);
    public static final ModConfigSpec.BooleanValue DISABLE_SPACE_STATIONS = BUILDER
            .comment("If set to true on a server, players will be completely unable to create space stations.")
            .translation("galacticraftlegacy.configgui.server.disable_space_station_creation")
            .worldRestart()
            .define("disableSpaceStationCreation", false);
    public static final ModConfigSpec.BooleanValue OVERRIDE_DONOR_CAPES = BUILDER
            .comment("By default, Galacticraft will override capes with the mod's donor cape. Set to false to disable.")
            .translation("galacticraftlegacy.configgui.server.override_capes")
            .worldRestart()
            .define("overrideDonorCapes", true);
    public static final ModConfigSpec.BooleanValue SEAL_EDGE_CHECK = BUILDER
            .comment("If this is enabled, areas sealed by Oxygen Sealers will run a seal check when the player breaks or places a block (or on block updates).", "This should be enabled for a 100% accurate sealed status, but can be disabled on servers for performance reasons.")
            .translation("galacticraftlegacy.configgui.server.seal_edge_check")
            .define("sealEdgeCheck", true);

    public static final ModConfigSpec.BooleanValue DISABLE_SPACESHIP_EXPLOSION = BUILDER
            .pop()
            .push("difficulty")
            .comment("Spaceships will not explode on contact if set to true.")
            .translation("galacticraftlegacy.configgui.server.disable_spaceship_explosion")
            .define("disableSpaceshipExplosion", false);
    public static final ModConfigSpec.BooleanValue DISABLE_METEOR_EXPLOSION = BUILDER
            .comment("Set to false to stop meteors from breaking blocks on contact.")
            .translation("galacticraftlegacy.configgui.server.disable_meteor_block_breaking")
            .define("disableMeteorBlockBreaking", false);
    public static final ModConfigSpec.DoubleValue METEOR_SPAWN_MULTIPLIER = BUILDER
            .comment("Set to a value between 0.0 and 1.0 to decrease meteor spawn chance (all dimensions).")
            .translation("galacticraftlegacy.configgui.server.meteor_spawn_multiplier")
            .worldRestart()
            .defineInRange("meteorSpawnMultiplier", 1.0D, 0.0D, 1.0D);
    public static final ModConfigSpec.DoubleValue ENERGY_MULTIPLIER = BUILDER
            .comment("Solar panels will work (default 2x) more effective on space stations.")
            .translation("galacticraftlegacy.configgui.server.solar_energy_multiplier")
            .worldRestart()
            .defineInRange("spaceStationSolarEnergyMultiplier", 2.0D, 1.0D, Double.MAX_VALUE);
    public static final ModConfigSpec.DoubleValue FUEL_USAGE_MULTIPLIER = BUILDER
            .comment("The normal factor is 1. Increase this to 2 - 5 if other mods with a lot of oil (e.g. BuildCraft) are installed to increase GC rocket fuel requirement.")
            .translation("galacticraftlegacy.configgui.server.fuel_usage_multiplier")
            .worldRestart()
            .defineInRange("fuelUsageMultiplier", 1.0D, 0.1D, 10.0D);
    public static final ModConfigSpec.BooleanValue QUICK_GAME = BUILDER
            .comment("Set this to true for less metal use in Galacticraft recipes (makes the game easier!)")
            .translation("galacticraftlegacy.configgui.server.quick_mode")
            .worldRestart()
            .define("quickMode", false);
    public static final ModConfigSpec.BooleanValue HARD_MODE = BUILDER
            .comment("Set this to true for increased difficulty in modpacks.")
            .translation("galacticraftlegacy.configgui.server.hard_mode")
            .worldRestart()
            .define("hardMode", false);
    public static final ModConfigSpec.BooleanValue ADVENTURE_MODE = BUILDER
            .comment("Set this to true for a challenging adventure where the player starts the game stranded in the Asteroids dimension with low resources")
            .translation("galacticraftlegacy.configgui.server.adventure_mode")
            .worldRestart()
            .define("adventureMode", false);
    public static final ModConfigSpec.IntValue ADVENTURE_MODE_FLAGS = BUILDER
            .comment(
                    "Add together flags 8, 4, 2, 1 to enable the four elements of adventure game mode. The default is 15",
                    "1 = extended compressor recipes.",
                    "2 = mob drops and spawning.",
                    "4 = more trees in hollow asteroids.",
                    "8 = start stranded in Asteroids."
            ).translation("galacticraftlegacy.configgui.server.adventure_mode_flags")
            .worldRestart()
            .defineInRange("adventureModeFlags", 15, 1, 15);
    public static final ModConfigSpec.IntValue SUFFOCATION_COOLDOWN = BUILDER
            .pop()
            .push("entities")
            .comment("Lower/Raise this value to change time between suffocation damage ticks.")
            .translation("galacticraftlegacy.configgui.server.suffocation_cooldown")
            .defineInRange("suffocationCooldown", 100, 50, 250);
    public static final ModConfigSpec.IntValue SUFFOCATION_DAMAGE = BUILDER
            .comment("Change this value to modify the damage taken per suffocation tick")
            .translation("galacticraftlegacy.configgui.server.suffocation_damage")
            .defineInRange("suffocationCooldown", 2, 1, 20);
    public static final ModConfigSpec.DoubleValue BOSS_HEALTH_MODIFIER = BUILDER
            .comment("Change this if you wish to balance the mod (if you have more powerful weapon mods).")
            .translation("galacticraftlegacy.configgui.server.boss_health_modifier")
            .defineInRange("bossHealthModifier", 1.0D, 0.1D, 10.0D);

    static final ModConfigSpec SPEC = BUILDER.pop().build();
}
