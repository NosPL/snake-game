package com.noscompany.snake.game.online.server.message.handler.message.mapper;

public class MessageMapperCreator {

    public static MessageMapper create() {
        return new MessageMapperImpl();
    }
}
