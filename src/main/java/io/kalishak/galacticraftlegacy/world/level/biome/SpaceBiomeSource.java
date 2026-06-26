package io.kalishak.galacticraftlegacy.world.level.biome;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;

import java.util.List;
import java.util.function.Function;

public class SpaceBiomeSource {
    public static Climate.ParameterList<Holder<Biome>> moonBiomes(Function<ResourceKey<Biome>,? extends Holder<Biome>> lookup) {
        return new Climate.ParameterList<>(
                List.of(
                        Pair.of(
                                Climate.parameters(-0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F),
                                lookup.apply(MoonBiomes.MOON_PLAINS)
                        )
                )
        );
    }

    public interface BiomeResolver<T> {
        Climate.ParameterList<Holder<T>> create(Function<ResourceKey<Biome>, ? extends Holder<T>> lookup);
    }
}
