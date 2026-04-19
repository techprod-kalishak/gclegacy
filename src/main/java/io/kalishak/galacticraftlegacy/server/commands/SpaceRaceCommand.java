/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.server.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.kalishak.galacticraftlegacy.registry.SchematicVariant;
import io.kalishak.galacticraftlegacy.server.commands.arguments.SpaceRaceTeamArgument;
import io.kalishak.galacticraftlegacy.world.score.race.SpaceRaceHooks;
import io.kalishak.galacticraftlegacy.world.score.race.SpaceRaceScoreboard;
import io.kalishak.galacticraftlegacy.world.score.race.SpaceRaceTeam;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ColorArgument;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.commands.arguments.ScoreHolderArgument;
import net.minecraft.commands.arguments.TeamArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.ScoreHolder;
import net.minecraft.world.scores.Team;

import java.util.Collection;
import java.util.Collections;

public class SpaceRaceCommand {
    private static final SimpleCommandExceptionType ERROR_SPACE_RACE_ALREADY_EXISTS = new SimpleCommandExceptionType(
            Component.translatable("commands.space_race.add.duplicate")
    );
    private static final SimpleCommandExceptionType ERROR_SPACE_RACE_ALREADY_EMPTY = new SimpleCommandExceptionType(
            Component.translatable("commands.space_race.empty.unchanged")
    );
    private static final SimpleCommandExceptionType ERROR_SPACE_RACE_ALREADY_NAME = new SimpleCommandExceptionType(
            Component.translatable("commands.space_race.option.name.unchanged")
    );
    private static final SimpleCommandExceptionType ERROR_SPACE_RACE_ALREADY_COLOR = new SimpleCommandExceptionType(
            Component.translatable("commands.space_race.option.color.unchanged")
    );
    private static final SimpleCommandExceptionType ERROR_SPACE_RACE_ALREADY_FRIENDLYFIRE_ENABLED = new SimpleCommandExceptionType(
            Component.translatable("commands.space_race.option.friendlyfire.alreadyEnabled")
    );
    private static final SimpleCommandExceptionType ERROR_SPACE_RACE_ALREADY_FRIENDLYFIRE_DISABLED = new SimpleCommandExceptionType(
            Component.translatable("commands.space_race.option.friendlyfire.alreadyDisabled")
    );
    private static final SimpleCommandExceptionType ERROR_SPACE_RACE_ALREADY_FRIENDLYINVISIBLES_ENABLED = new SimpleCommandExceptionType(
            Component.translatable("commands.space_race.option.seeFriendlyInvisibles.alreadyEnabled")
    );
    private static final SimpleCommandExceptionType ERROR_SPACE_RACE_ALREADY_FRIENDLYINVISIBLES_DISABLED = new SimpleCommandExceptionType(
            Component.translatable("commands.space_race.option.seeFriendlyInvisibles.alreadyDisabled")
    );
    private static final SimpleCommandExceptionType ERROR_SPACE_RACE_NAMETAG_VISIBLITY_UNCHANGED = new SimpleCommandExceptionType(
            Component.translatable("commands.space_race.option.nametagVisibility.unchanged")
    );
    private static final SimpleCommandExceptionType ERROR_SPACE_RACE_DEATH_MESSAGE_VISIBLITY_UNCHANGED = new SimpleCommandExceptionType(
            Component.translatable("commands.space_race.option.deathMessageVisibility.unchanged")
    );
    private static final SimpleCommandExceptionType ERROR_SPACE_RACE_COLLISION_UNCHANGED = new SimpleCommandExceptionType(
            Component.translatable("commands.space_race.option.collisionRule.unchanged")
    );
    private static final SimpleCommandExceptionType ERROR_SPACE_RACE_SCHEMATIC_ALREADY_UNLOCKED = new SimpleCommandExceptionType(
            Component.translatable("commands.space_race.schematics.duplicate")
    );

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register(
                Commands.literal("spaceRace")
                        .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .then(
                                Commands.literal("list")
                                        .executes(c -> listTeams(c.getSource()))
                                        .then(Commands.argument("spaceRace", SpaceRaceTeamArgument.spaceRace()).executes(c -> listMembers(c.getSource(), SpaceRaceTeamArgument.getSpaceRace(c, "spaceRace"))))
                        )
                        .then(
                                Commands.literal("add")
                                        .then(
                                                Commands.argument("spaceRace", StringArgumentType.word())
                                                        .executes(c -> createTeam(c.getSource(), StringArgumentType.getString(c, "spaceRace")))
                                                        .then(
                                                                Commands.argument("displayName", ComponentArgument.textComponent(context))
                                                                        .executes(
                                                                                c -> createTeam(
                                                                                        c.getSource(),
                                                                                        StringArgumentType.getString(c, "spaceRace"),
                                                                                        ComponentArgument.getResolvedComponent(c, "displayName")
                                                                                )
                                                                        )
                                                        )
                                        )
                        )
                        .then(
                                Commands.literal("remove")
                                        .then(Commands.argument("spaceRace", SpaceRaceTeamArgument.spaceRace()).executes(c -> deleteTeam(c.getSource(), SpaceRaceTeamArgument.getSpaceRace(c, "spaceRace"))))
                        )
                        .then(
                                Commands.literal("empty")
                                        .then(Commands.argument("spaceRace", SpaceRaceTeamArgument.spaceRace()).executes(c -> emptyTeam(c.getSource(), SpaceRaceTeamArgument.getSpaceRace(c, "spaceRace"))))
                        )
                        .then(
                                Commands.literal("join")
                                        .then(
                                                Commands.argument("spaceRace", SpaceRaceTeamArgument.spaceRace())
                                                        .executes(
                                                                c -> joinTeam(c.getSource(), SpaceRaceTeamArgument.getSpaceRace(c, "spaceRace"), Collections.singleton(c.getSource().getEntityOrException()))
                                                        )
                                                        .then(
                                                                Commands.argument("members", ScoreHolderArgument.scoreHolders())
                                                                        .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
                                                                        .executes(
                                                                                c -> joinTeam(
                                                                                        c.getSource(), SpaceRaceTeamArgument.getSpaceRace(c, "spaceRace"), ScoreHolderArgument.getNamesWithDefaultWildcard(c, "members")
                                                                                )
                                                                        )
                                                        )
                                        )
                        )
                        .then(
                                Commands.literal("leave")
                                        .then(
                                                Commands.argument("members", ScoreHolderArgument.scoreHolders())
                                                        .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
                                                        .executes(c -> leaveSpaceRace(c.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(c, "members")))
                                        )
                        )
                        .then(
                                Commands.literal("modify")
                                        .then(
                                                Commands.argument("spaceRace", SpaceRaceTeamArgument.spaceRace())
                                                        .then(
                                                                Commands.literal("displayName")
                                                                        .then(
                                                                                Commands.argument("displayName", ComponentArgument.textComponent(context))
                                                                                        .executes(
                                                                                                c -> setDisplayName(
                                                                                                        c.getSource(),
                                                                                                        SpaceRaceTeamArgument.getSpaceRace(c, "spaceRace"),
                                                                                                        ComponentArgument.getResolvedComponent(c, "displayName")
                                                                                                )
                                                                                        )
                                                                        )
                                                        )
                                                        .then(
                                                                Commands.literal("color")
                                                                        .then(
                                                                                Commands.argument("value", ColorArgument.color())
                                                                                        .executes(c -> setColor(c.getSource(), SpaceRaceTeamArgument.getSpaceRace(c, "spaceRace"), ColorArgument.getColor(c, "value")))
                                                                        )
                                                        )
                                                        .then(
                                                                Commands.literal("friendlyFire")
                                                                        .then(
                                                                                Commands.argument("allowed", BoolArgumentType.bool())
                                                                                        .executes(
                                                                                                c -> setFriendlyFire(c.getSource(), SpaceRaceTeamArgument.getSpaceRace(c, "spaceRace"), BoolArgumentType.getBool(c, "allowed"))
                                                                                        )
                                                                        )
                                                        )
                                                        .then(
                                                                Commands.literal("seeFriendlyInvisibles")
                                                                        .then(
                                                                                Commands.argument("allowed", BoolArgumentType.bool())
                                                                                        .executes(
                                                                                                c -> setFriendlySight(
                                                                                                        c.getSource(), SpaceRaceTeamArgument.getSpaceRace(c, "spaceRace"), BoolArgumentType.getBool(c, "allowed")
                                                                                                )
                                                                                        )
                                                                        )
                                                        )
                                                        .then(
                                                                Commands.literal("nametagVisibility")
                                                                        .then(
                                                                                Commands.literal("never")
                                                                                        .executes(c -> setNametagVisibility(c.getSource(), SpaceRaceTeamArgument.getSpaceRace(c, "spaceRace"), Team.Visibility.NEVER))
                                                                        )
                                                                        .then(
                                                                                Commands.literal("hideForOtherTeams")
                                                                                        .executes(
                                                                                                c -> setNametagVisibility(
                                                                                                        c.getSource(), SpaceRaceTeamArgument.getSpaceRace(c, "spaceRace"), Team.Visibility.HIDE_FOR_OTHER_TEAMS
                                                                                                )
                                                                                        )
                                                                        )
                                                                        .then(
                                                                                Commands.literal("hideForOwnTeam")
                                                                                        .executes(
                                                                                                c -> setNametagVisibility(c.getSource(), SpaceRaceTeamArgument.getSpaceRace(c, "spaceRace"), Team.Visibility.HIDE_FOR_OWN_TEAM)
                                                                                        )
                                                                        )
                                                                        .then(
                                                                                Commands.literal("always")
                                                                                        .executes(c -> setNametagVisibility(c.getSource(), SpaceRaceTeamArgument.getSpaceRace(c, "spaceRace"), Team.Visibility.ALWAYS))
                                                                        )
                                                        )
                                                        .then(
                                                                Commands.literal("deathMessageVisibility")
                                                                        .then(
                                                                                Commands.literal("never")
                                                                                        .executes(c -> setDeathMessageVisibility(c.getSource(), SpaceRaceTeamArgument.getSpaceRace(c, "spaceRace"), Team.Visibility.NEVER))
                                                                        )
                                                                        .then(
                                                                                Commands.literal("hideForOtherTeams")
                                                                                        .executes(
                                                                                                c -> setDeathMessageVisibility(
                                                                                                        c.getSource(), SpaceRaceTeamArgument.getSpaceRace(c, "spaceRace"), Team.Visibility.HIDE_FOR_OTHER_TEAMS
                                                                                                )
                                                                                        )
                                                                        )
                                                                        .then(
                                                                                Commands.literal("hideForOwnTeam")
                                                                                        .executes(
                                                                                                c -> setDeathMessageVisibility(
                                                                                                        c.getSource(), SpaceRaceTeamArgument.getSpaceRace(c, "spaceRace"), Team.Visibility.HIDE_FOR_OWN_TEAM
                                                                                                )
                                                                                        )
                                                                        )
                                                                        .then(
                                                                                Commands.literal("always")
                                                                                        .executes(
                                                                                                c -> setDeathMessageVisibility(c.getSource(), SpaceRaceTeamArgument.getSpaceRace(c, "spaceRace"), Team.Visibility.ALWAYS)
                                                                                        )
                                                                        )
                                                        )
                                                        .then(
                                                                Commands.literal("collisionRule")
                                                                        .then(
                                                                                Commands.literal("never")
                                                                                        .executes(c -> setCollision(c.getSource(), SpaceRaceTeamArgument.getSpaceRace(c, "spaceRace"), Team.CollisionRule.NEVER))
                                                                        )
                                                                        .then(
                                                                                Commands.literal("pushOwnTeam")
                                                                                        .executes(c -> setCollision(c.getSource(), SpaceRaceTeamArgument.getSpaceRace(c, "spaceRace"), Team.CollisionRule.PUSH_OWN_TEAM))
                                                                        )
                                                                        .then(
                                                                                Commands.literal("pushOtherTeams")
                                                                                        .executes(
                                                                                                c -> setCollision(c.getSource(), SpaceRaceTeamArgument.getSpaceRace(c, "spaceRace"), Team.CollisionRule.PUSH_OTHER_TEAMS)
                                                                                        )
                                                                        )
                                                                        .then(
                                                                                Commands.literal("always")
                                                                                        .executes(c -> setCollision(c.getSource(), SpaceRaceTeamArgument.getSpaceRace(c, "spaceRace"), Team.CollisionRule.ALWAYS))
                                                                        )
                                                        )
                                                        .then(
                                                                Commands.literal("prefix")
                                                                        .then(
                                                                                Commands.argument("prefix", ComponentArgument.textComponent(context))
                                                                                        .executes(
                                                                                                c -> setPrefix(
                                                                                                        c.getSource(), SpaceRaceTeamArgument.getSpaceRace(c, "spaceRace"), ComponentArgument.getResolvedComponent(c, "prefix")
                                                                                                )
                                                                                        )
                                                                        )
                                                        )
                                                        .then(
                                                                Commands.literal("suffix")
                                                                        .then(
                                                                                Commands.argument("suffix", ComponentArgument.textComponent(context))
                                                                                        .executes(
                                                                                                c -> setSuffix(
                                                                                                        c.getSource(), SpaceRaceTeamArgument.getSpaceRace(c, "spaceRace"), ComponentArgument.getResolvedComponent(c, "suffix")
                                                                                                )
                                                                                        )
                                                                        )
                                                        )
                                        )
                        )
                        .then(
                                Commands.literal("copyFrom")
                                        .then(
                                                Commands.argument("team",  TeamArgument.team())
                                                        .executes(
                                                                c -> createFromPlayerTeam(c.getSource(), TeamArgument.getTeam(c, "team"))
                                                        )
                                        )
                        )
        );

    }

    private static Component getFirstMemberName(Collection<ScoreHolder> members) {
        return members.iterator().next().getFeedbackDisplayName();
    }

    private static int leaveSpaceRace(CommandSourceStack source, Collection<ScoreHolder> members) {
        SpaceRaceScoreboard scoreboard = SpaceRaceHooks.getFromServer(source.getServer());

        for (ScoreHolder member : members) {
            scoreboard.removePlayerFromSpaceRace(member.getScoreboardName());
        }

        if (members.size() == 1) {
            source.sendSuccess(() -> Component.translatable("commands.space_race.leave.success.single", getFirstMemberName(members)), true);
        } else {
            source.sendSuccess(() -> Component.translatable("commands.space_race.leave.success.multiple", members.size()), true);
        }

        return members.size();
    }

    private static int joinTeam(CommandSourceStack source, SpaceRaceTeam spaceRaceTeam, Collection<ScoreHolder> members) {
        SpaceRaceScoreboard scoreboard = SpaceRaceHooks.getFromServer(source.getServer());
        
        for (ScoreHolder member : members) {
            scoreboard.addPlayerToSpaceRace(member.getScoreboardName(), spaceRaceTeam);
        }

        if (members.size() == 1) {
            source.sendSuccess(() -> Component.translatable("commands.space_race.join.success.single", getFirstMemberName(members)), true);
        } else {
            source.sendSuccess(() -> Component.translatable("commands.space_race.join.success.multiple", members.size()), true);
        }

        return members.size();
    }

    private static int setNametagVisibility(CommandSourceStack source, SpaceRaceTeam spaceRaceTeam, Team.Visibility visibility) throws CommandSyntaxException {
        if (spaceRaceTeam.getNameTagVisibility() == visibility) {
            throw ERROR_SPACE_RACE_NAMETAG_VISIBLITY_UNCHANGED.create();
        } else {
            spaceRaceTeam.setNameTagVisibility(visibility);
            source.sendSuccess(() -> Component.translatable("commands.space_race.option.nametagVisibility.success", spaceRaceTeam.getFormattedDisplayName(), visibility.getDisplayName()), true);
            
            return 0;
        }
    }

    private static int setDeathMessageVisibility(CommandSourceStack source, SpaceRaceTeam spaceRaceTeam, Team.Visibility visibility) throws CommandSyntaxException {
        if (spaceRaceTeam.getDeathMessageVisibility() == visibility) {
            throw ERROR_SPACE_RACE_DEATH_MESSAGE_VISIBLITY_UNCHANGED.create();
        } else {
            spaceRaceTeam.setDeathMessageVisibility(visibility);
            source.sendSuccess(() -> Component.translatable("commands.space_race.option.deathMessageVisibility.success", spaceRaceTeam.getFormattedDisplayName(), visibility.getDisplayName()), true);
            
            return 0;
        }
    }

    private static int setCollision(CommandSourceStack source, SpaceRaceTeam spaceRaceTeam, Team.CollisionRule collision) throws CommandSyntaxException {
        if (spaceRaceTeam.getCollisionRule() == collision) {
            throw ERROR_SPACE_RACE_COLLISION_UNCHANGED.create();
        } else {
            spaceRaceTeam.setCollisionRule(collision);
            source.sendSuccess(() -> Component.translatable("commands.space_race.option.collisionRule.success", spaceRaceTeam.getFormattedDisplayName(), collision.getDisplayName()), true);

            return 0;
        }
    }

    private static int setFriendlySight(CommandSourceStack source, SpaceRaceTeam spaceRaceTeam, boolean allowed) throws CommandSyntaxException {
        if (spaceRaceTeam.canSeeFriendlyInvisibles() == allowed) {
            if (allowed) {
                throw ERROR_SPACE_RACE_ALREADY_FRIENDLYINVISIBLES_ENABLED.create();
            } else {
                throw ERROR_SPACE_RACE_ALREADY_FRIENDLYINVISIBLES_DISABLED.create();
            }
        } else {
            spaceRaceTeam.setSeeFriendlyInvisibles(allowed);
            source.sendSuccess(() -> Component.translatable("commands.space_race.option.seeFriendlyInvisibles." + (allowed ? "enabled" : "disabled"), spaceRaceTeam.getFormattedDisplayName()), true);
            return 0;
        }
    }

    private static int setFriendlyFire(CommandSourceStack source, SpaceRaceTeam spaceRaceTeam, boolean allowed) throws CommandSyntaxException {
        if (spaceRaceTeam.isAllowFriendlyFire() == allowed) {
            if (allowed) {
                throw ERROR_SPACE_RACE_ALREADY_FRIENDLYFIRE_ENABLED.create();
            } else {
                throw ERROR_SPACE_RACE_ALREADY_FRIENDLYFIRE_DISABLED.create();
            }
        } else {
            spaceRaceTeam.setAllowFriendlyFire(allowed);
            source.sendSuccess(() -> Component.translatable("commands.space_race.option.friendlyfire." + (allowed ? "enabled" : "disabled"), spaceRaceTeam.getFormattedDisplayName()), true);

            return 0;
        }
    }

    private static int setDisplayName(CommandSourceStack source, SpaceRaceTeam spaceRaceTeam, Component displayName) throws CommandSyntaxException {
        if (spaceRaceTeam.getDisplayName().equals(displayName)) {
            throw ERROR_SPACE_RACE_ALREADY_NAME.create();
        } else {
            spaceRaceTeam.setDisplayName(displayName);
            source.sendSuccess(() -> Component.translatable("commands.space_race.option.name.success", spaceRaceTeam.getFormattedDisplayName()), true);

            return 0;
        }
    }

    private static int setColor(CommandSourceStack source, SpaceRaceTeam spaceRaceTeam, ChatFormatting color) throws CommandSyntaxException {
        if (spaceRaceTeam.getColor() == color) {
            throw ERROR_SPACE_RACE_ALREADY_COLOR.create();
        } else {
            spaceRaceTeam.setColor(color);
            source.sendSuccess(() -> Component.translatable("commands.space_race.option.color.success", spaceRaceTeam.getFormattedDisplayName(), color.getName()), true);

            return 0;
        }
    }

    private static int emptyTeam(CommandSourceStack source, SpaceRaceTeam spaceRaceTeam) throws CommandSyntaxException {
        SpaceRaceScoreboard scoreboard = SpaceRaceHooks.getFromServer(source.getServer());

        Collection<String> members = Lists.newArrayList(spaceRaceTeam.getPlayers());
        if (members.isEmpty()) {
            throw ERROR_SPACE_RACE_ALREADY_EMPTY.create();
        } else {
            for(String member : members) {
                scoreboard.removePlayerFromSpaceRace(member, spaceRaceTeam);
            }

            source.sendSuccess(() -> Component.translatable("commands.space_race.empty.success", members.size(), spaceRaceTeam.getFormattedDisplayName()), true);
            return members.size();
        }
    }

    private static int deleteTeam(CommandSourceStack source, SpaceRaceTeam spaceRaceTeam) {
        SpaceRaceScoreboard scoreboard = SpaceRaceHooks.getFromServer(source.getServer());

        scoreboard.removeSpaceRace(spaceRaceTeam);
        source.sendSuccess(() -> Component.translatable("commands.space_race.remove.success", spaceRaceTeam.getFormattedDisplayName()), true);

        return scoreboard.getSpaceRaceTeams().size();
    }

    private static int createTeam(CommandSourceStack source, String name) throws CommandSyntaxException {
        return createTeam(source, name, Component.literal(name));
    }

    private static int createTeam(CommandSourceStack source, String name, Component displayName) throws CommandSyntaxException {
        SpaceRaceScoreboard scoreboard = SpaceRaceHooks.getFromServer(source.getServer());

        if (scoreboard.getSpaceRaceTeam(name) != null) {
            throw ERROR_SPACE_RACE_ALREADY_EXISTS.create();
        } else {
            SpaceRaceTeam spaceRaceTeam = scoreboard.getOrCreatePlayerSpaceRace(name);
            spaceRaceTeam.setDisplayName(displayName);
            source.sendSuccess(() -> Component.translatable("commands.space_race.add.success", spaceRaceTeam.getFormattedDisplayName()), true);

            return scoreboard.getSpaceRaceTeams().size();
        }
    }

    private static int listMembers(CommandSourceStack source, SpaceRaceTeam spaceRaceTeam) {
        Collection<String> members = spaceRaceTeam.getPlayers();
        if (members.isEmpty()) {
            source.sendSuccess(() -> Component.translatable("commands.space_race.list.members.empty", spaceRaceTeam.getFormattedDisplayName()), false);
        } else {
            source.sendSuccess(() -> Component.translatable("commands.space_race.list.members.success", spaceRaceTeam.getFormattedDisplayName(), members.size(), ComponentUtils.formatList(members)), false);
        }

        return members.size();
    }

    private static int listTeams(CommandSourceStack source) {
        SpaceRaceScoreboard scoreboard = SpaceRaceHooks.getFromServer(source.getServer());

        Collection<SpaceRaceTeam> spaceRaceTeams = scoreboard.getSpaceRaceTeams();
        if (spaceRaceTeams.isEmpty()) {
            source.sendSuccess(() -> Component.translatable("commands.space_race.list.space_races.empty"), false);
        } else {
            source.sendSuccess(() -> Component.translatable("commands.space_race.list.space_races.success", spaceRaceTeams.size(), ComponentUtils.formatList(spaceRaceTeams, SpaceRaceTeam::getFormattedDisplayName)), false);
        }

        return spaceRaceTeams.size();
    }

    private static int setPrefix(CommandSourceStack source, SpaceRaceTeam spaceRaceTeam, Component prefix) {
        spaceRaceTeam.setPlayerPrefix(prefix);
        source.sendSuccess(() -> Component.translatable("commands.space_race.option.prefix.success", prefix), false);

        return 1;
    }

    private static int setSuffix(CommandSourceStack source, SpaceRaceTeam spaceRaceTeam, Component suffix) {
        spaceRaceTeam.setPlayerSuffix(suffix);
        source.sendSuccess(() -> Component.translatable("commands.space_race.option.suffix.success", suffix), false);

        return 1;
    }

    private static int unlockSchematic(CommandSourceStack source, SpaceRaceTeam spaceRaceTeam, ResourceKey<SchematicVariant> schematic) throws CommandSyntaxException {
        if (spaceRaceTeam.getUnlockedSchematics().isUnlocked(schematic)) {
            throw ERROR_SPACE_RACE_SCHEMATIC_ALREADY_UNLOCKED.create();
        }

        spaceRaceTeam.getUnlockedSchematics().unlock(schematic);
        source.sendSuccess(() -> Component.translatable("commands.space_race.option.color.success", spaceRaceTeam.getFormattedDisplayName(), schematic.identifier()), true);

        return 1;
    }

    private static int createFromPlayerTeam(CommandSourceStack source, PlayerTeam spaceRace) throws CommandSyntaxException {
        SpaceRaceScoreboard scoreboard = SpaceRaceHooks.getFromServer(source.getServer());

        if (scoreboard.getSpaceRaceTeam(spaceRace.getName()) != null) {
            throw ERROR_SPACE_RACE_ALREADY_EXISTS.create();
        }

        SpaceRaceTeam spaceRaceTeam = scoreboard.addSpaceRace(spaceRace.getName());
        spaceRaceTeam.setDisplayName(spaceRace.getDisplayName());
        spaceRaceTeam.setPlayerPrefix(spaceRace.getPlayerPrefix());
        spaceRaceTeam.setPlayerSuffix(spaceRace.getPlayerSuffix());
        spaceRaceTeam.setAllowFriendlyFire(spaceRace.isAllowFriendlyFire());
        spaceRaceTeam.setDeathMessageVisibility(spaceRace.getDeathMessageVisibility());
        spaceRaceTeam.setColor(spaceRace.getColor());
        spaceRaceTeam.setCollisionRule(spaceRace.getCollisionRule());

        return 1;
    }
}