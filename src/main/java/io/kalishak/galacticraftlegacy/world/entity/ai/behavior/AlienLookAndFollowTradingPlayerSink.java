package io.kalishak.galacticraftlegacy.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import io.kalishak.galacticraftlegacy.world.entity.npc.villager.AlienVillager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.player.Player;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AlienLookAndFollowTradingPlayerSink extends Behavior<AlienVillager> {
    private final float speedModifier;

    public AlienLookAndFollowTradingPlayerSink(float speedModifier) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED), Integer.MAX_VALUE);
        this.speedModifier = speedModifier;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, AlienVillager body) {
        Player tradingPlayer = body.getTradingPlayer();
        return body.isAlive() && tradingPlayer != null && !body.isInWater() && !body.wasHurtRecently() && body.distanceToSqr(tradingPlayer) <= 16.0;
    }

    @Override
    protected boolean canStillUse(ServerLevel level, AlienVillager body, long timestamp) {
        return checkExtraStartConditions(level, body);
    }

    @Override
    protected void start(ServerLevel level, AlienVillager body, long timestamp) {
        this.followPlayer(body);
    }

    @Override
    protected void stop(ServerLevel level, AlienVillager body, long timestamp) {
        Brain<?> brain = body.getBrain();
        brain.eraseMemory(MemoryModuleType.WALK_TARGET);
        brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
    }

    @Override
    protected void tick(ServerLevel level, AlienVillager body, long timestamp) {
        this.followPlayer(body);
    }

    @Override
    protected boolean timedOut(long timestamp) {
        return false;
    }

    private void followPlayer(AlienVillager body) {
        Brain<?> brain = body.getBrain();
        brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new EntityTracker(body.getTradingPlayer(), false), this.speedModifier, 2));
        brain.setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(body.getTradingPlayer(), true));
    }
}
