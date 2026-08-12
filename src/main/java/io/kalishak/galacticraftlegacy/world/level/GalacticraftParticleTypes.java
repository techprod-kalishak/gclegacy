/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level;

import io.kalishak.galacticraftlegacy.Galacticraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GalacticraftParticleTypes {
    private static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(Registries.PARTICLE_TYPE, Galacticraft.MODID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DRIPPING_OIL = REGISTRY.register(
            "dripping_oil",
            () -> new SimpleParticleType(false)
    );
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SPARKS = REGISTRY.register(
            "sparks",
            () -> new SimpleParticleType(false)
    );
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> LAUNCH_FLAME = REGISTRY.register(
            "launch_flame",
            () -> new SimpleParticleType(false)
    );
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLYING_FLAME = REGISTRY.register(
            "flying_flame",
            () -> new SimpleParticleType(false)
    );

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
