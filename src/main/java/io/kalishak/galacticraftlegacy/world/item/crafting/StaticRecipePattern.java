/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.input.CompressingRecipeInput;
import it.unimi.dsi.fastutil.chars.CharArraySet;
import it.unimi.dsi.fastutil.chars.CharSet;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Util;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class StaticRecipePattern {
    public static final MapCodec<StaticRecipePattern> MAP_CODEC = StaticRecipePattern.Data.MAP_CODEC
            .flatXmap(
                    StaticRecipePattern::unpack,
                    p_344423_ -> p_344423_.data.map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Cannot encode unpacked recipe"))
            );
    public static final StreamCodec<RegistryFriendlyByteBuf, StaticRecipePattern> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, staticRecipePattern -> staticRecipePattern.width,
            ByteBufCodecs.VAR_INT, staticRecipePattern -> staticRecipePattern.height,
            Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), staticRecipePattern -> staticRecipePattern.ingredients,
            StaticRecipePattern::createFromNetwork
    );
    private final int width;
    private final int height;
    private final List<Optional<Ingredient>> ingredients;
    private final Optional<StaticRecipePattern.Data> data;
    private final int ingredientCount;
    private final boolean symmetrical;

    public StaticRecipePattern(int width, int height, List<Optional<Ingredient>> ingredients, Optional<StaticRecipePattern.Data> data) {
        this.width = width;
        this.height = height;
        this.ingredients = ingredients;
        this.data = data;
        this.ingredientCount = (int) ingredients.stream().flatMap(Optional::stream).count();
        this.symmetrical = Util.isSymmetrical(width, height, ingredients);
    }

    private static StaticRecipePattern createFromNetwork(Integer width, Integer height, List<Optional<Ingredient>> ingredients) {
        return new StaticRecipePattern(width, height, ingredients, Optional.empty());
    }

    public static StaticRecipePattern of(Map<Character, Ingredient> key, String... pattern) {
        return of(key, List.of(pattern));
    }

    public static StaticRecipePattern of(Map<Character, Ingredient> key, List<String> pattern) {
        StaticRecipePattern.Data data = new StaticRecipePattern.Data(key, pattern);
        return unpack(data).getOrThrow();
    }

    private static DataResult<StaticRecipePattern> unpack(StaticRecipePattern.Data data) {
        String[] astring = shrink(data.pattern);
        int i = astring[0].length();
        int j = astring.length;
        List<Optional<Ingredient>> list = new ArrayList<>(i * j);
        CharSet charset = new CharArraySet(data.key.keySet());

        for (String s : astring) {
            for (int k = 0; k < s.length(); k++) {
                char c0 = s.charAt(k);

                Optional<Ingredient> optional;

                if (c0 == ' ') {
                    optional = Optional.empty();
                } else {
                    Ingredient ingredient = data.key.get(c0);

                    if (ingredient == null) {
                        return DataResult.error(() -> "Pattern references symbol '" + c0 + "' but it's not defined in the key");
                    }

                    optional = Optional.of(ingredient);
                }

                charset.remove(c0);
                list.add(optional);
            }
        }

        return !charset.isEmpty()
                ? DataResult.error(() -> "Key defines symbols that aren't used in pattern: " + charset)
                : DataResult.success(new StaticRecipePattern(i, j, list, Optional.of(data)));
    }

    static String[] shrink(List<String> pattern) {
        int i = Integer.MAX_VALUE;
        int j = 0;
        int k = 0;
        int l = 0;

        for (int i1 = 0; i1 < pattern.size(); i1++) {
            String s = pattern.get(i1);
            i = Math.min(i, firstNonEmpty(s));
            int j1 = lastNonEmpty(s);
            j = Math.max(j, j1);

            if (j1 < 0) {
                if (k == i1) {
                    k++;
                }

                l++;
            } else {
                l = 0;
            }
        }

        if (pattern.size() == l) {
            return new String[0];
        }

        String[] astring = new String[pattern.size() - l - k];

        for (int k1 = 0; k1 < astring.length; k1++) {
            astring[k1] = pattern.get(k1 + k).substring(i, j + 1);
        }

        return astring;
    }

    private static int firstNonEmpty(String row) {
        int i = 0;

        while (i < row.length() && row.charAt(i) == ' ') {
            i++;
        }

        return i;
    }

    private static int lastNonEmpty(String row) {
        int i = row.length() - 1;

        while (i >= 0 && row.charAt(i) == ' ') {
            i--;
        }

        return i;
    }

    public boolean matches(CompressingRecipeInput input) {
        if (input.ingredientCount() != this.ingredientCount) {
            return false;
        } else {
            if (input.width() == this.width && input.height() == this.height) {
                if (!this.symmetrical && matches(input, true)) {
                    return true;
                }

                return matches(input, false);
            }

            return false;
        }
    }

    private boolean matches(CompressingRecipeInput input, boolean symmetrical) {
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                Optional<Ingredient> optional;

                if (symmetrical) {
                    optional = this.ingredients.get(this.width - j - 1 + i * this.width);
                } else {
                    optional = this.ingredients.get(j + i * this.width);
                }

                ItemStack itemstack = input.getItem(j, i);

                if (!Ingredient.testOptionalIngredient(optional, itemstack)) {
                    return false;
                }
            }
        }

        return true;
    }

    public int width() {
        return this.width;
    }

    public int height() {
        return this.height;
    }

    public List<Optional<Ingredient>> ingredients() {
        return this.ingredients;
    }

    public record Data(Map<Character, Ingredient> key, List<String> pattern) {
        private static final Codec<List<String>> PATTERN_CODEC = Codec.STRING.listOf().comapFlatMap(symbols -> {
            if (symbols.size() > 3) {
                return DataResult.error(() -> "Invalid pattern: too many rows, %s is maximum".formatted(3));
            } else if (symbols.isEmpty()) {
                return DataResult.error(() -> "Invalid pattern: empty pattern not allowed");
            } else {
                int i = symbols.getFirst().length();

                for (String s : symbols) {
                    if (s.length() > 3) {
                        return DataResult.error(() -> "Invalid pattern: too many columns, %s is maximum".formatted(3));
                    }

                    if (i != s.length()) {
                        return DataResult.error(() -> "Invalid pattern: each row must be the same width");
                    }
                }

                return DataResult.success(symbols);
            }
        }, Function.identity());
        private static final Codec<Character> SYMBOL_CODEC = Codec.STRING.comapFlatMap(symbol -> {
            if (symbol.length() != 1) {
                return DataResult.error(() -> "Invalid key entry: '" + symbol + "' is an invalid symbol (must be 1 character only).");
            } else {
                return " ".equals(symbol) ? DataResult.error(() -> "Invalid key entry: ' ' is a reserved symbol.") : DataResult.success(symbol.charAt(0));
            }
        }, String::valueOf);
        public static final MapCodec<Data> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ExtraCodecs.strictUnboundedMap(SYMBOL_CODEC, Ingredient.CODEC).fieldOf("key").forGetter(data -> data.key),
                PATTERN_CODEC.fieldOf("pattern").forGetter(data -> data.pattern)
        ).apply(instance, Data::new));
    }
}
