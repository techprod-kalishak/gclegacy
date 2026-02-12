package io.kalishak.galacticraftlegacy.client.data.models.model;

import io.kalishak.galacticraftlegacy.Galacticraft;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

public class GalacticraftTextureMapping {
    public static final Identifier MACHINE = Identifier.fromNamespaceAndPath(Galacticraft.MODID, "block/machine");

    public static TextureMapping simpleMachine(Identifier base) {
        return new TextureMapping()
                .put(TextureSlot.NORTH, base.withSuffix("_front"))
                .put(TextureSlot.SOUTH, MACHINE.withSuffix("_side"))
                .put(TextureSlot.EAST, MACHINE.withSuffix("_energy_output"))
                .put(TextureSlot.WEST, MACHINE.withSuffix("_side"))
                .put(TextureSlot.UP, MACHINE.withSuffix("_top"))
                .put(TextureSlot.DOWN, MACHINE.withSuffix("_top"))
                .put(TextureSlot.PARTICLE, MACHINE.withSuffix("_top"));
    }

    public static TextureMapping simpleMachine(Block block) {
        Identifier id = TextureMapping.getBlockTexture(block);
        return simpleMachine(id);
    }
}
