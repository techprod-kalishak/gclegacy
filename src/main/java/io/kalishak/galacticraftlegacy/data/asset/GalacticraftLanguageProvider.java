package io.kalishak.galacticraftlegacy.data.asset;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class GalacticraftLanguageProvider extends LanguageProvider {
    public GalacticraftLanguageProvider(PackOutput output) {
        super(output, Galacticraft.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("item.galacticraftlegacy.tank.tooltip", "Oxygen Left: ");
        add("galacticraftlegacy.networking_failed", "There was an error on the network thread: ");
        add("item.galacticraftlegacy.battery.tooltip", "Energy Stored: %s");
        add("item.galacticraftlegact.fluid_tank.empty", "Tank is empty");
        add("item.galacticraftlegacy.fluid_tank.tooltip", "Fluid in tank %s: %s");
        add("itemGroup.galacticraftlegacy.blocks", "Galacticraft Legacy: Blocks");
        add("itemGroup.galacticraftlegacy.items", "Galacticraft Legacy: Items");

        add("container.coal_generator.generating", "Generating");
        add("container.coal_generator.not_generating", "Not generating");
        add("container.coal_generator.heat_level", "Heat: %s");
        add("block.galacticraftlegacy.oxygen", "Oxygen");
        add("item.galacticraftlegacy.infinite", "Infinite");

        addBlock(GalacticraftBlocks.COAL_GENERATOR, "Coal Generator");
        addBlock(GalacticraftBlocks.CIRCUIT_FABRICATOR, "Circuit Fabricator");

        addItem(GalacticraftItems.BATTERY, "Battery");
        addItem(GalacticraftItems.INFINITE_BATTERY, "Battery");
        addItem(GalacticraftItems.THERMAL_CLOTH, "Thermal Cloth");
        addItem(GalacticraftItems.THERMAL_CAP, "Thermal Cap");
        addItem(GalacticraftItems.THERMAL_SHIRT, "Thermal Shirt");
        addItem(GalacticraftItems.THERMAL_LEGGINGS, "Thermal Leggings");
        addItem(GalacticraftItems.THERMAL_SOCKS, "Thermal Socks");
        addItem(GalacticraftItems.LIGHT_TANK, "Light Oxygen Tank");
        addItem(GalacticraftItems.MEDIUM_TANK, "Medium Oxygen Tank");
        addItem(GalacticraftItems.DENSE_TANK, "Heavy Oxygen Tank");
        addItem(GalacticraftItems.OXYGEN_MASK, "Oxygen Mask");
        addItem(GalacticraftItems.OXYGEN_GEAR, "Oxygen Gear");
        addItem(GalacticraftItems.BLACK_PARACHUTE, "Black Parachute");
        addItem(GalacticraftItems.BLUE_PARACHUTE,  "Blue Parachute");
        addItem(GalacticraftItems.BROWN_PARACHUTE,  "Brown Parachute");
        addItem(GalacticraftItems.CYAN_PARACHUTE,  "Cyan Parachute");
        addItem(GalacticraftItems.GRAY_PARACHUTE,  "Gray Parachute");
        addItem(GalacticraftItems.GREEN_PARACHUTE,  "Green Parachute");
        addItem(GalacticraftItems.LIGHT_BLUE_PARACHUTE,  "Light Blue Parachute");
        addItem(GalacticraftItems.LIGHT_GRAY_PARACHUTE,  "Light Gray Parachute");
        addItem(GalacticraftItems.LIME_PARACHUTE,  "Lime Parachute");
        addItem(GalacticraftItems.MAGENTA_PARACHUTE,  "Magenta Parachute");
        addItem(GalacticraftItems.ORANGE_PARACHUTE,  "Orange Parachute");
        addItem(GalacticraftItems.PINK_PARACHUTE,  "Pink Parachute");
        addItem(GalacticraftItems.PURPLE_PARACHUTE,  "Purple Parachute");
        addItem(GalacticraftItems.RED_PARACHUTE,  "Red Parachute");
        addItem(GalacticraftItems.WHITE_PARACHUTE,  "White Parachute");
        addItem(GalacticraftItems.YELLOW_PARACHUTE,  "Yellow Parachute");

        add(GalacticraftTags.Blocks.MACHINE, "Machine");
        add(GalacticraftTags.Blocks.MACHINE_BASIC, "Basic machine");
        add(GalacticraftTags.Blocks.MACHINE_ADVANCED, "Advanced machine");
        add(GalacticraftTags.EntityTypes.CAN_EQUIP_PARACHUTE, "Can equip parachute");
        add(GalacticraftTags.Items.PARACHUTE, "Parachute");
        add(GalacticraftTags.Items.WRENCH, "Wrench");
    }
}
