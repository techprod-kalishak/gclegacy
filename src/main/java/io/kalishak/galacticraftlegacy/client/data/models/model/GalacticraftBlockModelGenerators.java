package io.kalishak.galacticraftlegacy.client.data.models.model;

import io.kalishak.galacticraftlegacy.aunified.data.model.ExtendedBlockModelGenerator;
import io.kalishak.galacticraftlegacy.client.renderer.special.NasaWorkbenchSpecialRenderer;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.world.level.block.AbstractPadBlock;
import io.kalishak.galacticraftlegacy.world.level.block.FluidTankBlock;
import io.kalishak.galacticraftlegacy.world.level.block.MagneticCraftingBlock;
import io.kalishak.galacticraftlegacy.world.level.block.PadState;
import io.kalishak.galacticraftlegacy.world.level.block.wire.HeavyWireBlock;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.blockstate.UnbakedMutator;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class GalacticraftBlockModelGenerators extends ExtendedBlockModelGenerator {
    public GalacticraftBlockModelGenerators(BlockModelGenerators gen) {
        super(gen.blockStateOutput, gen.itemModelOutput, gen.modelOutput);
    }

    public void noBlockGen(Block block) {
        Identifier blockModel = BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/");
        createNonTemplateModelBlock(block);
        registerSimpleItemModel(block, blockModel);
    }

    public void createCauldron(Block block, Block liquidBlock) {
        this.blockStateOutput.accept(createSimpleBlock(
                block,
                plainVariant(ModelTemplates.CAULDRON_FULL.create(block, TextureMapping.cauldron(TextureMapping.getBlockTexture(liquidBlock, "_still")), this.modelOutput)))
        );
    }

    public void createBarsAndItem(Block block, TextureMapping textures) {
        createBars(
                block,
                ModelTemplates.BARS_POST_ENDS.create(block, textures, this.modelOutput),
                ModelTemplates.BARS_POST.create(block, textures, this.modelOutput),
                ModelTemplates.BARS_CAP.create(block, textures, this.modelOutput),
                ModelTemplates.BARS_CAP_ALT.create(block, textures, this.modelOutput),
                ModelTemplates.BARS_POST_SIDE.create(block, textures, this.modelOutput),
                ModelTemplates.BARS_POST_SIDE_ALT.create(block, textures, this.modelOutput)
        );
        registerSimpleFlatItemModel(block);
    }

    public void createCheeseBlock(Block cheeseBlock) {
        registerSimpleFlatItemModel(cheeseBlock.asItem());
        TextureMapping mainMapping = new TextureMapping()
                .put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(cheeseBlock, "_side"))
                .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(cheeseBlock, "_top"))
                .put(TextureSlot.TOP, TextureMapping.getBlockTexture(cheeseBlock, "_top"))
                .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(cheeseBlock, "_side"));
        TextureMapping innerMapping = mainMapping
                .copy()
                .put(TextureSlot.INSIDE, TextureMapping.getBlockTexture(cheeseBlock, "_inner"));
        Identifier mainModel = ModelTemplates.create(TextureSlot.PARTICLE, TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE)
                .extend()
                .parent(Identifier.withDefaultNamespace("block/cake"))
                .build()
                .create(cheeseBlock, mainMapping, this.modelOutput);
        Function<Integer, Identifier> gen = bitesCount -> ModelTemplates.create("cheese", "_slice" + bitesCount, TextureSlot.PARTICLE, TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.INSIDE)
                .extend()
                .parent(Identifier.withDefaultNamespace("block/cake_slice" + bitesCount))
                .build().create(cheeseBlock, innerMapping, this.modelOutput);

        this.blockStateOutput.accept(MultiVariantGenerator.dispatch(cheeseBlock)
                .with(PropertyDispatch.initial(BlockStateProperties.BITES)
                        .select(0, BlockModelGenerators.plainVariant(mainModel))
                        .select(1, BlockModelGenerators.plainVariant(gen.apply(1)))
                        .select(2, BlockModelGenerators.plainVariant(gen.apply(2)))
                        .select(3, BlockModelGenerators.plainVariant(gen.apply(3)))
                        .select(4, BlockModelGenerators.plainVariant(gen.apply(4)))
                        .select(5, BlockModelGenerators.plainVariant(gen.apply(5)))
                        .select(6, BlockModelGenerators.plainVariant(gen.apply(6)))
                )
        );
    }

    public void createLitMachine(Block block) {
        MultiVariant regularVariant = BlockModelGenerators.plainVariant(GalacticraftTexturedModel.BASIC_MACHINE.create(block, this.modelOutput));
        Material litTexture = TextureMapping.getBlockTexture(block, "_front_on");
        MultiVariant litVariant = BlockModelGenerators.plainVariant(GalacticraftTexturedModel.BASIC_MACHINE.get(block).updateTextures(mapping -> mapping.put(TextureSlot.NORTH, litTexture)).createWithSuffix(block, "_on", this.modelOutput));
        this.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.LIT, litVariant, regularVariant))
                        .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING)
        );
    }

    public void createMagneticCraftingTable(MagneticCraftingBlock block) {
        TextureMapping textureMapping = TextureMapping.cubeBottomTop(block);
        MultiVariant model = plainVariant(ModelTemplates.CUBE_BOTTOM_TOP.create(block, textureMapping, this.modelOutput));
        this.blockStateOutput
                .accept(
                        MultiVariantGenerator.dispatch(block)
                                .with(
                                        PropertyDispatch.initial(BlockStateProperties.FACING)
                                                .select(Direction.DOWN, model.with(X_ROT_180))
                                                .select(Direction.UP, model)
                                                .select(Direction.NORTH, model.with(X_ROT_90))
                                                .select(Direction.EAST, model.with(Y_ROT_90).with(X_ROT_90))
                                                .select(Direction.SOUTH, model.with(Y_ROT_180).with(X_ROT_90))
                                                .select(Direction.WEST, model.with(Y_ROT_270).with(X_ROT_90))
                                )
                );
    }

    public void createMeteor(Block block) {
        Identifier blockModel = Constants.id("block/fallen_meteor");
        createNonTemplateModelBlock(block);
        registerSimpleTintedItemModel(block, blockModel, ItemModelUtils.constantTint(0));
    }

    public void createRotationalMachine(TexturedModel.Provider provider, Block block) {
        this.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(
                        block,
                        BlockModelGenerators.plainVariant(provider.create(block, this.modelOutput))
                ).with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING)
        );
    }

    private Identifier generatePipeBaseModel(Block block, BiConsumer<Identifier, ModelInstance> maker) {
        Identifier parent = Constants.id("block/pipe_" + (block instanceof HeavyWireBlock ? "dense_" : "") + "template");
        ExtendedModelTemplateBuilder builder = ExtendedModelTemplateBuilder.builder().parent(parent).requiredTextureSlot(TextureSlot.TEXTURE);

        return builder.build().create(block, TextureMapping.defaultTexture(block), maker);
    }

    private Identifier generatePipeLegModel(Block block, Direction direction, BiConsumer<Identifier, ModelInstance> maker) {
        Identifier parent = Constants.id("block/pipe_" + (block instanceof HeavyWireBlock ? "dense_" : "") + "leg_template");
        ExtendedModelTemplateBuilder builder = ExtendedModelTemplateBuilder.builder().parent(parent).requiredTextureSlot(TextureSlot.TEXTURE);

        return builder.build().createWithSuffix(block, "_" + direction.getName(), TextureMapping.defaultTexture(block), maker);
    }

    public void pipeLike(Block block) {
        Identifier baseModel = generatePipeBaseModel(block, this.modelOutput);
        registerSimpleFlatItemModel(block.asItem());
        Map<Direction, Identifier> modelPerFace = new EnumMap<>(Direction.class);

        for (Direction direction : Direction.values()) {
            modelPerFace.put(direction, generatePipeLegModel(block, direction, this.modelOutput));
        }

        this.blockStateOutput.accept(MultiPartGenerator.multiPart(block)
                .with(plainVariant(baseModel))
                .with(
                        condition()
                                .term(BlockStateProperties.NORTH, true),
                        plainVariant(modelPerFace.get(Direction.NORTH))
                )
                .with(
                        condition()
                                .term(BlockStateProperties.EAST, true),
                        plainVariant(modelPerFace.get(Direction.EAST))
                                .with(Y_ROT_90)
                )
                .with(
                        condition()
                                .term(BlockStateProperties.SOUTH, true),
                        plainVariant(modelPerFace.get(Direction.EAST))
                                .with(Y_ROT_180)
                )
                .with(
                        condition()
                                .term(BlockStateProperties.WEST, true),
                        plainVariant(modelPerFace.get(Direction.EAST))
                                .with(Y_ROT_270)
                )
                .with(
                        condition()
                                .term(BlockStateProperties.UP, true),
                        plainVariant(modelPerFace.get(Direction.UP))
                                .with(X_ROT_270)
                )
                .with(
                        condition()
                                .term(BlockStateProperties.DOWN, true),
                        plainVariant(modelPerFace.get(Direction.UP))
                                .with(X_ROT_90)
                )
        );
    }

    public void createNasaWorkbench(Block block) {
        Identifier baseModel = GalacticraftModelTemplates.NASA_WORKBENCH.create(block, TextureMapping.cubeBottomTop(block), this.modelOutput);
        this.blockStateOutput.accept(createSimpleBlock(block, plainVariant(baseModel)));

        ItemModel.Unbaked model = ItemModelUtils.composite(
                ItemModelUtils.plainModel(baseModel),
                ItemModelUtils.specialModel(baseModel, new NasaWorkbenchSpecialRenderer.Unbaked())
        );
        this.itemModelOutput.accept(block.asItem(), model);
    }

    public void createPad(Block block) {
        Identifier model = GalacticraftModelTemplates.PAD.create(block, TextureMapping.defaultTexture(block), this.modelOutput);
        MultiVariant centerVariant = plainVariant(GalacticraftModelTemplates.FULL_PAD.create(block, GalacticraftTextureMapping.defaultWithTop(block), this.modelOutput));

        this.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(
                                PropertyDispatch.initial(AbstractPadBlock.PAD_STATE)
                                        .select(PadState.NONE, plainVariant(model))
                                        .select(PadState.CENTER, centerVariant)
                        )
        );
        registerSimpleItemModel(block.asItem(), model);
    }

    public void createFluidTank(Block block) {
        Identifier fallBack = ModelLocationUtils.getModelLocation(block);
        MultiVariant connectedUp = plainVariant(ModelLocationUtils.getModelLocation(block, "_up"));
        MultiVariant connectedDown = plainVariant(ModelLocationUtils.getModelLocation(block, "_down"));
        MultiVariant connectedBoth = plainVariant(ModelLocationUtils.getModelLocation(block, "_both"));

        this.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(
                                PropertyDispatch.initial(FluidTankBlock.UP_CONNECTION, FluidTankBlock.DOWN_CONNECTION)
                                        .select(true, true, connectedBoth)
                                        .select(true, false, connectedUp)
                                        .select(false, true, connectedDown)
                                        .select(false, false, plainVariant(fallBack))
                        )
        );
        registerSimpleItemModel(block.asItem(), fallBack);
    }
}
