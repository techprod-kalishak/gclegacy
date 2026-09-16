package io.kalishak.galacticraftlegacy.world.item;

import io.kalishak.galacticraftlegacy.attachment.AttachmentHelper;
import io.kalishak.galacticraftlegacy.transfer.entity.SpaceGearEquipment;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.item.component.GearEquippable;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Util;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;

import java.util.List;

public class SpaceEmergencyKitItem extends Item {
    public SpaceEmergencyKitItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (player.isShiftKeyDown()) {
            return InteractionResult.PASS;
        }

        ItemStack stack = player.getItemInHand(hand);

        if (stack.is(this)) {
            SpaceGearEquipment equipment = AttachmentHelper.getGearInventory(player).getGearEquipment();

            List<ItemStack> contents = getContents();
            for (int i = 0; i < 9; i++) {
                ItemStack emergencyItem = contents.get(i);
                GearEquippable equippable = emergencyItem.get(GalacticraftDataComponents.GEAR_EQUIPPABLE);

                if (equippable != null) {
                    if (equipment.get(equippable.gearSlot()).isEmpty()) {
                        equipment.set(equippable.gearSlot(), emergencyItem);
                    } else if (equippable.additionalGearSlot().isPresent() && equipment.get(equippable.additionalGearSlot().get()).isEmpty()) {
                        equipment.set(equippable.additionalGearSlot().get(), emergencyItem);
                    } else {
                        player.addItem(emergencyItem);
                    }
                } else {
                    player.addItem(emergencyItem);
                }
            }

            stack.consume(1, player);
            return InteractionResult.CONSUME;
        }

        return super.use(level, player, hand);
    }

    private static List<ItemStack> getContents() {
        return List.of(
                GalacticraftItems.OXYGEN_MASK.toStack(),
                GalacticraftItems.OXYGEN_GEAR.toStack(),
                GalacticraftItems.LIGHT_TANK.toStack(),
                GalacticraftItems.LIGHT_TANK.toStack(),
                GalacticraftItems.STEEL_PICKAXE.toStack(),
                GalacticraftItems.DEHYDRATED_POTATO.toStack(),
                potion(Potions.HEALING),
                potion(Potions.LONG_NIGHT_VISION),
                GalacticraftItems.PARACHUTE.white().toStack()
        );
    }

    private static ItemStack potion(Holder<Potion> potionHolder) {
        return Util.make(new ItemStack(Items.POTION), item -> item.set(DataComponents.POTION_CONTENTS, new PotionContents(potionHolder)));
    }
}
