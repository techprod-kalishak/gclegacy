package io.kalishak.galacticraftlegacy.attachment.block;

import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.AbstractMachineBlockEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;

public record SyncedEnergyHandler(int storedEnergy) {
    public static final StreamCodec<ByteBuf, SyncedEnergyHandler> STREAM_CODEC = ByteBufCodecs.INT.map(SyncedEnergyHandler::new, SyncedEnergyHandler::storedEnergy);

    public static SyncedEnergyHandler fromBlockEntity(IAttachmentHolder attachmentHolder) {
        if (!(attachmentHolder instanceof BlockEntity)) {
            throw new IllegalArgumentException(attachmentHolder.getClass() + " is not a BlockEntity");
        }

        if (attachmentHolder instanceof AbstractMachineBlockEntity machine) {
            Level level = machine.getLevel();

            if (level != null) {
                EnergyHandler energyHandler = level.getCapability(Capabilities.Energy.BLOCK, machine.getBlockPos(), null);

                if (energyHandler != null) {
                    return new SyncedEnergyHandler(energyHandler.getAmountAsInt());
                }
            }
        }

        return new SyncedEnergyHandler(0);
    }
}
