package io.kalishak.galacticraftlegacy.world.inventory.slot;

import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.attachment.GearInventory;
import io.kalishak.galacticraftlegacy.transfer.entity.MutableResourceHandler;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.item.GearEquippable;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.neoforge.transfer.DelegatingResourceHandler;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.jspecify.annotations.Nullable;

public class GearSlot extends MutableHandlerSlot {
    private final LivingEntity owner;
    private final GearEquipmentSlot slot;
    private final @Nullable Identifier icon;

    public GearSlot(DelegatingResourceHandler<ItemResource> handler, LivingEntity owner, GearEquipmentSlot slot, @Nullable Identifier icon, int index, int xPosition, int yPosition) {
        super(handler, modify(handler), resource -> canSet(resource, slot), index, xPosition, yPosition);
        this.owner = owner;
        this.slot = slot;
        this.icon = icon;
    }

    private static boolean canSet(ItemResource itemResource, GearEquipmentSlot slot) {
        GearEquippable gearEquippable = itemResource.get(GalacticraftDataComponents.GEAR_EQUIPPABLE);

        if (gearEquippable == null) {
            return false;
        }

        return gearEquippable.gearSlot() == slot || slot.isTank() && (gearEquippable.gearSlot().isTank() || gearEquippable.additionalGearSlot().filter(GearEquipmentSlot::isTank).isPresent());
    }

    private static IndexModifier<ItemResource> modify(DelegatingResourceHandler<ItemResource> handler) {
        return handler.getDelegate() instanceof MutableResourceHandler<ItemResource> mutableResourceHandler ? mutableResourceHandler : (index, resource, amount) -> {};
    }

    @Override
    public void setByPlayer(ItemStack newStack, ItemStack oldStack) {
        GearInventory data = this.owner.getData(GalacticraftAttachments.GEAR_INVENTORY);
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
