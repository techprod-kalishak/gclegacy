package io.kalishak.galacticraftlegacy.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.registry.SchematicVariant;
import io.kalishak.galacticraftlegacy.registry.SchematicVariants;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.EitherHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.function.Consumer;

public record SchematicContent(EitherHolder<SchematicVariant> schematic) implements TooltipProvider {
    public static final MapCodec<SchematicContent> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            EitherHolder.codec(GalacticraftRegistries.Keys.SCHEMATIC, SchematicVariant.CODEC).fieldOf("schematic_variant").forGetter(SchematicContent::schematic)
    ).apply(instance, SchematicContent::new));
    public static final Codec<SchematicContent> CODEC = MAP_CODEC.codec();
    public static final StreamCodec<RegistryFriendlyByteBuf, SchematicContent> STREAM_CODEC = StreamCodec.composite(
            EitherHolder.streamCodec(GalacticraftRegistries.Keys.SCHEMATIC, SchematicVariant.STREAM_CODEC), SchematicContent::schematic,
            SchematicContent::new
    );
    public static final SchematicContent DEFAULT = new SchematicContent(new EitherHolder<>(SchematicVariants.TIER_2_ROCKET));

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag flag, DataComponentGetter componentGetter) {
        HolderLookup.Provider registries = context.registries();

        if (registries != null) {
            this.schematic.unwrap(registries).ifPresent(schematic -> tooltipAdder.accept(schematic.value().title()));
        }
    }
}
