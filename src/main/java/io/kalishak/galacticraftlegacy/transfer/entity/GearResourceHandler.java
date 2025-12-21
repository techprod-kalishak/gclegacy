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
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class GearResourceHandler extends SnapshotJournal<EnumMap<GearEquipmentSlot, ItemResource>> implements ResourceHandler<@NonNull ItemResource>, ValueIOSerializable {
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

    GearResourceHandler(EnumMap<GearEquipmentSlot, ItemResource> items) {
        this.items = items;
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
        GearEquipmentSlot slot = findSlot(resource);

        if (isValid(index, resource) && slot != null) {
            setNoUpdate(slot, resource);
        }
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
        EnumMap<GearEquipmentSlot, ItemResource> newItems = new EnumMap<>(this.items);
        newItems.values().removeIf(ItemResource::isEmpty);

        return newItems;
    }

    @Override
    protected void revertToSnapshot(EnumMap<GearEquipmentSlot, ItemResource> gearEquipmentSlotItemResourceEnumMap) {
        clear();
        this.items.putAll(gearEquipmentSlotItemResourceEnumMap);
    }

    @Override
    public int size() {
        return GearEquipmentSlot.values().length;
    }

    @Override
    public ItemResource getResource(int index) {
        Objects.checkIndex(index, size());
        return this.items.get(GearEquipmentSlot.values()[index]);
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
        GearEquipmentSlot slot = GearEquipmentSlot.values()[index];
        GearEquippable equippable = resource.get(GalacticraftDataComponents.GEAR_EQUIPPABLE);

        return equippable != null && equippable.gearSlot() == slot;
    }

    @Override
    public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
        Objects.checkIndex(index, size());
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount);
        ItemResource currentResource = getResource(index);

        if (currentResource.isEmpty() && isValid(index, resource)) {
            int insertedAmount = Math.min(amount, 1 - amount);

            if (insertedAmount > 0) {
                updateSnapshots(transaction);
                currentResource = getResource(index);

                if (currentResource.isEmpty()) {
                    currentResource = resource;
                }

                set(index, currentResource, insertedAmount);

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

        if (resource.is(currentResource.getHolder())) {
            int extracted = Math.min(1, amount);

            if (extracted > 0) {
                updateSnapshots(transaction);

                set(index, ItemResource.EMPTY, extracted);

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
}
