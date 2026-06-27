/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.inventory;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.codec.SerializableEnum;
import io.kalishak.galacticraftlegacy.transfer.capability.item.MappedItemResourceHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.EnumMap;
import java.util.Objects;

public class MappedEquipment<E extends Enum<E> & StringRepresentable> {
    public static <E extends Enum<E>> Codec<EnumMap<E, ItemStack>> codec(Class<E> enumClass, Codec<E> enumCodec) {
        return Codec.unboundedMap(enumCodec, ItemStack.OPTIONAL_CODEC).xmap(map -> {
            EnumMap<E, ItemStack> items = new EnumMap<>(enumClass);
            items.putAll(map);
            return items;
        }, entries -> {
            EnumMap<E, ItemStack> items = new EnumMap<>(entries);
            items.values().removeIf(ItemStack::isEmpty);
            return items;
        });
    }

    public static <E extends Enum<E>> StreamCodec<RegistryFriendlyByteBuf, EnumMap<E, ItemStack>> streamCodec(Class<E> enumClass, StreamCodec<ByteBuf, E> enumStreamCodec) {
        return ByteBufCodecs.map(_ -> new EnumMap<>(enumClass), enumStreamCodec, ItemStack.OPTIONAL_STREAM_CODEC);
    }

    protected final EnumMap<E, ItemStack> items;

    protected MappedEquipment(EnumMap<E, ItemStack> items) {
        this.items = items;
    }

    public static <E extends Enum<E> & SerializableEnum> MappedItemResourceHandler<E> of(MappedEquipment<E> equipment, Class<E> clazz, Codec<E> enumCodec) {
        return new MappedItemResourceHandler<>(equipment.items, clazz, enumCodec);
    }

    public ItemStack set(E slot, ItemStack stack) {
        return Objects.requireNonNullElse(this.items.put(slot, stack), ItemStack.EMPTY);
    }

    public ItemStack get(E equipmentSlot) {
        return this.items.getOrDefault(equipmentSlot, ItemStack.EMPTY);
    }

    public boolean isEmpty() {
        for (ItemStack stack : this.items.values()) {
            if (!stack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public void tick(Entity owner) {
        for (ItemStack stack : this.items.values()) {
            if (!stack.isEmpty()) {
                stack.inventoryTick(owner.level(), owner, null);
            }
        }
    }

    public void setAll(MappedEquipment<E> equipment) {
        this.items.clear();
        this.items.putAll(equipment.items);
    }

    public void dropAll(LivingEntity dropper) {
        for (ItemStack stack : this.items.values()) {
            dropper.drop(stack, true, false);
        }

        clear();
    }

    public void clear() {
        this.items.replaceAll((_, _) -> ItemStack.EMPTY);
    }
}
