package io.kalishak.galacticraftlegacy.client.gui.screens.inventory;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.client.gui.components.ItemDisplayButton;
import io.kalishak.galacticraftlegacy.network.payload.ToggleGearInventoryPayload;
import io.kalishak.galacticraftlegacy.world.inventory.GearInventoryMenu;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.util.function.Consumer;

public class GearInventoryScreen extends AbstractContainerScreen<GearInventoryMenu> {
    public static final Identifier GEAR_INVENTORY_LOCATION = Identifier.fromNamespaceAndPath(Galacticraft.MODID, "textures/gui/container/gear_inventory.png");
    private float xMouse;
    private float yMouse;
    private final EffectsInInventory effects;

    public GearInventoryScreen(GearInventoryMenu menu, Inventory playerInventory, Component component) {
        super(menu, playerInventory, Component.empty());
        this.effects = new EffectsInInventory(this);
    }

    private static void addInventoryTabs(Consumer<Button> widgetConsumer, int leftPos, int topPos, Minecraft mc) {
        widgetConsumer.accept(new ItemDisplayButton(
                mc,
                leftPos,
                topPos - 20,
                26,
                24,
                Component.translatable("container.inventory"),
                Items.CRAFTING_TABLE.getDefaultInstance(),
                false,
                false,
                onClick -> {
                    mc.player.closeContainer();
                    mc.setScreen(new InventoryScreen(mc.player));
                },
                mutableComponentSupplier -> Component.empty()
        ));
        widgetConsumer.accept(new ItemDisplayButton(
                mc,
                leftPos + 28,
                topPos - 20,
                26,
                24,
                Component.translatable("container.gear"),
                GalacticraftItems.OXYGEN_MASK.toStack(),
                false,
                false,
                onClick -> ClientPacketDistributor.sendToServer(new ToggleGearInventoryPayload(true)),
                mutableComponentSupplier -> Component.empty()
        ));
    }

    @SubscribeEvent
    public static void setupScreen(ScreenEvent.Init.Post event) {
        Screen screen = event.getScreen();

        if (screen instanceof InventoryScreen inventoryScreen) {
            addInventoryTabs(event::addListener, inventoryScreen.getLeftPos(), inventoryScreen.getTopPos(), inventoryScreen.getMinecraft());
        }
    }

    @Override
    protected void init() {
        super.init();
        addInventoryTabs(this::addRenderableWidget, this.leftPos, this.topPos, this.minecraft);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        graphics.text(this.font, this.title, this.titleLabelX, this.titleLabelY, -12566464, false);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        extractTooltip(graphics, mouseX, mouseY);
        this.effects.extractRenderState(graphics, mouseX, mouseY);
        this.xMouse = mouseX;
        this.yMouse = mouseY;
    }

    @Override
    public boolean showsActiveEffects() {
        return this.effects.canSeeEffects();
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractBackground(graphics, mouseX, mouseY, partialTicks);
        int i = this.leftPos;
        int j = this.topPos;
        graphics.blit(RenderPipelines.GUI_TEXTURED, GEAR_INVENTORY_LOCATION, i, j, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
        InventoryScreen.extractEntityInInventoryFollowsMouse(graphics, i + 7, j + 7, i + 60, j + 78, 30, 0.0625F, this.xMouse, this.yMouse, this.minecraft.player);
    }
}
