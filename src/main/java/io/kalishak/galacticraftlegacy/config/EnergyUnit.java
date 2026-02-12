package io.kalishak.galacticraftlegacy.config;

import net.minecraft.util.Mth;

public enum EnergyUnit {
    GIGA_JOULES("gJ", 1.0F),
    JOULES("J", 100.0F),
    FORGE_ENERGY("FE", 0.1F);

    private final String unit;
    private final float scale;

    EnergyUnit(String unit, float scale) {
        this.unit = unit;
        this.scale = scale;
    }

    public String getUnit() {
        return this.unit;
    }

    public int calculate(int value) {
        return Mth.floor(value * this.scale);
    }
}
