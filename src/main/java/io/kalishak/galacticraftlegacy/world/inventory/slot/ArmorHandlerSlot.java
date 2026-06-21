package io.kalishak.galacticraftlegacy.world.inventory.slot;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.neoforge.transfer.item.PlayerInventoryWrapper;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;
import org.jspecify.annotations.Nullable;

public class ArmorHandlerSlot extends ResourceHandlerSlot {
    private final LivingEntity owner;
    private final EquipmentSlot slot;
    private final @Nullable Identifier emptyIcon;

    public ArmorHandlerSlot(Inventory playerInventory, LivingEntity owner, EquipmentSlot slot, int xPosition, int yPosition, @Nullable Identifier emptyIcon) {
        super(PlayerInventoryWrapper.of(playerInventory).getArmorSlots(), ((_, resource, amount) -> playerInventory.add(slot.getIndex(), resource.toStack(amount))), slot.getIndex(), xPosition, yPosition);
        this.owner = owner;
        this.slot = slot;
        this.emptyIcon = emptyIcon;
    }

    @Override
    public void setByPlayer(ItemStack itemStack, ItemStack previous) {
        this.owner.onEquipItem(this.slot, previous, itemStack);
        super.setByPlayer(itemStack, previous);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.canEquip(this.slot, this.owner);
    }

    @Override
    public boolean isActive() {
        return this.owner.canUseSlot(this.slot);
    }

    @Override
    public boolean mayPickup(Player player) {
        ItemStack itemStack = this.getItem();
        return (itemStack.isEmpty() || player.isCreative() || !EnchantmentHelper.has(itemStack, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE)) && super.mayPickup(player);
    }

    @Override
    public @Nullable Identifier getNoItemIcon() {
        return this.emptyIcon;
    }
}
