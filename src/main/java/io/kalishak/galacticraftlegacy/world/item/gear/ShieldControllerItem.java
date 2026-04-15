/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.gear;

import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.item.component.ShieldController;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public class ShieldControllerItem extends GearItem {
    public ShieldControllerItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return stack.has(GalacticraftDataComponents.SHIELD_CONTROLLER) || intOrZero(stack) < 300;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Mth.clamp(Math.round(13.0F - intOrZero(stack) * 13.0F / 300), 0, 13);
    }

    private static int intOrZero(ItemStack stack) {
        ShieldController shieldController = stack.get(GalacticraftDataComponents.SHIELD_CONTROLLER);

        if (shieldController == null) {
            return 0;
        }

        return shieldController.getTicksBeforeFatalDamage();
    }
}
