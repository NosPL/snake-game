package com.noscompany.snake.game.online.host.room;

import com.noscompany.snake.game.online.host.room.internal.chat.ChatCreator;
import com.noscompany.snake.game.online.host.room.internal.lobby.LobbyCreator;
import com.noscompany.snake.game.online.host.room.internal.user.registry.UserRegistryCreator;
import lombok.Value;
import snake.game.gameplay.SnakeGameCreator;
import snake.game.gameplay.SnakeGameEventHandler;

public class RoomCreator {

    public static Room create(SnakeGameEventHandler snakeGameEventHandler) {
        return create(snakeGameEventHandler, new SnakeGameCreator());
    }

    public static Room create(SnakeGameEventHandler snakeGameEventHandler,
                              SnakeGameCreator snakeGameCreator) {
        return create(snakeGameEventHandler, snakeGameCreator, new PlayersLimit(10));
    }

    public static Room create(SnakeGameEventHandler snakeGameEventHandler,
                              SnakeGameCreator snakeGameCreator,
                              PlayersLimit playersLimit) {
        return new RoomImpl(
                UserRegistryCreator.create(playersLimit),
                LobbyCreator.create(snakeGameEventHandler, snakeGameCreator),
                ChatCreator.create());
    }

    @Value
    public static class PlayersLimit {
        int playersLimit;
    }
}