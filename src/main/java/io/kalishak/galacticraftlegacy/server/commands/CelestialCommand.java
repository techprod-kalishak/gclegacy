package io.kalishak.galacticraftlegacy.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import io.kalishak.galacticraftlegacy.galaxies.celestial.CelestialBodyInstance;
import io.kalishak.galacticraftlegacy.galaxies.celestial.WorldCelestials;
import io.kalishak.galacticraftlegacy.server.commands.arguments.CelestialArgument;
import io.kalishak.galacticraftlegacy.server.commands.arguments.input.CelestialInput;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class CelestialCommand {
    private static final DynamicCommandExceptionType ERROR_INVALID_COMPONENT = new DynamicCommandExceptionType(obj -> Component.literal("Component" + obj + " does not exist"));
    private static final Dynamic2CommandExceptionType ERROR_EMPTY_COMPONENT = new Dynamic2CommandExceptionType((arg, qarg) -> Component.literal(qarg + "does not have " + arg + " component"));

    static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register(
                Commands.literal("celestial")
                        .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .then(
                                Commands.literal("get")
                                        .then(
                                                Commands.argument("instance", CelestialArgument.celestial(context))
                                                        .executes(c -> printCelestialInstance(c.getSource(), CelestialArgument.getCelestial(c, "instance")))
                                                        .then(
                                                                Commands.argument("component", IdentifierArgument.id())
                                                                        .executes(c -> printCelestialComponent(c.getSource(), CelestialArgument.getCelestial(c, "instance"), IdentifierArgument.getId(c, "component")))
                                                        )
                                        )

                        )
                        .then(
                                Commands.literal("modify")
                                        .then(
                                                Commands.argument("instance", CelestialArgument.celestial(context))
                                                        .then(Commands.argument("component", IdentifierArgument.id()))
                                                        .executes(c -> modifyComponent(c.getSource(), CelestialArgument.getCelestial(c, "instance"), IdentifierArgument.getId(c, "component")))
                                        )
                        )
                        .then(
                                Commands.literal("debug")
                                        .then(
                                                Commands.argument("instance", CelestialArgument.celestial(context))
                                                        .then(Commands.argument("player", EntityArgument.player())
                                                        .executes(c -> fill(c.getSource(), CelestialArgument.getCelestial(c, "instance"), EntityArgument.getPlayer(c, "player")))
                                        )
                        )
        ));
    }

    private static int printCelestialInstance(CommandSourceStack source, CelestialInput input) throws CommandSyntaxException {
        WorldCelestials worldCelestials = source.getServer().getDataStorage().computeIfAbsent(WorldCelestials.ID);
        CelestialBodyInstance instance = worldCelestials.get(input.celestial());

        if (instance.isEmpty()) {
            return -1;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(instance.typeHolder().getRegisteredName()).append(" has the following components: ");

        instance.getComponents().stream().forEach(component -> {
            Identifier id = source.registryAccess().lookupOrThrow(Registries.DATA_COMPONENT_TYPE).getKey(component.type());
            var value = component.value();

            builder.append(id).append("=").append(value).append(",\n");
        });

        source.sendSuccess(() -> Component.literal(builder.toString()), false);

        return 1;
    }

    private static int printCelestialComponent(CommandSourceStack source, CelestialInput input, Identifier componentId) throws CommandSyntaxException {
        WorldCelestials worldCelestials = source.getServer().getDataStorage().computeIfAbsent(WorldCelestials.ID);
        CelestialBodyInstance instance = worldCelestials.get(input.celestial());

        DataComponentType<?> optionalDataComponent = getComponent(source.registryAccess(), componentId);

        var component = instance.get(optionalDataComponent);

        if (component == null) {
            throw ERROR_EMPTY_COMPONENT.create(instance.typeHolder().getRegisteredName(), componentId);
        }

        source.sendSuccess(() -> Component.literal(componentId + "=" + component), false);

        return 1;
    }

    private static int modifyComponent(CommandSourceStack source, CelestialInput input, Identifier componentId) throws CommandSyntaxException {
        WorldCelestials worldCelestials = source.getServer().getDataStorage().computeIfAbsent(WorldCelestials.ID);
        DataComponentType<?> data = getComponent(source.registryAccess(), componentId);

        input.components().keySet().forEach(component -> {

        });

        return 1;
    }

    private static int fill(CommandSourceStack source, CelestialInput input, Player player) throws CommandSyntaxException {
        WorldCelestials worldCelestials = source.getServer().getDataStorage().computeIfAbsent(WorldCelestials.ID);

        worldCelestials.modify(input.celestial(), celestial -> {
            celestial.setCustomName(Component.literal("I have a name!").withColor(TextColor.AQUA));
            celestial.setOwner(EntityReference.of(player));
            celestial.setPosition(new Vec3(1.0F, 2.0F, 3.0F));
        });

        return 1;
    }

    @SuppressWarnings("unchecked")
    private static <T> DataComponentType<T> getComponent(RegistryAccess registryAccess, Identifier componentId) throws CommandSyntaxException {
        Registry<DataComponentType<?>> components = registryAccess.lookupOrThrow(Registries.DATA_COMPONENT_TYPE);

        Optional<DataComponentType<?>> optionalDataComponent = components.getOptional(componentId);

        if (optionalDataComponent.isEmpty()) {
            throw ERROR_INVALID_COMPONENT.create(componentId);
        }

        return (DataComponentType<T>) optionalDataComponent.get();
    }

}
