package io.kalishak.galacticraftlegacy.world.inventory;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.attachment.GearInventory;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import io.kalishak.galacticraftlegacy.world.inventory.slot.GearSlot;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.DelegatingResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

import java.util.Map;

public class GearInventoryMenu extends AbstractContainerMenu {
    public static final int GEAR_SLOT_START = 0;
    public static final int GEAR_SLOT_END = 9;
    public static final int INV_SLOT_START = 9;
    public static final int INV_SLOT_END = 36;
    public static final int USE_ROW_SLOT_START = 36;
    public static final int USE_ROW_SLOT_END = 45;
    public static final Identifier EMPTY_ARMOR_SLOT_HELMET = Identifier.fromNamespaceAndPath(Galacticraft.MODID, "container/slot/helmet");
    public static final Identifier EMPTY_ARMOR_SLOT_GEAR = Identifier.fromNamespaceAndPath(Galacticraft.MODID, "container/slot/gear");
    public static final Identifier EMPTY_ARMOR_SLOT_TANK = Identifier.fromNamespaceAndPath(Galacticraft.MODID, "container/slot/tank");
    public static final Identifier EMPTY_ARMOR_SLOT_PARACHUTE = Identifier.fromNamespaceAndPath(Galacticraft.MODID, "container/slot/parachute");
    public static final Identifier EMPTY_ARMOR_SLOT_TELEMETRY = Identifier.fromNamespaceAndPath(Galacticraft.MODID, "container/slot/telemetry");
    public static final Identifier EMPTY_ARMOR_SLOT_THERMAL_HELMET = Identifier.fromNamespaceAndPath(Galacticraft.MODID, "container/slot/thermal_helmet");
    public static final Identifier EMPTY_ARMOR_SLOT_THERMAL_CHESTPLATE = Identifier.fromNamespaceAndPath(Galacticraft.MODID, "container/slot/thermal_chestplate");
    public static final Identifier EMPTY_ARMOR_SLOT_THERMAL_LEGGINGS = Identifier.fromNamespaceAndPath(Galacticraft.MODID, "container/slot/thermal_leggings");
    public static final Identifier EMPTY_ARMOR_SLOT_THERMAL_BOOTS = Identifier.fromNamespaceAndPath(Galacticraft.MODID, "container/slot/thermal_boots");
    private static final Map<GearEquipmentSlot, Identifier> TEXTURE_EMPTY_SLOTS = Util.makeEnumMap(GearEquipmentSlot.class, slot -> switch (slot) {
        case MASK -> EMPTY_ARMOR_SLOT_HELMET;
        case GEAR -> EMPTY_ARMOR_SLOT_GEAR;
        case TANK, ADDITIONAL_TANK -> EMPTY_ARMOR_SLOT_TANK;
        case PARACHUTE -> EMPTY_ARMOR_SLOT_PARACHUTE;
        case TELEMETRY -> EMPTY_ARMOR_SLOT_TELEMETRY;
        case THERMAL_CAP -> EMPTY_ARMOR_SLOT_THERMAL_HELMET;
        case THERMAL_SHIRT -> EMPTY_ARMOR_SLOT_THERMAL_CHESTPLATE;
        case THERMAL_LEGGINGS -> EMPTY_ARMOR_SLOT_THERMAL_LEGGINGS;
        case THERMAL_SOCKS -> EMPTY_ARMOR_SLOT_THERMAL_BOOTS;
    });
    private static final GearEquipmentSlot[] SLOTS_IDS = GearEquipmentSlot.values();

    public GearInventoryMenu(int syncId, Inventory playerInventory, Player player) {
        super(GalacticraftMenuType.GEAR.get(), syncId);
        GearInventory playerData = player.getData(GalacticraftAttachments.GEAR_INVENTORY);
        DelegatingResourceHandler<ItemResource> gearInventory = playerData.getDelegatingResourceHandler();

        addSlot(new GearSlot(gearInventory,
                player,
                GearEquipmentSlot.MASK,
                EMPTY_ARMOR_SLOT_HELMET,
                GEAR_SLOT_START,
                77,
                8)
        );
        addSlot(new GearSlot(gearInventory,
                player,
                GearEquipmentSlot.GEAR,
                EMPTY_ARMOR_SLOT_GEAR,
                GEAR_SLOT_START + 1,
                77,
                26)
        );
        addSlot(new GearSlot(gearInventory,
                player,
                GearEquipmentSlot.TANK,
                EMPTY_ARMOR_SLOT_TANK,
                GEAR_SLOT_START + 2,
                77,
                44)
        );
        addSlot(new GearSlot(gearInventory,
                player,
                GearEquipmentSlot.ADDITIONAL_TANK,
                EMPTY_ARMOR_SLOT_TANK,
                GEAR_SLOT_START + 3,
                95,
                44)
        );
        addSlot(new GearSlot(gearInventory,
                player,
                GearEquipmentSlot.PARACHUTE,
                EMPTY_ARMOR_SLOT_PARACHUTE,
                GEAR_SLOT_START + 4,
                95,
                18)
        );
        addSlot(new GearSlot(gearInventory,
                player,
                GearEquipmentSlot.TELEMETRY,
                EMPTY_ARMOR_SLOT_TELEMETRY,
                GEAR_SLOT_START + 5,
                113,
                18)
        );
        for (int i = 0; i < 4; i++) {
            GearEquipmentSlot slot = SLOTS_IDS[i + 6];
            Identifier identifier = TEXTURE_EMPTY_SLOTS.get(slot);

            addSlot(new GearSlot(gearInventory,
                    player,
                    slot,
                    identifier,
                    GEAR_SLOT_START + i + 6,
                    8,
                    8 + i * 18)
            );
        }

        addStandardInventorySlots(playerInventory, 8, 84);
    }

    public GearInventoryMenu(int syncId, Inventory playerInventory, RegistryFriendlyByteBuf data) {
        this(syncId, playerInventory, playerInventory.player);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack returnedStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack swappedStack = slot.getItem();
            returnedStack = swappedStack.copy();
            GearEquipmentSlot gearSlot = GearEquipmentSlot.getGearSlotForItem(returnedStack);

            if (gearSlot != null && (index == GearEquipmentSlot.TANK.getIndex() || index == GearEquipmentSlot.ADDITIONAL_TANK.getIndex())) {
                if (!moveItemStackTo(swappedStack, GearEquipmentSlot.TANK.getIndex(), GearEquipmentSlot.ADDITIONAL_TANK.getIndex(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.slots.get(GEAR_SLOT_END - 1 - gearSlot.getIndex()).hasItem()) {
                int gearSlotIndex = GEAR_SLOT_END - 1 - gearSlot.getIndex();

                if (!moveItemStackTo(swappedStack, gearSlotIndex, gearSlotIndex + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= INV_SLOT_START && index < INV_SLOT_END) {
                if (!moveItemStackTo(swappedStack, USE_ROW_SLOT_START, USE_ROW_SLOT_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= USE_ROW_SLOT_START && index < USE_ROW_SLOT_END) {
                if (!moveItemStackTo(swappedStack, INV_SLOT_START, INV_SLOT_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(swappedStack, INV_SLOT_START, USE_ROW_SLOT_END, false)) {
                return ItemStack.EMPTY;
            }

            if (swappedStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY, returnedStack);
            } else {
                slot.setChanged();
            }

            if (swappedStack.getCount() == returnedStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, swappedStack);
        }

        return returnedStack;
    }
}
