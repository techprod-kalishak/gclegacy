package io.kalishak.galacticraftlegacy.data.tag;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;

public class GalacticraftEntityTypeTagsProvider extends EntityTypeTagsProvider {
    public GalacticraftEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, Galacticraft.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(GalacticraftTags.EntityTypes.CAN_EQUIP_PARACHUTE)
                .add(EntityType.PLAYER)
                .add(EntityType.PIG)
                .add(EntityType.WOLF);
        tag(GalacticraftTags.EntityTypes.CAN_EQUIP_GEAR)
                .add(EntityType.CREEPER)
                .add(EntityType.PLAYER)
                .add(EntityType.SKELETON)
                .add(EntityType.WOLF)
                .add(EntityType.ZOMBIE);
    }
}
