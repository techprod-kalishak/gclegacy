package io.kalishak.galacticraftlegacy.world.item;

import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class StructureFinderItem extends Item {
    public StructureFinderItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.has(GalacticraftDataComponents.STRUCTURE_POS) || super.isFoil(stack);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack inHand = player.getItemInHand(hand);
        BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);

        if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            if (level instanceof ServerLevel serverLevel) {
                BlockPos structurePos = serverLevel.findNearestMapStructure(GalacticraftTags.Structures.IS_DUNGEON, player.blockPosition(), 100, false);

                if (structurePos == null) {
                    return InteractionResult.PASS;
                }

                inHand.set(GalacticraftDataComponents.STRUCTURE_POS, new GlobalPos(serverLevel.dimension(), structurePos));
                player.awardStat(Stats.ITEM_USED.get(this));
            }
        }

        return InteractionResult.SUCCESS_SERVER;
    }
}
