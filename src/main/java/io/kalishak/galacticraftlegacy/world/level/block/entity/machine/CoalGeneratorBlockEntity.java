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
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.util.Objects;

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
        registerEnergyCapability(GalacticraftBlockEntityType.COAL_GENERATOR.get(), event);
        registerItemCapability(GalacticraftBlockEntityType.COAL_GENERATOR.get(), event);
    }

    private static int getFuelTime(RegistryAccess access, ItemStack stack) {
        FurnaceFuel fuel = access.lookupOrThrow(Registries.ITEM).getData(NeoForgeDataMaps.FURNACE_FUELS, stack.typeHolder().unwrapKey().orElseThrow());

        return fuel != null ? fuel.burnTime() : 0;
    }

    public static void serverTick(ServerLevel level, BlockPos pos, BlockState state, CoalGeneratorBlockEntity entity) {
        boolean isLit = entity.isLit();
        boolean hadChanged = false;

        if (entity.heatLevel - MIN_ENERGY_PER_HEAT > 0) {
            try (Transaction tx = Transaction.open(null)) {
                if (entity.capacitor.insert(Mth.floor(entity.heatLevel) - MIN_ENERGY_PER_HEAT, tx) > 0) {
                    tx.commit();
                }
            }
        }

        if (entity.litTimeRemaining > 0) {
            entity.litTimeRemaining--;
            entity.heatLevel = Math.min(entity.heatLevel + Math.max(entity.heatLevel * 0.005F, HEAT_UP_SPEED), MAX_ENERGY_PER_HEAT);
        }

        ItemStack fuel = ItemUtil.getStack(entity.items, 0);
        int fuelTime = getFuelTime(level.registryAccess(), fuel);

        if (entity.litTimeRemaining == 0 && fuelTime > 0) {
            try (Transaction tx = Transaction.open(null)) {
                ItemStackTemplate remainder = fuel.getCraftingRemainder();


                if (entity.items.extract(ItemResource.of(fuel), 1, tx) > 0) {
                    tx.commit();

                    entity.litTotalTime = fuelTime;
                    entity.litTimeRemaining = fuelTime;

                    if (fuel.isEmpty()) {
                        entity.setItem(0, remainder != null ? remainder.create() : ItemStack.EMPTY);
                    }
                }
            }
        }

        if (!entity.isLit() && entity.heatLevel > 0) {
            entity.heatLevel = Mth.clamp(entity.heatLevel - HEAT_UP_SPEED, 0, entity.heatLevel);
        }

        if (isLit != entity.isLit()) {
            hadChanged = true;
            state = state.setValue(AbstractMachineBlock.LIT, entity.isLit());
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
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
        return new CoalGeneratorMenu(containerId, playerInventory, this, this.heatData);
    }

    @Override
    protected void onItemChange(int slot, ItemStack previousStack) {
        assert this.level != null;

        ItemStack setStack = getItem(slot);
        FurnaceFuel fuel = this.level.registryAccess().lookupOrThrow(Registries.ITEM).getData(NeoForgeDataMaps.FURNACE_FUELS, Objects.requireNonNull(setStack.typeHolder().getKey()));

        if (fuel != null && fuel.burnTime() > 0) {
            this.litTotalTime = fuel.burnTime();
            this.litTimeRemaining = fuel.burnTime();
        }

        ItemStackTemplate remainder = setStack.getCraftingRemainder();

        if (remainder != null && setStack.getCount() == 1) {
            setItem(slot, ItemResource.of(remainder), remainder.count());
        }
    }

    @Override
    public int getItemsSize() {
        return 1;
    }

    @Override
    protected int getBatterySlotIndex() {
        return -1;
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
