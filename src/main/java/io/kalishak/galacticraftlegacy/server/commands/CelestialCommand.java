package io.kalishak.galacticraftlegacy.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import io.kalishak.galacticraftlegacy.galaxies.celestial.Celestial;
import io.kalishak.galacticraftlegacy.galaxies.celestial.CelestialBodyInstance;
import io.kalishak.galacticraftlegacy.galaxies.celestial.WorldCelestials;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.server.commands.arguments.DataComponentTypeArgument;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;

public class CelestialCommand {
    private static final DynamicCommandExceptionType ERROR_EMPTY_CELESTIAL = new DynamicCommandExceptionType(obj -> Component.literal("CelestialBodyInstance" + obj + " does not exist"));
    private static final DynamicCommandExceptionType ERROR_INVALID_COMPONENT = new DynamicCommandExceptionType(obj -> Component.literal("Component" + obj + " does not exist"));
    private static final Dynamic2CommandExceptionType ERROR_EMPTY_COMPONENT = new Dynamic2CommandExceptionType((arg, qarg) -> Component.literal(arg + "does not have " + qarg + " component"));

    static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register(
                Commands.literal("celestial")
                        .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .then(
                                Commands.argument("body", ResourceArgument.resource(context, GalacticraftRegistries.Keys.CELESTIAL))
                                        .executes(
                                                c -> printCelestialBody(c.getSource(), ResourceArgument.getResource(c, "body", GalacticraftRegistries.Keys.CELESTIAL))
                                        )
                                        .then(
                                                Commands.argument("component", DataComponentTypeArgument.dataComponentType())
                                                        .executes(
                                                                c -> printCelestialBody(
                                                                        c.getSource(),
                                                                        ResourceArgument.getResource(c, "body", GalacticraftRegistries.Keys.CELESTIAL),
                                                                        DataComponentTypeArgument.getDataComponentType(c, "component")
                                                                )
                                                        )
                                        )
                        )
        );
    }

    private static int printCelestialBody(CommandSourceStack source, Holder<Celestial> celestial) throws CommandSyntaxException {
        MinecraftServer server = source.getServer();
        WorldCelestials worldCelestials = WorldCelestials.get(server);
        CelestialBodyInstance instance = worldCelestials.getOrDefault(celestial);

        if (instance.isEmpty()) {
            throw ERROR_EMPTY_CELESTIAL.create(celestial.getRegisteredName());
        }

        int finalValue = 1;
        Component customName = instance.getDisplayName();
        MutableComponent component = customName.copy().append(":\n");

        for (var dataComponent : instance.getComponents()) {
            Object value = instance.get(dataComponent.type());

            if (value != null) {
                finalValue++;
                component = component.append(dataComponent + "=" + value);
            }
        }

        source.sendSuccess(component::copy, false);

        return finalValue;
    }

    private static <T> int printCelestialBody(CommandSourceStack source, Holder<Celestial> celestial, DataComponentType<T> component) throws CommandSyntaxException {
        MinecraftServer server = source.getServer();
        WorldCelestials worldCelestials = WorldCelestials.get(server);
        CelestialBodyInstance instance = worldCelestials.getOrDefault(celestial);

        if (instance.isEmpty()) {
            throw ERROR_EMPTY_CELESTIAL.create(celestial.getRegisteredName());
        }

        T value = instance.get(component);

        if (value == null) {
            throw ERROR_EMPTY_COMPONENT.create(instance.getDisplayName(), component);
        }

        source.sendSuccess(() -> instance.getDisplayName().copy().append(Component.literal(":\n" + component + "=" + value)), false);

        return 1;
    }
}
