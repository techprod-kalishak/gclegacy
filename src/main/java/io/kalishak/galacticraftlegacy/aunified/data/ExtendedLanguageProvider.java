package io.kalishak.galacticraftlegacy.aunified.data;

import com.google.common.collect.Iterators;
import net.minecraft.data.PackOutput;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.level.block.ColorCollection;
import net.minecraft.world.level.block.WeatheringCopperCollection;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.Iterator;
import java.util.function.Function;

public abstract class ExtendedLanguageProvider extends LanguageProvider {
    protected final String namespace;

    public ExtendedLanguageProvider(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
        this.namespace = modid;
    }

    protected <Id> void addColorCollection(ColorCollection<Id> colorCollection, Function<Id, Identifier> toIdentifier, String targetPath) {
        colorCollection.forEach(id -> {
            Identifier identifier = toIdentifier.apply(id);
            String capitalized = capitalizeFirstCharForAll(identifier.getPath().split("_"));

            add(Util.makeDescriptionId(targetPath, identifier), capitalized);
        });
    }

    protected <Id> void addCopperCollection(WeatheringCopperCollection<Id> collection, Function<Id, Identifier> toIdentifier, String targetPath) {
        collection.forEach(id -> {
            Identifier identifier = toIdentifier.apply(id);
            String capitalized = capitalizeFirstCharForAll(identifier.getPath().split("_"));

            add(Util.makeDescriptionId(targetPath, identifier), capitalized);
        });
    }

    protected <R> void add(ResourceKey<R> id, String translation) {
        add(Util.makeDescriptionId(id.registry().getPath(), id.identifier()), translation);
    }

    protected void add(BlockItemId id, String translation) {
        add(id.block(), translation);
    }

    protected <R> void addWithSuffix(ResourceKey<R> resourceKey, String suffix, String translation) {
        add(translatable(resourceKey, suffix), translation);
    }

    protected void addAdvancement(String id, String title, String description) {
        add("advancements.%s.%s.title".formatted(this.namespace, id), title);
        add("advancements.%s.%s.description".formatted(this.namespace, id), description);
    }

    protected static <R> String translatable(ResourceKey<R> resourceKey, String suffix) {
        return resourceKey.identifier().toLanguageKey(resourceKey.registry().getPath(), suffix);
    }

    protected static String capitalizeFirstChar(String origin) {
        return Character.toUpperCase(origin.charAt(0)) + origin.substring(1);
    }

    protected static String capitalizeFirstCharForAll(String... splitStrings) {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<String> parts = Iterators.forArray(splitStrings);

        while (parts.hasNext()) {
            stringBuilder.append(capitalizeFirstChar(parts.next()));

            if (parts.hasNext()) {
                stringBuilder.append(" ");
            }
        }

        return stringBuilder.toString();
    }
}
