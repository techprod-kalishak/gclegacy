/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.inventory;

import io.kalishak.galacticraftlegacy.transfer.ResourcefulHelper;
import io.kalishak.galacticraftlegacy.world.inventory.slot.CraftingResultHandlerSlot;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import io.kalishak.galacticraftlegacy.world.level.block.entity.MagneticCraftingBlockEntity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class MagneticCraftingMenu extends AbstractCraftingMenu {
    private final MagneticCraftingBlockEntity blockEntity;
    private final Player player;

    public MagneticCraftingMenu(int containerId, Inventory inventory, Player player, MagneticCraftingBlockEntity blockEntity) {
        super(GalacticraftMenuType.MAGNETIC_CRAFTING.get(), containerId, 3, 3);
        this.blockEntity = blockEntity;
        this.player = player;
        ResourceHandler<ItemResource> resourceHandler = ResourcefulHelper.getResourceHandler(Capabilities.Item.BLOCK, ItemResource.EMPTY, blockEntity, null);

        addResultSlot(resourceHandler);
        addCraftingSlots(resourceHandler);
        addStandardInventorySlots(inventory, 8, 84);
    }

    public MagneticCraftingMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf data) {
        this(containerId, playerInventory, playerInventory.player, ResourcefulHelper.readBlockEntity(GalacticraftBlockEntityType.MAGNETIC_CRAFTING.get(), playerInventory.player.level(), data));

        if (playerInventory.player instanceof ServerPlayer serverPlayer) {
            slotChangedCraftingGrid(this, serverPlayer, null);
        }
    }

    private void addResultSlot(ResourceHandler<ItemResource> resourceHandler) {
        addSlot(new CraftingResultHandlerSlot(
                this.player,
                this.blockEntity,
                resourceHandler,
                this.blockEntity::set,
                0, 124, 35
        ));
    }

    private void addCraftingSlots(ResourceHandler<ItemResource> resourceHandler) {
        int index = 1;
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 3; ++x) {
                addSlot(new ResourceHandlerSlot(resourceHandler, this.blockEntity::set, index, 30 + x * 18, 17 + y * 18));
                index++;
            }
        }
    }

    protected static void slotChangedCraftingGrid(MagneticCraftingMenu menu, ServerPlayer player, @Nullable RecipeHolder<CraftingRecipe> recipeHint) {
        menu.blockEntity.refreshRecipeResult(recipeHint, itemStack -> {
            menu.blockEntity.set(0, ItemResource.of(itemStack), itemStack.getCount());
            menu.setRemoteSlot(0, itemStack);

            player.connection.send(new ClientboundContainerSetSlotPacket(menu.containerId, menu.incrementStateId(), 0, itemStack));
        });
    }

    @Override
    public void slotsChanged(Container container) {
        if (this.blockEntity.getLevel() instanceof ServerLevel) {
            slotChangedCraftingGrid(this, (ServerPlayer) this.player, null);
        }
    }

    @Override
    public void finishPlacingRecipe(ServerLevel level, RecipeHolder<CraftingRecipe> recipe) {
        slotChangedCraftingGrid(this, (ServerPlayer) this.player, recipe);
    }

    @Override
    public Slot getResultSlot() {
        return getSlot(0);
    }

    @Override
    public List<Slot> getInputGridSlots() {
        return this.slots.subList(1, 10);
    }

    @Override
    protected Player owner() {
        return this.player;
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.CRAFTING;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack clicked = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);

        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            clicked = stack.copy();

            if (slotIndex == 0) {
                stack.getItem().onCraftedBy(stack, player);
                if (!moveItemStackTo(stack, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(stack, clicked);
            } else if (slotIndex >= 10 && slotIndex < 46) {
                if (!moveItemStackTo(stack, 1, 10, false)) {
                    if (slotIndex < 37) {
                        if (!moveItemStackTo(stack, 37, 46, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!moveItemStackTo(stack, 10, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!moveItemStackTo(stack, 10, 46, false)) {
                return ItemStack.EMPTY;
            }

            if (stack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == clicked.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);

            if (slotIndex == 0) {
                player.drop(stack, false);
            }
        }

        return clicked;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack carried, Slot target) {
        return target.container != this.resultSlots && super.canTakeItemForPickAll(carried, target);
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this.blockEntity, player);
    }
}
