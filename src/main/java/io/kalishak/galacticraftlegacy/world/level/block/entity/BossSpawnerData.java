package io.kalishak.galacticraftlegacy.world.level.block.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.world.entity.monster.boss.AbstractSpaceBoss;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class BossSpawnerData<T extends AbstractSpaceBoss> {
    private boolean isBossDefeated;
    private boolean wasBossSummoned;
    private long lastKillTime;
    private @Nullable EntityReference<T> boss;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    BossSpawnerData(boolean isBossDefeated, boolean wasBossSummoned, long lastKillTime, Optional<EntityReference<T>> boss) {
        this.isBossDefeated = isBossDefeated;
        this.wasBossSummoned = wasBossSummoned;
        this.boss = boss.orElse(null);
    }

    public BossSpawnerData() {

    }

    public static <T extends AbstractSpaceBoss> Codec<BossSpawnerData<T>> codec() {
        return RecordCodecBuilder.create(i ->
                i.group(
                        Codec.BOOL.fieldOf("IsBossDefeated").forGetter(BossSpawnerData::isBossDefeated),
                        Codec.BOOL.fieldOf("WasBossSummoned").forGetter(BossSpawnerData::wasBossSummoned),
                        Codec.LONG.fieldOf("LastKillTime").forGetter(BossSpawnerData::getLastKillTime),
                        EntityReference.<T>codec().optionalFieldOf("boss").forGetter(BossSpawnerData::boss)
                ).apply(i, BossSpawnerData::new)
        );
    }
    public static <T extends AbstractSpaceBoss> StreamCodec<ByteBuf, BossSpawnerData<T>> streamCodec() {
        return StreamCodec.composite(
                ByteBufCodecs.BOOL, BossSpawnerData::isBossDefeated,
                ByteBufCodecs.BOOL, BossSpawnerData::wasBossSummoned,
                ByteBufCodecs.LONG, BossSpawnerData::getLastKillTime,
                ByteBufCodecs.optional(EntityReference.streamCodec()), BossSpawnerData::boss,
                BossSpawnerData::new
        );
    }

    public void setBossDefeated(boolean bossDefeated) {
        this.isBossDefeated = bossDefeated;
    }

    public boolean isBossDefeated() {
        return this.isBossDefeated;
    }

    public void setBossSummoned(boolean wasBossSummoned) {
        this.wasBossSummoned = wasBossSummoned;
    }

    public boolean wasBossSummoned() {
        return this.wasBossSummoned;
    }

    public void setLastKillTime(long lastKillTime) {
        this.lastKillTime = lastKillTime;
    }

    public long getLastKillTime() {
        return this.lastKillTime;
    }

    public void setBoss(@Nullable T boss) {
        if (boss != null) {
            this.boss = EntityReference.of(boss);
        } else {
            this.boss = null;
        }
    }

    public Optional<EntityReference<T>> boss() {
        return this.boss == null ? Optional.empty() : Optional.of(this.boss);
    }

    @SuppressWarnings("unchecked")
    public @Nullable T getBoss(Level level) {
        if (this.boss == null) {
            return null;
        }

        return EntityReference.get(this.boss, level, (Class<T>) AbstractSpaceBoss.class);
    }
}
