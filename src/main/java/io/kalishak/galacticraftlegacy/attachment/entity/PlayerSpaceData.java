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
import io.kalishak.galacticraftlegacy.transfer.entity.SpaceGearEquipment;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import io.kalishak.galacticraftlegacy.world.entity.ai.attributes.GalacticraftAttributes;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gamerules.GameRules;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class PlayerSpaceData extends GearInventoryProvider {
    public static final MapCodec<PlayerSpaceData> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SpaceGearEquipment.CODEC.fieldOf("GearEquipment").forGetter(PlayerSpaceData::getGearEquipment)
    ).apply(instance, PlayerSpaceData::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerSpaceData> STREAM_CODEC = StreamCodec.composite(
            SpaceGearEquipment.STREAM_CODEC, GearInventoryProvider::getGearEquipment,
            PlayerSpaceData::new
    );

    private PlayerSpaceData(SpaceGearEquipment gearEquipment) {
        super(gearEquipment);
    }

    @ApiStatus.Internal
    public static PlayerSpaceData create(IAttachmentHolder attachmentHolder) {
        if (attachmentHolder instanceof Player) {
            return new PlayerSpaceData();
        }

        throw new IllegalStateException("Space Player data cannot be created for non Player entity!");
    }

    public PlayerSpaceData() {
        this(new SpaceGearEquipment());
    }

    @Override
    public SpaceGearEquipment getGearEquipment() {
        return this.gearEquipment;
    }

    @Override
    public void dropAll(ServerLevel level, @NonNull LivingEntity gearOwner, @Nullable DamageSource cause) {
        this.gearEquipment.dropAll(gearOwner);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public float getThermalArmorEffectiveness() {
        List<ItemStack> thermal = List.of(
                getGearEquipment().get(GearEquipmentSlot.THERMAL_CAP),
                getGearEquipment().get(GearEquipmentSlot.THERMAL_SHIRT),
                getGearEquipment().get(GearEquipmentSlot.THERMAL_LEGGINGS),
                getGearEquipment().get(GearEquipmentSlot.THERMAL_SOCKS)
        );

        return (float) thermal.stream()
                .filter(stack -> stack.has(GalacticraftDataComponents.GEAR_ATTRIBUTE_MODIFIERS))
                .mapToDouble(stack -> stack.get(GalacticraftDataComponents.GEAR_ATTRIBUTE_MODIFIERS).compute(GalacticraftAttributes.THERMAL_PROTECTION, 0.0D, stack.get(GalacticraftDataComponents.GEAR_EQUIPPABLE)))
                .sum() / 4.0F;
    }

    @Override
    public boolean mayBreath(LivingEntity livingEntity) {
        return (livingEntity instanceof Player player && player.getAbilities().invulnerable) || super.mayBreath(livingEntity);
    }

    public boolean inPlanetSelection() {
        return false;
    }

    public @Nullable PlayerSpaceData copyOnDeath(IAttachmentHolder attachmentHolder, HolderLookup.Provider provider) {
        if (attachmentHolder instanceof ServerPlayer player) {
            PlayerSpaceData spaceData = player.getData(GalacticraftAttachments.PLAYER_SPACE_DATA);

            boolean copyItems = player.level().getGameRules().get(GameRules.KEEP_INVENTORY);

            SpaceGearEquipment gear = new SpaceGearEquipment();

            if (copyItems) {
                gear.setAll(spaceData.getGearEquipment());
            }

            return new PlayerSpaceData(gear);
        }

        return null;
    }
}
