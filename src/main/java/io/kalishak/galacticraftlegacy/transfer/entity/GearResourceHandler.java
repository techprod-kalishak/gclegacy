package io.kalishak.galacticraftlegacy.transfer.entity;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.item.GearEquippable;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import net.neoforged.neoforge.transfer.StacksResourceHandler;
import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class GearResourceHandler extends SnapshotJournal<EnumMap<GearEquipmentSlot, ItemResource>> implements MutableResourceHandler<@NonNull ItemResource>, ValueIOSerializable {
    public static final Codec<GearResourceHandler> CODEC = Codec.unboundedMap(GearEquipmentSlot.CODEC, ItemResource.CODEC).xmap(map -> {
        EnumMap<GearEquipmentSlot, ItemResource> items = new EnumMap<>(GearEquipmentSlot.class);
        items.putAll(map);
        return new GearResourceHandler(items);
    }, gearHandler -> {
        Map<GearEquipmentSlot, ItemResource> items = new EnumMap<>(gearHandler.items);
        items.values().removeIf(ItemResource::isEmpty);
        return items;
    });
    public static final StreamCodec<RegistryFriendlyByteBuf, GearResourceHandler> STREAM_CODEC = ByteBufCodecs.map(
            $ -> new EnumMap<>(GearEquipmentSlot.class),
            GearEquipmentSlot.STREAM_CODEC,
            ItemResource.STREAM_CODEC
    ).map(map -> {
        EnumMap<GearEquipmentSlot, ItemResource> items = new EnumMap<>(GearEquipmentSlot.class);
        items.putAll(map);
        return new GearResourceHandler(items);
    }, gearHandler -> {
        EnumMap<GearEquipmentSlot, ItemResource> items = new EnumMap<>(gearHandler.items);
        items.values().removeIf(ItemResource::isEmpty);
        return items;
    });
    private final EnumMap<GearEquipmentSlot, ItemResource> items;
    private final List<GearJournal> snapshots;

    GearResourceHandler(EnumMap<GearEquipmentSlot, ItemResource> items) {
        this.items = items;
        this.snapshots = new ArrayList<>(items.size());
        for (int i = 0; i < items.size(); i++) {
            snapshots.add(new GearJournal(i));
        }
    }

    public static GearResourceHandler empty() {
        EnumMap<GearEquipmentSlot, ItemResource> items = new EnumMap<>(GearEquipmentSlot.class);

        for (GearEquipmentSlot slot : GearEquipmentSlot.values()) {
            items.put(slot, ItemResource.EMPTY);
        }

        return new GearResourceHandler(items);
    }

    public static @Nullable GearEquipmentSlot findSlot(ItemResource resource) {
        GearEquippable equippable = resource.get(GalacticraftDataComponents.GEAR_EQUIPPABLE);

        return equippable != null ? equippable.gearSlot() : null;
    }

    public ItemResource setNoUpdate(GearEquipmentSlot slot, ItemResource resource) {
        return Objects.requireNonNullElse(this.items.put(slot, resource), ItemResource.EMPTY);
    }

    public void set(int index, ItemResource resource, int amount) {
        GearEquipmentSlot slot = GearEquipmentSlot.byId(index);

        if (isValid(index, resource)) {
            onChange(index, setNoUpdate(slot, resource));
        }
    }

    public void onChange(int index, ItemResource oldResource) {
        //Update Player attributes and equipped models
    }

    public boolean isEmpty() {
        for (ItemResource stack : this.items.values()) {
            if (!stack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public void clear() {
        this.items.replaceAll((slot, resource) -> ItemResource.EMPTY);
    }

    @Override
    protected EnumMap<GearEquipmentSlot, ItemResource> createSnapshot() {
        EnumMap<GearEquipmentSlot, ItemResource> snapshot = new EnumMap<>(this.items);
        snapshot.values().removeIf(ItemResource::isEmpty);

        return snapshot;
    }

    @Override
    protected void revertToSnapshot(EnumMap<GearEquipmentSlot, ItemResource> snapshot) {
        clear();
        this.items.putAll(snapshot);
    }

    @Override
    public int size() {
        return GearEquipmentSlot.values().length;
    }

    @Override
    public ItemResource getResource(int index) {
        Objects.checkIndex(index, size());
        return Objects.requireNonNullElse(this.items.get(GearEquipmentSlot.byId(index)), ItemResource.EMPTY);
    }

    @Override
    public long getAmountAsLong(int index) {
        Objects.checkIndex(index, size());
        return getResource(index).isEmpty() ? 0L : 1L;
    }

    @Override
    public long getCapacityAsLong(int index, ItemResource resource) {
        Objects.checkIndex(index, size());
        return 1L;
    }

    @Override
    public boolean isValid(int index, ItemResource resource) {
        Objects.checkIndex(index, size());

        GearEquipmentSlot slot = GearEquipmentSlot.byId(index);
        GearEquippable gearEquippable = resource.get(GalacticraftDataComponents.GEAR_EQUIPPABLE);

        return gearEquippable != null && (gearEquippable.gearSlot() == slot || gearEquippable.additionalGearSlot().filter(GearEquipmentSlot::isTank).isPresent());
    }

    @Override
    public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
        Objects.checkIndex(index, size());
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount);

        ItemResource currentResource = getResource(index);
        int count = getAmountAsInt(index);

        if (count == 0 || currentResource.matches(resource.toStack()) && isValid(index, resource)) {
            int insertedAmount = Math.min(amount, 1 - amount);

            if (insertedAmount > 0) {
                updateSnapshots(transaction);
                setNoUpdate(GearEquipmentSlot.byId(index), resource);

                return insertedAmount;
            }
        }

        return 0;
    }

    @Override
    public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
        Objects.checkIndex(index, this.size());
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount);

        ItemResource currentResource = getResource(index);

        if (resource.matches(currentResource.toStack())) {
            int currentAmount = getAmountAsInt(index);
            int extracted = Math.min(amount, currentAmount);

            if (extracted > 0) {
                updateSnapshots(transaction);
                setNoUpdate(GearEquipmentSlot.byId(index), resource);

                return extracted;
            }
        }

        return 0;
    }

    @Override
    public void serialize(ValueOutput valueOutput) {
        valueOutput.store("Gear", GearResourceHandler.CODEC, this);
    }

    @Override
    public void deserialize(ValueInput valueInput) {
        valueInput.read("Gear", GearResourceHandler.CODEC).ifPresent(savedData -> this.items.putAll(savedData.items));
    }

    private class GearJournal extends SnapshotJournal<ItemResource> {
        private final int index;

        private GearJournal(int index) {
            this.index = index;
        }

        @Override
        protected ItemResource createSnapshot() {
            return GearResourceHandler.this.items.get(GearEquipmentSlot.byId(this.index));
        }

        @Override
        protected void revertToSnapshot(ItemResource snapshot) {
            GearResourceHandler.this.items.put(GearEquipmentSlot.byId(this.index), snapshot);
        }

        @Override
        protected void onRootCommit(ItemResource originalState) {
            GearResourceHandler.this.onChange(this.index, originalState);
        }
    }
}
