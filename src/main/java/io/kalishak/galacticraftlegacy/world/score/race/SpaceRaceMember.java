/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.score.race;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import org.jspecify.annotations.Nullable;

public interface SpaceRaceMember {
    String WILDCARD_NAME = "*";

    String getScoreboardName();

    default @Nullable Component getDisplayName() {
        return null;
    }

    default Component getFeedbackDisplayName() {
        Component displayName = getDisplayName();
        return displayName != null ? displayName.copy().withStyle(style -> style.withHoverEvent(new HoverEvent.ShowText(Component.literal(getScoreboardName())))) : Component.literal(getScoreboardName());
    }

    static SpaceRaceMember forNameOnly(String name) {
        return new SpaceRaceMember() {
            @Override
            public String getScoreboardName() {
                return name;
            }

            @Override
            public Component getFeedbackDisplayName() {
                return Component.literal(name);
            }
        };
    }

    static SpaceRaceMember forGameProfile(GameProfile profile) {
        return profile::name;
    }
}
