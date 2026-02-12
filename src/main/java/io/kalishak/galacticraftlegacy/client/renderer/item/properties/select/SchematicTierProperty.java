package io.kalishak.galacticraftlegacy.client.renderer.item.properties.select;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.item.component.SchematicContent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.core.HolderLookup;
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
        if (level != null) {
            SchematicContent schematic = stack.getOrDefault(GalacticraftDataComponents.SCHEMATIC, SchematicContent.DEFAULT);
            HolderLookup.Provider registries = level.registryAccess();

            return schematic.schematic().unwrap(registries).map(schematicVariantHolder -> schematicVariantHolder.value().tier()).orElse(FeatureTier.TIER_1);
        }

        return FeatureTier.TIER_1;
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
