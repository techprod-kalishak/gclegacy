package io.kalishak.galacticraftlegacy.data.villager;

import io.kalishak.galacticraftlegacy.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.component.SchematicVariant;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.registry.BuiltinSchematicVariants;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.level.material.fluid.GalacticraftFluids;
import io.kalishak.galacticraftlegacy.world.level.storage.loot.functions.SetItemCapacitorFunction;
import io.kalishak.galacticraftlegacy.world.level.storage.loot.functions.SetItemFluidTankFunction;
import io.kalishak.galacticraftlegacy.world.level.storage.loot.functions.SetSchematicVariantFunction;
import net.minecraft.advancements.predicates.DataComponentMatchers;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.TradeCost;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.item.trading.VillagerTrades;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetRandomDyesFunction;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import net.neoforged.neoforge.fluids.SimpleFluidContent;

import java.util.List;

public class GalacticraftTrades {
    public static final ResourceKey<VillagerTrade> MOONLLAGER_1_OXYGEN_MASK_LUNAR = trade("moon/1/oxygen_mask_lunar");
    public static final ResourceKey<VillagerTrade> MOONLLAGER_1_LIGHT_TANK_LUNAR = trade("moon/1/light_tank_lunar");
    public static final ResourceKey<VillagerTrade> MOONLLAGER_1_OXYGEN_GEAR_LUNAR = trade("moon/1/oxygen_gear_lunar");

    public static final ResourceKey<VillagerTrade> MOONLLAGER_2_FUEL_CANISTER_LUNAR = trade("moon/2/fuel_canister_lunar");
    public static final ResourceKey<VillagerTrade> MOONLLAGER_2_PARACHUTE_LUNAR = trade("moon/2/parachute_lunar");
    public static final ResourceKey<VillagerTrade> MOONLLAGER_2_BATTERY_LUNAR = trade("moon/2/battery_lunar");

    public static final ResourceKey<VillagerTrade> MOONLLAGER_3_CANISTER_CARROT = trade("moon/3/oil_canister_carrot");
    public static final ResourceKey<VillagerTrade> MOONLLAGER_3_BASIC_WAFER_LUNAR = trade("moon/3/basic_wafer_lunar");
    public static final ResourceKey<VillagerTrade> MOONLLAGER_3_EXCHANGE_BUGGY_SCHEMATIC = trade("moon/3/exchange_buggy_schematic");
    public static final ResourceKey<VillagerTrade> MOONLLAGER_3_EXCHANGE_T2_SCHEMATIC = trade("moon/3/exchange_t2_schematic");

    public static final ResourceKey<VillagerTrade> MOONLLAGER_4_COMPRESSED_TIN_LUNAR = trade("moon/4/compressed_tin_lunar");
    public static final ResourceKey<VillagerTrade> MOONLLAGER_4_COMPRESSED_COPPER_LUNAR = trade("moon/4/compressed_copper_lunar");

    public static final ResourceKey<VillagerTrade> MOONLLAGER_5_JUNGLE_TREE_LUNAR = trade("moon/5/jungle_tree_lunar");

