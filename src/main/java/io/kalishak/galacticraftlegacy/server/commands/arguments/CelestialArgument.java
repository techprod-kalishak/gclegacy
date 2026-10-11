package io.kalishak.galacticraftlegacy.server.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.kalishak.galacticraftlegacy.server.commands.arguments.input.CelestialInput;
import net.minecraft.commands.CommandBuildContext;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class CelestialArgument implements ArgumentType<CelestialInput> {
    private static final Collection<String> EXAMPLES = Arrays.asList("moon", "galacticraftlegacy:mars");

    private final CelestialParser parser;

    public CelestialArgument(CommandBuildContext context) {
        this.parser = new CelestialParser(context);
    }

    @Override
    public CelestialInput parse(StringReader reader) throws CommandSyntaxException {
        return this.parser.parse(reader);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return this.parser.fillSuggestions(builder);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static CelestialArgument celestial(CommandBuildContext context) {
        return new CelestialArgument(context);
    }

    public static <S> CelestialInput getCelestial(CommandContext<S> context, String name) {
        return context.getArgument(name, CelestialInput.class);
    }
}
