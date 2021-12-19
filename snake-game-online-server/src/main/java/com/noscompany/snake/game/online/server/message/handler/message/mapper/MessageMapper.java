package com.noscompany.snake.game.online.server.message.handler.message.mapper;

import com.noscompany.snake.game.online.server.message.handler.Message;

public interface MessageMapper {
    Message map(String message);
}
