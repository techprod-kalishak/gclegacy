/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.gear;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.attachment.entity.GearInventoryProvider;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlotGroup;
import io.kalishak.galacticraftlegacy.world.entity.ai.attributes.GalacticraftAttributes;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.item.component.GearAttributeModifiers;
import io.kalishak.galacticraftlegacy.world.item.component.GearEquippable;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.NonNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public  class GearItem extends Item {
    public GearItem(Properties properties) {
        super(properties);
    }

    public static Properties tankProperties(ResourceKey<EquipmentAsset> equipmentAsset) {
        return new Properties()
                .stacksTo(1)
                .component(GalacticraftDataComponents.GEAR_EQUIPPABLE, GearEquippable.builder(GearEquipmentSlot.TANK).setAsset(equipmentAsset).setAdditionalSlot(GearEquipmentSlot.ADDITIONAL_TANK).build())
                .component(GalacticraftDataComponents.FLUID_TANK, SimpleFluidContent.copyOf(FluidStack.EMPTY));
    }

    public static Properties thermalPiece(GearEquipmentSlot slot, ResourceKey<EquipmentAsset> assetId, float thermal) {
        return new Properties()
                .stacksTo(1)
                .component(GalacticraftDataComponents.GEAR_EQUIPPABLE, GearEquippable.thermal(slot, assetId))
                .component(
                        GalacticraftDataComponents.GEAR_ATTRIBUTE_MODIFIERS,
                        GearAttributeModifiers.builder()
                                .add(
                                        GalacticraftAttributes.THERMAL_PROTECTION,
                                        new AttributeModifier(
                                                Constants.id("thermal_protection"),
                                                thermal,
                                                AttributeModifier.Operation.ADD_VALUE
                                        ),
                                        GearEquipmentSlotGroup.THERMAL
                                )
                                .build()
                );
    }
    public static Properties parachute(DyeColor color) {
        return new Properties()
                .stacksTo(1)
                .component(
                        GalacticraftDataComponents.GEAR_EQUIPPABLE,
                        GearEquippable.parachute(color)
                );
    }

    public static Properties simpleGear(GearEquipmentSlot slot) {
        return new Properties()
                .stacksTo(1)
                .component(
                        GalacticraftDataComponents.GEAR_EQUIPPABLE,
                        GearEquippable.builder(slot).build()
                );
    }

    @Override
    public @NonNull InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        GearEquippable equippable = itemstack.get(GalacticraftDataComponents.GEAR_EQUIPPABLE);

        if (equippable != null && equippable.swappable()) {
            try (Transaction tx = Transaction.open(null)) {
                return equippable.swapWithGearEquipmentSlot(itemstack, player, tx);
            }
        }

        return super.use(level, player, hand);
    }

    protected boolean isEquipped(ItemStack stack, GearInventoryProvider gearEquipment) {
        GearEquippable equippable = stack.get(GalacticraftDataComponents.GEAR_EQUIPPABLE);

        return equippable != null && !gearEquipment.getGearEquipment().get(equippable.gearSlot()).isEmpty();
    }
}
