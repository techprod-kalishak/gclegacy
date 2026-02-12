package io.kalishak.galacticraftlegacy.transfer.capability.fluid;

import net.minecraft.core.component.DataComponentType;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.ItemAccessFluidHandler;

import java.util.function.Predicate;

public class ItemAccessFluidTank extends ItemAccessFluidHandler {
    protected final Predicate<FluidResource> validator;

    public ItemAccessFluidTank(ItemAccess itemAccess, DataComponentType<SimpleFluidContent> componentType, int capacity, Predicate<FluidResource> validator) {
        super(itemAccess, componentType, capacity);
        this.validator = validator;
    }

    @Override
    public boolean isValid(int index, FluidResource resource) {
        return this.validator.test(resource) && super.isValid(index, resource);
    }
}
