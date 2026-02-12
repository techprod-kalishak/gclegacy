package io.kalishak.galacticraftlegacy.client.renderer.entity.state;

import io.kalishak.galacticraftlegacy.registry.SchematicVariant;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.core.Direction;
import org.jspecify.annotations.Nullable;

public class SchematicRenderState extends EntityRenderState {
    public Direction direction = Direction.NORTH;
    public @Nullable SchematicVariant schematicVariant;
    public int[] lightCoordsPerBlock = new int[0];
}
