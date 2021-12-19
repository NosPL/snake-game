package com.noscompany.snake.game.online.server.message.handler;

import com.noscompany.snake.game.online.server.message.handler.message.mapper.MessageMapperCreator;
import com.noscompany.snake.game.online.server.room.RoomCreator;
import org.atmosphere.cpr.Broadcaster;

public class MessageHandlerCreator {

    public static MessageHandler create(Broadcaster broadcaster) {
        var atmosphereMessageSender = MessageSender.create(broadcaster);
        var room = RoomCreator.create(atmosphereMessageSender);
        var messageMapper = MessageMapperCreator.create();
        return new MessageHandlerImpl(
                room,
                atmosphereMessageSender,
                messageMapper);
    }
}