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

    public static TextureMapping simpleMachine(Block block) {
        return simpleMachine(TextureMapping.getBlockTexture(block));
    }

    private static Material withSuffix(Material material, String suffix) {
        return new Material(material.sprite().withSuffix(suffix));
    }
}
