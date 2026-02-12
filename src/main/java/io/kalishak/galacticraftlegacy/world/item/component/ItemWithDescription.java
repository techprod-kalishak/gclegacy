package io.kalishak.galacticraftlegacy.world.item.component;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.function.Consumer;
import java.util.function.Supplier;

public record ItemWithDescription(String translationKey) implements TooltipProvider {
    public static final Codec<ItemWithDescription> CODEC = Codec.STRING.xmap(ItemWithDescription::new, ItemWithDescription::translationKey);
    public static final StreamCodec<ByteBuf, ItemWithDescription> STREAM_CODEC = ByteBufCodecs.STRING_UTF8.map(ItemWithDescription::new, ItemWithDescription::translationKey);

    public static Supplier<Item.Properties> withDescription(Supplier<Item.Properties> properties, Identifier id) {
        return () -> properties.get().component(GalacticraftDataComponents.ITEM_WITH_DESCRIPTION, new ItemWithDescription(id.toLanguageKey("item", "desc")));
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag flag, DataComponentGetter componentGetter) {
        if (!flag.hasShiftDown()) {
            tooltipAdder.accept(Component.translatable("item.galacticraftlegacy.press_shift").withStyle(ChatFormatting.GRAY));
        } else {
            tooltipAdder.accept(Component.translatable(this.translationKey).withStyle(ChatFormatting.GRAY));
        }
    }
}
