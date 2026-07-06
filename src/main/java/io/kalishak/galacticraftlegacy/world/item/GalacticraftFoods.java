/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item;

import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import io.kalishak.galacticraftlegacy.world.item.component.CannedFood;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Items;

public class GalacticraftFoods {
    public static final FoodProperties CHEESE_SLICE = new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).build();
    public static final FoodProperties BURGER_BUN = new FoodProperties.Builder().nutrition(4).saturationModifier(0.8F).build();
    public static final FoodProperties RAW_BEEF_PATTY = new FoodProperties.Builder().nutrition(2).saturationModifier(0.3F).build();
    public static final FoodProperties BEEF_PATTY = new FoodProperties.Builder().nutrition(4).saturationModifier(0.6F).build();
    public static final FoodProperties CHEESEBURGER = new FoodProperties.Builder().nutrition(14).saturationModifier(1.0F).build();
    public static final FoodProperties CHEESE = new FoodProperties.Builder().nutrition(2).saturationModifier(0.2F).build();

    public static final CannedFood DEHYDRATED_APPLE = CannedFood.builder()
            .name(GalacticraftComponents.ITEM_DEHYDRATED_APPLE)
            .food(builder -> builder.nutrition(8).saturationModifier(0.3F))
            .consumable(builder -> builder.animation(ItemUseAnimation.TOOT_HORN))
            .build();
    public static final CannedFood DEHYDRATED_CARROT = CannedFood.builder()
            .name(GalacticraftComponents.ITEM_DEHYDRATED_CARROT)
            .food(builder -> builder.nutrition(8).saturationModifier(0.6F))
            .build();
    public static final CannedFood DEHYDRATED_MELON = CannedFood.builder()
            .name(GalacticraftComponents.ITEM_DEHYDRATED_MELON)
            .food(builder -> builder.nutrition(4).saturationModifier(0.4F))
            .additionalRemainder(new ItemStackTemplate(Items.MELON_SEEDS))
            .build();
    public static final CannedFood DEHYDRATED_PUMPKIN = CannedFood.builder()
            .name(GalacticraftComponents.ITEM_DEHYDRATED_PUMPKIN)
            .food(builder -> builder.nutrition(4).saturationModifier(0.4F))
            .additionalRemainder(new ItemStackTemplate(Items.PUMPKIN_SEEDS))
            .build();
    public static final CannedFood DEHYDRATED_BEET = CannedFood.builder()
            .name(GalacticraftComponents.ITEM_DEHYDRATED_BEETROOT)
            .food(builder -> builder.nutrition(2).saturationModifier(0.3F))
            .additionalRemainder(new ItemStackTemplate(Items.BEETROOT_SEEDS))
            .build();
    public static final CannedFood DEHYDRATED_POTATO = CannedFood.builder()
            .name(GalacticraftComponents.ITEM_DEHYDRATED_POTATO)
            .food(builder -> builder.nutrition(2).saturationModifier(0.3F))
            .build();
    public static final CannedFood CANNED_BEEF = CannedFood.builder()
            .name(GalacticraftComponents.ITEM_CANNED_BEEF)
            .food(builder -> builder.nutrition(8).saturationModifier(0.6F))
            .build();
}
