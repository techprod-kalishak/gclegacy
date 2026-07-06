/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity;

import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
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
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class ParachestBlockEntity extends BaseItemStorageBlockEntity implements LidBlockEntity {
    private final int slotCount;
    private FluidStack tank = FluidStack.EMPTY;
    private @Nullable EntityReference<Player> owner;
    private DyeColor parachuteColor = DyeColor.RED;

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

    public ParachestBlockEntity(BlockPos blockPos, BlockState blockState, int baseChestSlotCount) {
        super(GalacticraftBlockEntityType.PARACHEST.get(), blockPos, blockState);
        this.slotCount = 3 + baseChestSlotCount;
    }

    public ParachestBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(blockPos, blockState, 0);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.Item.BLOCK,
                GalacticraftBlockEntityType.PARACHEST.get(),
                (entity, _) -> entity.items
        );
        event.registerBlockEntity(
                Capabilities.Fluid.BLOCK,
                GalacticraftBlockEntityType.PARACHEST.get(),
                (entity, _) -> new SingleTankResourceHandler(entity.tank, 6000)
        );
    }

    @Override
    public int getItemsSize() {
        return this.slotCount;
    }

    public DyeColor getParachuteColor() {
        return this.parachuteColor;
    }

    @Override
    public void setItem(int index, ItemStack itemStack) {
        if (itemStack.has(GalacticraftDataComponents.FLUID_TANK)) {
            SingleTankResourceHandler thisHandler = new SingleTankResourceHandler(this.tank, 6000);

            ItemResource newResource = ResourcefulHelper.fillTank(thisHandler, fluid -> fluid.is(GalacticraftTags.Fluids.IS_FUEL), itemStack, null);

            if (!ItemStack.isSameItemSameComponents(itemStack, newResource.toStack())) {
                super.setItem(index, itemStack);
            }
        }
    }

    @Override
    protected Component getDefaultName() {
        return GalacticraftComponents.BLOCK_PARACHEST;
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.tank = input.read("FluidStack", FluidStack.OPTIONAL_CODEC).orElse(FluidStack.EMPTY);
        this.owner = EntityReference.read(input, "Owner");
        this.parachuteColor = input.read("TeamColor", DyeColor.CODEC).orElse(DyeColor.RED);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        output.store("FluidStack", FluidStack.OPTIONAL_CODEC, this.tank);

        if (this.owner != null) {
            this.owner.store(output, "Owner");
        }

        if (this.parachuteColor != DyeColor.RED) {
            output.store("TeamColor", DyeColor.CODEC, this.parachuteColor);
        }
    }

    public static void lidAnimateTick(Level level, BlockPos pos, BlockState state, ParachestBlockEntity entity) {
        entity.chestLidController.tickLid();
    }

    static void playSound(Level level, BlockPos pos, SoundEvent sound) {
        level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, sound, SoundSource.BLOCKS, 0.5F, level.getRandom().nextFloat() * 0.1F + 0.9F);
    }

    public void copyItemsFrom(NonNullList<ItemStack> items) {
        for (int i = 0; i < getItemsSize(); i++) {
            setItem(i, items.get(i));
        }
    }

    public void copyItems(NonNullList<ItemStack> items) {
        for (int i = 0; i < getItemsSize(); i++) {
            items.set(i, ItemUtil.getStack(this.items, i));
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

    @Override
    public boolean canOpen(Player player) {
        boolean par = super.canOpen(player);

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
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new ParachestMenu(containerId, inventory, this);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter componentGetter) {
        super.applyImplicitComponents(componentGetter);
        this.owner = componentGetter.get(GalacticraftDataComponents.PLAYER_REFERENCE);
        this.tank = componentGetter.getOrDefault(GalacticraftDataComponents.FLUID_TANK, SimpleFluidContent.EMPTY).copy();
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);

        if (this.owner != null) {
            components.set(GalacticraftDataComponents.PLAYER_REFERENCE, this.owner);
        }

        components.set(GalacticraftDataComponents.FLUID_TANK, SimpleFluidContent.copyOf(this.tank));
    }
}