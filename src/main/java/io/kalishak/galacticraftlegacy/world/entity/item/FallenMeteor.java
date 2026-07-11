/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.entity.item;

import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.world.entity.GalacticraftEntityType;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.item.component.HotContent;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import io.kalishak.galacticraftlegacy.world.level.block.entity.FallenMeteorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class FallenMeteor extends FallingBlockEntity {
    public FallenMeteor(EntityType<? extends FallingBlockEntity> type, Level level) {
        super(type, level);
        this.blockState = GalacticraftBlocks.FALLEN_METEOR.get().defaultBlockState();
    }

    private FallenMeteor(Level level, double x, double y, double z, HotContent hotContent) {
        this(GalacticraftEntityType.FALLEN_METEOR.get(), level);
        this.blocksBuilding = true;
        setPos(x, y, z);
        setDeltaMovement(Vec3.ZERO);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        setStartPos(blockPosition());
        setData(GalacticraftAttachments.HOT_CONTENT, hotContent);
    }

    public static void createFallingMeteor(ServerLevel level) {
        if (level.dimensionTypeRegistration().is(GalacticraftTags.DimensionTypes.HAS_METEORS)) {
            ServerPlayer player = level.getRandomPlayer();

            if (player != null) {
                assert player.level().dimensionTypeRegistration().is(GalacticraftTags.DimensionTypes.HAS_METEORS);

                RandomSource random = level.getRandom();
                BlockPos candidate = randomInRange(random, 16, player.blockPosition());

                if (random.nextInt(230) % 20 == 0) {
                    if (level.getBlockState(candidate).isAir() && level.isLoaded(candidate)) {
                        prepareMeteor(level, candidate);
                    }
                }
            }
        }
    }

    private static BlockPos randomInRange(RandomSource random, int range, BlockPos originalPos) {
        int x = random.nextInt(1, range);
        int z = random.nextInt(1, range);

        return originalPos.offset(x, 0, z);
    }

    private static void prepareMeteor(Level level, BlockPos pos) {
        ChunkAccess chunk = level.getChunkAt(pos);
        List<BlockPos> existingMeteors = chunk.getBlockEntitiesPos().stream()
                .filter(blockPos -> {
                    BlockEntity blockEntity = chunk.getBlockEntity(blockPos);

                    return blockEntity instanceof FallenMeteorBlockEntity;
                })
                .toList();

        if (!existingMeteors.isEmpty()) {
            return;
        }

        List<FallenMeteor> existingMeteorEntities = level.getEntitiesOfClass(FallenMeteor.class, new AABB(pos).inflate(16.0D));

        if (!existingMeteorEntities.isEmpty()) {
            return;
        }

        FallenMeteor meteor = new FallenMeteor(level, pos.getX(), pos.getY(), pos.getZ(), HotContent.DEFAULT);

        level.addFreshEntity(meteor);
        level.gameEvent(GameEvent.ENTITY_PLACE, pos, GameEvent.Context.of(meteor));
    }

    @Override
    @SuppressWarnings("unchecked")
    public @Nullable <T> T get(DataComponentType<? extends T> type) {
        return type == GalacticraftDataComponents.HOT_CONTENT.get() ? castComponentValue((DataComponentType<T>) type, getData(GalacticraftAttachments.HOT_CONTENT)) : super.get(type);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        applyImplicitComponentIfPresent(components, GalacticraftDataComponents.HOT_CONTENT.get());
        super.applyImplicitComponents(components);
    }

    @Override
    protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
        if (type == GalacticraftDataComponents.HOT_CONTENT.get()) {
            setData(GalacticraftAttachments.HOT_CONTENT, castComponentValue(GalacticraftDataComponents.HOT_CONTENT.get(), value));
            return true;
        }

        return super.applyImplicitComponent(type, value);
    }
}
