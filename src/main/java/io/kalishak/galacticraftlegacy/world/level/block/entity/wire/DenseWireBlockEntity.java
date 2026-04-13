package io.kalishak.galacticraftlegacy.world.level.block.entity.wire;

import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class DenseWireBlockEntity extends WireBlockEntity {
    public DenseWireBlockEntity(BlockPos pos, BlockState blockState) {
        super(GalacticraftBlockEntityType.DENSE_WIRE.get(), pos, blockState);
    }

    @Override
    protected int getCapacity() {
        return 10000;
    }

    @Override
    protected int getMaxTransfer() {
        return 250;
    }
}
