package com.noscompany.snake.game.online.client.internal.state.connected;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noscompany.snake.game.online.client.ClientEventHandler;
import com.noscompany.snake.game.online.client.HostAddress;
import com.noscompany.snake.game.online.client.internal.state.ClientState;
import com.noscompany.snake.game.online.contract.object.mapper.ObjectMapperCreator;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.wasync.*;
import org.atmosphere.wasync.impl.AtmosphereClient;

import static org.atmosphere.wasync.Request.METHOD.GET;

@Slf4j
public class ConnectedClientCreator {
    private static final String URL_PROTOCOL_PREFIX = "ws://";

    public static Try<ClientState> create(HostAddress hostAddress, ClientEventHandler eventHandler) {
        var objectMapper = ObjectMapperCreator.createInstance();
        return createOpenSocket(hostAddress, eventHandler, objectMapper)
                .map(socket -> new MessageSender(socket, objectMapper))
                .map(messageSender -> (ClientState) new Connected(messageSender, eventHandler))
                .onSuccess(connected -> log.info("Socket got successfully open"))
                .onFailure(t -> log.info("Failed to open socket connection, cause: ", t));
    }

    private static Try<Socket> createOpenSocket(HostAddress hostAddress, ClientEventHandler clientEventHandler, ObjectMapper objectMapper) {
        AtmosphereClient client = ClientFactory.getDefault().newClient(AtmosphereClient.class);
        var socket = createSocket(clientEventHandler, client, objectMapper);
        var request = createRequest(hostAddress, client);
        return Try
                .of(() -> socket.open(request))
                .onFailure(t -> {
                    log.warn("Failed to connect to server", t);
                });

    }

    private static Socket createSocket(ClientEventHandler clientEventHandler, Client client, ObjectMapper objectMapper) {
        var socketMessageHandler = SocketMessageHandler.create(clientEventHandler, objectMapper);
        return client.create()
                .on(Event.MESSAGE, socketMessageHandler)
                .on(Event.ERROR, object -> socketMessageHandler.connectionClosedBecauseOfError())
                .on(Event.CLOSE, object -> socketMessageHandler.connectionClosed());
    }

    private static Request createRequest(HostAddress hostAddress, Client client) {
        return client
                .newRequestBuilder()
                .method(GET)
                .uri(toUrl(hostAddress))
                .transport(Request.TRANSPORT.WEBSOCKET)
                .transport(Request.TRANSPORT.LONG_POLLING)
                .build();
    }

    private static String toUrl(HostAddress hostAddress) {
        return URL_PROTOCOL_PREFIX + hostAddress.getAddress() + "/room";
    }
}