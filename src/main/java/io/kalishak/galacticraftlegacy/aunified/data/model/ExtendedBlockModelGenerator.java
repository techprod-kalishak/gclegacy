/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.aunified.data.model;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class ExtendedBlockModelGenerator extends BlockModelGenerators {
    public ExtendedBlockModelGenerator(Consumer<BlockModelDefinitionGenerator> blockStateOutput, ItemModelOutput itemModelOutput, BiConsumer<Identifier, ModelInstance> modelOutput) {
        super(blockStateOutput, itemModelOutput, modelOutput);
    }

    public void createSlab(Block slabBlock, Block donor, Function<Block, TextureMapping> textureMapping) {
        TextureMapping mapping = textureMapping.apply(donor);
        Identifier bottom = ModelTemplates.SLAB_BOTTOM.create(slabBlock, mapping, this.modelOutput);
        Identifier top = ModelTemplates.SLAB_TOP.create(slabBlock, mapping, this.modelOutput);
        this.blockStateOutput.accept(BlockModelGenerators.createSlab(slabBlock, BlockModelGenerators.plainVariant(bottom), BlockModelGenerators.plainVariant(top), BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(donor))));
        this.registerSimpleItemModel(slabBlock, bottom);
    }

    public void createStairs(Block slabBlock, Block donor, Function<Block, TextureMapping> textureMapping) {
        TextureMapping mapping = textureMapping.apply(donor);
        MultiVariant inner = BlockModelGenerators.plainVariant(ModelTemplates.STAIRS_INNER.create(slabBlock, mapping, this.modelOutput));
        Identifier straight = ModelTemplates.STAIRS_STRAIGHT.create(slabBlock, mapping, this.modelOutput);
        MultiVariant outer = BlockModelGenerators.plainVariant(ModelTemplates.STAIRS_OUTER.create(slabBlock, mapping, this.modelOutput));
        this.blockStateOutput.accept(BlockModelGenerators.createStairs(slabBlock, inner, BlockModelGenerators.plainVariant(straight), outer));
        this.registerSimpleItemModel(slabBlock, straight);
    }

    public void createWall(Block wallBlock, Block donor, Function<Block, TextureMapping> textureMapping) {
        TextureMapping mapping = textureMapping.apply(donor);

        MultiVariant post = BlockModelGenerators.plainVariant(ModelTemplates.WALL_POST.create(wallBlock, mapping, this.modelOutput));
        MultiVariant low = BlockModelGenerators.plainVariant(ModelTemplates.WALL_LOW_SIDE.create(wallBlock, mapping, this.modelOutput));
        MultiVariant high = BlockModelGenerators.plainVariant(ModelTemplates.WALL_TALL_SIDE.create(wallBlock, mapping, this.modelOutput));
        this.blockStateOutput.accept(BlockModelGenerators.createWall(wallBlock, post, low, high));

        Identifier inventory = ModelTemplates.WALL_INVENTORY.create(wallBlock, mapping, this.modelOutput);
        registerSimpleItemModel(wallBlock, inventory);
    }
}
