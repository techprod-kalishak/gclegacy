/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.ConsumableListener;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public record CannedFood(Component title, FoodProperties foodProperties, Consumable consumable, Optional<ItemStackTemplate> additionalRemainder) {
    public static Builder builder() {
        return new Builder();
    }

    public record CannedComponent(Optional<ItemStackTemplate> additionalRemainder) implements ConsumableListener {
        public static final Codec<CannedComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ItemStackTemplate.CODEC.optionalFieldOf("additional_remainder").forGetter(CannedComponent::additionalRemainder)
        ).apply(instance, CannedComponent::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, CannedComponent> STREAM_CODEC = ItemStackTemplate.STREAM_CODEC.apply(ByteBufCodecs::optional).map(CannedComponent::new, CannedComponent::additionalRemainder);

        @Override
        public void onConsume(Level level, LivingEntity user, ItemStack stack, Consumable consumable) {
            RandomSource random = user.getRandom();
            if (user instanceof Player player && this.additionalRemainder.isPresent()) {
                if (random.nextFloat() < 0.2621379F) {
                    ItemStack remainder = this.additionalRemainder.get().create();

                    if (!player.getInventory().add(remainder)) {
                        player.drop(remainder, false);
                    }
                }
            }
        }
    }

    public static Item.Properties createProperties(CannedFood cannedFood) {
        Item.Properties properties = new Item.Properties();

        if (cannedFood.additionalRemainder.isPresent()) {
            properties.component(GalacticraftDataComponents.CANNED_FOOD::value, new CannedComponent(cannedFood.additionalRemainder()));
        }

        return properties
                .food(cannedFood.foodProperties(), cannedFood.consumable())
                .craftRemainder(new ItemStackTemplate(GalacticraftItems.TIN_CANISTER));
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
