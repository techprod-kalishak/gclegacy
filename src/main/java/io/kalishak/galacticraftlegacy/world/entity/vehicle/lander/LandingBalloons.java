/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.entity.vehicle.lander;

import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class LandingBalloons extends AbstractLander {
    private int groundHitCount;
    private float rotationPitchSpeed;
    private float rotationYawSpeed;

    public LandingBalloons(EntityType<?> entityType, Level level) {
        super(entityType, level, 1);
    }

    @Override
    public NonNullList<ItemStack> getDrops() {
        return null;
    }

    @Override
    public boolean shouldMove() {
        return false;
    }

    @Override
    public boolean shouldSpawnParticles() {
        return false;
    }

    @Override
    public Map<Vec3, Vec3> getParticleMap() {
        return Map.of();
    }

    @Override
    public ParticleOptions getParticle(RandomSource rand) {
        return null;
    }

    @Override
    public void tickInAir() {

    }

    @Override
    public void tickOnGround() {

    }

    @Override
    public void onGroundHit() {

    }

    @Override
    public Vec3 getMotionVec() {
        return null;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {

    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {

    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {

    }
}
