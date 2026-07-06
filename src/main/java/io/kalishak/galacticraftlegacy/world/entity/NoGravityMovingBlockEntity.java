/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.entity;

import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

public class NoGravityMovingBlockEntity extends Entity {
    protected static final EntityDataAccessor<BlockPos> DATA_START_POS = SynchedEntityData.defineId(NoGravityMovingBlockEntity.class, EntityDataSerializers.BLOCK_POS);
    protected static final EntityDataAccessor<Vector3fc> DATA_MOVEMENT = SynchedEntityData.defineId(NoGravityMovingBlockEntity.class, EntityDataSerializers.VECTOR3);
    protected BlockState blockState = GalacticraftBlocks.ASTEROID_ROCK.get().defaultBlockState();
    public @Nullable CompoundTag blockData;

    public NoGravityMovingBlockEntity(EntityType<? extends NoGravityMovingBlockEntity> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    private NoGravityMovingBlockEntity(Level level, double x, double y, double z, BlockState blockState, Vec3 movement) {
        this(GalacticraftEntityType.NO_GRAVITY_MOVING_BLOCK.get(), level);
        this.blockState = blockState;
        this.blocksBuilding = true;
        setPos(x, y, z);
        setDeltaMovement(movement);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        setStartPos(blockPosition());
        setMovement(getDeltaMovement());
    }

    public static NoGravityMovingBlockEntity spawnRandomMovingMeteor(Level level, BlockPos initialPos, Vec3 initialVector) {
        Holder<Block> holder = level.registryAccess().get(GalacticraftTags.Blocks.METEOR_BLOCK_REPLACEABLE)
                .flatMap(named -> named.getRandomElement(level.getRandom()))
                .orElse(GalacticraftBlocks.ASTEROID_ROCK);

        NoGravityMovingBlockEntity block = new NoGravityMovingBlockEntity(level, initialPos.getX(), initialPos.getY(), initialPos.getZ(), holder.value().defaultBlockState(), initialVector);
        level.addFreshEntity(block);

        return block;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        entityData.define(DATA_START_POS, BlockPos.ZERO);
        entityData.define(DATA_MOVEMENT, Vec3.ZERO.toVector3f());
    }

    @Override
    public boolean isAttackable() {
        return this.blockState != null && !this.blockState.isAir();
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        if (!isInvulnerableToBase(source)) {
            markHurt();
        }

        return false;
    }

    @Override
    protected MovementEmission getMovementEmission() {
        return MovementEmission.NONE;
    }

    @Override
    public boolean isPickable() {
        return isAttackable() && !isRemoved();
    }

    @Override
    public void tick() {
        if (this.blockState.isAir()) {
            discard();
        } else {
            move(MoverType.SELF, getMovement());
            applyEffectsFromBlocks();
        }

        super.tick();
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.store("BlockState", BlockState.CODEC, this.blockState);
        output.store("Movement", Vec3.CODEC, getMovement());

        if (this.blockData != null) {
            output.store("BlockEntityData", CompoundTag.CODEC, this.blockData);
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        this.blockState = input.read("BlockState", BlockState.CODEC).orElse(GalacticraftBlocks.ASTEROID_ROCK.get().defaultBlockState());
        setMovement(input.read("Movement", Vec3.CODEC).orElse(Vec3.ZERO));
        this.blockData = input.read("BlockEntityData", CompoundTag.CODEC).orElse(null);
    }

    @Override
    public boolean displayFireAnimation() {
        return level().dimensionTypeRegistration().is(GalacticraftTags.DimensionTypes.OPEN_SPACE);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket(this, serverEntity, Block.getId(this.blockState));
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        this.blockState = Block.stateById(packet.getData());
        this.blocksBuilding = true;
        setPos(packet.getX(), packet.getY(), packet.getZ());
        setStartPos(blockPosition());
    }

    public BlockState getBlockState() {
        return this.blockState;
    }

    public void setStartPos(BlockPos pos) {
        this.entityData.set(DATA_START_POS, pos);
    }

    public BlockPos getStartPos() {
        return this.entityData.get(DATA_START_POS);
    }

    public void setMovement(Vec3 vec3) {
        this.entityData.set(DATA_MOVEMENT, vec3.toVector3f());
    }

    public Vec3 getMovement() {
        return new Vec3(this.entityData.get(DATA_MOVEMENT));
    }
}
