package io.kalishak.galacticraftlegacy.client.gui.components;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

import java.util.function.BooleanSupplier;

public class DispatchTooltipRegion extends TooltipRegion {
    private final Component altMessage;
    private final BooleanSupplier shouldShowAltMessage;

    public DispatchTooltipRegion(@Nullable AbstractWidget parent, int x, int y, int width, int height, Component message, Component altMessage, BooleanSupplier shouldShowAltMessage) {
        super(parent, x, y, width, height, message);
        this.altMessage = altMessage;
        this.shouldShowAltMessage = shouldShowAltMessage;
    }

    public DispatchTooltipRegion(AbstractWidget parent, Component message, Component altMessage, BooleanSupplier shouldShowAltMessage) {
        this(parent, parent.getX(), parent.getY(), parent.getWidth(), parent.getHeight(), message, altMessage, shouldShowAltMessage);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float ticks) {
        this.isHovered = graphics.containsPointInScissor(mouseX, mouseY) && isHovered(mouseX, mouseY);

        if (this.shouldShowAltMessage.getAsBoolean()) {
            setMessage(this.altMessage);
        } else {
            setMessage(this.message);
        }

        if (this.isHovered) {
            graphics.setTooltipForNextFrame(this.message, mouseX, mouseY);
        }
    }
}
