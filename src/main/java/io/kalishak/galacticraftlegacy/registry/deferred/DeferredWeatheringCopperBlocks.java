/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.registry.deferred;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public record DeferredWeatheringCopperBlocks(
        DeferredBlock<Block> unaffected, DeferredBlock<Block> exposed, DeferredBlock<Block> weathered, DeferredBlock<Block> oxidized,
        DeferredBlock<Block> waxed, DeferredBlock<Block> waxedExposed, DeferredBlock<Block> waxedWeathered, DeferredBlock<Block> waxedOxidized) {
    public static <WaxedBlock extends Block, WeatheringBlock extends Block & WeatheringCopper> DeferredWeatheringCopperBlocks create(
            String baseName,
            ContentFactory factory,
            Function<BlockBehaviour.Properties, WaxedBlock> waxedFactory,
            BiFunction<WeatheringCopper.WeatherState, BlockBehaviour.Properties, WeatheringBlock> weatheringFactory,
            Function<WeatheringCopper.WeatherState, BlockBehaviour.Properties> properties
    ) {
        DeferredBlock<Block> unaffected = factory.create(baseName, prop -> weatheringFactory.apply(WeatheringCopper.WeatherState.UNAFFECTED, prop), () -> properties.apply(WeatheringCopper.WeatherState.UNAFFECTED));
        DeferredBlock<Block> exposed = factory.create("exposed_" + baseName, prop -> weatheringFactory.apply(WeatheringCopper.WeatherState.EXPOSED, prop), () -> properties.apply(WeatheringCopper.WeatherState.EXPOSED));
        DeferredBlock<Block> weathered = factory.create("weathered_" + baseName, prop -> weatheringFactory.apply(WeatheringCopper.WeatherState.WEATHERED, prop), () -> properties.apply(WeatheringCopper.WeatherState.WEATHERED));
        DeferredBlock<Block> oxidized = factory.create("oxidized_" + baseName, prop -> weatheringFactory.apply(WeatheringCopper.WeatherState.OXIDIZED, prop), () -> properties.apply(WeatheringCopper.WeatherState.OXIDIZED));
        DeferredBlock<Block> waxed = factory.create("waxed_" + baseName, waxedFactory::apply, () -> properties.apply(WeatheringCopper.WeatherState.UNAFFECTED));
        DeferredBlock<Block> waxedExposed = factory.create("waxed_exposed_" + baseName, waxedFactory::apply, () -> properties.apply(WeatheringCopper.WeatherState.EXPOSED));
        DeferredBlock<Block> waxedWeathered = factory.create("waxed_weathered_" + baseName, waxedFactory::apply, () -> properties.apply(WeatheringCopper.WeatherState.WEATHERED));
        DeferredBlock<Block> waxedOxidized = factory.create("waxed_oxidized_" + baseName, waxedFactory::apply, () -> properties.apply(WeatheringCopper.WeatherState.OXIDIZED));

        return new DeferredWeatheringCopperBlocks(
                unaffected, exposed, weathered, oxidized,
                waxed, waxedExposed, waxedWeathered, waxedOxidized
        );
    }

    public void forEach(Consumer<Block> consumer) {
        consumer.accept(this.unaffected.get());
        consumer.accept(this.exposed.get());
        consumer.accept(this.weathered.get());
        consumer.accept(this.oxidized.get());
        consumer.accept(this.waxed.get());
        consumer.accept(this.waxedExposed.get());
        consumer.accept(this.waxedWeathered.get());
        consumer.accept(this.waxedOxidized.get());
    }

    public ImmutableBiMap<Holder<Block>, Holder<Block>> weatheringMapping() {
        return ImmutableBiMap.of(this.unaffected, this.exposed, this.exposed, this.weathered, this.weathered, this.oxidized);
    }

    public ImmutableBiMap<Block, Block> waxedMapping() {
        return ImmutableBiMap.of(this.unaffected.get(), this.waxed.get(), this.exposed.get(), this.waxedExposed.get(), this.weathered.get(), this.waxedWeathered.get(), this.oxidized.get(), this.waxedOxidized.get());
    }

    public ImmutableList<DeferredBlock<Block>> asList() {
        return ImmutableList.of(this.unaffected, this.waxed, this.exposed, this.waxedExposed, this.weathered, this.waxedWeathered, this.oxidized, this.waxedOxidized);
    }

    public interface ContentFactory {
        DeferredBlock<Block> create(String id, Function<BlockBehaviour.Properties, Block> blockGetter, Supplier<BlockBehaviour.Properties> properties);
    }
}
