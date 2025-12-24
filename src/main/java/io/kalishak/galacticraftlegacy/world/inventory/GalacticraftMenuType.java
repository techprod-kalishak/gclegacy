package io.kalishak.galacticraftlegacy.world.inventory;

import io.kalishak.galacticraftlegacy.Galacticraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jspecify.annotations.NonNull;

public final class GalacticraftMenuType {
    private static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU, Galacticraft.MODID);

    public static final DeferredHolder<MenuType<?>, @NonNull MenuType<GearInventoryMenu>> GEAR = REGISTRY.register("gear", () -> IMenuTypeExtension.create(GearInventoryMenu::new));
    public static final DeferredHolder<MenuType<?>, @NonNull MenuType<CircuitFabricatorMenu>> CIRCUIT_FABRICATOR = REGISTRY.register("circuit_fabricator", () -> IMenuTypeExtension.create(CircuitFabricatorMenu::new));
    public static final DeferredHolder<MenuType<?>, @NonNull MenuType<CoalGeneratorMenu>> COAL_GENERATOR = REGISTRY.register("coal_generator", () -> IMenuTypeExtension.create(CoalGeneratorMenu::new));

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
