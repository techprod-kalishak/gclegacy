package io.kalishak.galacticraftlegacy.server.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.serialization.DataResult;
import io.kalishak.galacticraftlegacy.galaxies.celestial.Celestial;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.server.commands.arguments.input.CelestialInput;
import it.unimi.dsi.fastutil.objects.ReferenceArraySet;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class CelestialParser {
    private static final DynamicCommandExceptionType ERROR_UNKNOWN_CELESTIAL = new DynamicCommandExceptionType(
            id -> Component.translatableEscape("argument.item.id.invalid", id)
    );
    private static final DynamicCommandExceptionType ERROR_UNKNOWN_COMPONENT = new DynamicCommandExceptionType(
            id -> Component.translatableEscape("arguments.item.component.unknown", id)
    );
    private static final Dynamic2CommandExceptionType ERROR_MALFORMED_COMPONENT = new Dynamic2CommandExceptionType(
            (type, message) -> Component.translatableEscape("arguments.item.component.malformed", type, message)
    );
    private static final SimpleCommandExceptionType ERROR_EXPECTED_COMPONENT = new SimpleCommandExceptionType(
            Component.translatable("arguments.item.component.expected")
    );
    private static final DynamicCommandExceptionType ERROR_REPEATED_COMPONENT = new DynamicCommandExceptionType(
            id -> Component.translatableEscape("arguments.item.component.repeated", id)
    );
    public static final char SYNTAX_START_COMPONENTS = '[';
    public static final char SYNTAX_END_COMPONENTS = ']';
    public static final char SYNTAX_COMPONENT_SEPARATOR = ',';
    public static final char SYNTAX_COMPONENT_ASSIGNMENT = '=';
    public static final char SYNTAX_REMOVED_COMPONENT = '!';
    private static final Function<SuggestionsBuilder, CompletableFuture<Suggestions>> SUGGEST_NOTHING = SuggestionsBuilder::buildFuture;
    private final HolderLookup.RegistryLookup<Celestial> celestials;
    private final RegistryOps<Tag> registryOps;
    private final TagParser<Tag> tagParser;

    public CelestialParser(HolderLookup.Provider registries) {
        this.celestials = registries.lookupOrThrow(GalacticraftRegistries.Keys.CELESTIAL);
        this.registryOps = registries.createSerializationContext(NbtOps.INSTANCE);
        this.tagParser = TagParser.create(this.registryOps);
    }

    public CelestialInput parse(StringReader reader) throws CommandSyntaxException {
        MutableObject<Holder<Celestial>> result = new MutableObject<>();
        DataComponentPatch.Builder builder = DataComponentPatch.builder();
        Visitor visitor = new Visitor() {
            @Override
            public void visit(Holder<Celestial> celestial) {
                result.setValue(celestial);
            }

            @Override
            public <T> void visitComponent(DataComponentType<T> type, T value) {
                builder.set(type, value);
            }

            @Override
            public <T> void visitRemovedComponent(DataComponentType<T> type) {
                builder.remove(type);
            }
        };
        parse(reader, visitor);
        Holder<Celestial> celestial = Objects.requireNonNull(result.get(), "Parser gave no celestial");
        DataComponentPatch components = builder.build();

        return new CelestialInput(celestial, components);
    }

    public void parse(StringReader reader, Visitor visitor) throws CommandSyntaxException {
        int cursor = reader.getCursor();

        try {
            new State(reader, visitor).parse();
        } catch (CommandSyntaxException e) {
            reader.setCursor(cursor);
            throw e;
        }
    }

    public CompletableFuture<Suggestions> fillSuggestions(SuggestionsBuilder builder) {
        StringReader reader = new StringReader(builder.getInput());
        reader.setCursor(builder.getStart());

        SuggestionsVisitor handler = new SuggestionsVisitor();
        State state = new State(reader, handler);

        try {
            state.parse();
        } catch (CommandSyntaxException _) {

        }

        return handler.resolveSuggestions(builder, reader);
    }

    private class State {
        private final StringReader reader;
        private final Visitor visitor;

        private State(StringReader reader, Visitor visitor) {
            this.reader = reader;
            this.visitor = visitor;
        }

        public void parse() throws CommandSyntaxException {
            this.visitor.visitSuggestions(this::suggestCelestial);
            readCelestial();
            this.visitor.visitSuggestions(this::suggestStartComponents);

            if (this.reader.canRead() && this.reader.peek() == '[') {
                this.visitor.visitSuggestions(SUGGEST_NOTHING);
                this.readComponents();
            }
        }

        private void readCelestial() throws CommandSyntaxException {
            int cursor = this.reader.getCursor();
            Identifier id = Identifier.read(this.reader);

            this.visitor.visit(CelestialParser.this.celestials.get(ResourceKey.create(GalacticraftRegistries.Keys.CELESTIAL, id)).orElseThrow(() -> {
                this.reader.setCursor(cursor);
                return ERROR_UNKNOWN_CELESTIAL.createWithContext(this.reader, id);
            }));
        }

        private void readComponents() throws CommandSyntaxException {
            this.reader.expect('[');
            this.visitor.visitSuggestions(this::suggestComponentAssignmentOrRemoval);
            Set<DataComponentType<?>> visitedComponents = new ReferenceArraySet<>();

            while (this.reader.canRead() && this.reader.peek() != ']') {
                this.reader.skipWhitespace();

                if (this.reader.canRead() && this.reader.peek() == '!') {
                    this.reader.skip();
                    this.visitor.visitSuggestions(this::suggestComponent);
                    DataComponentType<?> componentType = readComponentType(this.reader);

                    if (!visitedComponents.add(componentType)) {
                        throw ERROR_REPEATED_COMPONENT.create(componentType);
                    }

                    this.visitor.visitRemovedComponent(componentType);
                    this.visitor.visitSuggestions(SUGGEST_NOTHING);
                    this.reader.skipWhitespace();
                } else {
                    DataComponentType<?> componentType = readComponentType(this.reader);

                    if (!visitedComponents.add(componentType)) {
                        throw ERROR_REPEATED_COMPONENT.create(componentType);
                    }

                    this.visitor.visitSuggestions(this::suggestAssignment);
                    this.reader.skipWhitespace();
                    this.reader.expect('=');
                    this.visitor.visitSuggestions(SUGGEST_NOTHING);
                    this.reader.skipWhitespace();
                    this.readComponent(CelestialParser.this.tagParser, CelestialParser.this.registryOps, componentType);
                    this.reader.skipWhitespace();
                }

                this.visitor.visitSuggestions(this::suggestNextOrEndComponents);
                if (!this.reader.canRead() || this.reader.peek() != ',') {
                    break;
                }

                this.reader.skip();
                this.reader.skipWhitespace();
                this.visitor.visitSuggestions(this::suggestComponentAssignmentOrRemoval);

                if (!this.reader.canRead()) {
                    throw ERROR_EXPECTED_COMPONENT.createWithContext(this.reader);
                }
            }

            this.reader.expect(']');
            this.visitor.visitSuggestions(SUGGEST_NOTHING);
        }

        public static DataComponentType<?> readComponentType(StringReader reader) throws CommandSyntaxException {
            if (!reader.canRead()) {
                throw ERROR_EXPECTED_COMPONENT.createWithContext(reader);
            }

            int cursor = reader.getCursor();
            Identifier id = Identifier.read(reader);
            DataComponentType<?> component = BuiltInRegistries.DATA_COMPONENT_TYPE.getValue(id);

            if (component != null && !component.isTransient()) {
                return component;
            }

            reader.setCursor(cursor);
            throw ERROR_UNKNOWN_COMPONENT.createWithContext(reader, id);
        }

        private <T, O> void readComponent(TagParser<O> tagParser, RegistryOps<O> registryOps, DataComponentType<T> componentType) throws CommandSyntaxException {
            int cursor = this.reader.getCursor();
            O tag = tagParser.parseAsArgument(this.reader);
            DataResult<T> result = componentType.codecOrThrow().parse(registryOps, tag);
            this.visitor.visitComponent(componentType, result.getOrThrow(message -> {
                this.reader.setCursor(cursor);
                return ERROR_MALFORMED_COMPONENT.createWithContext(this.reader, componentType.toString(), message);
            }));
        }

        private CompletableFuture<Suggestions> suggestStartComponents(SuggestionsBuilder builder) {
            if (builder.getRemaining().isEmpty()) {
                builder.suggest(String.valueOf('['));
            }

            return builder.buildFuture();
        }

        private CompletableFuture<Suggestions> suggestNextOrEndComponents(SuggestionsBuilder builder) {
            if (builder.getRemaining().isEmpty()) {
                builder.suggest(String.valueOf(','));
                builder.suggest(String.valueOf(']'));
            }

            return builder.buildFuture();
        }

        private CompletableFuture<Suggestions> suggestAssignment(SuggestionsBuilder builder) {
            if (builder.getRemaining().isEmpty()) {
                builder.suggest(String.valueOf('='));
            }

            return builder.buildFuture();
        }

        private CompletableFuture<Suggestions> suggestCelestial(SuggestionsBuilder builder) {
            return SharedSuggestionProvider.suggestResource(CelestialParser.this.celestials.listElementIds().map(ResourceKey::identifier), builder);
        }

        private CompletableFuture<Suggestions> suggestComponentAssignmentOrRemoval(SuggestionsBuilder builder) {
            builder.suggest(String.valueOf('!'));
            return this.suggestComponent(builder, String.valueOf('='));
        }

        private CompletableFuture<Suggestions> suggestComponent(SuggestionsBuilder builder) {
            return this.suggestComponent(builder, "");
        }

        private CompletableFuture<Suggestions> suggestComponent(SuggestionsBuilder builder, String suffix) {
            String contents = builder.getRemaining().toLowerCase(Locale.ROOT);
            SharedSuggestionProvider.filterResources(
                    BuiltInRegistries.DATA_COMPONENT_TYPE.entrySet(), contents, entry -> entry.getKey().identifier(), entry -> {
                        DataComponentType<?> type = entry.getValue();

                        if (type.codec() != null) {
                            Identifier id = entry.getKey().identifier();
                            builder.suggest(id + suffix);
                        }
                    }
            );

            return builder.buildFuture();
        }
    }

    private static class SuggestionsVisitor implements Visitor {
        private Function<SuggestionsBuilder, CompletableFuture<Suggestions>> suggestions = SUGGEST_NOTHING;

        @Override
        public void visitSuggestions(Function<SuggestionsBuilder, CompletableFuture<Suggestions>> suggestions) {
            this.suggestions = suggestions;
        }

        public CompletableFuture<Suggestions> resolveSuggestions(SuggestionsBuilder builder, StringReader reader) {
            return this.suggestions.apply(builder.createOffset(reader.getCursor()));
        }
    }

    public interface Visitor {
        default void visit(Holder<Celestial> celestial) {

        }

        default <T> void visitComponent(DataComponentType<T> type, T value) {

        }

        default <T> void visitRemovedComponent(DataComponentType<T> type) {

        }

        default void visitSuggestions(Function<SuggestionsBuilder, CompletableFuture<Suggestions>> suggestions) {

        }
    }
}
