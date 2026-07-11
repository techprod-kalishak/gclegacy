/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.references;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;

public class GalacticraftEntityIds {
    public static final ResourceKey<EntityType<?>> ENTRY_POD = create("entry_pod");
    public static final ResourceKey<EntityType<?>> FALLING_PARACHEST = create("falling_parachest");
    public static final ResourceKey<EntityType<?>> FALLEN_METEOR = create("fallen_meteor");
    public static final ResourceKey<EntityType<?>> MOVING_BLOCK = create("no_gravity_moving_block");
    public static final ResourceKey<EntityType<?>> METEOR_CHUNK = create("thrown_meteor_chunk");
    public static final ResourceKey<EntityType<?>> LANDER = create("moon_lander");
    public static final ResourceKey<EntityType<?>> LANDING_BALLOONS = create("landing_balloons");
    public static final ResourceKey<EntityType<?>> T1_ROCKET = create("t1_rocket");
    public static final ResourceKey<EntityType<?>> CARGO_ROCKET = create("cargo_rocket");
    public static final ResourceKey<EntityType<?>> T2_ROCKET = create("t2_rocket");
    public static final ResourceKey<EntityType<?>> T3_ROCKET = create("t3_rocket");
    public static final ResourceKey<EntityType<?>> BUGGY = create("buggy");
    public static final ResourceKey<EntityType<?>> ASTRO_MINER = create("astro_miner");
    public static final ResourceKey<EntityType<?>> FLAG = create("flag");
    public static final ResourceKey<EntityType<?>> SLIMELING = create("slimeling");
    public static final ResourceKey<EntityType<?>> EVOLVED_ZOMBIE = create("evolved_zombie");
    public static final ResourceKey<EntityType<?>> EVOLVED_SKELETON = create("evolved_skeleton");
    public static final ResourceKey<EntityType<?>> EVOLVED_CREEPER = create("evolved_creeper");
    public static final ResourceKey<EntityType<?>> EVOLVED_ENDERMAN = create("evolved_enderman");
    public static final ResourceKey<EntityType<?>> EVOLVED_SPIDER = create("evolved_spider");
    public static final ResourceKey<EntityType<?>> EVOLVED_WITCH = create("evolved_witch");
    public static final ResourceKey<EntityType<?>> EVOLVED_WITHER_SKELETON = create("evolved_wither_skeleton");
    public static final ResourceKey<EntityType<?>> EVOLVED_CREEPER_KING = create("evolved_creeper_king");
    public static final ResourceKey<EntityType<?>> EVOLVED_SPIDER_QUEEN = create("evolved_spider_queen");
    public static final ResourceKey<EntityType<?>> MOON_VILLAGER = create("moon_villager");
    public static final ResourceKey<EntityType<?>> HANGING_SCHEMATIC = create("schematic");
    
    private static ResourceKey<EntityType<?>> create(String name) {
        return Constants.key(Registries.ENTITY_TYPE, name);
    }
}
