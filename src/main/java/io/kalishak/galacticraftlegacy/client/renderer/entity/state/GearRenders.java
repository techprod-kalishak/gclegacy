package io.kalishak.galacticraftlegacy.client.renderer.entity.state;

import io.kalishak.galacticraftlegacy.transfer.entity.SpaceGearEquipment;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.item.ItemStack;

public interface GearRenders {
    static GearRenders vanillaWrapper(EntityRenderState state) {
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
                return state.getRenderDataOrDefault(GearRenderState.PARACHUTE, GearRenders.super.parachute());
            }

            @Override
            public ItemStack telemetryModule() {
                return state.getRenderDataOrDefault(GearRenderState.TELEMETRY_MODULE, GearRenders.super.telemetryModule());
            }

            @Override
            public ItemStack shieldController() {
                return state.getRenderDataOrDefault(GearRenderState.SHIELD_CONTROLLER, GearRenders.super.shieldController());
            }
        };
    }

    static GearRenders of(SpaceGearEquipment gear) {
        return new GearRenders() {
            @Override
            public ItemStack oxygenMask() {
                return gear.get(GearEquipmentSlot.GEAR);
            }

            @Override
            public ItemStack oxygenGear() {
                return gear.get(GearEquipmentSlot.MASK);
            }

            @Override
            public ItemStack leftTank() {
                return gear.get(GearEquipmentSlot.TANK);
            }

            @Override
            public ItemStack rightTank() {
                return gear.get(GearEquipmentSlot.ADDITIONAL_TANK);
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

    default ItemStack telemetryModule() {
        return ItemStack.EMPTY;
    }

    default ItemStack shieldController() {
        return ItemStack.EMPTY;
    }
}
