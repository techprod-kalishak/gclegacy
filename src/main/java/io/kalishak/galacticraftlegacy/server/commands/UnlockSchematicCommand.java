/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.attachment.entity.Schematics;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.registry.SchematicVariant;
import io.kalishak.galacticraftlegacy.server.commands.arguments.SchematicArgument;
import io.kalishak.galacticraftlegacy.server.commands.arguments.SpaceRaceTeamArgument;
import io.kalishak.galacticraftlegacy.world.score.race.SpaceRaceTeam;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;
import java.util.function.BiPredicate;

public class UnlockSchematicCommand {
    private static final SimpleCommandExceptionType ERROR_SCHEMATIC_ALREADY_UNLOCKED = new SimpleCommandExceptionType(GalacticraftComponents.COMMAND_ERROR_SCHEMATIC_ALREADY_UNLOCKED);
    private static final SimpleCommandExceptionType ERROR_SCHEMATIC_NOT_UNLOCKED = new SimpleCommandExceptionType(GalacticraftComponents.COMMAND_ERROR_SCHEMATIC_NOT_UNLOCKED);

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register(
                Commands.literal("schematic")
                        .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .then(
                                Commands.argument("targets", EntityArgument.players())
                                        .then(
                                                Commands.literal("unlock")
                                                        .executes(c -> unlockAllPlayerSchematics(c.getSource(), EntityArgument.getPlayers(c, "targets")))
                                                        .then(
                                                                Commands.argument("id", SchematicArgument.schematic())
                                                                        .executes(c -> addPlayerSchematic(c.getSource(), SchematicArgument.getSchematic(c, "id"), EntityArgument.getPlayers(c, "targets")))
                                                        )
                                        )
                                        .then(
                                                Commands.literal("remove")
                                                        .executes(c -> removeAllPlayerSchematics(c.getSource(), EntityArgument.getPlayers(c, "targets")))
                                                        .then(
                                                                Commands.argument("id", SchematicArgument.schematic())
                                                                        .executes(c -> removePlayerSchematics(c.getSource(), SchematicArgument.getSchematic(c, "id"), EntityArgument.getPlayers(c, "targets")))
                                                        )
                                        )
                                        .then(
                                                Commands.literal("list")
                                                        .executes(c -> showPlayerSchematics(c.getSource(), EntityArgument.getPlayers(c, "targets")))
                                        )
                        )
                        .then(
                                Commands.argument("team", SpaceRaceTeamArgument.spaceRace())
                                        .then(
                                                Commands.literal("unlock")
                                                        .executes(c -> unlockAllSpaceRaceSchematics(c.getSource(), SpaceRaceTeamArgument.getSpaceRace(c, "spaceRace")))
                                                        .then(
                                                                Commands.argument("id", SchematicArgument.schematic())
                                                                        .executes(c -> addSpaceRaceSchematic(c.getSource(), SchematicArgument.getSchematic(c, "id"), SpaceRaceTeamArgument.getSpaceRace(c, "spaceRace")))
                                                        )
                                        )
                                        .then(
                                                Commands.literal("remove")
                                                        .executes(c -> removeAllSpaceRaceSchematics(c.getSource(), SpaceRaceTeamArgument.getSpaceRace(c, "spaceRace")))
                                                        .then(
                                                                Commands.argument("id", SchematicArgument.schematic())
                                                                        .executes(c -> removeSpaceRaceSchematic(c.getSource(), SchematicArgument.getSchematic(c, "id"), SpaceRaceTeamArgument.getSpaceRace(c, "spaceRace")))
                                                        )
                                        )
                                        .then(
                                                Commands.literal("list")
                                                        .executes(c -> showSpaceRaceSchematics(c.getSource(), SpaceRaceTeamArgument.getSpaceRace(c, "spaceRace")))
                                        )
                        )
        );
    }

    private static int unlockAllPlayerSchematics(CommandSourceStack source, Collection<ServerPlayer> players) {
        for (ServerPlayer player : players) {
            Schematics schematics = player.getData(GalacticraftAttachments.PLAYER_SPACE_DATA).getSchematics();

            update(schematics, getRegisteredSchematics(source.registryAccess()), UnlockSchematicCommand::add);
            source.sendSuccess(() -> GalacticraftComponents.COMMAND_SUCCESS_SCHEMATIC_ADD_ALL.apply(player.getDisplayName()), false);
        }

        return 1;
    }

    private static int addPlayerSchematic(CommandSourceStack source, ResourceKey<SchematicVariant> id, Collection<ServerPlayer> players) throws CommandSyntaxException {
        int addedCount = 0;

        for (ServerPlayer player : players) {
            Schematics schematics = player.getData(GalacticraftAttachments.PLAYER_SPACE_DATA).getSchematics();

            if (!add(schematics, id)) {
                throw ERROR_SCHEMATIC_ALREADY_UNLOCKED.create();
            }

            source.sendSuccess(() -> GalacticraftComponents.COMMAND_SUCCESS_SCHEMATIC_ADD.apply(player.getDisplayName(), id.identifier().toString()), false);
            addedCount++;
        }

        return addedCount;
    }

    private static int removeAllPlayerSchematics(CommandSourceStack source, Collection<ServerPlayer> players) {
        for (ServerPlayer player : players) {
            Schematics schematics = player.getData(GalacticraftAttachments.PLAYER_SPACE_DATA).getSchematics();

            update(schematics, getRegisteredSchematics(source.registryAccess()), UnlockSchematicCommand::remove);
            source.sendSuccess(() -> GalacticraftComponents.COMMAND_SUCCESS_SCHEMATIC_REMOVE_ALL.apply(player.getDisplayName()), false);
        }

        return players.size();
    }

    private static int removePlayerSchematics(CommandSourceStack source, ResourceKey<SchematicVariant> id, Collection<ServerPlayer> players) throws CommandSyntaxException {
        int removedCount = 0;

        for (ServerPlayer player : players) {
            Schematics schematics = player.getData(GalacticraftAttachments.PLAYER_SPACE_DATA).getSchematics();

            if (!remove(schematics, id)) {
                throw ERROR_SCHEMATIC_NOT_UNLOCKED.create();
            }

            source.sendSuccess(() -> GalacticraftComponents.COMMAND_SUCCESS_SCHEMATIC_REMOVE.apply(player.getDisplayName(), id.identifier().toString()), false);
            removedCount++;
        }

        return removedCount;
    }

    private static int showPlayerSchematics(CommandSourceStack source, Collection<ServerPlayer> players) {
        for (ServerPlayer player : players) {
            Schematics schematics = player.getData(GalacticraftAttachments.PLAYER_SPACE_DATA).getSchematics();

            if (schematics.isEmpty()) {
                source.sendSuccess(() -> GalacticraftComponents.COMMAND_SUCCESS_SCHEMATIC_EMPTY_LIST.apply(player.getDisplayName()), false);
            } else {
                source.sendSuccess(() -> GalacticraftComponents.COMMAND_SUCCESS_SCHEMATICS_LIST.apply(player.getDisplayName(), schematics.unlockedCount(), ComponentUtils.formatList(schematics.asStringList())), false);
            }
        }

        return players.size();
    }

    private static int unlockAllSpaceRaceSchematics(CommandSourceStack source, SpaceRaceTeam spaceRaceTeam) throws CommandSyntaxException {
        Schematics schematics = spaceRaceTeam.getUnlockedSchematics();

        if (update(schematics, getRegisteredSchematics(source.registryAccess()), UnlockSchematicCommand::add) < 0) {
            if (schematics.isEmpty()) {
                throw ERROR_SCHEMATIC_ALREADY_UNLOCKED.create();
            }
        }

        source.sendSuccess(() -> GalacticraftComponents.COMMAND_SUCCESS_SCHEMATIC_ADD_ALL.apply(spaceRaceTeam.getDisplayName()), true);

        return schematics.unlockedCount();
    }

    private static int addSpaceRaceSchematic(CommandSourceStack source, ResourceKey<SchematicVariant> id, SpaceRaceTeam spaceRaceTeam) throws CommandSyntaxException {
        Schematics schematics = spaceRaceTeam.getUnlockedSchematics();

        if (!remove(schematics, id)) {
            throw ERROR_SCHEMATIC_ALREADY_UNLOCKED.create();
        }

        source.sendSuccess(() -> GalacticraftComponents.COMMAND_SUCCESS_SCHEMATIC_ADD.apply(spaceRaceTeam.getDisplayName(), id.identifier().toString()), false);
        return 1;
    }

    private static int removeAllSpaceRaceSchematics(CommandSourceStack source, SpaceRaceTeam spaceRaceTeam) throws CommandSyntaxException {
        Schematics schematics = spaceRaceTeam.getUnlockedSchematics();

        int i = update(schematics, getRegisteredSchematics(source.registryAccess()), UnlockSchematicCommand::remove);
        source.sendSuccess(() -> GalacticraftComponents.COMMAND_SUCCESS_SCHEMATIC_REMOVE_ALL.apply(spaceRaceTeam.getDisplayName()), true);

        return i;
    }

    private static int removeSpaceRaceSchematic(CommandSourceStack source, ResourceKey<SchematicVariant> id, SpaceRaceTeam spaceRaceTeam) throws CommandSyntaxException {
        Schematics schematics = spaceRaceTeam.getUnlockedSchematics();

        if (!remove(schematics, id)) {
            throw ERROR_SCHEMATIC_NOT_UNLOCKED.create();
        }

        source.sendSuccess(() -> GalacticraftComponents.COMMAND_SUCCESS_SCHEMATIC_REMOVE.apply(spaceRaceTeam.getDisplayName(), id.identifier().toString()), false);
        return 1;
    }

    private static int showSpaceRaceSchematics(CommandSourceStack source, SpaceRaceTeam spaceRaceTeam) {
        Schematics schematics = spaceRaceTeam.getUnlockedSchematics();

        if (schematics.isEmpty()) {
            source.sendSuccess(() -> GalacticraftComponents.COMMAND_SUCCESS_SCHEMATIC_EMPTY_LIST.apply(spaceRaceTeam.getDisplayName()), false);
        } else {
            source.sendSuccess(() -> GalacticraftComponents.COMMAND_SUCCESS_SCHEMATICS_LIST.apply(spaceRaceTeam.getDisplayName(), schematics.unlockedCount(), ComponentUtils.formatList(schematics.asStringList())), false);
        }

        return schematics.unlockedCount();
    }

    private static int update(Schematics schematics, Collection<ResourceKey<SchematicVariant>> keys, BiPredicate<Schematics, ResourceKey<SchematicVariant>> consumer) {
        int removed = 0;

        for (ResourceKey<SchematicVariant> key : keys) {
            if (!consumer.test(schematics, key)) {
                return -1;
            } else {
                removed++;
            }
        }

        return removed;
    }

    private static boolean add(Schematics schematics, ResourceKey<SchematicVariant> schematicVariantKey) {
        return schematics.unlock(schematicVariantKey);
    }

    private static boolean remove(Schematics schematics, ResourceKey<SchematicVariant> schematicVariantKey) {
        return schematics.remove(schematicVariantKey);
    }

    private static Collection<ResourceKey<SchematicVariant>> getRegisteredSchematics(RegistryAccess registryAccess) {
        return registryAccess.lookupOrThrow(GalacticraftRegistries.Keys.SCHEMATIC).registryKeySet();
    }
}
