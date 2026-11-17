package io.kalishak.galacticraftlegacy.world.entity.ai.behavior;

import io.kalishak.galacticraftlegacy.world.entity.npc.villager.AlienVillager;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import org.apache.commons.lang3.mutable.MutableLong;

import java.util.List;

public class StrollToAlienPoiList {
    public static BehaviorControl<AlienVillager> create(
            MemoryModuleType<List<GlobalPos>> strollToMemoryType,
            float speedModifier,
            int closeEnoughDist,
            int maxDistanceFromPoi,
            MemoryModuleType<GlobalPos> mustBeCloseToMemoryType
    ) {
        MutableLong nextOkStartTime = new MutableLong(0L);
        return BehaviorBuilder.create(
                i -> i.group(i.registered(MemoryModuleType.WALK_TARGET), i.present(strollToMemoryType), i.present(mustBeCloseToMemoryType))
                        .apply(
                                i,
                                (walkTarget, strollToMemory, mustBeCloseToMemory) -> (level, body, timestamp) -> {
                                    List<GlobalPos> strollTo = i.get(strollToMemory);
                                    GlobalPos stayCloseTo = i.get(mustBeCloseToMemory);

                                    if (strollTo.isEmpty()) {
                                        return false;
                                    }

                                    GlobalPos targetPos = strollTo.get(level.getRandom().nextInt(strollTo.size()));
                                    if (targetPos != null && level.dimension() == targetPos.dimension() && stayCloseTo.pos().closerToCenterThan(body.position(), maxDistanceFromPoi)) {
                                        if (timestamp > nextOkStartTime.longValue()) {
                                            walkTarget.set(new WalkTarget(targetPos.pos(), speedModifier, closeEnoughDist));
                                            nextOkStartTime.setValue(timestamp + 100L);
                                        }

                                        return true;
                                    }

                                    return false;
                                }
                        )
        );
    }
}
