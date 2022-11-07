package com.noscompany.snake.game.online.client.internal.state.connected;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.client.ClientError;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.wasync.Socket;

import java.io.IOException;

@Slf4j
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

    void closeConnection() {
        log.info("closing socket");
        socket.close();
    }
}