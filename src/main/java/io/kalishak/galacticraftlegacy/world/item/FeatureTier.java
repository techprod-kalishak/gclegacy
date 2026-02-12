package io.kalishak.galacticraftlegacy.world.item;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.codec.SerializableEnum;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.jspecify.annotations.NonNull;

public enum FeatureTier implements SerializableEnum {
    TIER_1("basic", 0, 1, 25, GalacticraftTags.Biomes.HAS_BASIC_FEATURES),
    TIER_2("advanced", 1, 2, 50, GalacticraftTags.Biomes.HAS_ADVANCED_FEATURES),
    TIER_3("ultimate", 2, 3, 75, GalacticraftTags.Biomes.HAS_ULTIMATE_FEATURES);

    public static final Codec<FeatureTier> CODEC = SerializableEnum.codec(FeatureTier.class);
    public static final StreamCodec<ByteBuf, FeatureTier> STREAM_CODEC = SerializableEnum.streamCodec(FeatureTier.class);
    private final String name;
    private final int id;
    private final int level;
    private final int energyConsumption;
    private final TagKey<Biome> availableIn;

    FeatureTier(String name, int id, int level, int energyConsumption, TagKey<Biome> availableIn) {
        this.name = name;
        this.id = id;
        this.level = level;
        this.energyConsumption = energyConsumption;
        this.availableIn = availableIn;
    }

    @Override
    public @NonNull String getSerializedName() {
        return this.name;
    }

    @Override
    public int getIndex() {
        return this.id;
    }

    public int getLevel() {
        return this.level;
    }

    public int getEnergyConsumption() {
        return this.energyConsumption;
    }

    public TagKey<Biome> getAvailableIn() {
        return this.availableIn;
    }

    public boolean isAvailableAt(Level level, BlockPos pos) {
        return level.getBiome(pos).is(this.availableIn);
    }
}
