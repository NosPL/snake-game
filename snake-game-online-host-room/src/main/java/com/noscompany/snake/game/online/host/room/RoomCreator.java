package com.noscompany.snake.game.online.host.room;

import com.noscompany.snake.game.online.host.room.internal.chat.ChatCreator;
import com.noscompany.snake.game.online.host.room.internal.lobby.LobbyCreator;
import com.noscompany.snake.game.online.host.room.internal.user.registry.UserRegistryCreator;
import snake.game.gameplay.SnakeGameCreator;
import snake.game.gameplay.SnakeGameEventHandler;

public class RoomCreator {
    private static final int USERS_LIMIT = 20;

    public static Room create(SnakeGameEventHandler snakeGameEventHandler) {
        return create(snakeGameEventHandler, new SnakeGameCreator());
    }

    public static Room create(SnakeGameEventHandler snakeGameEventHandler,
                              SnakeGameCreator snakeGameCreator) {
        return new RoomImpl(
                USERS_LIMIT,
                UserRegistryCreator.create(),
                LobbyCreator.create(snakeGameEventHandler, snakeGameCreator),
                ChatCreator.create());
    }
}