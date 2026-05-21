/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity;

import io.kalishak.galacticraftlegacy.world.score.race.SpaceRaceHooks;
import io.kalishak.galacticraftlegacy.world.score.race.SpaceRaceScoreboard;
import io.kalishak.galacticraftlegacy.world.score.race.SpaceRaceTeam;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.transfer.ResourcefulHelper;
import io.kalishak.galacticraftlegacy.transfer.capability.fluid.SingleTankResourceHandler;
import io.kalishak.galacticraftlegacy.world.inventory.ParachestMenu;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.level.block.ParachestBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.LockCode;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class ParachestBlockEntity extends NamedBlockEntity implements LidBlockEntity {
    private FluidStack tank = FluidStack.EMPTY;
    private LockCode lockKey = LockCode.NO_LOCK;
    private @Nullable EntityReference<Player> owner;
    private final NonNullList<ItemStack> inventory;
    private DyeColor parachuteColor = DyeColor.RED;

    private final ItemStacksResourceHandler itemResources;
    private final SingleTankResourceHandler fluidResource = new SingleTankResourceHandler() {
        @Override
        public FluidStack getFluidStack() {
            return ParachestBlockEntity.this.tank;
        }

        @Override
        public void setFluidStack(FluidStack stack) {
            ParachestBlockEntity.this.tank = stack;
        }

        @Override
        protected int getCapacity(FluidResource resource) {
            return resource.is(GalacticraftTags.Fluids.IS_FUEL) ? 8 * FluidType.BUCKET_VOLUME : 0;
        }

        @Override
        protected void notifyChange() {
            ParachestBlockEntity.this.setChanged();
        }
    };
    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        @Override
        protected void onOpen(Level level, BlockPos blockPos, BlockState blockState) {
            if (blockState.getBlock() instanceof ParachestBlock) {
                playSound(level, blockPos, SoundEvents.CHEST_OPEN);
            }
        }

        @Override
        protected void onClose(Level p_155367_, BlockPos p_155368_, BlockState p_155369_) {
            if (p_155369_.getBlock() instanceof ParachestBlock) {
                playSound(p_155367_, p_155368_, SoundEvents.CHEST_CLOSE);
            }
        }

        @Override
        protected void openerCountChanged(Level level, BlockPos blockPos, BlockState blockState, int p_155364_, int p_155365_) {
            ParachestBlockEntity.this.signalOpenCount(level, blockPos, blockState, p_155364_, p_155365_);
        }

        @Override
        public boolean isOwnContainer(Player player) {
            return (player.containerMenu instanceof ParachestMenu);
        }
    };
    private final ChestLidController chestLidController = new ChestLidController();

    public ParachestBlockEntity(BlockPos blockPos, BlockState blockState, int chestSlots) {
        super(GalacticraftBlockEntityType.PARACHEST.get(), blockPos, blockState);
        this.inventory = NonNullList.withSize(3 + chestSlots, ItemStack.EMPTY);
        this.itemResources = new ItemStacksResourceHandler(this.inventory) {
            @Override
            protected void onContentsChanged(int index, ItemStack previousContents) {
                ParachestBlockEntity.this.setChanged();
            }
        };
    }

    public ParachestBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(blockPos, blockState, 0);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.Item.BLOCK,
                GalacticraftBlockEntityType.PARACHEST.get(),
                (block, cxt) -> block.itemResources
        );
        event.registerBlockEntity(
                Capabilities.Fluid.BLOCK,
                GalacticraftBlockEntityType.PARACHEST.get(),
                (block, cxt) -> block.fluidResource
        );
    }

    public int size() {
        return this.inventory.size();
    }

    public DyeColor getParachuteColor() {
        return this.parachuteColor;
    }

    public void setTankItem(int index, ItemResource itemResource, int amount) {
        ItemResource newResource = ResourcefulHelper.fillTank(this.fluidResource, fluid -> fluid.is(GalacticraftTags.Fluids.IS_FUEL), itemResource.toStack(), null);

        if (!ResourcefulHelper.areResourcesEqual(newResource, itemResource, ItemResource::toStack, ItemStack::isSameItemSameComponents)) {
            this.itemResources.set(index, itemResource, amount);
        }
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.parachest");
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.lockKey = LockCode.fromTag(input);
        this.owner = EntityReference.read(input, "Owner");
        this.itemResources.deserialize(input);
        this.fluidResource.deserialize(input);
        this.parachuteColor = input.read("TeamColor", DyeColor.CODEC).orElse(DyeColor.RED);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        this.lockKey.addToTag(output);
        this.owner.store(output, "Owner");
        this.itemResources.serialize(output);
        this.fluidResource.serialize(output);

        if (this.parachuteColor != DyeColor.RED) {
            output.store("TeamColor", DyeColor.CODEC, this.parachuteColor);
        }
    }

    public static void lidAnimateTick(Level level, BlockPos pos, BlockState state, ParachestBlockEntity blockEntity) {
        blockEntity.chestLidController.tickLid();
    }

    static void playSound(Level level, BlockPos pos, SoundEvent sound) {
        level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, sound, SoundSource.BLOCKS, 0.5F, level.getRandom().nextFloat() * 0.1F + 0.9F);
    }

    public void copyItemsFrom(NonNullList<ItemStack> items) {
        for (int i = 0; i < this.inventory.size(); i++) {
            this.inventory.set(i, items.get(i).copy());
        }
    }

    public void copyItems(NonNullList<ItemStack> items) {
        for (int i = 0; i < this.inventory.size(); i++) {
            items.set(i, this.inventory.get(i).copy());
        }
    }

    public void copyTankFrom(FluidStack tank) {
        this.tank = tank.copy();
    }

    public FluidStack copyTank() {
        return this.tank.copy();
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        if (id == 1) {
            this.chestLidController.shouldBeOpen(type > 0);
            return true;
        }

        return super.triggerEvent(id, type);
    }

    public boolean canOpen(Player player) {
        boolean par = this.lockKey.canUnlock(player);

        if (this.owner == null || this.owner.getUUID().equals(player.getUUID())) {
            return par;
        }

        if (this.level instanceof ServerLevel serverLevel) {
            SpaceRaceScoreboard spaceRaceScoreboard = SpaceRaceHooks.getFromLevel(serverLevel);
            Player owner = this.owner.getEntity(serverLevel, Player.class);

            if (owner != null) {
                SpaceRaceTeam ownerTeam = spaceRaceScoreboard.getSpaceRaceTeam(owner.getScoreboardName());

                return ownerTeam != null && ownerTeam.getPlayers().contains(player.getScoreboardName());
            }
        }

        return par;
    }

    public void startOpen(ContainerUser containerUser) {
        if (!this.remove && !containerUser.getLivingEntity().isSpectator()) {
            this.openersCounter
                    .incrementOpeners(
                            containerUser.getLivingEntity(), getLevel(), getBlockPos(), getBlockState(), containerUser.getContainerInteractionRange()
                    );
        }
    }

    public void stopOpen(ContainerUser containerUser) {
        if (!this.remove && !containerUser.getLivingEntity().isSpectator()) {
            this.openersCounter.decrementOpeners(containerUser.getLivingEntity(), getLevel(), getBlockPos(), getBlockState());
        }
    }

    public List<ContainerUser> getEntitiesWithContainerOpen() {
        return this.openersCounter.getEntitiesWithContainerOpen(getLevel(), getBlockPos());
    }

    @Override
    public float getOpenNess(float partialTicks) {
        return this.chestLidController.getOpenness(partialTicks);
    }

    public boolean isLockedWithKey() {
        return !this.lockKey.equals(LockCode.NO_LOCK);
    }

    public void recheckOpen() {
        if (!this.remove) {
            this.openersCounter.recheckOpeners(getLevel(), getBlockPos(), getBlockState());
        }
    }

    protected void signalOpenCount(Level level, BlockPos pos, BlockState state, int eventId, int eventParam) {
        Block block = state.getBlock();
        level.blockEvent(pos, block, 1, eventParam);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        if (canOpen(player)) {
            return new ParachestMenu(containerId, playerInventory, this);
        }

        BaseContainerBlockEntity.sendChestLockedNotifications(getBlockPos().getCenter(), player, getDisplayName());
        return null;
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter componentGetter) {
        super.applyImplicitComponents(componentGetter);
        this.lockKey = componentGetter.getOrDefault(DataComponents.LOCK, LockCode.NO_LOCK);
        this.owner = componentGetter.get(GalacticraftDataComponents.ENTITY_REFERENCE);
        componentGetter.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(this.inventory);
        this.tank = componentGetter.getOrDefault(GalacticraftDataComponents.FLUID_TANK, SimpleFluidContent.EMPTY).copy();
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        if (isLockedWithKey()) {
            components.set(DataComponents.LOCK, this.lockKey);
        }

        if (this.owner != null) {
            components.set(GalacticraftDataComponents.ENTITY_REFERENCE, this.owner);
        }

        components.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(this.inventory));
        components.set(GalacticraftDataComponents.FLUID_TANK, SimpleFluidContent.copyOf(this.tank));
    }

    @Override
    public void removeComponentsFromTag(ValueOutput output) {
        super.removeComponentsFromTag(output);
        output.discard("lock");
        output.discard(ItemStacksResourceHandler.VALUE_IO_KEY);
        output.discard(SingleTankResourceHandler.VALUE_IO_KEY);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);

        if (this.level != null) {
            for (int i = 0; i < size(); i++) {
                Containers.dropItemStack(this.level, pos.getX(), pos.getY(), pos.getZ(), ItemUtil.getStack(this.itemResources, i));
            }
        }
    }
}