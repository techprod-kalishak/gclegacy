package io.kalishak.galacticraftlegacy.world.level.block.entity;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import org.jspecify.annotations.Nullable;

public interface SoundboundEntity {
    @Nullable TickableSoundInstance getSoundUpdater();

    @Nullable Sound setSoundUpdater(LocalPlayer localPlayer);
}
