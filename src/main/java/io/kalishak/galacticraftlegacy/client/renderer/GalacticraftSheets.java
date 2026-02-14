package io.kalishak.galacticraftlegacy.client.renderer;

import com.google.common.collect.ImmutableList;
import io.kalishak.galacticraftlegacy.Constants;
import net.minecraft.client.renderer.MaterialMapper;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class GalacticraftSheets {
    public static final Identifier PARACHUTE_SHEET = Constants.id("textures/atlas/parachutes.png");
    public static final Identifier SCHEMATIC_SHEET = Constants.id("textures/atlas/schematics.png");
    public static final Material PARACHEST = Sheets.CHEST_MAPPER.apply(Constants.id("parachest"));
    public static final MaterialMapper PARACHUTE_MAPPER = new MaterialMapper(PARACHUTE_SHEET, "entity/equipment/galacticraftlegacy/parachute");
    public static final List<Material> PARACHUTE_TEXTURE_LOCATION = Arrays.stream(DyeColor.values())
            .sorted(Comparator.comparingInt(DyeColor::getId))
            .map(GalacticraftSheets::createParachuteMaterial)
            .collect(ImmutableList.toImmutableList());

    public static Material getParachuteMaterial(DyeColor color) {
        return PARACHUTE_TEXTURE_LOCATION.get(color.getId());
    }

    public static Material createParachuteMaterial(DyeColor color) {
        return PARACHUTE_MAPPER.apply(Constants.id( color.getName()));
    }
}
