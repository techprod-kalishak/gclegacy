package io.kalishak.galacticraftlegacy.world.level.block.entity.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.Set;

public class OxygenDistributorBlockEntity extends BlockEntity {
    public static Set<GlobalPos> loadedBlocks = new HashSet<>();

    public OxygenDistributorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public boolean inBubble(double x, double y, double z) {
        return false;
    }
}
