package com.noscompany.snake.game.online.host.server.javalin.internal.state.running;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.host.room.message.dispatcher.RoomCommandHandlerForRemoteClients;
import com.noscompany.snake.game.online.host.room.message.dispatcher.dto.RemoteClientId;
import com.noscompany.snake.game.online.host.room.message.dispatcher.ports.RoomEventHandlerForRemoteClients;
import com.noscompany.snake.game.online.host.room.message.dispatcher.ports.RoomEventHandlerForRemoteClients.SendMessageError;
import com.noscompany.snake.game.online.host.server.javalin.internal.state.ServerState;
import com.noscompany.snake.game.online.host.server.javalin.internal.state.not.running.NotRunningServerState;
import io.javalin.Javalin;
import io.javalin.websocket.WsContext;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.util.Map;

@AllArgsConstructor
class RunningServerState implements ServerState {
    private final Javalin javalin;
    private final Map<String, WsContext> connectedUsersById;
    private final ObjectMapper objectMapper;

    @Override
    public Try<ServerState> start(int port, RoomCommandHandlerForRemoteClients handlerForRemoteClients) {
        return Try.success(this);
    }

    @Override
    public boolean isRunning() {
        return true;
    }

    @Override
    public ServerState shutdown() {
        javalin.close();
        return new NotRunningServerState();
    }

    @SneakyThrows
    @Override
    public Option<SendMessageError> sendToAllClients(OnlineMessage onlineMessage) {
        String serializedMessage = objectMapper.writeValueAsString(onlineMessage);
        connectedUsersById
                .values()
                .forEach(wsContext -> wsContext.send(serializedMessage));
        return Option.none();
    }

    @SneakyThrows
    @Override
    public Option<SendMessageError> sendToClientWithId(RemoteClientId remoteClientId, OnlineMessage onlineMessage) {
        String serializedMessage = objectMapper.writeValueAsString(onlineMessage);
        WsContext wsContext = connectedUsersById.get(remoteClientId.getId());
        if (wsContext != null)
            wsContext.send(serializedMessage);
        return Option.none();
    }
}