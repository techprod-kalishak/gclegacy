/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import io.kalishak.galacticraftlegacy.attachment.AttachmentHelper;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import io.kalishak.galacticraftlegacy.server.commands.arguments.item.EmergencyEquipment;
import io.kalishak.galacticraftlegacy.transfer.entity.SpaceGearEquipment;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Arrays;
import java.util.Collection;

public class EmergencyEquipCommand {
    private static final Dynamic2CommandExceptionType ERROR_ITEMS_NOT_EQUPPABLE = new Dynamic2CommandExceptionType(GalacticraftComponents.COMMAND_ERROR_NOT_EQUIPABLE);
    private static final DynamicCommandExceptionType ERROR_INVALID_ENTITIY = new  DynamicCommandExceptionType(GalacticraftComponents.COMMAND_ERROR_INVALID_GEAR_OWNER);
    public static final SuggestionProvider<CommandSourceStack> SUGGEST_EQUIPMENT = (_, builder) ->
            SharedSuggestionProvider.suggest(Arrays.stream(EmergencyEquipment.values()).map(EmergencyEquipment::getSerializedName), builder);

    static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("equipment")
                        .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .then(
                                Commands.argument("target", EntityArgument.player())
                                        .executes(
                                                c -> fillPlayerEquipment(
                                                        c.getSource(), EntityArgument.getPlayer(c, "target"), EmergencyEquipment.FULL, false
                                                )
                                        )
                                        .then(
                                                Commands.argument("equipment", StringArgumentType.word())
                                                        .suggests(SUGGEST_EQUIPMENT)
                                                        .executes(
                                                                c -> fillPlayerEquipment(
                                                                        c.getSource(), EntityArgument.getPlayer(c, "target"), StringArgumentType.getString(c, "equipment"), false
                                                                )
                                                        )
                                                        .then(
                                                                Commands.argument("swapExisting", BoolArgumentType.bool())
                                                                        .executes(c -> fillPlayerEquipment(
                                                                                c.getSource(), EntityArgument.getPlayer(c, "target"), StringArgumentType.getString(c, "equipment"), BoolArgumentType.getBool(c, "swapExisting")
                                                                        ))
                                                        )
                                        )
                        )
                        .then(
                                Commands.argument("targets", EntityArgument.players())
                                        .executes(
                                                c -> fillPlayersEquipments(
                                                        c.getSource(), EntityArgument.getPlayers(c, "targets"), EmergencyEquipment.FULL, false
                                                )
                                        )
                                        .then(
                                                Commands.argument("equipment", StringArgumentType.word())
                                                        .executes(
                                                                c -> fillPlayersEquipments(
                                                                        c.getSource(), EntityArgument.getPlayers(c, "target"), StringArgumentType.getString(c, "equipment"), false
                                                                )
                                                        )
                                                        .then(
                                                                Commands.argument("swapExisting", BoolArgumentType.bool())
                                                                        .executes(
                                                                                c -> fillPlayersEquipments(
                                                                                        c.getSource(), EntityArgument.getPlayers(c, "target"), StringArgumentType.getString(c, "equipment"), BoolArgumentType.getBool(c, "swapExisting")
                                                                                )
                                                                        )
                                                        )
                                        )
                        )
        );
    }

    private static int fillPlayerEquipment(CommandSourceStack source, ServerPlayer player, String equipment, boolean swapExisting) throws CommandSyntaxException {
        return fillPlayerEquipment(
                source,
                player,
                EmergencyEquipment.byName(equipment),
                swapExisting
        );
    }

    private static int fillPlayerEquipment(CommandSourceStack source, ServerPlayer player, EmergencyEquipment equipment, boolean swapExisting) throws CommandSyntaxException {
        int swapped = createEquipment(player, equipment, swapExisting);

        if (swapped > 0) {
            source.sendSuccess(() -> GalacticraftComponents.COMMAND_SUCCESS_EQUIP_EMERGENCY_GEAR.apply(player.getDisplayName(), swapped), true);
        } else {
            throw ERROR_INVALID_ENTITIY.create(player.getDisplayName());
        }

        return swapped;
    }

    private static int fillPlayersEquipments(CommandSourceStack source, Collection<ServerPlayer> players, EmergencyEquipment equipment, boolean swapExisting) throws CommandSyntaxException {
        for (ServerPlayer player : players) {
            fillPlayerEquipment(source, player, equipment, swapExisting);
        }

        return players.size();
    }

    private static int fillPlayersEquipments(CommandSourceStack source, Collection<ServerPlayer> players, String equipment, boolean swapExisting) throws CommandSyntaxException {
        return fillPlayersEquipments(
                source,
                players,
                EmergencyEquipment.byName(equipment),
                swapExisting
        );
    }

    static int createEquipment(LivingEntity target, EmergencyEquipment args, boolean swap) throws CommandSyntaxException {
        int swapped = 0;

        if (!target.is(GalacticraftTags.EntityTypes.CAN_EQUIP_GEAR)) {
            throw ERROR_ITEMS_NOT_EQUPPABLE.create(target.getType(), args);
        }

        SpaceGearEquipment spaceGearEquipment = new SpaceGearEquipment();
        spaceGearEquipment.set(GearEquipmentSlot.MASK, GalacticraftItems.OXYGEN_MASK.toStack());
        spaceGearEquipment.set(GearEquipmentSlot.GEAR, GalacticraftItems.OXYGEN_GEAR.toStack());
        spaceGearEquipment.set(GearEquipmentSlot.TANK, args.getPreferredTank().get().getDefaultInstance());
        spaceGearEquipment.set(GearEquipmentSlot.ADDITIONAL_TANK, args.getPreferredTank().get().getDefaultInstance());

        if (args == EmergencyEquipment.FULL) {
            if (!(target instanceof Player)) {
                throw ERROR_ITEMS_NOT_EQUPPABLE.create(target.getType(), args);
            }

            spaceGearEquipment.set(GearEquipmentSlot.THERMAL_CAP, GalacticraftItems.ISOTHERMAL_HELM.toStack());
            spaceGearEquipment.set(GearEquipmentSlot.THERMAL_SHIRT, GalacticraftItems.ISOTHERMAL_CHESTPIECE.toStack());
            spaceGearEquipment.set(GearEquipmentSlot.THERMAL_LEGGINGS, GalacticraftItems.ISOTHERMAL_LEGGINGS.toStack());
            spaceGearEquipment.set(GearEquipmentSlot.THERMAL_SOCKS, GalacticraftItems.ISOTHERMAL_BOOTS.toStack());
            spaceGearEquipment.set(GearEquipmentSlot.PARACHUTE, GalacticraftItems.WHITE_PARACHUTE.toStack());
            //spaceGearEquipment.set(GearEquipmentSlot.TELEMETRY, GalacticraftItems.TELEMETRY_MODULE.toStack());
            spaceGearEquipment.set(GearEquipmentSlot.SHIELD, GalacticraftItems.SHIELD_CONTROLLER.toStack());
        }

        SpaceGearEquipment entitySpaceGearEquipment = AttachmentHelper.getGearInventory(target).getGearEquipment();

        for (GearEquipmentSlot slot : args.getGroup().validSlots()) {
            if (!entitySpaceGearEquipment.get(slot).isEmpty() && !swap) {
                break;
            }

            entitySpaceGearEquipment.set(slot, spaceGearEquipment.get(slot));
            swapped++;
        }

        return swapped;
    }
}
