/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.machine.electric;

import net.minecraft.util.StringRepresentable;

public enum SolarPanelPart implements StringRepresentable {
    BASE("base"),
    POLE("pole"),
    PANEL("panel");

    private final String name;

    SolarPanelPart(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
