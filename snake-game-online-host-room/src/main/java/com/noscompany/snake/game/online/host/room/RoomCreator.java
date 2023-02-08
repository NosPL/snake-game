package com.noscompany.snake.game.online.host.room;

import com.noscompany.snake.game.online.host.room.internal.chat.ChatCreator;
import com.noscompany.snake.game.online.host.room.internal.lobby.LobbyCreator;
import com.noscompany.snake.game.online.host.room.internal.user.registry.UserRegistryCreator;
import lombok.Value;
import snake.game.gameplay.SnakeGameplayCreator;
import snake.game.gameplay.SnakeGameplayEventHandler;
import snake.game.gameplay.SnakeGameplayConfiguration;

public class RoomCreator {

    public static Room create(SnakeGameplayEventHandler snakeGameplayEventHandler) {
        return create(snakeGameplayEventHandler, new SnakeGameplayConfiguration().snakeGameplayCreator());
    }

    public static Room create(SnakeGameplayEventHandler snakeGameplayEventHandler,
                              SnakeGameplayCreator snakeGameplayCreator) {
        return create(snakeGameplayEventHandler, snakeGameplayCreator, new PlayersLimit(10));
    }

    public static Room create(SnakeGameplayEventHandler snakeGameplayEventHandler,
                              SnakeGameplayCreator snakeGameplayCreator,
                              PlayersLimit playersLimit) {
        return new RoomImpl(
                UserRegistryCreator.create(playersLimit),
                LobbyCreator.create(snakeGameplayEventHandler, snakeGameplayCreator),
                ChatCreator.create());
    }

    @Value
    public static class PlayersLimit {
        int playersLimit;
    }
}