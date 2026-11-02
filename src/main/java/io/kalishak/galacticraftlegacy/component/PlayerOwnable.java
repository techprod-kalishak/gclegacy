package io.kalishak.galacticraftlegacy.component;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.world.score.race.SpaceRaceHooks;
import io.kalishak.galacticraftlegacy.world.score.race.SpaceRaceTeam;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

public final class PlayerOwnable implements TooltipProvider {
    public static final Codec<PlayerOwnable> CODEC = Codec.either(
            Codec.STRING, EntityReference.<Player>codec()
    ).xmap(PlayerOwnable::new, playerOwnable -> playerOwnable.owners);
    public static final StreamCodec<ByteBuf, PlayerOwnable> STREAM_CODEC = ByteBufCodecs.either(
            ByteBufCodecs.STRING_UTF8,
            EntityReference.<Player>streamCodec()
    ).map(PlayerOwnable::new, playerOwnable -> playerOwnable.owners);

    private final Either<String, EntityReference<Player>> owners;

    private PlayerOwnable(Either<String, EntityReference<Player>> owners) {
        this.owners = owners;
    }

    public static PlayerOwnable byPlayer(Player player) {
        return new PlayerOwnable(Either.right(EntityReference.of(player)));
    }

    public static PlayerOwnable byTeam(SpaceRaceTeam team) {
        return new PlayerOwnable(Either.left(team.getName()));
    }

    public @Nullable SpaceRaceTeam getTeam(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            return this.owners.left()
                    .flatMap(name -> Optional.ofNullable(SpaceRaceHooks.getFromLevel(serverLevel).getSpaceRaceTeam(name)))
                    .orElse(null);
        }

        return null;
    }

    public @Nullable Player getPlayer(Level level) {
        return this.owners.right()
                .map(reference -> EntityReference.getPlayer(reference, level))
                .orElse(null);
    }

    public boolean isOwnedBy(Player player) {
        if (this.owners.right().isPresent()) {
            return this.owners.right().get().matches(player);
        }

        if (player instanceof ServerPlayer serverPlayer) {
            var playerTeam = SpaceRaceHooks.getSpaceRaceTeam(serverPlayer);

            return playerTeam.map(spaceRaceTeam -> this.owners.left()
                    .filter(teamName -> spaceRaceTeam.getName().equals(teamName))
                    .isPresent()
            ).orElse(true);
        }

        return false;
    }

    public @Nullable Component resolveDisplayName(ServerLevel level) {
        SpaceRaceTeam team = getTeam(level);

        if (team != null) {
            return team.getDisplayName();
        }

        Player player = getPlayer(level);

        if (player != null) {
            return player.getDisplayName();
        }

        return null;
    }

    @Override
    public void addToTooltip(Item.@NonNull TooltipContext tooltipContext, @NonNull Consumer<Component> consumer, @NonNull TooltipFlag tooltipFlag, @NonNull DataComponentGetter dataComponentGetter) {
        Level level = tooltipContext.level();

        if (level instanceof ServerLevel serverLevel) {
            Component displayName = resolveDisplayName(serverLevel);

            if (displayName != null) {
                consumer.accept(Component.translatable("item.galacticraftlegacy.ownable.description").withStyle(ChatFormatting.GRAY));
                consumer.accept(displayName);
            }
        }
    }
}
