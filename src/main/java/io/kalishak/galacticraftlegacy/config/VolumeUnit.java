package io.kalishak.galacticraftlegacy.config;

import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public enum VolumeUnit {
    MILI_BUCKETS("mB", 1.0F),
    BUCKETS("B", 1.0F / 1000.0F),
    PERCENTAGE("%", 0.0F);
    private final String unit;
    private final float scale;

    VolumeUnit(String unit, float scale) {
        this.unit = unit;
        this.scale = scale;
    }

    public String getUnit() {
        return this.unit;
    }

    private int calculate(int value) {
        return Mth.floor(value * this.scale);
    }

    public Component toLiteralComponent(int value, int capacity) {
        if (this == PERCENTAGE) {
            int perc = (int) Math.floor(value / (float) capacity);
            return Component.literal(perc + "%");
        }

        return Component.literal(calculate(value) + " " + this.unit);
    }

    public Component toLiteralComponent(int value) {
        return Component.literal(calculate(value) + " " + this.unit);
    }
}
