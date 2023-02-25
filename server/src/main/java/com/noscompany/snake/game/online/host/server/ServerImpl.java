package com.noscompany.snake.game.online.host.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.server.ServerGotShutdown;
import com.noscompany.snake.game.online.host.server.dto.SendMessageError;
import com.noscompany.snake.game.online.host.server.dto.ServerParams;
import com.noscompany.snake.game.online.host.server.dto.ServerStartError;
import com.noscompany.snake.game.online.host.server.ports.RoomApiForRemoteClients;
import com.noscompany.snake.game.online.host.server.dto.RemoteClientId;
import com.noscompany.snake.game.online.host.server.ports.Websocket;
import com.noscompany.snake.game.online.host.server.ports.WebsocketCreator;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.noscompany.snake.game.online.host.server.dto.SendMessageError.FAILED_TO_SERIALIZE_MESSAGE;

@Slf4j
@AllArgsConstructor
class ServerImpl implements Server {
    private final WebsocketCreator websocketCreator;
    private Websocket websocket;
    private final ObjectMapper objectMapper;

    @Override
    public Option<ServerStartError> start(ServerParams serverParams, RoomApiForRemoteClients roomMediatorForRemoteClients) {
        if (websocket.isOpen())
            Option.none();
        var roomWebsocketAdapter = RoomWebsocketAdapter.create(roomMediatorForRemoteClients);
        return websocketCreator
                .create(serverParams, roomWebsocketAdapter)
                .peek(websocket -> this.websocket = websocket)
                .transform(this::toResult);
    }

    private Option<ServerStartError> toResult(Try<Websocket> createWebsocketResult) {
        return createWebsocketResult
                .toEither()
                .mapLeft(ServerStartError::new)
                .swap()
                .toOption();
    }

    @Override
    public boolean isRunning() {
        return websocket.isOpen();
    }

    @Override
    public void shutdown() {
        sendToAllClients(new ServerGotShutdown());
        websocket.close();
        websocket = new ClosedWebsocket();
    }

    @Override
    public Option<SendMessageError> sendToAllClients(OnlineMessage onlineMessage) {
        try {
            String serializedMessage = objectMapper.writeValueAsString(onlineMessage);
            return websocket.sendToAllClients(serializedMessage);
        } catch (Throwable t) {
            return Option.of(FAILED_TO_SERIALIZE_MESSAGE);
        }
    }

    @Override
    public Option<SendMessageError> sendToClientWithId(RemoteClientId remoteClientId, OnlineMessage onlineMessage) {
        try {
            String serializedMessage = objectMapper.writeValueAsString(onlineMessage);
            return websocket.sendToClient(remoteClientId, serializedMessage);
        } catch (Throwable t) {
            return Option.of(FAILED_TO_SERIALIZE_MESSAGE);
        }
    }
}