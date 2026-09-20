package io.kalishak.galacticraftlegacy.client.renderer.blockentity.state;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;

import java.util.Map;

public class EmergencyPostRenderState extends BlockEntityRenderState {
    public final Map<Direction, Float> opennessPerSide = Util.makeEnumMap(Direction.class, _ -> 0.0F);
    public float height;
    public boolean hasKit;

    public void updateOpenness(Direction side, float value) {
        if (side != null && side.getAxis().isHorizontal()) {
            this.opennessPerSide.put(side, value);
        }
    }

    public float getOpenness(@Nullable Direction side) {
        return side == null ? 0.0F : this.opennessPerSide.get(side);
    }
}
