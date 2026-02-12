package io.kalishak.galacticraftlegacy.attachment.level;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.registry.CelestialBodyLevelDataEntries;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.space.environment.AtmosphereInfo;
import io.kalishak.galacticraftlegacy.space.environment.CelestialBodyTransition;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.IAttachmentHolder;

/**
 * Immutable data regarding every space object. Do not have to be habitable by the Player
 * @param temperatureModifier Scaled modifier for temperature calculations
 * @param atmosphereInfo General information about the atmosphere of this celestial body
 * @param gravityScale Modifier used to modify entity's gravity attribute
 * @param transition Information about transitions to this celestial body
 */
public record CelestialBodyLevelData(float temperatureModifier, AtmosphereInfo atmosphereInfo, float gravityScale, CelestialBodyTransition transition) {
    public static final Codec<CelestialBodyLevelData> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("temperature_modifier").forGetter(CelestialBodyLevelData::temperatureModifier),
            AtmosphereInfo.CODEC.fieldOf("atmosphere_info").forGetter(CelestialBodyLevelData::atmosphereInfo),
            Codec.FLOAT.fieldOf("gravity_scale").forGetter(CelestialBodyLevelData::gravityScale),
            CelestialBodyTransition.CODEC.fieldOf("transition").forGetter(CelestialBodyLevelData::transition)
    ).apply(instance, CelestialBodyLevelData::new));
    public static final Codec<Holder<CelestialBodyLevelData>> CODEC = RegistryFixedCodec.create(GalacticraftRegistries.Keys.CELESTIAL_BODY_LEVEL_DATA);
    public static final StreamCodec<RegistryFriendlyByteBuf, CelestialBodyLevelData> DIRECT_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, CelestialBodyLevelData::temperatureModifier,
            AtmosphereInfo.STREAM_CODEC, CelestialBodyLevelData::atmosphereInfo,
            ByteBufCodecs.FLOAT, CelestialBodyLevelData::gravityScale,
            CelestialBodyTransition.STREAM_CODEC, CelestialBodyLevelData::transition,
            CelestialBodyLevelData::new
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<CelestialBodyLevelData>> STREAM_CODEC = ByteBufCodecs.holder(
            GalacticraftRegistries.Keys.CELESTIAL_BODY_LEVEL_DATA,
            DIRECT_STREAM_CODEC
    );

    public static Holder<CelestialBodyLevelData> fromLevel(IAttachmentHolder attachmentHolder) {
        if (attachmentHolder instanceof Level level) {
            return level.registryAccess().getOrThrow(level.dimension().cast(GalacticraftRegistries.Keys.CELESTIAL_BODY_LEVEL_DATA).orElse(CelestialBodyLevelDataEntries.OVERWORLD));
        } else {
            throw new IllegalArgumentException("AttachmentHolder is not a Level!");
        }
    }
}
