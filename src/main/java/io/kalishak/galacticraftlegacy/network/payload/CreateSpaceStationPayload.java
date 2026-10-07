package io.kalishak.galacticraftlegacy.network.payload;

import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.world.level.dimension.SpaceStationRecipe;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record CreateSpaceStationPayload(Holder<SpaceStationRecipe> recipe) implements CustomPacketPayload {
    public static final Type<CreateSpaceStationPayload> TYPE = new Type<>(Constants.id("create_space_station"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CreateSpaceStationPayload> STREAM_CODEC = SpaceStationRecipe.STREAM_CODEC.map(CreateSpaceStationPayload::new, CreateSpaceStationPayload::recipe);

    @Override
    public Type<CreateSpaceStationPayload> type() {
        return TYPE;
    }
}
