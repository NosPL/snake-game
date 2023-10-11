package com.noscompany.snake.game.online.client.internal.state.connected;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.snake.game.online.client.HostAddress;
import com.noscompany.snake.game.online.online.contract.serialization.OnlineMessageDeserializer;
import com.noscompany.snake.game.online.online.contract.serialization.OnlineMessageSerializer;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.wasync.*;
import org.atmosphere.wasync.impl.AtmosphereClient;
import org.atmosphere.wasync.impl.DefaultOptions;

import static org.atmosphere.wasync.Request.METHOD.GET;

@Slf4j
final class WebsocketCreator {
    private static final String URL_PROTOCOL_PREFIX = "ws://";

    Try<Websocket> create(HostAddress hostAddress,
                          MessagePublisher messagePublisher,
                          OnlineMessageDeserializer deserializer,
                          OnlineMessageSerializer serializer) {
        var socketMessageHandler = SocketMessageHandler.create(messagePublisher, deserializer);
        AtmosphereClient client = ClientFactory.getDefault().newClient(AtmosphereClient.class);
        var socket = createSocket(client, socketMessageHandler);
        var request = createRequest(hostAddress, client);
        return Try
                .of(() -> socket.open(request))
                .onSuccess(connected -> log.info("Socket got successfully open"))
                .onFailure(t -> log.info("Failed to open socket connection, cause: ", t))
                .map(s -> new Websocket(s, serializer));
    }

    private Socket createSocket(AtmosphereClient client, SocketMessageHandler socketMessageHandler) {
        DefaultOptions options = socketOptions(client);
        return client.create(options)
                .on(Event.MESSAGE, socketMessageHandler)
                .on(Event.ERROR, object -> socketMessageHandler.connectionClosedBecauseOfError())
                .on(Event.CLOSE, object -> socketMessageHandler.connectionClosed());
    }

    private DefaultOptions socketOptions(AtmosphereClient atmosphereClient) {
        return atmosphereClient
                .newOptionsBuilder()
                .reconnectAttempts(5)
                .pauseBeforeReconnectInSeconds(2)
                .build();
    }

    private Request createRequest(HostAddress hostAddress, Client client) {
        return client
                .newRequestBuilder()
                .method(GET)
                .uri(toUrl(hostAddress))
                .transport(Request.TRANSPORT.WEBSOCKET)
                .build();
    }

    private String toUrl(HostAddress hostAddress) {
        return URL_PROTOCOL_PREFIX + hostAddress.getAddress() + "/room";
    }
}