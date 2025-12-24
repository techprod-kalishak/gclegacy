package io.kalishak.galacticraftlegacy.world.level.block.entity;

import io.kalishak.galacticraftlegacy.world.inventory.CoalGeneratorMenu;
import io.kalishak.galacticraftlegacy.world.inventory.WorldlyEnergyHandler;
import io.kalishak.galacticraftlegacy.world.level.block.AbstractMachineBlock;
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
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.transfer.VoidingResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

public class CoalGeneratorBlockEntity extends AbstractMachineBlockEntity {
    public static final int DATA_LIT_DURATION = 0;
    public static final int DATA_LIT_TOTAL = 1;
    public static final int DATA_HEAT_LEVEL = 2;
    public static final int MIN_ENERGY_PER_HEAT = 30;
    public static final int MAX_ENERGY_PER_HEAT = 150;
    public static final float HEAT_UP_SPEED = 0.3F;
    int litTimeRemaining;
    int litTotalTime;
    float heatLevel;
    private final ContainerData containerData = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case DATA_LIT_DURATION -> {
                    if (CoalGeneratorBlockEntity.this.litTotalTime > Short.MAX_VALUE) {
                        yield Mth.floor((double) CoalGeneratorBlockEntity.this.litTimeRemaining / CoalGeneratorBlockEntity.this.litTotalTime * Short.MAX_VALUE);
                    }

                    yield CoalGeneratorBlockEntity.this.litTimeRemaining;
                }
                case DATA_LIT_TOTAL -> Math.min(CoalGeneratorBlockEntity.this.litTotalTime, Short.MAX_VALUE);
                case DATA_HEAT_LEVEL -> Mth.floor(CoalGeneratorBlockEntity.this.heatLevel);
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case DATA_LIT_DURATION -> CoalGeneratorBlockEntity.this.litTimeRemaining = value;
                case DATA_LIT_TOTAL -> CoalGeneratorBlockEntity.this.litTotalTime = value;
                case DATA_HEAT_LEVEL -> CoalGeneratorBlockEntity.this.heatLevel = value;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    public CoalGeneratorBlockEntity(BlockPos pos, BlockState blockState) {
        super(GalacticraftBlockEntityType.COAL_GENERATOR.get(), pos, blockState);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.Item.BLOCK,
                GalacticraftBlockEntityType.COAL_GENERATOR.get(),
                (machine, cxt) -> {
                    if (cxt == null || cxt == Direction.UP) {
                        return machine.innerResourceHandler;
                    }

                    return new VoidingResourceHandler<>(ItemResource.EMPTY);
                }
        );
        registerEnergyHandler(
                GalacticraftBlockEntityType.COAL_GENERATOR.get(),
                Direction.SOUTH,
                machine -> new WorldlyEnergyHandler(new WorldlyEnergyHandler.SidedSource() {
                    @Override
                    public Direction getOutputDirection() {
                        return Direction.SOUTH;
                    }}, Direction.SOUTH, machine.energyHandler),
                event
        );
    }

    public static void serverTick(ServerLevel level, BlockPos pos, BlockState state, CoalGeneratorBlockEntity coalGenerator) {
        boolean isLit = coalGenerator.isLit();
        boolean hadChanged = false;

        if (coalGenerator.heatLevel - MIN_ENERGY_PER_HEAT > 0) {
            try (Transaction tx = Transaction.open(null)) {
                if (coalGenerator.energyHandler.insert(Mth.floor(coalGenerator.heatLevel) - MIN_ENERGY_PER_HEAT, tx) > 0) {
                    tx.commit();
                }
            }
        }

        if (coalGenerator.litTimeRemaining > 0) {
            coalGenerator.litTimeRemaining--;
            coalGenerator.heatLevel = Math.min(coalGenerator.heatLevel + Math.max(coalGenerator.heatLevel * 0.005F, HEAT_UP_SPEED), MAX_ENERGY_PER_HEAT);
        }

        ItemResource fuel = coalGenerator.innerResourceHandler.getResource(0);

        if (coalGenerator.litTimeRemaining <= 0 && !fuel.isEmpty()) {
            try (Transaction tx = Transaction.open(null)) {
                FurnaceFuel furnaceFuel = level.registryAccess().lookupOrThrow(Registries.ITEM).getData(NeoForgeDataMaps.FURNACE_FUELS, fuel.getHolder().unwrapKey().orElseThrow());
                ItemStack remainder = fuel.toStack().getCraftingRemainder();

                if (coalGenerator.innerResourceHandler.extract(fuel, 1, tx) > 0) {
                    if (furnaceFuel != null && furnaceFuel.burnTime() > 0) {
                        if (!remainder.isEmpty()) {
                            coalGenerator.innerResourceHandler.set(0, ItemResource.of(remainder), remainder.getCount());
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
            setChanged(level, pos, state);
        }
    }

    public boolean isLit() {
        return this.litTimeRemaining > 0;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new CoalGeneratorMenu(containerId, playerInventory, this, this.containerData);
    }

    @Override
    public void set(int index, ItemResource resource, int amount) {
        ItemStack existingStack = ItemUtil.getStack(this.innerResourceHandler, index);
        ItemStack remainder = existingStack.getCraftingRemainder();

        if (!remainder.isEmpty()) {
            try (Transaction tx = Transaction.open(null)) {
                if (!ItemUtil.insertItemReturnRemaining(this.innerResourceHandler, existingStack, false, tx).isEmpty()) {
                    tx.commit();
                }
            }
        } else {
            super.set(index, resource, amount);
        }
        setChanged();
    }

    @Override
    protected int size() {
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
