package io.kalishak.galacticraftlegacy.world.inventory;

import com.google.common.base.Predicates;
import io.kalishak.galacticraftlegacy.EnumExtensions;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.transfer.ResourcefulHelper;
import io.kalishak.galacticraftlegacy.world.inventory.slot.CapabilityHandlerSlot;
import io.kalishak.galacticraftlegacy.world.inventory.slot.MutableHandlerSlot;
import io.kalishak.galacticraftlegacy.world.item.component.ItemAccessEnergyUtils;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.CircuitRecipe;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.input.SimpleResourceInput;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.CircuitFabricatorBlockEntity;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.transfer.item.ItemUtil;

import java.util.List;

public class CircuitFabricatorMenu extends AbstractMachineMenu<CircuitFabricatorBlockEntity> {
    private static final int INV_SLOT_START = 6;
    private static final int INV_SLOT_END = 33;
    private static final int USE_ROW_SLOT_START = 33;
    private static final int USE_ROW_SLOT_END = 42;

    public CircuitFabricatorMenu(int containerId, Inventory playerInventory, CircuitFabricatorBlockEntity circuitFabricator, ContainerData containerData) {
        super(GalacticraftMenuType.CIRCUIT_FABRICATOR.get(), containerId, playerInventory, circuitFabricator, containerData);

        addSlot(new CapabilityHandlerSlot<>(this.resourceHandler, circuitFabricator::set, Capabilities.Energy.ITEM, CircuitFabricatorBlockEntity.SLOT_BATTERY, 6, 69));
        addSlot(new MutableHandlerSlot(this.resourceHandler, circuitFabricator::set, stack -> stack.is(Tags.Items.GEMS_DIAMOND), CircuitFabricatorBlockEntity.SLOT_DIAMOND, 15, 17));
        addSlot(new MutableHandlerSlot(this.resourceHandler, circuitFabricator::set, stack -> stack.is(GalacticraftTags.Items.RAW_MATERIALS_SILICON), CircuitFabricatorBlockEntity.SLOT_SILICON_1, 74, 46));
        addSlot(new MutableHandlerSlot(this.resourceHandler, circuitFabricator::set, stack -> stack.is(GalacticraftTags.Items.RAW_MATERIALS_SILICON), CircuitFabricatorBlockEntity.SLOT_SILICON_2, 74, 64));
        addSlot(new MutableHandlerSlot(this.resourceHandler, circuitFabricator::set, stack -> stack.is(Tags.Items.DUSTS_REDSTONE), CircuitFabricatorBlockEntity.SLOT_REDSTONE, 122, 46));
        addSlot(new MutableHandlerSlot(this.resourceHandler, circuitFabricator::set, Predicates.alwaysTrue(), CircuitFabricatorBlockEntity.SLOT_INGREDIENT, 145, 20));
        addSlot(new MutableHandlerSlot(this.resourceHandler, circuitFabricator::set, Predicates.alwaysFalse(), CircuitFabricatorBlockEntity.SLOT_OUTPUT, 152, 86));
        addStandardInventorySlots(playerInventory, 8, 110);
        addDataSlots(containerData);
    }

    public CircuitFabricatorMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf data) {
        this(containerId, playerInventory, ResourcefulHelper.readBlockEntity(GalacticraftBlockEntityType.CIRCUIT_FABRICATOR.get(), playerInventory.player.level(), data), new SimpleContainerData(2));
    }

    public float getProgress() {
        int i = this.containerData.get(0);
        int j = this.containerData.get(1);

        return j != 0 && i != 0 ? Mth.clamp((float)i / j, 0.0F, 1.0F) : 0.0F;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack swappedStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack returnedStack = slot.getItem();
            swappedStack = returnedStack.copy();
            if (index == CircuitFabricatorBlockEntity.SLOT_OUTPUT) {
                if (!this.moveItemStackTo(returnedStack, INV_SLOT_START, USE_ROW_SLOT_END, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(returnedStack, swappedStack);
            } else if (index > 0) {
                if (ItemAccessEnergyUtils.hasEnergyHandler(returnedStack)) {
                    if (!this.moveItemStackTo(returnedStack, CircuitFabricatorBlockEntity.SLOT_BATTERY, CircuitFabricatorBlockEntity.SLOT_DIAMOND, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= INV_SLOT_START && index < INV_SLOT_END) {
                    if (!this.moveItemStackTo(returnedStack, USE_ROW_SLOT_START, USE_ROW_SLOT_END, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= USE_ROW_SLOT_START && index < USE_ROW_SLOT_END && !this.moveItemStackTo(returnedStack, INV_SLOT_START, INV_SLOT_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(returnedStack, INV_SLOT_START, USE_ROW_SLOT_END, false)) {
                return ItemStack.EMPTY;
            }

            if (returnedStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (returnedStack.getCount() == swappedStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, returnedStack);
        }

        return swappedStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PostPlaceAction handlePlacement(boolean useMaxItems, boolean isCreative, RecipeHolder<?> recipe, ServerLevel level, Inventory playerInventory) {
        final List<Slot> craftingSlots = this.slots.subList(CircuitFabricatorBlockEntity.SLOT_DIAMOND, CircuitFabricatorBlockEntity.SLOT_OUTPUT);

        return ServerPlaceRecipe.placeRecipe(new ServerPlaceRecipe.CraftingMenuAccess<>() {
            @Override
            public void fillCraftSlotsStackedContents(StackedItemContents stackedItemContents) {
                CircuitFabricatorMenu.this.fillCraftSlotsStackedContents(stackedItemContents);
            }

            @Override
            public void clearCraftingContent() {
                craftingSlots.forEach(slot -> slot.set(ItemStack.EMPTY));
            }

            @Override
            public boolean recipeMatches(RecipeHolder<CircuitRecipe> recipe) {
                return recipe.value().matches(new SimpleResourceInput(() -> CircuitFabricatorMenu.this.resourceHandler, CircuitFabricatorBlockEntity.SLOT_DIAMOND, CircuitFabricatorBlockEntity.SLOT_COUNT), level);
            }
        }, 3, 2, craftingSlots, craftingSlots, playerInventory, (RecipeHolder<CircuitRecipe>) recipe, useMaxItems, isCreative);
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedItemContents stackedItemContents) {
        for (int i = CircuitFabricatorBlockEntity.SLOT_DIAMOND; i < CircuitFabricatorBlockEntity.SLOT_OUTPUT; i++) {
            ItemStack stack = ItemUtil.getStack(this.resourceHandler, i);

            if (!stack.isEmpty()) {
                stackedItemContents.accountStack(stack, 1);
            }
        }
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return EnumExtensions.RECIPE_BOOK_TYPE_FABRICATING.getValue();
    }
}
