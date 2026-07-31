/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.references;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.config.values.EnergyUnit;
import io.kalishak.galacticraftlegacy.world.item.component.ItemAccessEnergyUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Constants {
    public static final double RADIANS_TO_DEGREES = 180.0D / Math.PI;

    public static Identifier id(String assetName) {
        return Identifier.fromNamespaceAndPath(Galacticraft.MODID, assetName);
    }

    public static Identifier texture(String assetName) {
        return id("textures/" + assetName);
    }

    public static <R> ResourceKey<R> key(ResourceKey<? extends Registry<R>> registryKey, String name) {
        return ResourceKey.create(registryKey, id(name));
    }

    public static <R, T> ResourceKey<T> castKey(ResourceKey<R> original, ResourceKey<? extends Registry<T>> targetRegistry) {
        return ResourceKey.create(targetRegistry, original.identifier());
    }

    public static <R> String translatable(ResourceKey<R> resourceKey, String suffix) {
        return resourceKey.identifier().toLanguageKey(resourceKey.registry().getPath(), suffix);
    }

    public static <T> Supplier<T> ifClient(@Nullable Level level, Supplier<T> clientValue, T defaultValue) {
        if (level != null && level.isClientSide()) {
            return clientValue;
        }

        return () -> defaultValue;
    }

    public static int calculateUnit(int amount, Supplier<EnergyUnit> unitSupplier) {
        return unitSupplier.get().calculate(amount);
    }

    public static void energy(int stored, int capacity, Supplier<EnergyUnit> unitSupplier, Consumer<Component> tooltipAdder) {
        int scaledStored = unitSupplier.get().calculate(stored);
        int scaledCapacity = unitSupplier.get().calculate(capacity);

        tooltipAdder.accept(
                GalacticraftComponents.TOOLTIP_BATTERY.apply("").withStyle(ChatFormatting.GRAY)
                        .append(Component.literal(" " + scaledStored + "/" + scaledCapacity + " " + unitSupplier.get().getUnit())
                                .withStyle(Style.EMPTY.withColor(ItemAccessEnergyUtils.colorFromStorage(stored, capacity))))
        );
    }
}
