/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.registry.deferred;

import com.google.common.collect.ImmutableBiMap;
import net.minecraft.core.Holder;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.function.Consumer;
import java.util.function.Function;

public record DeferredWeatheringCopperItems(
        DeferredItem<BlockItem> unaffected, DeferredItem<BlockItem> exposed, DeferredItem<BlockItem> weathered, DeferredItem<BlockItem> oxidized,
        DeferredItem<BlockItem> waxed, DeferredItem<BlockItem> waxedExposed, DeferredItem<BlockItem> waxedWeathered, DeferredItem<BlockItem> waxedOxidized) {
    public static DeferredWeatheringCopperItems create(DeferredWeatheringCopperBlocks blocks, Function<Holder<Block>, DeferredItem<BlockItem>> itemFactory) {
        return new DeferredWeatheringCopperItems(
                itemFactory.apply(blocks.unaffected()),
                itemFactory.apply(blocks.exposed()),
                itemFactory.apply(blocks.weathered()),
                itemFactory.apply(blocks.oxidized()),
                itemFactory.apply(blocks.waxed()),
                itemFactory.apply(blocks.waxedExposed()),
                itemFactory.apply(blocks.waxedWeathered()),
                itemFactory.apply(blocks.waxedOxidized())
        );
    }
    
    public ImmutableBiMap<BlockItem, BlockItem> waxedMapping() {
        return ImmutableBiMap.of(this.unaffected.get(), this.waxed.get(), this.exposed.get(), this.waxedExposed.get(), this.weathered.get(), this.waxedWeathered.get(), this.oxidized.get(), this.waxedOxidized.get());
    }
    
    public void forEach(Consumer<BlockItem> consumer) {
        consumer.accept(this.unaffected.get());
        consumer.accept(this.exposed.get());
        consumer.accept(this.weathered.get());
        consumer.accept(this.oxidized.get());
        consumer.accept(this.waxed.get());
        consumer.accept(this.waxedExposed.get());
        consumer.accept(this.waxedWeathered.get());
        consumer.accept(this.waxedOxidized.get());
    }
}
