/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.entity;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.references.GalacticraftEntityIds;
import io.kalishak.galacticraftlegacy.registry.deferred.DeferredEntityTypeRegister;
import io.kalishak.galacticraftlegacy.world.entity.item.FallenMeteor;
import io.kalishak.galacticraftlegacy.world.entity.monster.EvolvedMonster;
import io.kalishak.galacticraftlegacy.world.entity.monster.EvolvedSkeleton;
import io.kalishak.galacticraftlegacy.world.entity.monster.EvolvedZombie;
import io.kalishak.galacticraftlegacy.world.entity.projectile.ThrownMeteorChunk;
import io.kalishak.galacticraftlegacy.world.entity.vehicle.AstroMiner;
import io.kalishak.galacticraftlegacy.world.entity.vehicle.MoonBuggy;
import io.kalishak.galacticraftlegacy.world.entity.vehicle.lander.EntryPod;
import io.kalishak.galacticraftlegacy.world.entity.vehicle.lander.Lander;
import io.kalishak.galacticraftlegacy.world.entity.vehicle.lander.LandingBalloons;
import io.kalishak.galacticraftlegacy.world.entity.vehicle.rocket.Tier1Rocket;
import io.kalishak.galacticraftlegacy.world.entity.vehicle.rocket.TieredRocket;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jspecify.annotations.NonNull;

import java.util.stream.Stream;

