/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.score.race;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.List;

public class SpaceRaceScoreboard {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final Object2ObjectMap<String, SpaceRaceTeam> teamsByName = new Object2ObjectOpenHashMap<>();
    private final Object2ObjectMap<String, SpaceRaceTeam> teamsByPlayer = new Object2ObjectOpenHashMap<>();

    protected SpaceRaceScoreboard() {

    }

    protected void copyFrom(SpaceRaceScoreboard scoreboard) {
        this.teamsByName.putAll(scoreboard.teamsByName);
        this.teamsByName.putAll(scoreboard.teamsByPlayer);
        this.teamsByPlayer.putAll(scoreboard.teamsByName);
    }

    public @Nullable SpaceRaceTeam getPlayerSpaceRace(String name) {
        return this.teamsByName.get(name);
    }

    public SpaceRaceTeam addSpaceRace(String name) {
        SpaceRaceTeam team = new SpaceRaceTeam(this, name);

        this.teamsByName.put(name, team);
        onTeamAdded(team);

        return team;
    }

    public SpaceRaceTeam getOrCreatePlayerSpaceRace(String name) {
        SpaceRaceTeam team = getPlayerSpaceRace(name);
        if (team != null) {
            LOGGER.warn("Requested creation of existing team '{}'", name);
            return team;
        }

        team = new SpaceRaceTeam(this, name);

        this.teamsByName.put(name, team);
        onTeamAdded(team);

        return team;
    }

    public void removeSpaceRace(SpaceRaceTeam team) {
        this.teamsByName.remove(team.getName());

        for (String player : team.getPlayers()) {
            this.teamsByPlayer.remove(player);
        }

        onTeamRemoved(team);
    }

    public boolean addPlayerToSpaceRace(String player, SpaceRaceTeam team) {
        if (getSpaceRaceTeam(player) != null) {
            removePlayerFromSpaceRace(player);
        }

        this.teamsByPlayer.put(player, team);
        return team.getPlayers().add(player);
    }

    public boolean removePlayerFromSpaceRace(String player) {
        SpaceRaceTeam team = getSpaceRaceTeam(player);
        if (team != null) {
            removePlayerFromSpaceRace(player, team);
            return true;
        } else {
            return false;
        }
    }

    public void removePlayerFromSpaceRace(String player, SpaceRaceTeam team) {
        if (getSpaceRaceTeam(player) != team) {
            throw new IllegalStateException("Player is either on another team or not on any team. Cannot remove from team '" + team.getName() + "'.");
        } else {
            this.teamsByPlayer.remove(player);
            team.getPlayers().remove(player);
        }
    }

    public Collection<String> getTeamNames() {
        return this.teamsByName.keySet();
    }

    public Collection<SpaceRaceTeam> getSpaceRaceTeams() {
        return this.teamsByName.values();
    }

    public @Nullable SpaceRaceTeam getSpaceRaceTeam(String name) {
        return this.teamsByPlayer.get(name);
    }

    public void onTeamAdded(SpaceRaceTeam team) {

    }

    public void onTeamChanged(SpaceRaceTeam team) {

    }

    public void onTeamRemoved(SpaceRaceTeam team) {

    }

    protected List<SpaceRaceTeam.Packed> packSpaceRaceTeams() {
        return getSpaceRaceTeams().stream().map(SpaceRaceTeam::pack).toList();
    }

    protected void loadSpaceRaceTeam(SpaceRaceTeam.Packed packed) {
        SpaceRaceTeam team = getOrCreatePlayerSpaceRace(packed.name());
        packed.displayName().ifPresent(team::setDisplayName);
        team.setColor(packed.color());
        team.setAllowFriendlyFire(packed.allowFriendlyFire());
        team.setSeeFriendlyInvisibles(packed.seeFriendlyInvisibles());
        team.setPlayerPrefix(packed.memberNamePrefix());
        team.setPlayerSuffix(packed.memberNameSuffix());
        team.setNameTagVisibility(packed.nameTagVisibility());
        team.setDeathMessageVisibility(packed.deathMessageVisibility());
        team.setCollisionRule(packed.collisionRule());
        team.setReachedCelestialBodies(packed.reachedCelestialBodies());
        team.setUnlockedSchematics(packed.unlockedSchematics());
        packed.flag().ifPresent(team::setFlagData);

        for (String player : packed.players()) {
            addPlayerToSpaceRace(player, team);
        }
    }
}
