/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.component;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.codec.SerializableEnum;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlotGroup;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.apache.logging.log4j.util.TriConsumer;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Based on vanilla {@link net.minecraft.world.item.component.ItemAttributeModifiers} but for GearEquipment
 * @param modifiers List of attribute modifiers
 */
@SuppressWarnings("deprecation")
public record GearAttributeModifiers(List<GearAttributeModifiers.Entry> modifiers) {
    public static final GearAttributeModifiers EMPTY = new GearAttributeModifiers(List.of());
    public static final Codec<GearAttributeModifiers> CODEC = Entry.CODEC
            .listOf()
            .xmap(GearAttributeModifiers::new, GearAttributeModifiers::modifiers);
    public static final StreamCodec<RegistryFriendlyByteBuf, GearAttributeModifiers> STREAM_CODEC = StreamCodec.composite(
            Entry.STREAM_CODEC.apply(ByteBufCodecs.list()), GearAttributeModifiers::modifiers,
            GearAttributeModifiers::new
    );
    public static final DecimalFormat ATTRIBUTE_MODIFIER_FORMAT = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.ROOT));

    public static Builder builder() {
        return new Builder();
    }

    public GearAttributeModifiers withModifierAdded(Holder<Attribute> attribute, AttributeModifier modifier, GearEquipmentSlotGroup slot) {
        ImmutableList.Builder<Entry> newModifiers = ImmutableList.builderWithExpectedSize(this.modifiers.size() + 1);

        this.modifiers.stream().filter(entry -> !entry.matches(attribute, modifier.id())).forEach(newModifiers::add);
        newModifiers.add(new Entry(attribute, modifier, slot));
        return new GearAttributeModifiers(newModifiers.build());
    }

    public void forEach(GearEquipmentSlotGroup slot, TriConsumer<Holder<Attribute>, AttributeModifier, Display> consumer) {
        this.modifiers.stream().filter(entry -> entry.slot.equals(slot)).forEach(entry -> consumer.accept(entry.attribute, entry.modifier, entry.display));
    }

    public void forEach(GearEquipmentSlotGroup slot, BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
        this.modifiers.stream().filter(entry -> entry.slot.equals(slot)).forEach(entry -> consumer.accept(entry.attribute, entry.modifier));
    }

    public void forEach(GearEquipmentSlot slot, TriConsumer<Holder<Attribute>, AttributeModifier, Display> consumer) {
        this.modifiers.stream().filter(entry -> entry.slot.test(slot)).forEach(entry -> consumer.accept(entry.attribute, entry.modifier, entry.display));
    }

    public double compute(Holder<Attribute> attribute, double baseValue, GearEquipmentSlot slot) {
        double value = baseValue;

        for (Entry entry : this.modifiers) {
            if (entry.slot.test(slot) && entry.attribute == attribute) {
                double amount = entry.modifier.amount();

                value += switch (entry.modifier.operation()) {
                    case ADD_VALUE -> amount;
                    case ADD_MULTIPLIED_BASE -> amount * baseValue;
                    case ADD_MULTIPLIED_TOTAL -> amount * value;
                };
            }
        }

        return value;
    }

    public double compute(Holder<Attribute> attribute, double baseValue, @Nullable GearEquippable equippable) {
        if (equippable != null) {
            return compute(attribute, baseValue, equippable.gearSlot());
        }

        return baseValue;
    }

    public static class Builder {
        private final ImmutableList.Builder<GearAttributeModifiers.Entry> entries = ImmutableList.builder();

        private Builder() {

        }

        public Builder add(Holder<Attribute> attribute, AttributeModifier modifier, GearEquipmentSlotGroup slot) {
            this.entries.add(new GearAttributeModifiers.Entry(attribute, modifier, slot));
            return this;
        }

        public Builder add(Holder<Attribute> attribute, AttributeModifier modifier, GearEquipmentSlotGroup slot, Display display) {
            this.entries.add(new GearAttributeModifiers.Entry(attribute, modifier, slot, display));
            return this;
        }

        public GearAttributeModifiers build() {
            return new GearAttributeModifiers(this.entries.build());
        }
    }

    public interface Display {
        Codec<Display> CODEC = Type.CODEC.dispatch("type", Display::type, type -> type.codec);
        StreamCodec<RegistryFriendlyByteBuf, Display> STREAM_CODEC = Type.STREAM_CODEC.<RegistryFriendlyByteBuf>cast().dispatch(Display::type, Display.Type::streamCodec);

        static Display attributeModifiers() {
            return Display.Default.INSTANCE;
        }

        static Display hidden() {
            return Display.Hidden.INSTANCE;
        }

        static Display override(Component component) {
            return new Display.OverrideText(component);
        }

        Display.Type type();

        void apply(Consumer<Component> consumer, @Nullable Player player, Holder<Attribute> attribute, AttributeModifier modifier);

        record Default() implements Display {
            private static final Display.Default INSTANCE = new Default();
            private static final MapCodec<Default> CODEC = MapCodec.unit(INSTANCE);
            private static final StreamCodec<RegistryFriendlyByteBuf, Default> STREAM_CODEC = StreamCodec.unit(INSTANCE);

            @Override
            public Display.Type type() {
                return Display.Type.DEFAULT;
            }

            @Override
            public void apply(Consumer<Component> consumer, @Nullable Player player, Holder<Attribute> attribute, AttributeModifier modifier) {
                double amount = modifier.amount();
                boolean displayWithBase = false;
                if (player != null) {
                    if (modifier.is(Item.BASE_ATTACK_DAMAGE_ID)) {
                        amount += player.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
                        displayWithBase = true;
                    } else if (modifier.is(Item.BASE_ATTACK_SPEED_ID)) {
                        amount += player.getAttributeBaseValue(Attributes.ATTACK_SPEED);
                        displayWithBase = true;
                    }
                }

                double displayAmount;
                if (modifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                        || modifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
                    displayAmount = amount * 100.0;
                } else if (attribute.is(Attributes.KNOCKBACK_RESISTANCE)) {
                    displayAmount = amount * 10.0;
                } else {
                    displayAmount = amount;
                }

                if (displayWithBase) {
                    consumer.accept(
                            CommonComponents.space()
                                    .append(
                                            Component.translatable(
                                                    "attribute.modifier.equals." + modifier.operation().id(),
                                                    ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(displayAmount),
                                                    Component.translatable(attribute.value().getDescriptionId())
                                            )
                                    )
                                    .withStyle(ChatFormatting.DARK_GREEN)
                    );
                } else if (amount > 0.0) {
                    consumer.accept(
                            Component.translatable(
                                            "attribute.modifier.plus." + modifier.operation().id(),
                                            ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(displayAmount),
                                            Component.translatable(attribute.value().getDescriptionId())
                                    )
                                    .withStyle(attribute.value().getStyle(true))
                    );
                } else if (amount < 0.0) {
                    consumer.accept(
                            Component.translatable(
                                            "attribute.modifier.take." + modifier.operation().id(),
                                            ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(-displayAmount),
                                            Component.translatable(attribute.value().getDescriptionId())
                                    )
                                    .withStyle(attribute.value().getStyle(false))
                    );
                }
            }
        }

        record Hidden() implements Display {
            private static final Display.Hidden INSTANCE = new Hidden();
            private static final MapCodec<Hidden> CODEC = MapCodec.unit(INSTANCE);
            private static final StreamCodec<RegistryFriendlyByteBuf, Hidden> STREAM_CODEC = StreamCodec.unit(INSTANCE);

            @Override
            public Display.Type type() {
                return Display.Type.HIDDEN;
            }

            @Override
            public void apply(Consumer<Component> consumer, @Nullable Player player, Holder<Attribute> attribute, AttributeModifier modifier) {

            }
        }

        record OverrideText(Component component) implements Display {
            private static final MapCodec<Display.OverrideText> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    ComponentSerialization.CODEC.fieldOf("value").forGetter(Display.OverrideText::component)
            ).apply(instance, Display.OverrideText::new));
            private static final StreamCodec<RegistryFriendlyByteBuf, Display.OverrideText> STREAM_CODEC = StreamCodec.composite(
                    ComponentSerialization.STREAM_CODEC, Display.OverrideText::component,
                    Display.OverrideText::new
            );

            @Override
            public Display.Type type() {
                return Display.Type.OVERRIDE;
            }

            @Override
            public void apply(Consumer<Component> consumer, @Nullable Player player, Holder<Attribute> attribute, AttributeModifier modifier) {
                consumer.accept(this.component);
            }
        }

        enum Type implements SerializableEnum {
            DEFAULT("default", 0, Display.Default.CODEC, Display.Default.STREAM_CODEC),
            HIDDEN("hidden", 1, Display.Hidden.CODEC, Display.Hidden.STREAM_CODEC),
            OVERRIDE("override", 2, Display.OverrideText.CODEC, Display.OverrideText.STREAM_CODEC);

            public static final Codec<Type> CODEC = SerializableEnum.codec(Type.class);
            public static final StreamCodec<ByteBuf, Type> STREAM_CODEC = SerializableEnum.streamCodec(Type.class);
            private final String name;
            private final int index;
            private final MapCodec<? extends Display> codec;
            private final StreamCodec<RegistryFriendlyByteBuf, ? extends Display> streamCodec;

            Type(String name, int index, MapCodec<? extends Display> codec, StreamCodec<RegistryFriendlyByteBuf, ? extends Display> streamCodec) {
                this.name = name;
                this.index = index;
                this.codec = codec;
                this.streamCodec = streamCodec;
            }

            @Override
            public @NonNull String getSerializedName() {
                return this.name;
            }

            @Override
            public int getIndex() {
                return this.index;
            }

            private StreamCodec<RegistryFriendlyByteBuf, ? extends Display> streamCodec() {
                return this.streamCodec;
            }
        }
    }

    record Entry(Holder<Attribute> attribute, AttributeModifier modifier, GearEquipmentSlotGroup slot, Display display) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Attribute.CODEC.fieldOf("type").forGetter(Entry::attribute),
                AttributeModifier.MAP_CODEC.forGetter(Entry::modifier),
                GearEquipmentSlotGroup.CODEC.optionalFieldOf("slot", GearEquipmentSlotGroup.ANY).forGetter(Entry::slot),
                Display.CODEC.optionalFieldOf("display", Display.Default.INSTANCE).forGetter(Entry::display)
        ).apply(instance, Entry::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, Entry> STREAM_CODEC = StreamCodec.composite(
                Attribute.STREAM_CODEC, Entry::attribute,
                AttributeModifier.STREAM_CODEC, Entry::modifier,
                GearEquipmentSlotGroup.STREAM_CODEC, Entry::slot,
                Display.STREAM_CODEC, Entry::display,
                Entry::new
        );

        public Entry(Holder<Attribute> attribute, AttributeModifier modifier, GearEquipmentSlotGroup slot) {
            this(attribute, modifier, slot, Display.attributeModifiers());
        }

        public boolean matches(Holder<Attribute> attribute, Identifier id) {
            return attribute().equals(attribute) && modifier().is(id);
        }
    }
}
