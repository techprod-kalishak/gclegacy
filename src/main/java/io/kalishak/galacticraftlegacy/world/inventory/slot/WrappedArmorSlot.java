package io.kalishak.galacticraftlegacy.world.inventory.slot;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;
import org.jspecify.annotations.Nullable;

public class WrappedArmorSlot extends ResourceHandlerSlot {
    private final LivingEntity owner;
    private final EquipmentSlot slot;
    private final @Nullable Identifier emptyIcon;

    public WrappedArmorSlot(ResourceHandler<ItemResource> handler, LivingEntity owner, EquipmentSlot slot, int index, int xPosition, int yPosition, @Nullable Identifier emptyIcon) {
        super(handler, ((index1, resource, amount) -> {
            Equippable equippable = resource.get(DataComponents.EQUIPPABLE);

            if (equippable != null && resource.toStack().canEquip(equippable.slot(), owner)) {
                owner.setItemSlot(equippable.slot(), resource.toStack());
            }
        }), index, xPosition, yPosition);
        this.owner = owner;
        this.slot = slot;
        this.emptyIcon = emptyIcon;
    }

    @Override
    public void setByPlayer(ItemStack newStack, ItemStack oldStack) {
        this.owner.onEquipItem(this.slot, newStack, oldStack);
        super.setByPlayer(newStack, oldStack);
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
    public @Nullable Identifier getNoItemIcon() {
        return this.emptyIcon;
    }
}
