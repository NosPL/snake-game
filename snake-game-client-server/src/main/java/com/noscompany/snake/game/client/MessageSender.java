package com.noscompany.snake.game.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noscompany.snake.game.commons.MessageDto;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import org.atmosphere.wasync.Socket;

import java.io.IOException;

@AllArgsConstructor
class MessageSender {
    private final Socket socket;
    private final ObjectMapper objectMapper;

    Option<ClientError> send(MessageDto messageDto) {
        try {
            socket.fire(asJson(messageDto));
            return Option.none();
        } catch (JsonProcessingException je) {
            return Option.of(ClientError.FAILED_TO_SERIALIZE_MESSAGE);
        } catch (IOException ioe) {
            return Option.of(ClientError.CONNECTION_CLOSED);
        }
    }

    private String asJson(MessageDto messageDto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(messageDto);
    }

    void closeSocket() {
        socket.close();
    }
}