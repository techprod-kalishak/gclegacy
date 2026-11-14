package io.kalishak.galacticraftlegacy.references;

import io.kalishak.galacticraftlegacy.Galacticraft;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.Util;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public sealed interface KeyComponent {
    String asString();
    MutableComponent translated();
    MutableComponent translatedArg(Object... arg);

    default void translate(BiConsumer<String, String> dataProvider, String text) {
        dataProvider.accept(asString(), text);
    }

    static KeyComponent direct(String translationKey) {
        return new Direct(translationKey, UnaryOperator.identity());
    }

    static KeyComponent direct(String translationKey, ChatFormatting color) {
        return new Direct(translationKey, style -> style.withColor(color));
    }

    static <T> KeyComponent withArgEscape(String translationKey) {
        return new DirectEscape(translationKey, UnaryOperator.identity());
    }

    static <T> KeyComponent direct(ResourceKey<T> key, ChatFormatting color) {
        return new Direct(Util.makeDescriptionId(key.registry().getPath(), key.identifier()), style -> style.withColor(color));
    }

    static <T extends Enum<T> & StringRepresentable> Map<T, KeyComponent> mapKey(String baseKey, Class<T> enumClass) {
        return Util.makeEnumMap(enumClass, type -> direct(baseKey + "." + type.getSerializedName()));
    }

    record Direct(String translationKey, UnaryOperator<Style> styled) implements KeyComponent {
        @Override
        public String asString() {
            return this.translationKey;
        }

        @Override
        public MutableComponent translated() {
            return Component.translatable(this.translationKey).withStyle(this.styled);
        }

        @Override
        public MutableComponent translatedArg(Object... arg) {
            if (arg == null || arg.length == 0) {
                return translated();
            }

            return Component.translatable(this.translationKey, arg);
        }
    }

    record DirectEscape(String translationKey, UnaryOperator<Style> styled) implements KeyComponent {
        @Override
        public String asString() {
            return this.translationKey;
        }

        @Override
        public MutableComponent translated() {
            return Component.translatableEscape(this.translationKey);
        }

        @Override
        public MutableComponent translatedArg(Object... arg) {
            if (arg == null || arg.length == 0) {
                return translated();
            }

            return Component.translatableEscape(this.translationKey, arg).withStyle(this.styled);
        }
    }

    record Mapped<T extends Enum<T> & StringRepresentable>(Map<T, KeyComponent> components, Class<T> enumClass) implements KeyComponent {
        @Override
        public String asString() {
            return this.components.get(this.enumClass.getEnumConstants()[0]).asString();
        }

        @Override
        public MutableComponent translated() {
            throw new IllegalStateException("Called KeyComponent$Supplied#asComponent() without key argument, this will return null");
        }

        @Override
        @SuppressWarnings("unchecked")
        public MutableComponent translatedArg(Object... arg) {
            if (arg != null && arg.length > 0) {
                if (this.enumClass.isInstance(arg[0])) {
                    T castKey = (T) arg[0];

                    return this.components.get(castKey).translatedArg(castKey.getSerializedName());
                }
            }

            throw new IllegalArgumentException(String.format("args are either null or not an instance of %s", this.enumClass.getName()));
        }

        @Override
        public void translate(BiConsumer<String, String> dataProvider, String text) {
            Galacticraft.LOGGER.debug("DataProvider runs a Mapped instance of KeyComponent, use forEach instead");
        }

        public void forEach(BiConsumer<String, String> dataProvider, Function<T, String> text) {
            this.components.forEach((type, key) -> key.translate(dataProvider, text.apply(type)));
        }
    }

    record Nested(String translationKey, KeyComponent other, UnaryOperator<Style> styled) implements KeyComponent {
        @Override
        public String asString() {
            return this.translationKey;
        }

        @Override
        public MutableComponent translated() {
            return Component.translatable(this.translationKey, this.other.translated()).withStyle(this.styled);
        }

        @Override
        public MutableComponent translatedArg(Object... arg) {
            if (arg == null || arg.length == 0) {
                return translated();
            }

            return Component.translatable(this.translationKey, this.other.translated(), arg).withStyle(this.styled);
        }
    }
}
