/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.server.commands;

import io.kalishak.galacticraftlegacy.server.commands.arguments.item.EquipmentArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public class GalacticraftCommands {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        ArgumentTypeInfos.registerByClass(EquipmentArgument.class, SingletonArgumentInfo.contextFree(EquipmentArgument::equipment));

        EmergencyEquipCommand.register(event.getDispatcher());
    }
}
