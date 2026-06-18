package io.kalishak.galacticraftlegacy.world.inventory.slot;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

public class NotPlaceableResourceHandlerSlot extends ResourceHandlerSlot {
    public NotPlaceableResourceHandlerSlot(ResourceHandler<ItemResource> handler, int handlerSlot, int xPosition, int yPosition) {
        super(handler, (_, _, _) -> {}, handlerSlot, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }
}
