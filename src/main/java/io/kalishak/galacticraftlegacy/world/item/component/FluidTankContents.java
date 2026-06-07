/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidStackTemplate;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;

import java.util.*;
import java.util.function.Consumer;

public final class FluidTankContents implements TooltipProvider {
    public static final FluidTankContents EMPTY = new FluidTankContents(List.of());
    public static final Codec<FluidTankContents> CODEC = Slot.CODEC
            .sizeLimitedListOf(256)
            .xmap(FluidTankContents::fromSlots, FluidTankContents::asSlots);
    public static final StreamCodec<RegistryFriendlyByteBuf, FluidTankContents> STREAM_CODEC = FluidStackTemplate.STREAM_CODEC
            .apply(ByteBufCodecs::optional)
            .apply(ByteBufCodecs.list(256))
            .map(FluidTankContents::new, contents -> contents.fluids);

    private final List<Optional<FluidStackTemplate>> fluids;
    private final int hashCode;

    private FluidTankContents(List<Optional<FluidStackTemplate>> fluids) {
        if (fluids.size() > 256) {
            throw new IllegalArgumentException("Got " + fluids.size() + " fluids, but maximum is 256");
        } else {
            this.fluids = fluids;
            this.hashCode = fluids.hashCode();
        }
    }

    private static FluidTankContents fromSlots(List<Slot> slots) {
        OptionalInt maxSlotIndex = slots.stream().mapToInt(Slot::index).max();

        if (maxSlotIndex.isEmpty()) {
            return EMPTY;
        }

        List<Optional<FluidStackTemplate>> fluids = emptyContents(maxSlotIndex.getAsInt() + 1);

        for (Slot slot : slots) {
            fluids.set(slot.index(), Optional.of(slot.fluid()));
        }

        return new FluidTankContents(fluids);
    }

    private static List<Optional<FluidStackTemplate>> emptyContents(int size) {
        return new ArrayList<>(Collections.nCopies(size, Optional.empty()));
    }

    public static FluidTankContents fromFluids(List<FluidStack> fluidStacks) {
        int lastNonEmptySlot = findLastNonEmptySlot(fluidStacks);

        if (lastNonEmptySlot == -1) {
            return EMPTY;
        }

        List<Optional<FluidStackTemplate>> items = emptyContents(lastNonEmptySlot + 1);

        for (int i = 0; i <= lastNonEmptySlot; i++) {
            FluidStack sourceStack = fluidStacks.get(i);

            if (!sourceStack.isEmpty()) {
                items.set(i, Optional.of(FluidStackTemplate.fromNonEmptyStack(sourceStack)));
            }
        }

        return new FluidTankContents(items);
    }

    public static FluidTankContents fromHandler(ResourceHandler<FluidResource> handler) {
        int lastNonEmptySlot = findLastNonEmptySlot(handler);

        if (lastNonEmptySlot == -1) {
            return EMPTY;
        }

        List<Optional<FluidStackTemplate>> items = emptyContents(lastNonEmptySlot + 1);

        for (int i = 0; i <= lastNonEmptySlot; i++) {
            FluidStack sourceStack = FluidUtil.getStack(handler, i);

            if (!sourceStack.isEmpty()) {
                items.set(i, Optional.of(FluidStackTemplate.fromNonEmptyStack(sourceStack)));
            }
        }

        return new FluidTankContents(items);
    }

    private static int findLastNonEmptySlot(List<FluidStack> fluidStacks) {
        for (int i = fluidStacks.size() - 1; i >= 0; i--) {
            if (!fluidStacks.get(i).isEmpty()) {
                return i;
            }
        }

        return -1;
    }

    private static int findLastNonEmptySlot(ResourceHandler<FluidResource> handler) {
        for (int i = handler.size() - 1; i >= 0; i--) {
            if (!handler.getResource(i).isEmpty()) {
                return i;
            }
        }

        return -1;
    }

    private List<Slot> asSlots() {
        List<Slot> slots = new ArrayList<>();

        for (int i = 0; i < this.fluids.size(); i++) {
            Optional<FluidStackTemplate> fluid = this.fluids.get(i);
            if (fluid.isPresent()) {
                slots.add(new Slot(i, fluid.get()));
            }
        }

        return slots;
    }

    public void copyInto(NonNullList<FluidStack> destination) {
        for (int i = 0; i < destination.size(); i++) {
            destination.set(i, createStackFromSlot(i));
        }
    }

    public FluidStack createStackFromSlot(int slot) {
        if (slot < this.fluids.size()) {
            Optional<FluidStackTemplate> slotContents = this.fluids.get(slot);

            if (slotContents.isPresent()) {
                return slotContents.get().create();
            }
        }

        return FluidStack.EMPTY;
    }

    public int getSlots() {
        return this.fluids.size();
    }

    public FluidStack copyFirst() {
        return createStackFromSlot(0);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof FluidTankContents other && this.fluids.equals(other.fluids));
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
        int lineCount = 0;
        int count = 0;

        for (Optional<FluidStackTemplate> item : this.fluids) {
            if (item.isPresent()) {
                count++;

                if (lineCount <= 4) {
                    lineCount++;
                    FluidStack fluidStack = item.get().create();
                    consumer.accept(Component.translatable("item.galacticraftlegacy.tank.fluid_amount", fluidStack.getHoverName(), fluidStack.getAmount()));
                }
            }
        }

        if (count - lineCount > 0) {
            consumer.accept(Component.translatable("item.galacticraftlegacy.tank.more_fluids", count - lineCount).withStyle(ChatFormatting.ITALIC));
        }
    }

    private record Slot(int index, FluidStackTemplate fluid) {
        public static final Codec<Slot> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.intRange(0, 255).fieldOf("slot").forGetter(Slot::index),
                FluidStackTemplate.CODEC.fieldOf("fluid").forGetter(Slot::fluid)
        ).apply(instance, Slot::new));
    }
}
