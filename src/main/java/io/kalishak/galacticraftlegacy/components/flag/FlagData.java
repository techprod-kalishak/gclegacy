package io.kalishak.galacticraftlegacy.components.flag;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.function.Consumer;

public sealed interface FlagData extends TooltipProvider permits FixedFlagData, TeamFlagData {
    Codec<Either<FixedFlagData, TeamFlagData>> DISPATCH_CODEC = Codec.either(FixedFlagData.MAP_CODEC.codec(), TeamFlagData.MAP_CODEC.codec());
    MapCodec<FlagData> DATA_CODEC = DISPATCH_CODEC.dispatchMap(
            flagData -> switch (flagData) {
                case FixedFlagData fixed -> Either.left(fixed);
                case TeamFlagData team -> Either.right(team);
                case null -> throw new IllegalArgumentException("FlagData expected either Fixed or Team, got none");
            },
            either -> {
                if (either.left().isPresent()) {
                    return FixedFlagData.MAP_CODEC;
                } else if (either.right().isPresent()) {
                    return TeamFlagData.MAP_CODEC;
                }

                throw new IllegalArgumentException("FlagData expected either Fixed or Team, got none");
            }
    );
    Codec<FlagData> CODEC = DATA_CODEC.codec();
    StreamCodec<RegistryFriendlyByteBuf, Either<FixedFlagData, TeamFlagData>> DISPATCH_STREAM_CODEC = ByteBufCodecs.either(FixedFlagData.STREAM_CODEC, TeamFlagData.STREAM_CODEC);
    StreamCodec<RegistryFriendlyByteBuf, FlagData> DATA_STREAM_CODEC = DISPATCH_STREAM_CODEC.dispatch(
            flagData -> switch (flagData) {
                case FixedFlagData fixed -> Either.left(fixed);
                case TeamFlagData team -> Either.right(team);
                case null -> throw new IllegalArgumentException("FlagData expected either Fixed or Team, got none");
            },
            either -> {
                if (either.left().isPresent()) {
                    return FixedFlagData.STREAM_CODEC;
                } else if (either.right().isPresent()) {
                    return TeamFlagData.STREAM_CODEC;
                }

                throw new IllegalArgumentException("FlagData expected either Fixed or Team, got none");
            }
    );

    Holder<FlagMaterial> material();
    Component description();
    int width();
    int height();

    @Override
    default void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag flag, DataComponentGetter componentGetter) {
        if (!description().equals(CommonComponents.EMPTY)) {
            tooltipAdder.accept(GalacticraftComponents.TOOLTIP_FLAG.append(description()));
        }
    }

    static FlagData classic(RegistryAccess registryAccess) {
        return new FixedFlagData(registryAccess.getOrThrow(BuiltinFlagMaterials.CLASSIC));
    }
}
