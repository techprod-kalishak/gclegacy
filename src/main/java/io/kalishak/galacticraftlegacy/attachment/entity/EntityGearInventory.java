/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.attachment.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.transfer.entity.SpaceGearEquipment;
import io.kalishak.galacticraftlegacy.world.entity.GearDropChances;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentTable;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.item.component.GearEquippable;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntityGearInventory extends GearInventoryProvider {
    public static final MapCodec<EntityGearInventory> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SpaceGearEquipment.CODEC.fieldOf("gear_equipment").forGetter(EntityGearInventory::getGearEquipment),
            GearDropChances.CODEC.optionalFieldOf("drop_chances", GearDropChances.DEFAULT).forGetter(inventory -> inventory.gearDropChances)
    ).apply(instance, EntityGearInventory::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, EntityGearInventory> STREAM_CODEC = StreamCodec.composite(
            SpaceGearEquipment.STREAM_CODEC, EntityGearInventory::getGearEquipment,
            GearDropChances.STREAM_CODEC, inventory -> inventory.gearDropChances,
            EntityGearInventory::new
    );

    private GearDropChances gearDropChances = GearDropChances.DEFAULT;

    private EntityGearInventory(SpaceGearEquipment gearEquipment, GearDropChances gearDropChances) {
        super(gearEquipment);
        this.gearDropChances = gearDropChances;
    }

    public EntityGearInventory() {
        super(new SpaceGearEquipment());
    }

    public void equip(GearEquipmentTable gearEquipment, LootParams lootParams) {
        equip(gearEquipment.lootTable(), lootParams, gearEquipment.slotDropChances());
    }

    public void equip(ResourceKey<LootTable> lootTable, LootParams lootParams, Map<GearEquipmentSlot, Float> dropChances) {
        equip(lootTable, lootParams, 0L, dropChances);
    }

    public void equip(ResourceKey<LootTable> lootTable, LootParams lootParams, long optionalLootTableSeed, Map<GearEquipmentSlot, Float> dropChances) {
        LootTable table = lootParams.getLevel().getServer().reloadableRegistries().getLootTable(lootTable);

        if (table != LootTable.EMPTY) {
            List<ItemStack> possibleGear = table.getRandomItems(lootParams, optionalLootTableSeed);
            List<GearEquipmentSlot> insertedIntoSlots = new ArrayList<>();

            for (ItemStack toEquip : possibleGear) {
                GearEquipmentSlot slot = resolveSlot(toEquip, insertedIntoSlots);

                if (slot != null) {
                    ItemStack equipped = toEquip.split(1);
                    this.gearEquipment.set(slot, equipped);

                    Float dropChance = dropChances.get(slot);

                    if (dropChance != null) {
                        setDropChance(slot, dropChance);
                    }

                    insertedIntoSlots.add(slot);
                }
            }
        }
    }

    public @Nullable GearEquipmentSlot resolveSlot(ItemStack toEquip, List<GearEquipmentSlot> alreadyOccupied) {
        if (toEquip.isEmpty()) return null;

        GearEquippable gearEquippable = toEquip.get(GalacticraftDataComponents.GEAR_EQUIPPABLE);

        if (gearEquippable != null) {
            GearEquipmentSlot gearEquipmentSlot = gearEquippable.gearSlot();

            if (!alreadyOccupied.contains(gearEquipmentSlot)) {
                return gearEquipmentSlot;
            }
        }

        return null;
    }

    @Override
    public float getThermalArmorEffectiveness() {
        return 1.0F;
    }

    @Override
    public boolean mayBreath(LivingEntity livingEntity) {
        return livingEntity.is(EntityTypeTags.UNDEAD) || super.mayBreath(livingEntity);
    }

    public void markGuaranteedDrop(GearEquipmentSlot slot) {
        this.gearDropChances = this.gearDropChances.withGuaranteedDrop(slot);
    }

    public void setDropChance(GearEquipmentSlot slot, float dropChance) {
        this.gearDropChances = this.gearDropChances.withEquipmentChance(slot, dropChance);
    }

    @Override
    public void dropAll(ServerLevel level, @NonNull LivingEntity gearOwner, @Nullable DamageSource cause) {
        for (GearEquipmentSlot slot : GearEquipmentSlot.values()) {
            ItemStack stack = this.gearEquipment.get(slot);
            float dropChance = this.gearDropChances.byGear(slot);

            if (dropChance != 0.0F) {
                boolean preserve = this.gearDropChances.isPreserved(slot);

                if (cause != null && cause.getEntity() instanceof LivingEntity livingCause && livingCause.level() instanceof ServerLevel) {
                    dropChance = EnchantmentHelper.processEquipmentDropChance(level, livingCause, cause, dropChance);
                }

                if (!stack.isEmpty() && !EnchantmentHelper.has(stack, EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP)
                        && ((cause != null && cause.getEntity() instanceof Player) || preserve)
                        &&  level.getRandom().nextFloat() < dropChance) {

                    gearOwner.spawnAtLocation(level, stack);
                    this.gearEquipment.set(slot, ItemStack.EMPTY);
                }
            }
        }
    }

    public boolean shouldSave() {
        return !ResourceHandlerUtil.isEmpty(this.gearEquipment);
    }
}
