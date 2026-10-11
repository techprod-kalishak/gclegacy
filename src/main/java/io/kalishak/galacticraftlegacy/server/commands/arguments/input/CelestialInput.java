package io.kalishak.galacticraftlegacy.server.commands.arguments.input;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import io.kalishak.galacticraftlegacy.galaxies.celestial.Celestial;
import io.kalishak.galacticraftlegacy.galaxies.celestial.CelestialBodyInstance;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.PatchedDataComponentMap;

public record CelestialInput(Holder<Celestial> celestial, DataComponentPatch components) {
    public static final DynamicCommandExceptionType ERROR_MALFORMED = new DynamicCommandExceptionType(GalacticraftComponents.COMMAND_ERROR_CELESTIAL_MALFORMED);
    private static final DynamicCommandExceptionType ERROR_NAME_BLANK = new DynamicCommandExceptionType(GalacticraftComponents.COMMAND_ERROR_CELESTIAL_BLANK);

    public CelestialBodyInstance create() throws CommandSyntaxException {
        CelestialBodyInstance instance = new CelestialBodyInstance(this.celestial, PatchedDataComponentMap.fromPatch(this.celestial.components(), this.components));

        if (instance.isEmpty()) {
            throw ERROR_MALFORMED.create(this.celestial.getRegisteredName());
        }

        if (instance.getDisplayName().getString().isEmpty()) {
            throw ERROR_NAME_BLANK.create(this.celestial.getRegisteredName());
        }

        return instance;
    }
}
