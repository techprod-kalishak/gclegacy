package io.kalishak.galacticraftlegacy.world.entity.monster.boss;

import io.kalishak.galacticraftlegacy.component.SchematicVariant;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.level.block.entity.DungeonChestBlockEntity;
import io.kalishak.galacticraftlegacy.world.level.block.entity.spawner.SpaceBossSpawnerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public abstract class AbstractSpaceBoss extends Monster {
    protected SpaceBossSpawnerBlockEntity<?> spawner;
    protected int entitiesWithin;
    protected int entitiesWithinLast;
    protected final ServerBossEvent bossEvent = Util.make(
            new ServerBossEvent(Mth.createInsecureUUID(this.random), getDisplayName(), getBarColor(), BossEvent.BossBarOverlay.PROGRESS),
            e -> e.setDarkenScreen(true)
    );

    protected AbstractSpaceBoss(EntityType<? extends AbstractSpaceBoss> type, Level level) {
        super(type, level);
    }

    public abstract FeatureTier getChestTier();

    protected abstract ItemStack getKey();

    public abstract BossEvent.BossBarColor getBarColor();

    public void onBossSpawned(SpaceBossSpawnerBlockEntity<?> spawner) {
        this.spawner = spawner;
    }

    public ItemStack getTreasure(RandomSource random) {
        return SchematicVariant.setRandomOfTier(GalacticraftItems.SCHEMATIC.toStack(), registryAccess(), getChestTier(), random);
    }

    @Override
    protected void tickDeath() {
        this.deathTime++;

        if (this.deathTime >= 180 && this.deathTime <= 200) {
            EntityDimensions dimensions = getDimensions(getPose());

            final float x = (this.random.nextFloat() - 0.5F) * dimensions.width();
            final float y = (this.random.nextFloat() - 0.5F) * (dimensions.height() / 2.0F);
            final float z = (this.random.nextFloat() - 0.5F) * dimensions.width();
            level().addParticle(ParticleTypes.EXPLOSION, getX() + x, getY() + 2.0D + y, getZ() + z, 0.0D, 0.0D, 0.0D);
        }

        int xp;
        int realExperience;

        if (!level().isClientSide()) {
            if (this.deathTime > 150 && this.deathTime % 5 == 0) {
                xp = 30;

                while (xp > 0) {
                    realExperience = ExperienceOrb.getExperienceValue(xp);
                    xp -= realExperience;

                    level().addFreshEntity(new ExperienceOrb(level(), getX(), getY(), getZ(), realExperience));
                }
            }
        }

        if (this.deathTime == 200 && !level().isClientSide()) {
            xp = 20;

            while (xp > 0) {
                realExperience = ExperienceOrb.getExperienceValue(xp);
                xp -= realExperience;
                level().addFreshEntity(new ExperienceOrb(level(), getX(), getY(), getZ(), realExperience));
            }

            DungeonChestBlockEntity chest = null;

            if (this.spawner != null) {
                BlockEntity blockEntity = level().getBlockEntity(this.spawner.getChestPosition());

                if (blockEntity instanceof DungeonChestBlockEntity dungeonChestBlockEntity) {
                    chest = dungeonChestBlockEntity;
                }
            }

            if (chest == null) {
                chest = DungeonChestBlockEntity.findClosest(this, getChestTier());
            }

            if (chest != null) {
                double dist = distanceToSqr(chest.getBlockPos().getX() + 0.5D, chest.getBlockPos().getY() + 0.5D, chest.getBlockPos().getZ() + 0.5D);

                if (dist < 1000 * 1000) {
                    if (!chest.isLocked()) {
                        chest.unlock();
                    }

                    ItemStack schematic = getTreasure(this.random);
                    chest.setItemInRandomSlot(schematic, this.random);
                }
            }

            level().broadcastEntityEvent(this, EntityEvent.POOF);
            remove(Entity.RemovalReason.KILLED);

            if (level() instanceof ServerLevel serverLevel) {
                if (this.lastHurtByPlayerMemoryTime > 0) {
                    ItemEntity loot = spawnAtLocation(serverLevel, getKey());

                    if (loot != null) {
                        loot.setExtendedLifetime();
                    }
                }
            }

            if (this.spawner != null) {
                this.spawner.getBossSpawnerData().setBossDefeated(true);
                this.spawner.getBossSpawnerData().setBoss(null);
                this.spawner.getBossSpawnerData().setBossSummoned(false);
            }
        }
    }

    @Override
    public void tick() {
        if (this.spawner != null) {
            List<Player> playersWithin = level().getEntitiesOfClass(Player.class, this.spawner.getRangeBounds());

            this.entitiesWithin = playersWithin.size();

            if (this.entitiesWithin == 0 && this.entitiesWithinLast != 0) {
                List<Player> playersWithin2 = level().getEntitiesOfClass(Player.class, this.spawner.getExtendedRangeBounds());

                playersWithin2.forEach(player -> player.sendOverlayMessage(GalacticraftComponents.MOON_SKELETON_MESSAGE.translated()));

                discard();

                return;
            }

            this.entitiesWithinLast = this.entitiesWithin;
        }

        super.tick();
    }

    @Override
    protected void customServerAiStep(ServerLevel level) {
        super.customServerAiStep(level);

        this.bossEvent.setProgress(getHealth() / getMaxHealth());
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);

        if (this.spawner != null) {
            this.spawner.getBossSpawnerData().setBossDefeated(false);
            this.spawner.getBossSpawnerData().setBoss(null);
            this.spawner.getBossSpawnerData().setBossSummoned(false);
        }
    }

    @Override
    public void checkDespawn() {
        if (net.neoforged.neoforge.event.EventHooks.checkMobDespawn(this)) return;

        if (level().getDifficulty() == Difficulty.PEACEFUL && !getType().isAllowedInPeaceful()) {
            discard();
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("EntitiesWithin", this.entitiesWithin);
        output.putInt("EntitiesWithinLast", this.entitiesWithinLast);

        if (this.spawner != null) {
            output.store("SpawnerPosition", BlockPos.CODEC, this.spawner.getBlockPos());
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.entitiesWithin = input.getIntOr("EntitiesWithin", 0);
        this.entitiesWithinLast = input.getIntOr("EntitiesWithinLast", 0);
        input.read("SpawnerPosition", BlockPos.CODEC).ifPresent(blockPos -> {
            BlockEntity entity = level().getBlockEntity(blockPos);

            if (entity instanceof SpaceBossSpawnerBlockEntity<?> spawnerBlockEntity) {
                onBossSpawned(spawnerBlockEntity);
            }
        });
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }
}
