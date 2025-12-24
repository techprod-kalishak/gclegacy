package io.kalishak.galacticraftlegacy.world.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.attachment.GearInventory;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.transfer.ResourcefulHelper;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

@SuppressWarnings("deprecation")
public record GearEquippable(GearEquipmentSlot gearSlot, Holder<SoundEvent> equipSound, Optional<GearEquipmentSlot> additionalGearSlot, Optional<ResourceKey<EquipmentAsset>> assetId, Optional<HolderSet<EntityType<?>>> allowedEntities,
                             boolean damageable, boolean dispensable, boolean swappable, boolean equipOnInteract, boolean canBeSheared, Holder<SoundEvent> shearingSound) {
    public static final Codec<GearEquippable> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            GearEquipmentSlot.CODEC.fieldOf("gear_slot").forGetter(GearEquippable::gearSlot),
            SoundEvent.CODEC.optionalFieldOf("equip_sound", SoundEvents.ARMOR_EQUIP_GENERIC).forGetter(GearEquippable::equipSound),
            GearEquipmentSlot.CODEC.optionalFieldOf("additional_gear_slot").forGetter(GearEquippable::additionalGearSlot),
            ResourceKey.codec(EquipmentAssets.ROOT_ID).optionalFieldOf("asset_id").forGetter(GearEquippable::assetId),
            RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE).optionalFieldOf("allowed_entities").forGetter(GearEquippable::allowedEntities),
            Codec.BOOL.fieldOf("damageable").forGetter(GearEquippable::damageable),
            Codec.BOOL.fieldOf("dispensable").forGetter(GearEquippable::dispensable),
            Codec.BOOL.fieldOf("swappable").forGetter(GearEquippable::swappable),
            Codec.BOOL.fieldOf("equip_on_interact").forGetter(GearEquippable::equipOnInteract),
            Codec.BOOL.fieldOf("can_be_sheared").forGetter(GearEquippable::canBeSheared),
            SoundEvent.CODEC.optionalFieldOf("shearing_sound", BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.SHEARS_SNIP)).forGetter(GearEquippable::shearingSound)
    ).apply(instance, GearEquippable::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, GearEquippable> STREAM_CODEC = StreamCodec.composite(
            GearEquipmentSlot.STREAM_CODEC, GearEquippable::gearSlot,
            SoundEvent.STREAM_CODEC, GearEquippable::equipSound,
            GearEquipmentSlot.STREAM_CODEC.apply(ByteBufCodecs::optional), GearEquippable::additionalGearSlot,
            ResourceKey.streamCodec(EquipmentAssets.ROOT_ID).apply(ByteBufCodecs::optional), GearEquippable::assetId,
            ByteBufCodecs.holderSet(Registries.ENTITY_TYPE).apply(ByteBufCodecs::optional), GearEquippable::allowedEntities,
            ByteBufCodecs.BOOL, GearEquippable::damageable,
            ByteBufCodecs.BOOL, GearEquippable::dispensable,
            ByteBufCodecs.BOOL, GearEquippable::swappable,
            ByteBufCodecs.BOOL, GearEquippable::equipOnInteract,
            ByteBufCodecs.BOOL, GearEquippable::canBeSheared,
            SoundEvent.STREAM_CODEC, GearEquippable::shearingSound,
            GearEquippable::new
    );

    public static GearEquippable tank(ResourceKey<EquipmentAsset> assetId) {
        //HolderGetter<EntityType<?>> holderGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.ENTITY_TYPE);
        return builder(GearEquipmentSlot.TANK)
                .setAdditionalSlot(GearEquipmentSlot.ADDITIONAL_TANK)
                .setEquipSound(SoundEvents.ARMOR_EQUIP_IRON)
                .setAsset(assetId)
                .setAllowedEntities(EntityType.PLAYER, EntityType.WOLF, EntityType.CAT)
                .setCanBeSheared(true)
                .setShearingSound(SoundEvents.HORSE_ARMOR_UNEQUIP)
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
                .setEquipOnInteract(true)
                .setCanBeSheared(true)
                .setShearingSound(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.HARNESS_UNEQUIP))
                .build();
    }

    public static Builder builder(GearEquipmentSlot gearSlot) {
        return new Builder(gearSlot);
    }

    private static boolean isSameGear(ItemResource resource, GearEquipmentSlot gearSlot) {
        GearEquippable equippable = resource.get(GalacticraftDataComponents.GEAR_EQUIPPABLE);

        return equippable != null && equippable.gearSlot() == gearSlot;
    }

    public InteractionResult swapWithGearEquipmentSlot(ItemStack equippedFromHand, Player player, @Nullable Transaction tx) {
        if (!player.hasData(GalacticraftAttachments.GEAR_INVENTORY) || !canBeEquippedBy(player.getType())) return InteractionResult.PASS;

        GearInventory data = player.getData(GalacticraftAttachments.GEAR_INVENTORY);
        ResourceHandler<ItemResource> gearResourceHandler = data.getDelegatingResourceHandler();
        ItemAccess playerHand = ItemAccess.forPlayerInteraction(player, player.getUsedItemHand());

        ItemResource resourceInSlot = data.getResourceBySlot(this.gearSlot);
        ItemResource resourceInHand = playerHand.getResource();

        if ((!EnchantmentHelper.has(resourceInSlot.toStack(), EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE) || player.isCreative()) && !resourceInSlot.matches(equippedFromHand)) {
            if (!player.level().isClientSide()) {
                player.awardStat(Stats.ITEM_USED.get(resourceInSlot.getItem()));
            }

            if (equippedFromHand.getCount() <= 1) {
                try (Transaction childTx = Transaction.open(tx)) {
                    ItemStack returnedFromGear = resourceInSlot.isEmpty() ? equippedFromHand : resourceInSlot.toStack();

                    if (playerHand.exchange(ItemResource.of(returnedFromGear), 1, childTx) > 0) {
                        if (ResourcefulHelper.exchange(gearResourceHandler, gearSlot().getIndex(), resourceInHand, 1, childTx) > 0) {
                            childTx.commit();

                            return InteractionResult.SUCCESS.heldItemTransformedTo(returnedFromGear);
                        }
                    }
                }
            }

            return InteractionResult.PASS;
        }

        return InteractionResult.FAIL;
    }

    public InteractionResult equipOnTarget(Player player, LivingEntity entity, ItemStack stack, @Nullable Transaction tx) {
        if (this.equipOnInteract || stack.isEmpty()) {
            return InteractionResult.PASS;
        }

        GearInventory entityInventory = entity.getData(GalacticraftAttachments.GEAR_INVENTORY);

        if (canBeEquippedBy(entity.getType()) && entityInventory.getResourceBySlot(this.gearSlot).isEmpty()) {
            if (!player.level().isClientSide()) {
                ItemAccess playerHand = ItemAccess.forPlayerInteraction(player, player.getUsedItemHand());
                ItemResource resource = ItemResource.of(stack);

                try (Transaction childTx = Transaction.open(tx)) {
                    int extracted = playerHand.extract(resource, 1, childTx);

                    if (extracted > 0 && entityInventory.getDelegatingResourceHandler().insert(resource, 1, childTx) > 0) {
                        stack.split(1);
                        entityInventory.guaranteeDrop(gearSlot);
                        childTx.commit();

                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }

        return InteractionResult.PASS;
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
        private boolean damageable;
        private boolean dispensable;
        private boolean swappable;
        private boolean equipOnInteract;
        private boolean canBeSheared;
        private Holder<SoundEvent> shearingSound;

        Builder(GearEquipmentSlot gearSlot) {
            this.gearSlot = gearSlot;
            this.equipSound = SoundEvents.ARMOR_EQUIP_GENERIC;
            this.dispensable = true;
            this.swappable = true;
            this.shearingSound = BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.SHEARS_SNIP);
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

        public Builder setDamageable(boolean damageable) {
            this.damageable = damageable;
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

        public Builder setEquipOnInteract(boolean equipOnInteract) {
            this.equipOnInteract = equipOnInteract;
            return this;
        }

        public Builder setCanBeSheared(boolean canBeSheared) {
            this.canBeSheared = canBeSheared;
            return this;
        }

        public Builder setShearingSound(Holder<SoundEvent> shearingSound) {
            this.shearingSound = shearingSound;
            return this;
        }

        public GearEquippable build() {
            return new GearEquippable(this.gearSlot, this.equipSound, Optional.ofNullable(this.additionalSlot), Optional.ofNullable(this.assetId), Optional.ofNullable(this.allowedEntities), this.damageable, this.dispensable, this.swappable, this.equipOnInteract, this.canBeSheared, this.shearingSound);
        }
    }
}
