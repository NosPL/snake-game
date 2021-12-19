package com.noscompany.snake.game.online.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noscompany.snake.game.commons.OnlineMessage;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import org.atmosphere.wasync.Socket;

import java.io.IOException;

@AllArgsConstructor
class MessageSender {
    private final Socket socket;
    private final ObjectMapper objectMapper;

    Option<ClientError> send(OnlineMessage onlineMessage) {
        try {
            socket.fire(asJson(onlineMessage));
            return Option.none();
        } catch (JsonProcessingException je) {
            return Option.of(ClientError.FAILED_TO_SERIALIZE_MESSAGE);
        } catch (IOException ioe) {
            return Option.of(ClientError.CONNECTION_CLOSED);
        }
    }

    private String asJson(OnlineMessage onlineMessage) throws JsonProcessingException {
        return objectMapper.writeValueAsString(onlineMessage);
    }

    void closeSocket() {
        socket.close();
    }
}