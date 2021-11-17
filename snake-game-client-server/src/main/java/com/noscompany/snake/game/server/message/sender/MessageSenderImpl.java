package com.noscompany.snake.game.server.message.sender;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noscompany.snake.game.commons.MessageDto;
import com.noscompany.snake.game.commons.Constants;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;

@Slf4j
@RequiredArgsConstructor
class MessageSenderImpl implements MessageSender {
    private ObjectMapper objectMapper = new ObjectMapper();
    private final BroadcasterFactory broadcasterFactory;
    private Broadcaster broadcaster;

    @Override
    public void sendTo(String userId, MessageDto messageDto) {
        var serialized = serialize(messageDto);
        broadcaster()
                .getAtmosphereResources()
                .stream()
                .filter(r -> r.uuid().equals(userId))
                .forEach(r -> tryWrite(serialized, r));
    }

    @Override
    public void sendToAll(MessageDto messageDto) {
        broadcaster().broadcast(serialize(messageDto));
    }

    @SneakyThrows
    private String serialize(Object message) {
        return objectMapper.writeValueAsString(message);
    }

    private void tryWrite(String message, AtmosphereResource r) {
        try {
            r.write(message);
        } catch (Exception e) {
            log.error("failed to send message: " + message + " to resource: " + r.uuid(), e);
        }
    }

    private Broadcaster broadcaster() {
        if (broadcaster == null)
            broadcaster = broadcasterFactory.lookup(Constants.SNAKE_ENDPOINT_URL);
        return broadcaster;
    }
}