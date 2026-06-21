/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.attachment.AttachmentHelper;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.transfer.entity.SpaceGearEquipment;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import io.kalishak.galacticraftlegacy.world.item.GearEquipmentAssets;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

@SuppressWarnings("deprecation")
public record GearEquippable(GearEquipmentSlot gearSlot, Holder<SoundEvent> equipSound, Optional<GearEquipmentSlot> additionalGearSlot, Optional<ResourceKey<EquipmentAsset>> assetId,
                             Optional<HolderSet<EntityType<?>>> allowedEntities, boolean dispensable, boolean swappable) {
    public static final Codec<GearEquippable> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            GearEquipmentSlot.CODEC.fieldOf("gear_slot").forGetter(GearEquippable::gearSlot),
            SoundEvent.CODEC.optionalFieldOf("equip_sound", SoundEvents.ARMOR_EQUIP_GENERIC).forGetter(GearEquippable::equipSound),
            GearEquipmentSlot.CODEC.optionalFieldOf("additional_gear_slot").forGetter(GearEquippable::additionalGearSlot),
            ResourceKey.codec(EquipmentAssets.ROOT_ID).optionalFieldOf("asset_id").forGetter(GearEquippable::assetId),
            RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE).optionalFieldOf("allowed_entities").forGetter(GearEquippable::allowedEntities),
            Codec.BOOL.fieldOf("dispensable").forGetter(GearEquippable::dispensable),
            Codec.BOOL.fieldOf("swappable").forGetter(GearEquippable::swappable)
    ).apply(instance, GearEquippable::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, GearEquippable> STREAM_CODEC = StreamCodec.composite(
            GearEquipmentSlot.STREAM_CODEC, GearEquippable::gearSlot,
            SoundEvent.STREAM_CODEC, GearEquippable::equipSound,
            GearEquipmentSlot.STREAM_CODEC.apply(ByteBufCodecs::optional), GearEquippable::additionalGearSlot,
            ResourceKey.streamCodec(EquipmentAssets.ROOT_ID).apply(ByteBufCodecs::optional), GearEquippable::assetId,
            ByteBufCodecs.holderSet(Registries.ENTITY_TYPE).apply(ByteBufCodecs::optional), GearEquippable::allowedEntities,
            ByteBufCodecs.BOOL, GearEquippable::dispensable,
            ByteBufCodecs.BOOL, GearEquippable::swappable,
            GearEquippable::new
    );

    public static GearEquippable tank(ResourceKey<EquipmentAsset> assetId) {
        HolderGetter<EntityType<?>> holderGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.ENTITY_TYPE);
        return builder(GearEquipmentSlot.TANK)
                .setAdditionalSlot(GearEquipmentSlot.ADDITIONAL_TANK)
                .setEquipSound(SoundEvents.ARMOR_EQUIP_IRON)
                .setAsset(assetId)
                .setAllowedEntities(holderGetter.getOrThrow(GalacticraftTags.EntityTypes.CAN_EQUIP_GEAR))
                .build();
    }
    public static GearEquippable thermal(GearEquipmentSlot gearSlot, ResourceKey<EquipmentAsset> assetId) {
        return builder(gearSlot)
                .setEquipSound(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.WOOL_PLACE))
                .setAsset(assetId)
                .setAllowedEntities(EntityType.PLAYER)
                .build();
    }

    public static GearEquippable parachute(DyeColor color) {
        HolderGetter<EntityType<?>> holdergetter = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.ENTITY_TYPE);
        return builder(GearEquipmentSlot.PARACHUTE)
                .setEquipSound(SoundEvents.HARNESS_EQUIP)
                .setAsset(GearEquipmentAssets.PARACHUTES.get(color))
                .setAllowedEntities(holdergetter.getOrThrow(GalacticraftTags.EntityTypes.CAN_EQUIP_PARACHUTE))
                .build();
    }

    public static Builder builder(GearEquipmentSlot gearSlot) {
        return new Builder(gearSlot);
    }

    public static Optional<ResourceKey<EquipmentAsset>> extractAssetId(ItemStack itemStack) {
        GearEquippable gearEquippable = itemStack.get(GalacticraftDataComponents.GEAR_EQUIPPABLE);

        return Optional.ofNullable(gearEquippable).flatMap(GearEquippable::assetId);
    }

    private static boolean isSameGear(ItemResource resource, GearEquipmentSlot gearSlot) {
        GearEquippable equippable = resource.get(GalacticraftDataComponents.GEAR_EQUIPPABLE);

        return equippable != null && equippable.gearSlot() == gearSlot;
    }

    public InteractionResult swapWithGearEquipmentSlot(ItemStack equippedFromHand, Player player, @Nullable Transaction tx) {
        if (!canBeEquippedBy(player.getType())) return InteractionResult.PASS;

        SpaceGearEquipment gearResourceHandler = AttachmentHelper.getGearInventory(player).getGearEquipment();

        ItemStack stackInSlot = gearResourceHandler.get(this.gearSlot);

        if ((!EnchantmentHelper.has(stackInSlot, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE) || player.isCreative()) && !ItemStack.isSameItemSameComponents(stackInSlot, equippedFromHand)) {
            if (!player.level().isClientSide()) {
                player.awardStat(Stats.ITEM_USED.get(equippedFromHand.getItem()));
            }

            if (equippedFromHand.getCount() <= 1) {
                ItemStack returnedFromGear;
                ItemStack placedToGear = player.isCreative() ? equippedFromHand.copy() : equippedFromHand.copyAndClear();

                if (stackInSlot.isEmpty()) {
                    returnedFromGear = equippedFromHand;
                } else {
                    returnedFromGear = stackInSlot.copyWithCount(1);
                    gearResourceHandler.set(this.gearSlot, ItemStack.EMPTY); //bruh
                }

                gearResourceHandler.set(this.gearSlot, placedToGear);


                return InteractionResult.SUCCESS.heldItemTransformedTo(returnedFromGear);
            } else {
                ItemStack returnedFromGear = stackInSlot.copyWithCount(1);
                ItemStack placedToGear = equippedFromHand.consumeAndReturn(1, player);
                gearResourceHandler.set(this.gearSlot, placedToGear);

                if (!player.getInventory().add(returnedFromGear)) {
                    player.drop(returnedFromGear, false);
                }

                return InteractionResult.SUCCESS.heldItemTransformedTo(equippedFromHand);
            }
        }

        return InteractionResult.FAIL;
    }

    public boolean canBeEquippedBy(EntityType<?> entityType) {
        return this.allowedEntities.isEmpty() || this.allowedEntities.get().contains(entityType.builtInRegistryHolder());
    }

    public static class Builder {
        private final GearEquipmentSlot gearSlot;
        private Holder<SoundEvent> equipSound;
        private @Nullable GearEquipmentSlot additionalSlot;
        private @Nullable ResourceKey<EquipmentAsset> assetId;
        private @Nullable HolderSet<EntityType<?>> allowedEntities;
        private boolean dispensable;
        private boolean swappable;

        Builder(GearEquipmentSlot gearSlot) {
            this.gearSlot = gearSlot;
            this.equipSound = SoundEvents.ARMOR_EQUIP_GENERIC;
            this.dispensable = true;
            this.swappable = true;
        }

        public Builder setEquipSound(Holder<SoundEvent> equipSound) {
            this.equipSound = equipSound;
            return this;
        }

        public Builder setAdditionalSlot(@Nullable GearEquipmentSlot additionalSlot) {
            this.additionalSlot = additionalSlot;
            return this;
        }

        public Builder setAsset(@Nullable ResourceKey<EquipmentAsset> asset) {
            this.assetId = asset;
            return this;
        }

        public Builder setAllowedEntities(EntityType<?>... allowedEntities) {
            return setAllowedEntities(HolderSet.direct(EntityType::builtInRegistryHolder, allowedEntities));
        }

        public Builder setAllowedEntities(@Nullable HolderSet<EntityType<?>> allowedEntities) {
            this.allowedEntities = allowedEntities;
            return this;
        }

        public Builder setDispensable(boolean dispensable) {
            this.dispensable = dispensable;
            return this;
        }

        public Builder setSwappable(boolean swappable) {
            this.swappable = swappable;
            return this;
        }

        public GearEquippable build() {
            return new GearEquippable(this.gearSlot, this.equipSound, Optional.ofNullable(this.additionalSlot), Optional.ofNullable(this.assetId), Optional.ofNullable(this.allowedEntities), this.dispensable, this.swappable);
        }
    }
}
