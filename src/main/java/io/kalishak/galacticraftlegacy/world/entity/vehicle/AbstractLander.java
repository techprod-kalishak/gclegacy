/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.entity.vehicle;

import io.kalishak.galacticraftlegacy.transfer.capability.fluid.SingleTankResourceHandler;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

public abstract class AbstractLander extends MovingEntity {
    private static final int FUEL_CAPACITY = 5000;
    protected FluidStack fuelStack = FluidStack.EMPTY;
    protected final SingleTankResourceHandler tankResourceHandler = new SingleTankResourceHandler() {
        @Override
        public @NonNull FluidStack getFluidStack() {
            return AbstractLander.this.fuelStack;
        }

        @Override
        public void setFluidStack(@NonNull FluidStack stack) {
            AbstractLander.this.fuelStack = stack;
        }

        @Override
        public int getCapacity() {
            return FUEL_CAPACITY;
        }
    };
    private boolean lastShouldMove;
    private UUID persistantRiderUUID;
    private boolean shouldMoveClient;
    private boolean shouldMoveServer;
    private boolean syncAdjustFlag = true;

    public AbstractLander(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public AbstractLander(EntityType<?> entityType, Level level, double x, double y, double z) {
        super(entityType, level, x, y, z);
    }

    protected static <T extends AbstractLander> void registerFluidResourceHandler(RegisterCapabilitiesEvent event, EntityType<T> type) {
        event.registerEntity(
                Capabilities.Fluid.ENTITY,
                type,
                (entity, context) -> entity.tankResourceHandler
        );
    }
}
