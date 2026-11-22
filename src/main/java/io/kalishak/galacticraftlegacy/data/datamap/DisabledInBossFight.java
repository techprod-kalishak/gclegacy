package io.kalishak.galacticraftlegacy.data.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record DisabledInBossFight(boolean ignorePersistence) {
    public static final Codec<DisabledInBossFight> PERSISTENCE_CODEC = Codec.BOOL
            .xmap(DisabledInBossFight::new, DisabledInBossFight::ignorePersistence);
    public static final Codec<DisabledInBossFight> CODEC = Codec.withAlternative(
            RecordCodecBuilder.create(i -> i.group(
                    Codec.BOOL.fieldOf("ignore_persistence").forGetter(DisabledInBossFight::ignorePersistence)
            ).apply(i, DisabledInBossFight::new)),
            PERSISTENCE_CODEC
    );
}
