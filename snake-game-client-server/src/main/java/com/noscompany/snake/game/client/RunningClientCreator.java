package com.noscompany.snake.game.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noscompany.snake.game.commons.Constants;
import com.noscompany.snake.game.commons.Ipv4Validator;
import com.noscompany.snake.game.commons.PortValidator;
import com.noscompany.snake.game.commons.messages.events.decoder.MessageDecoder;
import io.vavr.control.Option;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.wasync.*;
import org.atmosphere.wasync.impl.AtmosphereClient;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.noscompany.snake.game.client.StartingClientError.INCORRECT_IPV4_FORMAT;
import static com.noscompany.snake.game.client.StartingClientError.INCORRECT_PORT_FORMAT;
import static org.atmosphere.wasync.Request.METHOD.GET;

@Slf4j
public class RunningClientCreator {

    @SneakyThrows
    public static Option<SnakeOnlineClient> startClient(String ipv4Address,
                                                        String port,
                                                        ClientEventHandler eventHandler) {
        if (!Ipv4Validator.isValid(ipv4Address)) {
            eventHandler.handle(INCORRECT_IPV4_FORMAT);
            return Option.none();
        } else if (!PortValidator.isValid(port)) {
            eventHandler.handle(INCORRECT_PORT_FORMAT);
            return Option.none();
        }
        Socket socket = new NullSocket();
        try {
            socket = createSocket(ipv4Address, port, eventHandler);
            var messageSender = new MessageSender(socket, new ObjectMapper());
            return Option.of(new ConnectedClient(messageSender, eventHandler));
        } catch (Exception e) {
            log.error("Failed to connect to server", e);
            eventHandler.handle(StartingClientError.FAILED_TO_CONNECT_TO_SERVER);
            socket.close();
            return Option.none();
        }
    }

    private static Socket createSocket(String ipv4Address,
                                       String port,
                                       ClientEventHandler eventHandler) throws IOException {
        var messageDispatcher = new MessageDispatcher(eventHandler, new MessageDecoder());
        var client = getClient();
        var request = createRequest(ipv4Address, port, client);
        return client.create()
                .on(Event.MESSAGE, messageDispatcher)
                .on(Event.CLOSE, s -> eventHandler.connectionClosed())
                .open(request);
    }

    private static Client getClient() {
        return ClientFactory.getDefault().newClient(AtmosphereClient.class);
    }

    private static Request createRequest(String serverIpAddress,
                                         String port,
                                         Client client) {
        return client
                .newRequestBuilder()
                .method(GET)
                .uri(serverUrl(serverIpAddress, port))
                .transport(Request.TRANSPORT.WEBSOCKET)
                .transport(Request.TRANSPORT.LONG_POLLING)
                .build();
    }

    private static String serverUrl(String serverIpAddress, String port) {
        return "ws://" + serverIpAddress + ":" + port + Constants.SNAKE_ENDPOINT_URL;
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