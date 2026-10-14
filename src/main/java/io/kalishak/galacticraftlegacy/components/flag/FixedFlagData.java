package io.kalishak.galacticraftlegacy.components.flag;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;

public record FixedFlagData(Holder<FlagMaterial> material) implements FlagData {
    static final MapCodec<FixedFlagData> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            FlagMaterial.CODEC.fieldOf("material").forGetter(FixedFlagData::material)
    ).apply(instance, FixedFlagData::new));
    static final StreamCodec<RegistryFriendlyByteBuf, FixedFlagData> STREAM_CODEC = FlagMaterial.STREAM_CODEC.map(FixedFlagData::new, FixedFlagData::material);

    @Override
    public Component description() {
        return this.material.value().description();
    }

    @Override
    public int width() {
        return this.material.value().flagProvider().width();
    }

    @Override
    public int height() {
        return this.material.value().flagProvider().height();
    }
}
