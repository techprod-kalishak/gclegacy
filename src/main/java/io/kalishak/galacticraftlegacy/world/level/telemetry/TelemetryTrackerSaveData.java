/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.telemetry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.UniquelyIdentifyable;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.*;

public class TelemetryTrackerSaveData extends SavedData {
    public static final SavedDataType<TelemetryTrackerSaveData> SAVE_DATA_ID = new SavedDataType<>(
            Constants.id("telemetry_trackers"),
            TelemetryTrackerSaveData::new,
            TelemetryTrackerSaveData.Packed.CODEC
                    .xmap(TelemetryTrackerSaveData::new, telemetryTrackerSaveData -> telemetryTrackerSaveData.packedTracker)
    );
    private final Packed packedTracker;

    TelemetryTrackerSaveData() {
        this.packedTracker = new Packed(Level.OVERWORLD, List.of(), Set.of(), List.of());
    }

    TelemetryTrackerSaveData(Packed packed) {
        this.packedTracker = packed;
        setDirty();
    }

    static TelemetryTrackerSaveData.Packed get(MinecraftServer server) {
        return server.getDataStorage().computeIfAbsent(TelemetryTrackerSaveData.SAVE_DATA_ID).packedTracker;
    }

    record Packed(ResourceKey<Level> dimension, List<GlobalPos> trackers, Set<GloballyReferencedEntity<UniquelyIdentifyable>> trackables, List<GlobalPos> launchControllers) {
        public static final Codec<Packed> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceKey.codec(Registries.DIMENSION).fieldOf("dimension").forGetter(Packed::dimension),
                GlobalPos.CODEC.listOf().optionalFieldOf("trackers", List.of()).forGetter(Packed::trackers),
                GloballyReferencedEntity.CODEC_SET.optionalFieldOf("trackables", Set.of()).forGetter(Packed::trackables),
                GlobalPos.CODEC.listOf().optionalFieldOf("launch_controllers", List.of()).forGetter(Packed::launchControllers)
        ).apply(instance, Packed::new));
    }
}
