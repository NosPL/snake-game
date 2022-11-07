package com.noscompany.snake.game.online.host.server.message.handler.internal.serializer;

public class MessageMapperCreator {

    public static MessageMapper create() {
        return new MessageMapperImpl();
    }
}
