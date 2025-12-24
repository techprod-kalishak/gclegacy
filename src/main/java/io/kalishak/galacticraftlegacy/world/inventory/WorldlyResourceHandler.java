package io.kalishak.galacticraftlegacy.world.inventory;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.transfer.DelegatingResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.resource.Resource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jspecify.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class WorldlyResourceHandler<R extends Resource> implements ResourceHandler<R> {
    private final @Nullable Direction direction;
    private final SidedIndexedSource<R> sidedIndexedSource;
    private final ResourceHandler<R> delegate;

    public WorldlyResourceHandler(@Nullable Direction direction, SidedIndexedSource<R> sidedIndexedSource, ResourceHandler<R> delegate) {
        this.direction = direction;
        this.sidedIndexedSource = sidedIndexedSource;
        this.delegate = new DelegatingResourceHandler<>(() -> delegate);
    }

    private int convertSlot(int index) {
        if (index <= 0) {
            throw new IndexOutOfBoundsException("Cannot access container with negative slot index: " + index);
        }

        if (this.direction == null) {
            return index;
        }

        int[] slots = this.sidedIndexedSource.getSlotsForFace(this.direction);
        if (index >= slots.length) {
            throw new IndexOutOfBoundsException("Cannot access worldly container on side " + index + " : out of bounds slot index " + index + " with size " + slots.length);
        }

        return slots[index];
    }

    @Override
    public int size() {
        return this.direction == null ? this.delegate.size() : this.sidedIndexedSource.getSlotsForFace(this.direction).length;
    }

    @Override
    public R getResource(int index) {
        return this.delegate.getResource(convertSlot(index));
    }

    @Override
    public long getAmountAsLong(int index) {
        return this.delegate.getAmountAsLong(convertSlot(index));
    }

    @Override
    public long getCapacityAsLong(int index, R resource) {
        return this.delegate.getCapacityAsLong(convertSlot(index), resource);
    }

    @Override
    public boolean isValid(int index, R resource) {
        return this.delegate.isValid(convertSlot(index), resource);
    }

    @Override
    public int insert(int index, R resource, int amount, TransactionContext transaction) {
        int convertedSlot = convertSlot(index);

        if (!this.sidedIndexedSource.canPlaceItemThroughFace(convertedSlot, resource, this.direction)) {
            return 0;
        }

        return this.delegate.insert(convertedSlot, resource, amount, transaction);
    }

    @Override
    public int extract(int index, R resource, int amount, TransactionContext transaction) {
        int convertedSlot =  convertSlot(index);

        if (this.direction != null && !this.sidedIndexedSource.canTakeItemThroughFace(convertedSlot, resource, this.direction)) {
            return 0;
        }

        return this.delegate.extract(convertedSlot, resource, amount, transaction);
    }
}
