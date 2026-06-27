/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public class HotContent implements TooltipProvider {
    public static final MapCodec<HotContent> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("heat_level").forGetter(HotContent::getHeatLevel)
    ).apply(instance, HotContent::new));
    public static final Codec<HotContent> CODEC = MAP_CODEC.codec();
    public static final StreamCodec<ByteBuf, HotContent> STREAM_CODEC = ByteBufCodecs.INT.map(HotContent::new, HotContent::getHeatLevel);
    public static final int MAX_HEAT_LEVEL = 5000;
    public static final HotContent DEFAULT = new HotContent(MAX_HEAT_LEVEL);
    private int heatLevel;

    public HotContent(int initialHeat) {
        this.heatLevel = initialHeat;
    }

    public boolean decrease(@Nullable ItemStack stack) {
        if (this.heatLevel > 0) {
            this.heatLevel--;
            return true;
        } else if (stack != null) {
            stack.remove(GalacticraftDataComponents.HOT_CONTENT);
        }

        return false;
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
        float burnTime = Math.round(this.heatLevel / 10.0F) / 2.0F;

        consumer.accept(Component.translatable("item.hot_content.description", burnTime + "s"));
    }

    public int getHeatLevel() {
        return this.heatLevel;
    }

    public void setHeatLevel(int heatLevel) {
        this.heatLevel = Math.max(heatLevel, 0);
    }

    public float getScaledHeatLevel() {
        return (float) this.heatLevel / MAX_HEAT_LEVEL;
    }
}
