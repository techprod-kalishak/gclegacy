/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.inventory.slot;

import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.attachment.entity.PlayerSpaceData;
import io.kalishak.galacticraftlegacy.transfer.entity.SpaceGearEquipment;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.item.component.GearEquippable;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.jspecify.annotations.Nullable;

public class GearSlot extends ConditionalHandlerSlot {
    private final LivingEntity owner;
    private final GearEquipmentSlot slot;
    private final @Nullable Identifier icon;

    public GearSlot(ResourceHandler<ItemResource> handler, SpaceGearEquipment equipment, LivingEntity owner, GearEquipmentSlot slot, @Nullable Identifier icon, int index, int xPosition, int yPosition) {
        super(handler, (rIndex, resource, amount) -> equipment.set(GearEquipmentSlot.byId(rIndex), resource.toStack(amount)), resource -> canSet(resource, slot), index, xPosition, yPosition);
        this.owner = owner;
        this.slot = slot;
        this.icon = icon;
    }

    private static boolean canSet(DataComponentGetter componentGetter, GearEquipmentSlot slot) {
        GearEquippable gearEquippable = componentGetter.get(GalacticraftDataComponents.GEAR_EQUIPPABLE);

        if (gearEquippable == null) {
            return false;
        }

        return gearEquippable.gearSlot() == slot || slot.isTank() && (gearEquippable.gearSlot().isTank() || gearEquippable.additionalGearSlot().filter(GearEquipmentSlot::isTank).isPresent());
    }

    @Override
    public void setByPlayer(ItemStack newStack, ItemStack oldStack) {
        PlayerSpaceData data = this.owner.getData(GalacticraftAttachments.PLAYER_SPACE_DATA);
        data.onGearEquipped(this.owner, this.slot, newStack, oldStack);

        super.setByPlayer(newStack, oldStack);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean mayPickup(Player player) {
        ItemStack stack = getStackCopy();

        return super.mayPickup(player) && (player.isCreative() || !EnchantmentHelper.has(stack, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE));
    }

    @Override
    public @Nullable Identifier getNoItemIcon() {
        return this.icon;
    }

    public GearEquipmentSlot getSlot() {
        return this.slot;
    }
}
