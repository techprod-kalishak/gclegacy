package io.kalishak.galacticraftlegacy.registry.deferred;

import io.kalishak.galacticraftlegacy.world.item.component.ItemWithDescription;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public class DeferredItemRegister extends DeferredRegister.Items {
    public DeferredItemRegister(String namespace) {
        super(namespace);
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
}
