package com.noscompany.snake.game.online.client.internal.state.connected;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noscompany.snake.game.online.client.ClientEventHandler;
import com.noscompany.snake.game.online.client.internal.state.ClientState;
import com.noscompany.snake.game.online.contract.object.mapper.ObjectMapperCreator;
import io.vavr.control.Try;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.wasync.*;
import org.atmosphere.wasync.impl.AtmosphereClient;

import static org.atmosphere.wasync.Request.METHOD.GET;

@Slf4j
public class ConnectedClientCreator {

    public static Try<ClientState> create(String url, ClientEventHandler eventHandler) {
        var objectMapper = ObjectMapperCreator.createInstance();
        return createOpenSocket(url, eventHandler, objectMapper)
                .map(socket -> new MessageSender(socket, objectMapper))
                .map(messageSender -> (ClientState) new Connected(messageSender, eventHandler))
                .onSuccess(connected -> log.info("Socket got successfully open"))
                .onFailure(t -> log.info("Failed to open socket connection, cause: ", t));
    }

    private static Try<Socket> createOpenSocket(String roomName, ClientEventHandler clientEventHandler, ObjectMapper objectMapper) {
        var client = ClientFactory.getDefault().newClient(AtmosphereClient.class);
        var socket = createSocket(clientEventHandler, client, objectMapper);
        var request = createRequest(roomName, client);
        return Try
                .of(() ->socket.open(request))
                .onFailure(t -> socket.close());

    }

    private static Socket createSocket(ClientEventHandler clientEventHandler, Client client, ObjectMapper objectMapper) {
        var socketMessageHandler = SocketMessageHandler.create(clientEventHandler, objectMapper);
        return client.create()
                .on(Event.MESSAGE, socketMessageHandler)
                .on(Event.ERROR, object -> socketMessageHandler.connectionClosedBecauseOfError())
                .on(Event.CLOSE, object -> socketMessageHandler.connectionClosed());
    }

    private static Request createRequest(String url, Client client) {
        return client
                .newRequestBuilder()
                .method(GET)
                .uri(url)
                .transport(Request.TRANSPORT.WEBSOCKET)
                .transport(Request.TRANSPORT.LONG_POLLING)
                .build();
    }

}