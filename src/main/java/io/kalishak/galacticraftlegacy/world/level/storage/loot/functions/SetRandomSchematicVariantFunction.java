/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.storage.loot.functions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.component.SchematicVariant;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public class SetRandomSchematicVariantFunction extends LootItemConditionalFunction {
    public static final MapCodec<SetRandomSchematicVariantFunction> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> commonFields(instance)
                    .and(FeatureTier.CODEC.fieldOf("feature_tier").forGetter(func -> func.featureTier)
    ).apply(instance, SetRandomSchematicVariantFunction::new));
    private final FeatureTier featureTier;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private SetRandomSchematicVariantFunction(Optional<Holder<LootItemCondition>> condition, FeatureTier featureTier) {
        super(condition);
        this.featureTier = featureTier;
    }

    @SuppressWarnings("unused")
    public static LootItemConditionalFunction.Builder<?> randomTiered(FeatureTier tier) {
        return simpleBuilder(i -> new SetRandomSchematicVariantFunction(i, tier));
    }

    @Override
    public @NonNull MapCodec<SetRandomSchematicVariantFunction> codec() {
        return MAP_CODEC;
    }

    @Override
    protected @NonNull ItemStack run(@NonNull ItemStack itemStack, LootContext context) {
        return SchematicVariant.setRandomOfTier(itemStack, context.getResolver(), this.featureTier, context.getRandom());
    }
}
