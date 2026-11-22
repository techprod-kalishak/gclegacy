package io.kalishak.galacticraftlegacy.world.entity.monster.boss;

import io.kalishak.galacticraftlegacy.advancements.GalacticraftCriteriaTriggers;
import io.kalishak.galacticraftlegacy.config.ServerConfig;
import io.kalishak.galacticraftlegacy.sounds.GalacticraftSounds;
import io.kalishak.galacticraftlegacy.world.entity.ai.attributes.GalacticraftAttributes;
import io.kalishak.galacticraftlegacy.world.entity.ai.goal.PickupThrowPlayerGoal;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class SkeletonBoss extends AbstractSpaceBoss implements RangedAttackMob {
    private PickupThrowPlayerGoal pickupThrowGoal;

    public SkeletonBoss(EntityType<? extends SkeletonBoss> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.pickupThrowGoal = new PickupThrowPlayerGoal(this);
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new RangedBowAttackGoal<>(this, 1.0, 25, 10.0F));
        this.goalSelector.addGoal(2, this.pickupThrowGoal);
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 150.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(GalacticraftAttributes.THERMAL_PROTECTION, 1.0D);
    }

    @Override
    protected void tickRidden(Player controller, Vec3 riddenInput) {
        double offsetX = Math.sin(-this.yHeadRot / Mth.RAD_TO_DEG);
        double offsetZ = Math.cos(this.yHeadRot / Mth.RAD_TO_DEG);
        double offsetY = 2 * Math.cos((this.pickupThrowGoal.throwTimer + this.pickupThrowGoal.postThrowDelay) * 0.05F);

        controller.setPos(getX() + offsetX, getY() + getAttachments().getAverage(EntityAttachment.PASSENGER).y + offsetY, getZ() + offsetZ);
    }

    @Override
    protected void tickDeath() {
        super.tickDeath();

        if (this.deathTime == 100) {
            level().playSound(null, this, GalacticraftSounds.BOSS_DEATH, SoundSource.HOSTILE, getSoundVolume(), getVoicePitch() + 1.0F);
        }
    }

    @Override
    public void knockback(double power, double xd, double zd, DamageSource source, float damage, boolean comesFromEffect) {
    }

    @Override
    public void playerTouch(Player player) {
        if (!isSilent() && !isEffectiveAi()) {
            if (!getPassengers().isEmpty() && this.pickupThrowGoal.postThrowDelay == 0 && this.pickupThrowGoal.throwTimer == 0 && getTarget() == player && this.deathTime == 0) {
                level().playSound(null, blockPosition(), GalacticraftSounds.BOSS_LAUGH.get(), SoundSource.HOSTILE, getSoundVolume(), getVoicePitch());
                player.startRiding(this, true, true);

                this.pickupThrowGoal.throwTimer = 40;
            }
        }
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }

    @Override
    protected @NonNull SoundEvent getHurtSound(DamageSource source) {
        return GalacticraftSounds.BOSS_HURT.get();
    }

    @Override
    public void tick() {
        if (level() instanceof ServerLevel && level().hasNearbyAlivePlayer(getX(), getY(), getZ(), 12.0F)) {
            List<Player> players = level().getEntitiesOfClass(Player.class, new AABB(blockPosition()).inflate(12.0D));

            for (Player player : players) {
                if (!player.isSpectator()) {
                    double distance = player.distanceToSqr(this);

                    if (distance < 400) {
                        GalacticraftCriteriaTriggers.FIND_SPACE_BOSS.get().trigger((ServerPlayer) player, this);
                    }
                }
            }
        }

        super.tick();
    }

    @Override
    public FeatureTier getChestTier() {
        return FeatureTier.TIER_1;
    }

    @Override
    protected ItemStack getKey() {
        return GalacticraftItems.MOON_DUNGEON_KEY.toStack();
    }

    @Override
    public BossEvent.BossBarColor getBarColor() {
        return BossEvent.BossBarColor.GREEN;
    }

    public float getThrowTimer() {
        return this.pickupThrowGoal == null ? 0.0F : this.pickupThrowGoal.throwTimer;
    }

    public float getPostThrowDelay() {
        return this.pickupThrowGoal == null ? 0.0F : this.pickupThrowGoal.postThrowDelay;
    }

    @SuppressWarnings("ConstantConditions")
    private void handleAttributes(float effectiveDifficulty) {
        float modifier = Math.min(0.0F, effectiveDifficulty - 1.0F);

        getAttribute(Attributes.MAX_HEALTH).setBaseValue(150.0F * ServerConfig.BOSS_HEALTH_MODIFIER.getAsDouble());
        getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25D + 0.75D * modifier);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData spawnGroupData = super.finalizeSpawn(level, difficulty, spawnReason, groupData);
        handleAttributes(difficulty.getEffectiveDifficulty());

        setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BOW));
        setItemInHand(InteractionHand.OFF_HAND, new ItemStack(Items.BOW));

        return spawnGroupData;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float power) {
        if (!getPassengers().isEmpty()) {
            return;
        }

        ItemStack bowItem = getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof BowItem));
        ItemStack projectile = getProjectile(bowItem);
        AbstractArrow arrow = ProjectileUtil.getMobArrow(this, projectile, power, bowItem);

        if (bowItem.getItem() instanceof ProjectileWeaponItem weaponItem) {
            arrow = weaponItem.customArrow(arrow, projectile, bowItem);
        }

        double xd = target.getX() - getX();
        double yd = target.getBoundingBox().minY + (double) (target.getBbHeight() / 3.0F) - arrow.getY();
        double zd = target.getZ() - getZ();
        double distance = Mth.sqrt((float) (xd * xd + zd * zd));

        if (level() instanceof ServerLevel serverLevel) {
            Projectile.spawnProjectileUsingShoot(arrow, serverLevel, projectile, xd, yd + distance * 0.20000000298023224D, zd, 1.6F, rangedAttackUncertainty(serverLevel));
        }

        playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.random.nextFloat() * 0.4F + 0.8F));
    }

    @Override
    public boolean canUseNonMeleeWeapon(ItemStack item) {
        return item.is(Items.BOW);
    }
}
