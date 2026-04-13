package io.kalishak.galacticraftlegacy.world.inventory;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.attachment.entity.PlayerSpaceData;
import io.kalishak.galacticraftlegacy.transfer.entity.SpaceGearEquipment;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import io.kalishak.galacticraftlegacy.world.inventory.slot.GearSlot;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class GearInventoryMenu extends AbstractContainerMenu {
    public static final int GEAR_SLOT_START = 0;
    public static final int GEAR_SLOT_END = 10;
    public static final int INV_SLOT_START = 10;
    public static final int INV_SLOT_END = 37;
    public static final int USE_ROW_SLOT_START = 37;
    public static final int USE_ROW_SLOT_END = 46;
    public static final Identifier EMPTY_SLOT_THERMAL_HELMET = Identifier.fromNamespaceAndPath(Galacticraft.MODID, "container/slot/thermal_helmet");
    public static final Identifier EMPTY_SLOT_THERMAL_CHESTPLATE = Identifier.fromNamespaceAndPath(Galacticraft.MODID, "container/slot/thermal_chestplate");
    public static final Identifier EMPTY_SLOT_THERMAL_LEGGINGS = Identifier.fromNamespaceAndPath(Galacticraft.MODID, "container/slot/thermal_leggings");
    public static final Identifier EMPTY_SLOT_THERMAL_BOOTS = Identifier.fromNamespaceAndPath(Galacticraft.MODID, "container/slot/thermal_boots");
    public static final Identifier EMPTY_SLOT_HELMET = Identifier.fromNamespaceAndPath(Galacticraft.MODID, "container/slot/helmet");
    public static final Identifier EMPTY_SLOT_GEAR = Identifier.fromNamespaceAndPath(Galacticraft.MODID, "container/slot/gear");
    public static final Identifier EMPTY_SLOT_TANK = Identifier.fromNamespaceAndPath(Galacticraft.MODID, "container/slot/tank");
    public static final Identifier EMPTY_SLOT_PARACHUTE = Identifier.fromNamespaceAndPath(Galacticraft.MODID, "container/slot/parachute");
    public static final Identifier EMPTY_SLOT_TELEMETRY = Identifier.fromNamespaceAndPath(Galacticraft.MODID, "container/slot/telemetry");
    public static final Identifier EMPTY_SLOT_SHIELD = Identifier.fromNamespaceAndPath(Galacticraft.MODID, "container/slot/shield");
    private static final Map<GearEquipmentSlot, Identifier> TEXTURE_EMPTY_SLOTS = Util.makeEnumMap(GearEquipmentSlot.class, slot -> switch (slot) {
        case THERMAL_CAP -> EMPTY_SLOT_THERMAL_HELMET;
        case THERMAL_SHIRT -> EMPTY_SLOT_THERMAL_CHESTPLATE;
        case THERMAL_LEGGINGS -> EMPTY_SLOT_THERMAL_LEGGINGS;
        case THERMAL_SOCKS -> EMPTY_SLOT_THERMAL_BOOTS;
        case MASK -> EMPTY_SLOT_HELMET;
        case GEAR -> EMPTY_SLOT_GEAR;
        case TANK, ADDITIONAL_TANK -> EMPTY_SLOT_TANK;
        case PARACHUTE -> EMPTY_SLOT_PARACHUTE;
        case TELEMETRY -> EMPTY_SLOT_TELEMETRY;
        case SHIELD -> EMPTY_SLOT_SHIELD;
    });
    private static final GearEquipmentSlot[] SLOTS_IDS = GearEquipmentSlot.values();
    public static final Identifier EMPTY_ARMOR_SLOT_HELMET = Identifier.withDefaultNamespace("container/slot/helmet");
    public static final Identifier EMPTY_ARMOR_SLOT_CHESTPLATE = Identifier.withDefaultNamespace("container/slot/chestplate");
    public static final Identifier EMPTY_ARMOR_SLOT_LEGGINGS = Identifier.withDefaultNamespace("container/slot/leggings");
    public static final Identifier EMPTY_ARMOR_SLOT_BOOTS = Identifier.withDefaultNamespace("container/slot/boots");
    private static final Map<EquipmentSlot, Identifier> ARMOR_TEXTURE_EMPTY_SLOTS = Map.of(
            EquipmentSlot.FEET,
            EMPTY_ARMOR_SLOT_BOOTS,
            EquipmentSlot.LEGS,
            EMPTY_ARMOR_SLOT_LEGGINGS,
            EquipmentSlot.CHEST,
            EMPTY_ARMOR_SLOT_CHESTPLATE,
            EquipmentSlot.HEAD,
            EMPTY_ARMOR_SLOT_HELMET
    );
    private static final EquipmentSlot[] ARMOR_SLOT_IDS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    public GearInventoryMenu(int syncId, Inventory playerInventory, Player player) {
        super(GalacticraftMenuType.GEAR.get(), syncId);
        PlayerSpaceData playerData = player.getData(GalacticraftAttachments.PLAYER_SPACE_DATA);
        SpaceGearEquipment gearInventory = playerData.getGearEquipment();

        for (int i = 0; i < 4; i++) {
            GearEquipmentSlot slot = SLOTS_IDS[i];
            Identifier identifier = TEXTURE_EMPTY_SLOTS.get(slot);
            addSlot(new GearSlot(gearInventory, player, slot, identifier, GEAR_SLOT_START + i, 79, 8 + i * 18));
        }

        addSlot(new GearSlot(gearInventory, player, GearEquipmentSlot.MASK, EMPTY_SLOT_HELMET, GEAR_SLOT_START + 4, 125, 26));
        addSlot(new GearSlot(gearInventory, player, GearEquipmentSlot.GEAR, EMPTY_SLOT_GEAR, GEAR_SLOT_START + 5, 125, 44));
        addSlot(new GearSlot(gearInventory, player, GearEquipmentSlot.TANK, EMPTY_SLOT_TANK, GEAR_SLOT_START + 6, 116, 62));
        addSlot(new GearSlot(gearInventory, player, GearEquipmentSlot.ADDITIONAL_TANK, EMPTY_SLOT_TANK, GEAR_SLOT_START + 7, 134, 62));
        addSlot(new GearSlot(gearInventory, player, GearEquipmentSlot.PARACHUTE, EMPTY_SLOT_PARACHUTE, GEAR_SLOT_START + 8, 143, 26));
        addSlot(new GearSlot(gearInventory, player, GearEquipmentSlot.TELEMETRY, EMPTY_SLOT_TELEMETRY, GEAR_SLOT_START + 9, 107, 26));
        addSlot(new GearSlot(gearInventory, player, GearEquipmentSlot.SHIELD, EMPTY_SLOT_SHIELD, GEAR_SLOT_START + 10, 125, 8));

        for (int i = 0; i < 4; i++) {
            EquipmentSlot equipmentslot = ARMOR_SLOT_IDS[i];
            Identifier identifier = ARMOR_TEXTURE_EMPTY_SLOTS.get(equipmentslot);
            addSlot(new ArmorSlot(playerInventory, player, equipmentslot, 39 - i, 61, 8 + i * 18, identifier));
        }
        addStandardInventorySlots(playerInventory, 8, 84);
    }

    public GearInventoryMenu(int syncId, Inventory playerInventory, RegistryFriendlyByteBuf data) {
        this(syncId, playerInventory, playerInventory.player);
    }

    @Override
    public boolean stillValid(Player player) {
        return player.hasData(GalacticraftAttachments.PLAYER_SPACE_DATA);
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
            } else if (gearSlot != null && !this.slots.get(GEAR_SLOT_END - 1 - gearSlot.getIndex()).hasItem()) {
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
