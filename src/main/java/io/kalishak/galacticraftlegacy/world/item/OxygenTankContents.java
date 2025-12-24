package io.kalishak.galacticraftlegacy.world.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.ItemAccessFluidHandler;

import java.util.function.Consumer;

public class OxygenTankContents extends ItemAccessFluidHandler implements TooltipProvider {
    public OxygenTankContents(ItemAccess itemAccess, DataComponentType<SimpleFluidContent> component, int capacity) {
        super(itemAccess, component, capacity);
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag flag, DataComponentGetter componentGetter) {
        tooltipAdder.accept(
                Component.translatable("item.galacticraftlegacy.tank.tooltip")
                        .append(Component.literal(this.capacity + "mB").withStyle(Style.EMPTY.withColor(ChatFormatting.BLUE)))
        );
    }
}
