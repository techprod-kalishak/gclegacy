package io.kalishak.galacticraftlegacy.data.tag;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.world.timeline.GalacticraftTimelines;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.KeyTagProvider;
import net.minecraft.tags.TimelineTags;
import net.minecraft.world.timeline.Timeline;

import java.util.concurrent.CompletableFuture;

public class GalacticraftTimelinesTagsProvider extends KeyTagProvider<Timeline> {
    public GalacticraftTimelinesTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, Registries.TIMELINE, registries, Galacticraft.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(GalacticraftTags.Timelines.IN_ORBIT)
                .addTag(TimelineTags.IN_OVERWORLD);
        tag(GalacticraftTags.Timelines.IN_MOON)
                .add(GalacticraftTimelines.MOON_DAY)
                .add(GalacticraftTimelines.EARTH);
        tag(GalacticraftTags.Timelines.IN_MARS)
                .add(GalacticraftTimelines.MARS_DAY);
        tag(GalacticraftTags.Timelines.IN_ASTEROIDS)
                .addTag(TimelineTags.UNIVERSAL);
        tag(GalacticraftTags.Timelines.IN_VENUS)
                .addTag(TimelineTags.UNIVERSAL);
    }
}
