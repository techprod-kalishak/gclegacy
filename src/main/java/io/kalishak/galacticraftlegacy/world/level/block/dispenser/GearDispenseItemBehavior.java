/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.dispenser;

import io.kalishak.galacticraftlegacy.attachment.AttachmentHelper;
import io.kalishak.galacticraftlegacy.attachment.entity.GearInventoryProvider;
import io.kalishak.galacticraftlegacy.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import io.kalishak.galacticraftlegacy.world.item.component.GearEquippable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class GearDispenseItemBehavior extends DefaultDispenseItemBehavior {
    public static final GearDispenseItemBehavior INSTANCE = new GearDispenseItemBehavior();

    @Override
    protected ItemStack execute(BlockSource source, ItemStack dispensed) {
        return dispenseGear(source, dispensed) ? dispensed : super.execute(source, dispensed);
    }

    public static boolean dispenseGear(BlockSource source, ItemStack dispensed) {
        GearEquippable equippable = dispensed.get(GalacticraftDataComponents.GEAR_EQUIPPABLE);

        if (equippable == null) {
            return false;
        }

        BlockPos pos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
        List<LivingEntity> targets = source.level().getEntitiesOfClass(LivingEntity.class, new AABB(pos), AttachmentHelper::hasGearInventory);

        if (targets.isEmpty()) {
            return false;
        }

        LivingEntity target = targets.getFirst();
        GearInventoryProvider gearInventory = AttachmentHelper.getGearInventory(target);
        GearEquipmentSlot slot = GearEquipmentSlot.getGearSlotForItem(dispensed);

        if (slot == null || equippable.canBeEquippedBy(target.getType())) {
            return false;
        }

        ItemStack equipped = dispensed.split(1);
        gearInventory.getGearEquipment().set(slot, equipped);

        return true;
    }
}
