package io.kalishak.galacticraftlegacy.world.inventory.slot;

import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import org.jspecify.annotations.Nullable;

public class CapabilityHandlerSlot<C> extends MutableHandlerSlot {

    public CapabilityHandlerSlot(ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier, ItemCapability<C, @Nullable ItemAccess> capability, int index, int xPosition, int yPosition) {
        super(handler, slotModifier, stack -> stack.getCapability(capability, ItemAccess.forStack(stack)) != null, index, xPosition, yPosition);
    }

    public CapabilityHandlerSlot(ItemStacksResourceHandler handler, ItemCapability<C, @Nullable ItemAccess> capability, int index, int xPosition, int yPosition) {
        super(handler, stack -> stack.getCapability(capability, ItemAccess.forStack(stack)) != null, index, xPosition, yPosition);
    }
}
