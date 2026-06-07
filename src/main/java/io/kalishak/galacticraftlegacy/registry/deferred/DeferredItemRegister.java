/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.registry.deferred;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.world.item.component.ItemWithDescription;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class DeferredItemRegister extends DeferredRegister.Items {
    public DeferredItemRegister(String namespace) {
        super(namespace);
    }

    public <I extends BlockItem> DeferredItem<I> registerBlockItem(Holder<Block> standingBlock, BiFunction<Block, Item.Properties, I> getter, Supplier<Item.Properties> properties) {
        String name = standingBlock.unwrapKey().orElseThrow().identifier().getPath();
        Objects.requireNonNull(standingBlock);
        return register(name, () -> getter.apply(standingBlock.value(), properties.get().setId(Constants.key(Registries.ITEM, name)).useBlockDescriptionPrefix()));
    }

    public <I extends BlockItem> DeferredItem<I> registerBlockItem(Holder<Block> standingBlock, BiFunction<Block, Item.Properties, I> getter, UnaryOperator<Item.Properties> properties) {
        return registerBlockItem(standingBlock, getter, () -> properties.apply(new Item.Properties()));
    }

    public <I extends BlockItem> DeferredItem<I> registerBlockItem(Holder<Block> standingBlock, BiFunction<Block, Item.Properties, I> getter) {
        return registerBlockItem(standingBlock, getter, UnaryOperator.identity());
    }

    public DeferredItem<BlockItem> registerSimpleBlockItemWithDescription(Holder<Block> block, Supplier<Item.Properties> properties) {
        Identifier id = block.unwrapKey().orElseThrow().identifier();

        return registerSimpleBlockItem(id.getPath(), block::value, ItemWithDescription.withDescription(properties, id));
    }

    public DeferredItem<BlockItem> registerSimpleBlockItemWithDescription(Holder<Block> block) {
        return registerSimpleBlockItemWithDescription(block, Item.Properties::new);
    }

    public <I extends Item> DeferredItem<I> registerItemWithDescription(String name, Function<Item.Properties, ? extends I> getter, Supplier<Item.Properties> properties) {
        return registerItem(name, getter, ItemWithDescription.withDescription(properties, Identifier.fromNamespaceAndPath(getNamespace(), name)));
    }

    public DeferredItem<Item> registerSimpleItemWithDescription(String name, Supplier<Item.Properties> properties) {
        return registerSimpleItem(name, ItemWithDescription.withDescription(properties, Identifier.fromNamespaceAndPath(getNamespace(), name)));
    }

    public DeferredItem<Item> registerSimpleItemWithDescription(String name, UnaryOperator<Item.Properties> properties) {
        return registerSimpleItemWithDescription(name, () -> properties.apply(new Item.Properties()));
    }
}
