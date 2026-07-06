package io.kalishak.galacticraftlegacy.world.level.block.entity;

import io.kalishak.galacticraftlegacy.transfer.ResourcefulHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.RangedResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.function.BiConsumer;

public abstract class BaseItemStorageBlockEntity extends BlockEntity implements MenuProvider, Nameable {
    private LockCode lockKey = LockCode.NO_LOCK;
    private @Nullable Component name;
    protected final ItemStacksResourceHandler items = new ItemStacksResourceHandler(getItemsSize()) {
        @Override
        protected void onContentsChanged(int index, ItemStack previousContents) {
            super.onContentsChanged(index, previousContents);
            BaseItemStorageBlockEntity.this.setChanged();
        }
    };

    protected BaseItemStorageBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) {
        super(type, worldPosition, blockState);
    }

    public static <BE extends BaseItemStorageBlockEntity> void registerDirectionalSlots(RegisterCapabilitiesEvent event, BlockEntityType<@NonNull BE> blockEntityType, Direction faceForInput, int inputSlotStart, int inputSlotEnd, Direction faceForOutput, int outputSlotStart, int outputSlotEnd) {
        event.registerBlockEntity(
                Capabilities.Item.BLOCK,
                blockEntityType,
                (entity, side) -> {
                    if (side == faceForInput) {
                        return RangedResourceHandler.of(() -> entity.items, inputSlotStart, inputSlotEnd);
                    } else if (side == faceForOutput) {
                        return RangedResourceHandler.of(() -> entity.items, outputSlotStart, outputSlotEnd);
                    }

                    return entity.items;
                }
        );
    }

    public static <BE extends BaseItemStorageBlockEntity> void registerDirectionalSlots(RegisterCapabilitiesEvent event, BlockEntityType<@NonNull BE> blockEntityType, Direction faceForInput, int inputSlotStart, int inputSlotEnd, Direction faceForOutput, int outputSlot) {
        event.registerBlockEntity(
                Capabilities.Item.BLOCK,
                blockEntityType,
                (entity, side) -> {
                    if (side == faceForInput) {
                        return RangedResourceHandler.of(() -> entity.items, inputSlotStart, inputSlotEnd);
                    } else if (side == faceForOutput) {
                        return RangedResourceHandler.ofSingleIndex(() -> entity.items, outputSlot);
                    }

                    return entity.items;
                }
        );
    }

    public static <BE extends BaseItemStorageBlockEntity> void registerDirectionalSlots(RegisterCapabilitiesEvent event, BlockEntityType<@NonNull BE> blockEntityType, Direction faceForInput, int inputSlot, Direction faceForOutput, int outputSlot) {
        event.registerBlockEntity(
                Capabilities.Item.BLOCK,
                blockEntityType,
                (entity, side) -> {
                    if (side == faceForInput) {
                        return RangedResourceHandler.ofSingleIndex(() -> entity.items, inputSlot);
                    } else if (side == faceForOutput) {
                        return RangedResourceHandler.ofSingleIndex(() -> entity.items, outputSlot);
                    }

                    return entity.items;
                }
        );
    }

    protected abstract int getItemsSize();

    protected void forEachResource(BiConsumer<ItemResource, Integer> consumer) {
        for (int i = 0; i < getItemsSize(); i++) {
            consumer.accept(this.items.getResource(i), this.items.getAmountAsInt(i));
        }
    }

    public ResourceHandler<ItemResource> getResourceHandler() {
        return this.items;
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.lockKey = LockCode.fromTag(input);
        this.name = parseCustomNameSafe(input, "CustomName");
        this.items.deserialize(input);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        this.lockKey.addToTag(output);
        output.storeNullable("CustomName", ComponentSerialization.CODEC, this.name);
        this.items.serialize(output);
    }

    @Override
    public Component getName() {
        return this.name != null ? this.name : getDefaultName();
    }

    @Override
    public Component getDisplayName() {
        return getName();
    }

    @Override
    public @Nullable Component getCustomName() {
        return this.name;
    }

    protected abstract Component getDefaultName();

    public boolean canOpen(Player player) {
        return this.lockKey.canUnlock(player);
    }

    public boolean isLocked() {
        return !this.lockKey.equals(LockCode.NO_LOCK);
    }

    public boolean isEmpty() {
        return ResourceHandlerUtil.isEmpty(this.items);
    }

    public ItemStack getItem(int slot) {
        return ItemUtil.getStack(this.items, slot);
    }

    public ItemStack removeItem(int slot, int count) {
        ItemResource resourceIn = this.items.getResource(slot);

        if (!resourceIn.isEmpty()) {
            try (Transaction tx = Transaction.open(null)) {
                int removed = this.items.extract(slot, resourceIn, count, tx);

                if (removed > 0) {
                    tx.commit();
                    return resourceIn.toStack(removed);
                }
            }

        }

        return ItemStack.EMPTY;
    }

    public void setItem(int slot, ItemStack stack) {
        setItem(slot, ItemResource.of(stack), stack.getCount());
    }

    public final void setItem(int slot, ItemResource resource, int count) {
        this.items.set(slot, resource, count);
    }

    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    public void clearContent() {
        for (int i = 0; i < getItemsSize(); i++) {
            setItem(i, ItemStack.EMPTY);
        }
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        if (canOpen(player)) {
            return createMenu(containerId, inventory);
        }

        BaseContainerBlockEntity.sendChestLockedNotifications(getBlockPos().getCenter(), player, getDisplayName());
        return null;
    }

    protected abstract AbstractContainerMenu createMenu(final int containerId, final Inventory inventory);

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        super.applyImplicitComponents(components);
        this.name = components.get(DataComponents.CUSTOM_NAME);
        this.lockKey = components.getOrDefault(DataComponents.LOCK, LockCode.NO_LOCK);
        ResourcefulHelper.applyContainerComponent(components, this::setItem);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        components.set(DataComponents.CUSTOM_NAME, this.name);

        if (isLocked()) {
            components.set(DataComponents.LOCK, this.lockKey);
        }

        ResourcefulHelper.collectContainerComponent(components, this.items);
    }

    @Override
    public void removeComponentsFromTag(ValueOutput output) {
        output.discard("CustomName");
        output.discard("lock");
        output.discard("Stacks");
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        if (this.level != null) {
            Containers.dropContents(this.level, pos, this.items.copyToList());
            clearContent();
        }
    }
}
