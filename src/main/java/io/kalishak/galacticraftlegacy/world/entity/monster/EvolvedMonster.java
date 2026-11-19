/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.entity.monster;

import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.attachment.entity.EntityGearInventory;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.transfer.entity.SpaceGearEquipment;
import io.kalishak.galacticraftlegacy.world.entity.GearDropChances;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import io.kalishak.galacticraftlegacy.world.entity.SpaceEntity;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;

import static net.minecraft.world.entity.monster.Monster.isDarkEnoughToSpawn;

public interface EvolvedMonster extends SpaceEntity {
    static void populateDefault(Entity entity, SpaceGearEquipment empty) {
        empty.set(GearEquipmentSlot.MASK, GalacticraftItems.OXYGEN_MASK.toStack());
        empty.set(GearEquipmentSlot.GEAR, GalacticraftItems.OXYGEN_GEAR.toStack());
        empty.set(GearEquipmentSlot.TANK, GalacticraftItems.HEAVY_TANK.toStack());
        empty.set(GearEquipmentSlot.ADDITIONAL_TANK, GalacticraftItems.HEAVY_TANK.toStack());
        entity.setData(GalacticraftAttachments.ENTITY_GEAR_INVENTORY, new EntityGearInventory(empty, GearDropChances.DEFAULT));
    }

    static <E extends Entity & EvolvedMonster> boolean checkEvolvedSpawnRules(EntityType<? extends Mob> type, ServerLevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        Holder<Biome> biome = level.getBiome(pos);
        boolean isSpace = biome.is(GalacticraftTags.Biomes.IS_OPEN_SPACE) && level.getDifficulty() != Difficulty.PEACEFUL;

        if (!isSpace) {
            return false;
        }

        return (EntitySpawnReason.ignoresLightRequirements(spawnReason) || isDarkEnoughToSpawn(level, pos, random))
                && Monster.checkMobSpawnRules(type, level, spawnReason, pos, random);
    }

    static void writeGear(Entity entity, SpaceGearEquipment gear) {
        entity.setData(GalacticraftAttachments.ENTITY_GEAR_INVENTORY, new EntityGearInventory(gear, GearDropChances.DEFAULT));
    }

    static void readGear(Entity entity, SpaceGearEquipment gear) {
        gear.setAll(entity.getData(GalacticraftAttachments.ENTITY_GEAR_INVENTORY).getGearEquipment());
    }
}
