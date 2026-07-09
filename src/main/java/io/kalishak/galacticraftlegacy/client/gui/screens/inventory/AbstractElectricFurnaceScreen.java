package io.kalishak.galacticraftlegacy.client.gui.screens.inventory;

import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.world.inventory.machine.AbstractElectricFurnaceMenu;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.ElectricCookingRecipe;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.AbstractElectricFurnaceBlockEntity;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

public abstract class AbstractElectricFurnaceScreen<R extends ElectricCookingRecipe, BE extends AbstractElectricFurnaceBlockEntity<R>, M extends AbstractElectricFurnaceMenu<R, BE>> extends AbstractMachineScreen<BE, M> {
    protected static final Identifier BURN_PROGRESS_SPRITE = Constants.id("container/electric_furnace/burn_progress");

    protected AbstractElectricFurnaceScreen(M menu, RecipeBookComponent<?> recipeBookComponent, Inventory playerInventory, Component title, Identifier backgroundTexture) {
        super(menu, recipeBookComponent, playerInventory, title, backgroundTexture);
    }

    @Override
    public void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    protected ScreenPosition getRecipeBookButtonPosition() {
        return new ScreenPosition(this.leftPos + 20, this.height / 2 - 56);
    }

    @Override
    public MachineScreen.Bounds getEnergyIndicatorBounds() {
        return new MachineScreen.Bounds(27, 53);
    }

    @Override
    public MachineScreen.Bounds getEnergyBarBounds() {
        return new MachineScreen.Bounds(40, 53);
    }

    @Override
    public void extractSprites(GuiGraphicsExtractor guiGraphics, int leftOffset, int topOffset, int energyCapacity) {
        super.extractSprites(guiGraphics, leftOffset, topOffset, energyCapacity);

        int processProgress = Mth.ceil(this.menu.getBurnProgress() * 24.0F);
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, BURN_PROGRESS_SPRITE, 24, 16, 0, 0, leftOffset + 78, topOffset + 24, processProgress, 16);
    }
}
