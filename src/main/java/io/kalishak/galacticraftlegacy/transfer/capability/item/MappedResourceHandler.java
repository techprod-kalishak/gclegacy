package io.kalishak.galacticraftlegacy.transfer.capability.item;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.codec.SerializableEnum;
import net.minecraft.world.Clearable;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.resource.Resource;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Objects;

public abstract class MappedResourceHandler<S, R extends Resource, E extends Enum<E> & SerializableEnum> implements ResourceHandler<R>, ValueIOSerializable, Clearable {
    protected final EnumMap<E, S> stacks;
    protected final S emptyStack;
    protected final Codec<EnumMap<E, S>> codec;
    private final Class<E> enumClass;
    private final ArrayList<ValueJournal> snapshotJournals;

    protected MappedResourceHandler(Class<E> enumClass, S emptyStack, Codec<EnumMap<E, S>> codec) {
        this(new EnumMap<>(enumClass), enumClass, emptyStack, codec);
    }

    protected MappedResourceHandler(EnumMap<E, S> stacks, Class<E> enumClass, S emptyStack, Codec<EnumMap<E, S>> codec) {
        this.stacks = stacks;
        this.emptyStack = emptyStack;
        this.codec = codec;
        this.enumClass = enumClass;
        this.snapshotJournals = new ArrayList<>(enumClass.getEnumConstants().length);

        updateStacksSize();
    }

    private void updateStacksSize() {
        this.snapshotJournals.ensureCapacity(size());

        for (int i = 0; i < size(); i++) {
            E entry = this.enumClass.getEnumConstants()[i];
            this.snapshotJournals.add(new ValueJournal(entry));
        }

        if (this.snapshotJournals.size() > size()) {
            this.snapshotJournals.subList(size(), this.snapshotJournals.size()).clear();
        }

    }

    protected abstract R getResource(S stack);
    protected abstract int getAmount(S stack);
    protected abstract S getStack(R resource, int amount);
    protected abstract S copyOf(S stack);

    protected abstract int getCapacity(E entry, R resource);

    protected S getOrDefault(E entry) {
        return this.stacks.getOrDefault(entry, this.emptyStack);
    }

    protected boolean matches(S stack, R resource) {
        return getResource(stack).equals(resource);
    }

    protected void onContentsChanged(E entry, S oldStack) {

    }

    public boolean isValid(E entry, R resource) {
        return true;
    }

    public int size() {
        return this.enumClass.getEnumConstants().length;
    }

    public S set(E entry, S stack) {
        return Objects.requireNonNullElse(this.stacks.put(entry, stack), this.emptyStack);
    }

    public S get(E entry) {
        return Objects.requireNonNullElse(this.stacks.get(entry), this.emptyStack);
    }

    public void set(int index, R resource, int amount) {
        TransferPreconditions.checkNonNegative(amount);

        if (resource.isEmpty() && amount > 0) {
            throw new IllegalArgumentException("Resource is empty but the amount is positive: " + amount);
        }

        E entry = this.enumClass.getEnumConstants()[index];
        S old = this.stacks.put(entry, getStack(resource, amount));
        onContentsChanged(entry, old);
    }

    public void setAll(EnumMap<E, S> stacks) {
        this.stacks.clear();
        this.stacks.putAll(stacks);
    }

    @Override
    public boolean isValid(int index, R resource) {
        Objects.checkIndex(index, size());
        E entry = this.enumClass.getEnumConstants()[index];
        return isValid(entry, resource);
    }

    @Override
    public R getResource(int index) {
        Objects.checkIndex(index, size());
        E entry = this.enumClass.getEnumConstants()[index];
        return getResource(getOrDefault(entry));
    }

    @Override
    public long getAmountAsLong(int index) {
        Objects.checkIndex(index, size());
        E entry = this.enumClass.getEnumConstants()[index];
        return getAmount(getOrDefault(entry));
    }

    @Override
    public long getCapacityAsLong(int index, R resource) {
        Objects.checkIndex(index, this.size());
        E entry = this.enumClass.getEnumConstants()[index];
        return !resource.isEmpty() && !isValid(entry, resource) ? 0L : (long) getCapacity(entry, resource);
    }

    @Override
    public int insert(int index, R resource, int amount, TransactionContext transaction) {
        Objects.checkIndex(index, this.size());
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount);
        E entry = this.enumClass.getEnumConstants()[index];
        S currentStack = getOrDefault(entry);
        int currentAmount = getAmount(currentStack);

        if ((currentAmount == 0 || matches(currentStack, resource)) && isValid(entry, resource)) {
            int inserted = Math.min(amount, getCapacity(entry, resource) - currentAmount);
            if (inserted > 0) {
                this.snapshotJournals.get(index).updateSnapshots(transaction);
                set(index, resource, currentAmount + inserted);
                return inserted;
            }
        }

        return 0;
    }

    @Override
    public int extract(int index, R resource, int amount, TransactionContext transaction) {
        Objects.checkIndex(index, this.size());
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount);
        E entry = this.enumClass.getEnumConstants()[index];
        S currentStack = getOrDefault(entry);
        if (this.matches(currentStack, resource)) {
            int currentAmount = getAmount(currentStack);
            int extracted = Math.min(amount, currentAmount);
            if (extracted > 0) {
                this.snapshotJournals.get(index).updateSnapshots(transaction);
                set(index, resource, currentAmount - extracted);
                return extracted;
            }
        }

        return 0;
    }

    @Override
    public void serialize(ValueOutput valueOutput) {
        valueOutput.store("stacks", this.codec, this.stacks);
    }

    @Override
    public void deserialize(ValueInput valueInput) {
        valueInput.read("stacks", this.codec).ifPresent(stacks -> {
            this.stacks.clear();
            this.stacks.putAll(stacks);
        });
    }

    @Override
    public void clearContent() {
        this.stacks.replaceAll((k, v) -> this.emptyStack);
    }

    private class ValueJournal extends SnapshotJournal<S> {
        private final E entry;

        private ValueJournal(E entry) {
            this.entry = entry;
        }

        @Override
        protected S createSnapshot() {
            return MappedResourceHandler.this.copyOf(MappedResourceHandler.this.stacks.getOrDefault(this.entry, MappedResourceHandler.this.emptyStack));
        }

        @Override
        protected void revertToSnapshot(S s) {
            MappedResourceHandler.this.stacks.put(this.entry, s);
        }

        @Override
        protected void onRootCommit(S originalState) {
            MappedResourceHandler.this.onContentsChanged(this.entry, originalState);
        }
    }
}
