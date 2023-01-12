package com.noscompany.snake.game.online.client.internal.state.connected;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.client.SendClientMessageError;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.wasync.Socket;

import java.io.IOException;

import static org.atmosphere.wasync.Socket.STATUS.OPEN;
import static org.atmosphere.wasync.Socket.STATUS.REOPENED;

@Slf4j
@AllArgsConstructor
class MessageSender {
    private final Socket socket;
    private final ObjectMapper objectMapper;

    boolean isConnected() {
        return socket.status() == OPEN || socket.status() == REOPENED;
    }

    Option<SendClientMessageError> send(OnlineMessage onlineMessage) {
        try {
            socket.fire(asJson(onlineMessage));
            return Option.none();
        } catch (JsonProcessingException je) {
            return Option.of(SendClientMessageError.FAILED_TO_SERIALIZE_MESSAGE);
        } catch (IOException ioe) {
            return Option.of(SendClientMessageError.CONNECTION_CLOSED);
        }
    }

    private String asJson(OnlineMessage onlineMessage) throws JsonProcessingException {
        return objectMapper.writeValueAsString(onlineMessage);
    }

    void closeConnection() {
        log.info("closing socket");
        socket.close();
    }
}