package io.kalishak.galacticraftlegacy.client.renderer.item.properties.conditional;

import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.item.component.HotContent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record IsHotProperty() implements ConditionalItemModelProperty {
    public static final MapCodec<IsHotProperty> MAP_CODEC = MapCodec.unit(new IsHotProperty());

    @Override
    public MapCodec<IsHotProperty> type() {
        return MAP_CODEC;
    }

    @Override
    public boolean get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed, ItemDisplayContext displayContext) {
        HotContent hotContent = itemStack.get(GalacticraftDataComponents.HOT_CONTENT);

        return hotContent != null && hotContent.getHeatLevel() > 0;
    }
}
