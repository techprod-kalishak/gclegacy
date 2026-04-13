package io.kalishak.galacticraftlegacy.world.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.level.block.entity.KeyLockedBlockEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public record KeyLock(FeatureTier featureTier, boolean locked) {
    public static final String TAG_KEY = "KeyLock";
    public static final KeyLock UNLOCKED = new KeyLock(FeatureTier.TIER_1, false);
    public static final Codec<KeyLock> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            FeatureTier.CODEC.fieldOf("tier").forGetter(KeyLock::featureTier),
            Codec.BOOL.fieldOf("locked").forGetter(KeyLock::locked)
    ).apply(instance, KeyLock::new));
    public static final StreamCodec<ByteBuf, KeyLock> STREAM_CODEC = StreamCodec.composite(
            FeatureTier.STREAM_CODEC, KeyLock::featureTier,
            ByteBufCodecs.BOOL, KeyLock::locked,
            KeyLock::new
    );

    public static KeyLock preGenTier(FeatureTier featureTier) {
        return new KeyLock(featureTier, true);
    }

    public static void fromTag(ValueInput input, Consumer<KeyLock> consumer) {
        consumer.accept(input.read(TAG_KEY, CODEC).orElse(UNLOCKED));
    }

    public void addToTag(ValueOutput output) {
        if (!this.locked) {
            output.store(TAG_KEY, CODEC, this);
        }
    }

    public boolean canUnlock(ItemStack stack, @Nullable Level level, BlockPos pos) {
        KeyLock itemLock = stack.get(GalacticraftDataComponents.KEY_LOCK);

        if (itemLock != null && level != null) {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            return (blockEntity instanceof KeyLockedBlockEntity && itemLock.featureTier == this.featureTier);
        }

        return false;
    }
}
