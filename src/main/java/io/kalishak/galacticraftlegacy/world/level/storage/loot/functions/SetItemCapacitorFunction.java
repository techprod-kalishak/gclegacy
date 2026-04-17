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
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;

public class SetItemCapacitorFunction extends LootItemConditionalFunction {
    public static final MapCodec<SetItemCapacitorFunction> MAP_CODEC = RecordCodecBuilder.mapCodec(
            i -> commonFields(i)
                    .and(IntProviders.CODEC.fieldOf("value").forGetter(f -> f.value)
                    ).apply(i, SetItemCapacitorFunction::new)
    );
    private final IntProvider value;

    private SetItemCapacitorFunction(List<LootItemCondition> predicates, IntProvider value) {
        super(predicates);
        this.value = value;
    }

    @Override
    public MapCodec<SetItemCapacitorFunction> codec() {
        return MAP_CODEC;
    }

    @Override
    public ItemStack run(ItemStack itemStack, LootContext context) {
        itemStack.set(GalacticraftDataComponents.STORED_ENERGY, this.value.sample(context.getRandom()));
        return itemStack;
    }

    public static <T> LootItemConditionalFunction.Builder<?> setEnergy(IntProvider value) {
        return simpleBuilder(conditions -> new SetItemCapacitorFunction(conditions, value));
    }
}
