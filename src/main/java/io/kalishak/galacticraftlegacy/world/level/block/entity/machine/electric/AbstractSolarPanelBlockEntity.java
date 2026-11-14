/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.machine.electric;

import io.kalishak.galacticraftlegacy.world.attribute.GalacticraftEnvironmentAttributes;
import io.kalishak.galacticraftlegacy.world.inventory.machine.SolarPanelMenu;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.EnergyProducer;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.ToggledMachine;
import io.kalishak.galacticraftlegacy.world.level.dimension.GalacticraftDimensionTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public abstract class AbstractSolarPanelBlockEntity extends AbstractMachineBlockEntity implements EnergyProducer, ToggledMachine {
    protected boolean isDisabled = false;
    protected float solarStrength;
    protected float targetAngle;
    protected float currentAngle;
    private int lastEnergyProduced;
    private final DataSlot solarStrengthSlot = new DataSlot() {
        @Override
        public void set(int value) {
        }

        @Override
        public int get() {
            return (int) Math.floor(AbstractSolarPanelBlockEntity.this.getSolarStrength());
        }
    };

    protected AbstractSolarPanelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public static <T extends AbstractSolarPanelBlockEntity> void registerCapabilities(BlockEntityType<T> type, RegisterCapabilitiesEvent event) {
        registerEnergyCapability(type, event);
        registerItemCapability(type, event);
    }

    public static void serverTick(ServerLevel level, BlockPos blockPos, BlockState blockState, AbstractSolarPanelBlockEntity entity) {
        if (!entity.isDisabled()) {
            entity.updateEnergy(level, blockPos);

            chargeBattery(entity, 200, null);
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putBoolean("IsDisabled", this.isDisabled);
        output.putFloat("SolarStrength", this.solarStrength);
        output.putFloat("TargetAngle", this.targetAngle);
        output.putFloat("CurrentAngle", this.currentAngle);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.isDisabled = input.getBooleanOr("IsDisabled", false);
        this.solarStrength = input.getFloatOr("SolarStrength", 0.0F);
        this.targetAngle = input.getFloatOr("TargetAngle", 77.5F);
        this.currentAngle = input.getFloatOr("CurrentAngle", 0.0F);
    }

    @Override
    protected int getSize() {
        return 1;
    }

    @Override
    public boolean isDisabled() {
        return this.isDisabled;
    }

    @Override
    public void toggleGeneration() {
        this.isDisabled = !this.isDisabled;
    }

    public float getSolarStrength() {
        return this.solarStrength;
    }

    public void updateEnergy(Level level, BlockPos pos) {
        boolean canSeeLight = level.dimensionType().hasSkyLight() && level.isBrightOutside();
        boolean weatherTrouble = level.isRainingAt(pos) || level.isThundering();

        if (canSeeLight && !weatherTrouble) {
            this.solarStrength = 0.0F;

            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    int target = level.getEffectiveSkyBrightness(new BlockPos(i, pos.getY() + 2, j));

                    if (target > 0) {
                        this.solarStrength = Math.clamp(this.solarStrength + (target / 15.0F), 0.0F, 200.0F);
                    }
                }
            }
        }

        if (!canSeeLight || weatherTrouble) {
            this.currentAngle = 0.0F;
        }

        float sunAngle = getSunAngle(level, pos);
        this.targetAngle = 77.5F;

        if (!canSeeLight && weatherTrouble) {
            this.targetAngle += 180.0F;
        }

        if (sunAngle > 27.5F && sunAngle < 152.5F) {
            float difference = this.targetAngle - sunAngle + 12.0F;
            this.targetAngle -= difference / 20.0F;
        } else if (sunAngle <= 27.5F) {
            this.targetAngle = 15.0F;
        } else if (sunAngle >= 152.5F) {
            this.targetAngle = 140.0F;
        }

        if (this.currentAngle <= 0.0F) {
            this.currentAngle = targetAngle;
        } else if (this.currentAngle > this.targetAngle) {
            this.currentAngle--;
        } else if (this.currentAngle < this.targetAngle) {
            this.currentAngle++;
        }

        if (this.solarStrength > 0.0F) {
            int toGen = Math.min(this.capacitor.getCapacityAsInt() - this.capacitor.getAmountAsInt(), generateAmount(level, pos));

            try (Transaction tx = Transaction.open(null)) {
                if (this.capacitor.insert(toGen, tx) == toGen) {
                    tx.commit();
                }
            }
        }
    }

    private int generateAmount(Level level, BlockPos blockPos) {
        float sunAngle = getSunAngle(level, blockPos);
        float difference = (180.0F - Math.abs((this.currentAngle + 12.5F) % 180F - sunAngle)) / 180.0F;

        return this.lastEnergyProduced = Mth.floor(0.01F * difference * difference * (this.solarStrength * (Math.abs(difference) * 500.0F)) * getSolarBoost(level, blockPos));
    }

    public float getSolarBoost(Level level, BlockPos blockPos) {
        float solarBoost = level.environmentAttributes().getDimensionValue(GalacticraftEnvironmentAttributes.SOLAR_MULTIPLIER.get());

        if (level.dimensionTypeRegistration().is(GalacticraftDimensionTypes.VENUS)) {
            if (blockPos.getY() > 90) {
                solarBoost += (blockPos.getY() - 90) / 1000.0F;
            }
        }

        return solarBoost;
    }

    public float getCurrentAngle() {
        return this.currentAngle;
    }

    public int getEnergyProduced() {
        return this.lastEnergyProduced;
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new SolarPanelMenu(containerId, inventory, this, this.solarStrengthSlot);
    }

    public static float getSunAngle(Level level, BlockPos worldPosition) {
        return level.environmentAttributes().getValue(EnvironmentAttributes.SUN_ANGLE, worldPosition) * ((float) Math.PI / 180.0F);
    }

    public static float getSolarBoost(AbstractSolarPanelBlockEntity entity) {
        if (entity.level == null) {
            return 0.0F;
        }

        return entity.getSolarBoost(entity.level, entity.worldPosition);
    }
}
