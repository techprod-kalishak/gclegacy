/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block;

import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class GalacticraftDispenserBehaviors {
    public static void registerDispenseBehaviors() {
        DispenseItemBehavior dispenseLiquid = new DefaultDispenseItemBehavior() {
            private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

            @Override
            public ItemStack execute(BlockSource blockSource, ItemStack itemStack) {
                DispensibleContainerItem dispensibleContainer = (DispensibleContainerItem) itemStack.getItem();
                BlockPos blockPos = blockSource.pos().relative(blockSource.state().getValue(DispenserBlock.FACING));
                Level level = blockSource.level();

                if (dispensibleContainer.emptyContents(null, level, blockPos, null, itemStack)) {
                    dispensibleContainer.checkExtraContent(null, level, itemStack, blockPos);
                    return this.consumeWithRemainder(blockSource, itemStack, new ItemStack(Items.BUCKET));
                }

                return this.defaultDispenseItemBehavior.dispense(blockSource, itemStack);
            }
        };
        DispenserBlock.registerBehavior(GalacticraftItems.OIL_BUCKET, dispenseLiquid);
        DispenserBlock.registerBehavior(GalacticraftItems.FUEL_BUCKET, dispenseLiquid);
    }
}
