/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.entity;

import io.kalishak.galacticraftlegacy.world.attribute.GalacticraftEnvironmentAttributes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
import org.jspecify.annotations.NonNull;

import java.util.function.Function;

public interface SpaceEntity {
    static float getGravity(@NonNull Level level) {
        return level.environmentAttributes().getDimensionValue(GalacticraftEnvironmentAttributes.GRAVITY.get());
    }

    static boolean spaceJump(Entity jumper, Function<Float, Float> jumpPowerGetter) {
        float dimensionGravity = getGravity(jumper.level());

        if (dimensionGravity == 0.08F) {
            float jumpPower = jumpPowerGetter.apply((float) (dimensionGravity / 0.08D));

            Vec3 movement = jumper.getDeltaMovement();

            jumper.setDeltaMovement(movement.x, Math.max(jumpPower, movement.y), movement.z);

            if (jumper.isSprinting()) {
                float angle = jumper.getYRot() * (float) (Math.PI / 180.0D);
                jumper.addDeltaMovement(new Vec3(-Mth.sin(angle) * 0.2D, 0.0D, Mth.cos(angle) * 0.2D));
            }

            jumper.needsSync = true;

            if (jumper instanceof LivingEntity livingJumper) {
                CommonHooks.onLivingJump(livingJumper);
            }

            return true;
        }

        return false;
    }

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
