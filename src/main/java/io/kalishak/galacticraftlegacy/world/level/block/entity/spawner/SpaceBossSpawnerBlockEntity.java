package io.kalishak.galacticraftlegacy.world.level.block.entity.spawner;

import io.kalishak.galacticraftlegacy.data.datamap.DisabledInBossFight;
import io.kalishak.galacticraftlegacy.data.datamap.GalacticraftDataMaps;
import io.kalishak.galacticraftlegacy.world.entity.monster.boss.AbstractSpaceBoss;
import io.kalishak.galacticraftlegacy.world.level.block.entity.BossSpawnerData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class SpaceBossSpawnerBlockEntity<T extends AbstractSpaceBoss> extends BlockEntity {
    protected final Holder<EntityType<?>> bossType;
    protected BlockPos chestPosition = BlockPos.ZERO;
    protected BossSpawnerData<T> bossSpawnerData = new BossSpawnerData<>();
    protected boolean playerInRange;
    protected boolean lastPlayerInRange;
    private BlockPos roomCoords = BlockPos.ZERO;
    private BlockPos roomSize = BlockPos.ZERO;
    private AABB range15 = null;
    private AABB rangeBounds = null;
    private AABB rangeBoundsPlus3 = null;
    private AABB extendedRangeBounds = null;

    public SpaceBossSpawnerBlockEntity(BlockEntityType<?> type, Holder<EntityType<?>> bossType, BlockPos worldPosition, BlockState blockState) {
        super(type, worldPosition, blockState);
        this.bossType = bossType;
    }

    @SuppressWarnings("unchecked")
    public static <T extends AbstractSpaceBoss> void serverTick(ServerLevel level, BlockState blockState, BlockPos worldPosition, SpaceBossSpawnerBlockEntity<T> spawner) {
        if (spawner.roomCoords != BlockPos.ZERO) {
            if (spawner.range15 == null) {
                final Vec3 thisVec = new Vec3(spawner.worldPosition);
                spawner.range15 = new AABB(thisVec.x - 15, thisVec.y - 15, thisVec.z - 15, thisVec.x + 15, thisVec.y + 15, thisVec.z + 15);
                spawner.rangeBounds = AABB.encapsulatingFullBlocks(spawner.roomCoords, spawner.roomSize);
                spawner.rangeBoundsPlus3 = spawner.rangeBounds.inflate(3.0D);
            }

            if (spawner.getBossSpawnerData().getLastKillTime() > 0 && level.getGameTime() - spawner.getBossSpawnerData().getLastKillTime() > 900000L) {
                spawner.getBossSpawnerData().setLastKillTime(0);
                spawner.bossSpawnerData.setBossDefeated(false);
            }

            List<? extends Entity> bosses = level.getEntitiesOfClass(spawner.bossType.value().getBaseClass(), spawner.range15);
            for (Entity entity : bosses) {
                if (entity instanceof AbstractSpaceBoss boss && entity.isAlive()) {
                    spawner.getBossSpawnerData().setBoss((T) boss);
                    spawner.getBossSpawnerData().setBossSummoned(true);
                    spawner.getBossSpawnerData().setBossDefeated(false);
                    boss.onBossSpawned(spawner);
                }
            }

            List<Mob> entitiesWithin = level.getEntitiesOfClass(Mob.class, spawner.rangeBoundsPlus3);
            for (Mob mob : entitiesWithin) {
                DisabledInBossFight rule = mob.typeHolder().getData(GalacticraftDataMaps.DISABLED_IN_BOSS_FIGHT);

                if (rule != null && rule.ignorePersistence()) {
                    mob.discard();
                }
            }

            List<Player> playersWithin = level.getEntitiesOfClass(Player.class, spawner.rangeBounds);
            spawner.playerInRange = !playersWithin.isEmpty();
            if (spawner.playerInRange) {
                if (!spawner.lastPlayerInRange && !spawner.getBossSpawnerData().wasBossSummoned()) {
                    if (spawner.getBossSpawnerData().getBoss(level) == null && !spawner.getBossSpawnerData().isBossDefeated()) {
                        T entity = (T) spawner.bossType.value().create(level, EntitySpawnReason.SPAWNER);

                        if (entity != null) {
                            entity.setPos(worldPosition.getX() + 0.5D, worldPosition.getY() + 1.0D, worldPosition.getZ() + 0.5D);
                            level.addFreshEntity(entity);
                            spawner.playSpawnSound(entity);
                            spawner.getBossSpawnerData().setBossSummoned(true);
                            spawner.getBossSpawnerData().setBoss(entity);
                        }
                    }
                }
            }

            spawner.lastPlayerInRange = spawner.playerInRange;
        }
    }

    protected void playSpawnSound(T boss) {

    }

    public void updateBossRoom(BlockPos roomCoords, BlockPos roomSize) {
        this.roomCoords = roomCoords;
        this.roomSize = roomSize;
    }

    public BlockPos getChestPosition() {
        return this.chestPosition;
    }

    public void setChestPosition(BlockPos chestPosition) {
        this.chestPosition = chestPosition;
    }

    public BossSpawnerData<T> getBossSpawnerData() {
        return this.bossSpawnerData;
    }

    private void setBossSpawnerData(BossSpawnerData<T> bossSpawnerData) {
        this.bossSpawnerData = bossSpawnerData;
    }

    public AABB getRangeBounds() {
        if (this.rangeBounds == null) {
            this.rangeBounds = AABB.encapsulatingFullBlocks(this.roomCoords, this.roomSize);
        }

        return this.rangeBounds;
    }

    public AABB getExtendedRangeBounds() {
        if (this.extendedRangeBounds == null)
            this.extendedRangeBounds = getRangeBounds().inflate(11.0D);

        return this.extendedRangeBounds;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.store("ChestPosition", BlockPos.CODEC, this.chestPosition);
        output.store("BossSpawnerData", BossSpawnerData.codec(), this.bossSpawnerData);
        output.putBoolean("IsPlayerInRange", this.playerInRange);
        output.putBoolean("WasPlayerInRange", this.lastPlayerInRange);
        output.store("RoomCoords", BlockPos.CODEC, this.roomCoords);
        output.store("RoomSize", BlockPos.CODEC, this.roomSize);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        input.read("ChestPosition", BlockPos.CODEC).ifPresent(this::setChestPosition);
        input.read("BossSpawnerData", BossSpawnerData.<T>codec()).ifPresent(this::setBossSpawnerData);
        this.playerInRange = input.getBooleanOr("IsPlayerInRange", false);
        this.lastPlayerInRange = input.getBooleanOr("WasPlayerInRange", false);
        this.roomCoords = input.read("RoomCoords", BlockPos.CODEC).orElse(BlockPos.ZERO);
        this.roomSize = input.read("RoomSize", BlockPos.CODEC).orElse(BlockPos.ZERO);
    }
}
