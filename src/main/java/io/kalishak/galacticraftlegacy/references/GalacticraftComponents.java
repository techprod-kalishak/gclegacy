/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.references;

import io.kalishak.galacticraftlegacy.config.ClientConfig;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.MachineStatus;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class GalacticraftComponents {
    //Non-translated
    public static final MutableComponent SPACE_STATION_SSINVITE = Component.literal("/ssinvite").withStyle(ChatFormatting.AQUA);

    // Misc
    public static final KeyComponent DATAPACK_DESCRIPTION = KeyComponent.direct("pack.galacticraftlegacy.description");
    public static final KeyComponent CLASSIC_ASSETS_DESCRIPTION = KeyComponent.direct("pack.galacticraftlegacy.developers_art.description");
    public static final KeyComponent ADVENTURE_MODE_DESCRIPTION = KeyComponent.direct("pack.galacticraftlegacy.adventure_mode.description");
    public static final KeyComponent SPACE_TRAVEL_TEXT = KeyComponent.direct("galacticraftlegacy.space_travel.loading");
    public static final KeyComponent BED_RULE_CRYOGENIC_CHAMBER = KeyComponent.direct("block.galacticraftlegacy.bed.sleep_in_cryo_chamber");
    public static final KeyComponent ALLOW_ENTRY = KeyComponent.direct("gui.spacestation.to_allow_entry", ChatFormatting.YELLOW);
    public static final KeyComponent SPACE_STATION_ALLOW_ENTRY = new KeyComponent.Nested("gui.spacestation.playername", ALLOW_ENTRY, UnaryOperator.identity());
    public static final MutableComponent SPACE_STATION_TYPE_COMMAND = Component.translatable("gui.spacestation.type_command")
            .withStyle(ChatFormatting.YELLOW)
            .append(SPACE_STATION_SSINVITE)
            .append(SPACE_STATION_ALLOW_ENTRY.translated());
    public static final KeyComponent NETWORK_FAILURE = KeyComponent.direct("galacticraftlegacy.networking_failed");

    // Inventory
    public static final KeyComponent INVENTORY_TAB = KeyComponent.direct("container.inventory");
    public static final KeyComponent GEAR_TAB = KeyComponent.direct("container.gear");
    public static final KeyComponent CREATIVE_MODE_TAB_ITEMS = KeyComponent.direct("itemGroup.galacticraftlegacy.items");
    public static final KeyComponent CREATIVE_MODE_TAB_BLOCKS = KeyComponent.direct("itemGroup.galacticraftlegacy.blocks");
    public static final KeyComponent CREATIVE_ONLY = KeyComponent.direct("item.galacticraftlegacy.creative_only", ChatFormatting.RED);
    public static final KeyComponent INFINITE = KeyComponent.direct("item.galacticraftlegacy.infinite", ChatFormatting.GREEN);
    public static final KeyComponent TOOLTIP_BATTERY = KeyComponent.direct("item.galacticraftlegacy.battery.tooltip", ChatFormatting.GRAY);
    public static final KeyComponent TOOLTIP_BATTERY_SLOT = KeyComponent.direct("container.galacticraftlegacy.battery_slot.tooltip", ChatFormatting.GRAY);
    public static final KeyComponent TOOLTIP_EMPTY_TANK = KeyComponent.direct("item.galacticraftlegacy.fluid_tank.empty", ChatFormatting.GRAY);
    public static final KeyComponent TOOLTIP_MORE = KeyComponent.direct("item.galacticraftlegacy.press_shift", ChatFormatting.GRAY);
    public static final KeyComponent TOOLTIP_ENERGY_PER_TICK = KeyComponent.direct("item.galacticraftlegacy.energy_per_tick");
    public static final KeyComponent TOOLTIP_HOT_CONTENT = KeyComponent.direct("item.hot_content.description");
    public static final KeyComponent TOOLTIP_MORE_FLUIDS = KeyComponent.direct("item.galacticraftlegacy.tank.more_fluids", ChatFormatting.ITALIC);
    public static final KeyComponent TOOLTIP_SINGLE_FLUID = KeyComponent.direct("item.galacticraftlegacy.tank.fluid_amount");
    public static final KeyComponent TOOLTIP_OWNED_BY = KeyComponent.direct("item.galacticraftlegacy.ownable.description", ChatFormatting.GRAY);
    public static final KeyComponent NEXT_PAGE = KeyComponent.direct("container.nasa_workbench.next_button");
    public static final KeyComponent PREVIOUS_PAGE = KeyComponent.direct("container.nasa_workbench.previous_button");
    public static final KeyComponent UNLOCK_SCHEMATIC = KeyComponent.direct("container.nasa_workbench.unlock_schematic");
    public static final KeyComponent NEW_SCHEMATIC = KeyComponent.direct("container.nasa_workbench.add_new_schematic");
    public static final KeyComponent MACHINE_BUTTON_ENABLED = KeyComponent.direct("container.machine.button.enable");
    public static final KeyComponent MACHINE_BUTTON_DISABLED = KeyComponent.direct("container.machine.button.disable");
    public static final KeyComponent SUN_VISIBLE = KeyComponent.direct("container.status.sun_visible");
    public static final KeyComponent SUN_NOT_VISIBLE = KeyComponent.direct("container.status.sun_not_visible");
    public static final KeyComponent MACHINE_STATUS = KeyComponent.direct("container.machine_status");
    public static final Map<MachineStatus.Type, KeyComponent> MACHINE_STATUS_TYPE = KeyComponent.mapKey("container.machine_status", MachineStatus.Type.class);
    public static final KeyComponent ENVIRONMENTAL_BOOST = KeyComponent.direct("container.environmental_boost");

    // Items
    public static final KeyComponent ITEM_DEHYDRATED_APPLE = KeyComponent.direct(GalacticraftItemIds.DEHYDRATED_APPLE, ChatFormatting.YELLOW);
    public static final KeyComponent ITEM_DEHYDRATED_CARROT = KeyComponent.direct(GalacticraftItemIds.DEHYDRATED_CARROT, ChatFormatting.YELLOW);
    public static final KeyComponent ITEM_DEHYDRATED_MELON = KeyComponent.direct(GalacticraftItemIds.DEHYDRATED_MELON, ChatFormatting.YELLOW);
    public static final KeyComponent ITEM_DEHYDRATED_PUMPKIN = KeyComponent.direct(GalacticraftItemIds.DEHYDRATED_PUMPKIN, ChatFormatting.YELLOW);
    public static final KeyComponent ITEM_DEHYDRATED_BEETROOT = KeyComponent.direct(GalacticraftItemIds.DEHYDRATED_BEETROOT, ChatFormatting.YELLOW);
    public static final KeyComponent ITEM_DEHYDRATED_POTATO = KeyComponent.direct(GalacticraftItemIds.DEHYDRATED_POTATO, ChatFormatting.YELLOW);
    public static final KeyComponent ITEM_CANNED_BEEF = KeyComponent.direct(GalacticraftItemIds.CANNED_BEEF, ChatFormatting.YELLOW);
    public static final KeyComponent FREQUENCY_MODULE_DESC = KeyComponent.direct("item.galacticraftlegacy.frequency_module.desc");
    public static final KeyComponent FREQUENCY_MODULE_WARNING = KeyComponent.direct("item.galacticraftlegacy.missing_frequency_module.warning");
    public static final KeyComponent NEED_FREQUENCY_MODULE = KeyComponent.direct("item.galacticraftlegacy.missing_frequency_module.villager");

    // Blocks
    public static final KeyComponent BLOCK_DUNGEON_CHEST = KeyComponent.direct("container.dungeon_chest");

    // Machines
    public static final KeyComponent COAL_GENERATOR_NOT_GENERATING = KeyComponent.direct("container.coal_generator.not_generating");
    public static final KeyComponent GENERATOR_GENERATING = KeyComponent.direct("container.coal_generator.generating");
    public static final KeyComponent COAL_GENERATOR_HEAT_LEVEL = KeyComponent.direct("container.coal_generator.heat_level");
    public static final KeyComponent FILTER_NAME_HEATABLE = KeyComponent.direct("gui.recipebook.toggleRecipes.heatable");
    public static final KeyComponent FILTER_NAME_ARC_HEATABLE = KeyComponent.direct("gui.recipebook.toggleRecipes.arc_heatable");

    // Advancements
    public static final KeyComponent ADVANCEMENT_GC = KeyComponent.direct("advancements.galacticraftlegacy.galacticraft.title");
    public static final KeyComponent ADVANCEMENT_GC_DESC = KeyComponent.direct("advancements.galacticraftlegacy.galacticraft.description");
    public static final KeyComponent ADVANCEMENT_COAL_POWER = KeyComponent.direct("advancements.galacticraftlegacy.coal_power.title");
    public static final KeyComponent ADVANCEMENT_COAL_POWER_DESC = KeyComponent.direct("advancements.galacticraftlegacy.coal_power.description");
    public static final KeyComponent ADVANCEMENT_FABRICATED = KeyComponent.direct("advancements.galacticraftlegacy.fabricated.title");
    public static final KeyComponent ADVANCEMENT_FABRICATED_DESC = KeyComponent.direct("advancements.galacticraftlegacy.fabricated.description");
    public static final KeyComponent ADVANCEMENT_WAFERS = KeyComponent.direct("advancements.galacticraftlegacy.wafers.title");
    public static final KeyComponent ADVANCEMENT_WAFERS_DESC = KeyComponent.direct("advancements.galacticraftlegacy.wafers.description");
    public static final KeyComponent ADVANCEMENT_GOLDEN_WAFERS = KeyComponent.direct("advancements.galacticraftlegacy.golden_wafers.title");
    public static final KeyComponent ADVANCEMENT_GOLDEN_WAFERS_DESC = KeyComponent.direct("advancements.galacticraftlegacy.golden_wafers.description");
    public static final KeyComponent ADVANCEMENT_COMPRESSED = KeyComponent.direct("advancements.galacticraftlegacy.compressed.title");
    public static final KeyComponent ADVANCEMENT_COMPRESSED_DESC = KeyComponent.direct("advancements.galacticraftlegacy.compressed.description");

    // Commands
    public static final KeyComponent COMMAND_ERROR_SPACE_RACE_DUPE = KeyComponent.direct("galacticraftlegacy.commands.space_race.add.duplicate");
    public static final KeyComponent COMMAND_ERROR_SPACE_RACE_UNCHANGED = KeyComponent.direct("galacticraftlegacy.commands.space_race.empty.unchanged");
    public static final KeyComponent COMMAND_ERROR_SPACE_RACE_NAME = KeyComponent.direct("galacticraftlegacy.commands.space_race.option.name.unchanged");
    public static final KeyComponent COMMAND_ERROR_SPACE_RACE_COLOR = KeyComponent.direct("galacticraftlegacy.commands.space_race.option.color.unchanged");
    public static final KeyComponent COMMAND_ERROR_SPACE_RACE_FRIENDLY_FIRE_ENABLED = KeyComponent.direct("galacticraftlegacy.commands.space_race.option.friendlyfire.alreadyEnabled");
    public static final KeyComponent COMMAND_ERROR_SPACE_RACE_FRIENDLY_FIRE_DISABLED = KeyComponent.direct("galacticraftlegacy.commands.space_race.option.friendlyfire.alreadyDisabled");
    public static final KeyComponent COMMAND_ERROR_SPACE_RACE_FRIENDLY_INVISIBLES_ENABLED = KeyComponent.direct("galacticraftlegacy.commands.space_race.option.seeFriendlyInvisibles.alreadyEnabled");
    public static final KeyComponent COMMAND_ERROR_SPACE_RACE_FRIENDLY_INVISIBLES_DISABLED = KeyComponent.direct("galacticraftlegacy.commands.space_race.option.seeFriendlyInvisibles.alreadyDisabled");
    public static final KeyComponent COMMAND_ERROR_SPACE_RACE_NAME_TAG = KeyComponent.direct("galacticraftlegacy.commands.space_race.option.nametagVisibility.unchanged");
    public static final KeyComponent COMMAND_ERROR_SPACE_RACE_DEATH_MESSAGE = KeyComponent.direct("galacticraftlegacy.commands.space_race.option.deathMessageVisibility.unchanged");
    public static final KeyComponent COMMAND_ERROR_SPACE_RACE_COLLISION_RULE = KeyComponent.direct("galacticraftlegacy.commands.space_race.option.collisionRule.unchanged");
    public static final KeyComponent COMMAND_ERROR_SPACE_RACE_SCHEMATIC = KeyComponent.direct("galacticraftlegacy.commands.space_race.schematics.unchanged");
    public static final KeyComponent COMMAND_ERROR_NOT_EQUIPABLE = KeyComponent.withArgEscape("galacticraftlegacy.commands.equipment.not_equippable");
    public static final KeyComponent COMMAND_ERROR_INVALID_GEAR_OWNER = KeyComponent.withArgEscape("galacticraftlegacy.commands.invalid_gear_owner");
    public static final KeyComponent COMMAND_ERROR_SPACE_RACE_DONT_EXIST = KeyComponent.withArgEscape("galacticraftlegacy.commands.space_race.notFound");
    public static final KeyComponent COMMAND_ERROR_EMPTY_CELESTIAL = KeyComponent.direct("galacticraftlegacy.commands.data.celestial.empty");
    public static final KeyComponent COMMAND_ERROR_SCHEMATIC_ALREADY_UNLOCKED = KeyComponent.direct("galacticraftlegacy.commands.schematics.add.error");
    public static final KeyComponent COMMAND_ERROR_SCHEMATIC_NOT_UNLOCKED = KeyComponent.direct("galacticraftlegacy.commands.schematics.remove.error");
    public static final KeyComponent COMMAND_ERROR_SCHEMATIC_INVALID = KeyComponent.direct("galacticraftlegacy.argument.schematic.invalid");
    public static final KeyComponent COMMAND_SUCCESS_SPACE_RACE_LEAVE_SINGLE = KeyComponent.direct("galacticraftlegacy.commands.space_race.leave.success.single");
    public static final KeyComponent COMMAND_SUCCESS_SPACE_RACE_LEAVE_MULTIPLE = KeyComponent.direct("galacticraftlegacy.commands.space_race.leave.success.multiple");
    public static final KeyComponent COMMAND_SUCCESS_SPACE_RACE_JOIN_SINGLE = KeyComponent.direct("galacticraftlegacy.commands.space_race.join.success.single");
    public static final KeyComponent COMMAND_SUCCESS_SPACE_RACE_JOIN_MULTIPLE = KeyComponent.direct("galacticraftlegacy.commands.space_race.join.success.multiple");
    public static final KeyComponent COMMAND_SUCCESS_SPACE_RACE_NAMETAG_CHANGE = KeyComponent.direct("galacticraftlegacy.commands.space_race.option.nametagVisibility.success");
    public static final KeyComponent COMMAND_SUCCESS_SPACE_RACE_DEATH_MESSAGE_CHANGE = KeyComponent.direct("galacticraftlegacy.commands.space_race.option.deathMessageVisibility.success");
    public static final KeyComponent COMMAND_SUCCESS_SPACE_RACE_COLLISION_RULE_CHANGE = KeyComponent.direct("galacticraftlegacy.commands.space_race.option.collisionRule.success");
    public static final KeyComponent COMMAND_SUCCESS_SPACE_RACE_FRIENDLY_INVISIBLES_ENABLED = KeyComponent.direct("galacticraftlegacy.commands.space_race.option.seeFriendlyInvisibles.enabled");
    public static final KeyComponent COMMAND_SUCCESS_SPACE_RACE_FRIENDLY_INVISIBLES_DISABLED = KeyComponent.direct("galacticraftlegacy.commands.space_race.option.seeFriendlyInvisibles.disabled");
    public static final KeyComponent COMMAND_SUCCESS_SPACE_RACE_FRIENDLY_FIRE_ENABLED = KeyComponent.direct("galacticraftlegacy.commands.space_race.option.friendlyfire.enabled");
    public static final KeyComponent COMMAND_SUCCESS_SPACE_RACE_FRIENDLY_FIRE_DISABLED = KeyComponent.direct("galacticraftlegacy.commands.space_race.option.friendlyfire.disabled");
    public static final KeyComponent COMMAND_SUCCESS_SPACE_RACE_NAME_SET = KeyComponent.direct("galacticraftlegacy.commands.space_race.option.name.success");
    public static final KeyComponent COMMAND_SUCCESS_SPACE_RACE_COLOR_SET = KeyComponent.direct("galacticraftlegacy.commands.space_race.option.color.success");
    public static final KeyComponent COMMAND_SUCCESS_SPACE_RACE_EMPTY = KeyComponent.direct("galacticraftlegacy.commands.space_race.empty.success");
    public static final KeyComponent COMMAND_SUCCESS_SPACE_RACE_REMOVE = KeyComponent.direct("galacticraftlegacy.commands.space_race.remove.success");
    public static final KeyComponent COMMAND_SUCCESS_SPACE_RACE_ADD = KeyComponent.direct("galacticraftlegacy.commands.space_race.add.success");
    public static final KeyComponent COMMAND_SUCCESS_SPACE_RACE_LIST_EMPTY = KeyComponent.direct("galacticraftlegacy.commands.space_race.list.members.empty");
    public static final KeyComponent COMMAND_SUCCESS_SPACE_RACE_LIST = KeyComponent.direct("galacticraftlegacy.commands.space_race.list.members.success");
    public static final KeyComponent COMMAND_SUCCESS_SPACE_RACES_EMPTY = KeyComponent.direct("galacticraftlegacy.commands.space_race.list.space_races.empty");
    public static final KeyComponent COMMAND_SUCCESS_SPACE_RACES_EXIST = KeyComponent.direct("galacticraftlegacy.commands.space_race.list.space_races.sucess");
    public static final KeyComponent COMMAND_SUCCESS_SPACE_RACE_PREFIX = KeyComponent.direct("galacticraftlegacy.commands.space_race.option.prefix.success");
    public static final KeyComponent COMMAND_SUCCESS_SPACE_RACE_SUFFIX = KeyComponent.direct("galacticraftlegacy.commands.space_race.option.suffix.success");
    public static final KeyComponent COMMAND_SUCCESS_SPACE_RACE_SCHEMATIC = KeyComponent.direct("galacticraftlegacy.commands.space_race.option.schematic.success");
    public static final KeyComponent COMMAND_SUCCESS_EQUIP_EMERGENCY_GEAR = KeyComponent.direct("galacticraftlegacy.commands.emergency_equip.success");
    public static final KeyComponent COMMAND_SUCCESS_SCHEMATICS_LIST = KeyComponent.direct("galacticraftlegacy.commands.schematics.list.success");
    public static final KeyComponent COMMAND_SUCCESS_SCHEMATIC_ADD_ALL = KeyComponent.direct("galacticraftlegacy.commands.schematics.add.all");
    public static final KeyComponent COMMAND_SUCCESS_SCHEMATIC_REMOVE_ALL = KeyComponent.direct("galacticraftlegacy.commands.schematics.remove.all");
    public static final KeyComponent COMMAND_SUCCESS_SCHEMATIC_EMPTY_LIST = KeyComponent.direct("galacticraftlegacy.commands.schematics.list.empty");
    public static final KeyComponent COMMAND_SUCCESS_SCHEMATIC_ADD = KeyComponent.direct("galacticraftlegacy.commands.schematics.add");
    public static final KeyComponent COMMAND_SUCCESS_SCHEMATIC_REMOVE = KeyComponent.direct("galacticraftlegacy.commands.schematics.remove");

    // Config Strings
    public static final String CONFIG_GENERAL = "galacticraftlegacy.configuration.general";
    public static final String CONFIG_DEBUG_MODE = "galacticraftlegacy.configgui.common.debug_mode";
    public static final String CONFIG_DISABLED_ROCKETS_DIMENSIONS = "galacticraftlegacy.configgui.common.dimensions_with_disabled_rockets";
    public static final String CONFIG_DISABLE_RETURNING_ROCKETS = "galacticraftlegacy.configgui.common.disable_returning_rockets";
    public static final String CONFIG_FORCE_OVERWORLD_RESPAWN = "galacticraftlegacy.configgui.common.force_overworld_respawn";
    public static final String CONFIG_DISABLE_LANDERS = "galacticraftlegacy.configgui.common.disable_landers";
    public static final String CONFIG_ENVIRONMENT = "galacticraftlegacy.configuration.environment";
    public static final String CONFIG_GUI = "galacticraftlegacy.configuration.gui";
    public static final String CONFIG_MORE_STARS = "galacticraftlegacy.configgui.client.more_stars";
    public static final String CONFIG_DISABLE_PARTICLES = "galacticraftlegacy.configgui.client.disable_rocket_particles";
    public static final String CONFIG_DISABLE_THIRD_PERSON_VIEW = "galacticraftlegacy.configgui.client.disable_vehicle_tpv";
    public static final String CONFIG_ENERGY_UNIT = "galacticraftlegacy.configgui.client.energy_unit";
    public static final String CONFIG_OXYGEN_TANK_POS = "galacticraftlegacy.configgui.client.oxygen_tanks_pos";
    public static final String CONFIG_ICON_ROTATIONS = "galacticraftlegacy.configgui.client.icons_rotation";
    public static final String CONFIG_ACCESSIBILITY = "galacticraftlegacy.configuration.accessibility";
    public static final String CONFIG_SCROLL_SENSITIVITY = "galacticraftlegacy.configgui.client.scroll_sensitivity";
    public static final String CONFIG_INVERT_SCROLL = "galacticraftlegacy.configgui.client.invert_scroll";
    public static final String CONFIG_SPACE_RACE_POPUP = "galacticraftlegacy.configgui.client.space_race_popup";
    public static final String CONFIG_WORLD_BORDERS = "galacticraftlegacy.configgui.server.world_borders";
    public static final String CONFIG_STATION_PERMISSIONS = "galacticraftlegacy.configgui.server.space_stations_permissions";
    public static final String CONFIG_DISABLE_SPACE_STATIONS = "galacticraftlegacy.configgui.server.disable_space_station_creation";
    public static final String CONFIG_OVERRIDE_CAPES = "galacticraftlegacy.configgui.server.override_capes";
    public static final String CONFIG_SEAL_EDGE_CHECK = "galacticraftlegacy.configgui.server.seal_edge_check";
    public static final String CONFIG_DIFFICULTY = "galacticraftlegacy.configuration.difficulty";
    public static final String CONFIG_DISABLE_ROCKET_EXPLOSION = "galacticraftlegacy.configgui.server.disable_spaceship_explosion";
    public static final String CONFIG_DISABLE_METEOR_GRIEFING = "galacticraftlegacy.configgui.server.disable_meteor_block_breaking";
    public static final String CONFIG_METEOR_SPAWN_MULTIPLIER = "galacticraftlegacy.configgui.server.meteor_spawn_multiplier";
    public static final String CONFIG_SOLAR_ENERGY_MULTIPLIER = "galacticraftlegacy.configgui.server.solar_energy_multiplier";
    public static final String CONFIG_FUEL_USAGE_MULTIPLIER = "galacticraftlegacy.configgui.server.fuel_usage_multiplier";
    public static final String CONFIG_GAMEMODE = "galacticraftlegacy.configgui.server.gamemode";
    public static final String CONFIG_QUICK_MODE = "galacticraftlegacy.configgui.server.quick_mode";
    public static final String CONFIG_HARD_MODE = "galacticraftlegacy.configgui.server.hard_mode";
    public static final String CONFIG_ADVENTURE_MODE = "galacticraftlegacy.configgui.server.adventure_mode";
    public static final String CONFIG_ADVENTURE_MODE_FLAGS = "galacticraftlegacy.configgui.server.adventure_mode_flags";
    public static final String CONFIG_ENTITIES = "galacticraftlegacy.configuration.entities";
    public static final String CONFIG_SUFFOCATION_COOLDOWN = "galacticraftlegacy.configgui.server.suffocation_cooldown";
    public static final String CONFIG_SUFFOCATION_DAMAGE = "galacticraftlegacy.configgui.server.suffocation_damage";
    public static final String CONFIG_BOSS_HEALTH_MODIFIER = "galacticraftlegacy.configgui.server.boss_health_modifier";

    public static Component energyComponent(int amount, boolean applyPerTickSuffix, UnaryOperator<Style> withStyle) {
        return Component.literal(ClientConfig.ENERGY_UNIT.get().calculate(amount) + " " + ClientConfig.ENERGY_UNIT.get().getUnit() + (applyPerTickSuffix ? "/t" : "")).withStyle(withStyle);
    }

    public static Component energyComponent(int amount, boolean applyPerTickSuffix) {
        return energyComponent(amount, applyPerTickSuffix, UnaryOperator.identity());
    }

    public static Component energyComponent(EnergyHandler energyHandler, boolean applyPerTickSuffix, UnaryOperator<Style> withStyle) {
        return energyComponent(energyHandler.getAmountAsInt(), applyPerTickSuffix, withStyle);
    }

    public static Component energyComponentWithCapacity(int amount, int capacity, UnaryOperator<Style> withStyle) {
        return GalacticraftComponents.TOOLTIP_BATTERY.translatedArg(ClientConfig.ENERGY_UNIT.get().calculate(amount) + "/" + ClientConfig.ENERGY_UNIT.get().calculate(capacity) + " " + ClientConfig.ENERGY_UNIT.get().getUnit()).withStyle(withStyle);
    }

    public static Component energyComponentWithCapacity(EnergyHandler energyHandler, UnaryOperator<Style> withStyle) {
        return energyComponentWithCapacity(energyHandler.getAmountAsInt(), energyHandler.getCapacityAsInt(), withStyle);
    }

    public static void infinite(Consumer<Component> tooltipAdder) {
        tooltipAdder.accept(INFINITE.translated());
        tooltipAdder.accept(CREATIVE_ONLY.translated());
    }

    public static Void networkFailureMessage(Consumer<Component> consumer, Throwable throwable) {
        consumer.accept(NETWORK_FAILURE.translatedArg(throwable.getLocalizedMessage()));
        return null;
    }

    public static Component setToggleButtonMessage(boolean isDisabled) {
        return isDisabled ? MACHINE_BUTTON_DISABLED.translated() : MACHINE_BUTTON_ENABLED.translated();
    }
}
