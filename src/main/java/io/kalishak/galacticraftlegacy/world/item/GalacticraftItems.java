package io.kalishak.galacticraftlegacy.world.item;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.transfer.energy.InfiniteEnergyHandler;
import net.neoforged.neoforge.transfer.energy.ItemAccessEnergyHandler;
import net.neoforged.neoforge.transfer.fluid.ItemAccessFluidHandler;

public final class GalacticraftItems {
    private static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(Galacticraft.MODID);

    public static final DeferredItem<BatteryItem> BATTERY = REGISTRY.registerItem(
            "battery",
            BatteryItem::new,
            properties -> properties.stacksTo(1)
    );
    public static final DeferredItem<Item> INFINITE_BATTERY = REGISTRY.registerSimpleItem(
            "infinite_battery",
            properties -> properties
                    .stacksTo(1)
                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
    );
    public static final DeferredItem<Item> THERMAL_CLOTH = REGISTRY.registerSimpleItem("thermal_cloth");
    public static final DeferredItem<GearItem> THERMAL_CAP = REGISTRY.registerItem(
            "thermal_cap",
            GearItem::new,
            () -> GearItem.thermalPiece(GearEquipmentSlot.THERMAL_CAP, GearEquipmentAssets.THIN_THERMAL)
    );
    public static final DeferredItem<GearItem> THERMAL_SHIRT = REGISTRY.registerItem(
            "thermal_shirt",
            GearItem::new,
            () -> GearItem.thermalPiece(GearEquipmentSlot.THERMAL_SHIRT, GearEquipmentAssets.THIN_THERMAL)
    );
    public static final DeferredItem<GearItem> THERMAL_LEGGINGS = REGISTRY.registerItem(
            "thermal_leggings",
            GearItem::new,
            () -> GearItem.thermalPiece(GearEquipmentSlot.THERMAL_LEGGINGS, GearEquipmentAssets.THIN_THERMAL)
    );
    public static final DeferredItem<GearItem> THERMAL_SOCKS = REGISTRY.registerItem(
            "thermal_socks",
            GearItem::new,
            () -> GearItem.thermalPiece(GearEquipmentSlot.THERMAL_SOCKS, GearEquipmentAssets.THIN_THERMAL)
    );
    public static final DeferredItem<GearItem> LIGHT_TANK = REGISTRY.registerItem(
            "light_oxygen_tank",
            GearItem::new,
            GearItem::tankProperties
    );
    public static final DeferredItem<GearItem> MEDIUM_TANK = REGISTRY.registerItem(
            "medium_oxygen_tank",
            GearItem::new,GearItem::tankProperties
    );
    public static final DeferredItem<GearItem> DENSE_TANK = REGISTRY.registerItem(
            "dense_oxygen_tank",
            GearItem::new,
            GearItem::tankProperties
    );
    public static final DeferredItem<GearItem> OXYGEN_MASK = REGISTRY.registerItem(
            "oxygen_mask",
            GearItem::new,
            () -> GearItem.simpleGear(GearEquipmentSlot.MASK)
    );
    public static final DeferredItem<GearItem> OXYGEN_GEAR = REGISTRY.registerItem(
            "oxygen_gear",
            GearItem::new,
            () -> GearItem.simpleGear(GearEquipmentSlot.GEAR)
    );
    public static final DeferredItem<Item> WRENCH = REGISTRY.registerSimpleItem(
            "wrench",
            properties -> properties.durability(256)
    );
    public static final DeferredItem<GearItem> BLACK_PARACHUTE = REGISTRY.registerItem(
            "black_parachute",
            GearItem::new,
            () -> GearItem.parachute(DyeColor.BLACK)
    );
    public static final DeferredItem<GearItem> BLUE_PARACHUTE = REGISTRY.registerItem(
            "blue_parachute",
            GearItem::new,
            () -> GearItem.parachute(DyeColor.BLUE)
    );
    public static final DeferredItem<GearItem> BROWN_PARACHUTE = REGISTRY.registerItem(
            "brown_parachute",
            GearItem::new,
            () -> GearItem.parachute(DyeColor.BROWN)
    );
    public static final DeferredItem<GearItem> CYAN_PARACHUTE = REGISTRY.registerItem(
            "cyan_parachute",
            GearItem::new,
            () -> GearItem.parachute(DyeColor.CYAN)
    );
    public static final DeferredItem<GearItem> GRAY_PARACHUTE = REGISTRY.registerItem(
            "gray_parachute",
            GearItem::new,
            () -> GearItem.parachute(DyeColor.GRAY)
    );
    public static final DeferredItem<GearItem> GREEN_PARACHUTE = REGISTRY.registerItem(
            "green_parachute",
            GearItem::new,
            () -> GearItem.parachute(DyeColor.GREEN)
    );
    public static final DeferredItem<GearItem> LIGHT_BLUE_PARACHUTE = REGISTRY.registerItem(
            "light_blue_parachute",
            GearItem::new,
            () -> GearItem.parachute(DyeColor.LIGHT_BLUE)
    );
    public static final DeferredItem<GearItem> LIGHT_GRAY_PARACHUTE = REGISTRY.registerItem(
            "light_gray_parachute",
            GearItem::new,
            () -> GearItem.parachute(DyeColor.LIGHT_GRAY)
    );
    public static final DeferredItem<GearItem> LIME_PARACHUTE = REGISTRY.registerItem(
            "lime_parachute",
            GearItem::new,
            () -> GearItem.parachute(DyeColor.LIME)
    );
    public static final DeferredItem<GearItem> MAGENTA_PARACHUTE = REGISTRY.registerItem(
            "magenta_parachute",
            GearItem::new,
            () -> GearItem.parachute(DyeColor.MAGENTA)
    );
    public static final DeferredItem<GearItem> ORANGE_PARACHUTE = REGISTRY.registerItem(
            "orange_parachute",
            GearItem::new,
            () -> GearItem.parachute(DyeColor.ORANGE)
    );
    public static final DeferredItem<GearItem> PINK_PARACHUTE = REGISTRY.registerItem(
            "pink_parachute",
            GearItem::new,
            () -> GearItem.parachute(DyeColor.PINK)
    );
    public static final DeferredItem<GearItem> PURPLE_PARACHUTE = REGISTRY.registerItem(
            "purple_parachute",
            GearItem::new,
            () -> GearItem.parachute(DyeColor.PURPLE)
    );
    public static final DeferredItem<GearItem> RED_PARACHUTE = REGISTRY.registerItem(
            "red_parachute",
            GearItem::new,
            () -> GearItem.parachute(DyeColor.RED)
    );
    public static final DeferredItem<GearItem> WHITE_PARACHUTE = REGISTRY.registerItem(
            "white_parachute",
            GearItem::new,
            () -> GearItem.parachute(DyeColor.WHITE)
    );
    public static final DeferredItem<GearItem> YELLOW_PARACHUTE = REGISTRY.registerItem(
            "yellow_parachute",
            GearItem::new,
            () -> GearItem.parachute(DyeColor.YELLOW)
    );

