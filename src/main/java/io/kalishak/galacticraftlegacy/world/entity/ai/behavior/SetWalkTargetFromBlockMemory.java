package io.kalishak.galacticraftlegacy.world.entity.ai.behavior;

import io.kalishak.galacticraftlegacy.world.entity.npc.villager.AlienVillager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.ai.behavior.OneShot;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class SetWalkTargetFromBlockMemory {
    public static OneShot<AlienVillager> create(
            MemoryModuleType<GlobalPos> memoryType, float speedModifier, int closeEnoughDist, int tooFarDistance, int tooLongUnreachableDuration
    ) {
        return BehaviorBuilder.create(
                i -> i.group(i.registered(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE), i.absent(MemoryModuleType.WALK_TARGET), i.present(memoryType))
                        .apply(
                                i,
                                (cantReachSince, walkTarget, memory) -> (level, body, timestamp) -> {
                                    GlobalPos targetPos = i.get(memory);
                                    Optional<Long> cantReachTargetSince = i.tryGet(cantReachSince);

                                    if (targetPos.dimension() == level.dimension() && (cantReachTargetSince.isEmpty() || level.getGameTime() - cantReachTargetSince.get() <= tooLongUnreachableDuration)) {
                                        if (targetPos.pos().distManhattan(body.blockPosition()) > tooFarDistance) {
                                            Vec3 towardsTargetPos = null;
                                            int tries = 0;
                                            int MAX_TRIES = 1000;

                                            while (towardsTargetPos == null || BlockPos.containing(towardsTargetPos).distManhattan(body.blockPosition()) > tooFarDistance) {
                                                towardsTargetPos = DefaultRandomPos.getPosTowards(
                                                        body, 15, 7, Vec3.atBottomCenterOf(targetPos.pos()), (float) (Math.PI / 2)
                                                );
                                                if (++tries == MAX_TRIES) {
                                                    body.releasePoi(memoryType);
                                                    memory.erase();
                                                    cantReachSince.set(timestamp);

                                                    return true;
                                                }
                                            }

                                            walkTarget.set(new WalkTarget(towardsTargetPos, speedModifier, closeEnoughDist));
                                        } else if (targetPos.pos().distManhattan(body.blockPosition()) > closeEnoughDist) {
                                            walkTarget.set(new WalkTarget(targetPos.pos(), speedModifier, closeEnoughDist));
                                        }
                                    } else {
                                        body.releasePoi(memoryType);
                                        memory.erase();
                                        cantReachSince.set(timestamp);
                                    }

                                    return true;
                                }
                        )
        );
    }
}
