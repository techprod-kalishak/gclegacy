package io.kalishak.galacticraftlegacy.components.flag;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public class FlagTypeProviders {
    private static final Codec<Either<BlankFlagProvider, FlagTypeProvider>> BLANK_OR_DISPATCH_CODEC = Codec.either(
            BlankFlagProvider.MAP_CODEC.codec(), Codec.lazyInitialized(GalacticraftRegistries.FLAG_TYPE::byNameCodec).dispatch(FlagTypeProvider::type, Function.identity()
    ));
    public static final Codec<FlagTypeProvider> CODEC = BLANK_OR_DISPATCH_CODEC.xmap(
            either -> either.map(Function.identity(), Function.identity()),
            flagType -> flagType instanceof BlankFlagProvider blank ? Either.left(blank) : Either.right(flagType)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, FlagTypeProvider> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

    private static final DeferredRegister<MapCodec<? extends FlagTypeProvider>> REGISTRY = DeferredRegister.create(GalacticraftRegistries.Keys.FLAG_TYPE, Galacticraft.MODID);

    public static final Supplier<MapCodec<BlankFlagProvider>> BLANK = REGISTRY.register(
            "blank",
            () -> BlankFlagProvider.MAP_CODEC
    );
    public static final Supplier<MapCodec<MutableFlagTypeProvider>> MUTABLE = REGISTRY.register(
            "mutable",
            () -> MutableFlagTypeProvider.MAP_CODEC
    );
    public static final Supplier<MapCodec<TexturedFlagProvider>> TEXTURED = REGISTRY.register(
            "textured",
            () -> TexturedFlagProvider.MAP_CODEC
    );

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
