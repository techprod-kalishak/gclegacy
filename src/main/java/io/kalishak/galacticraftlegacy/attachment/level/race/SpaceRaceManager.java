package io.kalishak.galacticraftlegacy.attachment.level.race;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class SpaceRaceManager extends SavedData {
    public static final SavedDataType<SpaceRaceManager> SAVE_DATA_ID = new SavedDataType<>("space_rane_manager", SpaceRaceManager::new, SpaceRaceManager.CODEC);
    public static final Codec<SpaceRaceManager> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.listOf().fieldOf("TeamNames").forGetter(SpaceRaceManager::listTeamsNames),
            SpaceRaceTeam.CODEC.listOf().fieldOf("Teams").forGetter(SpaceRaceManager::listTeams)
    ).apply(instance, (teamNames, teams) -> {
        SpaceRaceManager manager = new SpaceRaceManager();

        for (int i = 0; i < teamNames.size(); i++) {
            String name = teamNames.get(i);
            SpaceRaceTeam team = teams.get(i);

            team.getMembers()
                    .stream()
                    .map(SpaceRaceMember::getPlayerId)
                    .forEach(uuid -> manager.teamsByPlayer.put(uuid, team));
            manager.teamsByName.put(name, team);
        }

        return manager;
    }));
    private final Object2ObjectMap<String, SpaceRaceTeam> teamsByName = new Object2ObjectOpenHashMap<>();
    private final Object2ObjectMap<UUID, SpaceRaceTeam> teamsByPlayer = new Object2ObjectOpenHashMap<>();

    SpaceRaceManager() {
        setDirty();
    }

    public static @NonNull SpaceRaceManager getFromLevel(ServerLevel serverLevel) {
        return serverLevel.getServer().overworld().getDataStorage().computeIfAbsent(SAVE_DATA_ID);
    }

    public static FlagData getPlayerFlag(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            SpaceRaceManager manager = SpaceRaceManager.getFromLevel(serverPlayer.level());

            SpaceRaceTeam spaceRaceTeam = manager.getSpaceRaceTeamByPlayerId(player.getUUID());

            if (spaceRaceTeam != null) {
                return spaceRaceTeam.getFlagData();
            }
        }

        return player.getData(GalacticraftAttachments.PLAYER_SPACE_DATA).getPrivateFlagData();
    }

    public @Nullable SpaceRaceTeam getSpaceRaceTeam(String teamName) {
        return this.teamsByName.get(teamName);
    }

    public SpaceRaceTeam getOrCreateSpaceRaceTeam(String teamName) {
        SpaceRaceTeam spaceRaceTeam = getSpaceRaceTeam(teamName);

        if (spaceRaceTeam != null) {
            return spaceRaceTeam;
        }

        spaceRaceTeam = new SpaceRaceTeam(teamName, Collections.emptySet());
        this.teamsByName.put(teamName, spaceRaceTeam);
        onTeamAdded(spaceRaceTeam);

        return spaceRaceTeam;
    }

    public void removeSpaceRaceTeam(SpaceRaceTeam spaceRaceTeam) {
        this.teamsByName.remove(spaceRaceTeam.getTeamName());

        spaceRaceTeam.getMembers()
                .stream()
                .map(SpaceRaceMember::getPlayerId)
                .forEach(this.teamsByPlayer::remove);

        onTeamRemoved(spaceRaceTeam);
    }

    public boolean addPlayerToSpaceRace(Player player, SpaceRaceTeam spaceRaceTeam) {
        if (getSpaceRaceTeamByPlayerId(player.getUUID()) != null) {
            removePlayerFromTeam(player);
        }

        this.teamsByPlayer.put(player.getUUID(), spaceRaceTeam);
        return spaceRaceTeam.addMember(player);
    }

    public boolean removePlayerFromTeam(Player player) {
        SpaceRaceTeam spaceRaceTeam = getSpaceRaceTeamByPlayerId(player.getUUID());

        if (spaceRaceTeam != null) {
            removePlayerFromTeam(player, spaceRaceTeam);
            return true;
        }

        return false;
    }

    public boolean removePlayerFromTeam(Player player, SpaceRaceTeam spaceRaceTeam) {
        if (getSpaceRaceTeamByPlayerId(player.getUUID()) != spaceRaceTeam) {
            throw new IllegalStateException("Player is either on another team or not on any team. Cannot remove from team '" + spaceRaceTeam.getTeamName() + "'.");
        }

        this.teamsByPlayer.remove(player.getUUID());
        return spaceRaceTeam.removeMember(player);
    }

    public Collection<String> getTeamNames() {
        return this.teamsByName.keySet();
    }

    public Collection<SpaceRaceTeam> getSpaceRaceTeams() {
        return this.teamsByName.values();
    }

    private List<String> listTeamsNames() {
        return List.copyOf(getTeamNames());
    }

    private List<SpaceRaceTeam> listTeams() {
        return List.copyOf(getSpaceRaceTeams());
    }

    public @Nullable SpaceRaceTeam getSpaceRaceTeamByPlayerId(UUID uuid) {
        return this.teamsByPlayer.get(uuid);
    }

    public void onTeamAdded(SpaceRaceTeam spaceRaceTeam) {
    }

    public void onTeamChanged(SpaceRaceTeam spaceRaceTeam) {
    }

    public void onTeamRemoved(SpaceRaceTeam spaceRaceTeam) {
    }

    public boolean shouldSave() {
        return !this.teamsByName.isEmpty();
    }
}
