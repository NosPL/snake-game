package com.noscompany.snake.game.server.local.api;

import com.noscompany.snake.game.commons.Ipv4Validator;
import com.noscompany.snake.game.commons.PortValidator;
import com.noscompany.snake.game.server.atmosphere.AtmosphereEndpoint;
import com.noscompany.snake.game.server.connected.users.ConnectedUsers;
import com.noscompany.snake.game.server.lobby.GameLobbyCreator;
import com.noscompany.snake.game.server.message.sender.MessageSenderCreator;
import io.vavr.control.Option;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.config.managed.ManagedAtmosphereHandler;
import org.atmosphere.cpr.AtmosphereFramework;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.nettosphere.Config;
import org.atmosphere.nettosphere.Nettosphere;

import java.util.Map;

import static com.noscompany.snake.game.server.local.api.StartingServerError.*;

@Slf4j
class RunningServerStateCreator {

    static Option<SnakeServer> create(String ipAddress,
                                      int port,
                                      SnakeServerEventHandler eventHandler) {
        return create(ipAddress, String.valueOf(port), eventHandler);
    }

    static Option<SnakeServer> create(String ipAddress,
                                      String port,
                                      SnakeServerEventHandler eventHandler) {
        if (!Ipv4Validator.isValid(ipAddress)) {
            eventHandler.handle(INCORRECT_IPv4_ADDRESS_FORMAT);
            return Option.none();
        } else if (!PortValidator.isValid(port)) {
            eventHandler.handle(INCORRECT_PORT_FORMAT);
            return Option.none();
        } else
            return runValidated(ipAddress, Integer.parseInt(port), eventHandler);
    }

    private static Option<SnakeServer> runValidated(String ipAddress, int port, SnakeServerEventHandler eventHandler) {
        try {
            var nettosphere = nettosphereServer(ipAddress, port);
            var snakeServer = snakeServer(eventHandler, nettosphere);
            nettosphere.start();
            return Option.of(snakeServer);
        } catch (Exception e) {
            log.error("exception occured", e);
            eventHandler.handle(UNKNOWN_ERROR);
            return Option.none();
        }
    }

    private static Nettosphere nettosphereServer(String ipAddress, int port) {
        return new Nettosphere.Builder().config(new Config.Builder()
                .port(port)
                .host(ipAddress)
                .initParam(
                        "org.atmosphere.cpr.scanClassPath",
                        "false")
                .initParam(
                        "org.atmosphere.cpr.packages",
                        "com.noscompany.snake.game.server.atmosphere")
                .build()).build();
    }

    private static SnakeServer snakeServer(SnakeServerEventHandler eventHandler,
                                           Nettosphere nettosphere) {
        var broadcasterFactory = getBroadcasterFactory(nettosphere);
        var atmosphereEndpoint = getAtmosphereEndpoint(nettosphere);
        var messageSender = MessageSenderCreator.messageSender(broadcasterFactory);
        var gameLobby = GameLobbyCreator.gameLobby(eventHandler, messageSender);
        var connectedUsers = ConnectedUsers.instance(gameLobby);
        atmosphereEndpoint.set(messageSender);
        atmosphereEndpoint.set(gameLobby);
        atmosphereEndpoint.set(connectedUsers);
        atmosphereEndpoint.set(eventHandler);
        return new RunningServerState(gameLobby, eventHandler, messageSender, connectedUsers, nettosphere);
    }

    private static BroadcasterFactory getBroadcasterFactory(Nettosphere nettosphere) {
        return nettosphere.framework().getBroadcasterFactory();
    }

    private static AtmosphereEndpoint getAtmosphereEndpoint(Nettosphere nettosphere) {
        final Map<String, AtmosphereFramework.AtmosphereHandlerWrapper> atmosphereHandlers = nettosphere
                .framework()
                .getAtmosphereHandlers();
        var handler = (ManagedAtmosphereHandler) atmosphereHandlers
                .get("/snake")
                .atmosphereHandler;
        return (AtmosphereEndpoint) handler.target();
    }
}