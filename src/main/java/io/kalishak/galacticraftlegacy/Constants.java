package io.kalishak.galacticraftlegacy;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.util.function.Consumer;

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

    public static void infinite(Consumer<Component> tooltipAdder) {
        tooltipAdder.accept(Component.translatable("item.galacticraftlegacy.infinite").withStyle(ChatFormatting.GREEN));
        tooltipAdder.accept(Component.translatable("item.galacticraftlegacy.creative_only").withStyle(ChatFormatting.RED));
    }
}
