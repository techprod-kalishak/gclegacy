package io.kalishak.galacticraftlegacy.client.gui.screens.inventory;

import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.attachment.block.SyncedEnergyHandler;
import io.kalishak.galacticraftlegacy.client.gui.components.DispatchTooltipRegion;
import io.kalishak.galacticraftlegacy.client.gui.components.TooltipRegion;
import io.kalishak.galacticraftlegacy.network.payload.ToggleMachineButtonPayload;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import io.kalishak.galacticraftlegacy.world.inventory.machine.SolarPanelMenu;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.electric.AbstractSolarPanelBlockEntity;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class SolarPanelScreen extends AbstractContainerScreen<SolarPanelMenu> implements MachineScreen {
    public static final Identifier TEXTURE = Constants.id("textures/gui/container/solar_panel.png");
    public static final Identifier SUN_SPRITE = Constants.id("container/solar_panel/sun_visible");
    private ImageWidget sun;

    public SolarPanelScreen(SolarPanelMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 201);
        this.inventoryLabelY = getImageHeight() - 94;
    }

    private boolean isSunVisible() {
        return this.menu.getSolarStrength() > 0;
    }

    @Override
    protected void init() {
        super.init();

        addRenderableOnly(getEnergyTooltip(this.menu.getEnergyCapacity(), this.leftPos, this.topPos));

        Button enableGeneration = Button.builder(
                GalacticraftComponents.setToggleButtonMessage(this.menu.getSolarPanel().isDisabled()), button -> {
                    ClientPacketDistributor.sendToServer(new ToggleMachineButtonPayload(this.menu.getSolarPanel().getBlockPos()));
                    button.setMessage(GalacticraftComponents.setToggleButtonMessage(!this.menu.getSolarPanel().isDisabled()));
                }).bounds(this.leftPos + 52, this.topPos + 79, 72, 20).build();
        addRenderableWidget(enableGeneration);

        this.sun = ImageWidget.sprite(18, 18, SUN_SPRITE);
        this.sun.setPosition(this.leftPos + 47, this.topPos + 20);
        this.sun.setTooltip(Tooltip.create(GalacticraftComponents.SUN_VISIBLE.translated()));

        addRenderableOnly(this.sun);
        TooltipRegion sunTooltip = new DispatchTooltipRegion(this.sun, GalacticraftComponents.SUN_NOT_VISIBLE.translated(), GalacticraftComponents.SUN_VISIBLE.translated(), this::isSunVisible);
        sunTooltip.copySize(this.sun);
        addRenderableOnly(sunTooltip);
    }

    @Override
    public int getEnergyStored() {
        return getMenu().getSolarPanel().getExistingData(GalacticraftAttachments.SYNC_ENERGY_STORAGE)
                .map(SyncedEnergyHandler::storedEnergy)
                .orElse(0);
    }

    @Override
    public float getEnergyProgress() {
        int stored = getEnergyStored();

        return stored != 0 ? Math.clamp((float) stored / this.menu.getEnergyCapacity(), 0.0F, 1.0F) : 0.0F;
    }

    @Override
    public Bounds getEnergyBarBounds() {
        return new Bounds(97, 25);
    }

    @Override
    public Bounds getEnergyIndicatorBounds() {
        return new Bounds(84, 25);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        int i = this.leftPos;
        int j = this.topPos;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, i, j, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
        extractSprites(graphics, i, j, this.menu.getEnergyCapacity());
    }

    @Override
    public void extractSprites(GuiGraphicsExtractor graphics, int leftOffset, int topOffset, int energyCapacity) {
        MachineScreen.super.extractSprites(graphics, leftOffset, topOffset, energyCapacity);
        this.sun.visible = isSunVisible();
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractLabels(graphics, mouseX, mouseY);
        int xOffset = 88;

        Component generation = GalacticraftComponents.GENERATOR_GENERATING.translated().append(": ").append(GalacticraftComponents.energyComponent(this.menu.getSolarPanel().getEnergyProduced(), true));
        graphics.text(this.font, generation, xOffset - this.font.width(generation) / 2, 46, -12566464, false);

        Component machineStatus = GalacticraftComponents.MACHINE_STATUS_TYPE.get(this.menu.getSolarPanel().getMachineStatus().type()).translated();
        Component fullStatus = GalacticraftComponents.MACHINE_STATUS.translated();
        int baseX = xOffset - this.font.width(machineStatus) / 2;
        int typeXOffset = baseX - (this.font.width(machineStatus) + this.font.width(fullStatus)) / 2;
        graphics.text(this.font, machineStatus, baseX, 57, this.menu.getSolarPanel().getMachineStatus().type().getTextColor(), false);
        graphics.text(this.font, fullStatus, typeXOffset, 57, -12566464, false);

        float boostValue = ((AbstractSolarPanelBlockEntity.getSolarBoost(this.menu.getSolarPanel()) - 1) * 1000) / 10.0F;
        String arg = String.format("%.1f", boostValue) + "%";
        Component envBoost = GalacticraftComponents.ENVIRONMENTAL_BOOST.translatedArg(arg);
        graphics.text(this.font, envBoost, xOffset - this.font.width(envBoost) / 2, 68, -12566464, false);
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
        super.extractTooltip(guiGraphics, mouseX, mouseY);
        extractBatterySlotTooltip(guiGraphics, this.font, this.hoveredSlot, 0, mouseX, mouseY);
    }
}
