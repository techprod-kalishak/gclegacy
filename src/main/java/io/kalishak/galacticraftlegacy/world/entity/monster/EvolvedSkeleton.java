/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.entity.monster;

import io.kalishak.galacticraftlegacy.transfer.entity.SpaceGearEquipment;
import io.kalishak.galacticraftlegacy.world.entity.GalacticraftEntityType;
import io.kalishak.galacticraftlegacy.world.entity.SpaceEntity;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class EvolvedSkeleton extends Skeleton implements EvolvedMonster {
    private static final EntityDataAccessor<Float> DATA_PITCH_ID = SynchedEntityData.defineId(EvolvedSkeleton.class, EntityDataSerializers.FLOAT);
    private final SpaceGearEquipment gear = new SpaceGearEquipment();
    private float tumbling = 0.0F;
    private float tumbleAngle = 0.0F;

    public EvolvedSkeleton(EntityType<? extends EvolvedSkeleton> type, Level level) {
        super(type, level);
    }

    public EvolvedSkeleton(Level level) {
        this(GalacticraftEntityType.EVOLVED_SKELETON.get(), level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 25)
                .add(Attributes.MOVEMENT_SPEED, 0.35);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(DATA_PITCH_ID, 0.0F);
    }

    public float getSpinY() {
        return this.entityData.get(DATA_PITCH_ID);
    }

    public void setSpinY(float pitch) {
        this.entityData.set(DATA_PITCH_ID, pitch);
    }

    @Override
    public void tick() {
        super.tick();
        this.gear.tick(this);

        if (isAlive()) {
            if (this.tumbling != 0.0F && onGround()) {
                this.tumbling = 0.0F;
            }

            if (!level().isClientSide()) {
                setSpinY(this.tumbling);
            } else {
                this.tumbling = getSpinY();
                this.tumbleAngle -= this.tumbling;

                if (this.tumbling == 0.0F && this.tumbleAngle != 0.0F) {
                    this.tumbleAngle *= 0.8F;
                    if (Math.abs(this.tumbleAngle) < 1.0F)
                        this.tumbleAngle = 0.0F;
                }
            }
        }
    }

    @Override
    public void jumpFromGround() {
        if (SpaceEntity.shouldJump(this, this::getJumpPower)) {
            super.jumpFromGround();
        }
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(random, difficulty);
        EvolvedMonster.populateDefault(this, this.gear);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.gear.serialize(output);
        output.putFloat("Tumbling", this.tumbling);
        EvolvedMonster.writeGear(this, this.gear);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.gear.deserialize(input);
        this.tumbling = input.getFloatOr("Tumbling", 0.0F);
        EvolvedMonster.readGear(this, this.gear);
    }

    @Override
    public void setTumbling(float tumbling) {
        if (tumbling != 0.0F && this.tumbling == 0.0F) {
            this.tumbling = (this.random.nextFloat() + 0.5F) * tumbling;
        } else {
            this.tumbling = 0.0F;
        }
    }

    @Override
    public float getTumblingAngle(float partialTicks) {
        float angle = this.tumbleAngle - partialTicks * this.tumbling;
        if (angle > 360.0F) {
            this.tumbleAngle -= 360.0F;
            angle -= 360.0F;
        }

        if (angle < 0F) {
            this.tumbleAngle += 360.0F;
            angle += 360.0F;
        }

        return angle;
    }
}
