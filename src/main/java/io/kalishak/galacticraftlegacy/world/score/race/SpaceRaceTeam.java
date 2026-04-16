/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.score.race;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.world.entity.FlagData;
import io.kalishak.galacticraftlegacy.galaxies.CelestialObject;
import io.kalishak.galacticraftlegacy.attachment.entity.Schematics;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.registry.SchematicVariant;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.Team;
import org.jspecify.annotations.Nullable;

import java.util.*;

/**
 * Mostly based on {@link net.minecraft.world.scores.PlayerTeam}, but with space race stuff duh
 */
public class SpaceRaceTeam extends Team {
    private final SpaceRaceScoreboard spaceRaceScoreboard;
    private final String name;
    private final Set<String> players = Sets.newHashSet();
    private Component displayName;
    private Component playerPrefix = CommonComponents.EMPTY;
    private Component playerSuffix = CommonComponents.EMPTY;
    private boolean allowFriendlyFire = true;
    private boolean seeFriendlyInvisibles = true;
    private Team.Visibility nameTagVisibility = Team.Visibility.ALWAYS;
    private Team.Visibility deathMessageVisibility = Team.Visibility.ALWAYS;
    private ChatFormatting color = ChatFormatting.RESET;
    private Team.CollisionRule collisionRule = Team.CollisionRule.ALWAYS;
    private final Style displayNameStyle;
    private final Object2LongMap<ResourceKey<CelestialObject>> reachedCelestialBodies = new Object2LongOpenHashMap<>();
    private final Schematics unlockedSchematics = Schematics.empty();
    private FlagData flagData = FlagData.DEFAULT;

    SpaceRaceTeam(SpaceRaceScoreboard spaceRaceScoreboard, String name) {
        this.spaceRaceScoreboard = spaceRaceScoreboard;
        this.name = name;
        this.displayName = Component.literal(name);
        this.displayNameStyle = Style.EMPTY.withInsertion(name).withHoverEvent(new HoverEvent.ShowText(Component.literal(name)));
    }

    public Packed pack() {
        return new Packed(
                this.name,
                Optional.of(this.displayName),
                this.color != ChatFormatting.RESET ? Optional.of(this.color) : Optional.empty(),
                this.allowFriendlyFire,
                this.seeFriendlyInvisibles,
                this.playerPrefix,
                this.playerSuffix,
                this.nameTagVisibility,
                this.deathMessageVisibility,
                this.collisionRule,
                List.copyOf(this.players),
                Map.copyOf(this.reachedCelestialBodies),
                Schematics.copyOf(this.unlockedSchematics),
                this.flagData != FlagData.DEFAULT ? Optional.of(this.flagData) : Optional.empty()
        );
    }

    @Override
    public String getName() {
        return this.name;
    }

    public Component getDisplayName() {
        return this.displayName;
    }

    public MutableComponent getFormattedDisplayName() {
        MutableComponent result = ComponentUtils.wrapInSquareBrackets(this.displayName.copy().withStyle(this.displayNameStyle));
        ChatFormatting color = this.getColor();
        if (color != ChatFormatting.RESET) {
            result.withStyle(color);
        }

        return result;
    }

    public void setDisplayName(Component displayName) {
        if (displayName == null) {
            throw new IllegalArgumentException("Name cannot be null");
        } else {
            this.displayName = displayName;
            this.spaceRaceScoreboard.onTeamChanged(this);
        }
    }

    public void setPlayerPrefix(@Nullable Component playerPrefix) {
        this.playerPrefix = playerPrefix == null ? CommonComponents.EMPTY : playerPrefix;
        this.spaceRaceScoreboard.onTeamChanged(this);
    }

    public Component getPlayerPrefix() {
        return this.playerPrefix;
    }

    public void setPlayerSuffix(@Nullable Component playerSuffix) {
        this.playerSuffix = playerSuffix == null ? CommonComponents.EMPTY : playerSuffix;
        this.spaceRaceScoreboard.onTeamChanged(this);
    }

    public Component getPlayerSuffix() {
        return this.playerSuffix;
    }

    @Override
    public Collection<String> getPlayers() {
        return this.players;
    }

    @Override
    public MutableComponent getFormattedName(Component teamMemberName) {
        MutableComponent result = Component.empty().append(this.playerPrefix).append(teamMemberName).append(this.playerSuffix);
        ChatFormatting color = this.getColor();
        if (color != ChatFormatting.RESET) {
            result.withStyle(color);
        }

        return result;
    }

    public static MutableComponent formatNameForTeam(@Nullable Team team, Component name) {
        return team == null ? name.copy() : team.getFormattedName(name);
    }

    @Override
    public boolean isAllowFriendlyFire() {
        return this.allowFriendlyFire;
    }

    public void setAllowFriendlyFire(boolean allowFriendlyFire) {
        this.allowFriendlyFire = allowFriendlyFire;
        this.spaceRaceScoreboard.onTeamChanged(this);
    }

    @Override
    public boolean canSeeFriendlyInvisibles() {
        return this.seeFriendlyInvisibles;
    }

    public void setSeeFriendlyInvisibles(boolean seeFriendlyInvisibles) {
        this.seeFriendlyInvisibles = seeFriendlyInvisibles;
        this.spaceRaceScoreboard.onTeamChanged(this);
    }

    @Override
    public Team.Visibility getNameTagVisibility() {
        return this.nameTagVisibility;
    }

