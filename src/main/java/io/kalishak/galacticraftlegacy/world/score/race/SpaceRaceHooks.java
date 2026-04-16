package io.kalishak.galacticraftlegacy.world.score.race;

import io.kalishak.galacticraftlegacy.world.entity.FlagData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class SpaceRaceHooks {
    private static @Nullable SpaceRaceScoreboard instance;

    private static SpaceRaceScoreboard loadScoreboard(MinecraftServer server) {
        if (SpaceRaceHooks.instance == null) {
            SpaceRaceScoreboardSaveData saveData = server.overworld().getDataStorage().computeIfAbsent(SpaceRaceScoreboardSaveData.TYPE);

            SpaceRaceHooks.instance = new SpaceRaceScoreboard();
            saveData.getData().teams().forEach(SpaceRaceHooks.instance::loadSpaceRaceTeam);
        }

        return SpaceRaceHooks.instance;
    }

    @SubscribeEvent
    public static void onServerStop(ServerStoppedEvent event) {
        SpaceRaceHooks.instance = null;
    }

    @SubscribeEvent
    public static void onServerInit(ServerStartingEvent event) {
        loadScoreboard(event.getServer());
    }

    public static @NonNull SpaceRaceScoreboard getFromLevel(ServerLevel serverLevel) {
        return SpaceRaceHooks.instance != null ? SpaceRaceHooks.instance : loadScoreboard(serverLevel.getServer());
    }

    public static FlagData getPlayerFlag(ServerPlayer player) {
        SpaceRaceScoreboard spaceRaceScoreboard = getFromLevel(player.level());
        SpaceRaceTeam spaceRaceTeam = spaceRaceScoreboard.getOrCreatePlayerSpaceRace(player.getScoreboardName());

        return spaceRaceTeam.getFlagData();
    }

    public static Optional<SpaceRaceTeam> getSpaceRaceTeam(ServerPlayer player) {
        SpaceRaceScoreboard spaceRaceScoreboard = getFromLevel(player.level());
        return Optional.ofNullable(spaceRaceScoreboard.getPlayerSpaceRace(player.getScoreboardName()));
    }
}
