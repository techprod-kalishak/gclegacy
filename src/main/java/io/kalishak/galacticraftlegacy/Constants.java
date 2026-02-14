package io.kalishak.galacticraftlegacy;

import io.kalishak.galacticraftlegacy.config.EnergyUnit;
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
    public static Identifier id(String assetName) {
        return Identifier.fromNamespaceAndPath(Galacticraft.MODID, assetName);
    }

    public static Identifier texture(String assetName) {
        return id("textures/" + assetName);
    }

    public static <R> ResourceKey<R> key(ResourceKey<? extends Registry<R>> registryKey, String name) {
        return ResourceKey.create(registryKey, id(name));
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

    public static void infinite(Consumer<Component> tooltipAdder) {
        tooltipAdder.accept(Component.translatable("item.galacticraftlegacy.infinite").withStyle(ChatFormatting.GREEN));
        tooltipAdder.accept(Component.translatable("item.galacticraftlegacy.creative_only").withStyle(ChatFormatting.RED));
    }

    public static void energy(int stored, int capacity, Supplier<EnergyUnit> unitSupplier, Consumer<Component> tooltipAdder) {
        int stored0 = unitSupplier.get().calculate(stored);
        int capacity0 = unitSupplier.get().calculate(stored);

        tooltipAdder.accept(
                Component.translatable("item.galacticraftlegacy.battery.tooltip").withStyle(ChatFormatting.GRAY)
                        .append(Component.literal(stored0 + "/" + capacity0 + " " + unitSupplier.get().getUnit())
                                .withStyle(Style.EMPTY.withColor(ItemAccessEnergyUtils.colorFromStorage(stored, capacity))))
        );
    }
}
