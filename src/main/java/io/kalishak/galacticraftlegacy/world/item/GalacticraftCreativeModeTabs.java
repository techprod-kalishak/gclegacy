package io.kalishak.galacticraftlegacy.world.item;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import io.kalishak.galacticraftlegacy.world.level.material.fluid.GalacticraftFluids;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public final class GalacticraftCreativeModeTabs {
    private static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Galacticraft.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ITEMS = REGISTRY.register(
            "items",
            CreativeModeTab.builder()
                    .icon(GalacticraftItems.OXYGEN_MASK::toStack)
                    .displayItems(GalacticraftCreativeModeTabs::buildItems)
                    .title(Component.translatable("itemGroup.galacticraftlegacy.items"))
                    ::build
    );

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BLOCKS = REGISTRY.register(
            "blocks",
            CreativeModeTab.builder()
                    .icon(GalacticraftBlocks.COAL_GENERATOR::toStack)
                    .displayItems(GalacticraftCreativeModeTabs::buildBlocks)
                    .title(Component.translatable("itemGroup.galacticraftlegacy.blocks"))
                    ::build
    );

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }

    private static void buildItems(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
        output.accept(charged(GalacticraftItems.BATTERY));

        output.accept(GalacticraftItems.THERMAL_CAP);
        output.accept(GalacticraftItems.THERMAL_SHIRT);
        output.accept(GalacticraftItems.THERMAL_LEGGINGS);
        output.accept(GalacticraftItems.THERMAL_SOCKS);
        output.accept(GalacticraftItems.OXYGEN_MASK);
        output.accept(GalacticraftItems.OXYGEN_GEAR);

        output.accept(filled(GalacticraftItems.LIGHT_TANK, GalacticraftFluids.OXYGEN.get()));
        output.accept(filled(GalacticraftItems.MEDIUM_TANK, GalacticraftFluids.OXYGEN.get()));
        output.accept(filled(GalacticraftItems.DENSE_TANK, GalacticraftFluids.OXYGEN.get()));

        output.accept(GalacticraftItems.BLACK_PARACHUTE);
        output.accept(GalacticraftItems.BLUE_PARACHUTE);
        output.accept(GalacticraftItems.BROWN_PARACHUTE);
        output.accept(GalacticraftItems.CYAN_PARACHUTE);
        output.accept(GalacticraftItems.GRAY_PARACHUTE);
        output.accept(GalacticraftItems.LIGHT_BLUE_PARACHUTE);
        output.accept(GalacticraftItems.LIGHT_GRAY_PARACHUTE);
        output.accept(GalacticraftItems.LIME_PARACHUTE);
        output.accept(GalacticraftItems.MAGENTA_PARACHUTE);
        output.accept(GalacticraftItems.ORANGE_PARACHUTE);
        output.accept(GalacticraftItems.PINK_PARACHUTE);
        output.accept(GalacticraftItems.PURPLE_PARACHUTE);
        output.accept(GalacticraftItems.RED_PARACHUTE);
        output.accept(GalacticraftItems.WHITE_PARACHUTE);
        output.accept(GalacticraftItems.YELLOW_PARACHUTE);;
    }

    private static void buildBlocks(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
        output.accept(GalacticraftBlocks.COAL_GENERATOR);
    }

    private static ItemStack charged(ItemLike item) {
        ItemStack stack = new ItemStack(item);
        EnergyHandler energyHandler = stack.getCapability(Capabilities.Energy.ITEM, ItemAccess.forStack(stack));

        if (energyHandler != null) {
            try (Transaction tx = Transaction.open(null)) {
                int capacity = energyHandler.getCapacityAsInt();

                if (energyHandler.insert(capacity, tx) > 0) {
                    tx.commit();
                }
            }
        }

        return stack;
    }

    private static ItemStack filled(ItemLike item, Fluid fluid) {
        ItemStack stack = new ItemStack(item);
        ResourceHandler<FluidResource> fluidHandler = stack.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(stack));

        if (fluidHandler != null) {
            try (Transaction tx = Transaction.open(null)) {
                FluidResource resource = FluidResource.of(fluid);
                int capacity = fluidHandler.getCapacityAsInt(0, resource);

                if (fluidHandler.insert(resource, capacity, tx) == capacity) {
                    tx.commit();
                }
            }
        }

        return stack;
    }
}
