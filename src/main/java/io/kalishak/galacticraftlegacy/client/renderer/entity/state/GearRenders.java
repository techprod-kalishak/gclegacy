/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.entity.state;

import io.kalishak.galacticraftlegacy.transfer.entity.SpaceGearEquipment;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.item.ItemStack;

public interface GearRenders {
    static GearRenders fromRenderState(EntityRenderState state) {
        return new GearRenders() {
            @Override
            public ItemStack oxygenMask() {
                return state.getRenderDataOrDefault(GearRenderState.OXYGEN_MASK, ItemStack.EMPTY);
            }

            @Override
            public ItemStack oxygenGear() {
                return state.getRenderDataOrDefault(GearRenderState.OXYGEN_GEAR, ItemStack.EMPTY);
            }

            @Override
            public ItemStack leftTank() {
                return state.getRenderDataOrDefault(GearRenderState.TANK, ItemStack.EMPTY);
            }

            @Override
            public ItemStack rightTank() {
                return state.getRenderDataOrDefault(GearRenderState.ADDITIONAL_TANK, ItemStack.EMPTY);
            }

            @Override
            public ItemStack parachute() {
                return state.getRenderDataOrDefault(GearRenderState.PARACHUTE, ItemStack.EMPTY);
            }

            @Override
            public ItemStack frequencyModule() {
                return state.getRenderDataOrDefault(GearRenderState.FREQUENCY_MODULE, ItemStack.EMPTY);
            }

            @Override
            public ItemStack shieldController() {
                return state.getRenderDataOrDefault(GearRenderState.SHIELD_CONTROLLER, ItemStack.EMPTY);
            }
        };
    }

    static GearRenders fromGear(SpaceGearEquipment gear) {
        return new GearRenders() {
            @Override
            public ItemStack oxygenMask() {
                return gear.get(GearEquipmentSlot.MASK);
            }

            @Override
            public ItemStack oxygenGear() {
                return gear.get(GearEquipmentSlot.GEAR);
            }

            @Override
            public ItemStack leftTank() {
                return gear.get(GearEquipmentSlot.TANK);
            }

            @Override
            public ItemStack rightTank() {
                return gear.get(GearEquipmentSlot.ADDITIONAL_TANK);
            }

            @Override
            public ItemStack parachute() {
                return gear.get(GearEquipmentSlot.PARACHUTE);
            }

            @Override
            public ItemStack frequencyModule() {
                return gear.get(GearEquipmentSlot.FREQUENCY_MODULE);
            }

            @Override
            public ItemStack shieldController() {
                return gear.get(GearEquipmentSlot.SHIELD);
            }
        };
    }

    static GearRenders fixed() {
        return new GearRenders() {
            @Override
            public ItemStack oxygenMask() {
                return GalacticraftItems.OXYGEN_MASK.toStack();
            }

            @Override
            public ItemStack oxygenGear() {
                return GalacticraftItems.OXYGEN_GEAR.toStack();
            }

            @Override
            public ItemStack leftTank() {
                return GalacticraftItems.HEAVY_TANK.toStack();
            }

            @Override
            public ItemStack rightTank() {
                return GalacticraftItems.HEAVY_TANK.toStack();
            }
        };
    }

    ItemStack oxygenMask();
    ItemStack oxygenGear();
    ItemStack leftTank();
    ItemStack rightTank();

    default ItemStack parachute() {
        return ItemStack.EMPTY;
    }

    default ItemStack frequencyModule() {
        return ItemStack.EMPTY;
    }

    default ItemStack shieldController() {
        return ItemStack.EMPTY;
    }
}
