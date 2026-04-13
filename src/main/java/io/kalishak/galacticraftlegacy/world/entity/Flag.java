package io.kalishak.galacticraftlegacy.world.entity;

import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.attachment.level.race.FlagData;
import io.kalishak.galacticraftlegacy.attachment.level.race.SpaceRaceManager;
import io.kalishak.galacticraftlegacy.attachment.level.race.SpaceRaceTeam;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.item.component.FlagItemData;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class Flag extends Entity {
    protected static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> DATA_OWNER_UUID = SynchedEntityData.defineId(Flag.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);
    public long lastHit;

    public Flag(EntityType<? extends Flag> flagEntityType, Level level) {
        super(flagEntityType, level);
    }

    public Flag(Level level, FlagData flagData, double x, double y, double z, float facingAngle) {
        this(GalacticraftEntityType.FLAG.get(), level);
        setFlagData(flagData);
        setPos(x, y, z);
        setXRot(facingAngle);
        this.noPhysics = true;
    }

    @Override
    public @NonNull ItemStack getPickResult() {
        ItemStack stack = GalacticraftItems.FLAG.toStack();
        FlagData flagData = getFlagData();
        Component teamName = Component.empty();

        if (getOwner() instanceof ServerPlayer player) {
            SpaceRaceManager manager = SpaceRaceManager.getFromLevel(player.level());

            SpaceRaceTeam team = manager.getSpaceRaceTeamByPlayerId(player.getUUID());

            if (team != null) {
                teamName = team.getDisplayName();
            }
        }

        stack.set(GalacticraftDataComponents.FLAG, new FlagItemData(flagData, teamName));
        return stack;
    }

    public void destroy(ServerLevel level, boolean sendFeedback, boolean shouldDrop) {
        if (sendFeedback) {
            showBreakingParticles(level);
            playBrokenSound();
        }

        if (shouldDrop && level.getGameRules().get(GameRules.ENTITY_DROPS)) {
            Block.popResource(level, getOnPos().above(), getPickResult());
        }

        kill(level);
    }

    private void showBreakingParticles(ServerLevel level) {
        level.sendParticles(
                new BlockParticleOption(ParticleTypes.BLOCK, Blocks.IRON_BLOCK.defaultBlockState()),
                getX(),
                getY(0.6666666666666666),
                getZ(),
                10,
                getBbWidth() / 4.0F,
                getBbHeight() / 4.0F,
                getBbWidth() / 4.0F,
                0.05
        );
    }

    private void playBrokenSound() {
        level().playSound(null, getX(), getY(), getZ(), SoundEvents.METAL_BREAK, getSoundSource(), 1.0F, 1.0F);
    }

    @Override
    protected Entity.@NonNull MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    @Override
    public boolean canBeCollidedWith(@Nullable Entity entity) {
        return true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_OWNER_UUID, Optional.empty());
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        if (isRemoved()) {
            return false;
        } else if (!level.getGameRules().get(GameRules.MOB_GRIEFING) && damageSource.getEntity() instanceof Mob) {
            return false;
        } else if (damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            kill(level);
            return false;
        } else if (isInvulnerableToBase(damageSource)) {
            return false;
        } else if (damageSource.is(DamageTypeTags.IS_EXPLOSION)) {
            kill(level);
            return false;
        }

        if (damageSource.getEntity() instanceof Player player && !player.getAbilities().mayBuild) {
            return false;
        } else if (damageSource.isCreativePlayer()) {
            destroy(level, true, false);
            return true;
        }

        long i = level.getGameTime();
        if (i - this.lastHit > 5L) {
            level.broadcastEntityEvent(this, (byte) 32);
            gameEvent(GameEvent.ENTITY_DAMAGE, damageSource.getEntity());
            this.lastHit = i;
        } else {
            destroy(level, true, true);
        }

        return true;
    }

    @Override
    public void handleEntityEvent(byte eventId) {
        if (eventId == 32) {
            if (this.level().isClientSide()) {
                this.level().playLocalSound(getX(), getY(), getZ(), SoundEvents.METAL_HIT, getSoundSource(), 0.3F, 1.0F, false);
                this.lastHit = level().getGameTime();
            }
        } else {
            super.handleEntityEvent(eventId);
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        setFlagData(input.read("FlagData", FlagData.CODEC).orElse(FlagData.DEFAULT));
        setOwnerReference(EntityReference.read(input, "Owner"));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.store("FlagData", FlagData.CODEC, getFlagData());
        EntityReference.store(getOwnerReference(), output, "Owner");
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide() && (this.tickCount - 1) % 20 == 0) {
            if (getOwner() instanceof ServerPlayer player && distanceToSqr(player) < 50.0D) {
                FlagData flagData = SpaceRaceManager.getPlayerFlag(player);
                setFlagData(flagData);
            }
        }

        if (getBlockStateOn().isAir()) {
            setDeltaMovement(0.0F, -1.0F, 0.0F);
        }
    }

    public void setFlagData(FlagData flagData) {
        setData(GalacticraftAttachments.DATA_FLAG, flagData);
    }

    public @Nullable LivingEntity getOwner() {
        EntityReference<LivingEntity> reference = getOwnerReference();
        return reference == null ? null : reference.getEntity(level(), LivingEntity.class);
    }

    public @Nullable EntityReference<LivingEntity> getOwnerReference() {
        return this.entityData.get(DATA_OWNER_UUID).orElse(null);
    }

    public void setOwner(@Nullable LivingEntity owner) {
        this.entityData.set(DATA_OWNER_UUID, Optional.ofNullable(owner).map(EntityReference::of));
    }

    public void setOwnerReference(@Nullable EntityReference<LivingEntity> owner) {
        this.entityData.set(DATA_OWNER_UUID, Optional.ofNullable(owner));
    }

    public FlagData getFlagData() {
        return getData(GalacticraftAttachments.DATA_FLAG);
    }
}
