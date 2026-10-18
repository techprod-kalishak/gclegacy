package io.kalishak.galacticraftlegacy.components.flag;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record FlagMaterial(FlagTypeProvider flagProvider, Component description) {
    public static final Codec<FlagMaterial> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            FlagTypeProviders.CODEC.fieldOf("flag_provider").forGetter(FlagMaterial::flagProvider),
            ComponentSerialization.CODEC.optionalFieldOf("team_name", Component.empty()).forGetter(FlagMaterial::description)
    ).apply(instance, FlagMaterial::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, FlagMaterial> DIRECT_STREAM_CODEC = StreamCodec.composite(
            FlagTypeProviders.STREAM_CODEC, FlagMaterial::flagProvider,
            ComponentSerialization.STREAM_CODEC, FlagMaterial::description,
            FlagMaterial::new
    );
    public static final Codec<Holder<FlagMaterial>> CODEC = RegistryCodecs.holder(GalacticraftRegistries.Keys.FLAG_MATERIAL);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<FlagMaterial>> STREAM_CODEC = ByteBufCodecs.holder(
            GalacticraftRegistries.Keys.FLAG_MATERIAL, DIRECT_STREAM_CODEC
    );
    public static final FlagMaterial EMPTY = new FlagMaterial(new BlankFlagProvider(1, 1, 1), Component.empty());
}
