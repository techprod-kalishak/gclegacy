/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.entity;

import net.minecraft.world.phys.Vec3;

public interface TumblingEntity {
    void setTumbling(float angle);
    float getTumblingAngle(float partialTicks);

    Vec3 getDeltaMovement();

    default float getTumbleMotionX() {
        double motion = getDeltaMovement().horizontalDistance();

        if (motion == 0.0D) return 1.0F;

        return (float) (getDeltaMovement().x / motion);
    }

    default float getTumbleMotionZ() {
        double motion = getDeltaMovement().horizontalDistance();

        if (motion == 0.0D) return 0.0F;

        return (float) (getDeltaMovement().z / motion);
    }
}
