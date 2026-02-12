package io.kalishak.galacticraftlegacy.transfer.entity;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.Clearable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;

public class SpaceGearEquipment implements ResourceHandler<ItemResource>, Clearable {
    public static final Codec<SpaceGearEquipment> CODEC = Codec.unboundedMap(GearEquipmentSlot.CODEC, ItemStack.OPTIONAL_CODEC).xmap(map -> {
        EnumMap<GearEquipmentSlot, ItemStack> items = new EnumMap<>(GearEquipmentSlot.class);
        items.putAll(map);
        return new SpaceGearEquipment(items);
    }, gearEquipment -> {
        EnumMap<GearEquipmentSlot, ItemStack> items = new EnumMap<>(gearEquipment.items);
        items.values().removeIf(ItemStack::isEmpty);
        return items;
    });
    public static final StreamCodec<RegistryFriendlyByteBuf, SpaceGearEquipment> STREAM_CODEC = ByteBufCodecs.map(
            size -> new EnumMap<>(GearEquipmentSlot.class),
            GearEquipmentSlot.STREAM_CODEC,
            ItemStack.OPTIONAL_STREAM_CODEC
    ).map(SpaceGearEquipment::new, spaceGearEquipment -> spaceGearEquipment.items);

    private final EnumMap<GearEquipmentSlot, ItemStack> items;
    private final List<GearJournal> snapshots;

    private SpaceGearEquipment(EnumMap<GearEquipmentSlot, ItemStack> items) {
        this.items = items;
        this.snapshots = new ArrayList<>(GearEquipmentSlot.values().length);

        for (int i = 0; i < GearEquipmentSlot.values().length; i++) {
            this.snapshots.add(new GearJournal(i));
        }
    }

    public SpaceGearEquipment() {
        this(new EnumMap<>(GearEquipmentSlot.class));
    }

    private ItemStack set(GearEquipmentSlot slot, ItemStack stack) {
        return Objects.requireNonNullElse(this.items.put(slot, stack), ItemStack.EMPTY);
    }

    public void set(int index, ItemResource itemResource, int amount) {
        TransferPreconditions.checkNonNegative(amount);

        ItemStack previousStack = set(GearEquipmentSlot.byId(index), itemResource.toStack(amount));
        setChanged(index, previousStack);
    }

    private ItemStack get(GearEquipmentSlot slot) {
        return this.items.getOrDefault(slot, ItemStack.EMPTY);
    }

    public boolean isEmpty() {
        for (ItemStack stack : this.items.values()) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean checkGear(BiPredicate<GearEquipmentSlot, ItemStack> predicate) {
        return this.items.entrySet().stream().allMatch(entry -> predicate.test(entry.getKey(), entry.getValue()));
    }

    public void setAll(SpaceGearEquipment other) {
        this.items.clear();
        this.items.putAll(other.items);
    }

    public void dropAll(LivingEntity entity) {
        for (ItemStack stack : this.items.values()) {
            if (!stack.isEmpty()) {
                entity.drop(stack, true, false);
            }
        }

        this.items.clear();
    }

    public void setChanged(int index, ItemStack oldResource) {

    }

    @Override
    public boolean isValid(int index, ItemResource resource) {
        return true;
    }

    @Override
    public int size() {
        return 12;
    }

    @Override
    public ItemResource getResource(int index) {
        Objects.checkIndex(index, size());
        return ItemResource.of(get(GearEquipmentSlot.byId(index)));
    }

    @Override
    public long getAmountAsLong(int index) {
        Objects.checkIndex(index, size());
        return get(GearEquipmentSlot.byId(index)).getCount();
    }

    @Override
    public long getCapacityAsLong(int index, ItemResource resource) {
        return 1L;
    }

    @Override
    public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
        Objects.checkIndex(index, size());
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount);

        ItemStack currentStack = get(GearEquipmentSlot.byId(index));
        int currentAmount = currentStack.getCount();

        if ((currentAmount == 0 || resource.matches(currentStack) && isValid(index, resource))) {
            int inserted = Math.min(amount, 1 - currentAmount);

            if (inserted > 0) {
                this.snapshots.get(index).updateSnapshots(transaction);
                set(GearEquipmentSlot.byId(index), resource.toStack(currentAmount + inserted));
                return inserted;
            }
        }

        return 0;
    }

    @Override
    public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
        Objects.checkIndex(index, size());
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount);

        ItemStack currentStack = get(GearEquipmentSlot.byId(index));

        if (resource.matches(currentStack)) {
            int currentAmount = getAmountAsInt(index);
            int extracted = Math.min(amount, currentAmount);

            if (extracted > 0) {
                this.snapshots.get(index).updateSnapshots(transaction);
                set(GearEquipmentSlot.byId(index), resource.toStack(currentAmount - extracted));
                return extracted;
            }
        }

        return 0;
    }

    @Override
    public void clearContent() {
        this.items.replaceAll((slot, resource) -> ItemStack.EMPTY);
    }

    private class GearJournal extends SnapshotJournal<ItemStack> {
        private final int index;

        private GearJournal(int index) {
            this.index = index;
        }

        @Override
        protected ItemStack createSnapshot() {
            return SpaceGearEquipment.this.get(GearEquipmentSlot.byId(this.index)).copy();
        }

        @Override
        protected void revertToSnapshot(ItemStack snapshot) {
            SpaceGearEquipment.this.set(GearEquipmentSlot.byId(this.index), snapshot);
        }

        @Override
        protected void onRootCommit(ItemStack originalState) {
            SpaceGearEquipment.this.setChanged(this.index, originalState);
        }
    }
}
