package io.kalishak.galacticraftlegacy.client.gui.components;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public class TooltipRegion implements LayoutElement, Renderable {
    protected final @Nullable AbstractWidget parent;
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected Component message;
    protected boolean isHovered;

    public TooltipRegion(@Nullable AbstractWidget parent, int x, int y, int width, int height, Component message) {
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.message = message;
    }

    public TooltipRegion(AbstractWidget parent, Component message) {
        this(parent, parent.getX(), parent.getY(), parent.getWidth(), parent.getHeight(), message);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float ticks) {
        this.isHovered = isHovered(mouseX, mouseY);

        if (this.isHovered) {
            graphics.setTooltipForNextFrame(this.message, mouseX, mouseY);
        }
    }

    @Override
    public void visitWidgets(Consumer<AbstractWidget> consumer) {
        if (this.parent != null) {
            consumer.accept(this.parent);
        }
    }

    protected boolean isHovered(double x, double y) {
        return x >= (double) getX() && y >= (double) getY() && x < (double) getRight() && y < (double) getBottom();
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    public void copySize(LayoutElement element) {
        setPosition(element.getX(), element.getY());
        setWidth(element.getWidth());
        setHeight(element.getHeight());
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getRight() {
        return getX() + getWidth();
    }

    public int getBottom() {
        return getY() + getHeight();
    }

    public Component getMessage() {
        return this.message;
    }

    public void setMessage(Component message) {
        this.message = message;
    }

    public boolean isVisible() {
        return this.parent == null || this.parent.visible;
    }
}
