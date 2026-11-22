package io.kalishak.galacticraftlegacy.world.entity.ai.attributes;

import net.minecraft.ChatFormatting;
import net.neoforged.neoforge.common.PercentageAttribute;

public class TwoWayPercentageAttribute extends PercentageAttribute {
    public TwoWayPercentageAttribute(String pDescriptionId, double pDefaultValue, double pMin, double pMax, double scaleFactor) {
        super(pDescriptionId, pDefaultValue, pMin, pMax, scaleFactor);
    }

    public TwoWayPercentageAttribute(String pDescriptionId, double pDefaultValue, double pMin, double pMax) {
        super(pDescriptionId, pDefaultValue, pMin, pMax);
    }

    @Override
    public ChatFormatting getStyle(boolean valueIncrease) {
        return valueIncrease ? ChatFormatting.DARK_AQUA : ChatFormatting.BLUE;
    }
}