    public static void bootstrap(BootstrapContext<VillagerTrade> context) {
        HolderGetter<Item> items = context.lookup(Registries.ITEM);
        HolderGetter<SchematicVariant> schematics = context.lookup(GalacticraftRegistries.Keys.SCHEMATIC);

        context.register(
                MOONLLAGER_1_OXYGEN_MASK_LUNAR,
                VillagerTrade.builder(
                        price(GalacticraftItems.SAPPHIRE, 1, 2),
                        new ItemStackTemplate(GalacticraftItems.OXYGEN_MASK),
                        9,
                        2,
                        0.0F
                ).build()
        );
        context.register(
                MOONLLAGER_1_LIGHT_TANK_LUNAR,
                VillagerTrade.builder(
                        price(GalacticraftItems.SAPPHIRE, 3, 4),
                        new ItemStackTemplate(GalacticraftItems.LIGHT_TANK),
                        4,
                        3,
                        0.0F
                ).addModifier(SetItemFluidTankFunction.setFluid(GalacticraftFluids.OXYGEN, ContextIntProviders.between(0, 235))).build()
        );
        context.register(
                MOONLLAGER_1_OXYGEN_GEAR_LUNAR,
                VillagerTrade.builder(
                        price(GalacticraftItems.SAPPHIRE, 3, 4),
                        new ItemStackTemplate(GalacticraftItems.OXYGEN_GEAR),
                        3,
                        2,
                        0.0F
                ).build()
        );
        context.register(
                MOONLLAGER_2_FUEL_CANISTER_LUNAR,
                VillagerTrade.builder(
                        price(GalacticraftItems.SAPPHIRE, 3, 4),
                        new ItemStackTemplate(GalacticraftItems.FLUID_CANISTER),
                        6,
                        4,
                        0.0F
                ).addModifier(SetItemFluidTankFunction.setFluid(GalacticraftFluids.FUEL, ContextIntProviders.between(0, 317))).build()
        );
        context.register(
                MOONLLAGER_2_PARACHUTE_LUNAR,
                VillagerTrade.builder(
                        price(GalacticraftItems.SAPPHIRE, 1, 2),
                        new ItemStackTemplate(GalacticraftItems.PARACHUTE.white()),
                        4,
                        1,
                        0.0F
                ).build()
        );
        context.register(
                MOONLLAGER_2_BATTERY_LUNAR,
                VillagerTrade.builder(
                        price(GalacticraftItems.SAPPHIRE, 2, 4),
                        new ItemStackTemplate(GalacticraftItems.BATTERY),
                        4,
                        3,
                        0.0F
                ).addModifier(SetItemCapacitorFunction.setEnergy(UniformInt.of(0, 7500))).build()
        );
        context.register(
                MOONLLAGER_3_CANISTER_CARROT,
                VillagerTrade.builder(
                        new TradeCost(Items.CARROT, 1),
                        new ItemStackTemplate(GalacticraftItems.FLUID_CANISTER),
                        24,
                        1,
                        0.0F
                ).build()
        );
        context.register(
                MOONLLAGER_3_BASIC_WAFER_LUNAR,
                VillagerTrade.builder(
                        price(GalacticraftItems.SAPPHIRE, 3, 4),
                        new ItemStackTemplate(GalacticraftItems.BASIC_WAFER),
                        3,
                        4,
                        0.0F
                ).build()
        );
        Holder<SchematicVariant> tier2Rocket = schematics.getOrThrow(BuiltinSchematicVariants.TIER_2_ROCKET);
        Holder<SchematicVariant> buggy = schematics.getOrThrow(BuiltinSchematicVariants.MOON_BUGGY);

        context.register(
                MOONLLAGER_3_EXCHANGE_BUGGY_SCHEMATIC,
                VillagerTrade.builder(
                        new TradeCost(GalacticraftItems.SCHEMATIC, 1),
                        new ItemStackTemplate(GalacticraftItems.SCHEMATIC, DataComponentPatch.builder().set(GalacticraftDataComponents.SCHEMATIC.get(), tier2Rocket).build()),
                        2,
                        5,
                        0.0F
                ).addModifiers(exchangeSchematic(items, tier2Rocket, buggy)).build()
        );
        context.register(
                MOONLLAGER_3_EXCHANGE_T2_SCHEMATIC,
                VillagerTrade.builder(
                        new TradeCost(GalacticraftItems.SCHEMATIC, 1),
                        new ItemStackTemplate(GalacticraftItems.SCHEMATIC, DataComponentPatch.builder().set(GalacticraftDataComponents.SCHEMATIC.get(), buggy).build()),
                        2,
                        5,
                        0.0F
                ).addModifiers(exchangeSchematic(items, buggy, tier2Rocket)).build()
        );
        context.register(
                MOONLLAGER_4_COMPRESSED_TIN_LUNAR,
                VillagerTrade.builder(
                        new TradeCost(GalacticraftItems.SAPPHIRE, 1),
                        new ItemStackTemplate(GalacticraftItems.COMPRESSED_TIN),
                        12,
                        1,
                        0.0F
                ).additionalWants(new TradeCost(GalacticraftItems.TIN_INGOT, 2)).build()
        );
        context.register(
                MOONLLAGER_4_COMPRESSED_COPPER_LUNAR,
                VillagerTrade.builder(
                        new TradeCost(GalacticraftItems.SAPPHIRE, 1),
                        new ItemStackTemplate(GalacticraftItems.COMPRESSED_COPPER),
                        12,
                        1,
                        0.0F
                ).additionalWants(new TradeCost(Items.COPPER_INGOT, 2)).build()
        );
        context.register(
                MOONLLAGER_5_JUNGLE_TREE_LUNAR,
                VillagerTrade.builder(
                        new TradeCost(Items.JUNGLE_SAPLING, 1),
                        new ItemStackTemplate(GalacticraftItems.SAPPHIRE),
                        3,
                        6,
                        0.0F
                ).addModifier(SetItemCountFunction.setCount(ContextIntProviders.between(11, 39))).build()
        );
    }

