/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.storage.loot.functions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.component.SchematicVariant;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public class SetSchematicVariantFunction extends LootItemConditionalFunction {
    public static final MapCodec<SetSchematicVariantFunction> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> commonFields(instance)
                    .and(SchematicVariant.CODEC.fieldOf("variant").forGetter(func -> func.schematicVariant)
    ).apply(instance, SetSchematicVariantFunction::new));
    private final Holder<SchematicVariant> schematicVariant;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private SetSchematicVariantFunction(Optional<Holder<LootItemCondition>> condition, Holder<SchematicVariant> schematicVariant) {
        super(condition);
        this.schematicVariant = schematicVariant;
    }

    public static Builder<?> setVariant(Holder<SchematicVariant> schematicVariant) {
        return simpleBuilder(i -> new SetSchematicVariantFunction(i, schematicVariant));
    }

    @Override
    public @NonNull MapCodec<SetSchematicVariantFunction> codec() {
        return MAP_CODEC;
    }

    @Override
    protected @NonNull ItemStack run(@NonNull ItemStack itemStack, LootContext context) {
        if (!itemStack.is(GalacticraftItems.SCHEMATIC)) {
            return ItemStack.EMPTY;
        }

        itemStack.set(GalacticraftDataComponents.SCHEMATIC, this.schematicVariant);

        return itemStack;
    }
}
