package com.noscompany.snake.game.online.server.room.room;

import com.noscompany.snake.game.online.server.room.room.lobby.LobbyCreator;
import com.noscompany.snake.game.online.server.room.room.user.registry.UserRegistryCreator;
import snake.game.core.SnakeGameConfiguration;
import snake.game.core.SnakeGameEventHandler;

public class RoomCreator {
    private static final int USERS_LIMIT = 20;

    public static Room create(SnakeGameEventHandler snakeGameEventHandler) {
        return create(snakeGameEventHandler, new SnakeGameConfiguration());
    }

    public static Room create(SnakeGameEventHandler snakeGameEventHandler,
                              SnakeGameConfiguration snakeGameConfiguration) {
        return new RoomImpl(
                USERS_LIMIT,
                UserRegistryCreator.create(),
                LobbyCreator.create(snakeGameEventHandler, snakeGameConfiguration));
    }
}