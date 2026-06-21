/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.transfer.entity;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import io.kalishak.galacticraftlegacy.world.inventory.MappedEquipment;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

import java.util.*;

public class SpaceGearEquipment extends MappedEquipment<GearEquipmentSlot> implements ValueIOSerializable {
    private static final Codec<EnumMap<GearEquipmentSlot, ItemStack>> ENUM_MAP_CODEC = codec(GearEquipmentSlot.class, GearEquipmentSlot.CODEC);
    public static final Codec<SpaceGearEquipment> CODEC = ENUM_MAP_CODEC.xmap(SpaceGearEquipment::new, spaceEquipment -> spaceEquipment.items);
    public static final StreamCodec<RegistryFriendlyByteBuf, SpaceGearEquipment> STREAM_CODEC = streamCodec(GearEquipmentSlot.class, GearEquipmentSlot.STREAM_CODEC).map(SpaceGearEquipment::new, spaceEquipment -> spaceEquipment.items);

    public SpaceGearEquipment() {
        super(new EnumMap<>(GearEquipmentSlot.class));
    }

    private SpaceGearEquipment(EnumMap<GearEquipmentSlot, ItemStack> items) {
        super(items);
    }

    public void tick(Entity entity) {
        for (Map.Entry<GearEquipmentSlot, ItemStack> entry : this.items.entrySet()) {
            ItemStack stack = entry.getValue();

            if (!stack.isEmpty()) {
                stack.inventoryTick(entity.level(), entity, entry.getKey().getRelatedEquipment());
            }
        }
    }

    @Override
    public void serialize(ValueOutput output) {
        output.store("GearEquipment", ENUM_MAP_CODEC, this.items);
    }

    @Override
    public void deserialize(ValueInput input) {
        this.items.clear();
        input.read("GearEquipment", ENUM_MAP_CODEC).ifPresent(this.items::putAll);
    }
}
