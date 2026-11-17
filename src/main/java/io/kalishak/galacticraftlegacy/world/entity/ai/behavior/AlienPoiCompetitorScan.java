package io.kalishak.galacticraftlegacy.world.entity.ai.behavior;

import io.kalishak.galacticraftlegacy.world.entity.npc.villager.AlienVillager;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.villager.VillagerProfession;

import java.util.Optional;

public class AlienPoiCompetitorScan {
    public static BehaviorControl<AlienVillager> create() {
        return BehaviorBuilder.create(
                i -> i.group(i.present(MemoryModuleType.JOB_SITE), i.present(MemoryModuleType.NEAREST_LIVING_ENTITIES))
                        .apply(
                                i,
                                (jobSite, nearestEntities) -> (level, body, _) -> {
                                    GlobalPos pos = i.get(jobSite);
                                    level.getPoiManager()
                                            .getType(pos.pos())
                                            .ifPresent(
                                                    poiType -> i.get(nearestEntities)
                                                            .stream()
                                                            .filter(v -> v instanceof AlienVillager && v != body)
                                                            .map(v -> (AlienVillager) v)
                                                            .filter(LivingEntity::isAlive)
                                                            .filter(nearbyVillager -> competesForSameJobsite(pos, poiType, nearbyVillager))
                                                            .reduce(body, AlienPoiCompetitorScan::selectWinner)
                                            );
                                    return true;
                                }
                        )
        );
    }

    private static AlienVillager selectWinner(AlienVillager first, AlienVillager second) {
        AlienVillager winner;
        AlienVillager loser;

        if (first.getVillagerXp() > second.getVillagerXp()) {
            winner = first;
            loser = second;
        } else {
            winner = second;
            loser = first;
        }

        loser.getBrain().eraseMemory(MemoryModuleType.JOB_SITE);
        return winner;
    }

    private static boolean competesForSameJobsite(GlobalPos pos, Holder<PoiType> poiType, AlienVillager nearbyVillager) {
        Optional<GlobalPos> jobSite = nearbyVillager.getBrain().getMemory(MemoryModuleType.JOB_SITE);
        return jobSite.isPresent() && pos.equals(jobSite.get()) && hasMatchingProfession(poiType, nearbyVillager.getVillagerData().profession());
    }

    private static boolean hasMatchingProfession(Holder<PoiType> poiType, Holder<VillagerProfession> profession) {
        return profession.value().heldJobSite().test(poiType);
    }
}
