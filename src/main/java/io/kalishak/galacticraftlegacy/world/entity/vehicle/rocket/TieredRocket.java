/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.entity.vehicle.rocket;

import io.kalishak.galacticraftlegacy.galaxies.environment.CelestialBodyInfo;
import io.kalishak.galacticraftlegacy.config.CommonConfig;
import io.kalishak.galacticraftlegacy.data.datamap.GalacticraftDataMaps;
import io.kalishak.galacticraftlegacy.galaxies.CelestialBody;
import io.kalishak.galacticraftlegacy.galaxies.CelestialObject;
import io.kalishak.galacticraftlegacy.network.payload.ChangeCameraModePayload;
import io.kalishak.galacticraftlegacy.network.payload.EntityPlanetaryTransitionPayload;
import io.kalishak.galacticraftlegacy.network.payload.OpenGalaxySelectionScreenPayload;
import io.kalishak.galacticraftlegacy.world.entity.CameraOperator;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.FuelableDock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

public abstract class TieredRocket extends AbstractAutoRocket implements RocketTier, CameraOperator {
    protected final FeatureTier tier;
    protected final RocketTier.Type type;
    protected int launchCooldown;
    public float rumble;

    protected TieredRocket(EntityType<?> entityType, Level level, FeatureTier featureTier, RocketTier.Type type, Supplier<Item> droppedItemSupplier, Player owner) {
        super(entityType, level, droppedItemSupplier, owner, type.getAdditionalSlots(), type.isPrefueled());
        this.tier = featureTier;
        this.type = type;
        //size 0.98, 4
    }

    protected TieredRocket(EntityType<?> entityType, Level level, FeatureTier featureTier, RocketTier.Type type, Supplier<Item> droppedItemSupplier) {
        this(entityType, level, featureTier, type, droppedItemSupplier, null);
    }

    @Override
    public void tick() {
        if (isAwaitingForPlayer()) {
            if (!getPassengers().isEmpty()) {
                Entity passenger = getFirstPassenger();

                if (passenger != null) {
                    if (this.ownTicks >= 40) {
                        if (!level().isClientSide()) {
                            removePassenger(passenger);
                            passenger.startRiding(this, true, false);
                        }

                        setAwaitingForPlayer(false);
                        move(MoverType.SELF, new Vec3(0.0D, -0.5D, 0.0D));
                    } else {
                        setDeltaMovement(Vec3.ZERO);
                    }
                }
            } else {
                setDeltaMovement(Vec3.ZERO);
            }
        }

        super.tick();

        if (!level().isClientSide()) {
            if (this.launchCooldown > 0) {
                this.launchCooldown--;
            }
        }

        if (this.rumble > 0.0F) {
            this.rumble--;
        } else if (this.rumble < 0.0F) {
            this.rumble++;
        }

        final double rumbleAmount = this.rumble / (double) (37 - 3 * Math.max(getFeatureTier().getLevel(), 3));//todo check tiers

        for (Entity passenger : getPassengers()) {
            passenger.move(MoverType.SELF, getDeltaMovement().add(rumbleAmount, 0.0D, rumbleAmount));
        }

        if (getLaunchPhase() != LaunchPhase.IGNITED) {
            animateHurt(0.0F);
            this.rumble = (float) this.random.nextInt(3) - 3;
        }

        if (!level().isClientSide()) {
            setDeltaMovement(getDeltaMovement());
        }
    }

    @Override
    public Type getRocketType() {
        return this.type;
    }

    @Override
    public FeatureTier getFeatureTier() {
        return this.tier;
    }

