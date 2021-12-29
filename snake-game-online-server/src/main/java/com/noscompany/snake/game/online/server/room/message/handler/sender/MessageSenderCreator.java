package com.noscompany.snake.game.online.server.room.message.handler.sender;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noscompany.snake.game.commons.object.mapper.ObjectMapperCreator;
import org.atmosphere.cpr.Broadcaster;

public class MessageSenderCreator {

    public static MessageSender create(Broadcaster broadcaster) {
        ObjectMapper objectMapper = ObjectMapperCreator.createInstance();
        return new MessageSenderImpl(broadcaster, objectMapper);
    }
}
