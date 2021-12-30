package com.noscompany.snake.game.online.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noscompany.snake.game.commons.SnakeOnlineServerConstants;
import com.noscompany.snake.game.commons.object.mapper.ObjectMapperCreator;
import io.vavr.control.Option;
import io.vavr.jackson.datatype.VavrModule;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.wasync.*;
import org.atmosphere.wasync.impl.AtmosphereClient;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.atmosphere.wasync.Request.METHOD.GET;

@Slf4j
class ConnectedClientCreator {

    @SneakyThrows
    static Option<SnakeOnlineClient> create(String url, ClientEventHandler eventHandler) {
        Socket socket = new NullSocket();
        try {
            socket = createSocket(url, eventHandler);
            var messageSender = new MessageSender(socket, new ObjectMapper());
            return Option.of(new ConnectedClient(messageSender, eventHandler));
        } catch (Exception e) {
            log.error("Failed to connect to server", e);
            eventHandler.handle(StartingClientError.FAILED_TO_CONNECT_TO_SERVER);
            socket.close();
            return Option.none();
        }
    }

    private static Socket createSocket(String roomName,
                                       ClientEventHandler eventHandler) throws IOException {
        ObjectMapper objectMapper = ObjectMapperCreator.createInstance();
        var messageDispatcher = new MessageDispatcher(eventHandler, objectMapper);
        var client = getClient();
        var request = createRequest(roomName, client);
        return client.create()
                .on(Event.MESSAGE, messageDispatcher)
                .on(Event.ERROR, s -> eventHandler.handle(ClientError.CONNECTION_CLOSED))
                .on(Event.CLOSE, s -> eventHandler.connectionClosed())
                .open(request);
    }

    private static Client getClient() {
        return ClientFactory.getDefault().newClient(AtmosphereClient.class);
    }

    private static Request createRequest(String url,
                                         Client client) {
        return client
                .newRequestBuilder()
                .method(GET)
                .uri(url)
                .transport(Request.TRANSPORT.WEBSOCKET)
                .transport(Request.TRANSPORT.LONG_POLLING)
                .build();
    }

    private static class NullSocket implements Socket {

        @Override
        public Future fire(Object data) throws IOException {
            return null;
        }

        @Override
        public Socket on(Function<?> function) {
            return null;
        }

        @Override
        public Socket on(String functionMessage, Function<?> function) {
            return null;
        }

        @Override
        public Socket on(Event event, Function<?> function) {
            return this;
        }

        @Override
        public Socket open(Request request) throws IOException {
            return this;
        }

        @Override
        public Socket open(Request request, long timeout, TimeUnit unit) throws IOException {
            return this;
        }

        @Override
        public void close() {

        }

        @Override
        public STATUS status() {
            return STATUS.CLOSE;
        }
    }
}