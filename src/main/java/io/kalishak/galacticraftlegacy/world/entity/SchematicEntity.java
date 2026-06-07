/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.entity;

import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.registry.SchematicVariant;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class SchematicEntity extends HangingEntity {
    public static final float DEPTH = 0.0625F;

    public SchematicEntity(EntityType<? extends SchematicEntity> entityType, Level level) {
        super(entityType, level);
    }

    public SchematicEntity(Level level, BlockPos pos) {
        super(GalacticraftEntityType.SCHEMATIC.get(), level, pos);
    }

    public SchematicEntity(Level level, BlockPos blockPos, Direction direction, Holder<SchematicVariant> schematicHolder) {
        this(level, blockPos);
        setDirection(direction);
        setSchematic(schematicHolder);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
    }

    public void setSchematic(Holder<SchematicVariant> schematic) {
        setData(GalacticraftAttachments.DATA_SCHEMATIC, schematic);
    }

    public Holder<SchematicVariant> getSchematic() {
        return getData(GalacticraftAttachments.DATA_SCHEMATIC);
    }

    @Override
    public @Nullable <T> T get(DataComponentType<? extends T> dataComponentType) {
        return dataComponentType == GalacticraftDataComponents.SCHEMATIC.get() ? castComponentValue(dataComponentType, getSchematic()) : super.get(dataComponentType);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter componentGetter) {
        applyImplicitComponentIfPresent(componentGetter, GalacticraftDataComponents.SCHEMATIC.get());
        super.applyImplicitComponents(componentGetter);
    }

    @Override
    protected <T> boolean applyImplicitComponent(DataComponentType<T> component, T value) {
        if (component == GalacticraftDataComponents.SCHEMATIC.get()) {
            setSchematic(castComponentValue(GalacticraftDataComponents.SCHEMATIC.get(), value));
            return true;
        }

        return super.applyImplicitComponent(component, value);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {
        super.addAdditionalSaveData(valueOutput);
        valueOutput.store("facing", Direction.CODEC, getDirection());
        valueOutput.store("schematic", SchematicVariant.CODEC, getSchematic());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {
        super.readAdditionalSaveData(valueInput);
        Direction direction = valueInput.read("facing", Direction.CODEC).orElse(Direction.SOUTH);
        setDirection(direction);
        valueInput.read("schematic", SchematicVariant.CODEC).ifPresent(this::setSchematic);
    }

    @Override
    protected AABB calculateBoundingBox(BlockPos blockPos, Direction facing) {
        Vec3 vec3 = Vec3.atCenterOf(blockPos).relative(facing, -0.46875);
        Direction direction = facing.getCounterClockWise();
        Vec3 vec31 = vec3.relative(direction, 0.5D).relative(Direction.UP, 0.5D);
        Direction.Axis axis = facing.getAxis();
        double x = axis == Direction.Axis.X ? DEPTH : 4.0D;
        double z = axis == Direction.Axis.Z ? DEPTH : 4.0D;

        return AABB.ofSize(vec31, x, 4.0D, z);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void dropItem(ServerLevel level, @Nullable Entity entity) {
        if (level.getGameRules().get(GameRules.ENTITY_DROPS)) {
            playSound(SoundEvents.PAINTING_BREAK, 1.0F, 1.0F);

            if (!(entity instanceof Player player && player.hasInfiniteMaterials())) {
                spawnAtLocation(level, getPickResult());
            }
        }
    }

    @Override
    public void playPlacementSound() {
        this.playSound(SoundEvents.PAINTING_PLACE, 1.0F, 1.0F);
    }

    @Override
    public void snapTo(double x, double y, double z, float yRot, float xRot) {
        setPos(x, y, z);
    }

    @Override
    public Vec3 trackingPosition() {
        return Vec3.atLowerCornerOf(this.pos);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket(this, getDirection().get3DDataValue(), getPos());
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        setDirection(Direction.from3DDataValue(packet.getData()));
    }

    @Override
    public ItemStack getPickResult() {
        ItemStack stack = GalacticraftItems.SCHEMATIC.toStack();
        stack.set(GalacticraftDataComponents.SCHEMATIC, getSchematic());

        return stack;
    }
}
