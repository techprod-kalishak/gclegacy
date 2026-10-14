package io.kalishak.galacticraftlegacy.components.flag;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;

public record TeamFlagData(MutableFlagTypeProvider provider, Component description) implements FlagData {
    static final MapCodec<TeamFlagData> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            MutableFlagTypeProvider.MAP_CODEC.forGetter(TeamFlagData::provider),
            ComponentSerialization.CODEC.fieldOf("description").forGetter(TeamFlagData::description)
    ).apply(instance, TeamFlagData::new));
    static final StreamCodec<RegistryFriendlyByteBuf, TeamFlagData> STREAM_CODEC = StreamCodec.composite(
            MutableFlagTypeProvider.STREAM_CODEC, TeamFlagData::provider,
            ComponentSerialization.TRUSTED_STREAM_CODEC, TeamFlagData::description,
            TeamFlagData::new
    );
    public static final TeamFlagData DEFAULT = new TeamFlagData(MutableFlagTypeProvider.DEFAULT, Component.empty());

    @Override
    public Holder<FlagMaterial> material() {
        return Holder.direct(new FlagMaterial(this.provider, this.description));
    }

    @Override
    public int width() {
        return this.provider.width();
    }

    @Override
    public int height() {
        return this.provider.height();
    }
}
