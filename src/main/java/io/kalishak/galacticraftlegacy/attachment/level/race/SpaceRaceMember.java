package io.kalishak.galacticraftlegacy.attachment.level.race;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.codec.SerializableEnum;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

public final class SpaceRaceMember {
    private final UUID playerId;
    private Permission permissionLevel;
    private boolean canManageSpaceRace;
    private boolean canManageSpaceBlocks;

    public static final Codec<SpaceRaceMember> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.CODEC.fieldOf("PlayerId").forGetter(SpaceRaceMember::getPlayerId),
            Permission.CODEC.fieldOf("PermissionLevel").forGetter(SpaceRaceMember::getPermissionLevel),
            Codec.BOOL.optionalFieldOf("CanManageSpaceRace", true).forGetter(SpaceRaceMember::canManageSpaceRace),
            Codec.BOOL.optionalFieldOf("CanManageSpaceBlocks", true).forGetter(SpaceRaceMember::canManageSpaceBlocks)
    ).apply(instance, SpaceRaceMember::new));
    public static final StreamCodec<ByteBuf, SpaceRaceMember> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, SpaceRaceMember::getPlayerId,
            Permission.STREAM_CODEC, SpaceRaceMember::getPermissionLevel,
            ByteBufCodecs.BOOL, SpaceRaceMember::canManageSpaceRace,
            ByteBufCodecs.BOOL, SpaceRaceMember::canManageSpaceBlocks,
            SpaceRaceMember::new
    );

    public SpaceRaceMember(UUID playerId, Permission permissionLevel, boolean canManageSpaceRace, boolean canManageSpaceBlocks) {
        this.playerId = playerId;
        this.permissionLevel = permissionLevel;
        this.canManageSpaceRace = canManageSpaceRace;
        this.canManageSpaceBlocks = canManageSpaceBlocks;
    }

    public SpaceRaceMember(Player player) {
        this(player.getUUID(), Permission.MEMBER, true, true);
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public Permission getPermissionLevel() {
        return permissionLevel;
    }

    public void changePermissionLevel(Permission permission) {
        this.permissionLevel = permission;
    }

    public boolean canManageSpaceRace() {
        return canManageSpaceRace;
    }

    public void manageSpaceRace(boolean manageSpaceRace) {
        this.canManageSpaceRace = manageSpaceRace;
    }

    public boolean canManageSpaceBlocks() {
        return canManageSpaceBlocks;
    }

    public void manageBlocks(boolean manageSpaceBlocks) {
        this.canManageSpaceBlocks = manageSpaceBlocks;
    }

    public enum Permission implements SerializableEnum {
        OWNER("owner", 0),
        VICE("vice", 1),
        MEMBER("member", 2);

        public static final Codec<Permission> CODEC = SerializableEnum.codec(Permission.class);
        public static final StreamCodec<ByteBuf, Permission> STREAM_CODEC = SerializableEnum.streamCodec(Permission.class);
        private final String name;
        private final int id;

        Permission(String name, int id) {
            this.name = name;
            this.id = id;
        }

        @Override
        public @NonNull String getSerializedName() {
            return this.name;
        }

        @Override
        public int getIndex() {
            return this.id;
        }
    }
}