    @Override
    public void onReachAtmosphere() {
        if (getTargetFrequency() != INVALID_FREQUENCY_ID) {
            if (level().isClientSide()) {
                stopRocketSound();
                return;
            }

            setTarget(true, getTargetFrequency());

            if (level() instanceof ServerLevel serverLevel) {
                GlobalPos globalPos = getTargetPos();

                if (globalPos != null) {
                    ResourceKey<Level> targetLevel = globalPos.dimension();

                    if (!level().dimension().equals(targetLevel)) {
                        Level otherLevel = serverLevel.getServer().getLevel(targetLevel);

                        if (otherLevel != null) {
                            boolean canReachTo = !CommonConfig.DIMENSIONS_WITH_DISABLED_ROCKETS.get().contains(targetLevel.identifier().toString()) && canAccess(otherLevel.dimensionTypeRegistration().getData(GalacticraftDataMaps.CELESTIAL_BODY_DATA));

                            if (canReachTo) {
                                if (!getPassengers().isEmpty()) {
                                    for (Entity passenger : getPassengers()) {
                                        if (passenger instanceof ServerPlayer serverPlayer) {
                                            PacketDistributor.sendToPlayer(serverPlayer, EntityPlanetaryTransitionPayload.player(serverPlayer, targetLevel, false, this));
                                        }
                                    }
                                } else {
                                    PacketDistributor.sendToPlayersTrackingEntity(this, EntityPlanetaryTransitionPayload.rocket(this, targetLevel, false));
                                }
                            }
                        }
                    } else {
                        BlockPos targetPos = getTargetPos().pos();

                        setPos(targetPos.getX() + 0.5D, targetPos.getY() + 800.0D, targetPos.getZ() + 0.5D);
                        setDeltaMovement(0, 0.1D, 0);

                        for (Entity passenger : getPassengers()) {
                            if (passenger instanceof ServerPlayer) {
                                passenger.setPos(getX(), getY(), getZ());
                                setAwaitingForPlayer(true);
                            }
                        }

                        setLaunchPhase(LaunchPhase.LANDING);
                    }
                } else {
                    kill(serverLevel);
                }
            }
        } else {
            for (Entity passenger : getPassengers()) {
                if (passenger instanceof ServerPlayer serverPlayer) {
                    onTransition(serverPlayer);
                    PacketDistributor.sendToPlayer(serverPlayer, new OpenGalaxySelectionScreenPayload(getFeatureTier()));
                }
            }
        }
    }

    public void onTransition(ServerPlayer serverPlayer) {

    }

    @Override
    protected boolean shouldCancelExplosion() {
        return hasEnoughFuel(this, 0) && Math.abs(getKnownMovement().y) < 4;
    }

    @Override
    protected void onRocketLand(BlockPos pos) {
        super.onRocketLand(pos);
        this.launchCooldown = 40;
    }

    @Override
    public boolean canSimulateMovement() {
        return true;
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 localization) {
        if (hand != InteractionHand.MAIN_HAND || getLaunchPhase() != LaunchPhase.LAUNCHED) {
            return InteractionResult.FAIL;
        }

        if (getPassengers().contains(player)) {
            if (player instanceof ServerPlayer serverPlayer) {
                PacketDistributor.sendToPlayer(serverPlayer, new ChangeCameraModePayload(true));
            }

            player.startRiding(this);

            return InteractionResult.SUCCESS_SERVER;
        }

        return super.interact(player, hand, localization);
    }

    @Override
    public @Nullable Entity teleport(TeleportTransition teleportTransition) {
        GlobalPos globalPos = getTargetPos();

        if (globalPos != null) {
            teleportTransition = new TeleportTransition(
                    teleportTransition.newLevel(),
                    new Vec3(globalPos.pos().getX() + 0.5D, globalPos.pos().getY() + 800, globalPos.pos().getZ() + 0.5D),
                    Vec3.ZERO,
                    teleportTransition.yRot(),
                    teleportTransition.xRot(),
                    teleportTransition.missingRespawnBlock(),
                    false,
                    teleportTransition.relatives(),
                    TeleportTransition.DO_NOTHING
            );
        }

        return super.teleport(teleportTransition);
    }

    @Override
    public LoadingState addCargo(ItemStack stack, boolean simulate, @Nullable Transaction tx) {
        return LoadingState.NO_TARGET;
    }

    @Override
    public Result removeCargo(boolean simulate, @Nullable Transaction tx) {
        return new Result(LoadingState.NO_TARGET, ItemStack.EMPTY);
    }

    @Override
    public void setPad(FuelableDock pad) {

    }

    @Override
    public FuelableDock getPad() {
        return null;
    }

    @Override
    public void onPadDestroyed() {

    }

    @Override
    public boolean isDockValid(FuelableDock dock) {
        return false;
    }

    @Override
    public float getRotationOffset() {
        return -1.5F;
    }

    @Override
    public boolean isPlayerRocket() {
        return true;
    }

    public boolean canAccess(@Nullable CelestialBodyInfo celestialBodyData) {
        if (celestialBodyData == null) return false;

        Holder<CelestialObject> celestialHolder = celestialBodyData.celestialObject();
        if (celestialHolder.isBound() && celestialHolder.value() instanceof CelestialBody celestialBody) {
            return celestialBody.isReachable() && celestialBody.getTierRequired().getLevel() <= this.tier.getLevel();
        }

        return false;
    }
}
