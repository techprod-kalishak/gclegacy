package io.kalishak.galacticraftlegacy.world.inventory;

import io.kalishak.galacticraftlegacy.GalacticraftLegacy;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jspecify.annotations.NonNull;

public final class GalacticraftLegacyMenuType {
    private static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU, GalacticraftLegacy.MODID);

    public static final DeferredHolder<MenuType<?>, @NonNull MenuType<GearInventoryMenu>> GEAR = REGISTRY.register("gear", () -> IMenuTypeExtension.create(GearInventoryMenu::new));

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
