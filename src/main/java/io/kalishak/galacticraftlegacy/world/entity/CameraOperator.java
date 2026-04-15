/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.entity;

public interface CameraOperator {
    float getZoom();

    boolean thirdPerson();

    float getRotationOffset();
}
