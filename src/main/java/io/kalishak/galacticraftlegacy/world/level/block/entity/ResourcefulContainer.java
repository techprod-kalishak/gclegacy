package io.kalishak.galacticraftlegacy.world.level.block.entity;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public interface ResourcefulContainer extends Container {
    ResourceHandler<ItemResource> getResourceHandler();

    @Override
    default boolean isEmpty() {
        return ResourceHandlerUtil.isEmpty(getResourceHandler());
    }

    @Override
    default ItemStack getItem(int slot) {
        return ItemUtil.getStack(getResourceHandler(), slot);
    }

    @Override
    default ItemStack removeItem(int slot, int amount) {
        ItemResource inSlot = getResourceHandler().getResource(slot);

        if (!inSlot.isEmpty()) {
            try (Transaction transaction = Transaction.open(null)) {
                int extracted = getResourceHandler().extract(slot, inSlot, amount, transaction);

                if (extracted > 0) {
                    transaction.commit();

                    return inSlot.toStack(amount);
                }
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    default ItemStack removeItemNoUpdate(int slot) {
        return removeItem(slot, getResourceHandler().getAmountAsInt(slot));
    }

    @Override
    default void setItem(int slot, ItemStack stack) {
        if (getResourceHandler() instanceof ItemStacksResourceHandler stacks) {
            stacks.set(slot, ItemResource.of(stack), stack.getCount());
        }
    }

    @Override
    default void setChanged() {

    }

    @Override
    default boolean stillValid(Player player) {
        if (this instanceof BlockEntity blockEntity) {
            return Container.stillValidBlockEntity(blockEntity, player);
        }

        return true;
    }

    @Override
    default void clearContent() {
        for (int i = 0; i < getContainerSize(); i++) {
            setItem(i, ItemStack.EMPTY);
        }
    }
}
