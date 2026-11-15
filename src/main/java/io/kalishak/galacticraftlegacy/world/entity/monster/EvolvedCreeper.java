package io.kalishak.galacticraftlegacy.world.entity.monster;

import io.kalishak.galacticraftlegacy.world.entity.GalacticraftEntityTypes;
import io.kalishak.galacticraftlegacy.world.entity.SpaceEntity;
import io.kalishak.galacticraftlegacy.world.entity.ai.attributes.GalacticraftAttributes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class EvolvedCreeper extends Creeper implements EvolvedMonster {
    private static final EntityDataAccessor<Float> DATA_PITCH_ID = SynchedEntityData.defineId(EvolvedCreeper.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(EvolvedCreeper.class, EntityDataSerializers.BOOLEAN);
    private static final Identifier SPEED_MODIFIER_BABY_ID = Identifier.withDefaultNamespace("baby");
    private static final AttributeModifier SPEED_MODIFIER_BABY = new AttributeModifier(
            SPEED_MODIFIER_BABY_ID, 0.5D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE
    );
    private static final EntityDimensions BABY_DIMENSIONS = EntityDimensions.scalable(0.35F, 1.11F)
            .withEyeHeight(0.775F)
            .withAttachments(EntityAttachments.builder().attach(EntityAttachment.VEHICLE, 0.0F, 0.1875F, 0.0F));
    private float tumbling = 0.0F;
    private float tumbleAngle = 0.0F;

    public EvolvedCreeper(EntityType<? extends EvolvedCreeper> type, Level level) {
        super(type, level);
    }

    public EvolvedCreeper(Level level) {
        super(GalacticraftEntityTypes.EVOLVED_CREEPER.get(), level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Creeper.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 25.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(GalacticraftAttributes.THERMAL_PROTECTION, 1.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(DATA_PITCH_ID, 0.0F);
        entityData.define(DATA_BABY_ID, false);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        if (DATA_BABY_ID.equals(accessor)) {
            refreshDimensions();
        }

        super.onSyncedDataUpdated(accessor);
    }

    @Override
    public void thunderHit(ServerLevel level, LightningBolt lightningBolt) {
        if (isBaby()) {
            setRemainingFireTicks(getRemainingFireTicks() + 1);
            if (getRemainingFireTicks() == 0) {
                igniteForSeconds(8.0F);
            }

            hurtServer(level, damageSources().lightningBolt(), lightningBolt.getDamage());

        } else {
            super.thunderHit(level, lightningBolt);
        }
    }

    public float getSpinY() {
        return this.entityData.get(DATA_PITCH_ID);
    }

    @Override
    public boolean isBaby() {
        return this.entityData.get(DATA_BABY_ID);
    }

    public void setSpinY(float pitch) {
        this.entityData.set(DATA_PITCH_ID, pitch);
    }

    @Override
    public void setBaby(boolean isBaby) {
        this.entityData.set(DATA_BABY_ID, isBaby);

        if (!level().isClientSide()) {
            AttributeInstance speed = getAttribute(Attributes.MOVEMENT_SPEED);

            if (speed != null) {
                speed.removeModifier(SPEED_MODIFIER_BABY_ID);

                if (isBaby) {
                    speed.addTransientModifier(SPEED_MODIFIER_BABY);
                }
            }
        }
    }

    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        if (this.isBaby()) {
            this.xpReward = (this.xpReward * 5 / 2);
        }

        return super.getBaseExperienceReward(level);
    }

    @Override
    public void tick() {
        super.tick();

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
        output.putBoolean("IsBaby", isBaby());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.tumbling = input.getFloatOr("Tumbling", 0.0F);
        setBaby(input.getBooleanOr("IsBaby", false));
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

    @Override
    protected EntityDimensions getDefaultDimensions(Pose pose) {
        return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
    }
}
