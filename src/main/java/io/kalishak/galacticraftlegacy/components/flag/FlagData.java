package io.kalishak.galacticraftlegacy.components.flag;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.function.Consumer;

public record FlagData(Either<Holder<FlagMaterial>, MutableFlagTypeProvider> data) implements TooltipProvider {
    public static final MapCodec<FlagData> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.either(FlagMaterial.CODEC, MutableFlagTypeProvider.CODEC).fieldOf("data").forGetter(FlagData::data)
    ).apply(instance, FlagData::new));
    public static final Codec<FlagData> CODEC = MAP_CODEC.codec();
    public static final StreamCodec<RegistryFriendlyByteBuf, FlagData> STREAM_CODEC = ByteBufCodecs.either(
            FlagMaterial.STREAM_CODEC,
            MutableFlagTypeProvider.STREAM_CODEC
    ).map(FlagData::new, FlagData::data);
    public static final FlagData EMPTY = withMutable(MutableFlagTypeProvider.DEFAULT);

    public static FlagData withMaterial(Holder<FlagMaterial> flagMaterial) {
        return new FlagData(Either.left(flagMaterial));
    }

    public static FlagData withMutable(MutableFlagTypeProvider flagTypeProvider) {
        return new FlagData(Either.right(flagTypeProvider));
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
        this.data.ifLeft(flagMaterial -> consumer.accept(flagMaterial.value().description()));
    }

    public FlagTypeProvider extractProvider() {
        Either<FlagTypeProvider, MutableFlagTypeProvider> mapped = this.data.mapLeft(holder -> holder.value().flagProvider());

        if (mapped.right().isPresent()) {
            return mapped.right().get();
        }

        return mapped.orThrow();
    }
}
