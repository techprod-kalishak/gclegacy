package io.kalishak.galacticraftlegacy.client.gui.screens.inventory;

import io.kalishak.galacticraftlegacy.world.inventory.CircuitFabricatorMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class CircuitFabricatorScreen extends AbstractRecipeBookScreen<CircuitFabricatorMenu> implements MachineScreen {
    public CircuitFabricatorScreen(CircuitFabricatorMenu menu, RecipeBookComponent<?> recipeBookComponent, Inventory playerInventory, Component title) {
        super(menu, recipeBookComponent, playerInventory, title);
    }

    @Override
    public void updateEnergy(int previousAmount, int delta) {

    }

    @Override
    protected ScreenPosition getRecipeBookButtonPosition() {
        return null;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {

    }
}
