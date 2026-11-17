package io.kalishak.galacticraftlegacy.world.entity.ai.behavior;

import io.kalishak.galacticraftlegacy.world.entity.npc.villager.AlienVillager;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.villager.VillagerProfession;

import java.util.Optional;

public class AssignAlienProfessionFromJobSite {
    public static BehaviorControl<AlienVillager> create() {
        return BehaviorBuilder.create(
                i -> i.group(i.present(MemoryModuleType.POTENTIAL_JOB_SITE), i.registered(MemoryModuleType.JOB_SITE))
                        .apply(
                                i,
                                (potentialJobSite, jobSite) -> (level, body, _) -> {
                                    GlobalPos pos = i.get(potentialJobSite);
                                    if (!pos.pos().closerToCenterThan(body.position(), 2.0)) {
                                        return false;
                                    }

                                    potentialJobSite.erase();
                                    jobSite.set(pos);
                                    level.broadcastEntityEvent(body, (byte)14);

                                    if (!body.getVillagerData().profession().is(VillagerProfession.NONE)) {
                                        return true;
                                    }

                                    MinecraftServer server = level.getServer();
                                    Optional.ofNullable(server.getLevel(pos.dimension()))
                                            .flatMap(l -> l.getPoiManager().getType(pos.pos()))
                                            .flatMap(
                                                    poiType -> BuiltInRegistries.VILLAGER_PROFESSION
                                                            .listElements()
                                                            .filter(profession -> profession.value().heldJobSite().test(poiType))
                                                            .findFirst()
                                            )
                                            .ifPresent(profession -> {
                                                body.setVillagerData(body.getVillagerData().withProfession(profession));
                                                body.refreshBrain(level);
                                            });
                                    return true;
                                }
                        )
        );
    }
}
