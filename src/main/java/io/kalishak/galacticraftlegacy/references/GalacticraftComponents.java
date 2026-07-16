/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.references;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.fluids.FluidStack;
import org.apache.commons.lang3.function.TriFunction;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class GalacticraftComponents {
    //private static final List<Component> UNTRANSLATED_COMPONENTS = new ArrayList<>();
    public static final String FREQUENCY_MODULE_DESC = "item.galacticraftlegacy.frequency_module.desc";
    public static final String FREQUENCY_MODULE_WARNING = "item.galacticraftlegacy.frequency_module.warning";

    // Misc
    public static final Component DATAPACK_DESCRIPTION = Component.translatable("pack.galacticraftlegacy.description");
    public static final Component SPACE_TRAVEL_TEXT = Component.translatable("galacticraftlegacy.space_travel.loading");
    public static final Component BED_RULE_CRYOGENIC_CHAMBER = Component.translatable("block.galacticraftlegacy.bed.sleep_in_cryo_chamber");
    public static final MutableComponent SPACE_STATION_SSINVITE = Component.literal("/ssinvite").withStyle(ChatFormatting.AQUA);
    public static final MutableComponent SPACE_STATION_ALLOW_ENTRY = Component.translatable("gui.spacestation.playername", Component.translatable("gui.spacestation.to_allow_entry").withStyle(ChatFormatting.YELLOW));
    public static final MutableComponent SPACE_STATION_TYPE_COMMAND = Component.translatable("gui.spacestation.type_command")
            .withStyle(ChatFormatting.YELLOW)
            .append(SPACE_STATION_SSINVITE)
            .append(SPACE_STATION_ALLOW_ENTRY);

    // Inventory
    public static final Component INVENTORY_TAB = Component.translatable("container.inventory");
    public static final Component GEAR_TAB = Component.translatable("container.gear");
    public static final Component CREATIVE_MODE_TAB_ITEMS = Component.translatable("itemGroup.galacticraftlegacy.items");
    public static final Component CREATIVE_MODE_TAB_BLOCKS = Component.translatable("itemGroup.galacticraftlegacy.blocks");
    public static final Component CREATIVE_ONLY = Component.translatable("item.galacticraftlegacy.creative_only").withStyle(ChatFormatting.RED);
    public static final Component INFINITE = Component.translatable("item.galacticraftlegacy.infinite").withStyle(ChatFormatting.GREEN);
    public static final Function<String, MutableComponent> TOOLTIP_BATTERY = arg -> Component.translatable("item.galacticraftlegacy.battery.tooltip", arg);
    public static final MutableComponent TOOLTIP_FLAG = Component.translatable("item.galacticraftlegacy.team_flag");
    public static final Component TOOLTIP_EMPTY_TANK = Component.translatable("item.galacticraftlegact.fluid_tank.empty").withStyle(ChatFormatting.GRAY);
    public static final Component TOOLTIP_MORE = Component.translatable("item.galacticraftlegacy.press_shift").withStyle(ChatFormatting.GRAY);
    public static final Function<Number, MutableComponent> TOOLTIP_ENERGY_PER_TICK = (amount) -> Component.translatable("item.galacticraftlegacy.energy_per_tick", amount);
    public static final Function<Number, MutableComponent> TOOLTIP_HOT_CONTENT = (amount) -> Component.translatable("item.hot_content.description", amount + "s");
    public static final Function<Number, Component> TOOLTIP_MORE_FLUIDS = amount -> Component.translatable("item.galacticraftlegacy.tank.more_fluids", amount).withStyle(ChatFormatting.ITALIC);
    public static final Function<FluidStack, Component> TOOLTIP_SINGLE_FLUID = fluidStack -> Component.translatable("item.galacticraftlegacy.tank.fluid_amount", fluidStack.getHoverName(), fluidStack.getAmount());

    // Items
    public static final Component ITEM_DEHYDRATED_APPLE = Component.translatable("item.galacticraftlegacy.dehydrated_apple").withStyle(ChatFormatting.YELLOW);
    public static final Component ITEM_DEHYDRATED_CARROT = Component.translatable("item.galacticraftlegacy.dehydrated_carrot").withStyle(ChatFormatting.YELLOW);
    public static final Component ITEM_DEHYDRATED_MELON = Component.translatable("item.galacticraftlegacy.dehydrated_melon").withStyle(ChatFormatting.YELLOW);
    public static final Component ITEM_DEHYDRATED_PUMPKIN = Component.translatable("item.galacticraftlegacy.dehydrated_pumpkin").withStyle(ChatFormatting.YELLOW);
    public static final Component ITEM_DEHYDRATED_BEETROOT = Component.translatable("item.galacticraftlegacy.dehydrated_beetroot").withStyle(ChatFormatting.YELLOW);
    public static final Component ITEM_DEHYDRATED_POTATO = Component.translatable("item.galacticraftlegacy.dehydrated_potato").withStyle(ChatFormatting.YELLOW);
    public static final Component ITEM_CANNED_BEEF = Component.translatable("item.galacticraftlegacy.canned_beef").withStyle(ChatFormatting.YELLOW);

    // Blocks
    public static final Component BLOCK_DUNGEON_CHEST = Component.translatable("galacticraftlegacy.container.dungeon_chest");
    public static final Component BLOCK_PARACHEST = Component.translatable("galacticraftlegacy.container.parachest");;

    // Machines
    public static final Component COAL_GENERATOR_NOT_GENERATING = Component.translatable("container.coal_generator.not_generating");
    public static final Component COAL_GENERATOR_GENERATING = Component.translatable("container.coal_generator.generating");
    public static final MutableComponent COAL_GENERATOR_HEAT_LEVEL = Component.translatable("container.coal_generator.heat_level");

    public static final Component FILTER_NAME_HEATABLE = Component.translatable("gui.recipebook.toggleRecipes.heatable");
    public static final Component FILTER_NAME_ARC_HEATABLE = Component.translatable("gui.recipebook.toggleRecipes.arc_heatable");

    // Advancements
    public static final Component ADVANCEMENT_GC = Component.translatable("advancements.galacticraftlegacy.galacticraft.title");
    public static final Component ADVANCEMENT_GC_DESC = Component.translatable("advancements.galacticraftlegacy.galacticraft.description");
    public static final Component ADVANCEMENT_COAL_POWER = Component.translatable("advancements.galacticraftlegacy.coal_power.title");
    public static final Component ADVANCEMENT_COAL_POWER_DESC = Component.translatable("advancements.galacticraftlegacy.coal_power.description");
    public static final Component ADVANCEMENT_FABRICATED = Component.translatable("advancements.galacticraftlegacy.fabricated.title");
    public static final Component ADVANCEMENT_FABRICATED_DESC = Component.translatable("advancements.galacticraftlegacy.fabricated.description");
    public static final Component ADVANCEMENT_WAFERS = Component.translatable("advancements.galacticraftlegacy.wafers.title");
    public static final Component ADVANCEMENT_WAFERS_DESC = Component.translatable("advancements.galacticraftlegacy.wafers.description");
    public static final Component ADVANCEMENT_GOLDEN_WAFERS = Component.translatable("advancements.galacticraftlegacy.golden_wafers.title");
    public static final Component ADVANCEMENT_GOLDEN_WAFERS_DESC = Component.translatable("advancements.galacticraftlegacy.golden_wafers.description");
    public static final Component ADVANCEMENT_COMPRESSED = Component.translatable("advancements.galacticraftlegacy.compressed.title");
    public static final Component ADVANCEMENT_COMPRESSED_DESC = Component.translatable("advancements.galacticraftlegacy.compressed.description");

    // Commands
    public static final Component COMMAND_ERROR_SPACE_RACE_DUPE = Component.translatable("commands.space_race.add.duplicate");
    public static final Component COMMAND_ERROR_SPACE_RACE_UNCHANGED = Component.translatable("commands.space_race.empty.unchanged");
    public static final Component COMMAND_ERROR_SPACE_RACE_NAME = Component.translatable("commands.space_race.option.name.unchanged");
    public static final Component COMMAND_ERROR_SPACE_RACE_COLOR = Component.translatable("commands.space_race.option.color.unchanged");
    public static final Component COMMAND_ERROR_SPACE_RACE_FRIENDLY_FIRE_ENABLED = Component.translatable("commands.space_race.option.friendlyfire.alreadyEnabled");
    public static final Component COMMAND_ERROR_SPACE_RACE_FRIENDLY_FIRE_DISABLED = Component.translatable("commands.space_race.option.friendlyfire.alreadyDisabled");
    public static final Component COMMAND_ERROR_SPACE_RACE_FRIENDLY_INVISIBLES_ENABLED = Component.translatable("commands.space_race.option.seeFriendlyInvisibles.alreadyEnabled");
    public static final Component COMMAND_ERROR_SPACE_RACE_FRIENDLY_INVISIBLES_DISABLED = Component.translatable("commands.space_race.option.seeFriendlyInvisibles.alreadyEnabled");
    public static final Component COMMAND_ERROR_SPACE_RACE_NAME_TAG = Component.translatable("commands.space_race.option.nametagVisibility.unchanged");
    public static final Component COMMAND_ERROR_SPACE_RACE_DEATH_MESSAGE = Component.translatable("commands.space_race.option.deathMessageVisibility.unchanged");
    public static final Component COMMAND_ERROR_SPACE_RACE_COLLISION_RULE = Component.translatable("commands.space_race.option.collisionRule.unchanged");
    public static final Component COMMAND_ERROR_SPACE_RACE_SCHEMATIC = Component.translatable("commands.space_race.schematics.duplicate");
    public static final Dynamic2CommandExceptionType.Function COMMAND_ERROR_NOT_EQUIPABLE = (entity, equipment) -> Component.translatableEscape("galacticraftlegacy.arguments.equipment.not_equippable", entity, equipment);
    public static final Function<Object, Message> COMMAND_ERROR_INVALID_GEAR_OWNER = entity -> Component.translatableEscape("galacticraftlegacy.arguments.invalid_entity", entity);
    public static final Function<Object, Message> COMMAND_ERROR_SPACE_RACE_DONT_EXIST = name -> Component.translatableEscape("space_race.notFound", name);

    public static final MutableComponent COMMAND_SUCCESS_SPACE_RACE_LEAVE_SINGLE = Component.translatable("commands.space_race.leave.success.single");
    public static final MutableComponent COMMAND_SUCCESS_SPACE_RACE_LEAVE_MULTIPLE = Component.translatable("commands.space_race.leave.success.multiple");
    public static final MutableComponent COMMAND_SUCCESS_SPACE_RACE_JOIN_SINGLE = Component.translatable("commands.space_race.join.success.single");
    public static final MutableComponent COMMAND_SUCCESS_SPACE_RACE_JOIN_MULTIPLE = Component.translatable("commands.space_race.join.success.multiple");
    public static final BiFunction<Component, Component, Component> COMMAND_SUCCESS_SPACE_RACE_NAMETAG_CHANGE = (formattedName, displayName) -> Component.translatable("commands.space_race.option.nametagVisibility.success", formattedName, displayName);
    public static final BiFunction<Component, Component, Component> COMMAND_SUCCESS_SPACE_RACE_DEATH_MESSAGE_CHANGE = (formattedName, displayName) -> Component.translatable("commands.space_race.option.deathMessageVisibility.success", formattedName, displayName);
    public static final BiFunction<Component, Component, Component> COMMAND_SUCCESS_SPACE_RACE_COLLISION_RULE_CHANGE = (formattedName, displayName) -> Component.translatable("commands.space_race.option.collisionRule.success", formattedName, displayName);
    public static final MutableComponent COMMAND_SUCCESS_SPACE_RACE_FRIENDLY_INVISIBLES_ENABLED = Component.translatable("commands.space_race.option.seeFriendlyInvisibles.enabled");
    public static final MutableComponent COMMAND_SUCCESS_SPACE_RACE_FRIENDLY_INVISIBLES_DISABLED = Component.translatable("commands.space_race.option.seeFriendlyInvisibles.disabled");
    public static final MutableComponent COMMAND_SUCCESS_SPACE_RACE_FRIENDLY_FIRE_ENABLED = Component.translatable("commands.space_race.option.friendlyfire.enabled");
    public static final MutableComponent COMMAND_SUCCESS_SPACE_RACE_FRIENDLY_FIRE_DISABLED = Component.translatable("commands.space_race.option.friendlyfire.disabled");
    public static final MutableComponent COMMAND_SUCCESS_SPACE_RACE_NAME_SET = Component.translatable("commands.space_race.option.name.success");
    public static final BiFunction<Component, String, Component> COMMAND_SUCCESS_SPACE_RACE_COLOR_SET = (formattedName, colorName) -> Component.translatable("commands.space_race.option.color.success", formattedName, colorName);
    public static final BiFunction<Number, Component, Component> COMMAND_SUCCESS_SPACE_RACE_EMPTY = (size, formattedName) -> Component.translatable("commands.space_race.empty.success", String.valueOf(size), formattedName);
    public static final MutableComponent COMMAND_SUCCESS_SPACE_RACE_REMOVE = Component.translatable("commands.space_race.remove.success");
    public static final MutableComponent COMMAND_SUCCESS_SPACE_RACE_ADD = Component.translatable("commands.space_race.add.success");
    public static final MutableComponent COMMAND_SUCCESS_SPACE_RACE_LIST_EMPTY = Component.translatable("commands.space_race.list.members.empty");
    public static final TriFunction<Component, Number, Component, Component> COMMAND_SUCCESS_SPACE_RACE_LIST = (formattedName, size, members) ->  Component.translatable("commands.space_race.list.members.success", formattedName, size, members);
    public static final Component COMMAND_SUCCESS_SPACE_RACES_EMPTY = Component.translatable("commands.space_race.list.space_races.empty");
    public static final BiFunction<Number, Component, Component> COMMAND_SUCCESS_SPACE_RACES_EXIST = (size, members) -> Component.translatable("commands.space_race.list.space_races.sucess", size, members);
    public static final MutableComponent COMMAND_SUCCESS_SPACE_RACE_PREFIX = Component.translatable("commands.space_race.option.prefix.success");
    public static final MutableComponent COMMAND_SUCCESS_SPACE_RACE_SUFFIX = Component.translatable("commands.space_race.option.suffix.success");
    public static final BiFunction<Component, Identifier, Component> COMMAND_SUCCESS_SPACE_RACE_SCHEMATIC = (formattedName, schematicId) -> Component.translatable("commands.space_race.option.schematic.success", formattedName, schematicId);
    public static final BiFunction<Component, Number, Component> COMMAND_SUCCESS_EQUIP_EMERGENCY_GEAR = (formattedName, amount) -> Component.translatable("galacticraftlegacy.commands.emergency_equip.success", formattedName, amount);


    public static void infinite(Consumer<Component> tooltipAdder) {
        tooltipAdder.accept(INFINITE);
        tooltipAdder.accept(CREATIVE_ONLY);
    }

    public static Void networkFailureMessage(Consumer<Component> consumer, Throwable throwable) {
        consumer.accept(Component.translatable("galacticraftlegacy.networking_failed", throwable.getLocalizedMessage()));
        return null;
    }

    /*private static Component withRegistry(Component component) {
        UNTRANSLATED_COMPONENTS.add(component);
        return component;
    }

    public static Component withRegistryTranslatable(String translationKey) {
        return withRegistry(Component.translatable(translationKey));
    }

    public static Component withRegistryTranslatable(String translationKey, Object... args) {
        return withRegistry(Component.translatable(translationKey, args));
    }

    public static Component withRegistryTranslatable(String translationKey, UnaryOperator<MutableComponent> componentBuilder, Object... args) {
        return withRegistry(componentBuilder.apply(Component.translatable(translationKey, args)));
    }*/
}
