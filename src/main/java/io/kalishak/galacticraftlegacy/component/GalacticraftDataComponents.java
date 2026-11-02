/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.component;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.component.flag.FlagData;
import io.kalishak.galacticraftlegacy.config.ClientConfig;
import io.kalishak.galacticraftlegacy.config.values.EnergyUnit;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.world.inventory.container.memory.CraftingMemory;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import io.kalishak.galacticraftlegacy.world.item.KeyLock;
import io.kalishak.galacticraftlegacy.world.item.VehicleComponentType;
import io.kalishak.galacticraftlegacy.world.item.component.*;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.tooltip.TooltipAppender;
import net.neoforged.neoforge.common.tooltip.TooltipLocation;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.event.RegisterTooltipAppendersEvent;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public final class GalacticraftDataComponents {
    private static final DeferredRegister.DataComponents REGISTRY = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Galacticraft.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CannedFood.CannedComponent>> CANNED_FOOD = REGISTRY.registerComponentType(
            "canned_food",
            builder -> builder.persistent(CannedFood.CannedComponent.CODEC).networkSynchronized(CannedFood.CannedComponent.STREAM_CODEC).cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CraftingMemory>> CRAFTING_MEMORY = REGISTRY.registerComponentType(
            "crafting_memory",
            builder -> builder.persistent(CraftingMemory.CODEC).networkSynchronized(CraftingMemory.STREAM_CODEC).cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<PlayerOwnable>> PLAYER_OWNABLE = REGISTRY.registerComponentType(
            "player_ownable",
            builder -> builder.persistent(PlayerOwnable.CODEC).networkSynchronized(PlayerOwnable.STREAM_CODEC).cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<FlagData>> FLAG_DATA = REGISTRY.registerComponentType(
            "flag_data",
            builder -> builder.persistent(FlagData.CODEC).networkSynchronized(FlagData.STREAM_CODEC)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SimpleFluidContent>> FLUID_TANK = REGISTRY.registerComponentType(
            "fluid_tank",
            builder -> builder.persistent(SimpleFluidContent.CODEC).networkSynchronized(SimpleFluidContent.STREAM_CODEC)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<FluidTankContents>> FLUID_TANK_CONTENTS = REGISTRY.registerComponentType(
            "fluid_tank_contents",
            builder -> builder.persistent(FluidTankContents.CODEC).networkSynchronized(FluidTankContents.STREAM_CODEC).cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<GearAttributeModifiers>> GEAR_ATTRIBUTE_MODIFIERS = REGISTRY.registerComponentType(
            "gear_attribute_modifiers",
            builder -> builder.persistent(GearAttributeModifiers.CODEC).networkSynchronized(GearAttributeModifiers.STREAM_CODEC).cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<GearEquippable>> GEAR_EQUIPPABLE = REGISTRY.registerComponentType(
            "gear_equippable",
            builder -> builder.persistent(GearEquippable.CODEC).networkSynchronized(GearEquippable.STREAM_CODEC).cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<HotContent>> HOT_CONTENT = REGISTRY.registerComponentType(
            "hot_content",
            builder -> builder.persistent(HotContent.CODEC).networkSynchronized(HotContent.STREAM_CODEC)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemWithDescription>> ITEM_WITH_DESCRIPTION = REGISTRY.registerComponentType(
            "item_with_description",
            builder -> builder.persistent(ItemWithDescription.CODEC).networkSynchronized(ItemWithDescription.STREAM_CODEC).cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<KeyLock>> KEY_LOCK = REGISTRY.registerComponentType(
            "key_lock",
            builder -> builder.persistent(KeyLock.CODEC).networkSynchronized(KeyLock.STREAM_CODEC).cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<VehiclePart>> ROCKET_PART = REGISTRY.registerComponentType(
            "rocket_part",
            builder -> builder.persistent(VehiclePart.CODEC).networkSynchronized(VehiclePart.STREAM_CODEC).cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Holder<SchematicVariant>>> SCHEMATIC = REGISTRY.registerComponentType(
            "schematic",
            builder -> builder.persistent(SchematicVariant.CODEC).networkSynchronized(SchematicVariant.STREAM_CODEC).ignoreSwapAnimation()
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ShieldController>> SHIELD_CONTROLLER = REGISTRY.registerComponentType(
            "shield_controller",
            builder -> builder.persistent(ShieldController.CODEC).networkSynchronized(ShieldController.STREAM_CODEC)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> STORED_ENERGY = REGISTRY.registerComponentType(
            "stored_energy",
            builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<GlobalPos>> STRUCTURE_POS = REGISTRY.registerComponentType(
            "structure_pos",
            builder -> builder.persistent(GlobalPos.CODEC).networkSynchronized(GlobalPos.STREAM_CODEC)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> VEHICLE_STORAGE = REGISTRY.registerComponentType(
            "vehicle_storage",
            builder -> builder.persistent(ExtraCodecs.intRange(0, 4)).networkSynchronized(ByteBufCodecs.INT)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Vec3>> BODY_POSITION = REGISTRY.registerComponentType(
            "body_position",
            builder -> builder.persistent(Vec3.CODEC).networkSynchronized(Vec3.STREAM_CODEC)
    );

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
        bus.addListener(GalacticraftDataComponents::modifyDefaultComponents);
        bus.addListener(GalacticraftDataComponents::appendComponentTooltip);
    }

    @SubscribeEvent
    public static void modifyDefaultComponents(ModifyDefaultComponentsEvent event) {
        final VehiclePart storage = new VehiclePart(VehicleComponentType.STORAGE, List.of(FeatureTier.values()));

        event.modify(
                Items.CHEST,
                (components, _, _) -> components.set(GalacticraftDataComponents.ROCKET_PART, storage)
        );
        Items.COPPER_CHEST.forEach(item -> event.modify(
                item,
                (components, _, _) -> components.set(GalacticraftDataComponents.ROCKET_PART, storage)
        ));
    }

    @SubscribeEvent
    public static void appendComponentTooltip(RegisterTooltipAppendersEvent event) {
        appendIfPresent(event::registerAppender, TooltipLocation.HEAD, GalacticraftDataComponents.PLAYER_OWNABLE);
        appendIfPresent(event::registerAppender, TooltipLocation.TAIL, GalacticraftDataComponents.ITEM_WITH_DESCRIPTION);
        appendIfPresent(event::registerAppender, TooltipLocation.HEAD, GalacticraftDataComponents.CRAFTING_MEMORY);
        appendIfPresent(event::registerAppender, TooltipLocation.HEAD, GalacticraftDataComponents.FLAG_DATA);
        appendIfPresent(event::registerAppender, TooltipLocation.HEAD, GalacticraftDataComponents.FLUID_TANK_CONTENTS);
        appendIfPresent(event::registerAppender, TooltipLocation.HEAD, GalacticraftDataComponents.HOT_CONTENT);
        appendIfHolderPresent(event::registerAppender, TooltipLocation.HEAD, GalacticraftDataComponents.SCHEMATIC);
        appendIfPresent(event::registerAppender, TooltipLocation.POST_CUSTOM, GalacticraftDataComponents.SHIELD_CONTROLLER);
        appendIfPresent(event::registerAppender, TooltipLocation.HEAD, GalacticraftDataComponents.CANNED_FOOD);
        appendCapacitor(event::registerAppender);
        appendFluidTank(event::registerAppender);
    }

    private static boolean isModdedItem(ItemStack itemStack) {
        return itemStack.typeHolder().key().identifier().getNamespace().equals(Galacticraft.MODID);
    }

    private static void appendFluidTank(BiConsumer<TooltipLocation, TooltipAppender> consumer) {
        consumer.accept(
                TooltipLocation.HEAD,
                (stack, _, _, _, _, builder) -> {
                    if (isModdedItem(stack)) {
                        ResourceHandler<FluidResource> handler = stack.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(stack));

                        if (handler != null) {
                            ItemAccessFluidUtils.addTooltip(handler, builder);
                        }
                    }
                }
        );
    }

    private static void appendCapacitor(BiConsumer<TooltipLocation, TooltipAppender> consumer) {
        consumer.accept(
                TooltipLocation.HEAD,
                (stack, context, _, _, _, builder) -> {
                    if (isModdedItem(stack)) {
                        EnergyHandler handler = stack.getCapability(Capabilities.Energy.ITEM, ItemAccess.forStack(stack));

                        if (handler != null) {
                            ItemAccessEnergyUtils.addTooltip(handler, Constants.ifClient(context.level(), ClientConfig.ENERGY_UNIT, EnergyUnit.GIGA_JOULES), builder);
                        }
                    }
                }
        );
    }

    private static <T> void appendIfPresent(BiConsumer<TooltipLocation, TooltipAppender> consumer, TooltipLocation tooltipLocation, Supplier<? extends DataComponentType<T>> componentType) {
        consumer.accept(
                tooltipLocation,
                (stack, context, display, _, tooltipFlag, builder) -> {
                    if (display.shows(componentType.get())) {
                        T component = stack.get(componentType);

                        if (component instanceof TooltipProvider tooltipProvider) {
                            tooltipProvider.addToTooltip(context, builder, tooltipFlag, stack);
                        }
                    }
                }
        );
    }

    private static <T> void appendIfHolderPresent(BiConsumer<TooltipLocation, TooltipAppender> consumer, TooltipLocation tooltipLocation, Supplier<? extends DataComponentType<Holder<T>>> componentType) {
        consumer.accept(
                tooltipLocation,
                (stack, context, display, _, tooltipFlag, builder) -> {
                    if (display.shows(componentType.get())) {
                        Holder<T> component = stack.get(componentType);

                        if (component != null && component.value() instanceof TooltipProvider tooltipProvider) {
                            tooltipProvider.addToTooltip(context, builder, tooltipFlag, stack);
                        }
                    }
                }
        );
    }
}
