/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.server.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.registry.SchematicVariant;
import io.kalishak.galacticraftlegacy.registry.SchematicVariants;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class SchematicArgument implements ArgumentType<Identifier> {
    private static final Collection<String> EXAMPLES = Stream.of(SchematicVariants.TIER_2_ROCKET, SchematicVariants.MOON_BUGGY)
            .map(key -> key.identifier().toString())
            .toList();
    private static final DynamicCommandExceptionType ERROR_INVALID_VALUE = new DynamicCommandExceptionType(
            value -> Component.translatableEscape("galacticraft.argument.schematic.invalid", value)
    );

    @Override
    public Identifier parse(StringReader reader) throws CommandSyntaxException {
        return Identifier.read(reader);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return context.getSource() instanceof SharedSuggestionProvider provider
                ? SharedSuggestionProvider.suggestResource(provider.registryAccess().lookupOrThrow(GalacticraftRegistries.Keys.SCHEMATIC).keySet(), builder)
                : Suggestions.empty();
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static SchematicArgument schematic() {
        return new SchematicArgument();
    }

    public static ResourceKey<SchematicVariant> getSchematic(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        Identifier location = context.getArgument(name, Identifier.class);
        ResourceKey<SchematicVariant> schematic = ResourceKey.create(GalacticraftRegistries.Keys.SCHEMATIC, location);
        Optional<Holder.Reference<SchematicVariant>> variantHolder = context.getSource().getServer().registryAccess().holder(schematic);

        if (variantHolder.isEmpty()) {
            throw ERROR_INVALID_VALUE.create(location);
        }

        return schematic;
    }
}
