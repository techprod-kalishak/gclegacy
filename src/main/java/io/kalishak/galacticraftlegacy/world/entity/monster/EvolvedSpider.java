package io.kalishak.galacticraftlegacy.world.entity.monster;

import io.kalishak.galacticraftlegacy.world.entity.GalacticraftEntityTypes;
import io.kalishak.galacticraftlegacy.world.entity.SpaceEntity;
import io.kalishak.galacticraftlegacy.world.entity.ai.attributes.GalacticraftAttributes;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class EvolvedSpider extends Spider implements EvolvedMonster {
    private static final EntityDataAccessor<Float> DATA_PITCH_ID = SynchedEntityData.defineId(EvolvedSpider.class, EntityDataSerializers.FLOAT);
    private float tumbling = 0.0F;
    private float tumbleAngle = 0.0F;

    public EvolvedSpider(EntityType<? extends EvolvedSpider> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 22.0)
                .add(Attributes.MOVEMENT_SPEED, 0.4F)
                .add(GalacticraftAttributes.THERMAL_PROTECTION, 1.0F);
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
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData groupData) {
        groupData = super.finalizeSpawn(level, difficulty, spawnReason, groupData);
        RandomSource random = level.getRandom();

        if (random.nextInt(100) == 0) {
            EvolvedSkeleton skeleton = GalacticraftEntityTypes.EVOLVED_SKELETON.get().create(level(), EntitySpawnReason.JOCKEY);

            if (skeleton != null) {
                skeleton.snapTo(getX(), getY(), getZ(), getYRot(), 0.0F);
                skeleton.finalizeSpawn(level, difficulty, spawnReason, null);
                skeleton.startRiding(this, false, false);
            }
        }

        if (groupData == null) {
            groupData = new Spider.SpiderEffectsGroupData();
            if (level.getDifficulty() == Difficulty.HARD && random.nextFloat() < 0.1F * difficulty.getSpecialMultiplier()) {
                ((Spider.SpiderEffectsGroupData) groupData).setRandomEffect(random);
            }
        }

        if (groupData instanceof Spider.SpiderEffectsGroupData spiderEffectsGroupData) {
            Holder<MobEffect> effect = spiderEffectsGroupData.effect;

            if (effect != null) {
                addEffect(new MobEffectInstance(effect, -1));
            }
        }

        return groupData;
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
