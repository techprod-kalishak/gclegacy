package io.kalishak.galacticraftlegacy.client.renderer.item.properties.range;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import org.jspecify.annotations.Nullable;

public record FluidAmountProperty() implements RangeSelectItemModelProperty {
    public static final MapCodec<FluidAmountProperty> CODEC = MapCodec.unit(FluidAmountProperty::new);

    @Override
    public float get(ItemStack stack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        ResourceHandler<FluidResource> resourceHandler = stack.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(stack));

        if (resourceHandler != null) {
            FluidResource resource = resourceHandler.getResource(0);

            return (float) resourceHandler.getAmountAsInt(0) / (float) resourceHandler.getCapacityAsInt(0, resource);
        }

        return 0.0F;
    }

    @Override
    public MapCodec<FluidAmountProperty> type() {
        return CODEC;
    }
}
