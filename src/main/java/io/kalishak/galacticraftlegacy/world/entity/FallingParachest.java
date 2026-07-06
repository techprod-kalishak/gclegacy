/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.entity;

import com.mojang.logging.LogUtils;
import io.kalishak.galacticraftlegacy.attachment.entity.ParachuteFalling;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.transfer.capability.fluid.SingleTankResourceHandler;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import io.kalishak.galacticraftlegacy.world.level.block.entity.ParachestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class FallingParachest extends FallingBlockEntity implements ParachuteFalling {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final NonNullList<ItemStack> inventory;
    private FluidStack fuelTank = FluidStack.EMPTY;
    private DyeColor parachuteColor = DyeColor.RED;

    private final ItemStacksResourceHandler itemResources;
    private final SingleTankResourceHandler fluidResource = new SingleTankResourceHandler(8 * FluidType.BUCKET_VOLUME);

    public FallingParachest(EntityType<? extends FallingParachest> entityType, Level level) {
        super(entityType, level);
        this.blockState = GalacticraftBlocks.PARACHEST.get().defaultBlockState();
        this.inventory = NonNullList.withSize(3, ItemStack.EMPTY);
        this.itemResources = new ItemStacksResourceHandler(this.inventory);

        setInvulnerable(true);
    }

    private FallingParachest(Level level, double x, double y, double z, BlockState state, @Nullable BlockEntity blockEntity) {
        super(GalacticraftEntityType.FALLING_PARACHEST.get(), level);
        this.blockState = state;
        this.blocksBuilding = true;
        this.xo = x;
        this.yo = y;
        this.zo = z;

        if (blockEntity instanceof ParachestBlockEntity parachestBlockEntity) {
            this.inventory = NonNullList.withSize(parachestBlockEntity.getItemsSize(), ItemStack.EMPTY);
            parachestBlockEntity.copyItems(this.inventory);
            this.fuelTank = parachestBlockEntity.copyTank();

            this.parachuteColor = parachestBlockEntity.getParachuteColor();
        } else {
            this.inventory = NonNullList.withSize(3, ItemStack.EMPTY);
        }

        this.itemResources = new ItemStacksResourceHandler(this.inventory);

        setPos(x, y, z);
        setDeltaMovement(Vec3.ZERO);
        setStartPos(blockPosition());
        setInvulnerable(true);
        disableDrop();
    }

    public static FallingParachest fall(Level level, BlockPos pos, BlockState blockState) {
        FallingParachest fallingParachest = new FallingParachest(
                level,
                pos.getX() + 0.5D,
                pos.getY(),
                pos.getZ() + 0.5D,
                blockState.hasProperty(BlockStateProperties.WATERLOGGED) ? blockState.setValue(BlockStateProperties.WATERLOGGED, false) : blockState,
                level.getBlockEntity(pos)
        );

        level.setBlock(pos, blockState.getFluidState().createLegacyBlock(), Block.UPDATE_ALL);
        level.addFreshEntity(fallingParachest);
        return fallingParachest;
    }

    public static FallingParachest returnFromSpace(Level level, Player player, Entity rocket, ResourceKey<Level> previousDimension) {
        BlockPos parachestInitialPos = player.blockPosition().above();
        BlockState state = getStateFromRocketStorage(rocket);
        FallingParachest fallingParachest = new FallingParachest(
                level,
                parachestInitialPos.getX(),
                parachestInitialPos.getY(),
                parachestInitialPos.getZ(),
                state,
                null
        );

        fallingParachest.setFallingTicks(0);
        level.setBlock(parachestInitialPos, state.getFluidState().createLegacyBlock(), Block.UPDATE_ALL);
        level.addFreshEntity(fallingParachest);

        return fallingParachest;
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerEntity(
                Capabilities.Item.ENTITY,
                GalacticraftEntityType.FALLING_PARACHEST.get(),
                (entity, cxt) -> entity.itemResources
        );
        event.registerEntity(
                Capabilities.Fluid.ENTITY,
                GalacticraftEntityType.FALLING_PARACHEST.get(),
                (entity, cxt) -> entity.fluidResource
        );
    }

    private static BlockState getStateFromRocketStorage(Entity rocket) {
        return GalacticraftBlocks.PARACHEST.get().defaultBlockState();
    }

    @Override
    public void onRemoval(RemovalReason reason) {
        super.onRemoval(reason);

        if (reason == RemovalReason.DISCARDED && level() instanceof ServerLevel serverLevel) {
            this.inventory.forEach(itemStack -> spawnAtLocation(serverLevel, itemStack));
        }
    }

    @Override
    public void tick() {
        if (this.blockState.isAir()) {
            discard();
        } else {
            Block block = this.blockState.getBlock();
            this.time++;
            applyGravity();
            move(MoverType.SELF, this.getDeltaMovement());
            applyEffectsFromBlocks();
            handlePortal();
            if (level() instanceof ServerLevel serverlevel && (isAlive() || this.forceTickAfterTeleportToDuplicate)) {
                BlockPos blockpos = blockPosition();

                if (!onGround()) {
                    if (this.time > 100 && (blockpos.getY() <= this.level().getMinY() || blockpos.getY() > this.level().getMaxY()) || this.time > 600) {
                        discard();
                    }
                } else {
                    BlockState blockstate = level().getBlockState(blockpos);
                    setDeltaMovement(getDeltaMovement().multiply(0.7, -0.5, 0.7));
                    boolean canBeReplaced = blockstate.canBeReplaced(
                            new DirectionalPlaceContext(level(), blockpos, Direction.DOWN, ItemStack.EMPTY, Direction.UP)
                    );
                    boolean emptyBelow = FallingBlock.isFree(level().getBlockState(blockpos.below()));
                    boolean survivesOnBelow = this.blockState.canSurvive(level(), blockpos) && !emptyBelow;
                    if (canBeReplaced && survivesOnBelow) {
                        if (this.blockState.hasProperty(BlockStateProperties.WATERLOGGED) && level().getFluidState(blockpos).getType() == Fluids.WATER) {
                            this.blockState = this.blockState.setValue(BlockStateProperties.WATERLOGGED, true);
                        }

                        if (level().setBlock(blockpos, this.blockState, Block.UPDATE_ALL)) {
                            serverlevel.getChunkSource().chunkMap.sendToTrackingPlayers(this, new ClientboundBlockUpdatePacket(blockpos, level().getBlockState(blockpos)));
                            discard();
                            onLand(this, blockpos);

                            if (block instanceof Fallable fallable) {
                                fallable.onLand(level(), blockpos, this.blockState, blockstate, this);
                            }

                            if (this.blockData != null && this.blockState.hasBlockEntity()) {
                                BlockEntity blockentity = level().getBlockEntity(blockpos);
                                if (blockentity != null) {
                                    try (ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(blockentity.problemPath(), LOGGER)) {
                                        RegistryAccess registryAccess = level().registryAccess();
                                        TagValueOutput valueOutput = TagValueOutput.createWithContext(scopedCollector, registryAccess);
                                        blockentity.saveWithoutMetadata(valueOutput);
                                        CompoundTag compoundtag = valueOutput.buildResult();
                                        this.blockData.forEach((key, tag) -> compoundtag.put(key, tag.copy()));
                                        blockentity.loadWithComponents(TagValueInput.create(scopedCollector, registryAccess, compoundtag));
                                    } catch (Exception exception) {
                                        LOGGER.error("Failed to load block entity from falling block", exception);
                                    }

                                    blockentity.setChanged();
                                }
                            }
                        }
                    } else {
                        discard();
                    }
                }
            }

            setDeltaMovement(this.getDeltaMovement().scale(0.49));
            if (isAlive() && block instanceof Fallable feblock) {
                feblock.fallingTick(level(), blockPosition(), this);
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {
        super.addAdditionalSaveData(valueOutput);
        this.itemResources.serialize(valueOutput);
        this.fluidResource.serialize(valueOutput);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {
        super.readAdditionalSaveData(valueInput);
        this.itemResources.deserialize(valueInput);
        this.fluidResource.deserialize(valueInput);
    }

    @Override
    public void onLand(Entity owner, BlockPos landedPos) {
        BlockState selfState = level().getBlockState(landedPos.below());

        if (selfState.is(GalacticraftBlocks.PARACHEST)) {
            BlockEntity blockEntity = level().getBlockEntity(landedPos.below());

            if (blockEntity instanceof ParachestBlockEntity parachestBlock) {
                parachestBlock.copyItemsFrom(this.inventory);
                parachestBlock.copyTankFrom(this.fuelTank);
            }
        }
    }

    @Override
    protected double getDefaultGravity() {
        return 0.015D;
    }

    @Override
    public void setFallingTicks(int ticks) {
        this.time = ticks;
    }

    @Override
    public int getFallingTicks() {
        return this.time;
    }

    public DyeColor getParachuteColor() {
        return this.parachuteColor;
    }
}
