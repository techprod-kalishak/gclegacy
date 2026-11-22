/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.gear;

import io.kalishak.galacticraftlegacy.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.item.component.ProtoShieldControllerData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class ShieldControllerItem extends GearItem {
    public ShieldControllerItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return stack.has(GalacticraftDataComponents.SHIELD_CONTROLLER) || intOrZero(stack) < 300;
    }

    @Override
    public int getBarWidth(@NonNull ItemStack stack) {
        return Mth.clamp(Math.round(13.0F - intOrZero(stack) * 13.0F / 300), 0, 13);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
        super.inventoryTick(itemStack, level, owner, slot);
    }

    private static int intOrZero(ItemStack stack) {
        ProtoShieldControllerData protoShieldControllerData = stack.get(GalacticraftDataComponents.SHIELD_CONTROLLER);

        if (protoShieldControllerData == null) {
            return 0;
        }

        return protoShieldControllerData.getTicksBeforeFatalDamage();
    }
}
