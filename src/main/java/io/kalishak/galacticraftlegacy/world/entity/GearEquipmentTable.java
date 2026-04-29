package io.kalishak.galacticraftlegacy.world.entity;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public record GearEquipmentTable(ResourceKey<LootTable> lootTable, Map<GearEquipmentSlot, Float> slotDropChances) {
    public static final Codec<Map<GearEquipmentSlot, Float>> DROP_CHANCES_CODEC = Codec.either(Codec.FLOAT, Codec.unboundedMap(GearEquipmentSlot.CODEC, Codec.FLOAT))
            .xmap(
                    either -> either.map(GearEquipmentTable::create, Function.identity()),
                    provider -> {
                        boolean dropChancesTheSame = provider.values().stream().distinct().count() == 1L;
                        boolean allSlotsArePresent = provider.keySet().containsAll(List.of(GearEquipmentSlot.values()));
                        return dropChancesTheSame && allSlotsArePresent
                                ? Either.left(provider.values().stream().findFirst().orElse(0.0F))
                                : Either.right(provider);
                    }
            );
    public static final Codec<GearEquipmentTable> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            LootTable.KEY_CODEC.fieldOf("loot_table").forGetter(GearEquipmentTable::lootTable),
            DROP_CHANCES_CODEC.optionalFieldOf("slot_drop_chances", Map.of()).forGetter(GearEquipmentTable::slotDropChances)
    ).apply(instance, GearEquipmentTable::new));

    public GearEquipmentTable(ResourceKey<LootTable> lootTable, float dropChance) {
        this(lootTable, create(dropChance));
    }

    private static Map<GearEquipmentSlot, Float> create(float dropChance) {
        return create(List.of(GearEquipmentSlot.values()), dropChance);
    }

    private static Map<GearEquipmentSlot, Float> create(List<GearEquipmentSlot> slots, float dropChance) {
        Map<GearEquipmentSlot, Float> values = Maps.newHashMap();

        for (GearEquipmentSlot slot : slots) {
            values.put(slot, dropChance);
        }

        return values;
    }
}
