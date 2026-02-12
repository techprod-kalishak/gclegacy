package io.kalishak.galacticraftlegacy.world.item;

import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import net.minecraft.world.item.ToolMaterial;

public class GalacticraftToolMaterials {
    //Iron level
    public static final ToolMaterial DESH = new ToolMaterial(
            GalacticraftTags.Blocks.INCORRECT_FOR_DESH_TOOL,
            1024,
            5.0F,
            2.5F,
            10,
            GalacticraftTags.Items.REPAIRS_DESH_TOOL
    );
    //Iron level
    public static final ToolMaterial STEEL = new ToolMaterial(
            GalacticraftTags.Blocks.INCORRECT_FOR_STEEL_TOOL,
            768,
            5.0F,
            2.0F,
            8,
            GalacticraftTags.Items.REPAIRS_STEEL_TOOL
    );
    //Diamond level
    public static final ToolMaterial TITANIUM = new ToolMaterial(
            GalacticraftTags.Blocks.INCORRECT_FOR_TITANIUM_TOOL,
            760,
            14.0F,
            4.0F,
            16,
            GalacticraftTags.Items.REPAIRS_TITANIUM_TOOL
    );
}
