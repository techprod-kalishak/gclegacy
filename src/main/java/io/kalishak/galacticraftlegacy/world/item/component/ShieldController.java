/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.component;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.attachment.AttachmentHelper;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class ShieldController implements TooltipProvider {
    public static final Codec<ShieldController> CODEC = Codec.INT.xmap(ShieldController::new, ShieldController::getTicksBeforeFatalDamage);
    public static final StreamCodec<ByteBuf, ShieldController> STREAM_CODEC = ByteBufCodecs.INT.map(ShieldController::new, ShieldController::getTicksBeforeFatalDamage);
    private int ticksBeforeFatalDamage;

    public ShieldController(final int ticksBeforeFatalDamage) {
        this.ticksBeforeFatalDamage = ticksBeforeFatalDamage;
    }

    public int getTicksBeforeFatalDamage() {
        return ticksBeforeFatalDamage;
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag flag, DataComponentGetter componentGetter) {
        if (flag.hasShiftDown()) {
            tooltipAdder.accept(Component.literal("Remaining time: " + this.ticksBeforeFatalDamage));
        }
    }

    public void depleteByValue(ItemStack controller, LivingEntity owner, Level level, int value) {
        if (this.ticksBeforeFatalDamage - value <= 0) {
            level.playLocalSound(owner, SoundEvents.ITEM_BREAK.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
            controller.shrink(1);
            AttachmentHelper.getGearInventory(owner).getGearEquipment().set(GearEquipmentSlot.SHIELD, ItemStack.EMPTY);
        } else {
            this.ticksBeforeFatalDamage -= value;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof ShieldController)) {
            return false;
        }

        return ((ShieldController)obj).ticksBeforeFatalDamage == this.ticksBeforeFatalDamage;
    }

    @Override
    public int hashCode() {
        return 23 + Integer.hashCode(this.ticksBeforeFatalDamage);
    }
}
