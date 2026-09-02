/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.references;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import io.kalishak.galacticraftlegacy.config.ClientConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import org.apache.commons.lang3.function.TriFunction;
import org.jspecify.annotations.NonNull;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class GalacticraftComponents {
    public static final String FREQUENCY_MODULE_DESC = "item.galacticraftlegacy.frequency_module.desc";
    public static final String FREQUENCY_MODULE_WARNING = "item.galacticraftlegacy.frequency_module.warning";

    // Misc
    public static final Key DATAPACK_DESCRIPTION = key("pack.galacticraftlegacy.description");
    public static final Key CLASSIC_ASSETS_DESCRIPTION = key("pack.galacticraftlegacy.developers_art.description");
    public static final Key ADVENTURE_MODE_DESCRIPTION = key("pack.galacticraftlegacy.adventure_mode.description");
    public static final Key SPACE_TRAVEL_TEXT = key("galacticraftlegacy.space_travel.loading");
    public static final Key BED_RULE_CRYOGENIC_CHAMBER = key("block.galacticraftlegacy.bed.sleep_in_cryo_chamber");
    public static final MutableComponent SPACE_STATION_SSINVITE = Component.literal("/ssinvite").withStyle(ChatFormatting.AQUA);
    public static final MutableComponent SPACE_STATION_ALLOW_ENTRY = Component.translatable("gui.spacestation.playername", Component.translatable("gui.spacestation.to_allow_entry").withStyle(ChatFormatting.YELLOW));
    public static final MutableComponent SPACE_STATION_TYPE_COMMAND = Component.translatable("gui.spacestation.type_command")
            .withStyle(ChatFormatting.YELLOW)
            .append(SPACE_STATION_SSINVITE)
            .append(SPACE_STATION_ALLOW_ENTRY);

    // Inventory
    public static final Key INVENTORY_TAB = key("container.inventory");
    public static final Key GEAR_TAB = key("container.gear");
    public static final Key CREATIVE_MODE_TAB_ITEMS = key("itemGroup.galacticraftlegacy.items");
    public static final Key CREATIVE_MODE_TAB_BLOCKS = key("itemGroup.galacticraftlegacy.blocks");
    public static final Key CREATIVE_ONLY = key("item.galacticraftlegacy.creative_only", ChatFormatting.RED);
    public static final Key INFINITE = key("item.galacticraftlegacy.infinite", ChatFormatting.GREEN);
    public static final Function<String, MutableComponent> TOOLTIP_BATTERY = arg -> Component.translatable("item.galacticraftlegacy.battery.tooltip", arg);
    public static final MutableComponent TOOLTIP_FLAG = Component.translatable("item.galacticraftlegacy.team_flag");
    public static final Key TOOLTIP_EMPTY_TANK = key("item.galacticraftlegact.fluid_tank.empty", ChatFormatting.GRAY);
    public static final Key TOOLTIP_MORE = key("item.galacticraftlegacy.press_shift", ChatFormatting.GRAY);
    public static final Function<Number, MutableComponent> TOOLTIP_ENERGY_PER_TICK = (amount) -> Component.translatable("item.galacticraftlegacy.energy_per_tick", amount);
    public static final Function<Number, MutableComponent> TOOLTIP_HOT_CONTENT = (amount) -> Component.translatable("item.hot_content.description", amount + "s");
    public static final Function<Number, Component> TOOLTIP_MORE_FLUIDS = amount -> Component.translatable("item.galacticraftlegacy.tank.more_fluids", amount).withStyle(ChatFormatting.ITALIC);
    public static final Function<FluidStack, Component> TOOLTIP_SINGLE_FLUID = fluidStack -> Component.translatable("item.galacticraftlegacy.tank.fluid_amount", fluidStack.getHoverName(), fluidStack.getAmount());
    public static final Key NEXT_PAGE = key("container.nasa_workbench.next_button");
    public static final Key PREVIOUS_PAGE = key("container.nasa_workbench.previous_button");
    public static final Key UNLOCK_SCHEMATIC = key("container.nasa_workbench.unlock_schematic");
    public static final Key NEW_SCHEMATIC = key("container.nasa_workbench.add_new_schematic");

    // Items
    public static final Key ITEM_DEHYDRATED_APPLE = key("item.galacticraftlegacy.dehydrated_apple", ChatFormatting.YELLOW);
    public static final Key ITEM_DEHYDRATED_CARROT = key("item.galacticraftlegacy.dehydrated_carrot", ChatFormatting.YELLOW);
    public static final Key ITEM_DEHYDRATED_MELON = key("item.galacticraftlegacy.dehydrated_melon", ChatFormatting.YELLOW);
    public static final Key ITEM_DEHYDRATED_PUMPKIN = key("item.galacticraftlegacy.dehydrated_pumpkin", ChatFormatting.YELLOW);
    public static final Key ITEM_DEHYDRATED_BEETROOT = key("item.galacticraftlegacy.dehydrated_beetroot", ChatFormatting.YELLOW);
    public static final Key ITEM_DEHYDRATED_POTATO = key("item.galacticraftlegacy.dehydrated_potato", ChatFormatting.YELLOW);
    public static final Key ITEM_CANNED_BEEF = key("item.galacticraftlegacy.canned_beef", ChatFormatting.YELLOW);

    // Blocks
    public static final Key BLOCK_DUNGEON_CHEST = key("galacticraftlegacy.container.dungeon_chest");

    // Machines
    public static final Key COAL_GENERATOR_NOT_GENERATING = key("container.coal_generator.not_generating");
    public static final Key COAL_GENERATOR_GENERATING = key("container.coal_generator.generating");
    public static final Key COAL_GENERATOR_HEAT_LEVEL = key("container.coal_generator.heat_level");

    public static final Key FILTER_NAME_HEATABLE = key("gui.recipebook.toggleRecipes.heatable");
    public static final Key FILTER_NAME_ARC_HEATABLE = key("gui.recipebook.toggleRecipes.arc_heatable");

    // Advancements
    public static final Key ADVANCEMENT_GC = key("advancements.galacticraftlegacy.galacticraft.title");
    public static final Key ADVANCEMENT_GC_DESC = key("advancements.galacticraftlegacy.galacticraft.description");
    public static final Key ADVANCEMENT_COAL_POWER = key("advancements.galacticraftlegacy.coal_power.title");
    public static final Key ADVANCEMENT_COAL_POWER_DESC = key("advancements.galacticraftlegacy.coal_power.description");
    public static final Key ADVANCEMENT_FABRICATED = key("advancements.galacticraftlegacy.fabricated.title");
    public static final Key ADVANCEMENT_FABRICATED_DESC = key("advancements.galacticraftlegacy.fabricated.description");
    public static final Key ADVANCEMENT_WAFERS = key("advancements.galacticraftlegacy.wafers.title");
    public static final Key ADVANCEMENT_WAFERS_DESC = key("advancements.galacticraftlegacy.wafers.description");
    public static final Key ADVANCEMENT_GOLDEN_WAFERS = key("advancements.galacticraftlegacy.golden_wafers.title");
    public static final Key ADVANCEMENT_GOLDEN_WAFERS_DESC = key("advancements.galacticraftlegacy.golden_wafers.description");
    public static final Key ADVANCEMENT_COMPRESSED = key("advancements.galacticraftlegacy.compressed.title");
    public static final Key ADVANCEMENT_COMPRESSED_DESC = key("advancements.galacticraftlegacy.compressed.description");

    // Commands
    public static final Component COMMAND_ERROR_SPACE_RACE_DUPE = Component.translatable("galacticraftlegacy.commands.space_race.add.duplicate");
    public static final Component COMMAND_ERROR_SPACE_RACE_UNCHANGED = Component.translatable("galacticraftlegacy.commands.space_race.empty.unchanged");
    public static final Component COMMAND_ERROR_SPACE_RACE_NAME = Component.translatable("galacticraftlegacy.commands.space_race.option.name.unchanged");
    public static final Component COMMAND_ERROR_SPACE_RACE_COLOR = Component.translatable("galacticraftlegacy.commands.space_race.option.color.unchanged");
    public static final Component COMMAND_ERROR_SPACE_RACE_FRIENDLY_FIRE_ENABLED = Component.translatable("galacticraftlegacy.commands.space_race.option.friendlyfire.alreadyEnabled");
    public static final Component COMMAND_ERROR_SPACE_RACE_FRIENDLY_FIRE_DISABLED = Component.translatable("galacticraftlegacy.commands.space_race.option.friendlyfire.alreadyDisabled");
    public static final Component COMMAND_ERROR_SPACE_RACE_FRIENDLY_INVISIBLES_ENABLED = Component.translatable("galacticraftlegacy.commands.space_race.option.seeFriendlyInvisibles.alreadyEnabled");
    public static final Component COMMAND_ERROR_SPACE_RACE_FRIENDLY_INVISIBLES_DISABLED = Component.translatable("galacticraftlegacy.commands.space_race.option.seeFriendlyInvisibles.alreadyEnabled");
    public static final Component COMMAND_ERROR_SPACE_RACE_NAME_TAG = Component.translatable("galacticraftlegacy.commands.space_race.option.nametagVisibility.unchanged");
    public static final Component COMMAND_ERROR_SPACE_RACE_DEATH_MESSAGE = Component.translatable("galacticraftlegacy.commands.space_race.option.deathMessageVisibility.unchanged");
    public static final Component COMMAND_ERROR_SPACE_RACE_COLLISION_RULE = Component.translatable("galacticraftlegacy.commands.space_race.option.collisionRule.unchanged");
    public static final Component COMMAND_ERROR_SPACE_RACE_SCHEMATIC = Component.translatable("galacticraftlegacy.commands.space_race.schematics.duplicate");
    public static final Dynamic2CommandExceptionType.Function COMMAND_ERROR_NOT_EQUIPABLE = (entity, equipment) -> Component.translatableEscape("galacticraftlegacy.commands.equipment.not_equippable", entity, equipment);
    public static final Function<Object, Message> COMMAND_ERROR_INVALID_GEAR_OWNER = entity -> Component.translatableEscape("galacticraftlegacy.commands.invalid_gear_owner", entity);
    public static final Function<Object, Message> COMMAND_ERROR_SPACE_RACE_DONT_EXIST = name -> Component.translatableEscape("galacticraftlegacy.commands.space_race.notFound", name);

    public static final MutableComponent COMMAND_SUCCESS_SPACE_RACE_LEAVE_SINGLE = Component.translatable("galacticraftlegacy.commands.space_race.leave.success.single");
    public static final MutableComponent COMMAND_SUCCESS_SPACE_RACE_LEAVE_MULTIPLE = Component.translatable("galacticraftlegacy.commands.space_race.leave.success.multiple");
    public static final MutableComponent COMMAND_SUCCESS_SPACE_RACE_JOIN_SINGLE = Component.translatable("galacticraftlegacy.commands.space_race.join.success.single");
    public static final MutableComponent COMMAND_SUCCESS_SPACE_RACE_JOIN_MULTIPLE = Component.translatable("galacticraftlegacy.commands.space_race.join.success.multiple");
    public static final BiFunction<Component, Component, Component> COMMAND_SUCCESS_SPACE_RACE_NAMETAG_CHANGE = (formattedName, displayName) -> Component.translatable("galacticraftlegacy.commands.space_race.option.nametagVisibility.success", formattedName, displayName);
    public static final BiFunction<Component, Component, Component> COMMAND_SUCCESS_SPACE_RACE_DEATH_MESSAGE_CHANGE = (formattedName, displayName) -> Component.translatable("galacticraftlegacy.commands.space_race.option.deathMessageVisibility.success", formattedName, displayName);
    public static final BiFunction<Component, Component, Component> COMMAND_SUCCESS_SPACE_RACE_COLLISION_RULE_CHANGE = (formattedName, displayName) -> Component.translatable("galacticraftlegacy.commands.space_race.option.collisionRule.success", formattedName, displayName);
    public static final MutableComponent COMMAND_SUCCESS_SPACE_RACE_FRIENDLY_INVISIBLES_ENABLED = Component.translatable("galacticraftlegacy.commands.space_race.option.seeFriendlyInvisibles.enabled");
    public static final MutableComponent COMMAND_SUCCESS_SPACE_RACE_FRIENDLY_INVISIBLES_DISABLED = Component.translatable("galacticraftlegacy.commands.space_race.option.seeFriendlyInvisibles.disabled");
    public static final MutableComponent COMMAND_SUCCESS_SPACE_RACE_FRIENDLY_FIRE_ENABLED = Component.translatable("galacticraftlegacy.commands.space_race.option.friendlyfire.enabled");
    public static final MutableComponent COMMAND_SUCCESS_SPACE_RACE_FRIENDLY_FIRE_DISABLED = Component.translatable("galacticraftlegacy.commands.space_race.option.friendlyfire.disabled");
    public static final MutableComponent COMMAND_SUCCESS_SPACE_RACE_NAME_SET = Component.translatable("galacticraftlegacy.commands.space_race.option.name.success");
    public static final BiFunction<Component, String, Component> COMMAND_SUCCESS_SPACE_RACE_COLOR_SET = (formattedName, colorName) -> Component.translatable("galacticraftlegacy.commands.space_race.option.color.success", formattedName, colorName);
    public static final BiFunction<Number, Component, Component> COMMAND_SUCCESS_SPACE_RACE_EMPTY = (size, formattedName) -> Component.translatable("galacticraftlegacy.commands.space_race.empty.success", String.valueOf(size), formattedName);
    public static final MutableComponent COMMAND_SUCCESS_SPACE_RACE_REMOVE = Component.translatable("galacticraftlegacy.commands.space_race.remove.success");
    public static final MutableComponent COMMAND_SUCCESS_SPACE_RACE_ADD = Component.translatable("galacticraftlegacy.commands.space_race.add.success");
    public static final MutableComponent COMMAND_SUCCESS_SPACE_RACE_LIST_EMPTY = Component.translatable("galacticraftlegacy.commands.space_race.list.members.empty");
    public static final TriFunction<Component, Number, Component, Component> COMMAND_SUCCESS_SPACE_RACE_LIST = (formattedName, size, members) ->  Component.translatable("galacticraftlegacy.commands.space_race.list.members.success", formattedName, size, members);
    public static final Component COMMAND_SUCCESS_SPACE_RACES_EMPTY = Component.translatable("galacticraftlegacy.commands.space_race.list.space_races.empty");
    public static final BiFunction<Number, Component, Component> COMMAND_SUCCESS_SPACE_RACES_EXIST = (size, members) -> Component.translatable("galacticraftlegacy.commands.space_race.list.space_races.sucess", size, members);
    public static final MutableComponent COMMAND_SUCCESS_SPACE_RACE_PREFIX = Component.translatable("galacticraftlegacy.commands.space_race.option.prefix.success");
    public static final MutableComponent COMMAND_SUCCESS_SPACE_RACE_SUFFIX = Component.translatable("galacticraftlegacy.commands.space_race.option.suffix.success");
    public static final BiFunction<Component, String, Component> COMMAND_SUCCESS_SPACE_RACE_SCHEMATIC = (formattedName, schematicId) -> Component.translatable("galacticraftlegacy.commands.space_race.option.schematic.success", formattedName, schematicId);
    public static final BiFunction<Component, Number, Component> COMMAND_SUCCESS_EQUIP_EMERGENCY_GEAR = (formattedName, amount) -> Component.translatable("galacticraftlegacy.commands.emergency_equip.success", formattedName, amount);
    public static final Component COMMAND_ERROR_SCHEMATIC_ALREADY_UNLOCKED = Component.translatable("galacticraftlegacy.commands.schematics.add.error");
    public static final Component COMMAND_ERROR_SCHEMATIC_NOT_UNLOCKED = Component.translatable("galacticraftlegacy.commands.schematics.remove.error");
    public static final TriFunction<Component, Number, Component, Component> COMMAND_SUCCESS_SCHEMATICS_LIST = (playerName, unlockedCount, schematics) ->  Component.translatable("galacticraftlegacy.commands.schematics.list.success", playerName, unlockedCount, schematics);
    public static final Function<Component, MutableComponent> COMMAND_SUCCESS_SCHEMATIC_ADD_ALL = name -> Component.translatable("galacticraftlegacy.commands.schematics.add.all", name);
    public static final Function<Component, MutableComponent> COMMAND_SUCCESS_SCHEMATIC_REMOVE_ALL = name -> Component.translatable("galacticraftlegacy.commands.schematics.remove.all", name);
    public static final Function<Component, MutableComponent> COMMAND_SUCCESS_SCHEMATIC_EMPTY_LIST = playerName -> Component.translatable("galacticraftlegacy.commands.schematics.list.empty", playerName);
    public static final BiFunction<Component, String, MutableComponent> COMMAND_SUCCESS_SCHEMATIC_ADD = (name, id) -> Component.translatable("galacticraftlegacy.commands.schematics.add", name, id);
    public static final BiFunction<Component, String, MutableComponent> COMMAND_SUCCESS_SCHEMATIC_REMOVE = (name, id) -> Component.translatable("galacticraftlegacy.commands.schematics.remove", id, name);

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
        return GalacticraftComponents.TOOLTIP_BATTERY.apply(ClientConfig.ENERGY_UNIT.get().calculate(amount) + "/" + ClientConfig.ENERGY_UNIT.get().calculate(capacity) + " " + ClientConfig.ENERGY_UNIT.get().getUnit()).withStyle(withStyle);
    }

    public static Component energyComponentWithCapacity(EnergyHandler energyHandler, UnaryOperator<Style> withStyle) {
        return energyComponentWithCapacity(energyHandler.getAmountAsInt(), energyHandler.getCapacityAsInt(), withStyle);
    }

    public static void infinite(Consumer<Component> tooltipAdder) {
        tooltipAdder.accept(INFINITE.asComponent());
        tooltipAdder.accept(CREATIVE_ONLY.asComponent());
    }

    public static Void networkFailureMessage(Consumer<Component> consumer, Throwable throwable) {
        consumer.accept(Component.translatable("galacticraftlegacy.networking_failed", throwable.getLocalizedMessage()));
        return null;
    }

    private static Key key(String translationKey) {
        return new Key(translationKey, UnaryOperator.identity());
    }

    private static Key key(String translationKey, ChatFormatting color) {
        return new Key(translationKey, style -> style.withColor(color));
    }

    public record Key(String translationKey, UnaryOperator<Style> styled) {
        public MutableComponent asComponent() {
            return Component.translatable(this.translationKey).withStyle(this.styled);
        }

        public MutableComponent asComponent(Object... objects) {
            return Component.translatable(this.translationKey, objects);
        }

        @Override
        public @NonNull String toString() {
            return this.translationKey;
        }
    }
}