    @Override
    public Team.Visibility getDeathMessageVisibility() {
        return this.deathMessageVisibility;
    }

    public void setNameTagVisibility(Team.Visibility visibility) {
        this.nameTagVisibility = visibility;
        this.spaceRaceScoreboard.onTeamChanged(this);
    }

    public void setDeathMessageVisibility(Team.Visibility visibility) {
        this.deathMessageVisibility = visibility;
        this.spaceRaceScoreboard.onTeamChanged(this);
    }

    @Override
    public Team.CollisionRule getCollisionRule() {
        return this.collisionRule;
    }

    public void setCollisionRule(Team.CollisionRule collisionRule) {
        this.collisionRule = collisionRule;
        this.spaceRaceScoreboard.onTeamChanged(this);
    }

    public void setReachedCelestialBodies(Map<ResourceKey<CelestialObject>, Long> discoveredCelestialBodies) {
        this.reachedCelestialBodies.putAll(discoveredCelestialBodies);
        this.spaceRaceScoreboard.onTeamChanged(this);
    }

    public void setUnlockedSchematics(Schematics schematics) {
        schematics.updateSpaceRaceTeam(this);
        this.spaceRaceScoreboard.onTeamChanged(this);
    }

    public int packOptions() {
        int result = 0;
        if (this.isAllowFriendlyFire()) {
            result |= 1;
        }

        if (this.canSeeFriendlyInvisibles()) {
            result |= 2;
        }

        return result;
    }

    public void unpackOptions(int options) {
        this.setAllowFriendlyFire((options & 1) > 0);
        this.setSeeFriendlyInvisibles((options & 2) > 0);
    }

    public void setColor(ChatFormatting color) {
        this.color = color;
        this.spaceRaceScoreboard.onTeamChanged(this);
    }

    @Override
    public ChatFormatting getColor() {
        return this.color;
    }

    public FlagData getFlagData() {
        return this.flagData;
    }

    public void setFlagData(FlagData flagData) {
        this.flagData = flagData;
        this.spaceRaceScoreboard.onTeamChanged(this);
    }

    public Object2LongMap<ResourceKey<CelestialObject>> getVisitedCelestialBodies() {
        return Object2LongMaps.unmodifiable(this.reachedCelestialBodies);
    }

    public Schematics getUnlockedSchematics() {
        return this.unlockedSchematics;
    }

    public void setReachedCelestialBody(ResourceKey<CelestialObject> body, Level level) {
        this.reachedCelestialBodies.put(body, level.getGameTime());
        this.spaceRaceScoreboard.onTeamChanged(this);
    }

    public void addNewSchematic(ResourceKey<SchematicVariant> schematicId) {
        this.unlockedSchematics.schematicUnlockedByTeam(schematicId);
        this.spaceRaceScoreboard.onTeamChanged(this);
    }

    public record Packed(
            String name,
            Optional<Component> displayName,
            Optional<ChatFormatting> color,
            boolean allowFriendlyFire,
            boolean seeFriendlyInvisibles,
            Component memberNamePrefix,
            Component memberNameSuffix,
            Team.Visibility nameTagVisibility,
            Team.Visibility deathMessageVisibility,
            Team.CollisionRule collisionRule,
            List<String> players,
            Map<ResourceKey<CelestialObject>, Long> reachedCelestialBodies,
            Schematics unlockedSchematics,
            Optional<FlagData> flag
    ) {
        public static final Codec<Packed> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("Name").forGetter(Packed::name),
                ComponentSerialization.CODEC.optionalFieldOf("DisplayName").forGetter(Packed::displayName),
                ChatFormatting.COLOR_CODEC.optionalFieldOf("TeamColor").forGetter(Packed::color),
                Codec.BOOL.optionalFieldOf("AllowFriendlyFire", true).forGetter(Packed::allowFriendlyFire),
                Codec.BOOL.optionalFieldOf("SeeFriendlyInvisibles", true).forGetter(Packed::seeFriendlyInvisibles),
                ComponentSerialization.CODEC.optionalFieldOf("MemberNamePrefix", CommonComponents.EMPTY).forGetter(Packed::memberNamePrefix),
                ComponentSerialization.CODEC.optionalFieldOf("MemberNameSuffix", CommonComponents.EMPTY).forGetter(Packed::memberNameSuffix),
                Team.Visibility.CODEC.optionalFieldOf("NameTagVisibility", Team.Visibility.ALWAYS).forGetter(Packed::nameTagVisibility),
                Team.Visibility.CODEC.optionalFieldOf("DeathMessageVisibility", Team.Visibility.ALWAYS).forGetter(Packed::deathMessageVisibility),
                Team.CollisionRule.CODEC.optionalFieldOf("CollisionRule", Team.CollisionRule.ALWAYS).forGetter(Packed::collisionRule),
                Codec.STRING.listOf().optionalFieldOf("Players", List.of()).forGetter(Packed::players),
                Codec.unboundedMap(ResourceKey.codec(GalacticraftRegistries.Keys.CELESTIAL_OBJECT), Codec.LONG).optionalFieldOf("ReachedCelestialBodies", Map.of()).forGetter(Packed::reachedCelestialBodies),
                Schematics.CODEC.optionalFieldOf("UnlockedSchematics", Schematics.empty()).forGetter(Packed::unlockedSchematics),
                FlagData.CODEC.optionalFieldOf("Flag").forGetter(Packed::flag)
        ).apply(instance, Packed::new));
    }
}
