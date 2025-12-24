package io.kalishak.galacticraftlegacy.space.environment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.world.inventory.slot.GearSlot;
import it.unimi.dsi.fastutil.doubles.Double2ObjectArrayMap;
import it.unimi.dsi.fastutil.doubles.Double2ObjectFunction;
import it.unimi.dsi.fastutil.doubles.Double2ObjectMap;
import it.unimi.dsi.fastutil.doubles.Double2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.material.Fluid;

public interface AtmosphereInfo {
    Codec<AtmosphereInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("is_corrosive").forGetter(AtmosphereInfo::isCorrosive),
            Codec.BOOL.fieldOf("has_precipitation").forGetter(AtmosphereInfo::hasPrecipitation),
            Codec.unboundedMap(Codec.DOUBLE, ResourceKey.codec(Registries.FLUID)).xmap(Double2ObjectOpenHashMap::new, Object2ObjectOpenHashMap::new).fieldOf("gas_composition").forGetter(AtmosphereInfo::getGasComposition),
            Codec.FLOAT.fieldOf("temperature").forGetter(AtmosphereInfo::getTemperature)
    ).apply(instance, AtmosphereInfo::create));

    static AtmosphereInfo create(boolean isCorrosive, boolean hasPrecipitation, Double2ObjectOpenHashMap<ResourceKey<Fluid>> gasComposition, float temperature) {
        return new AtmosphereInfoImpl(isCorrosive, hasPrecipitation, gasComposition, temperature);
    }

    boolean isCorrosive();
    boolean hasPrecipitation();
    Double2ObjectOpenHashMap<ResourceKey<Fluid>> getGasComposition();
    float getTemperature();
}
