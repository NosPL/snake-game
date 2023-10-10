package com.noscompany.snake.game.online.host.server;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.snake.game.online.contract.messages.DedicatedClientMessage;
import com.noscompany.snake.game.online.contract.messages.PublicClientMessage;
import com.noscompany.snake.game.online.contract.messages.server.ServerParams;
import com.noscompany.snake.game.online.contract.messages.server.events.FailedToStartServer;
import com.noscompany.snake.game.online.contract.messages.server.events.ServerFailedToSendMessageToRemoteClients;
import com.noscompany.snake.game.online.contract.messages.server.events.ServerGotShutdown;
import com.noscompany.snake.game.online.contract.messages.server.events.ServerStarted;
import com.noscompany.snake.game.online.host.server.ports.Websocket;
import com.noscompany.snake.game.online.host.server.ports.WebsocketCreator;
import com.noscompany.snake.game.online.online.contract.serialization.OnlineMessageDeserializer;
import com.noscompany.snake.game.online.online.contract.serialization.OnlineMessageSerializer;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.noscompany.snake.game.online.contract.messages.server.events.ServerFailedToSendMessageToRemoteClients.Reason.FAILED_TO_SERIALIZE_MESSAGE;
import static java.util.function.Function.identity;

@Slf4j
@AllArgsConstructor
class ServerImpl implements Server {
    private final MessagePublisher messagePublisher;
    private final WebsocketCreator websocketCreator;
    private Websocket websocket;
    private final OnlineMessageSerializer onlineMessageSerializer;
    private final OnlineMessageDeserializer messageDeserializer;

    @Override
    public Either<FailedToStartServer, ServerStarted> start(ServerParams serverParams) {
        if (websocket.isOpen())
            shutdown();
        var roomWebsocketAdapter = RoomWebsocketAdapter.create(messagePublisher, messageDeserializer);
        return websocketCreator
                .create(serverParams, roomWebsocketAdapter)
                .peek(websocket -> this.websocket = websocket)
                .map(websocket -> new ServerStarted(serverParams));
    }

    @Override
    public ServerGotShutdown shutdown() {
        sendPublicClientMessage(new ServerGotShutdown());
        websocket.close();
        websocket = new ClosedWebsocket();
        return new ServerGotShutdown();
    }

    @Override
    public Option<ServerFailedToSendMessageToRemoteClients> sendPublicClientMessage(PublicClientMessage publicMessage) {
        return onlineMessageSerializer
                .serialize(publicMessage)
                .map(websocket::sendToAllClients)
                .toEither()
                .mapLeft(t -> new ServerFailedToSendMessageToRemoteClients(FAILED_TO_SERIALIZE_MESSAGE))
                .fold(Option::of, identity());
    }

    @Override
    public Option<ServerFailedToSendMessageToRemoteClients> sendDedicatedClientMessage(DedicatedClientMessage dedicatedMessage) {
        var userId = dedicatedMessage.getUserId();
        return onlineMessageSerializer
                .serialize(dedicatedMessage)
                .map(msgString -> websocket.sendToClient(userId, msgString))
                .toEither()
                .mapLeft(t -> new ServerFailedToSendMessageToRemoteClients(FAILED_TO_SERIALIZE_MESSAGE))
                .fold(Option::of, identity());
    }
}