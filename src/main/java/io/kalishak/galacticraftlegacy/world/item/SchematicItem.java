package io.kalishak.galacticraftlegacy.world.item;

import io.kalishak.galacticraftlegacy.world.entity.SchematicEntity;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.item.component.SchematicContent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class SchematicItem extends Item {
    public SchematicItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos clickedPos = context.getClickedPos();
        Direction clickedFace = context.getClickedFace();
        BlockPos relative = clickedPos.relative(clickedFace);
        Player player = context.getPlayer();
        ItemStack itemInHand = context.getItemInHand();

        if (player != null && !mayPlace(player, clickedFace, itemInHand, relative)) {
            return InteractionResult.FAIL;
        }

        Level level = context.getLevel();
        SchematicContent schematicHolder = itemInHand.getOrDefault(GalacticraftDataComponents.SCHEMATIC, SchematicContent.DEFAULT);

        SchematicEntity schematicEntity = new SchematicEntity(level, clickedPos, clickedFace, schematicHolder);

        EntityType.createDefaultStackConfig(level, itemInHand, player).accept(schematicEntity);

        if (schematicEntity.survives()) {
            if (!level.isClientSide()) {
                schematicEntity.playPlacementSound();
                level.addFreshEntity(schematicEntity);
                level.gameEvent(player, GameEvent.ENTITY_PLACE, schematicEntity.position());
            }

            itemInHand.shrink(1);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.CONSUME;
        }
    }

    private boolean mayPlace(Player player, Direction direction, ItemStack schematicStack, BlockPos pos) {
        return !direction.getAxis().isVertical() && player.mayUseItemAt(pos, direction, schematicStack);
    }
}
