package com.noscompany.snake.game.online.host.server.javalin.internal.state.running;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noscompany.snake.game.online.contract.object.mapper.ObjectMapperCreator;
import com.noscompany.snake.game.online.host.room.message.dispatcher.RoomCommandHandlerForRemoteClients;
import com.noscompany.snake.game.online.host.server.javalin.internal.state.ServerState;
import io.javalin.Javalin;
import io.javalin.websocket.WsContext;
import io.vavr.control.Try;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RunningServerCreator {

    public static Try<ServerState> createRunningServer(int port, RoomCommandHandlerForRemoteClients handlerForRemoteClients) {
        try {
            Map<String, WsContext> connectedUsersById = new ConcurrentHashMap<>();
            RoomMessageHandler roomMessageHandler = new RoomMessageHandler(handlerForRemoteClients, new MessageDeserializer(), connectedUsersById);
            ObjectMapper objectMapper = ObjectMapperCreator.createInstance();
            Javalin javalin = startJavalinServer(port, roomMessageHandler);
            return Try.success(new RunningServerState(javalin, connectedUsersById, objectMapper));
        } catch (Throwable t) {
            return Try.failure(t);
        }

    }

    private static Javalin startJavalinServer(int port, RoomMessageHandler roomMessageHandler) {
        return Javalin
                .create()
                .ws("/", wsConfig -> {
                    wsConfig.onConnect(roomMessageHandler);
                    wsConfig.onMessage(roomMessageHandler);
                    wsConfig.onClose(roomMessageHandler);
                    wsConfig.onError(roomMessageHandler);
                })
                .start(port);
    }
}