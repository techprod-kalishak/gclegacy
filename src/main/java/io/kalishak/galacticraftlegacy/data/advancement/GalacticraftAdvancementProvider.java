/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.advancement;

import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.advancements.AdvancementSubProvider;

import java.util.List;

public class GalacticraftAdvancementProvider extends AdvancementProvider {
    private GalacticraftAdvancementProvider(List<AdvancementSubProvider.Factory> subProviders) {
        super(subProviders);
    }

    public static GalacticraftAdvancementProvider create() {
        return new GalacticraftAdvancementProvider(
                List.of(
                        GalacticraftAdvancements::new
                )
        );
    }
}
