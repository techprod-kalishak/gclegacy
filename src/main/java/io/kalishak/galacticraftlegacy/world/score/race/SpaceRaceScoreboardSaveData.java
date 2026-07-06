/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.score.race;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.references.Constants;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.List;

public class SpaceRaceScoreboardSaveData extends SavedData {
    public static final SavedDataType<SpaceRaceScoreboardSaveData> TYPE = new SavedDataType<>(
            Constants.id("space_race_manager"),
            SpaceRaceScoreboardSaveData::new,
            SpaceRaceScoreboardSaveData.Packed.CODEC.xmap(SpaceRaceScoreboardSaveData::new, SpaceRaceScoreboardSaveData::getData)
    );
    private SpaceRaceScoreboardSaveData.Packed data;

    private SpaceRaceScoreboardSaveData() {
        this.data = new SpaceRaceScoreboardSaveData.Packed(List.of());
    }

    public SpaceRaceScoreboardSaveData(Packed data) {
        this.data = data;
    }

    public Packed getData() {
        return this.data;
    }

    public void setData(List<SpaceRaceTeam.Packed> teams) {
        this.data = new Packed(teams);
        setDirty();
    }

    public record Packed(List<SpaceRaceTeam.Packed> teams) {
        public static final Codec<SpaceRaceScoreboardSaveData.Packed> CODEC = SpaceRaceTeam.Packed.CODEC.listOf().xmap(SpaceRaceScoreboardSaveData.Packed::new, SpaceRaceScoreboardSaveData.Packed::teams);
    }
}
