/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.entity.vehicle.rocket;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.attachment.entity.PlayerSpaceData;
import io.kalishak.galacticraftlegacy.world.score.race.SpaceRaceHooks;
import io.kalishak.galacticraftlegacy.world.score.race.SpaceRaceScoreboard;
import io.kalishak.galacticraftlegacy.world.score.race.SpaceRaceTeam;
import io.kalishak.galacticraftlegacy.codec.SerializableEnum;
import io.kalishak.galacticraftlegacy.world.damagesource.GalacticraftDamageTypes;
import io.kalishak.galacticraftlegacy.world.entity.Trackable;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.TeamColor;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.function.Supplier;

public abstract class AbstractSpaceShip extends VehicleEntity implements Trackable<AbstractSpaceShip> {
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractSpaceShip.class);
    protected static final EntityDataAccessor<Byte> DATA_LAUNCH_PHASE_ID = SynchedEntityData.defineId(AbstractSpaceShip.class, EntityDataSerializers.BYTE);
    protected static final EntityDataAccessor<Float> DATA_ROLL_ID = SynchedEntityData.defineId(AbstractSpaceShip.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Float> DATA_SINCE_LAUNCH_ID = SynchedEntityData.defineId(AbstractSpaceShip.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Integer> DATA_UNTIL_LAUNCH_ID = SynchedEntityData.defineId(AbstractSpaceShip.class, EntityDataSerializers.INT);
    protected long ownTicks = 0;
    private boolean addToTelemetry;
    protected final Supplier<Item> droppedItemSupplier;

    protected EntityReference<Player> owner;

    protected AbstractSpaceShip(EntityType<?> entityType, Level level, Supplier<Item> droppedItemSupplier, @Nullable Player owner) {
        super(entityType, level);
        this.droppedItemSupplier = droppedItemSupplier;

        this.owner = EntityReference.of(owner);
        this.addToTelemetry = true;
    }

    protected AbstractSpaceShip(EntityType<?> entityType, Level level, Supplier<Item> droppedItemSupplier) {
        this(entityType, level, droppedItemSupplier, null);
    }

    protected static int getScaledFuel(AbstractAutoRocket rocket) {
        FluidResource fuel = rocket.fluid.getResource(0);

        return Mth.floor((float) rocket.fluid.getAmountAsInt(0) / (float) rocket.fluid.getCapacityAsInt(0, fuel) * 100.0F);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_LAUNCH_PHASE_ID, (byte) 0);
        builder.define(DATA_ROLL_ID, 0.0F);
        builder.define(DATA_SINCE_LAUNCH_ID, 0.0F);
        builder.define(DATA_UNTIL_LAUNCH_ID, 0);
    }

    @Override
    protected Item getDropItem() {
        return this.droppedItemSupplier.get();
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {
        this.owner = EntityReference.read(valueInput, "Owner");
        this.entityData.set(DATA_LAUNCH_PHASE_ID, valueInput.getByteOr("LaunchPhase", (byte) 0));
        setRollAmplitude(valueInput.getFloatOr("RollAmplitude", 0.0F));
        setTimeSinceLaunch(valueInput.getFloatOr("TimeSinceLaunch", 0.0F));
        setTimeUntilLaunch(valueInput.getIntOr("TimeUntilLaunch", 0));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {
        this.owner.store(valueOutput, "Owner");
        valueOutput.putByte("LaunchPhase", this.entityData.get(DATA_LAUNCH_PHASE_ID));
        valueOutput.putFloat("RollAmplitude", getRollAmplitude());
        valueOutput.putFloat("TimeSinceLaunch", getTimeSinceLaunch());
        valueOutput.putInt("TimeUntilLaunch", getTimeUntilLaunch());
    }

    @Override
    public boolean canBeCollidedWith(@Nullable Entity entity) {
        return isAlive();
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float amount) {
        float finalDamage = 0;

        if (isAlive()) {
            Entity source = damageSource.getDirectEntity();
            boolean isPlayer = source instanceof Player player && player.isCreative();

            if (isInvulnerableToBase(damageSource) || this.getY() > 300.0D || !(source instanceof Player)) {
                return false;
            }

            setRollAmplitude(10.0F);
            processFlappingMovement();
            finalDamage += amount * 10.0F;

            if (isPlayer) {
                finalDamage = 100.0F;
            }

            if (isPlayer || getDamage() > 90.0F) {
                ejectPassengers();

                if (isPlayer) {
                    remove(RemovalReason.KILLED);
                } else {
                    destroy(serverLevel, getDropAsRocket());
                }

                return true;
            }
        }

        return super.hurtServer(serverLevel, damageSource, finalDamage);
    }

    protected void destroy(ServerLevel level, ItemStack rocket) {
        kill(level);
        if (level.getGameRules().get(GameRules.ENTITY_DROPS)) {
            rocket.set(DataComponents.CUSTOM_NAME, getCustomName());
            spawnAtLocation(level, rocket);
        }
    }

    public abstract int getPreLaunchDelay();

    protected abstract ItemStack getDropAsRocket();

    @Override
    public abstract Collection<ItemEntity> captureDrops();

    @Override
    public boolean shouldRiderSit() {
        return false;
    }

    @Override
    public void tick() {
        this.ownTicks++;

        super.tick();

        if (!addToTracker()) {
            LOGGER.warn("Adding to tracker failed!");
        }

        getPassengers().forEach(e -> e.fallDistance = 0.0D);

        if (getY() > 1200 && getLaunchPhase() != LaunchPhase.LANDING) {
            onReachAtmosphere();
        }

        if (getRollAmplitude() > 0.0F) {
            decreaseRoll(1.0F);
        }

        if (getDamage() > 0.0D) {
            setDamage(Math.min(getDamage() - 1.0F, getDamage()));
        }

        if (level() instanceof ServerLevel serverLevel) {
            if (getY() < serverLevel.getMinY()) {
                discard();
            } else if (getY() > (1200 + (getLaunchPhase() == LaunchPhase.LANDING ? 355 : 100))) {
                for (Entity entity : getPassengers()) {
                    if (entity instanceof ServerPlayer serverPlayer) {
                        PlayerSpaceData spaceData = serverPlayer.getData(GalacticraftAttachments.PLAYER_SPACE_DATA);
                        if (spaceData.inPlanetSelection()) {
                            discard();
                        }
                    } else {
                        discard();
                    }
                }
            }

            if (getTimeSinceLaunch() > 50 && onGround()) {
                failRocket(serverLevel);
            }
        }

        switch (getLaunchPhase()) {
            case UNIGNITED -> setTimeUntilLaunch(getPreLaunchDelay());
            case IGNITED -> {
                if (getTimeUntilLaunch() > 0) {
                    decreaseTimeUntilLaunch(1);
                }
            }

            case LAUNCHED, LANDING -> increaseTimeUntilLaunch(1);
        }

        if (getTimeUntilLaunch() == 0 && getLaunchPhase() == LaunchPhase.IGNITED) {
            setLaunchPhase(LaunchPhase.LAUNCHED);
            onLaunch();
        }

        if (getYRot() > 90) {
            setYRot(90);
        } else if (getYRot() < -90) {
            setYRot(-90);
        }

        double deltaX = -(50 * Math.cos(getXRot() / 180 / Math.PI) * Math.sin(getY() * 0.01 / 180 / Math.PI));
        double deltaZ = -(50 * Math.sin(getXRot() / 180 / Math.PI) * Math.sin(getY() * 0.01 / 180 / Math.PI));

        move(MoverType.SELF, new Vec3(deltaX, 0.0D, deltaZ));

        if (getLaunchPhase() == LaunchPhase.IGNITED || getLaunchPhase() == LaunchPhase.UNIGNITED) {
            setDeltaMovement(Vec3.ZERO);
        }

        if (level().isClientSide()) {
            if (canSimulateMovement()) {
                move(MoverType.SELF, getDeltaMovement());
            }
        } else {
            move(MoverType.SELF, getDeltaMovement());
        }

        this.xOld = getX();
        this.yOld = getY();
        this.zOld = getZ();
    }

    public void turnYRot(float f)
    {
        setYRot(getYRot() + f);
    }

    public void turnXRot(float f)
    {
        setXRot(getXRot() + f);
    }

    protected void failRocket(ServerLevel serverLevel) {
        for (Entity passenger : getPassengers()) {
            passenger.hurtServer(serverLevel, damageSources().source(GalacticraftDamageTypes.SPACESHIP_CRASH, this),  (float) (4.0D * 20 + 1.0D));
        }

        if (serverLevel.getGameRules().get(GameRules.MOB_GRIEFING)) {
            serverLevel.explode(this, getX(), getY(), getZ(), 5, Level.ExplosionInteraction.TNT);
        }

        kill(serverLevel);
    }

    public void ignite() {
        setLaunchPhase(LaunchPhase.IGNITED);
    }

    @Override
    public boolean isNewTrackable() {
        return this.addToTelemetry;
    }

    @Override
    public void markAsTracked() {
        this.addToTelemetry = false;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        double d0 = getBoundingBox().getSize();
        if (Double.isNaN(d0)) {
            d0 = 1.0F;
        }

        d0 *= 64.0D * 5.0D;
        return distance < d0 * d0;
    }

    public boolean canEnter(ServerPlayer otherPlayer) {
        if (!this.owner.matches(otherPlayer)) {
            Player owner = this.owner.getEntity(level(), Player.class);

            if (owner != null) {
                SpaceRaceScoreboard spaceRaceScoreboard = SpaceRaceHooks.getFromLevel(otherPlayer.level());
                SpaceRaceTeam spaceRaceTeam = spaceRaceScoreboard.getPlayerSpaceRace(owner.getScoreboardName());

                return spaceRaceTeam == null || spaceRaceTeam.getPlayers().contains(otherPlayer.getScoreboardName());
            }
        }

        return false;
    }

    public void onLaunch() {

    }

    public void onReachAtmosphere() {

    }

    public double getOnPadOffset() {
        return 0.0D;
    }

    @Override
    public void transmitData(int[] data) {
        data[0] = getTimeUntilLaunch();
        data[1] = getBlockY();
        data[2] = (this instanceof AbstractAutoRocket autoRocket ? getScaledFuel(autoRocket) : 0);
        data[3] = Mth.floor(getXRot());
    }

    @Override
    public void receiveData(int[] data, String[] str) {
        int countdown = data[0];
    }

    @Override
    public void adjustDisplay(int[] data) {

    }

    public void setLaunchPhase(LaunchPhase launchPhase) {
        this.entityData.set(DATA_LAUNCH_PHASE_ID, (byte) launchPhase.getIndex());
    }

    public LaunchPhase getLaunchPhase() {
        return LaunchPhase.values()[this.entityData.get(DATA_LAUNCH_PHASE_ID)];
    }

    public boolean isLaunched() {
        return getLaunchPhase() == LaunchPhase.LAUNCHED || getLaunchPhase() == LaunchPhase.LANDING;
    }

    public float getRollAmplitude() {
        return this.entityData.get(DATA_ROLL_ID);
    }

    public void setRollAmplitude(float rollAmplitude) {
        this.entityData.set(DATA_ROLL_ID, rollAmplitude);
    }

    public void increaseRoll(float delta) {
        setRollAmplitude(getRollAmplitude() + delta);
    }

    public void decreaseRoll(float delta) {
        increaseRoll(-delta);
    }

    public float getTimeSinceLaunch() {
        return this.entityData.get(DATA_SINCE_LAUNCH_ID);
    }

    public void setTimeSinceLaunch(float timeSinceLaunch) {
        this.entityData.set(DATA_SINCE_LAUNCH_ID, timeSinceLaunch);
    }

    public int getTimeUntilLaunch() {
        return this.entityData.get(DATA_UNTIL_LAUNCH_ID);
    }

    public void setTimeUntilLaunch(int timeUntilLaunch) {
        this.entityData.set(DATA_UNTIL_LAUNCH_ID, timeUntilLaunch);
    }

    public void decreaseTimeUntilLaunch(int delta) {
        setTimeUntilLaunch(getTimeUntilLaunch() - delta);
    }

    public void increaseTimeUntilLaunch(int delta) {
        setTimeUntilLaunch(getTimeUntilLaunch() + delta);
    }

    public void setOwner(Player player) {
        this.owner = EntityReference.of(player);
    }

    public TeamColor getSpaceRaceTeamConeColor() {
        Player owner = this.owner.getEntity(level(), Player.class);

        if (owner instanceof ServerPlayer serverPlayer) {
            return SpaceRaceHooks.getSpaceRaceTeam(serverPlayer)
                    .flatMap(SpaceRaceTeam::getColor)
                    .orElse(TeamColor.RED);
        }

        return TeamColor.WHITE;
    }

    public enum LaunchPhase implements SerializableEnum {
        UNIGNITED("unignited", 0),
        IGNITED("ignited", 1),
        LAUNCHED("launched", 2),
        LANDING("landing", 3);

        public static final Codec<LaunchPhase> CODEC = SerializableEnum.codec(LaunchPhase.class);
        public static final StreamCodec<ByteBuf, LaunchPhase> STREAM_CODEC = SerializableEnum.streamCodec(LaunchPhase.class);
        private final String name;
        private final int index;

        LaunchPhase(String name, int index) {
            this.name = name;
            this.index = index;
        }

        @Override
        public @NonNull String getSerializedName() {
            return this.name;
        }

        @Override
        public int getIndex() {
            return this.index;
        }
    }
}
