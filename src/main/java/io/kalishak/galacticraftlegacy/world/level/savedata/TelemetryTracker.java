/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.savedata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.world.entity.Trackable;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.LaunchControllerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.*;
import java.util.stream.Collectors;

public class TelemetryTracker extends SavedData {
    public static final SavedDataType<TelemetryTracker> SAVE_DATA_ID = new SavedDataType<>(Constants.id("telemetry_trackers"), TelemetryTracker::new, TelemetryTracker::codec);
    private static final Logger LOGGER = LoggerFactory.getLogger(TelemetryTracker.class);
    private final @Nullable ServerLevel serverLevel;
    private final ResourceKey<Level> dimension;
    private final List<BlockPos> trackers = new ArrayList<>();
    private final List<BlockPos> launchControllers = new ArrayList<>();
    private final List<UUID> trackables = new ArrayList<>();

    TelemetryTracker(@Nullable ServerLevel serverLevel) {
        this.serverLevel = serverLevel;
        this.dimension = serverLevel == null ? Level.OVERWORLD : serverLevel.dimension();
    }

    TelemetryTracker(@Nullable ServerLevel serverLevel, List<BlockPos> trackers, List<UUID> trackables, List<BlockPos> launchControllers) {
        this(serverLevel);
        this.trackers.addAll(trackers);
        this.trackables.addAll(trackables);
        this.launchControllers.addAll(launchControllers);
    }

    @SuppressWarnings("ConstantConditions")
    public static Map<ResourceKey<Level>, TelemetryTracker> getTrackers(@NonNull MinecraftServer server) {
        return server.levelKeys().stream()
                .filter(level -> server.getLevel(level) != null)
                .collect(Collectors.toMap(key -> key, key -> server.getLevel(key).getDataStorage().computeIfAbsent(SAVE_DATA_ID)));
    }

    private boolean compute(Predicate<GlobalPos> action, GlobalPos value) {
        if (value.dimension() != this.dimension && this.serverLevel != null) {
            LOGGER.warn("Attempted to add a telemetry tracker at {} in {}, but this TelemetryTracker only tracks {}. Attempting to add to target dimension.", value.pos(), value.dimension(), this.dimension);

            ServerLevel otherLevel = this.serverLevel.getServer().getLevel(value.dimension());

            if (otherLevel != null) {
                TelemetryTracker telemetryTracker = otherLevel.getDataStorage().get(SAVE_DATA_ID);

                if (telemetryTracker != null && action.test(value)) {
                    LOGGER.warn("Successfully added telemetry tracker at {} in {} to the correct TelemetryTracker.", value.pos(), value.dimension());
                } else {
                    LOGGER.warn("Failed to add telemetry tracker at {} in {} to the correct TelemetryTracker.", value.pos(), value.dimension());
                }
            }

            return true;
        }

        return false;
    }

    public void add(Trackable trackable) {
        this.trackables.add(trackable.getUUID());
        setDirty();
    }

    public boolean addTracker(GlobalPos globalPos) {
        if (!compute(this::addTracker, globalPos)) {
            this.trackers.add(globalPos.pos());
            setDirty();
        }

        return isDirty();
    }

    public boolean addLaunchControllerAt(GlobalPos globalPos) {
        if (!compute(this::addLaunchControllerAt, globalPos)) {
            this.launchControllers.add(globalPos.pos());
            setDirty();
        }

        return isDirty();
    }

    public boolean removeLaunchControllerAt(GlobalPos globalPos) {
        if (!compute(this::removeLaunchControllerAt, globalPos)) {
            if (this.launchControllers.remove(globalPos.pos())) {
                setDirty();
            }

            return isDirty();
        }

        return false;
    }

    public List<LaunchControllerBlockEntity> getLaunchControllers() {
        List<LaunchControllerBlockEntity> list = new ArrayList<>();

        if (this.serverLevel != null) {
            for (BlockPos pos : this.launchControllers) {
                BlockEntity blockEntity = this.serverLevel.getBlockEntity(pos);

                if (blockEntity instanceof LaunchControllerBlockEntity) {
                    list.add((LaunchControllerBlockEntity) blockEntity);
                } else {
                    if (this.launchControllers.remove(pos)) {
                        setDirty();
                    }
                }
            }
        }

        return list;
    }

    private static Codec<TelemetryTracker> codec(@Nullable ServerLevel serverLevel) {
        return RecordCodecBuilder.create(instance -> instance.group(
                BlockPos.CODEC.listOf().optionalFieldOf("trackers", List.of()).forGetter(telemetryTracker -> telemetryTracker.trackers),
                UUIDUtil.CODEC.listOf().optionalFieldOf("trackables", List.of()).forGetter(telemetryTracker -> telemetryTracker.trackables),
                BlockPos.CODEC.listOf().optionalFieldOf("launch_controllers", List.of()).forGetter(telemetryTracker -> telemetryTracker.launchControllers)
        ).apply(instance, (trackers, trackables, launchControllers) -> new TelemetryTracker(serverLevel, trackers, trackables, launchControllers)));
    }
}
