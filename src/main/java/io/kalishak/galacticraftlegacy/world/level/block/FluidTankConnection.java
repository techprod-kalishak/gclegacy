package io.kalishak.galacticraftlegacy.world.level.block;

import net.minecraft.util.StringRepresentable;

public enum FluidTankConnection implements StringRepresentable {
    NONE("none"),
    DOWN("down"),
    UP("up"),
    BOTH("both");

    private final String name;

    FluidTankConnection(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
