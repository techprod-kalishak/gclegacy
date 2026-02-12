package io.kalishak.galacticraftlegacy.client.item;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import org.jspecify.annotations.Nullable;

public record ColorByFluid() implements ItemTintSource {
    public static final MapCodec<ColorByFluid> CODEC = MapCodec.unit(ColorByFluid::new);

    @Override
    public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity) {
        FluidStack fluid = FluidUtil.getFirstStackContained(stack);

        if (!fluid.isEmpty()) {
            IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid.getFluidType());

            return extensions.getTintColor();
        }

        return 0;
    }

    @Override
    public MapCodec<ColorByFluid> type() {
        return CODEC;
    }
}
