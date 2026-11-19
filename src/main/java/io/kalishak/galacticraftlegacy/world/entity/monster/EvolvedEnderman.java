package io.kalishak.galacticraftlegacy.world.entity.monster;

import io.kalishak.galacticraftlegacy.world.entity.GalacticraftEntityTypes;
import io.kalishak.galacticraftlegacy.world.entity.SpaceEntity;
import io.kalishak.galacticraftlegacy.world.entity.ai.attributes.GalacticraftAttributes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enderman;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class EvolvedEnderman extends Enderman implements EvolvedMonster {
    private static final EntityDataAccessor<Float> DATA_PITCH_ID = SynchedEntityData.defineId(EvolvedEnderman.class, EntityDataSerializers.FLOAT);
    private float tumbling = 0.0F;
    private float tumbleAngle = 0.0F;

    public EvolvedEnderman(EntityType<? extends EvolvedEnderman> type, Level level) {
        super(type, level);
    }

    public EvolvedEnderman(Level level) {
        super(GalacticraftEntityTypes.EVOLVED_ENDERMAN.get(), level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 45.0)
                .add(Attributes.MOVEMENT_SPEED, 0.34F)
                .add(Attributes.ATTACK_DAMAGE, 7.0)
                .add(Attributes.FOLLOW_RANGE, 64.0)
                .add(Attributes.STEP_HEIGHT, 1.0)
                .add(GalacticraftAttributes.THERMAL_PROTECTION, 1.0);
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
    public void aiStep() {
        super.aiStep();

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
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putFloat("Tumbling", this.tumbling);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.tumbling = input.getFloatOr("Tumbling", 0.0F);
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
