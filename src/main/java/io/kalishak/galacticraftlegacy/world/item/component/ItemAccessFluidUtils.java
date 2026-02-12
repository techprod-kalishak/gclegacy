package io.kalishak.galacticraftlegacy.world.item.component;

import io.kalishak.galacticraftlegacy.Constants;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.component.TooltipProvider;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.InfiniteResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public interface ItemAccessFluidUtils extends TooltipProvider {
    static void addTooltip(@NonNull ResourceHandler<FluidResource> resourceHandler, Consumer<Component> consumer) {
        if (resourceHandler instanceof InfiniteResourceHandler<FluidResource>) {
            Constants.infinite(consumer);
        } else if (resourceHandler.getResource(0).isEmpty()) {
            consumer.accept(Component.translatable("item.galacticraftlegact.fluid_tank.empty"));
        } else {
            FluidStack fluidStack = FluidUtil.getStack(resourceHandler, 0);
            int color = IClientFluidTypeExtensions.of(fluidStack.getFluidType()).getTintColor();

            consumer.accept(
                    Component.translatable(fluidStack.getDescriptionId())
                            .append(": ")
                            .append(Component.literal(fluidStack.getAmount() + "mB")
                                    .withStyle(style -> style.withColor(color))));
        }
    }
}
