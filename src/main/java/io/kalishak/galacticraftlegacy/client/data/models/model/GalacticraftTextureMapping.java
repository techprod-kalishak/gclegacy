/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.data.models.model;

import io.kalishak.galacticraftlegacy.Galacticraft;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

import static net.minecraft.client.data.models.model.TextureMapping.getBlockTexture;

public class GalacticraftTextureMapping {
    private static final Material BASIC_MACHINE = new Material(Identifier.fromNamespaceAndPath(Galacticraft.MODID, "block/machine"));
    private static final Material ADVANCED_MACHINE = new Material(Identifier.fromNamespaceAndPath(Galacticraft.MODID, "block/advanced_machine"));

    public static TextureMapping basicMachine(Material base) {
        return new TextureMapping()
                .put(TextureSlot.NORTH, withSuffix(base, "_front"))
                .put(TextureSlot.SOUTH, withSuffix(BASIC_MACHINE, "_side"))
                .put(TextureSlot.EAST, withSuffix(BASIC_MACHINE, "_energy_output"))
                .put(TextureSlot.WEST, withSuffix(BASIC_MACHINE, "_side"))
                .put(TextureSlot.UP, withSuffix(BASIC_MACHINE, "_top"))
                .put(TextureSlot.DOWN, withSuffix(BASIC_MACHINE, "_top"))
                .put(TextureSlot.PARTICLE, withSuffix(BASIC_MACHINE, "_top"));
    }

    public static TextureMapping advancedMachine(Material base) {
        return new TextureMapping()
                .put(TextureSlot.NORTH, withSuffix(base, "_front"))
                .put(TextureSlot.SOUTH, withSuffix(ADVANCED_MACHINE, "_side"))
                .put(TextureSlot.EAST, withSuffix(ADVANCED_MACHINE, "_energy_output"))
                .put(TextureSlot.WEST, withSuffix(ADVANCED_MACHINE, "_side"))
                .put(TextureSlot.UP, withSuffix(ADVANCED_MACHINE, "_top"))
                .put(TextureSlot.DOWN, withSuffix(ADVANCED_MACHINE, "_top"))
                .put(TextureSlot.PARTICLE, withSuffix(ADVANCED_MACHINE, "_top"));
    }

    public static TextureMapping oxygenCollector(Block block) {
        return new TextureMapping()
                .put(TextureSlot.NORTH, getBlockTexture(block, "_side"))
                .put(TextureSlot.SOUTH, getBlockTexture(block, "_side"))
                .put(TextureSlot.EAST, withSuffix(BASIC_MACHINE, "_energy_input"))
                .put(TextureSlot.WEST, withSuffix(BASIC_MACHINE, "_oxygen_output"))
                .put(TextureSlot.UP, getBlockTexture(block, "_side"))
                .put(TextureSlot.DOWN, getBlockTexture(block, "_side"))
                .put(TextureSlot.PARTICLE, getBlockTexture(block, "_side"));
    }

    public static TextureMapping defaultWithTop(Block block) {
        return new TextureMapping()
                .put(TextureSlot.TEXTURE, getBlockTexture(block))
                .put(TextureSlot.TOP, getBlockTexture(block, "_top"));
    }

    public static TextureMapping basicMachine(Block block) {
        return basicMachine(getBlockTexture(block));
    }

    public static TextureMapping advancedMachine(Block block) {
        return advancedMachine(getBlockTexture(block));
    }

    private static Material withSuffix(Material material, String suffix) {
        return new Material(material.sprite().withSuffix(suffix));
    }
}
