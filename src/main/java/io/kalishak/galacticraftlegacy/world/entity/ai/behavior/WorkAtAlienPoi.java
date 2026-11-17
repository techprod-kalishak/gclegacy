package io.kalishak.galacticraftlegacy.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import io.kalishak.galacticraftlegacy.world.entity.npc.villager.AlienVillager;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Optional;

public class WorkAtAlienPoi extends Behavior<AlienVillager> {
    private static final int CHECK_COOLDOWN = 300;
    private static final double DISTANCE = 1.73D;
    private long lastCheck;

    public WorkAtAlienPoi() {
        super(ImmutableMap.of(MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_PRESENT, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, AlienVillager body) {
        if (level.getGameTime() - this.lastCheck < CHECK_COOLDOWN) {
            return false;
        }

        if (level.getRandom().nextInt(2) != 0) {
            return false;
        }

        this.lastCheck = level.getGameTime();
        GlobalPos target = body.getBrain().getMemory(MemoryModuleType.JOB_SITE).get();
        return target.dimension() == level.dimension() && target.pos().closerToCenterThan(body.position(), DISTANCE);
    }

    @Override
    protected void start(ServerLevel level, AlienVillager body, long timestamp) {
        Brain<AlienVillager> brain = body.getBrain();
        brain.setMemory(MemoryModuleType.LAST_WORKED_AT_POI, timestamp);
        brain.getMemory(MemoryModuleType.JOB_SITE).ifPresent(globalPos -> brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(globalPos.pos())));
        body.playWorkSound();
        useWorkstation(level, body);

        if (body.shouldRestock(level)) {
            body.restock();
        }
    }

    protected void useWorkstation(ServerLevel level, AlienVillager body) {
    }

    @Override
    protected boolean canStillUse(ServerLevel level, AlienVillager body, long timestamp) {
        Optional<GlobalPos> jobSiteMemory = body.getBrain().getMemory(MemoryModuleType.JOB_SITE);
        if (jobSiteMemory.isEmpty()) {
            return false;
        }

        GlobalPos target = jobSiteMemory.get();
        return target.dimension() == level.dimension() && target.pos().closerToCenterThan(body.position(), 1.73);
    }
}
