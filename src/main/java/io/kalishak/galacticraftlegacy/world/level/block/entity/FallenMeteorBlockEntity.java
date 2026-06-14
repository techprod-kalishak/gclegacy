package io.kalishak.galacticraftlegacy.world.level.block.entity;

import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.item.component.HotContent;
import io.kalishak.galacticraftlegacy.world.level.block.FallenMeteorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;

public class FallenMeteorBlockEntity extends BlockEntity {
    public FallenMeteorBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(GalacticraftBlockEntityType.FALLEN_METEOR.get(), worldPosition, blockState);
    }

    public static void serverTick(ServerLevel level, BlockPos worldPosition, BlockState blockState, FallenMeteorBlockEntity meteor) {
        HotContent content = meteor.getData(GalacticraftAttachments.HOT_CONTENT);

        if (content.decrease(null)) {
            if (blockState.getValue(FallenMeteorBlock.WATERLOGGED)) {
                FluidState state = level.getFluidState(worldPosition);

                if (state.is(FluidTags.WATER)) {
                    int heatLevel = content.getHeatLevel();

                    if (content.getHeatLevel() > 1000) {
                        int newHeat = Math.max(0, heatLevel - 1000);

                        content.setHeatLevel(newHeat);
                        meteor.setData(GalacticraftAttachments.HOT_CONTENT, content);

                        blockState = blockState.setValue(FallenMeteorBlock.WATERLOGGED, false);
                        level.gameEvent(GameEvent.BLOCK_CHANGE, worldPosition, GameEvent.Context.of(blockState));
                        level.playSound(null, worldPosition, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
                    }
                }
            }
        }
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);

        int heatLevel = getData(GalacticraftAttachments.HOT_CONTENT).getHeatLevel();

        if (heatLevel > 0) {
            components.set(GalacticraftDataComponents.HOT_CONTENT, new HotContent(heatLevel));
        }
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        super.applyImplicitComponents(components);

        HotContent hotContent = components.get(GalacticraftDataComponents.HOT_CONTENT);

        if (hotContent != null) {
            setData(GalacticraftAttachments.HOT_CONTENT, hotContent);
        }
    }

    public float getScaledHeatLevel() {
        return getData(GalacticraftAttachments.HOT_CONTENT).getHeatLevel() / 5000.0F;
    }
}
