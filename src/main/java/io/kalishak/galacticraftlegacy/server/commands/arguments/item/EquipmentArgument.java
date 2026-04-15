/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.server.commands.arguments.item;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.GameModeArgument;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EquipmentArgument implements ArgumentType<EmergencyEquipment> {
    private static final Collection<String> EXAMPLES = Stream.of(EmergencyEquipment.LIGHT, EmergencyEquipment.FULL).map(EmergencyEquipment::getSerializedName).collect(Collectors.toList());
    private static final DynamicCommandExceptionType ERROR_INVALID = new DynamicCommandExceptionType((equipment) -> Component.translatableEscape("galacticraftlegacy.argument.equipment.invalid", equipment));

    public static EquipmentArgument equipment() {
        return new EquipmentArgument();
    }

    public static EmergencyEquipment getEquipment(CommandContext<?> context, String name) {
        return context.getArgument(name, EmergencyEquipment.class);
    }

    @Override
    public EmergencyEquipment parse(StringReader reader) throws CommandSyntaxException {
        String s = reader.readUnquotedString();
        EmergencyEquipment args = EmergencyEquipment.byName(s);

        if (args == null) {
            throw ERROR_INVALID.createWithContext(reader, s);
        }

        return args;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return context.getSource() instanceof SharedSuggestionProvider ? SharedSuggestionProvider.suggest(Arrays.stream(EmergencyEquipment.values()).map(EmergencyEquipment::getSerializedName), builder) : Suggestions.empty();
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
