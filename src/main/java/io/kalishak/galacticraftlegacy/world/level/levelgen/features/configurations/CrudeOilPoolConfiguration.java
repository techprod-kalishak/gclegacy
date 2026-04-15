package io.kalishak.galacticraftlegacy.world.level.levelgen.features.configurations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public record CrudeOilPoolConfiguration(RuleTest target, IntProvider radius, IntProvider height, Optional<Integer> distanceFromRoof) implements FeatureConfiguration {
    public static final Codec<CrudeOilPoolConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RuleTest.CODEC.fieldOf("target").forGetter(CrudeOilPoolConfiguration::target),
            IntProviders.codec(0, 16).fieldOf("radius").forGetter(CrudeOilPoolConfiguration::radius),
            IntProviders.codec(0, 16).fieldOf("height").forGetter(CrudeOilPoolConfiguration::height),
            Codec.INT.optionalFieldOf("max_column_height").forGetter(CrudeOilPoolConfiguration::distanceFromRoof)
    ).apply(instance, CrudeOilPoolConfiguration::new));

    public static class Builder {
        private final RuleTest target;
        private IntProvider radius = ConstantInt.ZERO;
        private IntProvider height = ConstantInt.ZERO;
        private @Nullable Integer distanceFromRoof;

        public Builder(Block block) {
            this.target = new BlockMatchTest(block);
        }

        public Builder(RuleTest target) {
            this.target = target;
        }

        public static Builder ofBlock(Holder<Block> block) {
            return new Builder(block.value());
        }

        public static Builder ofTag(TagKey<Block> targets) {
            return new Builder(new TagMatchTest(targets));
        }

        public Builder radius(int min, int max) {
            this.radius = UniformInt.of(min, max);
            return this;
        }

        public Builder radius(int radius) {
            this.radius = ConstantInt.of(radius);
            return this;
        }

        public Builder height(int min, int max) {
            this.height = UniformInt.of(min, max);
            return this;
        }

        public Builder height(int height) {
            this.height = ConstantInt.of(height);
            return this;
        }

        public Builder distanceFromRoof(@Nullable Integer distanceFromRoof) {
            this.distanceFromRoof = distanceFromRoof;
            return this;
        }

        public CrudeOilPoolConfiguration build() {
            return new CrudeOilPoolConfiguration(this.target, this.radius, this.height, Optional.ofNullable(this.distanceFromRoof));
        }
    }
}
