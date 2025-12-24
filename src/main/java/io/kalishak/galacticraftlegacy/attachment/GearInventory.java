package io.kalishak.galacticraftlegacy.attachment;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.transfer.entity.GearResourceHandler;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.item.GearEquippable;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gamerules.GameRules;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.transfer.DelegatingResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;

public class GearInventory {
    public static final MapCodec<GearInventory> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            GearResourceHandler.CODEC.fieldOf("gear").forGetter(gearInventory -> gearInventory.resourceHandler),
            ItemStack.OPTIONAL_CODEC.listOf(1, 3).xmap(NonNullList::copyOf, Function.identity()).fieldOf("guaranteed_drops").forGetter(gearInventory -> gearInventory.guaranteedDrops)
    ).apply(instance, GearInventory::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, GearInventory> STREAM_CODEC = StreamCodec.composite(
            GearResourceHandler.STREAM_CODEC, gearInventory -> gearInventory.resourceHandler,
            ByteBufCodecs.<RegistryFriendlyByteBuf, ItemStack>list(3).apply(ItemStack.OPTIONAL_STREAM_CODEC).map(NonNullList::copyOf, Function.identity()), gearInventory -> gearInventory.guaranteedDrops,
            GearInventory::new
    );
    private final GearResourceHandler resourceHandler;
    private final NonNullList<ItemStack> guaranteedDrops;

    GearInventory(GearResourceHandler resourceHandler, NonNullList<ItemStack> guaranteedDrops) {
        this.resourceHandler = resourceHandler;
        this.guaranteedDrops = guaranteedDrops;
    }

    public GearInventory() {
        this(GearResourceHandler.empty(), NonNullList.withSize(3, ItemStack.EMPTY));
    }

    public void tick(@NonNull LivingEntity entity) {
        for (int i = 0; i < this.resourceHandler.size(); i++) {
            ItemStack stack = ItemUtil.getStack(this.resourceHandler, i);

            if (!stack.isEmpty()) {
                stack.inventoryTick(entity.level(), entity, null);
            }
        }
    }

    public void onGearEquipped(LivingEntity entity, GearEquipmentSlot slot, ItemStack newStack, ItemStack oldStack) {
        if (!entity.level().isClientSide() && !entity.isSpectator()) {
            if (!ItemStack.isSameItemSameComponents(oldStack, newStack)) {
                GearEquippable gearEquippable = newStack.get(GalacticraftDataComponents.GEAR_EQUIPPABLE);

                if (!entity.isSilent() && gearEquippable != null && slot == gearEquippable.gearSlot()) {
                    entity.level().playSeededSound(
                            null,
                            entity.getX(),
                            entity.getY(),
                            entity.getZ(),
                            gearEquippable.equipSound(),
                            entity.getSoundSource(),
                            1.0F,
                            1.0F,
                            entity.getRandom().nextLong()
                    );
                }
            }
        }
    }

    public DelegatingResourceHandler<ItemResource> getDelegatingResourceHandler() {
        return new DelegatingResourceHandler<>(() -> this.resourceHandler);
    }

    public GearResourceHandler getGearResourceHandler() {
        return this.resourceHandler;
    }

    public ItemResource getResourceBySlot(GearEquipmentSlot slot) {
        return this.resourceHandler.getResource(slot.getIndex());
    }

    private static boolean isEmpty(NonNullList<ItemStack> list) {
        for (ItemStack itemStack : list) {
            if (!itemStack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public void dropAll(@NonNull LivingEntity entity, boolean isPlayer) {
        if (isPlayer) {
            for (int i = 0; i < this.resourceHandler.size(); i++) {
                ItemStack stack = ItemUtil.getStack(this.resourceHandler, i);

                entity.drop(stack, true, false);
            }
        } else if (!isEmpty(this.guaranteedDrops)) {
            for (ItemStack guaranteedDrop : this.guaranteedDrops) {
                if (!guaranteedDrop.isEmpty()) {
                    entity.drop(guaranteedDrop, true, false);
                }
            }

            this.guaranteedDrops.clear();
        }

        this.resourceHandler.clear();
    }

    public void guaranteeDrop(GearEquipmentSlot slot) {
        ItemStack stack = getResourceBySlot(slot).toStack();

        if (!stack.isEmpty()) {
            this.guaranteedDrops.add(stack);
        }
    }

    public @Nullable GearInventory copyOnDeath(IAttachmentHolder attachmentHolder, HolderLookup.Provider provider) {
        if (attachmentHolder instanceof ServerPlayer player) {
            boolean doCopy = player.level().getGameRules().get(GameRules.KEEP_INVENTORY);

            if (!player.isCreative() && doCopy) {
                GearResourceHandler gear = GearResourceHandler.empty();
                for (int i = 0; i < this.resourceHandler.size(); i++) {
                    if (!this.resourceHandler.getResource(i).isEmpty()) {
                        gear.set(i, this.resourceHandler.getResource(i), 1);
                    }
                }

                return new GearInventory(gear, NonNullList.create());
            }
        }

        return null;
    }
}
