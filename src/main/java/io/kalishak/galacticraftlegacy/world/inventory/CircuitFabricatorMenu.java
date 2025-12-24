package io.kalishak.galacticraftlegacy.world.inventory;

import io.kalishak.galacticraftlegacy.transfer.ResourcefulHelper;
import io.kalishak.galacticraftlegacy.world.level.block.entity.CircuitFabricatorBlockEntity;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

public class CircuitFabricatorMenu extends RecipeBookMenu {
    public CircuitFabricatorMenu(int containerId, Inventory playerInventory, CircuitFabricatorBlockEntity circuitFabricator, ContainerData containerData) {
        super(GalacticraftMenuType.CIRCUIT_FABRICATOR.get(), containerId);
    }

    public CircuitFabricatorMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf data) {
        this(containerId, playerInventory, ResourcefulHelper.readBlockEntity(GalacticraftBlockEntityType.CIRCUIT_FABRICATOR.get(), playerInventory.player.level(), data), new SimpleContainerData(2));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public PostPlaceAction handlePlacement(boolean useMaxItems, boolean isCreative, RecipeHolder<?> recipe, ServerLevel level, Inventory playerInventory) {
        return null;
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedItemContents stackedItemContents) {

    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return null;
    }
}
