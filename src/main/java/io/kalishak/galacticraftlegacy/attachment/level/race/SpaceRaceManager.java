package io.kalishak.galacticraftlegacy.attachment.level.race;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class SpaceRaceManager {
    public static final MapCodec<SpaceRaceManager> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.listOf().fieldOf("TeamNames").forGetter(SpaceRaceManager::listTeamsNames),
            SpaceRaceTeam.CODEC.listOf().fieldOf("Teams").forGetter(SpaceRaceManager::listTeams)
    ).apply(instance, SpaceRaceManager::fromCodec));
    public static final StreamCodec<RegistryFriendlyByteBuf, SpaceRaceManager> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), SpaceRaceManager::listTeamsNames,
            SpaceRaceTeam.STREAM_CODEC.apply(ByteBufCodecs.list()), SpaceRaceManager::listTeams,
            SpaceRaceManager::fromCodec
    );
    private final Object2ObjectMap<String, SpaceRaceTeam> teamsByName = new Object2ObjectOpenHashMap<>();
    private final Object2ObjectMap<UUID, SpaceRaceTeam> teamsByPlayer = new Object2ObjectOpenHashMap<>();

    private static SpaceRaceManager fromCodec(List<String> teamNames, List<SpaceRaceTeam> teams) {
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
    }

    public SpaceRaceManager() {
    }

    public static FlagData getPlayerFlag(Level level, Player player) {
        SpaceRaceManager manager = level.getData(GalacticraftAttachments.SPACE_RACE_MANAGER);
        SpaceRaceTeam spaceRaceTeam = manager.getSpaceRaceTeamById(player.getUUID());

        if (spaceRaceTeam == null) {
            return player.getData(GalacticraftAttachments.PLAYER_SPACE_DATA).getPrivateFlagData();
        }

        return spaceRaceTeam.getFlagData();
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
        if (getSpaceRaceTeamById(player.getUUID()) != null) {
            removePlayerFromTeam(player);
        }

        this.teamsByPlayer.put(player.getUUID(), spaceRaceTeam);
        return spaceRaceTeam.addMember(player);
    }

    public boolean removePlayerFromTeam(Player player) {
        SpaceRaceTeam spaceRaceTeam = getSpaceRaceTeamById(player.getUUID());

        if (spaceRaceTeam != null) {
            removePlayerFromTeam(player, spaceRaceTeam);
            return true;
        }

        return false;
    }

    public boolean removePlayerFromTeam(Player player, SpaceRaceTeam spaceRaceTeam) {
        if (getSpaceRaceTeamById(player.getUUID()) != spaceRaceTeam) {
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

    public @Nullable SpaceRaceTeam getSpaceRaceTeamById(UUID uuid) {
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
