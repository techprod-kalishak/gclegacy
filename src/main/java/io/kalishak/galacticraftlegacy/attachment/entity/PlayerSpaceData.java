/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.attachment.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.attachment.level.race.FlagData;
import io.kalishak.galacticraftlegacy.transfer.entity.SpaceGearEquipment;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gamerules.GameRules;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class PlayerSpaceData extends GearInventoryProvider {
    public static final MapCodec<PlayerSpaceData> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SpaceGearEquipment.CODEC.fieldOf("GearEquipment").forGetter(PlayerSpaceData::getGearEquipment),
            Schematics.CODEC.fieldOf("Schematics").forGetter(PlayerSpaceData::getSchematics),
            FlagData.CODEC.optionalFieldOf("PrivateFlagData").forGetter(playerSpaceData -> Optional.ofNullable(playerSpaceData.privateFlagData))
    ).apply(instance, PlayerSpaceData::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerSpaceData> STREAM_CODEC = StreamCodec.composite(
            SpaceGearEquipment.STREAM_CODEC, GearInventoryProvider::getGearEquipment,
            Schematics.STREAM_CODEC, PlayerSpaceData::getSchematics,
            ByteBufCodecs.optional(FlagData.STREAM_CODEC), playerSpaceData -> Optional.ofNullable(playerSpaceData.privateFlagData),
            PlayerSpaceData::new
    );
    private final Schematics schematics;
    private @Nullable FlagData privateFlagData;

    private PlayerSpaceData(SpaceGearEquipment gearEquipment, Schematics schematics, Optional<FlagData> privateFlagData) {
        super(gearEquipment);
        this.schematics = schematics;
        this.privateFlagData = privateFlagData.orElse(null);
    }

    @ApiStatus.Internal
    public static PlayerSpaceData create(IAttachmentHolder attachmentHolder) {
        if (attachmentHolder instanceof Player) {
            return new PlayerSpaceData();
        }

        throw new IllegalStateException("Space Player data cannot be created for non Player entity!");
    }

    public PlayerSpaceData() {
        this(new SpaceGearEquipment(), Schematics.empty(), Optional.empty());
    }

    @Override
    public SpaceGearEquipment getGearEquipment() {
        return this.gearEquipment;
    }

    public Schematics getSchematics() {
        return this.schematics;
    }

    public ItemResource getResourceBySlot(GearEquipmentSlot slot) {
        return this.gearEquipment.getResource(slot.getIndex());
    }

    @Override
    public void dropAll(@NonNull LivingEntity entity) {
        this.gearEquipment.dropAll(entity);
    }

    @Override
    public float getThermalArmorEffectiveness() {
        int effectiveness = 4;

        for (int i = 0; i < 4; i++) {
            ItemResource resource = this.gearEquipment.getResource(i);

            if (resource.isEmpty()) {
                effectiveness--;
            }
        }

        return effectiveness / 4.0F;
    }

    @Override
    public boolean mayBreath(LivingEntity livingEntity) {
        return (livingEntity instanceof Player player && player.getAbilities().invulnerable) || super.mayBreath(livingEntity);
    }

    public FlagData getPrivateFlagData() {
        if (this.privateFlagData == null) {
            this.privateFlagData = FlagData.DEFAULT;
        }

        return this.privateFlagData;
    }

    public boolean inPlanetSelection() {
        return false;
    }

    public @Nullable PlayerSpaceData copyOnDeath(IAttachmentHolder attachmentHolder, HolderLookup.Provider provider) {
        if (attachmentHolder instanceof ServerPlayer player) {
            PlayerSpaceData spaceData = player.getData(GalacticraftAttachments.PLAYER_SPACE_DATA);
            boolean copyItems = player.level().getGameRules().get(GameRules.KEEP_INVENTORY);

            SpaceGearEquipment gear = new SpaceGearEquipment();
            Schematics schematics = Schematics.copyOf(spaceData.getSchematics());
            FlagData privateFlagData = spaceData.privateFlagData;

            if (copyItems) {
                gear.setAll(spaceData.getGearEquipment());
            }

            return new PlayerSpaceData(gear, schematics, Optional.ofNullable(privateFlagData));
        }

        return null;
    }
}
