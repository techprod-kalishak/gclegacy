package io.kalishak.galacticraftlegacy.world.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GearItem extends Item {
    public GearItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        GearEquippable equippable = itemstack.get(GalacticraftDataComponents.GEAR_EQUIPPABLE);

        if (equippable != null && equippable.swappable()) {
            return equippable.swapWithGearEquipmentSlot(itemstack, player, null);
        }

        return super.use(level, player, hand);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {

    }
}
