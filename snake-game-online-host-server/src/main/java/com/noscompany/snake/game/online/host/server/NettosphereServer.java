package com.noscompany.snake.game.online.host.server;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.host.server.ports.RoomApiForRemoteClients;
import com.noscompany.snake.game.online.host.server.dto.ServerParams;
import com.noscompany.snake.game.online.host.server.dto.ServerStartError;
import com.noscompany.snake.game.online.host.server.internal.state.ServerState;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
class NettosphereServer implements Server {
    private ServerState serverState;

    @Override
    public Option<ServerStartError> start(ServerParams serverParams, RoomApiForRemoteClients handlerForRemoteClients) {
        return serverState
                .start(serverParams, handlerForRemoteClients)
                .onSuccess(serverState -> this.serverState = serverState)
                .transform(this::toResult);
    }

    private Option<ServerStartError> toResult(Try<ServerState> startServerResult) {
        return startServerResult
                .toEither().swap().toOption()
                .map(ServerStartError::new);
    }

    @Override
    public boolean isRunning() {
        return serverState.isRunning();
    }

    @Override
    public void shutdown() {
        serverState = serverState.shutdown();
    }

    @Override
    public Option<SendMessageError> sendToAllClients(OnlineMessage onlineMessage) {
        return serverState.sendToAllClients(onlineMessage);
    }

    @Override
    public Option<SendMessageError> sendToClientWithId(RoomApiForRemoteClients.RemoteClientId remoteClientId, OnlineMessage onlineMessage) {
        return serverState.sendToClientWithId(remoteClientId, onlineMessage);
    }
}