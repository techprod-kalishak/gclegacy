package io.kalishak.galacticraftlegacy.world.inventory.slot;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

import java.util.function.Predicate;

public class MutableHandlerSlot extends ResourceHandlerSlot {
    protected final Predicate<ItemResource> isValid;

    public MutableHandlerSlot(ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier, Predicate<ItemResource> isValid, int index, int xPosition, int yPosition) {
        super(handler, slotModifier, index, xPosition, yPosition);
        this.isValid = isValid;
    }

    public MutableHandlerSlot(ItemStacksResourceHandler handler, Predicate<ItemResource> isValid, int index, int xPosition, int yPosition) {
        this(handler, handler::set, isValid,  index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return this.isValid.test(ItemResource.of(stack)) && super.mayPlace(stack);
    }
}
