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
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import io.kalishak.galacticraftlegacy.world.score.race.SpaceRaceHooks;
import io.kalishak.galacticraftlegacy.world.score.race.SpaceRaceScoreboard;
import io.kalishak.galacticraftlegacy.world.score.race.SpaceRaceTeam;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class SpaceRaceTeamArgument implements ArgumentType<String> {
    private static final Collection<String> EXAMPLES = Arrays.asList("spaceM", "MASA");
    private static final DynamicCommandExceptionType ERROR_SPACE_RACE_NOT_FOUND = new DynamicCommandExceptionType(GalacticraftComponents.COMMAND_ERROR_SPACE_RACE_DONT_EXIST);

    public static SpaceRaceTeamArgument spaceRace() {
        return new SpaceRaceTeamArgument();
    }

    public static SpaceRaceTeam getSpaceRace(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        String id = context.getArgument(name, String.class);
        SpaceRaceScoreboard scoreboard = SpaceRaceHooks.getFromServer(context.getSource().getServer());

        if (scoreboard.getSpaceRaceTeam(name) == null) throw ERROR_SPACE_RACE_NOT_FOUND.create(id);

        return scoreboard.getSpaceRaceTeam(id);
    }

    @Override
    public String parse(StringReader reader) {
        return reader.readUnquotedString();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> contextBuilder, SuggestionsBuilder builder) {
        if (contextBuilder.getSource() instanceof CommandSourceStack stack) {
            SpaceRaceScoreboard scoreboard = SpaceRaceHooks.getFromServer(stack.getServer());

            return SharedSuggestionProvider.suggest(scoreboard.getTeamNames(), builder);
        }

        return Suggestions.empty();
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
