/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.dispenser;

import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.neoforge.transfer.fluid.DispenseFluidContainer;

public class GalacticraftDispenserBehaviors {
    public static void registerDispenseBehaviors() {
        DispenserBlock.registerBehavior(GalacticraftItems.OIL_BUCKET, DispenseFluidContainer.getInstance());
        DispenserBlock.registerBehavior(GalacticraftItems.FUEL_BUCKET, DispenseFluidContainer.getInstance());

        DispenserBlock.registerBehavior(GalacticraftItems.THERMAL_PADDING_HELM, GearDispenseItemBehavior.INSTANCE);
        DispenserBlock.registerBehavior(GalacticraftItems.THERMAL_PADDING_CHESTPIECE, GearDispenseItemBehavior.INSTANCE);
        DispenserBlock.registerBehavior(GalacticraftItems.THERMAL_PADDING_LEGGINGS, GearDispenseItemBehavior.INSTANCE);
        DispenserBlock.registerBehavior(GalacticraftItems.THERMAL_PADDING_BOOTS, GearDispenseItemBehavior.INSTANCE);
        DispenserBlock.registerBehavior(GalacticraftItems.ISOTHERMAL_HELM, GearDispenseItemBehavior.INSTANCE);
        DispenserBlock.registerBehavior(GalacticraftItems.ISOTHERMAL_CHESTPIECE, GearDispenseItemBehavior.INSTANCE);
        DispenserBlock.registerBehavior(GalacticraftItems.ISOTHERMAL_LEGGINGS, GearDispenseItemBehavior.INSTANCE);
        DispenserBlock.registerBehavior(GalacticraftItems.ISOTHERMAL_BOOTS, GearDispenseItemBehavior.INSTANCE);

        DispenserBlock.registerBehavior(GalacticraftItems.OXYGEN_MASK, GearDispenseItemBehavior.INSTANCE);
        DispenserBlock.registerBehavior(GalacticraftItems.OXYGEN_GEAR, GearDispenseItemBehavior.INSTANCE);
        DispenserBlock.registerBehavior(GalacticraftItems.LIGHT_TANK, GearDispenseItemBehavior.INSTANCE);
        DispenserBlock.registerBehavior(GalacticraftItems.MEDIUM_TANK, GearDispenseItemBehavior.INSTANCE);
        DispenserBlock.registerBehavior(GalacticraftItems.HEAVY_TANK, GearDispenseItemBehavior.INSTANCE);
        DispenserBlock.registerBehavior(GalacticraftItems.SHIELD_CONTROLLER, GearDispenseItemBehavior.INSTANCE);
        DispenserBlock.registerBehavior(GalacticraftItems.PROTO_SHIELD_CONTROLLER, GearDispenseItemBehavior.INSTANCE);
        GalacticraftItems.PARACHUTE.forEach(item -> DispenserBlock.registerBehavior(item, GearDispenseItemBehavior.INSTANCE));
    }
}
