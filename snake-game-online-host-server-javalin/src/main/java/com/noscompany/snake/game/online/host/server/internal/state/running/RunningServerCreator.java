package com.noscompany.snake.game.online.host.server.internal.state.running;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noscompany.snake.game.online.contract.object.mapper.ObjectMapperCreator;
import com.noscompany.snake.game.online.host.server.dto.ServerParams;
import com.noscompany.snake.game.online.host.room.mediator.RoomMediatorForRemoteClients;
import com.noscompany.snake.game.online.host.server.internal.state.ServerState;
import io.javalin.Javalin;
import io.javalin.websocket.WsContext;
import io.vavr.control.Try;

import java.util.concurrent.ConcurrentHashMap;

public class RunningServerCreator {

    public static Try<ServerState> createRunningServer(ServerParams serverParams, RoomMediatorForRemoteClients handlerForRemoteClients) {
        try {
            ConcurrentHashMap<String, WsContext> connectedUsersById = new ConcurrentHashMap<>();
            ServerEventHandler serverEventHandler = new ServerEventHandler(handlerForRemoteClients, new MessageDeserializer(), connectedUsersById);
            ObjectMapper objectMapper = ObjectMapperCreator.createInstance();
            Javalin javalin = startJavalinServer(serverParams, serverEventHandler);
            RunningServerState runningServerState = new RunningServerState(javalin, connectedUsersById, objectMapper);
            return Try.success(runningServerState);
        } catch (Throwable t) {
            return Try.failure(t);
        }

    }

    private static Javalin startJavalinServer(ServerParams serverParams, ServerEventHandler serverEventHandler) {
        return Javalin
                .create()
                .ws("/room/", wsConfig -> {
                    wsConfig.onConnect(serverEventHandler);
                    wsConfig.onMessage(serverEventHandler);
                    wsConfig.onClose(serverEventHandler);
                    wsConfig.onError(serverEventHandler);
                })
                .start(serverParams.getPort());
    }
}