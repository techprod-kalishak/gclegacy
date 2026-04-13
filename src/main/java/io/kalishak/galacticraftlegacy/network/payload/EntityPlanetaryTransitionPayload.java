package io.kalishak.galacticraftlegacy.network.payload;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.world.entity.vehicle.rocket.AbstractAutoRocket;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.UUID;

public record EntityPlanetaryTransitionPayload(UUID entityId, ResourceKey<Level> destination, boolean transferItems, Optional<UUID> rocketId) implements CustomPacketPayload {
    public static final Type<EntityPlanetaryTransitionPayload> TYPE = new Type<>(Constants.id("entity_planetary_transition"));
    public static final StreamCodec<RegistryFriendlyByteBuf, EntityPlanetaryTransitionPayload> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, EntityPlanetaryTransitionPayload::entityId,
            ResourceKey.streamCodec(Registries.DIMENSION), EntityPlanetaryTransitionPayload::destination,
            ByteBufCodecs.BOOL, EntityPlanetaryTransitionPayload::transferItems,
            UUIDUtil.STREAM_CODEC.apply(ByteBufCodecs::optional), EntityPlanetaryTransitionPayload::rocketId,
            EntityPlanetaryTransitionPayload::new
    );

    public static EntityPlanetaryTransitionPayload player(Player player, ResourceKey<Level> destination, boolean transferItems, AbstractAutoRocket rocket) {
        return new EntityPlanetaryTransitionPayload(
                player.getUUID(),
                destination,
                transferItems,
                Optional.of(rocket.getUUID())
        );
    }

    public static EntityPlanetaryTransitionPayload rocket(AbstractAutoRocket rocket, ResourceKey<Level> destination, boolean transferItems) {
        return new EntityPlanetaryTransitionPayload(
                rocket.getUUID(),
                destination,
                transferItems,
                Optional.empty()
        );
    }

    @Override
    public Type<EntityPlanetaryTransitionPayload> type() {
        return TYPE;
    }
}
