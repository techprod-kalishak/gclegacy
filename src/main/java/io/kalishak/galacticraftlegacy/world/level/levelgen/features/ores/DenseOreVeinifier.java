package io.kalishak.galacticraftlegacy.world.level.levelgen.features.ores;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import net.minecraft.SharedConstants;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;

/**
 * Utility class cloned from {@link net.minecraft.world.level.levelgen.OreVeinifier}
 */
public interface DenseOreVeinifier {
    float VEININESS_THRESHOLD = 0.4F;
    int EDGE_ROUNDOFF_BEGIN = 20;
    double MAX_EDGE_ROUNDOFF = 0.2;
    float VEIN_SOLIDNESS = 0.7F;
    float MIN_RICHNESS = 0.1F;
    float MAX_RICHNESS = 0.3F;
    float MAX_RICHNESS_THRESHOLD = 0.6F;
    float CHANCE_OF_RAW_ORE_BLOCK = 0.02F;
    float SKIP_ORE_IF_GAP_NOISE_IS_BELOW = -0.3F;

    static NoiseChunk.BlockStateFiller create(DensityFunction veinToggle, DensityFunction veinRidged, DensityFunction veinGap, PositionalRandomFactory oreVeinsPositionalRandomFactory) {
        BlockState defaultState = SharedConstants.DEBUG_ORE_VEINS ? Blocks.AIR.defaultBlockState() : null;

        return context -> {
            double oreVeininessNoiseValue = veinToggle.compute(context);
            int posY = context.blockY();
            VeinType veinType = oreVeininessNoiseValue > (double) 0.0F ? VeinType.COPPER : VeinType.IRON;
            double veininessRidged = Math.abs(oreVeininessNoiseValue);
            int distanceFromTop = veinType.maxY - posY;
            int distanceFromBottom = posY - veinType.minY;

            if (distanceFromBottom >= 0 && distanceFromTop >= 0) {
                int distanceFromEdge = Math.min(distanceFromTop, distanceFromBottom);
                double edgeRoundoff = Mth.clampedMap(distanceFromEdge, 0.0F, EDGE_ROUNDOFF_BEGIN, -MAX_EDGE_ROUNDOFF, 0.0F);

                if (veininessRidged + edgeRoundoff < (double) VEININESS_THRESHOLD) {
                    return defaultState;
                }

                RandomSource positionalRandom = oreVeinsPositionalRandomFactory.at(context.blockX(), posY, context.blockZ());
                if (positionalRandom.nextFloat() > VEIN_SOLIDNESS) {
                    return defaultState;
                } else if (veinRidged.compute(context) >= (double) 0.0F) {
                    return defaultState;
                }

                double richness = Mth.clampedMap(veininessRidged, VEININESS_THRESHOLD, MAX_RICHNESS_THRESHOLD, MIN_RICHNESS, MAX_RICHNESS);

                if ((double) positionalRandom.nextFloat() < richness && veinGap.compute(context) > (double) SKIP_ORE_IF_GAP_NOISE_IS_BELOW) {
                    return positionalRandom.nextFloat() < CHANCE_OF_RAW_ORE_BLOCK ? veinType.rawOreBlock : veinType.ore;
                }

                return SharedConstants.DEBUG_ORE_VEINS ? Blocks.OAK_BUTTON.defaultBlockState() : veinType.filler;
            }

            return defaultState;
        };
    }

    record VeinType(BlockState ore, BlockState rawOreBlock, BlockState filler, int minY, int maxY) {
        public static final Codec<VeinType> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                BlockState.CODEC.fieldOf("ore").forGetter(VeinType::ore),
                BlockState.CODEC.fieldOf("raw_ore_block").forGetter(VeinType::rawOreBlock),
                BlockState.CODEC.fieldOf("filler").forGetter(VeinType::filler),
                Codec.INT.fieldOf("min_y").forGetter(VeinType::minY),
                Codec.INT.fieldOf("max_y").forGetter(VeinType::maxY)
        ).apply(instance, VeinType::new));
        public static final Codec<Holder<VeinType>> CODEC = RegistryFixedCodec.create(GalacticraftRegistries.Keys.VEIN_TYPE);
        public static final VeinType COPPER = new VeinType(Blocks.COPPER_ORE.defaultBlockState(), Blocks.RAW_COPPER_BLOCK.defaultBlockState(), Blocks.GRANITE.defaultBlockState(), 0, 50);
        public static final VeinType IRON = new VeinType(Blocks.DEEPSLATE_IRON_ORE.defaultBlockState(), Blocks.RAW_IRON_BLOCK.defaultBlockState(), Blocks.TUFF.defaultBlockState(), -60, -8);
    }
}
