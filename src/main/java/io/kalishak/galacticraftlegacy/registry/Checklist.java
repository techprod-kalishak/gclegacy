/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.registry;

import com.google.common.collect.ImmutableMap;
import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import net.minecraft.advancements.criterion.DataComponentMatchers;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

public final class Checklist {
    public static final ResourceKey<ChecklistEntry> EQUIP_PARACHUTE = Constants.key(GalacticraftRegistries.Keys.CHECKLIST, "equip_parachute");
    public static final ResourceKey<ChecklistEntry> EQUIP_OXYGEN_SUIT = Constants.key(GalacticraftRegistries.Keys.CHECKLIST, "equip_oxygen_suit");
    public static final ResourceKey<ChecklistEntry> EQUIP_THERMAL_PADDING = Constants.key(GalacticraftRegistries.Keys.CHECKLIST, "equip_thermal_padding");
    public static final ResourceKey<ChecklistEntry> EQUIP_ISOTHERMAL_PADDING = Constants.key(GalacticraftRegistries.Keys.CHECKLIST, "equip_isothermal_padding");
    public static final ResourceKey<ChecklistEntry> EQUIP_SHIELD_CONTROLLER = Constants.key(GalacticraftRegistries.Keys.CHECKLIST, "equip_shield_controller");
    public static final ResourceKey<ChecklistEntry> EQUIP_GRAPPLING_HOOK = Constants.key(GalacticraftRegistries.Keys.CHECKLIST, "equip_grappling_hook");

    public static void bootstrap(BootstrapContext<ChecklistEntry> cxt) {
        HolderGetter<Item> itemHolderGetter = cxt.lookup(Registries.ITEM);

        register(
                cxt,
                EQUIP_PARACHUTE,
                map -> map.put(
                        GearEquipmentSlot.PARACHUTE,
                        ItemPredicate.Builder.item().of(itemHolderGetter, GalacticraftTags.Items.PARACHUTE).build()
                )
        );
        register(
                cxt,
                EQUIP_OXYGEN_SUIT,
                map -> map
                        .put(GearEquipmentSlot.MASK, ItemPredicate.Builder.item().of(itemHolderGetter, GalacticraftItems.OXYGEN_MASK).build())
        );
        register(
                cxt,
                EQUIP_THERMAL_PADDING,
                map -> map
                        .put(GearEquipmentSlot.THERMAL_CAP, ItemPredicate.Builder.item().of(itemHolderGetter, GalacticraftItems.THERMAL_PADDING_HELM).build())
                        .put(GearEquipmentSlot.THERMAL_SHIRT, ItemPredicate.Builder.item().of(itemHolderGetter, GalacticraftItems.THERMAL_PADDING_CHESTPIECE).build())
                        .put(GearEquipmentSlot.THERMAL_LEGGINGS, ItemPredicate.Builder.item().of(itemHolderGetter, GalacticraftItems.THERMAL_PADDING_LEGGINGS).build())
                        .put(GearEquipmentSlot.THERMAL_SOCKS, ItemPredicate.Builder.item().of(itemHolderGetter, GalacticraftItems.THERMAL_PADDING_BOOTS).build())
        );
        register(
                cxt,
                EQUIP_ISOTHERMAL_PADDING,
                map -> map
                        .put(GearEquipmentSlot.THERMAL_CAP, ItemPredicate.Builder.item().of(itemHolderGetter, GalacticraftItems.ISOTHERMAL_HELM).build())
                        .put(GearEquipmentSlot.THERMAL_SHIRT, ItemPredicate.Builder.item().of(itemHolderGetter, GalacticraftItems.ISOTHERMAL_CHESTPIECE).build())
                        .put(GearEquipmentSlot.THERMAL_LEGGINGS, ItemPredicate.Builder.item().of(itemHolderGetter, GalacticraftItems.ISOTHERMAL_LEGGINGS).build())
                        .put(GearEquipmentSlot.THERMAL_SOCKS, ItemPredicate.Builder.item().of(itemHolderGetter, GalacticraftItems.ISOTHERMAL_BOOTS).build())
        );
        register(
                cxt,
                EQUIP_SHIELD_CONTROLLER,
                map -> map
                        .put(GearEquipmentSlot.SHIELD, ItemPredicate.Builder.item().withComponents(DataComponentMatchers.Builder.components().any(GalacticraftDataComponents.SHIELD_CONTROLLER.get()).build()).build())
        );
        register(
                cxt,
                EQUIP_GRAPPLING_HOOK,
                map -> map.put(GearEquipmentSlot.TELEMETRY, ItemPredicate.Builder.item().of(itemHolderGetter, GalacticraftItems.SENSOR_GLASSES).build())
        );
    }

    private static void register(BootstrapContext<ChecklistEntry> cxt, ResourceKey<ChecklistEntry> key, UnaryOperator<ImmutableMap.Builder<GearEquipmentSlot, ItemPredicate>> builder) {
        cxt.register(key, new ChecklistEntry(
                key.identifier().getPath(),
                builder.apply(ImmutableMap.builder()).build()
        ));
    }
}
