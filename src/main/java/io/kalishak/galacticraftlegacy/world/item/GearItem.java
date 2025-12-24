package io.kalishak.galacticraftlegacy.world.item;

import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class GearItem extends Item {
    public GearItem(Properties properties) {
        super(properties);
    }

    public static Properties tankProperties() {
        return new Properties()
                .stacksTo(1)
                .component(GalacticraftDataComponents.GEAR_EQUIPPABLE, GearEquippable.builder(GearEquipmentSlot.TANK).setAdditionalSlot(GearEquipmentSlot.ADDITIONAL_TANK).build())
                .component(GalacticraftDataComponents.OXYGEN_TANK, SimpleFluidContent.copyOf(FluidStack.EMPTY));
    }

    public static Properties thermalPiece(GearEquipmentSlot slot, ResourceKey<EquipmentAsset> assetId) {
        return new Properties()
                .stacksTo(1)
                .component(GalacticraftDataComponents.GEAR_EQUIPPABLE, GearEquippable.thermal(slot, assetId));
                //TODO add temperature modifer .attributes(ItemAttributeModifiers.EMPTY);
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
            return equippable.swapWithGearEquipmentSlot(itemstack, player, null);
        }

        return super.use(level, player, hand);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {

    }

    @Override
    public Component getName(ItemStack stack) {
        return super.getName(stack);
    }
}
