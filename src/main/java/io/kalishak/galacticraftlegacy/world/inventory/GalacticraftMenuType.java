/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.inventory;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.world.inventory.machine.*;
import io.kalishak.galacticraftlegacy.world.inventory.magnetic_crafting.MagneticCraftingMenu;
import io.kalishak.galacticraftlegacy.world.inventory.workbench.NasaWorkbenchEmptyPageMenu;
import io.kalishak.galacticraftlegacy.world.inventory.workbench.NasaWorkbenchMenu;
import io.kalishak.galacticraftlegacy.world.inventory.workbench.NasaWorkbenchPageMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class GalacticraftMenuType {
    private static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU, Galacticraft.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<CircuitFabricatorMenu>> CIRCUIT_FABRICATOR = REGISTRY.register("circuit_fabricator", () -> IMenuTypeExtension.create(CircuitFabricatorMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<CoalGeneratorMenu>> COAL_GENERATOR = REGISTRY.register("coal_generator", () -> IMenuTypeExtension.create(CoalGeneratorMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<CompressorMenu>> COMPRESSOR = REGISTRY.register("compressor", () -> IMenuTypeExtension.create(CompressorMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<DungeonChestMenu>> DUNGEON_CHEST = REGISTRY.register("refinery", () -> IMenuTypeExtension.create(DungeonChestMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<ElectricArcFurnaceMenu>> ELECTRIC_ARC_FURNACE = REGISTRY.register("electric_arc_furnace", () -> IMenuTypeExtension.create(ElectricArcFurnaceMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<ElectricCompressorMenu>> ELECTRIC_COMPRESSOR = REGISTRY.register("electric_compressor", () -> IMenuTypeExtension.create(ElectricCompressorMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<ElectricFurnaceMenu>> ELECTRIC_FURNACE = REGISTRY.register("electric_furnace", () -> IMenuTypeExtension.create(ElectricFurnaceMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<GearInventoryMenu>> GEAR = REGISTRY.register("gear", () -> IMenuTypeExtension.create(GearInventoryMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<MagneticCraftingMenu>> MAGNETIC_CRAFTING = REGISTRY.register("magnetic_crafting_table", () -> IMenuTypeExtension.create(MagneticCraftingMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<NasaWorkbenchMenu>> NASA_WORKBENCH = REGISTRY.register("nasa_workbench", () -> IMenuTypeExtension.create(NasaWorkbenchMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<NasaWorkbenchPageMenu>> NASA_WORKBENCH_PAGE = REGISTRY.register("nasa_workbench_page", () -> IMenuTypeExtension.create(NasaWorkbenchPageMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<NasaWorkbenchEmptyPageMenu>> NASA_WORKBENCH_EMPTY_PAGE = REGISTRY.register("nasa_workbench_empty_page", () -> IMenuTypeExtension.create(NasaWorkbenchEmptyPageMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<ParachestMenu>> PARACHEST = REGISTRY.register("parachest", () -> IMenuTypeExtension.create(ParachestMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<OxygenCollectorMenu>> OXYGEN_COLLECTOR = REGISTRY.register("oxygen_collector", () -> IMenuTypeExtension.create(OxygenCollectorMenu::new));

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
