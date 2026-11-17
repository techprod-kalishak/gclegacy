package io.kalishak.galacticraftlegacy.world.entity.ai.behavior;

import io.kalishak.galacticraftlegacy.world.entity.npc.villager.AlienVillager;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.villager.VillagerData;
import net.minecraft.world.entity.npc.villager.VillagerProfession;

public class ResetAlienProfession {
    public static BehaviorControl<AlienVillager> create() {
        return BehaviorBuilder.create(i -> i.group(i.absent(MemoryModuleType.JOB_SITE)).apply(i, _ -> (level, body, _) -> {
            VillagerData bodyData = body.getVillagerData();
            boolean canBeFired = !bodyData.profession().is(VillagerProfession.NONE) && !bodyData.profession().is(VillagerProfession.NITWIT);

            if (canBeFired && body.getVillagerXp() == 0 && bodyData.level() <= 1) {
                body.setVillagerData(body.getVillagerData().withProfession(level.registryAccess(), VillagerProfession.NONE));
                body.refreshBrain(level);
                return true;
            }

            return false;
        }));
    }
}
