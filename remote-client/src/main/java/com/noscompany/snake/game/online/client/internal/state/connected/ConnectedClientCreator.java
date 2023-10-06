package com.noscompany.snake.game.online.client.internal.state.connected;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noscompany.snake.game.online.client.ClientEventHandler;
import com.noscompany.snake.game.online.client.HostAddress;
import com.noscompany.snake.game.online.client.internal.state.ClientState;
import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.object.mapper.ObjectMapperCreator;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.wasync.*;
import org.atmosphere.wasync.impl.AtmosphereClient;
import org.atmosphere.wasync.impl.DefaultOptions;

import java.util.concurrent.atomic.AtomicReference;

import static org.atmosphere.wasync.Request.METHOD.GET;

@Slf4j
public class ConnectedClientCreator {
    private static final String URL_PROTOCOL_PREFIX = "ws://";

    public static Try<ClientState> create(HostAddress hostAddress, ClientEventHandler eventHandler) {
        var objectMapper = ObjectMapperCreator.createInstance();
        var userId = new AtomicReference<>(new UserId(""));
        var decoratedHandler = new UpdateUserIdHandler(userId, eventHandler);
        return createOpenSocket(hostAddress, decoratedHandler, objectMapper)
                .map(socket -> new MessageSender(socket, objectMapper))
                .map(messageSender -> new Connected(messageSender, decoratedHandler, userId));
    }

    private static Try<Socket> createOpenSocket(HostAddress hostAddress, ClientEventHandler clientEventHandler, ObjectMapper objectMapper) {
        SocketMessageHandler socketMessageHandler = SocketMessageHandler.create(clientEventHandler, objectMapper);
        AtmosphereClient client = ClientFactory.getDefault().newClient(AtmosphereClient.class);
        var socket = createSocket(client, socketMessageHandler);
        var request = createRequest(hostAddress, client);
        return Try
                .of(() -> socket.open(request))
                .onSuccess(connected -> log.info("Socket got successfully open"))
                .onFailure(t -> log.info("Failed to open socket connection, cause: ", t));
    }

    private static Socket createSocket(AtmosphereClient client, SocketMessageHandler socketMessageHandler) {
        DefaultOptions options = socketOptions(client);
        return client.create(options)
                .on(Event.MESSAGE, socketMessageHandler)
                .on(Event.ERROR, object -> socketMessageHandler.connectionClosedBecauseOfError())
                .on(Event.CLOSE, object -> socketMessageHandler.connectionClosed());
    }

    private static DefaultOptions socketOptions(AtmosphereClient atmosphereClient) {
        return atmosphereClient
                .newOptionsBuilder()
                .reconnectAttempts(5)
                .pauseBeforeReconnectInSeconds(2)
                .build();
    }

    private static Request createRequest(HostAddress hostAddress, Client client) {
        return client
                .newRequestBuilder()
                .method(GET)
                .uri(toUrl(hostAddress))
                .transport(Request.TRANSPORT.WEBSOCKET)
                .build();
    }

    private static String toUrl(HostAddress hostAddress) {
        return URL_PROTOCOL_PREFIX + hostAddress.getAddress() + "/room";
    }
}