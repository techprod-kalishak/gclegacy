package io.kalishak.galacticraftlegacy.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import io.kalishak.galacticraftlegacy.world.entity.npc.villager.AlienVillager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;

public class AlienVillagerPanicTrigger extends Behavior<AlienVillager> {
    public AlienVillagerPanicTrigger() {
        super(ImmutableMap.of());
    }

    @Override
    protected boolean canStillUse(ServerLevel level, AlienVillager body, long timestamp) {
        return isHurt(body) || hasHostile(body);
    }

    @Override
    protected void start(ServerLevel level, AlienVillager body, long timestamp) {
        if (isHurt(body) || hasHostile(body)) {
            Brain<?> brain = body.getBrain();
            if (!brain.isActive(Activity.PANIC)) {
                brain.eraseMemory(MemoryModuleType.PATH);
                brain.eraseMemory(MemoryModuleType.WALK_TARGET);
                brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
                brain.eraseMemory(MemoryModuleType.BREED_TARGET);
                brain.eraseMemory(MemoryModuleType.INTERACTION_TARGET);
            }

            brain.setActiveActivityIfPossible(Activity.PANIC);
        }
    }

    public static boolean hasHostile(LivingEntity myBody) {
        return myBody.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_HOSTILE);
    }

    public static boolean isHurt(LivingEntity myBody) {
        return myBody.getBrain().hasMemoryValue(MemoryModuleType.HURT_BY);
    }
}
