package io.kalishak.galacticraftlegacy.world.entity.ai.behavior;

import io.kalishak.galacticraftlegacy.world.entity.npc.villager.AlienVillager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.function.Predicate;

public class ValidateNearbyAlienPoi {
    private static final int MAX_DISTANCE = 16;

    public static BehaviorControl<LivingEntity> create(Predicate<Holder<PoiType>> poiType, MemoryModuleType<GlobalPos> memoryType) {
        return BehaviorBuilder.create(i -> i.group(i.present(memoryType)).apply(i, memory -> (level, body, _) -> {
            GlobalPos globalPos = i.get(memory);
            BlockPos pos = globalPos.pos();

            if (level.dimension() == globalPos.dimension() && pos.closerToCenterThan(body.position(), 16.0)) {
                ServerLevel poiLevel = level.getServer().getLevel(globalPos.dimension());
                if (poiLevel == null || !poiLevel.getPoiManager().exists(pos, poiType)) {
                    memory.erase();
                } else if (bedIsOccupied(poiLevel, pos, body)) {
                    memory.erase();
                    if (!bedIsOccupiedByVillager(poiLevel, pos)) {
                        level.getPoiManager().release(pos);
                        level.debugSynchronizers().updatePoi(pos);
                    }
                }

                return true;
            }

            return false;
        }));
    }

    private static boolean bedIsOccupied(ServerLevel poiLevel, BlockPos poiPos, LivingEntity body) {
        BlockState blockState = poiLevel.getBlockState(poiPos);

        return blockState.is(BlockTags.VILLAGERS_CAN_SLEEP_ON_BED) && blockState.getValue(BedBlock.OCCUPIED) && !body.isSleeping();
    }

    private static boolean bedIsOccupiedByVillager(ServerLevel poiLevel, BlockPos poiPos) {
        List<AlienVillager> villagers = poiLevel.getEntitiesOfClass(AlienVillager.class, new AABB(poiPos), LivingEntity::isSleeping);
        return !villagers.isEmpty();
    }
}
