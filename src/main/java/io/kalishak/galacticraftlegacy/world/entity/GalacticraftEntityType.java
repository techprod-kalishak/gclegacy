/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.entity;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.world.entity.monster.EvolvedMonster;
import io.kalishak.galacticraftlegacy.world.entity.monster.EvolvedSkeleton;
import io.kalishak.galacticraftlegacy.world.entity.monster.EvolvedZombie;
import io.kalishak.galacticraftlegacy.world.entity.projectile.ThrownMeteorChunk;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jspecify.annotations.NonNull;

import java.util.stream.Stream;

public final class GalacticraftEntityType {
    private static final DeferredRegister.Entities REGISTRY = DeferredRegister.createEntities(Galacticraft.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<FallingParachest>> FALLING_PARACHEST = REGISTRY.registerEntityType(
            "falling_parachest",
            FallingParachest::new,
            MobCategory.MISC,
            builder -> builder
                    .noLootTable()
                    .fireImmune()
                    .canSpawnFarFromPlayer()
    );
    public static final DeferredHolder<EntityType<?>, EntityType<Flag>> FLAG = REGISTRY.registerEntityType(
            "flag",
            Flag::new,
            MobCategory.MISC,
            builder -> builder
                    .noLootTable()
                    .noSummon()
                    .sized(0.4F, 3.0F)
    );
    public static final DeferredHolder<EntityType<?>, EntityType<NoGravityMovingBlockEntity>> NO_GRAVITY_MOVING_BLOCK = REGISTRY.registerEntityType(
            "no_gravity_moving_block",
            NoGravityMovingBlockEntity::new, MobCategory.AMBIENT,
            builder -> builder
                    .noLootTable()
                    .sized(1.0F, 1.0F)
                    .clientTrackingRange(20)
                    .updateInterval(20)
    );
    public static final DeferredHolder<EntityType<?>, EntityType<SchematicEntity>> SCHEMATIC = REGISTRY.registerEntityType(
            "schematic",
            SchematicEntity::new,
            MobCategory.MISC,
            builder -> builder
                    .noLootTable()
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
    );

    //Evolved variants
    public static final DeferredHolder<EntityType<?>, EntityType<EvolvedSkeleton>> EVOLVED_SKELETON = REGISTRY.registerEntityType(
            "evolved_skeleton",
            EvolvedSkeleton::new,
            MobCategory.MONSTER,
            builder -> builder
                    .sized(0.6F, 1.99F)
                    .eyeHeight(1.74F)
                    .ridingOffset(-0.7F)
                    .clientTrackingRange(8)
                    .notInPeaceful()
    );
    public static final DeferredHolder<EntityType<?>, EntityType<EvolvedZombie>> EVOLVED_ZOMBIE = REGISTRY.registerEntityType(
            "evolved_zombie",
            EvolvedZombie::new,
            MobCategory.MONSTER,
            builder -> builder
                    .sized(0.6F, 1.95F)
                    .eyeHeight(1.74F)
                    .passengerAttachments(2.0125F)
                    .ridingOffset(-0.7F)
                    .clientTrackingRange(8)
                    .notInPeaceful()
    );
    public static final DeferredHolder<EntityType<?>, EntityType<ThrownMeteorChunk>> THROWN_METEOR_CHUNK = REGISTRY.registerEntityType(
            "thrown_meteor_chunk",
            ThrownMeteorChunk::new, MobCategory.MISC,
                    builder -> builder
                    .noLootTable()
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
    );

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
        bus.addListener(GalacticraftEntityType::registerCapabilities);
        bus.addListener(GalacticraftEntityType::registerAttributes);
        bus.addListener(GalacticraftEntityType::registerSpawnPlacements);
    }

    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        FallingParachest.registerCapabilities(event);
    }

    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(EVOLVED_SKELETON.get(), EvolvedSkeleton.createAttributes().build());
        event.put(EVOLVED_ZOMBIE.get(), EvolvedZombie.createAttributes().build());
    }

    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(
                EVOLVED_SKELETON.get(),
                SpawnPlacementTypes.NO_RESTRICTIONS,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                withBound(10),
                RegisterSpawnPlacementsEvent.Operation.AND
        );
        event.register(
                EVOLVED_ZOMBIE.get(),
                SpawnPlacementTypes.NO_RESTRICTIONS,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                withBound(15),
                RegisterSpawnPlacementsEvent.Operation.AND
        );
    }

    private static <T extends Entity & EvolvedMonster> SpawnPlacements.SpawnPredicate<@NonNull T> withBound(int bounds) {
        return (type, level, spawnReason, pos, random) -> EvolvedMonster.checkEvolvedSpawnRules(type, level, spawnReason, pos, random, bounds);
    }

    public static Stream<EntityType<?>> asStream() {
        return REGISTRY.getEntries().stream().map(DeferredHolder::get);
    }
}
