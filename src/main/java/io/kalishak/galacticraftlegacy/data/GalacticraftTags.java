package io.kalishak.galacticraftlegacy.data;

import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.registry.SchematicVariant;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.timeline.Timeline;

public final class GalacticraftTags {
    public static final TagKey<SchematicVariant> PLACEABLE_SCHEMATICS = tagKey(GalacticraftRegistries.Keys.SCHEMATIC, "placeable");

    private static <R> TagKey<R> tagKey(ResourceKey<? extends Registry<R>> registryKey, String tagKey) {
        return TagKey.create(registryKey, Constants.id(tagKey));
    }

    public static class Biomes {
        public static final TagKey<Biome> IS_MOON = tagKey(Registries.BIOME, "is_moon");
        public static final TagKey<Biome> IS_MARS = tagKey(Registries.BIOME, "is_mars");
        public static final TagKey<Biome> IS_ASTEROIDS = tagKey(Registries.BIOME, "is_asteroids");
        public static final TagKey<Biome> IS_VENUS = tagKey(Registries.BIOME, "is_venus");

        public static final TagKey<Biome> HAS_BASIC_FEATURES = tagKey(Registries.BIOME, "has_basic_features");
        public static final TagKey<Biome> HAS_ADVANCED_FEATURES = tagKey(Registries.BIOME, "has_advanced_features");
        public static final TagKey<Biome> HAS_ULTIMATE_FEATURES = tagKey(Registries.BIOME, "has_ultimate_features");
    }

    public static class Blocks {
        public static final TagKey<Block> BREATHABLE_AIR = tagKey(Registries.BLOCK, "breathable_air");
        public static final TagKey<Block> INCORRECT_FOR_DESH_TOOL = tagKey(Registries.BLOCK, "incorrect_for_desh_tool");
        public static final TagKey<Block> INCORRECT_FOR_STEEL_TOOL = tagKey(Registries.BLOCK, "incorrect_for_steel_tool");
        public static final TagKey<Block> INCORRECT_FOR_TITANIUM_TOOL = tagKey(Registries.BLOCK, "incorrect_for_titanium_tool");
        public static final TagKey<Block> INFINIBURN_OPEN_SPACE = tagKey(Registries.BLOCK, "infiniburn_open_space");
        public static final TagKey<Block> INFINIBURN_VENUS = tagKey(Registries.BLOCK, "infiniburn_open_space");
        public static final TagKey<Block> MACHINE = tagKey(Registries.BLOCK, "machine");
        public static final TagKey<Block> MACHINE_BASIC = tagKey(Registries.BLOCK, "machine/basic");
        public static final TagKey<Block> MACHINE_ADVANCED = tagKey(Registries.BLOCK, "machine/advanced");
        public static final TagKey<Block> SEALABLE = tagKey(Registries.BLOCK, "sealable");
        public static final TagKey<Block> SENSOR_GLASSES_DETECTABLE = tagKey(Registries.BLOCK, "sensor_glasses_detectable");
    }

    public static class EntityTypes {
        public static final TagKey<EntityType<?>> CAN_EQUIP_GEAR = tagKey(Registries.ENTITY_TYPE, "can_equip_gear");
        public static final TagKey<EntityType<?>> CAN_EQUIP_PARACHUTE = tagKey(Registries.ENTITY_TYPE, "can_equip_parachute");
    }

    public static class Fluids {
        public static final TagKey<Fluid> IS_OXYGEN = tagKey(Registries.FLUID, "is_oxygen");
        public static final TagKey<Fluid> IS_OIL = tagKey(Registries.FLUID, "is_oil");
        public static final TagKey<Fluid> IS_FUEL = tagKey(Registries.FLUID, "is_fuel");
    }

    public static class Items {
        public static final TagKey<Item> INGOTS_DESH = tagKey(Registries.ITEM, "ingots/desh");
        public static final TagKey<Item> INGOTS_STEEL = tagKey(Registries.ITEM, "ingots/steel");
        public static final TagKey<Item> INGOTS_TITANIUM = tagKey(Registries.ITEM, "ingots/titanium");
        public static final TagKey<Item> INGOTS_LEAD = tagKey(Registries.ITEM, "ingots/lead");
        public static final TagKey<Item> NUGGETS_LEAD = tagKey(Registries.ITEM, "nuggets/lead");
        public static final TagKey<Item> NUGGETS_DESH = tagKey(Registries.ITEM, "nuggets/desh");
        public static final TagKey<Item> NUGGETS_STEEL = tagKey(Registries.ITEM, "nuggets/steel");
        public static final TagKey<Item> NUGGETS_TITANIUM = tagKey(Registries.ITEM, "nuggets/titanium");
        public static final TagKey<Item> PARACHUTE = tagKey(Registries.ITEM, "parachute");
        public static final TagKey<Item> RAW_MATERIALS_CHEESE = tagKey(Registries.ITEM, "raw_materials/cheese");
        public static final TagKey<Item> RAW_MATERIALS_DESH = tagKey(Registries.ITEM, "raw_materials/desh");
        public static final TagKey<Item> RAW_MATERIALS_LEAD = tagKey(Registries.ITEM, "raw_materials/lead");
        public static final TagKey<Item> RAW_MATERIALS_SILICON = tagKey(Registries.ITEM, "raw_materials/silicon");
        public static final TagKey<Item> RAW_MATERIALS_STEEL = tagKey(Registries.ITEM, "raw_materials/steel");
        public static final TagKey<Item> RAW_MATERIALS_TITANIUM = tagKey(Registries.ITEM, "raw_materials/titanium");
        public static final TagKey<Item> REPAIRS_DESH_ARMOR = tagKey(Registries.ITEM, "repairs_desh_armor");
        public static final TagKey<Item> REPAIRS_STEEL_ARMOR = tagKey(Registries.ITEM, "repairs_steel_armor");
        public static final TagKey<Item> REPAIRS_TITANIUM_ARMOR = tagKey(Registries.ITEM, "repairs_titanium_armor");
        public static final TagKey<Item> REPAIRS_DESH_TOOL = tagKey(Registries.ITEM, "desh_tool_materials");
        public static final TagKey<Item> REPAIRS_STEEL_TOOL = tagKey(Registries.ITEM, "steel_tool_materials");
        public static final TagKey<Item> REPAIRS_TITANIUM_TOOL = tagKey(Registries.ITEM, "titanium_tool_materials");
        public static final TagKey<Item> WRENCH = tagKey(Registries.ITEM, "tools/wrench");
    }

    public static class Structures {
        public static final TagKey<Structure> IS_DUNGEON = tagKey(Registries.STRUCTURE, "is_dungeon");
    }

    public static class Timelines {
        public static final TagKey<Timeline> IN_ORBIT = tagKey(Registries.TIMELINE, "in_orbit");
        public static final TagKey<Timeline> IN_MOON = tagKey(Registries.TIMELINE, "in_moon");
        public static final TagKey<Timeline> IN_MARS = tagKey(Registries.TIMELINE, "in_mars");
        public static final TagKey<Timeline> IN_ASTEROIDS = tagKey(Registries.TIMELINE, "in_asteroids");
        public static final TagKey<Timeline> IN_VENUS = tagKey(Registries.TIMELINE, "in_venus");
    }
}
