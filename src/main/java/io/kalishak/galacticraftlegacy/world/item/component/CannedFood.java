/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Prediction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.ConsumableListener;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public record CannedFood(Component title, FoodProperties foodProperties, Consumable consumable, Optional<ItemStackTemplate> additionalRemainder) {
    public static Builder builder() {
        return new Builder();
    }

    public record CannedComponent(Component title, Optional<ItemStackTemplate> additionalRemainder) implements ConsumableListener, TooltipProvider {
        public static final Codec<CannedComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ComponentSerialization.CODEC.fieldOf("title").forGetter(CannedComponent::title),
                ItemStackTemplate.CODEC.optionalFieldOf("additional_remainder").forGetter(CannedComponent::additionalRemainder)
        ).apply(instance, CannedComponent::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, CannedComponent> STREAM_CODEC = StreamCodec.composite(
                ComponentSerialization.TRUSTED_STREAM_CODEC, CannedComponent::title,
                ItemStackTemplate.STREAM_CODEC.apply(ByteBufCodecs::optional), CannedComponent::additionalRemainder,
                CannedComponent::new
        );

        @Override
        public void onConsume(@NonNull Level level, LivingEntity user, @NonNull ItemStack stack, @NonNull Consumable consumable) {
            RandomSource random = user.getRandom();
            if (user instanceof Player player && this.additionalRemainder.isPresent()) {
                if (random.nextFloat() < 0.2621379F) {
                    ItemStack remainder = this.additionalRemainder.get().create();

                    if (!player.getInventory().add(remainder)) {
                        player.drop(remainder, false, Prediction.PREDICTED);
                    }
                }
            }
        }

        @Override
        public void addToTooltip(Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter) {
            consumer.accept(this.title);
        }
    }

    public static Item.Properties createProperties(CannedFood cannedFood) {
        Item.Properties properties = new Item.Properties();

        return properties
                .component(GalacticraftDataComponents.CANNED_FOOD::value, new CannedComponent(cannedFood.title, cannedFood.additionalRemainder))
                .food(cannedFood.foodProperties(), cannedFood.consumable())
                .craftRemainder(new ItemStackTemplate(GalacticraftItems.TIN_CANISTER))
                .overrideDescription("item.galacticraftlegacy.canned_food");
    }

    public static class Builder {
        private Component name = CommonComponents.EMPTY;
        private final FoodProperties.Builder foodBuilder = new FoodProperties.Builder();
        private final Consumable.Builder consumableBuilder = Consumable.builder();
        private @Nullable ItemStackTemplate additionalRemainder;

        private Builder() {}

        public Builder name(Component name) {
            this.name = name;
            return this;
        }

        public Builder food(UnaryOperator<FoodProperties.Builder> foodBuilder) {
            foodBuilder.apply(this.foodBuilder);
            return this;
        }

        public Builder consumable(UnaryOperator<Consumable.Builder> consumableBuilder) {
            consumableBuilder.apply(this.consumableBuilder);
            return this;
        }

        public Builder additionalRemainder(@Nullable ItemStackTemplate additionalRemainder) {
            this.additionalRemainder = additionalRemainder;
            return this;
        }

        public CannedFood build() {
            return new CannedFood(this.name, this.foodBuilder.build(), this.consumableBuilder.build(), Optional.ofNullable(this.additionalRemainder));
        }
    }
}
