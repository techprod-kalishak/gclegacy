package io.kalishak.galacticraftlegacy.space.environment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.Constants;
import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.material.Fluid;

import java.util.HashMap;

public interface AtmosphereInfo {
    Codec<AtmosphereInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("is_corrosive").forGetter(AtmosphereInfo::isCorrosive),
            Codec.BOOL.fieldOf("has_precipitation").forGetter(AtmosphereInfo::hasPrecipitation),
            Codec.unboundedMap(ResourceKey.codec(Registries.FLUID), Codec.DOUBLE).xmap(map -> {
                Object2DoubleMap<ResourceKey<Fluid>> gasComposition = new Object2DoubleArrayMap<>();
                gasComposition.putAll(map);
                return gasComposition;
            }, HashMap::new
            ).fieldOf("gas_composition").forGetter(AtmosphereInfo::getGasComposition),
            Codec.FLOAT.fieldOf("temperature").forGetter(AtmosphereInfo::getTemperature)
    ).apply(instance, AtmosphereInfo::create));
    StreamCodec<RegistryFriendlyByteBuf, AtmosphereInfo> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, AtmosphereInfo::isCorrosive,
            ByteBufCodecs.BOOL, AtmosphereInfo::hasPrecipitation,
            ByteBufCodecs.map(Object2DoubleArrayMap::new, ResourceKey.streamCodec(Registries.FLUID), ByteBufCodecs.DOUBLE), AtmosphereInfo::getGasComposition,
            ByteBufCodecs.FLOAT, AtmosphereInfo::getTemperature,
            AtmosphereInfo::create
    );

    static AtmosphereInfo create(boolean isCorrosive, boolean hasPrecipitation, Object2DoubleMap<ResourceKey<Fluid>> gasComposition, float temperature) {
        return new AtmosphereInfoImpl(isCorrosive, hasPrecipitation, gasComposition, temperature);
    }

    AtmosphereInfo EARTH = create(
            false,
            true,
            new Object2DoubleArrayMap<>(new ResourceKey[]{
                    ResourceKey.create(Registries.FLUID, Constants.id("nitrogen")),
                    ResourceKey.create(Registries.FLUID, Constants.id("oxygen")),
                    ResourceKey.create(Registries.FLUID, Constants.id("argon")),
                    ResourceKey.create(Registries.FLUID, Constants.id("carbon_dioxide"))
            }, new double[]{78.08, 20.95, 0.93, 0.04}),
            288.15f
    );

    default boolean isBreathable() {
        double oxygenLevel = getGasComposition().getOrDefault(ResourceKey.create(Registries.FLUID, Constants.id("oxygen")), 0.0);
        return oxygenLevel >= 19.5 && oxygenLevel <= 23.5 && !isCorrosive();
    }

    boolean isCorrosive();
    boolean hasPrecipitation();
    Object2DoubleMap<ResourceKey<Fluid>> getGasComposition();
    float getTemperature();
}
