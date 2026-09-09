/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.entity.vehicle.rocket;

import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.attachment.entity.PlayerSpaceData;
import io.kalishak.galacticraftlegacy.attachment.entity.TransitionalRocketInfo;
import io.kalishak.galacticraftlegacy.galaxies.environment.CelestialBodyInfo;
import io.kalishak.galacticraftlegacy.config.CommonConfig;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.level.GalacticraftParticleTypes;
import io.kalishak.galacticraftlegacy.world.level.telemetry.GloballyReferencedEntity;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class Tier1Rocket extends TieredRocket {
    public Tier1Rocket(EntityType<?> entityType, Level level, Type type, @Nullable Player owner) {
        super(entityType, level, FeatureTier.TIER_1, type, GalacticraftItems.TIER_1_ROCKET::value, owner);
    }

    public Tier1Rocket(EntityType<?> entityType, Level level) {
        super(entityType, level, FeatureTier.TIER_1, Type.DEFAULT, GalacticraftItems.TIER_1_ROCKET::value);
    }

    @Override
    public void tick() {
        super.tick();

        int timeUntilLaunch = this.launchCooldown >= 100 ? Math.abs(this.launchCooldown / 100) : 1;

        if (getLaunchPhase() == LaunchPhase.IGNITED
                && this.random.nextInt(timeUntilLaunch) == 0
                && !CommonConfig.DISABLE_ROCKET_PARTICLES.get()
                && hasEnoughFuel(this, 1)
        ) {
            spawnLaunchParticles(isLaunched());
        }

        if (isLaunched()) {
            CelestialBodyInfo celestialBodyInfo = CelestialBodyInfo.getFromLevel(level());

            if (celestialBodyInfo != null) {
                if (getLaunchPhase() == LaunchPhase.LAUNCHED) {
                    double d = getTimeSinceLaunch() / 150.0D;

                    if (celestialBodyInfo.atmosphereInfo().getGasComposition().isEmpty()) {
                        d = Math.min(d * 1.2D, 1.6D);
                    } else {
                        d = Math.min(d, 1);
                    }

                    if (d != 0.0) {
                        move(MoverType.SELF, new Vec3(0.0D, -d * Math.cos(Math.toDegrees(getYRot() - 180.0D)), 0.0D));
                    }
                } else if (getLaunchPhase() == LaunchPhase.LANDING) {
                    move(MoverType.SELF, new Vec3(0.0D, getDeltaMovement().y - 0.008D, 0.0D));
                }

                double speedMultiplier = celestialBodyInfo.fuelUsageMultiplier().orElse(1.0F);

                if (getTimeSinceLaunch() % Mth.floor(3 * speedMultiplier) == 0) {
                    if (!AbstractAutoRocket.consumeFuel(this, 1)) {
                        stopRocketSound();
                    }
                }
            } else {
                setLaunchPhase(LaunchPhase.UNIGNITED);
            }
        }

        if (!hasEnoughFuel(this, 1) && isLaunched()) {
            double fallingModifier = Math.abs(Math.sin(getTimeSinceLaunch() / 1000.0D));

            if (fallingModifier / 10.0D != 0.0D) {
                move(MoverType.SELF, new Vec3(0.0D, -fallingModifier / 20.0D, 0.0D));
            }
        }
    }

    @Override
    public void onTransition(ServerPlayer serverPlayer) {
        PlayerSpaceData spaceData = serverPlayer.getData(GalacticraftAttachments.PLAYER_SPACE_DATA);
        DataComponentPatch.Builder patchBuilder = DataComponentPatch.builder();
        FluidStack fuel = FluidUtil.getStack(this.fluid, 0);

        if (!fuel.isEmpty()) {
            patchBuilder = patchBuilder.set(GalacticraftDataComponents.FLUID_TANK.get(), SimpleFluidContent.copyOf(fuel));
        }

        if (!ResourceHandlerUtil.isEmpty(this.items)) {
            patchBuilder = patchBuilder.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(this.items.copyToList()));
        }

        TransitionalRocketInfo rocketInfo = new TransitionalRocketInfo(
                getRocketType(),
                new ItemStackTemplate(this.droppedItemSupplier.get()).apply(patchBuilder.build()),
                fuel.amount()
        );
        spaceData.setTransitionalRocket(rocketInfo);
    }

    @Override
    public float getZoom() {
        return 0;
    }

    @Override
    public boolean thirdPerson() {
        return true;
    }
    protected void spawnLaunchParticles(boolean launched)
    {
        if (!isRemoved())
        {
            double sinPitch = Math.sin(Math.toDegrees(getYRot()));
            double xMotion = 2 * Math.sin(Math.toDegrees(getXRot())) * sinPitch;
            double zMotion = 2 * Math.sin(Math.toDegrees(getXRot())) * sinPitch;
            double yMotion = 2 * Math.cos(Math.toDegrees(getYRot() - 180.0F));

            if (getLaunchPhase() == LaunchPhase.LANDING && getTargetPos() != null) {
                double modifier = getY() - getTargetPos().pos().getY();
                modifier = Math.clamp(modifier, 120.0, 300.0);
                xMotion *= modifier / 100.0D;
                yMotion *= modifier / 100.0D;
                zMotion *= modifier / 100.0D;
            }

            double y = this.yOld + (getY() - this.yOld) + yMotion - getDeltaMovement().y + 1.2D;
            double x2 = getX() + xMotion - getDeltaMovement().x;
            double z2 = getZ() + zMotion - getDeltaMovement().z;

            LivingEntity rider = !getPassengers().isEmpty() && getPassengers().getFirst() instanceof LivingEntity livingEntity ? livingEntity : null;

            if (isLaunched()) {
                level().addParticle(GalacticraftParticleTypes.LAUNCH_FLAME.get(), x2 + 0.4 - this.random.nextDouble() / 10.0D, y, z2 + 0.4 - this.random.nextDouble() / 10.0D, xMotion, yMotion, zMotion);
                level().addParticle(GalacticraftParticleTypes.LAUNCH_FLAME.get(), x2 - 0.4 - this.random.nextDouble() / 10.0D, y, z2 + 0.4 - this.random.nextDouble() / 10.0D, xMotion, yMotion, zMotion);
                level().addParticle(GalacticraftParticleTypes.LAUNCH_FLAME.get(), x2 - 0.4 - this.random.nextDouble() / 10.0D, y, z2 + 0.4 - this.random.nextDouble() / 10.0D, xMotion, yMotion, zMotion);
                level().addParticle(GalacticraftParticleTypes.LAUNCH_FLAME.get(), x2 + 0.4 - this.random.nextDouble() / 10.0D, y, z2 + 0.4 - this.random.nextDouble() / 10.0D, xMotion, yMotion, zMotion);
                level().addParticle(GalacticraftParticleTypes.FLYING_FLAME.get(), x2, y, z2, xMotion, yMotion, zMotion);
                level().addParticle(GalacticraftParticleTypes.FLYING_FLAME.get(), x2 + 0.4D, y, z2, xMotion, yMotion, zMotion);
                level().addParticle(GalacticraftParticleTypes.FLYING_FLAME.get(), x2 - 0.0D, y, z2, xMotion, yMotion, zMotion);
                level().addParticle(GalacticraftParticleTypes.FLYING_FLAME.get(), x2, y, z2 + 0.4D, xMotion, yMotion, zMotion);
                level().addParticle(GalacticraftParticleTypes.FLYING_FLAME.get(), x2, y, z2 - 0.4D, xMotion, yMotion, zMotion);

            } else if (this.ownTicks % 2 == 0) {
                y += 0.6D;

                level().addParticle(GalacticraftParticleTypes.LAUNCH_FLAME.get(), x2 + 0.4D - this.random.nextDouble() / 10.0D, y, z2 + 0.4D - this.random.nextDouble() / 10.0D, this.random.nextDouble() / 2.0D - 0.25D, 0.0D, this.random.nextDouble() / 2.0D - 0.25D);
                level().addParticle(GalacticraftParticleTypes.LAUNCH_FLAME.get(), x2 - 0.4D - this.random.nextDouble() / 10.0D, y, z2 + 0.4D - this.random.nextDouble() / 10.0D, this.random.nextDouble() / 2.0D - 0.25D, 0.0D, this.random.nextDouble() / 2.0D + 0.25D);
                level().addParticle(GalacticraftParticleTypes.LAUNCH_FLAME.get(), x2 - 0.4D - this.random.nextDouble() / 10.0D, y, z2 + 0.4D - this.random.nextDouble() / 10.0D, this.random.nextDouble() / 2.0D - 0.25D, 0.0D, this.random.nextDouble() / 2.0D + 0.25D);
                level().addParticle(GalacticraftParticleTypes.LAUNCH_FLAME.get(), x2 + 0.4D - this.random.nextDouble() / 10.0D, y, z2 + 0.4D - this.random.nextDouble() / 10.0D, this.random.nextDouble() / 2.0D - 0.25D, 0.0D, this.random.nextDouble() / 2.0D - 0.25D);
            }
        }
    }

    @Override
    public boolean inFlight() {
        return getLaunchPhase() == LaunchPhase.LAUNCHED && this.fluid.getAmount() > 0;
    }

    @Override
    public int getPreLaunchDelay() {
        return 400;
    }

    @Override
    protected ItemStack getDropAsRocket() {
        ItemStack drop = new ItemStack(this.droppedItemSupplier.get());
        FluidStack fuel = this.fluid.getFluidStack();

        if (!fuel.isEmpty()) {
            drop.set(GalacticraftDataComponents.FLUID_TANK, SimpleFluidContent.copyOf(fuel));
        }

        return drop;
    }

    @Override
    public Collection<ItemEntity> captureDrops() {
        return List.of();
    }

    @Override
    public GloballyReferencedEntity<AbstractSpaceShip> asReference() {
        return new GloballyReferencedEntity<>(
                EntityReference.of(this),
                level().dimension(),
                blockPosition()
        );
    }
}
