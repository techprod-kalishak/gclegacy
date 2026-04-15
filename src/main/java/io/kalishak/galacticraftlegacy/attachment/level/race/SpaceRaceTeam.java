/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.attachment.level.race;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.galaxies.CelestialObject;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.attachment.entity.Schematics;
import io.kalishak.galacticraftlegacy.registry.SchematicVariant;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.*;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.compress.utils.Sets;

import java.util.*;

public class SpaceRaceTeam {
    public static final MapCodec<SpaceRaceTeam> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("Name").forGetter(SpaceRaceTeam::getTeamName),
            ComponentSerialization.CODEC.fieldOf("DisplayName").forGetter(SpaceRaceTeam::getDisplayName),
            Vec3.CODEC.optionalFieldOf("TeamColor", Vec3.ZERO).forGetter(SpaceRaceTeam::getTeamColor),
            SpaceRaceMember.CODEC.listOf().optionalFieldOf("Members", List.of()).forGetter(SpaceRaceTeam::listMembers),
            FlagData.CODEC.optionalFieldOf("FlagData", FlagData.DEFAULT).forGetter(SpaceRaceTeam::getFlagData),
            Codec.LONG.optionalFieldOf("Ticks", 0L).forGetter(SpaceRaceTeam::getTicks),
            Codec.unboundedMap(ResourceKey.codec(GalacticraftRegistries.Keys.CELESTIAL_OBJECT), Codec.LONG).xmap(map -> {
                        Object2LongMap<ResourceKey<CelestialObject>> celestialBodyStatus = new Object2LongOpenHashMap<>(4, 1F);
                        celestialBodyStatus.putAll(map);
                        return celestialBodyStatus;
                    }, Object2LongOpenHashMap::new
            ).fieldOf("DiscoveredCelestialBodies").forGetter(SpaceRaceTeam::getVisitedCelestialBodies),
            Schematics.CODEC.fieldOf("UnlockedSchematics").forGetter(SpaceRaceTeam::getTeamUnlockedSchematics)
    ).apply(instance, SpaceRaceTeam::load));
    public static final StreamCodec<RegistryFriendlyByteBuf, SpaceRaceTeam> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, SpaceRaceTeam::getTeamName,
            ComponentSerialization.STREAM_CODEC, SpaceRaceTeam::getDisplayName,
            Vec3.STREAM_CODEC, SpaceRaceTeam::getTeamColor,
            SpaceRaceMember.STREAM_CODEC.apply(ByteBufCodecs.list()), SpaceRaceTeam::listMembers,
            FlagData.STREAM_CODEC, SpaceRaceTeam::getFlagData,
            ByteBufCodecs.LONG, SpaceRaceTeam::getTicks,
            ByteBufCodecs.<ByteBuf, ResourceKey<CelestialObject>, Long, Object2LongMap<ResourceKey<CelestialObject>>>map(i -> new Object2LongOpenHashMap<>(4, 1.0F), ResourceKey.streamCodec(GalacticraftRegistries.Keys.CELESTIAL_OBJECT), ByteBufCodecs.LONG), SpaceRaceTeam::getVisitedCelestialBodies,
            Schematics.STREAM_CODEC, SpaceRaceTeam::getTeamUnlockedSchematics,
            SpaceRaceTeam::load
    );
    public static final Codec<SpaceRaceTeam> CODEC = MAP_CODEC.codec();
    private final String teamName;
    private final Style displayNameStyle;
    private final Set<SpaceRaceMember> members = Sets.newHashSet();
    private final Object2LongMap<ResourceKey<CelestialObject>> discoveredCelestialBodies = new Object2LongOpenHashMap<>(4, 1F);
    private Schematics teamUnlockedSchematics = Schematics.empty();
    private Component displayName;
    private FlagData flagData = FlagData.DEFAULT;
    private Vec3 teamColor = Vec3.ZERO;
    private long ticks = 0L;
    private boolean isDirty = false;

    SpaceRaceTeam(String teamName, Collection<SpaceRaceMember> members) {
        this.teamName = teamName;
        this.members.addAll(members);
        this.displayNameStyle = Style.EMPTY.withInsertion(teamName).withHoverEvent(new HoverEvent.ShowText(Component.literal(teamName)));
    }

    static SpaceRaceTeam load(String name, Component displayName, Vec3 color, List<SpaceRaceMember> members, FlagData flagData, long ticks,
                              Object2LongMap<ResourceKey<CelestialObject>> celestialBodyStatus, Schematics teamSharedUnlockedSchematics) {
        SpaceRaceTeam spaceRaceTeam = new SpaceRaceTeam(name, members);
        spaceRaceTeam.displayName = displayName;
        spaceRaceTeam.teamColor = color;
        spaceRaceTeam.flagData = flagData;
        spaceRaceTeam.ticks = ticks;
        spaceRaceTeam.discoveredCelestialBodies.putAll(celestialBodyStatus);
        spaceRaceTeam.teamUnlockedSchematics = teamSharedUnlockedSchematics;

        return spaceRaceTeam;
    }

    public void tick() {
        this.ticks++;
    }

    public Set<SpaceRaceMember> getMembers() {
        return this.members;
    }

    private List<SpaceRaceMember> listMembers() {
        return List.copyOf(this.members);
    }

    public boolean hasMember(Player player) {
        for (SpaceRaceMember member : this.members) {
            if (member.getPlayerId().equals(player.getUUID())) {
                return true;
            }
        }

        return false;
    }

    public Optional<SpaceRaceTeam> getPlayerTeam(Player player) {
        for (SpaceRaceMember member : this.members) {
            if (member.getPlayerId().equals(player.getUUID())) {
                return Optional.of(this);
            }
        }

        return Optional.empty();
    }

    public boolean addMember(SpaceRaceMember member) {
        if (this.members.add(member)) {
            this.isDirty = true;
            return true;
        }

        return false;
    }

    public boolean addMember(Player player) {
        return addMember(new SpaceRaceMember(player));
    }

    public void setOwnership(Player player) {
        for (SpaceRaceMember member : this.members) {
            if (member.getPermissionLevel() == SpaceRaceMember.Permission.OWNER) {
                member.changePermissionLevel(SpaceRaceMember.Permission.VICE);
                break;
            }
        }

        this.members.stream().filter(member -> member.getPlayerId().equals(player.getUUID())).findFirst().ifPresentOrElse(member -> {
            member.changePermissionLevel(SpaceRaceMember.Permission.OWNER);
        }, () -> {
            SpaceRaceMember member = new SpaceRaceMember(player.getUUID(), SpaceRaceMember.Permission.OWNER, true, true);
            this.members.add(member);
        });

        this.isDirty = true;
    }

    /**
     * @param player to be removed from Space Race
     * @return False when Player is an owner, otherwise it can be removed
     */
    public boolean removeMember(Player player) {
        SpaceRaceMember member = this.members.stream().filter(m -> m.getPlayerId().equals(player.getUUID())).findFirst().orElse(null);

        if (member == null) {
            return false;
        }

        if (member.getPermissionLevel() != SpaceRaceMember.Permission.OWNER) {
            this.members.remove(member);
            this.isDirty = true;
            return true;
        }

        return false;
    }

    public FlagData getFlagData() {
        return this.flagData;
    }

    public void setFlagData(FlagData flagData) {
        this.flagData = flagData;
        this.isDirty = true;
    }

    public Vec3 getTeamColor() {
        return this.teamColor;
    }

    public void setTeamColor(Vec3 teamColor) {
        this.teamColor = teamColor;
        this.isDirty = true;
    }

    public long getTicks() {
        return this.ticks;
    }

    public Object2LongMap<ResourceKey<CelestialObject>> getVisitedCelestialBodies() {
        return Object2LongMaps.unmodifiable(this.discoveredCelestialBodies);
    }

    public Schematics getTeamUnlockedSchematics() {
        return this.teamUnlockedSchematics;
    }

    public void setReachedCelestialBody(ResourceKey<CelestialObject> body) {
        this.discoveredCelestialBodies.put(body, this.ticks);
        this.isDirty = true;
    }

    public void addNewSchematic(ResourceKey<SchematicVariant> schematicId) {
        this.teamUnlockedSchematics.schematicUnlockedByTeam(schematicId);
        this.isDirty = true;
    }

    public void addNewSchematic(Player player, ResourceKey<SchematicVariant> schematicId) {
        this.teamUnlockedSchematics.schematicUnlockedByMember(player, schematicId);
        this.isDirty = true;
    }

    public void updatePlayerSchematics(Player player) {
        Schematics schematics = player.getData(GalacticraftAttachments.PLAYER_SPACE_DATA).getSchematics();

        if (!schematics.isEmpty()) {
            schematics.syncWithTeam(this);
        }
    }

    public String getTeamName() {
        return this.teamName;
    }

    public void setDisplayName(Component name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        } else {
            this.displayName = name;
            this.isDirty = true;
        }
    }

    public Component getDisplayName() {
        return this.displayName;
    }

    public MutableComponent getFormattedName(Component formattedName) {
        MutableComponent displayName = Component.empty().append(formattedName);

        if (this.teamColor != Vec3.ZERO) {
            int color = (((int) this.teamColor.x() + 128) << 16) | (((int) this.teamColor.y() + 128) << 8) | ((int) this.teamColor.z() + 128);

            displayName.withColor(color);
        }

        return displayName;
    }

    public MutableComponent getFormattedDisplayName() {
        MutableComponent displayName = ComponentUtils.wrapInSquareBrackets(this.displayName.copy().withStyle(this.displayNameStyle));

        if (this.teamColor != Vec3.ZERO) {
            int color = (((int) this.teamColor.x() + 128) << 16) | (((int) this.teamColor.y() + 128) << 8) | ((int) this.teamColor.z() + 128);

            displayName.withColor(color);
        }

        return displayName;
    }
}
