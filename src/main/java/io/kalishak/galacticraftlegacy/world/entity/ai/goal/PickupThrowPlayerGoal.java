package io.kalishak.galacticraftlegacy.world.entity.ai.goal;

import io.kalishak.galacticraftlegacy.world.entity.monster.boss.SkeletonBoss;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.SwingAnimation;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.EnumSet;

public class PickupThrowPlayerGoal extends Goal {
    private final SkeletonBoss skeletonBoss;
    public int throwTimer;
    public int postThrowDelay = 20;
    public @Nullable Entity thrownEntity;
    private Path path;
    private double pathedTargetX;
    private double pathedTargetY;
    private double pathedTargetZ;
    private int ticksUntilNextPathRecalculation;

    public PickupThrowPlayerGoal(SkeletonBoss skeletonBoss) {
        this.skeletonBoss = skeletonBoss;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if (this.throwTimer != 0 || this.postThrowDelay != 0) {
            return false;
        }

        LivingEntity target = this.skeletonBoss.getTarget();

        if (!(target instanceof Player)) {
            return false;
        }

        this.path = this.skeletonBoss.getNavigation().createPath(target, 0);
        return this.path != null && this.skeletonBoss.isWithinMeleeAttackRange(target);
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.skeletonBoss.getTarget();

        if (!(target instanceof Player) || !target.isAlive()) {
            return false;
        } else if (this.skeletonBoss.getNavigation().isDone()) {
            return false;
        }

        return this.skeletonBoss.isWithinHome(target.blockPosition()) && !(target instanceof Player player && (player.isSpectator() || player.isCreative()));
    }

    @Override
    public void start() {
        this.skeletonBoss.getNavigation().moveTo(this.path, 1.0F);
        this.skeletonBoss.setAggressive(true);
        this.ticksUntilNextPathRecalculation = 0;
        this.throwTimer = 0;
        this.postThrowDelay = 0;
    }

    @Override
    public void stop() {
        LivingEntity target = this.skeletonBoss.getTarget();

        if (target != null && !EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target)) {
            this.skeletonBoss.setTarget(null);
        }

        this.skeletonBoss.setAggressive(false);
        this.skeletonBoss.getNavigation().stop();
        this.throwTimer = 0;
        this.postThrowDelay = 20;
        this.thrownEntity = null;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();

        LivingEntity target = this.skeletonBoss.getTarget();

        if (target != null) {
            this.skeletonBoss.getLookControl().setLookAt(target, 30.0F, 30.0F);
            this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);

            if (this.skeletonBoss.getSensing().hasLineOfSight(target) && this.ticksUntilNextPathRecalculation <= 0 && (
                    this.pathedTargetX == 0.0 && this.pathedTargetY == 0.0 && this.pathedTargetZ == 0.0
                            || target.distanceToSqr(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 1.0
                            || this.skeletonBoss.getRandom().nextFloat() < 0.05F
            )) {
                this.pathedTargetX = target.getX();
                this.pathedTargetY = target.getY();
                this.pathedTargetZ = target.getZ();
                this.ticksUntilNextPathRecalculation = 4 + this.skeletonBoss.getRandom().nextInt(7);
                double targetDistanceSqr = this.skeletonBoss.distanceToSqr(target);
                if (targetDistanceSqr > 1024.0) {
                    this.ticksUntilNextPathRecalculation += 10;
                } else if (targetDistanceSqr > 256.0) {
                    this.ticksUntilNextPathRecalculation += 5;
                }

                if (!this.skeletonBoss.getNavigation().moveTo(target, 0, 1.0D)) {
                    this.ticksUntilNextPathRecalculation += 15;
                }

                this.ticksUntilNextPathRecalculation = this.adjustedTickDelay(this.ticksUntilNextPathRecalculation);
            }
        }

        if (this.throwTimer > 0) {
            this.throwTimer--;
        }

        if (this.postThrowDelay > 0) {
            this.postThrowDelay--;
        }

        if (!this.skeletonBoss.getPassengers().isEmpty() && this.throwTimer == 0) {
            this.postThrowDelay = 20;

            this.thrownEntity = this.skeletonBoss.getPassengers().getFirst();
            this.skeletonBoss.ejectPassengers();
        }

        if (this.thrownEntity != null && this.postThrowDelay == 18) {
            double d0 = this.skeletonBoss.getX() - this.thrownEntity.getX();
            double d1;

            for (d1 = this.skeletonBoss.getZ() - this.thrownEntity.getZ(); d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D) {
                d0 = (Math.random() - Math.random()) * 0.01D;
            }

            this.skeletonBoss.level().playSound(null, this.skeletonBoss.blockPosition(), SoundEvents.ARROW_SHOOT, SoundSource.HOSTILE);
            this.skeletonBoss.swing(InteractionHand.MAIN_HAND, SwingAnimation.DEFAULT);
            this.skeletonBoss.swing(InteractionHand.OFF_HAND, SwingAnimation.DEFAULT);

            float f =  Mth.sqrt((float) (d0 * d0 + d1 * d1));
            float f1 = 2.4F;
            Vec3 delta = this.thrownEntity.getDeltaMovement();
            double deltaX = delta.x / 2.0D - d0 / f * f1;
            double deltaY = delta.y / 2.0D + (double) f1 / 5.0D;
            double deltaZ = delta.z / 2.0D - d1 / f * f1;

            if (delta.y > 0.4000000059604645D) {
                deltaY = 0.4000000059604645D;
            }

            this.thrownEntity.addDeltaMovement(new Vec3(deltaX, deltaY, deltaZ));
        }
    }
}