public final class GalacticraftEntityType {
    private static final DeferredEntityTypeRegister REGISTRY = DeferredEntityTypeRegister.createEntities(Galacticraft.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<EntryPod>> ENTRY_POD = REGISTRY.registerEntityType(
            GalacticraftEntityIds.ENTRY_POD,
            EntryPod::new,
            MobCategory.MISC,
            builder -> builder
                    .noLootTable()
                    .fireImmune()
                    .sized(1.5F, 3.0F)
    );
    public static final DeferredHolder<EntityType<?>, EntityType<FallingParachest>> FALLING_PARACHEST = REGISTRY.registerEntityType(
            GalacticraftEntityIds.FALLING_PARACHEST,
            FallingParachest::new,
            MobCategory.MISC,
            builder -> builder
                    .noLootTable()
                    .fireImmune()
                    .canSpawnFarFromPlayer()
    );
    public static final DeferredHolder<EntityType<?>, EntityType<Flag>> FLAG = REGISTRY.registerEntityType(
            GalacticraftEntityIds.FLAG,
            Flag::new,
            MobCategory.MISC,
            builder -> builder
                    .noLootTable()
                    .noSummon()
                    .sized(0.4F, 3.0F)
    );
    public static final DeferredHolder<EntityType<?>, EntityType<FallenMeteor>> FALLEN_METEOR = REGISTRY.registerEntityType(
            GalacticraftEntityIds.FALLEN_METEOR,
            FallenMeteor::new,
            MobCategory.MISC,
            builder -> builder
                    .noLootTable()
                    .clientTrackingRange(90)
                    .fireImmune()
                    .sized(0.9F, 0.9F)
    );
    public static final DeferredHolder<EntityType<?>, EntityType<Lander>> LANDER = REGISTRY.registerEntityType(
            GalacticraftEntityIds.LANDER,
            Lander::new,
            MobCategory.MISC,
            builder -> builder
                    .noLootTable()
                    .fireImmune()
                    .sized(3.0F, 4.25F)
                    .passengerAttachments(2.25F)
    );
    public static final DeferredHolder<EntityType<?>, EntityType<LandingBalloons>> LANDING_BALLOONS = REGISTRY.registerEntityType(
            GalacticraftEntityIds.LANDING_BALLOONS,
            LandingBalloons::new,
            MobCategory.MISC,
            builder -> builder
                    .noLootTable()
                    .fireImmune()
                    .sized(2.0F, 2.0F)
    );
    public static final DeferredHolder<EntityType<?>, EntityType<NoGravityMovingBlockEntity>> NO_GRAVITY_MOVING_BLOCK = REGISTRY.registerEntityType(
            GalacticraftEntityIds.MOVING_BLOCK,
            NoGravityMovingBlockEntity::new, MobCategory.AMBIENT,
            builder -> builder
                    .noLootTable()
                    .sized(1.0F, 1.0F)
                    .clientTrackingRange(20)
                    .updateInterval(20)
    );
    public static final DeferredHolder<EntityType<?>, EntityType<SchematicEntity>> SCHEMATIC = REGISTRY.registerEntityType(
            GalacticraftEntityIds.HANGING_SCHEMATIC.identifier().getPath(),
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
            GalacticraftEntityIds.EVOLVED_SKELETON,
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
            GalacticraftEntityIds.EVOLVED_ZOMBIE,
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

    //Misc
    public static final DeferredHolder<EntityType<?>, EntityType<ThrownMeteorChunk>> THROWN_METEOR_CHUNK = REGISTRY.registerEntityType(
            GalacticraftEntityIds.METEOR_CHUNK,
            ThrownMeteorChunk::new, MobCategory.MISC,
                    builder -> builder
                    .noLootTable()
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
    );
    public static final DeferredHolder<EntityType<?>, EntityType<TieredRocket>> TIER_1_ROCKET = REGISTRY.registerEntityType(
            GalacticraftEntityIds.TIER_1_ROCKET,
            Tier1Rocket::new, MobCategory.MISC,
            builder -> builder
                    .noLootTable()
                    .sized(1.2F, 3.5F)
                    .attach(EntityAttachment.PASSENGER, new Vec3(0.0D, 0.3D, 0.0D))
    );
    public static final DeferredHolder<EntityType<?>, EntityType<MoonBuggy>> BUGGY = REGISTRY.registerEntityType(
            GalacticraftEntityIds.BUGGY,
            MoonBuggy::new, MobCategory.MISC,
            builder -> builder
                    .noLootTable()
                    .sized(0.98F, 4.0F)
                    .attach(EntityAttachment.PASSENGER, new Vec3(0.0D, 0.5D, 0.0D))
    );
    public static final DeferredHolder<EntityType<?>, EntityType<TieredRocket>> TIER_2_ROCKET = REGISTRY.registerEntityType(
            GalacticraftEntityIds.TIER_2_ROCKET,
            Tier1Rocket::new, MobCategory.MISC,
            builder -> builder
                    .noLootTable()
                    .sized(0.98F, 4.0F)
                    .attach(EntityAttachment.PASSENGER, new Vec3(0.0D, 0.5D, 0.0D))
    );
    public static final DeferredHolder<EntityType<?>, EntityType<TieredRocket>> CARGO_ROCKET = REGISTRY.registerEntityType(
            GalacticraftEntityIds.CARGO_ROCKET,
            Tier1Rocket::new, MobCategory.MISC,
            builder -> builder
                    .noLootTable()
                    .sized(0.98F, 4.0F)
    );
    public static final DeferredHolder<EntityType<?>, EntityType<TieredRocket>> TIER_3_ROCKET = REGISTRY.registerEntityType(
            GalacticraftEntityIds.TIER_3_ROCKET,
            Tier1Rocket::new, MobCategory.MISC,
            builder -> builder
                    .noLootTable()
                    .sized(0.98F, 4.0F)
                    .attach(EntityAttachment.PASSENGER, new Vec3(0.0D, 0.5D, 0.0D))
    );
    public static final DeferredHolder<EntityType<?>, EntityType<AstroMiner>> ASTRO_MINER = REGISTRY.registerEntityType(
            GalacticraftEntityIds.ASTRO_MINER,
            AstroMiner::new, MobCategory.MISC,
            builder -> builder
                    .noLootTable()
                    .sized(0.98F, 4.0F)
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
