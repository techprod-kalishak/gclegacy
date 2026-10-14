package io.kalishak.galacticraftlegacy.components.flag;

import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import net.minecraft.ChatFormatting;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;

public class BuiltinFlagMaterials {
    public static final ResourceKey<FlagMaterial> CLASSIC = key("classic");
    public static final ResourceKey<FlagMaterial> DEFAULT_MUTABLE = key("default_mutable");
    public static final ResourceKey<FlagMaterial> NASA = key("nasa");
    public static final ResourceKey<FlagMaterial> EARTH = key("earth");

    public static void bootstrap(BootstrapContext<FlagMaterial> context) {
        context.register(
                CLASSIC,
                new FlagMaterial(
                        new BlankFlagProvider(
                                48,
                                32,
                                127
                        ),
                        Component.literal("Galacticraft").withStyle(ChatFormatting.DARK_AQUA)
                )
        );
        context.register(
                DEFAULT_MUTABLE,
                new FlagMaterial(
                        new MutableFlagTypeProvider(
                                48,
                                32
                        ),
                        Component.empty()
                )
        );
        context.register(
                NASA,
                new FlagMaterial(
                        new TexturedFlagProvider(
                                Constants.texture("entity/flag/material/nasa"),
                                48,
                                32
                        ),
                        Component.literal("NASA").withStyle(ChatFormatting.YELLOW)
                )
        );
        context.register(
                EARTH,
                new FlagMaterial(
                        new TexturedFlagProvider(
                                Constants.texture("entity/flag/material/earth"),
                                48,
                                32
                        ),
                        Component.translatable(Util.makeDescriptionId("flag_material", EARTH.identifier()))
                )
        );
    }

    private static ResourceKey<FlagMaterial> key(String name) {
        return Constants.key(GalacticraftRegistries.Keys.FLAG_MATERIAL, name);
    }
}
