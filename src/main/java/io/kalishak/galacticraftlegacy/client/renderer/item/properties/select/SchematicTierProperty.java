/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.item.properties.select;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.registry.SchematicVariant;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record SchematicTierProperty() implements SelectItemModelProperty<FeatureTier> {
    public static final Codec<FeatureTier> VALUE_CODEC = FeatureTier.CODEC;
    public static final SelectItemModelProperty.Type<SchematicTierProperty, FeatureTier> TYPE = SelectItemModelProperty.Type.create(
            MapCodec.unit(SchematicTierProperty::new),
            VALUE_CODEC
    );

    @Override
    public FeatureTier get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed, ItemDisplayContext displayContext) {
        Holder<SchematicVariant> schematic = stack.get(GalacticraftDataComponents.SCHEMATIC);

        return schematic != null ? schematic.value().tier() : FeatureTier.TIER_1;
    }

    @Override
    public Codec<FeatureTier> valueCodec() {
        return VALUE_CODEC;
    }

    @Override
    public Type<SchematicTierProperty, FeatureTier> type() {
        return TYPE;
    }
}
