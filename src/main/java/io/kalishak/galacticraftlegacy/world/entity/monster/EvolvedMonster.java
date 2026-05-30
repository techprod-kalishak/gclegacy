/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.entity.monster;

import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.transfer.entity.SpaceGearEquipment;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import io.kalishak.galacticraftlegacy.world.entity.SpaceEntity;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.level.biome.GalacticraftBiomes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.zombie.Drowned;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import org.jspecify.annotations.NonNull;

import static net.minecraft.world.entity.monster.Monster.isDarkEnoughToSpawn;

public interface EvolvedMonster extends SpaceEntity {
    static void populateDefault(SpaceGearEquipment empty) {
        empty.set(GearEquipmentSlot.MASK, GalacticraftItems.OXYGEN_MASK.toStack());
        empty.set(GearEquipmentSlot.GEAR, GalacticraftItems.OXYGEN_GEAR.toStack());
        empty.set(GearEquipmentSlot.TANK, GalacticraftItems.HEAVY_TANK.toStack());
        empty.set(GearEquipmentSlot.ADDITIONAL_TANK, GalacticraftItems.HEAVY_TANK.toStack());
    }

    static <E extends Entity & EvolvedMonster> boolean checkEvolvedSpawnRules(EntityType<@NonNull E> type, ServerLevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random, int bound) {
        Holder<Biome> biome = level.getBiome(pos);
        boolean canMonsterSpawn = level.getDifficulty() != Difficulty.PEACEFUL
                && biome.is(GalacticraftTags.Biomes.IS_MOON)
                && (EntitySpawnReason.ignoresLightRequirements(spawnReason) || isDarkEnoughToSpawn(level, pos, random));
        if (!canMonsterSpawn || !EntitySpawnReason.isSpawner(spawnReason) && spawnReason != EntitySpawnReason.REINFORCEMENT) {
            return random.nextInt(bound) == 0 && canMonsterSpawn;
        }

        return true;
    }
}
