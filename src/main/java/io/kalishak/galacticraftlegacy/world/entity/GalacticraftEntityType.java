/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.entity;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.world.entity.monster.EvolvedSkeleton;
import io.kalishak.galacticraftlegacy.world.entity.monster.EvolvedZombie;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.stream.Stream;

public final class GalacticraftEntityType {
    private static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, Galacticraft.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<FallingParachest>> FALLING_PARACHEST = REGISTRY.register(
            "falling_parachest",
            () -> EntityType.Builder.of(FallingParachest::new, MobCategory.MISC)
                    .noLootTable()
                    .fireImmune()
                    .canSpawnFarFromPlayer()
                    .build(Constants.key(Registries.ENTITY_TYPE, "falling_parachest"))
    );
    public static final DeferredHolder<EntityType<?>, EntityType<Flag>> FLAG = REGISTRY.register(
            "flag",
            () -> EntityType.Builder.<Flag>of(Flag::new, MobCategory.MISC)
                    .noLootTable()
                    .noSummon()
                    .sized(0.4F, 3.0F)
                    .build(Constants.key(Registries.ENTITY_TYPE, "flag"))
    );
    public static final DeferredHolder<EntityType<?>, EntityType<SchematicEntity>> SCHEMATIC = REGISTRY.register(
            "schematic",
            () -> EntityType.Builder.<SchematicEntity>of(SchematicEntity::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .build(Constants.key(Registries.ENTITY_TYPE, "schematic"))
    );

    //Evolved variants
    public static final DeferredHolder<EntityType<?>, EntityType<EvolvedSkeleton>> EVOLVED_SKELETON = REGISTRY.register(
            "evolved_skeleton",
            () -> EntityType.Builder.<EvolvedSkeleton>of(EvolvedSkeleton::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.99F)
                    .eyeHeight(1.74F)
                    .ridingOffset(-0.7F)
                    .clientTrackingRange(8)
                    .notInPeaceful()
                    .build(Constants.key(Registries.ENTITY_TYPE, "evolved_skeleton"))
    );
    public static final DeferredHolder<EntityType<?>, EntityType<EvolvedZombie>> EVOLVED_ZOMBIE = REGISTRY.register(
            "evolved_zombie",
            () -> EntityType.Builder.<EvolvedZombie>of(EvolvedZombie::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .eyeHeight(1.74F)
                    .passengerAttachments(2.0125F)
                    .ridingOffset(-0.7F)
                    .clientTrackingRange(8)
                    .notInPeaceful()
                    .build(Constants.key(Registries.ENTITY_TYPE, "evolved_zombie"))
    );

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
        bus.addListener(GalacticraftEntityType::registerCapabilities);
    }

    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        FallingParachest.registerCapabilities(event);
    }

    public static Stream<EntityType<?>> asStream() {
        return REGISTRY.getEntries().stream().map(DeferredHolder::get);
    }
}
