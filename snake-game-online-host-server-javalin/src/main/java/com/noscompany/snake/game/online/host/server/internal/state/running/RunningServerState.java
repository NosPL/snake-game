package com.noscompany.snake.game.online.host.server.internal.state.running;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.host.server.dto.ServerParams;
import com.noscompany.snake.game.online.host.room.mediator.RoomMediatorForRemoteClients;
import com.noscompany.snake.game.online.host.room.mediator.dto.RemoteClientId;
import com.noscompany.snake.game.online.host.room.mediator.ports.RoomEventHandlerForRemoteClients.SendMessageError;
import com.noscompany.snake.game.online.host.server.internal.state.ServerState;
import com.noscompany.snake.game.online.host.server.internal.state.shutdown.ShutdownServerState;
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
    public Try<ServerState> start(ServerParams serverParams, RoomMediatorForRemoteClients roomMediatorForRemoteClients) {
        return Try.success(this);
    }

    @Override
    public boolean isRunning() {
        return true;
    }

    @Override
    public ServerState shutdown() {
        javalin.close();
        return new ShutdownServerState();
    }

    @SneakyThrows
    @Override
    public Option<SendMessageError> sendToAllClients(OnlineMessage onlineMessage) {
        String serializedMessage = objectMapper.writeValueAsString(onlineMessage);
        sendToAllClients(serializedMessage);
        return Option.none();
    }

    private void sendToAllClients(String serializedMessage) {
        connectedUsersById
                .values()
                .forEach(wsContext -> wsContext.send(serializedMessage));
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