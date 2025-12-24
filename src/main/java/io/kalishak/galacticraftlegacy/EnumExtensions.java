package io.kalishak.galacticraftlegacy;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Rarity;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

import java.util.function.UnaryOperator;

public class EnumExtensions {
    public static final EnumProxy<Rarity> RARITY_GALAXY = new  EnumProxy<>(Rarity.class, 4, "galacticraftlegacy:galaxy", (UnaryOperator<Style>) (style) -> style.withColor(ChatFormatting.BLUE));
}
