/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.entity.vehicle;

import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.attachment.entity.AdvancedMovement;
import io.kalishak.galacticraftlegacy.world.entity.EntityWithInventory;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class MovingEntity extends EntityWithInventory {
    protected long ticks = 0;
    public float currentDamage;
    public int timeSinceHit;
    public int rockDirection;
    public int posRotIncrements;

    protected boolean lastOnGround;

    public MovingEntity(EntityType<?> entityType, Level level, int inventorySize) {
        super(entityType, level, inventorySize);
        this.noPhysics = true;
    }

    public MovingEntity(EntityType<?> entityType, Level level, int inventorySize, double x, double y, double z) {
        this(entityType, level, inventorySize);
        setPos(x, y, z);
    }

    @Override
    public void tick() {
        this.ticks++;

        super.tick();

        if (level().isClientSide() && (getPassengers().isEmpty() || !hasExactlyOnePlayerPassenger())) {
            if (this.posRotIncrements > 0) {
                AdvancedMovement advancedMovement = getData(GalacticraftAttachments.ADVANCED_MOVEMENT);
                double newX = getX() + (advancedMovement.getAdvancedPositionX() - getX()) / this.posRotIncrements;
                double newY = getY() + (advancedMovement.getAdvancedPositionY() - getY()) / this.posRotIncrements;
                double newZ = getZ() + (advancedMovement.getAdvancedPositionZ() - getZ()) / this.posRotIncrements;
                double angle = Mth.wrapDegrees(advancedMovement.getAdvancedYaw() - getXRot());

                setXRot((float) (getXRot() + angle / this.posRotIncrements));
                setYRot((float) (getYRot() + (advancedMovement.getAdvancedPitch() - getYRot()) / this.posRotIncrements));
                --this.posRotIncrements;
                setPos(newX, newY, newZ);
                setData(GalacticraftAttachments.ADVANCED_MOVEMENT, advancedMovement);
            }
        }

        if (this.timeSinceHit > 0) {
            this.timeSinceHit--;
        }

        if (this.currentDamage > 0) {
            this.currentDamage--;
        }

        if (level().isClientSide() && shouldSpawnParticles()) {
            addParticles(getParticleMap());
        }

        if (onGround()) {
            tickOnGround();
        } else {
            tickInAir();
        }

        if (level().isClientSide()) {
            setDeltaMovement(getDeltaMovement());
        }

        move(MoverType.SELF, getDeltaMovement());

        if (onGround() && !this.lastOnGround) {
            onGroundHit();
        }

        setOldPos();
        this.lastOnGround = onGround();
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith(@Nullable Entity entity) {
        return isAlive();
    }

    @Override
    public void onPassengerTurned(Entity entityToUpdate) {
        if (hasPassenger(entityToUpdate)) {
            double xOffset = Math.cos(this.getXRot() / Constants.RADIANS_TO_DEGREES + 114.8D) * -0.5D;
            double zOffset = Math.sin(this.getXRot() / Constants.RADIANS_TO_DEGREES + 114.8D) * -0.5D;
            entityToUpdate.setPos(this.getX() + xOffset, this.getY() + getVehicleAttachmentPoint(entityToUpdate).y, this.getZ() + zOffset);
        }
    }

    protected boolean forceGroundUpdate() {
        return true;
    }

    @Override
    public void animateHurt(float yaw) {
        this.rockDirection = -this.rockDirection;
        this.timeSinceHit = 10;
        this.currentDamage *= 5;
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float v) {
        if (!isAlive() || damageSource.is(DamageTypes.CACTUS) || isInvulnerableToBase(damageSource)) {
            return true;
        }

        Entity sourceEntity = damageSource.getEntity();

        if (isInvulnerable() || getY() > 300 || (sourceEntity instanceof LivingEntity && !(sourceEntity instanceof Player))) {
            return false;
        }

        this.rockDirection = -this.rockDirection;
        this.timeSinceHit = 10;
        this.currentDamage = this.currentDamage + v * 10;

        if (sourceEntity instanceof Player player && player.isCreative()) {
            this.currentDamage = 100;
        }

        if (this.currentDamage > 70) {
            if (!getPassengers().isEmpty()) {
                ejectPassengers();

                return false;
            }
        }

        dropItems(serverLevel);
        kill(serverLevel);


        return true;
    }

    public abstract NonNullList<ItemStack> getDrops();

    public abstract boolean shouldMove();

    public abstract boolean shouldSpawnParticles();

    public abstract Map<Vec3, Vec3> getParticleMap();

    public abstract ParticleOptions getParticle(RandomSource rand);

    public abstract void tickInAir();

    public abstract void tickOnGround();

    public abstract void onGroundHit();

    public abstract Vec3 getMotionVec();

    public abstract boolean isInvulnerableTo(DamageSource damageSource);

    public void dropItems(ServerLevel serverLevel) {
        Containers.dropContents(serverLevel, blockPosition(), getDrops());
    }

    @Override
    public void move(MoverType type, Vec3 movement) {
        if (shouldMove()) {
            super.move(type, movement);
        }
    }

    public void addParticles(Map<Vec3, Vec3> map) {
        map.forEach((pos, motion) -> level().addParticle(getParticle(this.random), pos.x, pos.y, pos.z, motion.x, motion.y, motion.z));
    }
}
