package io.kalishak.galacticraftlegacy.world.inventory.workbench;

import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.world.inventory.GalacticraftMenuType;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.GalacticraftRecipeType;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.input.ResourceHandlerInput;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.rocket.VehicleCraftingDataRecipes;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.rocket.VehicleCraftingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.transfer.item.ItemResource;

import java.util.Optional;

public class NasaWorkbenchMenu extends AbstractNasaWorkbenchMenu {
    public NasaWorkbenchMenu(int containerId, Inventory inventory, Level level, BlockPos pos) {
        super(GalacticraftMenuType.NASA_WORKBENCH.get(), containerId, inventory, 18, level, pos);

        VehicleCraftingPage.populateContainer(
                VehicleCraftingDataRecipes.getDefault(level),
                this::addSlot,
                this.resourceHandler,
                this.resourceHandler::set,
                level,
                pos
        );
        addStandardInventorySlots(inventory, 8, 138);
    }

    public NasaWorkbenchMenu(int containerId, Inventory inventory, RegistryFriendlyByteBuf data) {
        this(containerId, inventory, inventory.player.level(), data.readBlockPos());
    }

    @Override
    public void slotsChanged(Container container) {
        super.slotsChanged(container);
        this.access.execute((level, _) -> {
            if (level instanceof ServerLevel serverLevel) {
                slotChangedCraftingGrid(this, serverLevel);
            }
        });
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack returnedStack = ItemStack.EMPTY;
        final Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            final ItemStack stack = slot.getItem();
            returnedStack = stack.copy();

            if (index <= 17) {
                if (!this.moveItemStackTo(stack, 18, 54, false)) {
                    return ItemStack.EMPTY;
                }

                if (index == 0) {
                    slot.onQuickCraft(stack, returnedStack);
                }
            } else if (returnedStack.is(GalacticraftItems.ROCKET_NOSE_CONE)) {
                if (!this.moveItemStackTo(stack, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (returnedStack.is(GalacticraftTags.Items.PLATE_HEAVY_DUTY)) {
                if (!this.moveItemStackTo(stack, 2, 10, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (returnedStack.is(GalacticraftItems.ROCKET_FIN)) {
                if (!this.moveItemStackTo(stack, 10, 12, false) && !this.moveItemStackTo(stack, 13, 15, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (returnedStack.is(GalacticraftItems.ROCKET_ENGINE)) {
                if (!this.moveItemStackTo(stack, 12, 13, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (returnedStack.is(Tags.Items.CHESTS)) {
                    if (!this.moveItemStackTo(stack, 15, 18, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 18 && index < 45) {
                    if (!this.moveItemStackTo(stack, 45, 54, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 45 && index < 54) {
                    if (!this.moveItemStackTo(stack, 18, 45, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (stack.getCount() == 0) {
                if (index == 0) {
                    slot.onTake(player, stack);
                }

                slot.set(ItemStack.EMPTY);
                return returnedStack;
            }

            if (stack.getCount() == returnedStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
            if (index == 0) {
                slot.setChanged();
            }
        }

        return returnedStack;
    }

    private static void slotChangedCraftingGrid(NasaWorkbenchMenu menu, ServerLevel level) {
        ResourceHandlerInput input = new ResourceHandlerInput(menu.resourceHandler);
        ServerPlayer serverPlayer = (ServerPlayer) menu.player;
        ItemStack result = ItemStack.EMPTY;
        Optional<RecipeHolder<VehicleCraftingRecipe>> maybeRecipe = level.getServer().getRecipeManager().getRecipeFor(GalacticraftRecipeType.VEHICLE_CRAFTING.get(), input, level);

        if (maybeRecipe.isPresent()) {
            RecipeHolder<VehicleCraftingRecipe> recipeHolder = maybeRecipe.get();
            VehicleCraftingRecipe craftingRecipe = recipeHolder.value();

            result = craftingRecipe.assemble(input);
        }

        int resultSlotIndex = 17;
        menu.resourceHandler.set(resultSlotIndex, ItemResource.of(result), result.count());
        menu.setRemoteSlot(resultSlotIndex, result);
        serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(menu.containerId, menu.incrementStateId(), resultSlotIndex, result));
    }
}
