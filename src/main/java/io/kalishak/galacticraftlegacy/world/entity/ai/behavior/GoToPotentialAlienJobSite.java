package io.kalishak.galacticraftlegacy.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import io.kalishak.galacticraftlegacy.world.entity.npc.villager.AlienVillager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.schedule.Activity;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
public class GoToPotentialAlienJobSite extends Behavior<AlienVillager> {
    private static final int TICKS_UNTIL_TIMEOUT = 1200;
    private final float speedModifier;

    public GoToPotentialAlienJobSite(float speedModifier) {
        super(ImmutableMap.of(MemoryModuleType.POTENTIAL_JOB_SITE, MemoryStatus.VALUE_PRESENT), TICKS_UNTIL_TIMEOUT);
        this.speedModifier = speedModifier;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, AlienVillager body) {
        return body.getBrain()
                .getActiveNonCoreActivity()
                .map(activity -> activity == Activity.IDLE || activity == Activity.WORK || activity == Activity.PLAY)
                .orElse(true);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, AlienVillager body, long timestamp) {
        return body.getBrain().hasMemoryValue(MemoryModuleType.POTENTIAL_JOB_SITE);
    }

    @Override
    protected void tick(ServerLevel level, AlienVillager body, long timestamp) {
        BehaviorUtils.setWalkAndLookTargetMemories(body, body.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE).get().pos(), this.speedModifier, 1);
    }

    @Override
    protected void stop(ServerLevel level, AlienVillager body, long timestamp) {
        Optional<GlobalPos> potentialJobSitePos = body.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE);

        potentialJobSitePos.ifPresent(globalPos -> {
            BlockPos pos = globalPos.pos();
            ServerLevel serverLevel = level.getServer().getLevel(globalPos.dimension());

            if (serverLevel != null) {
                PoiManager manager = serverLevel.getPoiManager();

                if (manager.exists(pos, p -> true)) {
                    manager.release(pos);
                }

                level.debugSynchronizers().updatePoi(pos);
            }
        });
        body.getBrain().eraseMemory(MemoryModuleType.POTENTIAL_JOB_SITE);
    }
}
