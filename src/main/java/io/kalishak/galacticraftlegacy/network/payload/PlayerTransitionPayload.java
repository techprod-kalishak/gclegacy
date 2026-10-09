package io.kalishak.galacticraftlegacy.network.payload;

import io.kalishak.galacticraftlegacy.galaxies.celestial.CelestialBodyInstance;
import io.kalishak.galacticraftlegacy.references.Constants;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record PlayerTransitionPayload(CelestialBodyInstance celestialBody) implements CustomPacketPayload {
    public static final Type<PlayerTransitionPayload> TYPE = new Type<>(Constants.id("player_transition"));
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerTransitionPayload> STREAM_CODEC = CelestialBodyInstance.STREAM_CODEC
            .map(PlayerTransitionPayload::new, PlayerTransitionPayload::celestialBody);

    @Override
    public Type<PlayerTransitionPayload> type() {
        return TYPE;
    }
}
