package io.kalishak.galacticraftlegacy.world.entity.monster;

import io.kalishak.galacticraftlegacy.world.entity.SpaceEntity;
import io.kalishak.galacticraftlegacy.world.entity.ai.attributes.GalacticraftAttributes;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownSplashPotion;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import org.jspecify.annotations.NonNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class EvolvedWitch extends Monster implements RangedAttackMob, EvolvedMonster {
    private static final Identifier SPEED_MODIFIER_DRINKING_ID = Identifier.withDefaultNamespace("drinking");
    private static final AttributeModifier SPEED_MODIFIER_DRINKING = new AttributeModifier(
            SPEED_MODIFIER_DRINKING_ID, -0.25, AttributeModifier.Operation.ADD_VALUE
    );
    private static final EntityDataAccessor<Boolean> DATA_USING_ITEM = SynchedEntityData.defineId(EvolvedWitch.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> DATA_PITCH_ID = SynchedEntityData.defineId(EvolvedWitch.class, EntityDataSerializers.FLOAT);
    private float tumbling = 0.0F;
    private float tumbleAngle = 0.0F;
    private int usingTime;

    public EvolvedWitch(EntityType<? extends EvolvedWitch> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0, 60, 10.0F));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Raider.class));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, null));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(DATA_USING_ITEM, false);
        entityData.define(DATA_PITCH_ID, 0.0F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.WITCH_AMBIENT;
    }

    @Override
    protected @NonNull SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.WITCH_HURT;
    }

    @Override
    protected @NonNull SoundEvent getDeathSound() {
        return SoundEvents.WITCH_DEATH;
    }

    public void setUsingItem(boolean using) {
        getEntityData().set(DATA_USING_ITEM, using);
    }

    public boolean isDrinkingPotion() {
        return getEntityData().get(DATA_USING_ITEM);
    }

    public float getSpinY() {
        return this.entityData.get(DATA_PITCH_ID);
    }

    public void setSpinY(float pitch) {
        this.entityData.set(DATA_PITCH_ID, pitch);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 26.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(GalacticraftAttributes.THERMAL_PROTECTION, 1.0D);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void aiStep() {
        if (!level().isClientSide() && isAlive()) {
            if (isDrinkingPotion()) {
                if (this.usingTime-- <= 0) {
                    setUsingItem(false);
                    ItemStack itemStack = this.getMainHandItem();
                    setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                    PotionContents potion = itemStack.get(DataComponents.POTION_CONTENTS);

                    if (itemStack.is(Items.POTION) && potion != null) {
                        potion.forEachEffect(this::addEffect, itemStack.getOrDefault(DataComponents.POTION_DURATION_SCALE, 1.0F));
                    }

                    gameEvent(GameEvent.DRINK);
                    getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER_DRINKING.id());
                }
            } else {
                Holder<Potion> potion = null;

                if (this.random.nextFloat() < 0.15F && getFluidInteraction().isEyeInFluidMatching(this, (entity, type, _) -> entity.canDrownInFluidType(type)) && !hasEffect(MobEffects.WATER_BREATHING)) {
                    potion = Potions.WATER_BREATHING;
                } else if (this.random.nextFloat() < 0.15F
                        && (isOnFire() || getLastDamageSource() != null && getLastDamageSource().is(DamageTypeTags.IS_FIRE))
                        && !hasEffect(MobEffects.FIRE_RESISTANCE)) {
                    potion = Potions.FIRE_RESISTANCE;
                } else if (this.random.nextFloat() < 0.05F && getHealth() < getMaxHealth()) {
                    potion = Potions.HEALING;
                } else if (this.random.nextFloat() < 0.5F
                        && getTarget() != null
                        && !hasEffect(MobEffects.SPEED)
                        && getTarget().distanceToSqr(this) > 121.0) {
                    potion = Potions.SWIFTNESS;
                }

                if (potion != null) {
                    setItemSlot(EquipmentSlot.MAINHAND, PotionContents.createItemStack(Items.POTION, potion));
                    this.usingTime = getMainHandItem().getUseDuration(this);
                    setUsingItem(true);

                    if (!isSilent()) {
                        level()
                                .playSound(
                                        null,
                                        getX(),
                                        getY(),
                                        getZ(),
                                        SoundEvents.WITCH_DRINK,
                                        getSoundSource(),
                                        1.0F,
                                        0.8F + this.random.nextFloat() * 0.4F
                                );
                    }

                    AttributeInstance speed = getAttribute(Attributes.MOVEMENT_SPEED);
                    speed.removeModifier(SPEED_MODIFIER_DRINKING_ID);
                    speed.addTransientModifier(SPEED_MODIFIER_DRINKING);
                }
            }

            if (this.random.nextFloat() < 7.5E-4F) {
                level().broadcastEntityEvent(this, EntityEvent.WITCH_HAT_MAGIC);
            }
        }

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

        super.aiStep();
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
        output.putInt("UsingTime", this.usingTime);
        output.putFloat("Tumbling", this.tumbling);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.usingTime = input.getIntOr("UsingTime", 0);
        this.tumbling = input.getFloatOr("Tumbling", 0.0F);
    }

    @Override
    public void handleEntityEvent(@EntityEvent.Value byte id) {
        if (id == EntityEvent.WITCH_HAT_MAGIC) {
            for (int i = 0; i < this.random.nextInt(35) + 10; i++) {
                level()
                        .addParticle(
                                ParticleTypes.WITCH,
                                getX() + this.random.nextGaussian() * 0.13F,
                                getBoundingBox().maxY + 0.5 + this.random.nextGaussian() * 0.13F,
                                getZ() + this.random.nextGaussian() * 0.13F,
                                0.0,
                                0.0,
                                0.0
                        );
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    protected float getDamageAfterMagicAbsorb(DamageSource damageSource, float damage) {
        damage = super.getDamageAfterMagicAbsorb(damageSource, damage);

        if (this.damageContainers == null) {
            return damage;
        }

        if (damageSource.getEntity() == this) {
            this.damageContainers.peek().setReduction(DamageContainer.Reduction.INNATE_RESISTANCE, damage);
            damage = 0.0F;
        } else if (damageSource.is(DamageTypeTags.WITCH_RESISTANT_TO)) {
            this.damageContainers.peek().setReduction(DamageContainer.Reduction.INNATE_RESISTANCE, damage * 0.85F);
            damage *= 0.15F;
        }

        return damage;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float power) {
        if (!isDrinkingPotion()) {
            Vec3 targetMovement = target.getDeltaMovement();
            double xd = target.getX() + targetMovement.x - getX();
            double yd = target.getEyeY() - 1.1F - this.getY();
            double zd = target.getZ() + targetMovement.z - getZ();
            double dist = Math.sqrt(xd * xd + zd * zd);
            Holder<Potion> potion = Potions.HARMING;

            if (dist >= 8.0 && !target.hasEffect(MobEffects.SLOWNESS)) {
                potion = Potions.SLOWNESS;
            } else if (target.getHealth() >= 8.0F && !target.hasEffect(MobEffects.POISON)) {
                potion = Potions.POISON;
            } else if (dist <= 3.0 && !target.hasEffect(MobEffects.WEAKNESS) && this.random.nextFloat() < 0.25F) {
                potion = Potions.WEAKNESS;
            }

            if (level() instanceof ServerLevel serverLevel) {
                ItemStack itemStack = PotionContents.createItemStack(Items.SPLASH_POTION, potion);

                Projectile.spawnProjectileUsingShoot(
                        ThrownSplashPotion::new,
                        serverLevel,
                        itemStack,
                        this,
                        xd,
                        yd + dist * 0.2,
                        zd,
                        dist <= 2.0 ? 0.45F : 0.75F,
                        rangedAttackUncertainty(serverLevel)
                );
            }

            if (!isSilent()) {
                level()
                        .playSound(
                                null,
                                getX(),
                                getY(),
                                getZ(),
                                SoundEvents.WITCH_THROW,
                                getSoundSource(),
                                1.0F,
                                0.8F + this.random.nextFloat() * 0.4F
                        );
            }
        }
    }

    @Override
    public float rangedAttackUncertainty(Level level) {
        return 8.0F;
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
