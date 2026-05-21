package io.kalishak.galacticraftlegacy.transfer.capability.energy;

import io.kalishak.galacticraftlegacy.transfer.ResourcefulHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;

public record SimpleEnergyPacket(int request) implements EnergyRequest {
    @Override
    public void cancelRequest(boolean committed) {
    }

    @Override
    public boolean requestMatches(int amount) {
        return this.request == amount;
    }

    @Override
    public EnergyHandler recreateFromSource(Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);

        return blockEntity == null ? null : ResourcefulHelper.getEnergyHandler(blockEntity, null);
    }
}
