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
    private static final Material MACHINE = new Material(Identifier.fromNamespaceAndPath(Galacticraft.MODID, "block/machine"));

    public static TextureMapping simpleMachine(Material base) {
        return new TextureMapping()
                .put(TextureSlot.NORTH, withSuffix(base, "_front"))
                .put(TextureSlot.SOUTH, withSuffix(MACHINE, "_side"))
                .put(TextureSlot.EAST, withSuffix(MACHINE, "_energy_output"))
                .put(TextureSlot.WEST, withSuffix(MACHINE, "_side"))
                .put(TextureSlot.UP, withSuffix(MACHINE, "_top"))
                .put(TextureSlot.DOWN, withSuffix(MACHINE, "_top"))
                .put(TextureSlot.PARTICLE, withSuffix(MACHINE, "_top"));
    }

    public static TextureMapping rotatableBlock(Block block) {
        return new TextureMapping()
                .put(TextureSlot.PARTICLE, getBlockTexture(block, "_side"))
                .put(TextureSlot.DOWN, getBlockTexture(block, "_bottom"))
                .put(TextureSlot.UP, getBlockTexture(block, "_top"))
                .put(TextureSlot.NORTH, getBlockTexture(block, "_side"))
                .put(TextureSlot.EAST, getBlockTexture(block, "_side"))
                .put(TextureSlot.SOUTH, getBlockTexture(block, "_side"))
                .put(TextureSlot.WEST, getBlockTexture(block, "_side"));
    }

    public static TextureMapping oxygenCollector(Block block) {
        return new TextureMapping()
                .put(TextureSlot.NORTH, getBlockTexture(block, "_side"))
                .put(TextureSlot.SOUTH, getBlockTexture(block, "_side"))
                .put(TextureSlot.EAST, withSuffix(MACHINE, "_energy_input"))
                .put(TextureSlot.WEST, withSuffix(MACHINE, "_oxygen_output"))
                .put(TextureSlot.UP, getBlockTexture(block, "_side"))
                .put(TextureSlot.DOWN, getBlockTexture(block, "_side"))
                .put(TextureSlot.PARTICLE, getBlockTexture(block, "_side"));
    }

    public static TextureMapping simpleMachine(Block block) {
        return simpleMachine(getBlockTexture(block));
    }

    private static Material withSuffix(Material material, String suffix) {
        return new Material(material.sprite().withSuffix(suffix));
    }
}
