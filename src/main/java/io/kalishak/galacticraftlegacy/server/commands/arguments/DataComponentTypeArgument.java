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
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class DataComponentTypeArgument implements ArgumentType<Identifier> {
    private static final Collection<String> EXAMPLES = List.of("minecraft:custom_name", "body_owner", "body_position");
    private static final DynamicCommandExceptionType ERROR_INVALID_VALUE = new DynamicCommandExceptionType(value -> Component.literal("Unknown data component " + value));

    public DataComponentTypeArgument() {
    }

    @Override
    public Identifier parse(StringReader reader) throws CommandSyntaxException {
        return Identifier.read(reader);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return context.getSource() instanceof SharedSuggestionProvider sharedSuggestionProvider
                ? SharedSuggestionProvider.suggestResource(sharedSuggestionProvider.registryAccess().lookupOrThrow(Registries.DATA_COMPONENT_TYPE).listElements().map(v -> v.key().identifier()), builder)
                : Suggestions.empty();
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static DataComponentTypeArgument dataComponentType() {
        return new DataComponentTypeArgument();
    }

    @SuppressWarnings("unchecked")
    public static <T> DataComponentType<T> getDataComponentType(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        Identifier id = context.getArgument(name, Identifier.class);
        ResourceKey<DataComponentType<?>> resourceKey = ResourceKey.create(Registries.DATA_COMPONENT_TYPE, id);
        RegistryAccess access = context.getSource().registryAccess();
        Optional<Holder.Reference<DataComponentType<?>>> optional = access.get(resourceKey);

        if (optional.isEmpty()) {
            throw ERROR_INVALID_VALUE.create(id);
        }

        return (DataComponentType<T>) optional.get().value();
    }
}
