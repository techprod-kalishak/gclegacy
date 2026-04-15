package io.kalishak.galacticraftlegacy.world.attribute;

import io.kalishak.galacticraftlegacy.sounds.GalacticraftSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.Music;
import net.minecraft.world.attribute.AmbientMoodSettings;
import net.minecraft.world.attribute.AmbientSounds;
import net.minecraft.world.attribute.BackgroundMusic;
import net.minecraft.world.attribute.BedRule;

import java.util.List;
import java.util.Optional;

public class GalacticraftWorldAttributes {
    public static final BackgroundMusic MUSIC_SPACE = new BackgroundMusic(new Music(GalacticraftSounds.MUSIC_SPACE_RACE, 88500, 265500, false));

    public static final BedRule BED_RULE_CRYO_CHAMBER = new BedRule(
            BedRule.Rule.NEVER, BedRule.Rule.NEVER, false, Optional.of(Component.translatable("block.galacticraftlegacy.bed.sleep_in_cryo_chamber"))
    );

    private static final AmbientMoodSettings AMBIENT_MOOD_SPACE = new AmbientMoodSettings(GalacticraftSounds.AMBIENT_SPACE, 12000, 8, 2.0F);
    public static final AmbientSounds AMBIENT_SOUNDS_SPACE = new AmbientSounds(
            Optional.empty(),
            Optional.of(AMBIENT_MOOD_SPACE),
            List.of()
    );
}
