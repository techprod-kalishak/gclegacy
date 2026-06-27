/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.dimension.transition;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public abstract class TransitionType<T extends PlanetaryTransition> {
    private static final DeferredRegister<TransitionType<?>> REGISTER = DeferredRegister.create(GalacticraftRegistries.Keys.PLANETARY_TRANSITION_TYPE, Galacticraft.MODID);

    public static final DeferredHolder<TransitionType<?>, TransitionType<FixedPlanetaryTransition>> FIXED_POSITION = REGISTER.register(
            "fixed",
            () -> simple(Constants.id("fixed"))
    );
    public static final DeferredHolder<TransitionType<?>, TransitionType<LanderPlanetaryTransition>> LANDER = REGISTER.register(
            "with_lander",
            () -> simple(Constants.id("with_lander"))
    );
    public static final DeferredHolder<TransitionType<?>, TransitionType<EarthPlanetaryTranstion>> PARACHUTE = REGISTER.register(
            "with_parachute",
            () -> simple(Constants.id("with_parachute"))
    );

    public static void init(IEventBus bus) {
        REGISTER.register(bus);
    }

    public static <T extends PlanetaryTransition> TransitionType<T> simple(Identifier id) {
        return new TransitionType<>() {
            @Override
            public String toString() {
                return id.toString();
            }
        };
    }
}
