package com.noscompany.snake.game.online.host.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.server.ServerParams;
import com.noscompany.snake.game.online.contract.messages.server.commands.StartServer;
import com.noscompany.snake.game.online.contract.messages.server.events.FailedToStartServer;
import com.noscompany.snake.game.online.contract.messages.server.events.ServerFailedToSendMessageToRemoteClients;
import com.noscompany.snake.game.online.contract.messages.server.events.ServerGotShutdown;
import com.noscompany.snake.game.online.contract.messages.server.events.ServerStarted;
import com.noscompany.snake.game.online.host.server.dto.RemoteClientId;
import com.noscompany.snake.game.online.host.server.ports.Websocket;
import com.noscompany.snake.game.online.host.server.ports.WebsocketCreator;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.noscompany.snake.game.online.contract.messages.server.events.ServerFailedToSendMessageToRemoteClients.Reason.FAILED_TO_SERIALIZE_MESSAGE;

@Slf4j
@AllArgsConstructor
class ServerImpl implements Server {
    private final MessagePublisher messagePublisher;
    private final WebsocketCreator websocketCreator;
    private Websocket websocket;
    private final ObjectMapper objectMapper;

    @Override
    public Either<FailedToStartServer, ServerStarted> start(ServerParams serverParams) {
        if (websocket.isOpen())
            shutdown();
        var roomWebsocketAdapter = RoomWebsocketAdapter.create(messagePublisher);
        return websocketCreator
                .create(serverParams, roomWebsocketAdapter)
                .peek(websocket -> this.websocket = websocket)
                .map(websocket -> new ServerStarted(serverParams));
    }

    @Override
    public ServerGotShutdown shutdown() {
        sendToAllClients(new ServerGotShutdown());
        websocket.close();
        websocket = new ClosedWebsocket();
        return new ServerGotShutdown();
    }

    @Override
    public Option<ServerFailedToSendMessageToRemoteClients> sendToAllClients(OnlineMessage onlineMessage) {
        try {
            String serializedMessage = objectMapper.writeValueAsString(onlineMessage);
            return websocket.sendToAllClients(serializedMessage);
        } catch (Throwable t) {
            return Option.of(new ServerFailedToSendMessageToRemoteClients(FAILED_TO_SERIALIZE_MESSAGE));
        }
    }

    @Override
    public Option<ServerFailedToSendMessageToRemoteClients> sendToClientWithGivenId(UserId remoteClientId, OnlineMessage onlineMessage) {
        try {
            String serializedMessage = objectMapper.writeValueAsString(onlineMessage);
            return websocket.sendToClient(remoteClientId, serializedMessage);
        } catch (Throwable t) {
            return Option.of(new ServerFailedToSendMessageToRemoteClients(FAILED_TO_SERIALIZE_MESSAGE));
        }
    }
}