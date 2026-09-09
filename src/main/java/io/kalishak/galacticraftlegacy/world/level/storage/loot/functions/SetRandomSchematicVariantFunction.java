/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.storage.loot.functions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.registry.SchematicVariant;
import io.kalishak.galacticraftlegacy.registry.SchematicVariants;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;

public class SetRandomSchematicVariantFunction extends LootItemConditionalFunction {
    public static final MapCodec<SetRandomSchematicVariantFunction> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> commonFields(instance)
                    .and(FeatureTier.CODEC.fieldOf("feature_tier").forGetter(func -> func.featureTier)
    ).apply(instance, SetRandomSchematicVariantFunction::new));
    private final FeatureTier featureTier;

    private SetRandomSchematicVariantFunction(List<LootItemCondition> predicates, FeatureTier featureTier) {
        super(predicates);
        this.featureTier = featureTier;
    }

    public LootItemConditionalFunction.Builder<?> randomTiered(FeatureTier tier) {
        return simpleBuilder(i -> new SetRandomSchematicVariantFunction(i, tier));
    }

    @Override
    public MapCodec<SetRandomSchematicVariantFunction> codec() {
        return MAP_CODEC;
    }

    @Override
    protected ItemStack run(ItemStack itemStack, LootContext context) {
        HolderGetter<SchematicVariant> variants = context.getResolver().lookupOrThrow(GalacticraftRegistries.Keys.SCHEMATIC);
        HolderSet<SchematicVariant> holders = HolderSet.direct(variants::getOrThrow, SchematicVariants.getVariants());
        List<Holder<SchematicVariant>> candidates = holders
                .stream()
                .filter(holder -> holder.value().tier() == this.featureTier)
                .toList();

        if (candidates.isEmpty()) {
            throw new IllegalStateException("No schematic variants found for tier " + this.featureTier);
        }

        Holder<SchematicVariant> result = candidates.size() > 1
                ? candidates.get(context.getRandom().nextInt(candidates.size() - 1))
                : candidates.getFirst();

        itemStack.set(GalacticraftDataComponents.SCHEMATIC, result);

        return itemStack;
    }
}
