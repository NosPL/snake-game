package com.noscompany.snake.game.online.server.room.message.handler.mapper;

import com.noscompany.snake.game.online.server.room.message.handler.Message;

public interface MessageMapper {
    Message map(String message);
}
