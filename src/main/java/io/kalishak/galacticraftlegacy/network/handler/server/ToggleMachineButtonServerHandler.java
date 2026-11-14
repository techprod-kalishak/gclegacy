package io.kalishak.galacticraftlegacy.network.handler.server;

import io.kalishak.galacticraftlegacy.network.payload.ToggleMachineButtonPayload;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.ToggledMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ToggleMachineButtonServerHandler {
    public static void handle(ToggleMachineButtonPayload payload, IPayloadContext cxt) {
        cxt.enqueueWork(() -> {
            BlockPos blockPos = payload.pos();
            Level level = cxt.player().level();
            BlockEntity blockEntity = level.getBlockEntity(blockPos);

            if (blockEntity instanceof ToggledMachine machine) {
                machine.toggleGeneration();
                machine.onToggle(level, blockEntity);
            } else {
                throw new IllegalStateException("Unexpected block entity: " + blockEntity);
            }

        }).exceptionally(e -> GalacticraftComponents.networkFailureMessage(cxt::disconnect, e));
    }
}
