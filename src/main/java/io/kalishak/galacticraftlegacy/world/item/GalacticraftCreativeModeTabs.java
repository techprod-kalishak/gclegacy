package io.kalishak.galacticraftlegacy.world.item;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.registry.SchematicVariant;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.item.component.SchematicContent;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import io.kalishak.galacticraftlegacy.world.level.material.fluid.GalacticraftFluids;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.EitherHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

import java.util.Comparator;

public final class GalacticraftCreativeModeTabs {
    private static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Galacticraft.MODID);
    private static final Comparator<Holder<SchematicVariant>> SCHEMATIC_SORTER = Comparator.comparing(
            Holder::value, Comparator.comparingInt(schematic -> schematic.tier().getLevel())
    );

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
        output.accept(GalacticraftItems.BATTERY);
        emptyAndCharged(output, GalacticraftItems.BATTERY);
        output.accept(GalacticraftItems.INFINITE_BATTERY);
        output.accept(GalacticraftItems.THERMAL_PADDING_HELM);
        output.accept(GalacticraftItems.THERMAL_PADDING_CHESTPIECE);
        output.accept(GalacticraftItems.THERMAL_PADDING_LEGGINGS);
        output.accept(GalacticraftItems.THERMAL_PADDING_BOOTS);
        output.accept(GalacticraftItems.ISOTHERMAL_HELM);
        output.accept(GalacticraftItems.ISOTHERMAL_CHESTPIECE);
        output.accept(GalacticraftItems.ISOTHERMAL_LEGGINGS);
        output.accept(GalacticraftItems.ISOTHERMAL_BOOTS);
        output.accept(GalacticraftItems.THERMAL_WOLF_JACKET);
        output.accept(GalacticraftItems.OXYGEN_MASK);
        output.accept(GalacticraftItems.OXYGEN_GEAR);
        emptyAndFilled(output, GalacticraftItems.LIGHT_TANK, GalacticraftFluids.OXYGEN.get());
        emptyAndFilled(output, GalacticraftItems.MEDIUM_TANK, GalacticraftFluids.OXYGEN.get());
        emptyAndFilled(output, GalacticraftItems.HEAVY_TANK, GalacticraftFluids.OXYGEN.get());
        output.accept(GalacticraftItems.INFINITE_OXYGEN_TANK);
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
        output.accept(GalacticraftItems.YELLOW_PARACHUTE);
        output.accept(GalacticraftItems.SENSOR_GLASSES);
        output.accept(GalacticraftItems.FLAG);
        itemDisplayParameters.holders().lookup(GalacticraftRegistries.Keys.SCHEMATIC).ifPresent(registry -> {
            generateSchematics(output, itemDisplayParameters.holders(), registry);
        });
        output.accept(GalacticraftItems.WRENCH);
        output.accept(GalacticraftItems.RAW_SILICON);
        output.accept(GalacticraftItems.RAW_STEEL);
        output.accept(GalacticraftItems.STEEL_INGOT);
        output.accept(GalacticraftItems.STEEL_NUGGET);
        output.accept(GalacticraftItems.STEEL_SWORD);
        output.accept(GalacticraftItems.STEEL_SPEAR);
        output.accept(GalacticraftItems.STEEL_SHOVEL);
        output.accept(GalacticraftItems.STEEL_PICKAXE);
        output.accept(GalacticraftItems.STEEL_AXE);
        output.accept(GalacticraftItems.STEEL_HOE);
        output.accept(GalacticraftItems.STEEL_HELMET);
        output.accept(GalacticraftItems.STEEL_CHESTPLATE);
        output.accept(GalacticraftItems.STEEL_LEGGINGS);
        output.accept(GalacticraftItems.STEEL_BOOTS);
        output.accept(GalacticraftItems.STEEL_HORSE_ARMOR);
        output.accept(GalacticraftItems.STEEL_NAUTILUS_ARMOR);
        output.accept(GalacticraftItems.RAW_DESH);
        output.accept(GalacticraftItems.DESH_INGOT);
        output.accept(GalacticraftItems.DESH_NUGGET);
        output.accept(GalacticraftItems.DESH_SWORD);
        output.accept(GalacticraftItems.DESH_SPEAR);
        output.accept(GalacticraftItems.DESH_SHOVEL);
        output.accept(GalacticraftItems.DESH_PICKAXE);
        output.accept(GalacticraftItems.DESH_AXE);
        output.accept(GalacticraftItems.DESH_HOE);
        output.accept(GalacticraftItems.DESH_HELMET);
        output.accept(GalacticraftItems.DESH_CHESTPLATE);
        output.accept(GalacticraftItems.DESH_LEGGINGS);
        output.accept(GalacticraftItems.DESH_BOOTS);
        output.accept(GalacticraftItems.RAW_TITANIUM);
        output.accept(GalacticraftItems.TITANIUM_INGOT);
        output.accept(GalacticraftItems.TITANIUM_NUGGET);
        output.accept(GalacticraftItems.TITANIUM_SWORD);
        output.accept(GalacticraftItems.TITANIUM_SPEAR);
        output.accept(GalacticraftItems.TITANIUM_SHOVEL);
        output.accept(GalacticraftItems.TITANIUM_PICKAXE);
        output.accept(GalacticraftItems.TITANIUM_AXE);
        output.accept(GalacticraftItems.TITANIUM_HOE);
        output.accept(GalacticraftItems.TITANIUM_HELMET);
        output.accept(GalacticraftItems.TITANIUM_CHESTPLATE);
        output.accept(GalacticraftItems.TITANIUM_LEGGINGS);
        output.accept(GalacticraftItems.TITANIUM_BOOTS);
        output.accept(GalacticraftItems.RAW_LEAD);
        output.accept(GalacticraftItems.LEAD_INGOT);
        output.accept(GalacticraftItems.LEAD_NUGGET);
        output.accept(GalacticraftItems.CHEESE_CHUNK);
        output.accept(GalacticraftItems.BASIC_WAFER);
        output.accept(GalacticraftItems.ADVANCED_WAFER);
        output.accept(GalacticraftItems.SOLAR_WAFER);
        output.accept(GalacticraftItems.FLUID_TANK);
        emptyAndFilled(output, GalacticraftItems.FLUID_TANK, GalacticraftFluids.OXYGEN.get());
        emptyAndFilled(output, GalacticraftItems.FLUID_TANK, GalacticraftFluids.OIL.get());
        emptyAndFilled(output, GalacticraftItems.FLUID_TANK, GalacticraftFluids.FUEL.get());
        output.accept(GalacticraftItems.OIL_BUCKET);
        output.accept(GalacticraftItems.FUEL_BUCKET);
    }

    private static void buildBlocks(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
        output.accept(GalacticraftBlocks.COAL_GENERATOR);
        output.accept(GalacticraftBlocks.CIRCUIT_FABRICATOR);
        output.accept(GalacticraftBlocks.ELECTRIC_FURNACE);
    }

    private static void emptyAndCharged(CreativeModeTab.Output output, ItemLike item) {
        ItemStack stack = new ItemStack(item);
        EnergyHandler energyHandler = stack.getCapability(Capabilities.Energy.ITEM, ItemAccess.forStack(stack));

        if (energyHandler != null) {
            stack.set(GalacticraftDataComponents.STORED_ENERGY, energyHandler.getCapacityAsInt());
            output.accept(stack);
        }
    }

    private static void emptyAndFilled(CreativeModeTab.Output output, ItemLike item, Fluid fluid) {
        ItemStack stack = new ItemStack(item);
        ResourceHandler<FluidResource> fluidHandler = stack.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(stack));

        if (fluidHandler != null) {
            FluidStack fluidStack = new FluidStack(fluid, fluidHandler.getCapacityAsInt(0, FluidResource.of(fluid)));
            stack.set(GalacticraftDataComponents.FLUID_TANK, SimpleFluidContent.copyOf(fluidStack));
            output.accept(stack);
        }
    }

    private static void generateSchematics(CreativeModeTab.Output output, HolderLookup.Provider registries, HolderLookup.RegistryLookup<SchematicVariant> schematicLookup) {
        RegistryOps<Tag> registryops = registries.createSerializationContext(NbtOps.INSTANCE);
        schematicLookup.listElements().sorted(SCHEMATIC_SORTER).forEach(holder -> {
            ItemStack itemstack = GalacticraftItems.SCHEMATIC.toStack();
            itemstack.set(GalacticraftDataComponents.SCHEMATIC, new SchematicContent(new EitherHolder<>(holder)));
            output.accept(itemstack);
        });
    }
}
