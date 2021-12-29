package com.noscompany.snake.game.online.server.room.message.handler.mapper;

public class MessageMapperCreator {

    public static MessageMapper create() {
        return new MessageMapperImpl();
    }
}
