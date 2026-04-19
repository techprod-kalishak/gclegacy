/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.storage.loot.functions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.fluids.FluidInstance;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;

import java.util.List;

public class SetItemFluidTankFunction extends LootItemConditionalFunction {
    public static final MapCodec<SetItemFluidTankFunction> MAP_CODEC = RecordCodecBuilder.mapCodec(
            i -> commonFields(i)
                    .and(FluidInstance.FLUID_HOLDER_CODEC.fieldOf("fluid").forGetter(f -> f.fluid))
                    .and(IntProviders.CODEC.fieldOf("value").forGetter(f -> f.value)
            ).apply(i, SetItemFluidTankFunction::new)
    );
    private final Holder<Fluid> fluid;
    private final IntProvider value;

    private SetItemFluidTankFunction(List<LootItemCondition> predicates, Holder<Fluid> fluid, IntProvider value) {
        super(predicates);
        this.fluid = fluid;
        this.value = value;
    }

    @Override
    public MapCodec<SetItemFluidTankFunction> codec() {
        return MAP_CODEC;
    }

    @Override
    public ItemStack run(ItemStack itemStack, LootContext context) {
        FluidStack fluidStack = new FluidStack(this.fluid, this.value.sample(context.getRandom()));

        itemStack.set(GalacticraftDataComponents.FLUID_TANK, SimpleFluidContent.copyOf(fluidStack));
        return itemStack;
    }

    public static <T> LootItemConditionalFunction.Builder<?> setFluid(Holder<Fluid> fluid, IntProvider value) {
        return simpleBuilder(conditions -> new SetItemFluidTankFunction(conditions, fluid, value));
    }
}
