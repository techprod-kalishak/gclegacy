/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.transfer.capability.item;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.codec.SerializableEnum;
import io.kalishak.galacticraftlegacy.world.inventory.MappedEquipment;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemResource;

import java.util.EnumMap;

public class MappedItemResourceHandler<E extends Enum<E> & StringRepresentable> extends MappedResourceHandler<ItemStack, ItemResource, E> {
    public MappedItemResourceHandler(Class<E> enumClass, Codec<E> enumCodec) {
        super(enumClass, ItemStack.EMPTY, MappedEquipment.codec(enumClass, enumCodec));
    }

    public MappedItemResourceHandler(EnumMap<E, ItemStack> stacks, Class<E> enumClass, Codec<E> enumCodec) {
        super(stacks, enumClass, ItemStack.EMPTY, MappedEquipment.codec(enumClass, enumCodec));
    }

    @Override
    protected ItemResource getResource(ItemStack stack) {
        return ItemResource.of(stack);
    }

    @Override
    protected int getAmount(ItemStack stack) {
        return stack.getCount();
    }

    @Override
    protected ItemStack getStack(ItemResource resource, int amount) {
        return resource.toStack(amount);
    }

    @Override
    protected ItemStack copyOf(ItemStack stack) {
        return stack.copy();
    }

    @Override
    protected int getCapacity(E entry, ItemResource resource) {
        Integer capacity = resource.get(DataComponents.MAX_STACK_SIZE);
        return capacity == null ? 64 : capacity;
    }
}
