/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.network.payload;

import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.Optional;

public record OpenGalaxySelectionScreenPayload(boolean canTransit, Optional<FeatureTier> featureTier) implements CustomPacketPayload {
    public static final Type<OpenGalaxySelectionScreenPayload> TYPE = new Type<>(Constants.id("open_galaxy_selection_screen"));
    public static final StreamCodec<ByteBuf, OpenGalaxySelectionScreenPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, OpenGalaxySelectionScreenPayload::canTransit,
            FeatureTier.STREAM_CODEC.apply(ByteBufCodecs::optional), OpenGalaxySelectionScreenPayload::featureTier,
            OpenGalaxySelectionScreenPayload::new
    );

    public OpenGalaxySelectionScreenPayload(FeatureTier featureTier) {
        this(true, Optional.of(featureTier));
    }

    @Override
    public Type<OpenGalaxySelectionScreenPayload> type() {
        return TYPE;
    }
}
