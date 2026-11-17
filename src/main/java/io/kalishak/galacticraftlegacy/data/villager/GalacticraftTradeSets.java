package io.kalishak.galacticraftlegacy.data.villager;

import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.references.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.trading.TradeSet;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

import java.util.Optional;

public class GalacticraftTradeSets {
    public static final ResourceKey<TradeSet> MOON_LEVEL_1 = Constants.key(Registries.TRADE_SET, "moon/level_1");
    public static final ResourceKey<TradeSet> MOON_LEVEL_2 = Constants.key(Registries.TRADE_SET, "moon/level_2");
    public static final ResourceKey<TradeSet> MOON_LEVEL_3 = Constants.key(Registries.TRADE_SET, "moon/level_3");
    public static final ResourceKey<TradeSet> MOON_LEVEL_4 = Constants.key(Registries.TRADE_SET, "moon/level_4");
    public static final ResourceKey<TradeSet> MOON_LEVEL_5 = Constants.key(Registries.TRADE_SET, "moon/level_5");

    public static void bootstrap(BootstrapContext<TradeSet> context) {
        register(context, MOON_LEVEL_1, GalacticraftTags.VillagerTrades.MOON_LEVEL_1);
        register(context, MOON_LEVEL_2, GalacticraftTags.VillagerTrades.MOON_LEVEL_2);
        register(context, MOON_LEVEL_3, GalacticraftTags.VillagerTrades.MOON_LEVEL_3);
        register(context, MOON_LEVEL_4, GalacticraftTags.VillagerTrades.MOON_LEVEL_4);
        register(context, MOON_LEVEL_5, GalacticraftTags.VillagerTrades.MOON_LEVEL_5);
    }

    private static void register(BootstrapContext<TradeSet> context, ResourceKey<TradeSet> key, TagKey<VillagerTrade> trades) {
        context.register(
                key,
                new TradeSet(
                        context.lookup(Registries.VILLAGER_TRADE).getOrThrow(trades),
                        ContextIntProviders.exactly(2),
                        false,
                        Optional.of(key.identifier().withPrefix("trade_set/"))
                )
        );
    }
}
