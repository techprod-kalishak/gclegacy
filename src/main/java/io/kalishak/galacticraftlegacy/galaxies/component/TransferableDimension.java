package io.kalishak.galacticraftlegacy.galaxies.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.dimension.DimensionType;

public record TransferableDimension(Holder<DimensionType> dimensionType, FeatureTier requiredTier) {
    public static final Codec<TransferableDimension> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            DimensionType.CODEC.fieldOf("dimension_type").forGetter(TransferableDimension::dimensionType),
            FeatureTier.CODEC.fieldOf("required_tier").forGetter(TransferableDimension::requiredTier)
    ).apply(instance, TransferableDimension::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, TransferableDimension> STREAM_CODEC = StreamCodec.composite(
            DimensionType.STREAM_CODEC, TransferableDimension::dimensionType,
            FeatureTier.STREAM_CODEC, TransferableDimension::requiredTier,
            TransferableDimension::new
    );
}