    private static ResourceKey<VillagerTrade> trade(String id) {
        return Constants.key(Registries.VILLAGER_TRADE, id);
    }

    private static TradeCost price(ItemLike item, int min, int max) {
        return new TradeCost(item, ContextIntProviders.between(min, max));
    }

    private static List<Holder<LootItemFunction>> exchangeSchematic(HolderGetter<Item> items, Holder<SchematicVariant> desiredVariant, Holder<SchematicVariant> expectedVariant) {
        ItemPredicate.Builder newSchematic = new ItemPredicate.Builder()
                .of(items, GalacticraftItems.SCHEMATIC)
                .withComponents(
                        DataComponentMatchers.Builder.components()
                                .exact(DataComponentExactPredicate.expect(GalacticraftDataComponents.SCHEMATIC.get(), expectedVariant)).build()
                );

        return VillagerTrades.discardItemIfItsNot(
                SetSchematicVariantFunction.setVariant(desiredVariant),
                newSchematic
        );
    }

    private static List<Holder<LootItemFunction>> filledTank(HolderGetter<Item> items, Holder<Fluid> fluid, Holder<ContextIntProvider> amount, Item expectedItem) {
        ItemPredicate.Builder itemWithTank = new ItemPredicate.Builder()
                .of(items, expectedItem)
                .withComponents(
                        DataComponentMatchers.Builder.components()
                                .exact(DataComponentExactPredicate.expect(GalacticraftDataComponents.FLUID_TANK.get(), SimpleFluidContent.EMPTY))
                                .build()
                );

        return VillagerTrades.discardItemIfItsNot(
                SetItemFluidTankFunction.setFluid(fluid, amount),
                itemWithTank
        );
    }

    private static List<Holder<LootItemFunction>> chargedBattery(HolderGetter<Item> items, IntProvider amount, Item expectedItem) {
        ItemPredicate.Builder itemWithTank = new ItemPredicate.Builder()
                .of(items, expectedItem)
                .withComponents(
                        DataComponentMatchers.Builder.components()
                                .exact(DataComponentExactPredicate.expect(GalacticraftDataComponents.STORED_ENERGY.get(), 0))
                                .build()
                );

        return VillagerTrades.discardItemIfItsNot(
                SetItemCapacitorFunction.setEnergy(amount),
                itemWithTank
        );
    }

    private static List<Holder<LootItemFunction>> dyedItem(HolderGetter<Item> items, Item expectedItem) {
        ItemPredicate.Builder anyDyedItem = new ItemPredicate.Builder()
                .of(items, expectedItem)
                .withComponents(DataComponentMatchers.Builder.components().any(DataComponents.DYED_COLOR).build());
        return VillagerTrades.discardItemIfItsNot(addRandomDye(), anyDyedItem);
    }

    private static LootItemFunction.Builder addRandomDye() {
        return SetRandomDyesFunction.withCount(ContextIntProviders.add(ContextIntProviders.exactly(1), ContextIntProviders.binomial(2, 0.75F)));
    }
}
