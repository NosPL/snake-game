package com.noscompany.snake.game.online.client.internal.state.connected;

import com.noscompany.snake.game.online.client.SendClientMessageError;
import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.online.contract.serialization.OnlineMessageSerializer;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.wasync.Socket;

import static com.noscompany.snake.game.online.client.SendClientMessageError.CONNECTION_CLOSED;
import static com.noscompany.snake.game.online.client.SendClientMessageError.FAILED_TO_SERIALIZE_MESSAGE;
import static java.util.function.Function.identity;
import static org.atmosphere.wasync.Socket.STATUS.OPEN;
import static org.atmosphere.wasync.Socket.STATUS.REOPENED;

@Slf4j
@AllArgsConstructor
class Websocket {
    private final Socket socket;
    private final OnlineMessageSerializer serializer;

    public boolean isConnected() {
        return socket.status() == OPEN || socket.status() == REOPENED;
    }

    public Option<SendClientMessageError> send(OnlineMessage onlineMessage) {
        return serializer
                .serialize(onlineMessage)
                .toEither(FAILED_TO_SERIALIZE_MESSAGE)
                .map(this::send)
                .fold(Option::of, identity());
    }

    private Option<SendClientMessageError> send(String stringMessage) {
        return Try
                .of(() -> socket.fire(stringMessage))
                .onFailure(t -> log.warn("Failed to send message. ", t))
                .toEither().swap().toOption()
                .map(t -> CONNECTION_CLOSED);
    }

    public void closeConnection() {
        log.info("closing socket");
        socket.close();
    }
}