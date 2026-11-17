package io.kalishak.galacticraftlegacy.world.entity.ai.sensing;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import io.kalishak.galacticraftlegacy.world.entity.GalacticraftEntityTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.Sensor;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Set;

@ParametersAreNonnullByDefault
public class AlienVillagerBabiesSensor extends Sensor<LivingEntity> {
    @Override
    protected void doTick(ServerLevel level, LivingEntity body) {
        body.getBrain().setMemory(MemoryModuleType.VISIBLE_VILLAGER_BABIES, getNearestAlienVillagerBabies(body));
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.VISIBLE_VILLAGER_BABIES);
    }

    private List<LivingEntity> getNearestAlienVillagerBabies(LivingEntity myBody) {
        return ImmutableList.copyOf(getVisibleEntities(myBody).findAll(this::isAlienVillagerBaby));
    }

    private boolean isAlienVillagerBaby(LivingEntity entity) {
        return entity.is(GalacticraftEntityTypes.ALIEN_VILLAGER) && entity.isBaby();
    }

    private NearestVisibleLivingEntities getVisibleEntities(LivingEntity myBody) {
        return myBody.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty());
    }
}
