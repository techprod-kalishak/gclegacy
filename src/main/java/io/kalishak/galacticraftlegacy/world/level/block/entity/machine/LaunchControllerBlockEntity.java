package io.kalishak.galacticraftlegacy.world.level.block.entity.machine;

import io.kalishak.galacticraftlegacy.world.level.savedata.TelemetryTracker;
import io.kalishak.galacticraftlegacy.world.entity.vehicle.rocket.AbstractAutoRocket;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

public class LaunchControllerBlockEntity extends BlockEntity {
    public static final GlobalPos INVALID_POS = GlobalPos.of(Level.OVERWORLD, BlockPos.ZERO);
    private @Nullable GlobalPos levelBoundedBlockPosition;
    private int targetFrequency = -1;

    public LaunchControllerBlockEntity(ResourceKey<Level> levelKey, BlockPos pos, BlockState blockState) {
        super(GalacticraftBlockEntityType.LAUNCH_CONTROLLER.get(), pos, blockState);
        this.levelBoundedBlockPosition = GlobalPos.of(levelKey, pos);
    }

    public LaunchControllerBlockEntity(BlockPos pos, BlockState blockState) {
        super(GalacticraftBlockEntityType.LAUNCH_CONTROLLER.get(), pos, blockState);
    }

    public void setLevelBoundedBlockPosition(@NonNull ResourceKey<Level> levelKey) {
        this.levelBoundedBlockPosition = GlobalPos.of(levelKey, getBlockPos());
    }

    public GlobalPos getLevelBoundedBlockPosition() {
        return Objects.requireNonNullElse(this.levelBoundedBlockPosition, INVALID_POS);
    }

    @Override
    public void setRemoved() {
        if (this.levelBoundedBlockPosition != null && this.level instanceof ServerLevel serverLevel) {
            TelemetryTracker tracker = serverLevel.getDataStorage().get(TelemetryTracker.SAVE_DATA_ID);

            if (tracker != null) {
                tracker.removeLaunchControllerAt(this.levelBoundedBlockPosition);
            }
        }

        super.setRemoved();
    }

    public boolean receivesRedstoneSignal(Level level, BlockPos pos) {
        return false;
    }

    public boolean canAutoLaunch() {
        return false;
    }

    public void setTargetFrequency(int targetFrequency) {
        this.targetFrequency = targetFrequency;
        setChanged();
    }

    public int getTargetFrequency() {
        return this.targetFrequency;
    }

    public ResourceKey<Level> getLevelKey() {
        if (this.levelBoundedBlockPosition == null) {
            return Level.OVERWORLD;
        }

        return this.levelBoundedBlockPosition.dimension();
    }

    public AbstractAutoRocket.AutoLaunchState getAutoLaunchState() {
        return AbstractAutoRocket.AutoLaunchState.CARGO_IS_FULL;
    }
}
