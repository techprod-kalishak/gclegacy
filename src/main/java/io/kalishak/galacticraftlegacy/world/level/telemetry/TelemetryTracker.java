/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.telemetry;

import io.kalishak.galacticraftlegacy.world.entity.Trackable;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.LaunchControllerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.entity.UniquelyIdentifyable;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TelemetryTracker {
    private static @Nullable TelemetryTracker instance;
    private final @Nullable ServerLevel serverLevel;
    private final List<GlobalPos> trackers = new ArrayList<>();
    private final List<GlobalPos> launchControllers = new ArrayList<>();
    private final Set<GloballyReferencedEntity<?>> trackables = new HashSet<>();

    public static TelemetryTracker get(MinecraftServer server) {
        if (instance == null) {
            TelemetryTrackerSaveData.Packed packed = TelemetryTrackerSaveData.get(server);
            instance = new TelemetryTracker(
                    server.getLevel(packed.dimension()),
                    packed.trackers(),
                    packed.trackables(),
                    packed.launchControllers()
            );
        }

        return instance;
    }

    public TelemetryTracker(@Nullable ServerLevel serverLevel, List<GlobalPos> trackers, Set<GloballyReferencedEntity<UniquelyIdentifyable>> trackables, List<GlobalPos> launchControllers) {
        this.serverLevel = serverLevel;
        this.trackers.addAll(trackers);
        this.trackables.addAll(trackables);
        this.launchControllers.addAll(launchControllers);
    }

    public boolean isEmpty() {
        return this.serverLevel == null;
    }

    public void addTracker(ResourceKey<Level> dimension, BlockPos blockPos) {
        this.trackers.add(GlobalPos.of(dimension, blockPos));
    }

    public List<GlobalPos> getTrackers() {
        return this.trackers;
    }

    public void addLaunchControllerAt(ResourceKey<Level> dimension, BlockPos blockPos) {
        this.launchControllers.add(GlobalPos.of(dimension, blockPos));
    }

    public void removeLaunchControllerAt(ResourceKey<Level> dimension, BlockPos blockPos) {
        this.launchControllers.remove(GlobalPos.of(dimension, blockPos));
    }

    public List<LaunchControllerBlockEntity> getLaunchControllers() {
        List<LaunchControllerBlockEntity> list = new ArrayList<>();

        if (this.serverLevel != null) {
            for (GlobalPos pos : this.launchControllers) {
                Level level = this.serverLevel.getServer().getLevel(pos.dimension());

                if (level != null) {
                    BlockEntity blockEntity = level.getBlockEntity(pos.pos());

                    if (blockEntity instanceof LaunchControllerBlockEntity) {
                        list.add((LaunchControllerBlockEntity) blockEntity);
                    } else {
                        this.launchControllers.remove(pos);
                    }
                }
            }
        }

        return list;
    }

    public void addTrackable(Trackable<?> trackable) {
        this.trackables.add(trackable.asReference());
    }

    public void removeTrackable(Trackable<?> trackable) {
        this.trackables.remove(trackable.asReference());
    }

    public Set<? extends Entity> getTrackedEntities(Predicate<ResourceKey<Level>> fromLevel) {
        if (this.serverLevel == null) return Collections.emptySet();

        return this.trackables.stream()
                .filter(referencedEntity -> fromLevel.test(referencedEntity.knownDimension))
                .map(referencedEntity -> this.serverLevel.getEntities().get(referencedEntity.getReference().getUUID()))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
