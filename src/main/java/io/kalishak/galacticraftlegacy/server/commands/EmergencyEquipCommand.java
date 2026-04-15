/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import io.kalishak.galacticraftlegacy.attachment.AttachmentHelper;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.server.commands.arguments.item.EmergencyEquipment;
import io.kalishak.galacticraftlegacy.server.commands.arguments.item.EquipmentArgument;
import io.kalishak.galacticraftlegacy.transfer.entity.SpaceGearEquipment;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;

public class EmergencyEquipCommand {
    private static final Dynamic2CommandExceptionType ERROR_ITEMS_NOT_EQUPPABLE = new Dynamic2CommandExceptionType(
            (entity, equipment) -> Component.translatableEscape("galacticraftlegacy.arguments.equipment.not_equippable", entity, equipment)
    );

    static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("equipment")
                        .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .executes(cxt -> fillPlayerEquipment(cxt.getSource(), cxt.getSource().getPlayerOrException()))
                        .then(Commands.argument("targets", EntityArgument.entities()))
                        .then(Commands.argument("equipment", EquipmentArgument.equipment()))
                        .then(Commands.argument("swapExisting", BoolArgumentType.bool()))
                        .executes(
                                cxt -> execute(
                                        cxt.getSource(),
                                        EntityArgument.getEntities(cxt, "targets"),
                                        EquipmentArgument.getEquipment(cxt, "equipment"),
                                        BoolArgumentType.getBool(cxt, "swapExisting")
                                )
                        )
        );
    }

    private static int fillPlayerEquipment(CommandSourceStack source, ServerPlayer player) throws CommandSyntaxException {
        int swapped = createEquipment(player, EmergencyEquipment.FULL, true);

        if (swapped > 0) {
            source.sendSuccess(() -> Component.translatable("galacticraftlegacy.commands.emergency_equip.success", player.getDisplayName(), swapped), true);
        } else {
            source.sendFailure(Component.translatable("galacticraftlegacy.commands.emergency_equip.failure", player.getDisplayName()));
        }

        return swapped;
    }

    private static int execute(CommandSourceStack source, Collection<? extends Entity> targets, EmergencyEquipment equipment, boolean swapExisting) throws CommandSyntaxException {
        for (Entity target : targets) {
            if (!(target instanceof LivingEntity)) {
                source.sendFailure(Component.translatable("galacticraftlegacy.commands.emergency_equip.failure", target.getDisplayName()));
            } else {
                int swapped = createEquipment((LivingEntity) target, equipment, swapExisting);

                if (swapped > 0) {
                    source.sendSuccess(() -> Component.translatable("galacticraftlegacy.commands.emergency_equip.success", target.getDisplayName(), swapped), true);
                } else {
                    source.sendFailure(Component.translatable("galacticraftlegacy.commands.emergency_equip.failure", target.getDisplayName()));
                }
            }
        }

        return targets.size();
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