    public static final DeferredItem<BlockItem> COAL_GENERATOR = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.COAL_GENERATOR);
    public static final DeferredItem<BlockItem> CIRCUIT_FABRICATOR = REGISTRY.registerSimpleBlockItem(GalacticraftBlocks.CIRCUIT_FABRICATOR);

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
        bus.addListener(GalacticraftItems::registerItemCapabilities);
    }

    private static void registerItemCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(
                Capabilities.Energy.ITEM,
                (item, cxt) -> new ItemAccessEnergyHandler(cxt, GalacticraftDataComponents.STORED_ENERGY.get(), 8000, 25),
                BATTERY
        );
        event.registerItem(
                Capabilities.Energy.ITEM,
                (item, cxt) -> InfiniteEnergyHandler.INSTANCE,
                INFINITE_BATTERY
        );
        event.registerItem(
                Capabilities.Fluid.ITEM,
                (item, cxt) -> new ItemAccessFluidHandler(cxt, GalacticraftDataComponents.OXYGEN_TANK.get(), 12000),
                LIGHT_TANK
        );
        event.registerItem(
                Capabilities.Fluid.ITEM,
                (item, cxt) -> new ItemAccessFluidHandler(cxt, GalacticraftDataComponents.OXYGEN_TANK.get(), 24000),
                MEDIUM_TANK
        );
        event.registerItem(
                Capabilities.Fluid.ITEM,
                (item, cxt) -> new ItemAccessFluidHandler(cxt, GalacticraftDataComponents.OXYGEN_TANK.get(), 36000),
                DENSE_TANK
        );
    }
}
