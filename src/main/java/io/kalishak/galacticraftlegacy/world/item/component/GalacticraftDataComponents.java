/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.component;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.registry.SchematicVariant;
import io.kalishak.galacticraftlegacy.world.item.KeyLock;
import io.kalishak.galacticraftlegacy.world.level.block.entity.MagneticCraftingBlockEntity;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.stream.Stream;

public final class GalacticraftDataComponents {
    private static final DeferredRegister.DataComponents REGISTRY = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Galacticraft.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CannedFood.CannedComponent>> CANNED_FOOD = REGISTRY.registerComponentType(
            "canned_food",
            builder -> builder.persistent(CannedFood.CannedComponent.CODEC).networkSynchronized(CannedFood.CannedComponent.STREAM_CODEC).cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<EntityReference<Player>>> PLAYER_REFERENCE = REGISTRY.registerComponentType(
            "entity_reference",
            builder -> builder.persistent(EntityReference.codec()).networkSynchronized(EntityReference.streamCodec()).cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<FlagItemData>> FLAG = REGISTRY.registerComponentType(
            "flag",
            builder -> builder.persistent(FlagItemData.CODEC).networkSynchronized(FlagItemData.STREAM_CODEC).cacheEncoding()
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
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemWithDescription>> ITEM_WITH_DESCRIPTION = REGISTRY.registerComponentType(
            "item_with_description",
            builder -> builder.persistent(ItemWithDescription.CODEC).networkSynchronized(ItemWithDescription.STREAM_CODEC).cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<KeyLock>> KEY_LOCK = REGISTRY.registerComponentType(
            "key_lock",
            builder -> builder.persistent(KeyLock.CODEC).networkSynchronized(KeyLock.STREAM_CODEC).cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<RecipeHolder<?>>> RECIPE_HOLDER = REGISTRY.registerComponentType(
            "recipe_holder",
            builder -> builder.persistent(MagneticCraftingBlockEntity.RECIPE_HOLDER_CODEC).networkSynchronized(RecipeHolder.STREAM_CODEC).cacheEncoding().ignoreSwapAnimation()
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

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }

    public static Stream<DataComponentType<?>> getTooltipProviders() {
        return REGISTRY.getEntries().stream().map(DeferredHolder::get);
    }
}
