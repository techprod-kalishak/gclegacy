/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import org.jspecify.annotations.Nullable;

public interface SoundboundEntity {
    @Nullable TickableSoundInstance getSoundUpdater();

    @Nullable Sound setSoundUpdater(LocalPlayer localPlayer);
}
