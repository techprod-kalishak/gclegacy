/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.machine;

import io.kalishak.galacticraftlegacy.world.inventory.machine.CoalGeneratorMenu;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import io.kalishak.galacticraftlegacy.world.level.block.machine.AbstractMachineBlock;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.transfer.EmptyResourceHandler;
import net.neoforged.neoforge.transfer.RangedResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

public class CoalGeneratorBlockEntity extends AbstractMachineBlockEntity {
    public static final int MIN_ENERGY_PER_HEAT = 30;
    public static final int MAX_ENERGY_PER_HEAT = 150;
    public static final float HEAT_UP_SPEED = 0.3F;
    int litTimeRemaining;
    int litTotalTime;
    float heatLevel;
    private final DataSlot heatData = new DataSlot() {

        @Override
        public int get() {
            return Mth.floor(CoalGeneratorBlockEntity.this.heatLevel);
        }

        @Override
        public void set(int value) {
            CoalGeneratorBlockEntity.this.heatLevel = value;
        }
    };

    public CoalGeneratorBlockEntity(BlockPos pos, BlockState blockState) {
        super(GalacticraftBlockEntityType.COAL_GENERATOR.get(), pos, blockState);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        registerSingleEnergyInputEnergyHandler(Direction.EAST, GalacticraftBlockEntityType.COAL_GENERATOR.get(), event);
        event.registerBlockEntity(
                Capabilities.Item.BLOCK,
                GalacticraftBlockEntityType.COAL_GENERATOR.get(),
                (machine, context) -> {
                    if (context == Direction.UP) {
                        return RangedResourceHandler.ofSingleIndex(() -> machine.items, 0);
                    } else if (context == null) {
                        return machine.items;
                    }

                    return EmptyResourceHandler.instance();
                }
        );
    }

    public static void serverTick(ServerLevel level, BlockPos pos, BlockState state, CoalGeneratorBlockEntity coalGenerator) {
        boolean isLit = coalGenerator.isLit();
        boolean hadChanged = false;

        if (coalGenerator.heatLevel - MIN_ENERGY_PER_HEAT > 0) {
            try (Transaction tx = Transaction.open(null)) {
                if (coalGenerator.capacitor.insert(Mth.floor(coalGenerator.heatLevel) - MIN_ENERGY_PER_HEAT, tx) > 0) {
                    tx.commit();
                }
            }
        }

        if (coalGenerator.litTimeRemaining > 0) {
            coalGenerator.litTimeRemaining--;
            coalGenerator.heatLevel = Math.min(coalGenerator.heatLevel + Math.max(coalGenerator.heatLevel * 0.005F, HEAT_UP_SPEED), MAX_ENERGY_PER_HEAT);
        }

        ItemResource fuel = coalGenerator.items.getResource(0);

        if (coalGenerator.litTimeRemaining <= 0 && !fuel.isEmpty()) {
            try (Transaction tx = Transaction.open(null)) {
                FurnaceFuel furnaceFuel = level.registryAccess().lookupOrThrow(Registries.ITEM).getData(NeoForgeDataMaps.FURNACE_FUELS, fuel.typeHolder().unwrapKey().orElseThrow());
                ItemStackTemplate remainder = fuel.toStack().getCraftingRemainder();

                if (coalGenerator.items.extract(fuel, 1, tx) > 0) {
                    if (furnaceFuel != null && furnaceFuel.burnTime() > 0) {
                        if (remainder != null) {
                            coalGenerator.items.set(0, ItemResource.of(remainder), remainder.count());
                        }

                        coalGenerator.litTotalTime =  furnaceFuel.burnTime();
                        coalGenerator.litTimeRemaining = coalGenerator.litTotalTime;

                        tx.commit();
                    }
                }
            }
        }

        if (!coalGenerator.isLit() && coalGenerator.heatLevel > 0) {
            coalGenerator.heatLevel = Mth.clamp(coalGenerator.heatLevel - HEAT_UP_SPEED, 0, coalGenerator.heatLevel);
        }

        if (isLit != coalGenerator.isLit()) {
            hadChanged = true;
            state = state.setValue(AbstractMachineBlock.LIT, coalGenerator.isLit());
            level.setBlock(pos, state, AbstractMachineBlock.UPDATE_ALL);
        }

        if (hadChanged) {
            BlockEntity.setChanged(level, pos, state);
        }
    }

    public boolean isLit() {
        return this.litTimeRemaining > 0;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new CoalGeneratorMenu(containerId, playerInventory, this, this.heatData);
    }

    @Override
    public void set(int index, ItemResource resource, int amount) {
        ItemStack existingStack = ItemUtil.getStack(this.items, index);
        ItemStackTemplate remainder = existingStack.getCraftingRemainder();

        if (remainder != null) {
            try (Transaction tx = Transaction.open(null)) {
                if (!ItemUtil.insertItemReturnRemaining(this.items, existingStack, false, tx).isEmpty()) {
                    tx.commit();
                }
            }
        } else {
            super.set(index, resource, amount);
        }
        setChanged();
    }

    @Override
    protected int containerSize() {
        return 1;
    }

    @Override
    protected int getBatterySlotIndex() {
        return -1;
    }

    @Override
    protected Component getDefaultName() {
        return GalacticraftBlocks.COAL_GENERATOR.get().getName();
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("LitTimeRemaining", this.litTimeRemaining);
        output.putInt("LitTotalTime", this.litTotalTime);
        output.putFloat("HeatLevel", this.heatLevel);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.litTimeRemaining = input.getIntOr("LitTimeRemaining", 0);
        this.litTotalTime = input.getIntOr("LitTotalTime", 0);
        this.heatLevel = input.getFloatOr("HeatLevel", 0.0F);
    }
}
