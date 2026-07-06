/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.machine;

import io.kalishak.galacticraftlegacy.world.inventory.machine.OxygenCollectorMenu;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import io.kalishak.galacticraftlegacy.world.level.material.fluid.GalacticraftFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class OxygenCollectorBlockEntity extends AbstractOxygenBlockEntity {
    public static float OXYGEN_PER_PLANT = 0.75F;
    public int lastOxygenCollected;
    private boolean noAtmosphericOxygen;
    private boolean isInitialised;
    private boolean producedLastTick;

    public OxygenCollectorBlockEntity(BlockPos pos, BlockState blockState) {
        super(GalacticraftBlockEntityType.OXYGEN_COLLECTOR.get(), pos, blockState, 100, FluidType.BUCKET_VOLUME * 6);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        registerItemCapability(GalacticraftBlockEntityType.OXYGEN_COLLECTOR.get(), event);
        registerEnergyCapability(GalacticraftBlockEntityType.OXYGEN_COLLECTOR.get(), event);
        registerFluidCapability(GalacticraftBlockEntityType.OXYGEN_COLLECTOR.get(), event);
    }

    public static void serverTick(ServerLevel level, BlockPos worldPosition, BlockState blockState, OxygenCollectorBlockEntity blockEntity) {
        extractBattery(blockEntity, false, 25, null);
        produce(level, worldPosition, blockEntity, Direction.EAST, null);

        blockEntity.producedLastTick = blockEntity.oxygenHandler.getAmount() < blockEntity.oxygenHandler.getCapacity();

        if (blockEntity.producedLastTick && level.getRandom().nextInt(10) == 0) {
            if (blockEntity.hasEnergyToOperate()) {
                float nearbyLeaves = 0;

                if (!blockEntity.isInitialised) {
                    blockEntity.noAtmosphericOxygen = !level.dimension().equals(ServerLevel.OVERWORLD);
                    blockEntity.isInitialised = true;
                }

                if (blockEntity.noAtmosphericOxygen) {
                    int minY = worldPosition.getY() - 5;
                    int maxY = worldPosition.getY() + 5;

                    if (minY < 0) {
                        minY = 0;
                    }

                    if (maxY >= level.getHeight()) {
                        maxY = level.getHeight() - 1;
                    }

                    for (int x = worldPosition.getX() - 5; x <= worldPosition.getX() + 5; x++) {
                        for (int z = worldPosition.getZ() - 5; z <= worldPosition.getZ() + 5; z++) {
                            for (int y = minY; y <= maxY; y++) {
                                BlockPos pos = new BlockPos(x, y, z);
                                BlockState state = level.getBlockState(pos);

                                if (state.is(BlockTags.LEAVES) || state.is(BlockTags.CROPS)) {
                                    nearbyLeaves += OXYGEN_PER_PLANT;
                                }
                            }
                        }
                    }
                } else {
                    nearbyLeaves = 9.3F * 10.0F;
                }

                blockEntity.lastOxygenCollected = (int) Math.floor(nearbyLeaves / 10.0F);

                try (Transaction transaction = Transaction.open(null)) {
                    if (blockEntity.oxygenHandler.insert(FluidResource.of(GalacticraftFluids.OXYGEN), blockEntity.lastOxygenCollected, transaction) > 0) {
                        if (blockEntity.capacitor.extract(300, transaction) > 0) {
                            transaction.commit();
                        }
                    }
                }

            } else {
                blockEntity.lastOxygenCollected = 0;
            }
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new OxygenCollectorMenu(containerId, inventory, this);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putBoolean("NoAtmosphericOxygen", this.noAtmosphericOxygen);
        output.putBoolean("IsInitialised", this.isInitialised);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.noAtmosphericOxygen = input.getBooleanOr("NoAtmosphericOxygen", false);
        this.isInitialised = input.getBooleanOr("IsInitialised", false);
    }

    @Override
    public boolean isOxygenConsumer() {
        return false;
    }

    @Override
    public int getItemsSize() {
        return 1;
    }

    @Override
    protected int getBatterySlotIndex() {
        return 0;
    }

    @Override
    public int getTanks() {
        return 1;
    }
}
