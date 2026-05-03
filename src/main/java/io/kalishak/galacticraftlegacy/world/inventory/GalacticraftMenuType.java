/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.inventory;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.world.inventory.machine.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class GalacticraftMenuType {
    private static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU, Galacticraft.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<ElectricFurnaceMenu>> ARC_FURNACE = REGISTRY.register("arc_furnace", () -> IMenuTypeExtension.create(ElectricFurnaceMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<GearInventoryMenu>> GEAR = REGISTRY.register("gear", () -> IMenuTypeExtension.create(GearInventoryMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<CircuitFabricatorMenu>> CIRCUIT_FABRICATOR = REGISTRY.register("circuit_fabricator", () -> IMenuTypeExtension.create(CircuitFabricatorMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<CoalGeneratorMenu>> COAL_GENERATOR = REGISTRY.register("coal_generator", () -> IMenuTypeExtension.create(CoalGeneratorMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<CompressorMenu>> COMPRESSOR = REGISTRY.register("compressor", () -> IMenuTypeExtension.create(CompressorMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<DungeonChestMenu>> DUNGEON_CHEST = REGISTRY.register("refinery", () -> IMenuTypeExtension.create(DungeonChestMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<ElectricCompressorMenu>> ELECTRIC_COMPRESSOR = REGISTRY.register("electric_compressor", () -> IMenuTypeExtension.create(ElectricCompressorMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<ParachestMenu>> PARACHEST = REGISTRY.register("parachest", () -> IMenuTypeExtension.create(ParachestMenu::new));

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
