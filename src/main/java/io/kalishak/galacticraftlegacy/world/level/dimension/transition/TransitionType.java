/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.dimension.transition;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.codec.SerializableEnum;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public final class TransitionType {
    private static final Codec<Either<EarthPlanetaryTranstion, PlanetaryTransition>> DEFAULT_OR_DISPATCH_CODEC = Codec.either(
            EarthPlanetaryTranstion.CODEC.codec(),
            Codec.lazyInitialized(GalacticraftRegistries.PLANETARY_TRANSITION_TYPE::byNameCodec).dispatch(PlanetaryTransition::codec, Function.identity())
    );
    public static final Codec<PlanetaryTransition> CODEC = DEFAULT_OR_DISPATCH_CODEC.xmap(
            either -> either.map(Function.identity(), Function.identity()),
            planetaryTransition -> planetaryTransition instanceof EarthPlanetaryTranstion earthPlanetaryTranstion ? Either.left(earthPlanetaryTranstion) : Either.right(planetaryTransition)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, PlanetaryTransition> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

    private static final DeferredRegister<MapCodec<? extends PlanetaryTransition>> REGISTRY = DeferredRegister.create(GalacticraftRegistries.Keys.PLANETARY_TRANSITION_TYPE, Galacticraft.MODID);

    public static final DeferredHolder<MapCodec<? extends PlanetaryTransition>, MapCodec<FixedPlanetaryTransition>> FIXED_POSITION = REGISTRY.register(
            "fixed",
            () -> FixedPlanetaryTransition.CODEC
    );
    public static final DeferredHolder<MapCodec<? extends PlanetaryTransition>, MapCodec<LanderPlanetaryTransition>> LANDER = REGISTRY.register(
            "with_lander",
            () -> LanderPlanetaryTransition.CODEC
    );
    public static final DeferredHolder<MapCodec<? extends PlanetaryTransition>, MapCodec<EarthPlanetaryTranstion>> PARACHUTE = REGISTRY.register(
            "with_parachute",
            () -> EarthPlanetaryTranstion.CODEC
    );

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
