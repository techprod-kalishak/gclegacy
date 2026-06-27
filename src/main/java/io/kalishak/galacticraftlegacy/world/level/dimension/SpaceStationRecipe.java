/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.dimension;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.PlayerInventoryWrapper;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public record SpaceStationRecipe(List<SizedIngredient> ingredients) {
    public static final Codec<SpaceStationRecipe> DIRECT_CODEC = SizedIngredient.NESTED_CODEC
            .listOf()
            .xmap(SpaceStationRecipe::new, SpaceStationRecipe::ingredients);
    public static final Codec<Holder<SpaceStationRecipe>> CODEC = RegistryFixedCodec.create(GalacticraftRegistries.Keys.SPACE_STATION_RECIPE);
    public static final StreamCodec<RegistryFriendlyByteBuf, SpaceStationRecipe> STREAM_CODEC = SizedIngredient.STREAM_CODEC
            .apply(ByteBufCodecs.list())
            .map(SpaceStationRecipe::new, SpaceStationRecipe::ingredients);
    public static final ResourceKey<SpaceStationRecipe> EARTH_SPACE_STATION_RECIPE = Constants.key(GalacticraftRegistries.Keys.SPACE_STATION_RECIPE, "earth");

    public static void bootstrap(BootstrapContext<SpaceStationRecipe> context) {
        HolderGetter<Item> items = context.lookup(Registries.ITEM);

        context.register(
                EARTH_SPACE_STATION_RECIPE,
                SpaceStationRecipe.Builder.builder()
                        .ingredient(GalacticraftTags.Items.INGOTS_TIN, 32, items)
                        .ingredient(GalacticraftTags.Items.INGOTS_ALUMINUM, 16, items)
                        .ingredient(GalacticraftItems.ADVANCED_WAFER)
                        .ingredient(Tags.Items.INGOTS_IRON, 24, items)
                        .build()
        );
    }

    public boolean matches(@NonNull ServerPlayer player, boolean doRemove) {
        ResourceHandler<ItemResource> playerInventory = PlayerInventoryWrapper.of(player);

        try (Transaction transaction = Transaction.open(null)) {
            for (SizedIngredient sizedIngredient : this.ingredients) {
                Ingredient ingredient = sizedIngredient.ingredient();
                int count = sizedIngredient.count();

                if (ingredient.isEmpty() || count == 0) return false;

                ItemResource resource = ResourceHandlerUtil.findExtractableResource(
                        playerInventory,
                        item -> ingredient.acceptsItem(item.typeHolder()),
                        transaction
                );

                if (resource == null || resource.isEmpty()) return false;

                int extractCount = playerInventory.extract(
                        resource,
                        count,
                        transaction
                );

                if (extractCount < count) return false;
            }

            if (doRemove) {
                transaction.commit();
            }
        }

        return true;
    }

    public static class Builder {
        private final List<SizedIngredient> ingredients = new ArrayList<>();

        private Builder() {}

        public static Builder builder() {
            return new Builder();
        }

        public Builder ingredient(Ingredient ingredient) {
            this.ingredients.add(new SizedIngredient(ingredient, 1));
            return this;
        }

        public Builder ingredient(Ingredient ingredient, int count) {
            this.ingredients.add(new SizedIngredient(ingredient, count));
            return this;
        }

        public Builder ingredient(ItemLike item) {
            return ingredient(Ingredient.of(item));
        }

        public Builder ingredient(ItemLike item, int count) {
            return ingredient(Ingredient.of(item), count);
        }

        public Builder ingredient(TagKey<Item> tagKey, HolderGetter<Item> items) {
            return ingredient(Ingredient.of(items.getOrThrow(tagKey)));
        }

        public Builder ingredient(TagKey<Item> tagKey, int count, HolderGetter<Item> items) {
            return ingredient(Ingredient.of(items.getOrThrow(tagKey)), count);
        }

        public SpaceStationRecipe build() {
            return new SpaceStationRecipe(this.ingredients);
        }
    }
}
