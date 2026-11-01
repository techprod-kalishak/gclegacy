/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.entity.vehicle.lander;

import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.attachment.entity.PlayerSpaceData;
import io.kalishak.galacticraftlegacy.attachment.entity.TransitionalRocketInfo;
import io.kalishak.galacticraftlegacy.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.transfer.capability.fluid.SingleTankResourceHandler;
import io.kalishak.galacticraftlegacy.world.entity.vehicle.MovingEntity;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.transfer.item.ItemResource;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public abstract class AbstractLander extends MovingEntity {
    private static final int FUEL_CAPACITY = 5000;
    protected final SingleTankResourceHandler fluid = new SingleTankResourceHandler(FUEL_CAPACITY);
    private boolean lastShouldMove;
    private UUID persistantRiderUUID;
    private boolean shouldMoveClient;
    private boolean shouldMoveServer;
    private final boolean syncAdjustFlag = true;

    public AbstractLander(EntityType<?> entityType, Level level, int inventorySize) {
        super(entityType, level, inventorySize);
    }

    public AbstractLander(EntityType<?> entityType, Level level, int inventorySize, double x, double y, double z) {
        super(entityType, level, inventorySize, x, y, z);
    }

    public int getScaledFuelLevel(int currentAmount) {
        double fuelLevel = this.fluid.getAmount();

        return fuelLevel > 0 ? (int) Math.floor(fuelLevel * currentAmount / FUEL_CAPACITY) : 0;
    }

    protected static void fillTransitionalRocketInfo(AbstractLander lander, Player player) {
        PlayerSpaceData spaceData = player.getData(GalacticraftAttachments.PLAYER_SPACE_DATA);
        TransitionalRocketInfo rocketInfo = spaceData.getTransitionalRocketInfo();

        if (rocketInfo != null) {
            ItemContainerContents itemContents = rocketInfo.rocket().get(DataComponents.CONTAINER);
            SimpleFluidContent fluidContent = rocketInfo.rocket().get(GalacticraftDataComponents.FLUID_TANK);

            if (itemContents != null) {
                for (int i = 0; i < itemContents.getSlots(); i++) {
                    Optional<ItemStackTemplate> stack = itemContents.getTemplateInSlot(i);

                    if (stack.isPresent()) {
                        lander.items.set(i, ItemResource.of(stack.get()), stack.get().count());
                    }
                }
            }

            if (fluidContent != null && !fluidContent.isEmpty()) {
                lander.fluid.setFluidStack(fluidContent.copy());
            }
        }
    }

    protected static <T extends AbstractLander> void registerFluidResourceHandler(RegisterCapabilitiesEvent event, EntityType<T> type) {
        event.registerEntity(
                Capabilities.Fluid.ENTITY,
                type,
                (entity, _) -> entity.fluid
        );
    }
}
