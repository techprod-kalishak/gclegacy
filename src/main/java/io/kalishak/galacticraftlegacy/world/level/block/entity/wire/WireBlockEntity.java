/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.wire;

import io.kalishak.galacticraftlegacy.transfer.node.FluidNodeNetwork;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler;

public class WireBlockEntity extends AbstractConnectableBlockEntity {
    final SimpleEnergyHandler energyHandler = new SimpleEnergyHandler(getCapacity(), getMaxTransfer());

    public WireBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public static <E extends WireBlockEntity> void registerCapabilities(RegisterCapabilitiesEvent event, BlockEntityType<E> blockEntityType) {
        event.registerBlockEntity(
                Capabilities.Energy.BLOCK,
                blockEntityType,
                (blockEntity, context) -> blockEntity.energyHandler
        );
    }

    public static void serverTick(ServerLevel level, BlockPos blockPos, BlockState blockState, WireBlockEntity wireBlockEntity) {

    }

    protected int getCapacity() {
        return 2500;
    }

    protected int getMaxTransfer() {
        return 100;
    }

    @Override
    protected void resetNetwork() {

    }

    public WireBlockEntity(BlockPos pos, BlockState blockState) {
        super(GalacticraftBlockEntityType.WIRE.get(), pos, blockState);
    }

    @Override
    public void addNetwork(FluidNodeNetwork network) {
        this.network = network;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        this.energyHandler.serialize(output);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.energyHandler.deserialize(input);
    }
}
