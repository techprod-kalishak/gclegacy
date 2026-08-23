package io.kalishak.galacticraftlegacy.aunified.data.model;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.data.PackOutput;

import java.util.function.Function;

public abstract class ExtendedModelProvider<B extends ExtendedBlockModelGenerator, I extends ExtendedItemModelGenerator> extends ModelProvider {
    private final Function<BlockModelGenerators, B> blockModelGenerators;
    private final Function<ItemModelGenerators, I> itemModelGenerators;

    public ExtendedModelProvider(PackOutput output, String modId, Function<BlockModelGenerators, B> blockModelGenerators, Function<ItemModelGenerators, I> itemModelGenerators) {
        super(output, modId);
        this.blockModelGenerators = blockModelGenerators;
        this.itemModelGenerators = itemModelGenerators;
    }

    @Override
    protected final void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        registerExtendedModels(this.blockModelGenerators.apply(blockModels), this.itemModelGenerators.apply(itemModels));
    }

    protected abstract void registerExtendedModels(B blockGen, I itemGen);
}
