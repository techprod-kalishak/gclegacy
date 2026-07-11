/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.registry.deferred;

import net.minecraft.core.registries.Registries;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ColorCollection;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.WeatheringCopperCollection;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.commons.lang3.function.TriFunction;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class DeferredBlockRegister extends DeferredRegister.Blocks {
    protected DeferredBlockRegister(String namespace) {
        super(namespace);
    }

    public static DeferredBlockRegister createBlockRegister(String namespace) {
        return new DeferredBlockRegister(namespace);
    }

    public <B extends Block> DeferredBlock<B> registerBlock(ResourceKey<Block> name, Function<BlockBehaviour.Properties, ? extends B> func, Supplier<BlockBehaviour.Properties> properties) {
        return register(name.identifier().getPath(), key -> func.apply(properties.get().setId(ResourceKey.create(Registries.BLOCK, key))));
    }

    public <B extends Block> DeferredBlock<B> registerBlock(BlockItemId id, Function<BlockBehaviour.Properties, ? extends B> func, Supplier<BlockBehaviour.Properties> properties) {
        return registerBlock(id.block(), func, properties);
    }

    public <B extends Block> DeferredBlock<B> registerBlock(ResourceKey<Block> name, Function<BlockBehaviour.Properties, ? extends B> func, UnaryOperator<BlockBehaviour.Properties> properties) {
        return registerBlock(name, func, () -> properties.apply(BlockBehaviour.Properties.of()));
    }

    public <B extends Block> DeferredBlock<B> registerBlock(BlockItemId id, Function<BlockBehaviour.Properties, ? extends B> func, UnaryOperator<BlockBehaviour.Properties> properties) {
        return registerBlock(id.block(), func, properties);
    }

    public DeferredBlock<Block> registerSimpleBlock(ResourceKey<Block> name, Supplier<BlockBehaviour.Properties> properties) {
        return registerBlock(name, Block::new, properties);
    }

    public DeferredBlock<Block> registerSimpleBlock(BlockItemId id, Supplier<BlockBehaviour.Properties> properties) {
        return registerSimpleBlock(id.block(), properties);
    }

    public DeferredBlock<Block> registerSimpleBlock(ResourceKey<Block> name, UnaryOperator<BlockBehaviour.Properties> properties) {
        return registerBlock(name, Block::new, () -> properties.apply(BlockBehaviour.Properties.of()));
    }

    public DeferredBlock<Block> registerSimpleBlock(BlockItemId id, UnaryOperator<BlockBehaviour.Properties> properties) {
        return registerSimpleBlock(id.block(), properties);
    }

    public <WaxedBlock extends Block, WeatheringBlock extends Block & WeatheringCopper> WeatheringCopperCollection<DeferredBlock<Block>> registerBlocks(
            WeatheringCopperCollection<BlockItemId> ids,
            BiFunction<WeatheringCopper.WeatherState, BlockBehaviour.Properties, WaxedBlock> waxedBlockFactory,
            BiFunction<WeatheringCopper.WeatherState, BlockBehaviour.Properties, WeatheringBlock> weatheringFactory,
            Function<WeatheringCopper.WeatherState, BlockBehaviour.Properties> propertiesSupplier
    ) {
        return ids.apply(
                weatheringIds -> WeatheringCopperCollection.zipMap(
                        WeatheringCopperCollection.STATES, weatheringIds, (state, id) -> registerBlock(id.block(), p -> weatheringFactory.apply(state, p), () -> propertiesSupplier.apply(state))
                ),
                waxedIds -> WeatheringCopperCollection.zipMap(WeatheringCopperCollection.STATES, waxedIds, (state, id) -> registerBlock(id.block(), p -> waxedBlockFactory.apply(state, p), () -> propertiesSupplier.apply(state)))
        );
    }

    public <ColoredBlock extends Block> ColorCollection<DeferredBlock<ColoredBlock>> registerColoredBlocks(
            ColorCollection<BlockItemId> ids,
            BiFunction<DyeColor, BlockBehaviour.Properties, ColoredBlock> factory,
            Function<DyeColor, BlockBehaviour.Properties> propertiesSupplier
    ) {
        return ColorCollection.zipMap(ColorCollection.VALUES, ids, (color, id) -> registerBlock(id.block(), prop -> factory.apply(color, prop), () -> propertiesSupplier.apply(color)));
    }
}
