package io.kalishak.galacticraftlegacy.world.level.block.entity.spawner;

import io.kalishak.galacticraftlegacy.world.entity.GalacticraftEntityTypes;
import io.kalishak.galacticraftlegacy.world.entity.monster.boss.SkeletonBoss;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class SkeletonBossSpawnerBlockEntity extends SpaceBossSpawnerBlockEntity<SkeletonBoss> {
    public SkeletonBossSpawnerBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(GalacticraftBlockEntityTypes.SKELETON_BOSS_SPAWNER.get(), GalacticraftEntityTypes.SKELETON_BOSS, worldPosition, blockState);
    }
}
