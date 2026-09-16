package io.kalishak.galacticraftlegacy.world.level.block.entity;

import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Util;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.item.ItemStackResourceHandler;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.function.BiConsumer;

public class EmergencyPostBlockEntity extends BlockEntity implements LidBlockEntity {
    private final SingleItemHandler singleItemHandler = new SingleItemHandler();
    private final PostLidController lidController = new PostLidController();
    private boolean isAnySideOpen;

    public EmergencyPostBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(GalacticraftBlockEntityType.EMERGENCY_POST.get(), worldPosition, blockState);
    }

    public static void clientTick(Level level, BlockPos blockPos, BlockState blockState, EmergencyPostBlockEntity emergencyPost) {
        emergencyPost.lidController.tick();
    }

    public static void registerCapability(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.Item.BLOCK,
                GalacticraftBlockEntityType.EMERGENCY_POST.get(),
                (entity, _) -> entity.singleItemHandler
        );
    }

    @Override
    public float getOpenNess(float partialTicks) {
        return this.lidController.getOpenness(partialTicks, Direction.NORTH);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        this.singleItemHandler.serialize(output);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.singleItemHandler.deserialize(input);
    }

    public void toggleOpen(Direction side) {
        this.isAnySideOpen = !this.isAnySideOpen;
        this.lidController.shouldBeOpen(this.isAnySideOpen, side);
    }

    public boolean isOpened() {
        return this.isAnySideOpen;
    }

    public boolean putEmergencyKit(ItemStack emergencyKit, @Nullable Direction side) {
        if (!this.isAnySideOpen) {
            return false;
        }

        if (!this.singleItemHandler.stack.isEmpty()) {
            return false;
        }

        this.singleItemHandler.setStack(emergencyKit);
        emergencyKit.shrink(1);

        return true;
    }

    public boolean consumeKit(Player player) {
        if (this.singleItemHandler.stack.isEmpty()) {
            return false;
        }

        player.addItem(this.singleItemHandler.getStack());
        return true;
    }

    public void extractSides(BiConsumer<Direction, Float> updater, float partialTicks) {
        this.lidController.controllerForSide.forEach((side, controller) -> updater.accept(side, controller.getOpenness(partialTicks)));
    }

    public static class PostLidController {
        private final Map<Direction, ChestLidController> controllerForSide = Util.make(Maps.newEnumMap(Direction.class), map -> {
            map.put(Direction.NORTH, new ChestLidController());
            map.put(Direction.EAST, new ChestLidController());
            map.put(Direction.SOUTH, new ChestLidController());
            map.put(Direction.WEST, new ChestLidController());
        });

        public void tick() {
            this.controllerForSide.values().forEach(ChestLidController::tickLid);
        }

        public float getOpenness(float a, @Nullable Direction side) {
            if (side == null || side.getAxis().isVertical()) {
                return 0.0F;
            }

            return this.controllerForSide.get(side).getOpenness(a);
        }

        public void shouldBeOpen(boolean shouldBeOpen, @Nullable Direction side) {
            if (side != null && side.getAxis().isHorizontal()) {
                this.controllerForSide.get(side).shouldBeOpen(shouldBeOpen);
            }
        }
    }

    private class SingleItemHandler extends ItemStackResourceHandler {
        private @NonNull ItemStack stack = ItemStack.EMPTY;

        @Override
        protected ItemStack getStack() {
            return this.stack;
        }

        @Override
        protected void setStack(ItemStack stack) {
            this.stack = stack.copy();
        }

        @Override
        protected void onRootCommit(ItemStack originalState) {
            super.onRootCommit(originalState);
            EmergencyPostBlockEntity.this.setChanged();
        }
    }
}
