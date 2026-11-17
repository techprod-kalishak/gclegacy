package io.kalishak.galacticraftlegacy.world.entity.ai.sensing;

import io.kalishak.galacticraftlegacy.data.datamap.GalacticraftDataMaps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.NearestVisibleLivingEntitySensor;
import net.neoforged.neoforge.registries.datamaps.builtin.AcceptableVillagerDistance;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AlienVillagerHostilesSensor extends NearestVisibleLivingEntitySensor {
    @Override
    protected boolean isMatchingEntity(ServerLevel level, LivingEntity body, LivingEntity mob) {
        return isHostile(mob) && isClose(body, mob);
    }

    @Override
    protected MemoryModuleType<LivingEntity> getMemoryToSet() {
        return MemoryModuleType.NEAREST_HOSTILE;
    }

    private boolean isClose(LivingEntity body, LivingEntity mob) {
        AcceptableVillagerDistance acceptableDistanceFromHostile = mob.getData(GalacticraftDataMaps.ACCEPTABLE_ALIEN_VILLAGER_DISTANCES);
        float distThreshold = acceptableDistanceFromHostile != null ? acceptableDistanceFromHostile.distance() : 0.0F;
        return mob.distanceToSqr(body) <= distThreshold * distThreshold;
    }

    private boolean isHostile(LivingEntity entity) {
        return entity.getData(GalacticraftDataMaps.ACCEPTABLE_ALIEN_VILLAGER_DISTANCES) != null;
    }
}
