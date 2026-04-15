/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.transfer.entity;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.transfer.capability.item.MappedItemResourceHandler;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.function.BiPredicate;

public class SpaceGearEquipment extends MappedItemResourceHandler<GearEquipmentSlot> {
    public static final Codec<SpaceGearEquipment> CODEC = codec(GearEquipmentSlot.class, GearEquipmentSlot.CODEC).xmap(SpaceGearEquipment::new, spaceEquipment -> spaceEquipment.stacks);
    public static final StreamCodec<RegistryFriendlyByteBuf, SpaceGearEquipment> STREAM_CODEC = streamCodec(GearEquipmentSlot.class, GearEquipmentSlot.STREAM_CODEC).map(SpaceGearEquipment::new, spaceEquipment -> spaceEquipment.stacks);

    public SpaceGearEquipment() {
        super(GearEquipmentSlot.class, GearEquipmentSlot.CODEC);
    }

    private SpaceGearEquipment(EnumMap<GearEquipmentSlot, ItemStack> stacks) {
        super(stacks, GearEquipmentSlot.class, GearEquipmentSlot.CODEC);
    }

    public void tick(Entity entity) {
        for (Map.Entry<GearEquipmentSlot, ItemStack> entry : this.stacks.entrySet()) {
            ItemStack stack = entry.getValue();

            if (!stack.isEmpty()) {
                stack.inventoryTick(entity.level(), entity, entry.getKey().getRelatedEquipment());
            }
        }
    }

    public void setAll(SpaceGearEquipment spaceEquipment) {
        this.stacks.clear();
        this.stacks.putAll(spaceEquipment.stacks);
    }

    public void dropAll(LivingEntity entity) {
        for (ItemStack stack : this.stacks.values()) {

            if (!stack.isEmpty()) {
                entity.drop(stack, true, false);
            }
        }

        clearContent();
    }
}
