/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.attachment.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.transfer.entity.SpaceGearEquipment;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import io.kalishak.galacticraftlegacy.world.entity.ai.attributes.GalacticraftAttributes;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gamerules.GameRules;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class PlayerSpaceData extends GearInventoryProvider {
    public static final MapCodec<PlayerSpaceData> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SpaceGearEquipment.CODEC.fieldOf("GearEquipment").forGetter(PlayerSpaceData::getGearEquipment),
            Schematics.CODEC.fieldOf("Schematics").forGetter(PlayerSpaceData::getSchematics),
            Codec.BOOL.optionalFieldOf("IsSensorGlassesActivated", false).forGetter(PlayerSpaceData::isSensorGlassesActivated),
            Codec.BOOL.optionalFieldOf("InPlanetSelection", false).forGetter(PlayerSpaceData::inPlanetSelection)
    ).apply(instance, PlayerSpaceData::sync));
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerSpaceData> STREAM_CODEC = StreamCodec.composite(
            SpaceGearEquipment.STREAM_CODEC, GearInventoryProvider::getGearEquipment,
            Schematics.STREAM_CODEC, PlayerSpaceData::getSchematics,
            ByteBufCodecs.BOOL, PlayerSpaceData::isSensorGlassesActivated,
            ByteBufCodecs.BOOL, PlayerSpaceData::inPlanetSelection,
            PlayerSpaceData::sync
    );
    private boolean isSensorGlassesActivated = false;
    private boolean inPlanetSelection = false;
    private final Schematics unlockedSchematics = Schematics.empty();

    private PlayerSpaceData(SpaceGearEquipment gearEquipment) {
        super(gearEquipment);
    }

    private static PlayerSpaceData sync(SpaceGearEquipment spaceGearEquipment, Schematics schematics, boolean isSensorGlassesActivated, boolean inPlanetSelection) {
        PlayerSpaceData spaceData = new PlayerSpaceData(spaceGearEquipment);
        spaceData.unlockedSchematics.sync(schematics);
        spaceData.toggleSensorGlasses(isSensorGlassesActivated);
        spaceData.togglePlanetSelection(inPlanetSelection);

        return spaceData;
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
    public void serverGearTick(ServerLevel serverLevel, LivingEntity gearOwner) {
        super.serverGearTick(serverLevel, gearOwner);

        if (this.isSensorGlassesActivated) {
            ItemStack stack = gearOwner.getItemBySlot(EquipmentSlot.HEAD);

            if (!stack.is(GalacticraftItems.SENSOR_GLASSES)) {
                this.isSensorGlassesActivated = false;
            }
        }
    }

    @Override
    public void dropAll(ServerLevel level, @NonNull LivingEntity gearOwner, @Nullable DamageSource cause) {
        this.gearEquipment.dropAll(gearOwner);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean isThermalPaddingEffective(float temperatureModifier) {
        List<ItemStack> thermal = List.of(
                getGearEquipment().get(GearEquipmentSlot.THERMAL_CAP),
                getGearEquipment().get(GearEquipmentSlot.THERMAL_SHIRT),
                getGearEquipment().get(GearEquipmentSlot.THERMAL_LEGGINGS),
                getGearEquipment().get(GearEquipmentSlot.THERMAL_SOCKS)
        );

        float effectiveness = (float) thermal.stream()
                .filter(stack -> stack.has(GalacticraftDataComponents.GEAR_ATTRIBUTE_MODIFIERS))
                .mapToDouble(stack -> stack.get(GalacticraftDataComponents.GEAR_ATTRIBUTE_MODIFIERS).compute(GalacticraftAttributes.THERMAL_PROTECTION, 0.0D, stack.get(GalacticraftDataComponents.GEAR_EQUIPPABLE)))
                .sum();

        return Math.abs(effectiveness) > Math.abs(temperatureModifier);
    }

    @Override
    public boolean mayBreath(LivingEntity livingEntity) {
        return (livingEntity instanceof Player player && player.getAbilities().invulnerable) || super.mayBreath(livingEntity);
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

    @Override
    public SpaceGearEquipment getGearEquipment() {
        return this.gearEquipment;
    }

    public Schematics getSchematics() {
        return this.unlockedSchematics;
    }

    public void toggleSensorGlasses(boolean state) {
        this.isSensorGlassesActivated = state;
    }

    public boolean isSensorGlassesActivated() {
        return this.isSensorGlassesActivated;
    }

    public void togglePlanetSelection(boolean state) {
        this.inPlanetSelection = state;
    }

    public boolean inPlanetSelection() {
        return this.inPlanetSelection;
    }
}
