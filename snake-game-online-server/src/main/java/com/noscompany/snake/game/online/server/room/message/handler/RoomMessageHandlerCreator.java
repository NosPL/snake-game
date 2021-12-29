package com.noscompany.snake.game.online.server.room.message.handler;

import com.noscompany.snake.game.online.server.room.message.handler.mapper.MessageMapper;
import com.noscompany.snake.game.online.server.room.message.handler.mapper.MessageMapperCreator;
import com.noscompany.snake.game.online.server.room.message.handler.sender.MessageSender;
import com.noscompany.snake.game.online.server.room.message.handler.sender.MessageSenderCreator;
import com.noscompany.snake.game.online.server.room.room.Room;
import com.noscompany.snake.game.online.server.room.room.RoomCreator;
import org.atmosphere.cpr.Broadcaster;

public class RoomMessageHandlerCreator {

    public static RoomMessageHandler create(Broadcaster broadcaster) {
        MessageSender messageSender = MessageSenderCreator.create(broadcaster);
        var room = RoomCreator.create(messageSender);
        var messageMapper = MessageMapperCreator.create();
        return new RoomMessageHandlerImpl(
                room,
                messageSender,
                messageMapper);
    }

    public static RoomMessageHandler create(Broadcaster broadcaster, Room room) {
        MessageMapper messageMapper = MessageMapperCreator.create();
        MessageSender messageSender = MessageSenderCreator.create(broadcaster);
        return new RoomMessageHandlerImpl(room, messageSender, messageMapper);
    }
}