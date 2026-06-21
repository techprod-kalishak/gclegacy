/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.mixin;

import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.transfer.ResourcefulHelper;
import io.kalishak.galacticraftlegacy.transfer.entity.SpaceGearEquipment;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(Inventory.class)
public class InventoryMixin {
    @Shadow
    @Final
    public Player player;

    @Inject(method = "clearOrCountMatchingItems", at = @At("TAIL"), cancellable = true)
    private void galacticraftlegacy$clearOrCountMatchingItems(Predicate<ItemStack> predicate, int amountToRemove, Container craftSlots, CallbackInfoReturnable<Integer> cir) {
        int value = cir.getReturnValue();
        SpaceGearEquipment equipment = player.getData(GalacticraftAttachments.PLAYER_SPACE_DATA).getGearEquipment();

        int cleared = 0;

        for (GearEquipmentSlot slot : GearEquipmentSlot.values()) {
            ItemStack stack = equipment.get(slot);

            cleared += ContainerHelper.clearOrCountMatchingItems(
                    stack,
                    predicate,
                    amountToRemove - value,
                    amountToRemove == 0
            );
        }

        if (cleared > 0) {
            cir.setReturnValue(value + cleared);
        }
    }
}
