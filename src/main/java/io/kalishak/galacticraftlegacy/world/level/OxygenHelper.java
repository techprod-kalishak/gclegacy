/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level;

import io.kalishak.galacticraftlegacy.galaxies.environment.CelestialBodyInfo;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.data.datamap.GalacticraftDataMaps;
import io.kalishak.galacticraftlegacy.world.level.block.SealableBlock;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.OxygenDistributorBlockEntity;
import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.ColoredPipeBlockEntity;
import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.ConnectorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.Tags;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class OxygenHelper {
    private static final Set<BlockPos> lastCheckedPositions = new HashSet<>();

    public static Set<BlockEntity> getFluidConnections(@NonNull ColoredPipeBlockEntity blockEntity, @Nullable Level level) {
        Set<BlockEntity> connections = new HashSet<>(Direction.values().length);

        if (level != null) {
            for (Direction direction : Direction.values()) {
                BlockPos neighbourPos = blockEntity.getBlockPos().relative(direction);
                BlockEntity neighbour = level.getBlockEntity(neighbourPos);
                boolean doConnect = false;

                if (neighbour instanceof ConnectorBlockEntity connector) {
                    doConnect = !level.isClientSide() || connector.canConnect(direction, blockEntity.getNetwork(direction).getType());
                } else if (neighbour != null) {
                    doConnect = level.getCapability(Capabilities.Fluid.BLOCK, neighbourPos, null) != null;
                }

                if (doConnect) {
                    connections.add(neighbour);
                }
            }
        }

        return connections;
    }

    public static boolean hasAtmosphericOxygen(Level level) {
        CelestialBodyInfo levelData = level.dimensionTypeRegistration().getData(GalacticraftDataMaps.CELESTIAL_BODY_DATA);

        return levelData == null || levelData.atmosphereInfo().isBreathable();
    }

    @SuppressWarnings("deprecation")
    public static boolean isThermalOxygen(Level blockGetter, AABB area) {
        int minX = Mth.floor(area.minX + 0.001D);
        int maxX = Mth.floor(area.maxX - 0.001D);
        int minY = Mth.floor(area.minY + 0.001D);
        int maxY = Mth.floor(area.maxY - 0.001D);
        int minZ = Mth.floor(area.minZ + 0.001D);
        int maxZ = Mth.floor(area.maxZ - 0.001D);

        if (blockGetter.hasChunksAt(minX, minY, minZ, maxX, maxY, maxZ)) {
            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        BlockPos pos = new BlockPos(x, y, z);

                        if (isBreathableAt(blockGetter, blockGetter.getBlockState(pos), pos, 0)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public static boolean withinOxygenBubble(Level blockGetter, double x, double y, double z) {
        ResourceKey<Level> dimension = blockGetter.dimension();

        for (final GlobalPos pos : OxygenDistributorBlockEntity.loadedBlocks) {
            if (pos != null && pos.dimension().equals(dimension)) {
                BlockEntity blockEntity = blockGetter.getBlockEntity(pos.pos());

                if (blockEntity instanceof OxygenDistributorBlockEntity oxygenDistributor) {
                    return oxygenDistributor.inBubble(x, y, z);
                }
            }
        }

        return false;
    }

    public static boolean isBreathableAir(Level level, BlockPos pos) {
        return level.getBlockState(pos).is(GalacticraftTags.Blocks.BREATHABLE_AIR);
    }

    public static boolean hasOxygenNearby(Level level, BlockPos pos, double range, boolean mustBeSealed) {
        AABB bounds = fromPos(pos).inflate(range);

        return hasOxygen(level, bounds, mustBeSealed);
    }

    public static AABB fromPos(BlockPos pos) {
        return Shapes.block().bounds().expandTowards(Vec3.atLowerCornerOf(pos));
    }

    public static boolean hasOxygen(Level blockGetter, AABB areaToSearch, boolean mustBeSealed) {
        final double avgX = (areaToSearch.minX + areaToSearch.maxX) / 2.0D;
        final double avgY = (areaToSearch.minY + areaToSearch.maxY) / 2.0D;
        final double avgZ = (areaToSearch.minZ + areaToSearch.maxZ) / 2.0D;

        if (mustBeSealed) {
            return isThermalOxygen(blockGetter, areaToSearch);
        }

        return withinOxygenBubble(blockGetter, avgX, avgY, avgZ) || isThermalOxygen(blockGetter, areaToSearch);
    }

    public static Stream<BlockPos> surrounding(BlockPos originalPos) {
        return Arrays.stream(Direction.values()).map(originalPos::relative).limit(Direction.values().length);
    }

    private static boolean canBlockPassAirFacing(Level level, BlockState state, BlockPos pos, Direction face) {
        if (state.getBlock() instanceof SealableBlock sealableBlock) {
            return !sealableBlock.isSealed(level, pos, face);
        }

        if (state.is(BlockTags.SLABS)) {
            return switch (state.getValue(SlabBlock.TYPE)) {
                case TOP -> face == Direction.UP;
                case BOTTOM -> face == Direction.DOWN;

                default -> true;
            };
        }

        if (state.is(GalacticraftTags.Blocks.SEALABLE_FROM_BOTTOM)) {
            return face != Direction.UP;
        }

        if (state.is(Blocks.PISTON) || state.is(Blocks.STICKY_PISTON)) {
            if (state.getValue(PistonBaseBlock.EXTENDED)) {
                return state.getValue(PistonBaseBlock.FACING) != face;
            }

            return false;
        }

        return state.isCollisionShapeFullBlock(level, pos);
    }

    private static synchronized boolean isBreathableAt(Level level, BlockState state, BlockPos pos, int limit) {
        OxygenHelper.lastCheckedPositions.add(pos);

        if (state.is(GalacticraftTags.Blocks.BREATHABLE_AIR)) {
            return true;
        } else if (!state.isAir()) {
            return false;
        }

        boolean permeableFlag = false;

        if (!state.is(BlockTags.LEAVES)) {
            if (state.isSolidRender()) {
                if (state.is(Blocks.GRAVEL) || state.is(Blocks.SPONGE) || state.is(Blocks.WET_SPONGE)) {
                    permeableFlag = true;
                } else return false;
            } else if (state.is(Tags.Blocks.GLASS_BLOCKS)) {
                return false;
            } else if (!state.getFluidState().isEmpty()) {
                return false;
            }
        } else {
            permeableFlag = true;
        }

        if (limit < 5) {
            for (Direction face : Direction.values()) {
                if (permeableFlag || canBlockPassAirFacing(level, state, pos, face)) {
                    BlockPos relativePos = pos.relative(face);

                    if (!OxygenHelper.lastCheckedPositions.contains(relativePos)) {
                        BlockState relativeState = level.getBlockState(relativePos);

                        if (isBreathableAt(level, relativeState, relativePos, limit + 1)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
}
