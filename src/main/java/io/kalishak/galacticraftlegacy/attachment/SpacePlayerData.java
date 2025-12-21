package io.kalishak.galacticraftlegacy.attachment;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.transfer.entity.GearResourceHandler;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gamerules.GameRules;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.transfer.DelegatingResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class SpacePlayerData {
    public static final MapCodec<SpacePlayerData> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            GearResourceHandler.CODEC.fieldOf("gear").forGetter(spaceData -> spaceData.resourceHandler)
    ).apply(instance, SpacePlayerData::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, SpacePlayerData> STREAM_CODEC = GearResourceHandler.STREAM_CODEC.map(SpacePlayerData::new, spaceData -> spaceData.resourceHandler);
    private final GearResourceHandler resourceHandler;

    SpacePlayerData(GearResourceHandler resourceHandler) {
        this.resourceHandler = resourceHandler;
    }

    public SpacePlayerData() {
        this(GearResourceHandler.empty());
    }

    public void tick(@NonNull LivingEntity entity) {
        for (int i = 0; i < this.resourceHandler.size(); i++) {
            ItemStack stack = ItemUtil.getStack(this.resourceHandler, i);

            if (!stack.isEmpty()) {
                stack.inventoryTick(entity.level(), entity, null);
            }
        }
    }

    public void setChanged() {
        //send packet
    }

    public ResourceHandler<ItemResource> getResourceHandler() {
        return new DelegatingResourceHandler<>(this.resourceHandler);
    }

    public ItemResource getResourceBySlot(GearEquipmentSlot slot) {
        return this.resourceHandler.getResource(slot.getIndex());
    }

    public void updateGear(GearEquipmentSlot slot, ItemResource resource) {
        this.resourceHandler.setNoUpdate(slot, resource);
    }

    public void dropAll(@NonNull LivingEntity entity) {
        for (int i = 0; i < this.resourceHandler.size(); i++) {
            ItemStack stack = ItemUtil.getStack(this.resourceHandler, i);

            entity.drop(stack, true, false);
        }

        this.resourceHandler.clear();
    }

    public @Nullable SpacePlayerData copyOnDeath(IAttachmentHolder attachmentHolder, HolderLookup.Provider provider) {
        if (attachmentHolder instanceof ServerPlayer player) {
            boolean doCopy = player.level().getGameRules().get(GameRules.KEEP_INVENTORY);

            if (!player.isCreative() && doCopy) {
                GearResourceHandler gear = GearResourceHandler.empty();
                for (int i = 0; i < this.resourceHandler.size(); i++) {
                    if (!this.resourceHandler.getResource(i).isEmpty()) {
                        gear.set(i, this.resourceHandler.getResource(i), 1);
                    }
                }

                return new SpacePlayerData(gear);
            }
        }

        return null;
    }
}
