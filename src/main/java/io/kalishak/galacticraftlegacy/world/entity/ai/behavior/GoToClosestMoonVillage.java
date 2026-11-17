package io.kalishak.galacticraftlegacy.world.entity.ai.behavior;

import io.kalishak.galacticraftlegacy.world.entity.npc.villager.AlienVillager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.phys.Vec3;

//TODO don't forget about me
public class GoToClosestMoonVillage {
    public static BehaviorControl<AlienVillager> create(float speedModifier, int closeEnoughDistance) {
        return BehaviorBuilder.create(i -> i.group(i.absent(MemoryModuleType.WALK_TARGET)).apply(i, walkTarget -> (level, body, _) -> {
            PoiManager poiManager = level.getPoiManager();

            int sectionsToVillage = poiManager.sectionsToVillage(SectionPos.of(body.blockPosition()));
            Vec3 targetPos = null;

            for (int j = 0; j < 5; j++) {
                Vec3 landPos = LandRandomPos.getPos(body, 15, 7, p -> -poiManager.sectionsToVillage(SectionPos.of(p)));

                if (landPos != null) {
                    int landPosSectionsToVillage = poiManager.sectionsToVillage(SectionPos.of(BlockPos.containing(landPos)));

                    if (landPosSectionsToVillage < sectionsToVillage) {
                        targetPos = landPos;
                        break;
                    }

                    if (landPosSectionsToVillage == sectionsToVillage) {
                        targetPos = landPos;
                    }
                }
            }

            if (targetPos != null) {
                walkTarget.set(new WalkTarget(targetPos, speedModifier, closeEnoughDistance));
            }

            return true;
        }));
    }
}
