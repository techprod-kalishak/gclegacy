package io.kalishak.galacticraftlegacy.world.inventory;

import io.kalishak.galacticraftlegacy.GalacticraftLegacy;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractCraftingMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Map;

public class GearInventoryMenu extends AbstractCraftingMenu {
    public static final int RESULT_SLOT = 0;
    private static final int CRAFTING_GRID_WIDTH = 2;
    private static final int CRAFTING_GRID_HEIGHT = 2;
    public static final int CRAFT_SLOT_START = 1;
    public static final int CRAFT_SLOT_COUNT = 4;
    public static final int CRAFT_SLOT_END = 5;
    public static final int INV_SLOT_START = 9;
    public static final int INV_SLOT_END = 36;
    public static final int USE_ROW_SLOT_START = 36;
    public static final int USE_ROW_SLOT_END = 45;
    public static final Identifier EMPTY_ARMOR_SLOT_HELMET = Identifier.fromNamespaceAndPath(GalacticraftLegacy.MODID, "container/slot/helmet");
    public static final Identifier EMPTY_ARMOR_SLOT_GEAR = Identifier.fromNamespaceAndPath(GalacticraftLegacy.MODID, "container/slot/gear");
    public static final Identifier EMPTY_ARMOR_SLOT_TANK = Identifier.fromNamespaceAndPath(GalacticraftLegacy.MODID, "container/slot/tank");
    public static final Identifier EMPTY_ARMOR_SLOT_PARACHUTE = Identifier.fromNamespaceAndPath(GalacticraftLegacy.MODID, "container/slot/parachute");
    public static final Identifier EMPTY_ARMOR_SLOT_TELEMETRY = Identifier.fromNamespaceAndPath(GalacticraftLegacy.MODID, "container/slot/telemetry");
    public static final Identifier EMPTY_ARMOR_SLOT_THERMAL_HELMET = Identifier.fromNamespaceAndPath(GalacticraftLegacy.MODID, "container/slot/helmet");
    public static final Identifier EMPTY_ARMOR_SLOT_THERMAL_CHESTPLATE = Identifier.fromNamespaceAndPath(GalacticraftLegacy.MODID, "container/slot/chestplate");
    public static final Identifier EMPTY_ARMOR_SLOT_THERMAL_LEGGINGS = Identifier.fromNamespaceAndPath(GalacticraftLegacy.MODID, "container/slot/leggings");
    public static final Identifier EMPTY_ARMOR_SLOT_THERMAL_BOOTS = Identifier.fromNamespaceAndPath(GalacticraftLegacy.MODID, "container/slot/boots");
    private static final Map<GearEquipmentSlot, Identifier> TEXTURE_EMPTY_SLOTS = Util.makeEnumMap(GearEquipmentSlot.class, slot -> switch (slot) {
        case HELMET -> EMPTY_ARMOR_SLOT_HELMET;
        case GEAR -> EMPTY_ARMOR_SLOT_GEAR;
        case LEFT_TANK, RIGHT_TANK -> EMPTY_ARMOR_SLOT_TANK;
        case PARACHUTE -> EMPTY_ARMOR_SLOT_PARACHUTE;
        case TELEMETRY -> EMPTY_ARMOR_SLOT_TELEMETRY;
        case THERMAL_HEAD -> EMPTY_ARMOR_SLOT_THERMAL_HELMET;
        case THERMAL_CHEST -> EMPTY_ARMOR_SLOT_THERMAL_CHESTPLATE;
        case THERMAL_LEG -> EMPTY_ARMOR_SLOT_THERMAL_LEGGINGS;
        case THERMAL_FOOT -> EMPTY_ARMOR_SLOT_THERMAL_BOOTS;
    });
    private static final GearEquipmentSlot[] SLOTS_IDS = GearEquipmentSlot.values();
    private final Player owner;

    public GearInventoryMenu(int syncId, Inventory playerInventory) {
        super(GalacticraftLegacyMenuType.GEAR.get(), syncId, CRAFTING_GRID_WIDTH, CRAFTING_GRID_HEIGHT);
        this.owner = playerInventory.player;
        addResultSlot(this.owner, 154, 28);
        addCraftingGridSlots(98, 18);


    }

    public GearInventoryMenu(int syncId, Inventory playerInventory, RegistryFriendlyByteBuf data) {
        this(syncId, playerInventory);
    }

    @Override
    public Slot getResultSlot() {
        return null;
    }

    @Override
    public List<Slot> getInputGridSlots() {
        return List.of();
    }

    @Override
    protected Player owner() {
        return this.owner;
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.CRAFTING;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }
}
