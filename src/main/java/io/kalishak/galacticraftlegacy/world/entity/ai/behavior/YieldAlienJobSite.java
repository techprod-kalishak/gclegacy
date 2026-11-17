package io.kalishak.galacticraftlegacy.world.entity.ai.behavior;

import io.kalishak.galacticraftlegacy.world.entity.npc.villager.AlienVillager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.level.pathfinder.Path;

import java.util.Optional;

public class YieldAlienJobSite {
    public static BehaviorControl<AlienVillager> create(float speedModifier) {
        return BehaviorBuilder.create(
                i -> i.group(
                                i.present(MemoryModuleType.POTENTIAL_JOB_SITE),
                                i.absent(MemoryModuleType.JOB_SITE),
                                i.present(MemoryModuleType.NEAREST_LIVING_ENTITIES),
                                i.registered(MemoryModuleType.WALK_TARGET),
                                i.registered(MemoryModuleType.LOOK_TARGET)
                        )
                        .apply(
                                i,
                                (potentialJob, _, nearestEntities, walkTarget, lookTarget) -> (level, body, _) -> {
                                    if (body.isBaby()) {
                                        return false;
                                    }

                                    if (!body.getVillagerData().profession().is(VillagerProfession.NONE)) {
                                        return false;
                                    }

                                    BlockPos poiPos = i.get(potentialJob).pos();
                                    Optional<Holder<PoiType>> poiType = level.getPoiManager().getType(poiPos);
                                    if (poiType.isEmpty()) {
                                        return true;
                                    }

                                    i.get(nearestEntities)
                                            .stream()
                                            .filter(v -> v instanceof AlienVillager && v != body)
                                            .map(v -> (AlienVillager)v)
                                            .filter(LivingEntity::isAlive)
                                            .filter(v -> nearbyWantsJobsite(poiType.get(), v, poiPos))
                                            .findFirst()
                                            .ifPresent(nearbyVillager -> {
                                                walkTarget.erase();
                                                lookTarget.erase();
                                                potentialJob.erase();
                                                if (nearbyVillager.getBrain().getMemory(MemoryModuleType.JOB_SITE).isEmpty()) {
                                                    BehaviorUtils.setWalkAndLookTargetMemories(nearbyVillager, poiPos, speedModifier, 1);
                                                    nearbyVillager.getBrain().setMemory(MemoryModuleType.POTENTIAL_JOB_SITE, GlobalPos.of(level.dimension(), poiPos));
                                                    level.debugSynchronizers().updatePoi(poiPos);
                                                }
                                            });
                                    return true;
                                }
                        )
        );
    }

    private static boolean nearbyWantsJobsite(Holder<PoiType> type, AlienVillager nearbyVillager, BlockPos poiPos) {
        boolean nearbyHasPotentialJobSite = nearbyVillager.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE).isPresent();

        if (nearbyHasPotentialJobSite) {
            return false;
        }

        Optional<GlobalPos> nearbyVillagerJobSiteMemory = nearbyVillager.getBrain().getMemory(MemoryModuleType.JOB_SITE);
        Holder<VillagerProfession> nearbyProfession = nearbyVillager.getVillagerData().profession();

        if (nearbyProfession.value().heldJobSite().test(type)) {
            return nearbyVillagerJobSiteMemory.map(globalPos -> globalPos.pos().equals(poiPos)).orElseGet(() -> canReachPos(nearbyVillager, poiPos, type.value()));
        }

        return false;
    }

    private static boolean canReachPos(PathfinderMob nearbyVillager, BlockPos poiPos, PoiType type) {
        Path path = nearbyVillager.getNavigation().createPath(poiPos, type.validRange());

        return path != null && path.canReach();
    }
}
