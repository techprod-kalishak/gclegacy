package io.kalishak.galacticraftlegacy.transfer.capability.item;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.codec.SerializableEnum;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemResource;

import java.util.EnumMap;

public class MappedItemResourceHandler<E extends Enum<E> & SerializableEnum> extends MappedResourceHandler<ItemStack, ItemResource, E> {
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
        return ByteBufCodecs.map(size -> new EnumMap<>(enumClass), enumStreamCodec, ItemStack.OPTIONAL_STREAM_CODEC);
    }

    public MappedItemResourceHandler(Class<E> enumClass, Codec<E> enumCodec) {
        super(enumClass, ItemStack.EMPTY, codec(enumClass, enumCodec));
    }

    protected MappedItemResourceHandler(EnumMap<E, ItemStack> stacks, Class<E> enumClass, Codec<E> enumCodec) {
        super(stacks, enumClass, ItemStack.EMPTY, codec(enumClass, enumCodec));
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
