package io.kalishak.galacticraftlegacy.world.inventory.machine;

import io.kalishak.galacticraftlegacy.EnumExtensions;
import io.kalishak.galacticraftlegacy.transfer.ResourcefulHelper;
import io.kalishak.galacticraftlegacy.world.inventory.GalacticraftMenuType;
import io.kalishak.galacticraftlegacy.world.inventory.slot.CapabilityHandlerSlot;
import io.kalishak.galacticraftlegacy.world.inventory.slot.ResultResourceHandlerSlot;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.ArcHeatingRecipe;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.GalacticraftRecipeType;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.ElectricArcFurnaceBlockEntity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

import java.util.List;

public class ElectricArcFurnaceMenu extends AbstractElectricFurnaceMenu<ArcHeatingRecipe, ElectricArcFurnaceBlockEntity> {
    public ElectricArcFurnaceMenu(int containerId, Inventory playerInventory, ElectricArcFurnaceBlockEntity machine, ContainerData containerData) {
        super(GalacticraftMenuType.ELECTRIC_ARC_FURNACE.get(), containerId, playerInventory, machine, containerData, GalacticraftRecipeType.ARC_HEATING);

        addSlot(new ResourceHandlerSlot(this.resourceHandler, machine::setItem, 0, 56, 25));
        addSlot(new CapabilityHandlerSlot<>(this.resourceHandler, machine::setItem, Capabilities.Energy.ITEM, 1, 8, 49));
        addSlot(new ResultResourceHandlerSlot(playerInventory.player, this.resourceHandler, machine::awardUsedRecipes, 2, 109, 25));
        addSlot(new ResultResourceHandlerSlot(playerInventory.player, this.resourceHandler, machine::awardUsedRecipes, 3, 127, 25));
        addStandardInventorySlots(playerInventory, 8, 84);
    }

    public ElectricArcFurnaceMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf data) {
        this(containerId, playerInventory, ResourcefulHelper.readBlockEntity(GalacticraftBlockEntityType.ELECTRIC_ARC_FURNACE.get(), playerInventory.player.level(), data), new SimpleContainerData(2));
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return EnumExtensions.RECIPE_BOOK_TYPE_ARC_HEATING.getValue();
    }

    @Override
    @SuppressWarnings("unchecked")
    public PostPlaceAction handlePlacement(boolean useMaxItems, boolean isCreative, RecipeHolder<?> recipe, ServerLevel level, Inventory playerInventory) {
        List<Slot> craftingSlots = List.of(getSlot(0), getSlot(2), getSlot(3));

        return ServerPlaceRecipe.placeRecipe(new ServerPlaceRecipe.CraftingMenuAccess<>() {
            @Override
            public void fillCraftSlotsStackedContents(StackedItemContents stackedItemContents) {
                ElectricArcFurnaceMenu.this.fillCraftSlotsStackedContents(stackedItemContents);
            }

            @Override
            public void clearCraftingContent() {
                craftingSlots.forEach(slot -> slot.set(ItemStack.EMPTY));
            }

            @Override
            public boolean recipeMatches(RecipeHolder<ArcHeatingRecipe> recipe) {
                return recipe.value().matches(new SingleRecipeInput(getSlot(0).getItem()), ElectricArcFurnaceMenu.this.level);
            }
        }, 1, 1, List.of(getSlot(0)), craftingSlots, playerInventory, (RecipeHolder<ArcHeatingRecipe>) recipe, useMaxItems, isCreative);
    }
}
