/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.config.values;

public enum OxygenTankPosition {
    TOP_RIGHT(true, true),
    TOP_LEFT(true, false),
    BOTTOM_RIGHT(false, true),
    BOTTOM_LEFT(false, false);

    private final boolean top;
    private final boolean right;

    OxygenTankPosition(boolean top, boolean right) {
        this.top = top;
        this.right = right;
    }

    public boolean onTop() {
        return this.top;
    }

    public boolean onBottom() {
        return !this.top;
    }

    public boolean onRight() {
        return this.right;
    }

    public boolean onLeft() {
        return !this.right;
    }
}
