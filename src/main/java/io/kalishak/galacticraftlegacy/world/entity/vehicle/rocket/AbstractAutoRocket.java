package io.kalishak.galacticraftlegacy.world.entity.vehicle.rocket;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.CommonConfig;
import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.codec.SerializableEnum;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.world.level.savedata.TelemetryTracker;
import io.kalishak.galacticraftlegacy.sounds.RocketSoundInstance;
import io.kalishak.galacticraftlegacy.transfer.capability.fluid.SingleTankResourceHandler;
import io.kalishak.galacticraftlegacy.world.entity.DockingEntity;
import io.kalishak.galacticraftlegacy.world.entity.LandingEntity;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.FuelableDock;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.LandingPad;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.LaunchControllerBlockEntity;
import io.kalishak.galacticraftlegacy.world.level.block.entity.SoundboundEntity;
import io.kalishak.galacticraftlegacy.world.level.material.fluid.GalacticraftFluids;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public abstract class AbstractAutoRocket extends AbstractSpaceShip implements LandingEntity, SoundboundEntity {
    public static final int INVALID_FREQUENCY_ID = -1;
    protected static final EntityDataAccessor<Integer> DATA_AUTO_LAUNCH_PHASE_ID = SynchedEntityData.defineId(AbstractAutoRocket.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> DATA_TARGET_FREQUENCY_ID = SynchedEntityData.defineId(AbstractAutoRocket.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Boolean> DATA_AWAITING_FOR_PLAYER_ID = SynchedEntityData.defineId(AbstractAutoRocket.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Integer> DATA_AUTO_LAUNCH_COUNTDOWN_ID = SynchedEntityData.defineId(AbstractAutoRocket.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Optional<GlobalPos>> DATA_TARGET_POS_ID = SynchedEntityData.defineId(AbstractAutoRocket.class, EntityDataSerializers.OPTIONAL_GLOBAL_POS);
    protected static final EntityDataAccessor<Optional<BlockPos>> DATA_LAUNCH_PAD_POS_ID = SynchedEntityData.defineId(AbstractAutoRocket.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
    protected static final EntityDataAccessor<Byte> DATA_STATUS_ID = SynchedEntityData.defineId(AbstractAutoRocket.class, EntityDataSerializers.BYTE);
    protected static final EntityDataAccessor<Long> DATA_STATUS_COOLDOWN_ID = SynchedEntityData.defineId(AbstractAutoRocket.class, EntityDataSerializers.LONG);
    protected static final EntityDataAccessor<Long> DATA_LAST_STATUS_COOLDOWN_ID = SynchedEntityData.defineId(AbstractAutoRocket.class, EntityDataSerializers.LONG);
    protected final @Nullable NonNullList<ItemStack> inventory;
    protected final @Nullable ItemStacksResourceHandler itemStacksResourceHandler;
    protected final SingleTankResourceHandler tankResourceHandler = new SingleTankResourceHandler() {
        @Override
        protected FluidStack getFluidStack() {
            return AbstractAutoRocket.this.fuel;
        }

        @Override
        protected void setFluidStack(FluidStack stack) {
            AbstractAutoRocket.this.fuel = stack;
        }

        @Override
        protected int getCapacity(FluidResource resource) {
            return FluidType.BUCKET_VOLUME * 8;
        }
    };
    protected FluidStack fuel = FluidStack.EMPTY;
    protected @Nullable LaunchControllerBlockEntity launchController;
    protected @Nullable BlockEntity landingPad;
    protected RocketSoundInstance soundUpdater;
    private boolean playRocketSound = false;

    protected AbstractAutoRocket(EntityType<?> entityType, Level level, Supplier<Item> droppedItemSupplier, Player owner, int storageSize, boolean preFueled) {
        super(entityType, level, droppedItemSupplier, owner);

        this.inventory = storageSize > 0 ? NonNullList.withSize(storageSize, ItemStack.EMPTY) : null;
        this.itemStacksResourceHandler = storageSize > 0 ? new ItemStacksResourceHandler(this.inventory) : null;

        if (preFueled) {
            this.fuel = new FluidStack(GalacticraftFluids.FUEL, FluidType.BUCKET_VOLUME * 8);
        }
    }

    protected AbstractAutoRocket(EntityType<?> entityType, Level level, Supplier<Item> droppedItemSupplier, Player owner) {
        this(entityType, level, droppedItemSupplier, owner, 2, false);
    }

    protected static <T extends AbstractAutoRocket> void registerCapabilities(RegisterCapabilitiesEvent event, EntityType<? extends T> rocket) {
        event.registerEntity(Capabilities.Item.ENTITY_AUTOMATION, rocket, (entity, context) -> {
            if (entity.hasInventory()) {
                if (context == null || context == Direction.DOWN) {
                    return entity.itemStacksResourceHandler;
                }
            }

            return null;
        });

        event.registerEntity(Capabilities.Fluid.ENTITY, rocket, (entity, context) -> {
            if (context == null || context == Direction.DOWN) {
                return entity.tankResourceHandler;
            }

            return null;
        });
    }

    protected static boolean hasEnoughFuel(AbstractAutoRocket rocket, int requiredAmount) {
        FluidResource fluidResource = rocket.tankResourceHandler.getResource(0);

        if (fluidResource.isEmpty() || !fluidResource.is(GalacticraftTags.Fluids.IS_FUEL)) {
            return false;
        }

        return rocket.tankResourceHandler.getAmountAsInt(0) > requiredAmount;
    }

    protected static boolean hasEnoughFuel(AbstractAutoRocket rocket, Predicate<Integer> requiredAmount) {
        FluidResource fluidResource = rocket.tankResourceHandler.getResource(0);

        if (fluidResource.isEmpty() || !fluidResource.is(GalacticraftTags.Fluids.IS_FUEL)) {
            return false;
        }

        return requiredAmount.test(rocket.tankResourceHandler.getCapacityAsInt(0, fluidResource));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_AUTO_LAUNCH_PHASE_ID, 0);
        builder.define(DATA_TARGET_FREQUENCY_ID, INVALID_FREQUENCY_ID);
        builder.define(DATA_AWAITING_FOR_PLAYER_ID, true);
        builder.define(DATA_AUTO_LAUNCH_COUNTDOWN_ID, 0);
        builder.define(DATA_TARGET_POS_ID, Optional.empty());
        builder.define(DATA_LAUNCH_PAD_POS_ID, Optional.empty());
        builder.define(DATA_STATUS_ID, (byte) 0);
        builder.define(DATA_STATUS_COOLDOWN_ID, 0L);
        builder.define(DATA_LAST_STATUS_COOLDOWN_ID, 0L);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);

        if (key == DATA_LAUNCH_PHASE_ID) {
            Optional<BlockPos> landingPadPos = this.entityData.get(DATA_LAUNCH_PAD_POS_ID);

            if (landingPadPos.isPresent() && level().getBlockEntity(landingPadPos.get()) instanceof FuelableDock dock) {
                this.landingPad = (BlockEntity) dock;
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {
        super.readAdditionalSaveData(valueInput);
        this.tankResourceHandler.deserialize(valueInput);
        if (this.itemStacksResourceHandler != null) {
            this.itemStacksResourceHandler.deserialize(valueInput);
        }
        this.entityData.set(DATA_AUTO_LAUNCH_PHASE_ID, valueInput.getIntOr("AutoLaunchState", 0));
        setTargetFrequency(valueInput.getIntOr("TargetFrequency", INVALID_FREQUENCY_ID));
        setAwaitingForPlayer(valueInput.getBooleanOr("AwaitingForPlayer", true));
        valueInput.read("TargetPos", GlobalPos.CODEC).ifPresent(this::setTargetPos);
        this.entityData.set(DATA_STATUS_ID, valueInput.getByteOr("Status", (byte) 0));
        setStatusCooldown(valueInput.getLongOr("StatusCooldown", 0));
        setLastStatusCooldown(valueInput.getLongOr("LastStatusCooldown", 0));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {
        super.addAdditionalSaveData(valueOutput);
        this.tankResourceHandler.serialize(valueOutput);
        if (this.itemStacksResourceHandler != null) {
            this.itemStacksResourceHandler.serialize(valueOutput);
        }
        valueOutput.putInt("AutoLaunchState", this.entityData.get(DATA_AUTO_LAUNCH_PHASE_ID));
        valueOutput.putInt("TargetFrequency", getTargetFrequency());
        valueOutput.putBoolean("AwaitingForPlayer", isAwaitingForPlayer());
        valueOutput.storeNullable("TargetPos", GlobalPos.CODEC, getTargetPos());
        valueOutput.putByte("Status", this.entityData.get(DATA_STATUS_ID));
        valueOutput.putLong("LastStatusCooldown", getLastStatusCooldown());
        valueOutput.putLong("StatusCooldown", getStatusCooldown());
    }

    @Override
    public void tick() {

        if (getLaunchPhase() == LaunchPhase.LANDING && hasEnoughFuel(this, 0)) {
            if (getTargetPos() != null) {
                double yDiff = getY() - getOnPadOffset() - getTargetPos().pos().getY();
                double motionY = Math.min(-2.0D, (yDiff - 0.04D) / -55.0D);
                double diff = getX() - getTargetPos().pos().getX() - 0.5D;
                double motionX, motionZ;

                setDeltaMovement(getDeltaMovement().x,  motionY, getDeltaMovement().z);

                if (diff > 0.0D) {
                    motionX = Math.max(-0.1D, diff / -100.0D);
                } else if (diff < 0.0D) {
                    motionX = Math.min(0.1D, diff / -100.0D);
                } else {
                    motionX = 0.0D;
                }

                diff = getZ() - getTargetPos().pos().getZ() - 0.5D;

                if (diff > 0.0D) {
                    motionZ = Math.max(-0.1D, diff / -100.0D);
                } else if (diff < 0.0D) {
                    motionZ = Math.min(0.1D, diff / -100.0D);
                } else {
                    motionZ = 0.0D;
                }

                if (motionX != 0.0D || motionZ != 0.0D) {
                    double yaw = Math.atan(motionZ / motionX);
                    double sign = motionX < 0 ? 50.0D : -50.0D;
                    double pitch = Math.atan(Math.sqrt(motionX * motionX + motionZ * motionZ) / sign) * 100.0D;
                    setRot((float) yaw, (float) pitch);
                } else {
                    setXRot(0.0F);
                }

                if (level() instanceof ServerLevel serverLevel) {
                    if (yDiff > 1.0D && yDiff < 40.0D) {
                        for (var obj : serverLevel.getEntities(this, getBoundingBox().move(0.0D, -3.0D, 0.0D))) {
                            if (obj instanceof AbstractSpaceShip spaceShip) {
                                spaceShip.captureDrops();
                                spaceShip.kill(serverLevel);
                            }
                        }
                    }

                    if (yDiff < 0.4D) {
                        int yMin = Mth.floor(getBoundingBox().minY - getOnPadOffset() - 0.45D) - 2;
                        int yMax = Mth.floor(getBoundingBox().maxY) + 1;
                        int zMin = getBlockZ() - 1;
                        int zMax = getBlockZ() + 1;
                        int xMin = getBlockX() - 1;
                        int xMax = getBlockX() + 1;

                        for (int x = xMin; x <= xMax; x++) {
                            for (int z = zMin; z <= zMax; z++) {
                                for (int y = yMin; y <= yMax; y++) {
                                    if (serverLevel.getBlockEntity(new BlockPos(x, y, z)) instanceof FuelableDock) {
                                        setXRot(0.0F);
                                        failRocket(serverLevel);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        super.tick();

        if (!level().isClientSide()) {
            long status = getStatusCooldown();

            if (status > 0) {
                setStatusCooldown(status - 1);
            }

            if (getStatusCooldown() == 0 && getLastStatusCooldown() > 0 && getAutoLaunchState() == AutoLaunchState.INVALID) {
                autoLaunch();
            }

            if (getAutoLaunchCountdown() > 0 && (!(this instanceof TieredRocket) || !getPassengers().isEmpty())) {
                if (getAutoLaunchCountdown() - 1 <= 0) {
                    autoLaunch();
                }
            }

            switch(getAutoLaunchState()) {
                case ROCKET_IS_FUELED -> {
                    if (hasEnoughFuel(this, this.tankResourceHandler.getCapacityAsInt(0, FluidResource.of(this.fuel))) && (!(this instanceof TieredRocket) || !getPassengers().isEmpty())) {
                        autoLaunch();
                    }
                }

                case INSTANT -> {
                    if (getAutoLaunchCountdown() == 0 && (!(this instanceof TieredRocket) || !getPassengers().isEmpty())) {
                        autoLaunch();
                    }
                }

                case REDSTONE_SIGNAL -> {
                    if (this.tickCount % 11 == 0 && this.launchController != null) {
                        if (this.launchController.receivesRedstoneSignal(level(), this.launchController.getBlockPos())) {
                            autoLaunch();
                        }
                    }
                }
            }

            switch (getLaunchPhase()) {
                case UNIGNITED -> {
                    if (this.landingPad != null && this.tickCount % 17 == 0) {
                        updateControllerSettings((FuelableDock) this.landingPad);
                    }
                }

                case LAUNCHED -> setPad(null);

                case IGNITED -> {
                    if (this.soundUpdater != null) {
                        this.soundUpdater.tick();
                        this.playRocketSound = true;
                    }
                }
            }

            if (getLaunchPhase() != LaunchPhase.IGNITED && !this.playRocketSound) {
                stopRocketSound();
            }
        }
    }

    private void autoLaunch() {
        AutoLaunchState autoLaunchState = getAutoLaunchState();

        if (autoLaunchState != AutoLaunchState.INVALID) {
            if (this.launchController != null) {
                boolean shouldAutoLaunch = this.launchController.canAutoLaunch();

                if (shouldAutoLaunch) {
                    if (hasEnoughFuel(this, cap -> this.fuel.getAmount() > cap * 2 / 5)) {
                        ignite();
                    } else {
                        triggerFailureMessage(AutoLaunchStatus.NOT_ENOUGH);
                    }
                }
            } else {
                triggerFailureMessage(AutoLaunchStatus.FAIL);
            }
        } else {
            ignite();
        }
    }

    public abstract boolean isPlayerRocket();

    public boolean igniteWithResult() {
        boolean shouldIgnite = verifyFrequency() || isPlayerRocket();

        if (shouldIgnite) {
            super.ignite();
        }

        this.launchController = null;
        return shouldIgnite;
    }

    @Override
    public void ignite() {
        igniteWithResult();
    }

    @Override
    public void onLand(BlockPos pos) {
        Level level = level();

        if (level.getBlockEntity(pos) instanceof FuelableDock fuelable) {
            if (isDockValid(fuelable)) {
                if (level instanceof ServerLevel serverLevel) {
                    DockingEntity docked = fuelable.getDockedEntity();

                    if (docked instanceof AbstractSpaceShip spaceShip && docked != this) {
                        spaceShip.captureDrops();
                        spaceShip.kill(serverLevel);
                    }

                    setPad(fuelable);
                }

                onRocketLand(pos);
            }
        }
    }

    public void updateControllerSettings(FuelableDock fuelable) {
        var platforms = fuelable.getFuelPads();

        for (LandingPad pad : platforms) {
            if (pad instanceof LaunchControllerBlockEntity controller) {
                boolean autoLaunchEnabled = pad.isControlEnabled();

                this.launchController = controller;

                if (autoLaunchEnabled) {
                    setAutoLaunchState(this.launchController.getAutoLaunchState());
                    int autoLaunchCooldown = getAutoLaunchCountdown();

                    switch (getAutoLaunchState()) {
                        case INSTANT -> {
                            if (autoLaunchCooldown <= 0 || autoLaunchCooldown > 12) {
                                setAutoLaunchCountdown(12);
                            }
                        }

                        case TIME_10_SECONDS -> {
                            if (autoLaunchCooldown <= 0 || autoLaunchCooldown > 200) {
                                setAutoLaunchCountdown(200);
                            }
                        }

                        case TIME_30_SECONDS -> {
                            if (autoLaunchCooldown <= 0 || autoLaunchCooldown > 600) {
                                setAutoLaunchCountdown(600);
                            }
                        }

                        case TIME_1_MINUTE -> {
                            if (autoLaunchCooldown <= 0 || autoLaunchCooldown > 1200) {
                                setAutoLaunchCountdown(1200);
                            }
                        }
                    }
                } else {
                    setAutoLaunchState(AutoLaunchState.INVALID);
                    setAutoLaunchCountdown(0);
                }
            }
        }
    }

    protected void onRocketLand(BlockPos pos) {
        setPos(pos.getX() + 0.5D, pos.getY() + 0.4D + getOnPadOffset(), pos.getZ() + 0.5D);
        setXRot(0.0F);
        stopRocketSound();
    }

    public void stopRocketSound() {
        if (level().isClientSide()) {
            if (this.soundUpdater != null) {
                this.soundUpdater.stopRocketSound();
            }
        }

        this.playRocketSound = false;
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);

        if (level().isClientSide()) {
            Minecraft.getInstance().getSoundManager().stop(this.soundUpdater);
        }
    }

    @Override
    protected void failRocket(ServerLevel serverLevel) {
        stopRocketSound();

        if (shouldCancelExplosion()) {
            for (int i = -3; i <= 3; i++) {
                BlockPos posAbove = blockPosition().above(i);

                if (getLaunchPhase() == LaunchPhase.LANDING && getTargetPos() != null && serverLevel.getBlockEntity(posAbove) instanceof FuelableDock && getY() - getTargetPos().pos().getY() < 5) {
                    for (int x = getBlockX() - 1; x <= getBlockX() + 1; x++) {
                        for (int y = getBlockY() - 1; y <= getBlockY() + 1; y++) {
                            for (int z = getBlockZ() - 1; z <= getBlockZ() + 1; z++) {
                                BlockPos padPos = new BlockPos(x, y, z);

                                if (serverLevel.getBlockEntity(padPos) instanceof FuelableDock) {
                                    onLand(padPos);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (getLaunchPhase() != LaunchPhase.UNIGNITED) {
            super.failRocket(serverLevel);
        }
    }

    protected boolean shouldCancelExplosion() {
        return hasEnoughFuel(this, 1);
    }

    public void cancelLaunch() {
        setLaunchPhase(LaunchPhase.UNIGNITED);
        setTimeUntilLaunch(0);
    }

    @Override
    public void onLaunch() {
        if (CommonConfig.getDimensions().contains(level().dimension())) {
            cancelLaunch();
            return;
        }

        super.onLaunch();
    }

    public boolean canBeLaunched() {
        setStatusCooldown(40L);

        if (hasEnoughFuel(this, 1000)) {
            if (getLaunchPhase() == LaunchPhase.UNIGNITED && !level().isClientSide()) {
                if (!verifyFrequency()) {
                    setTargetFrequency(INVALID_FREQUENCY_ID);
                    setStatus(AutoLaunchStatus.FREQUENCY);
                    return false;
                }

                setStatus(AutoLaunchStatus.SUCCESS);
                return true;
            }
        }

        setTargetFrequency(INVALID_FREQUENCY_ID);
        setStatus(AutoLaunchStatus.NOT_ENOUGH);

        return false;
    }

    public boolean verifyFrequency() {
        if (this.launchController instanceof LaunchControllerBlockEntity blockEntity) {
            int targetFrequency = blockEntity.getTargetFrequency();

            if (setTarget(false, targetFrequency)) {
                setTargetFrequency(targetFrequency);
                return true;
            }
        }

        setTargetFrequency(INVALID_FREQUENCY_ID);

        return false;
    }

    protected boolean setTarget(boolean simulate, int targetFrequency) {
        if (level() instanceof ServerLevel level) {
            TelemetryTracker tracker = level.getDataStorage().get(TelemetryTracker.SAVE_DATA_ID);

            if (tracker != null) {
                List<LaunchControllerBlockEntity> launchControllers = tracker.getLaunchControllers();
                LaunchControllerBlockEntity targetController = null;

                for (LaunchControllerBlockEntity launchController : launchControllers) {
                    if (launchController.getTargetFrequency() == targetFrequency) {
                        targetController = launchController;
                        break;
                    }
                }

                if (targetController == null) {
                    return false;
                }

                boolean targetSet = false;

                blockLoop: for (int x = -2; x <= 2; x++) {
                    for (int z = -2; z <= 2; z++) {
                        BlockPos pos = targetController.getBlockPos().offset(x, 0, z);
                        BlockEntity blockEntity = level().getBlockEntity(pos);

                        if (blockEntity instanceof LandingPad) {
                            if (!simulate) {
                                setTargetPos(new GlobalPos(targetController.getLevelKey(), pos));

                                targetSet = true;
                                break blockLoop;
                            }
                        }
                    }
                }

                if (!targetSet) {
                    if (!simulate) {
                        setTargetPos(null);
                    }

                    return false;
                }

                return true;
            }
        } else LOGGER.warn("Tried to call AbstractAutoRocket#setTarget from the client!");

        return false;
    }

    @Override
    public @Nullable TickableSoundInstance getSoundUpdater() {
        return this.soundUpdater;
    }

    @Override
    public @Nullable Sound setSoundUpdater(LocalPlayer localPlayer) {
        this.soundUpdater = RocketSoundInstance.create(this);
        return this.soundUpdater.getSound();
    }

    public void triggerFailureMessage(AutoLaunchStatus status) {
        if (!level().isClientSide() && !getPassengers().isEmpty() && getFirstPassenger() instanceof ServerPlayer player) {
            player.sendSystemMessage(status.getDescription());
        }
    }

    public boolean hasInventory() {
        return this.inventory != null;
    }

    public AutoLaunchState getAutoLaunchState() {
        return AutoLaunchState.values()[this.entityData.get(DATA_AUTO_LAUNCH_PHASE_ID)];
    }

    public void setAutoLaunchState(AutoLaunchState autoLaunchState) {
        this.entityData.set(DATA_AUTO_LAUNCH_PHASE_ID, autoLaunchState.getIndex());
    }

    public int getTargetFrequency() {
        return this.entityData.get(DATA_TARGET_FREQUENCY_ID);
    }

    public void setTargetFrequency(int targetFrequency) {
        this.entityData.set(DATA_TARGET_FREQUENCY_ID, targetFrequency);
    }

    public int getAutoLaunchCountdown() {
        return this.entityData.get(DATA_AUTO_LAUNCH_COUNTDOWN_ID);
    }

    public void setAutoLaunchCountdown(int autoLaunchCountdown) {
        this.entityData.set(DATA_AUTO_LAUNCH_COUNTDOWN_ID, autoLaunchCountdown);
    }

    public boolean isAwaitingForPlayer() {
        return this.entityData.get(DATA_AWAITING_FOR_PLAYER_ID);
    }

    public void setAwaitingForPlayer(boolean awaitingForPlayer) {
        this.entityData.set(DATA_AWAITING_FOR_PLAYER_ID, awaitingForPlayer);
    }

    public @Nullable GlobalPos getTargetPos() {
        return this.entityData.get(DATA_TARGET_POS_ID).orElse(null);
    }

    public void setTargetPos(@Nullable GlobalPos targetPos) {
        this.entityData.set(DATA_TARGET_POS_ID, Optional.ofNullable(targetPos));
    }

    public AutoLaunchStatus getStatus() {
        return AutoLaunchStatus.values()[this.entityData.get(DATA_STATUS_ID)];
    }

    public void setStatus(AutoLaunchStatus status) {
        this.entityData.set(DATA_STATUS_ID, (byte) status.getIndex());
    }

    public long getStatusCooldown() {
        return this.entityData.get(DATA_STATUS_COOLDOWN_ID);
    }

    public void setStatusCooldown(long statusCooldown) {
        this.entityData.set(DATA_STATUS_COOLDOWN_ID, statusCooldown);
    }

    public long getLastStatusCooldown() {
        return this.entityData.get(DATA_LAST_STATUS_COOLDOWN_ID);
    }

    public void setLastStatusCooldown(long lastStatusCooldown) {
        this.entityData.set(DATA_LAST_STATUS_COOLDOWN_ID, lastStatusCooldown);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entity) {
        return new ClientboundAddEntityPacket(this, entity);
    }

    public enum AutoLaunchState implements SerializableEnum {
        INVALID(0, "invalid"),
        CARGO_IS_UNLOADED(1, "cargo_unloaded"),
        CARGO_IS_FULL(2, "cargo_full"),
        ROCKET_IS_FUELED(3, "fully_fueled"),
        INSTANT(4, "instant"),
        TIME_10_SECONDS(5, "ten_sec"),
        TIME_30_SECONDS(6, "thirty_sec"),
        TIME_1_MINUTE(7, "one_min"),
        REDSTONE_SIGNAL(8, "redstone_sig");

        public static final Codec<AutoLaunchState> CODEC = SerializableEnum.codec(AutoLaunchState.class);
        public static final StreamCodec<ByteBuf, AutoLaunchState> STREAM_CODEC = SerializableEnum.streamCodec(AutoLaunchState.class);
        private final int index;
        private final String name;

        AutoLaunchState(int index, String name) {
            this.index = index;
            this.name = name;
        }

        @Override
        public @NonNull String getSerializedName() {
            return this.name;
        }

        @Override
        public int getIndex() {
            return this.index;
        }

        public String getDescriptionId() {
            return Galacticraft.MODID + ".container.cargo_rocket." + getSerializedName();
        }
    }

    public enum AutoLaunchStatus implements SerializableEnum {
        FREQUENCY(0, "frequency", "not_set", style -> style.withColor(0xA7C)),
        SUCCESS(1, "success", style -> style.withColor(0xA7A)),
        NOT_ENOUGH(2, "not_enough", "fuel", style -> style.withColor(0xA7C)),
        FAIL(3, "fail", style -> style.withColor(0xA7C));

        public static final Codec<AutoLaunchStatus> CODEC = SerializableEnum.codec(AutoLaunchStatus.class);
        public static final StreamCodec<ByteBuf, AutoLaunchStatus> STREAM_CODEC = SerializableEnum.streamCodec(AutoLaunchStatus.class);
        private final int index;
        private final String name;
        private final @Nullable String context;
        private final Style style;

        AutoLaunchStatus(int index, String name, @Nullable String context, UnaryOperator<Style> style) {
            this.index = index;
            this.name = name;
            this.context = context;
            this.style = style.apply(Style.EMPTY);
        }

        AutoLaunchStatus(int index, String name, UnaryOperator<Style> style) {
            this(index, name, null, style);
        }

        @Override
        public @NonNull String getSerializedName() {
            return this.name;
        }

        @Override
        public int getIndex() {
            return this.index;
        }

        public String getDescriptionId() {
            return Galacticraft.MODID + ".container.message." + getSerializedName();
        }

        public Component getDescription() {
            MutableComponent component = Component.translatable(getDescriptionId());

            if (this.context != null) {
                component.append(Component.translatable(Galacticraft.MODID + ".container.message." + this.context));
            }

            return component.withStyle(this.style);
        }
    }
}
