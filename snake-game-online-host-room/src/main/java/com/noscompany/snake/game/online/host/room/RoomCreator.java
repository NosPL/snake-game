package com.noscompany.snake.game.online.host.room;

import com.noscompany.snake.game.online.host.room.internal.chat.ChatCreator;
import com.noscompany.snake.game.online.host.room.internal.lobby.LobbyCreator;
import com.noscompany.snake.game.online.host.room.internal.user.registry.UserRegistryCreator;
import lombok.Value;
import snake.game.gameplay.SnakeGameEventHandler;
import snake.game.gameplay.SnakeGameplayBuilder;
import snake.game.gameplay.SnakeGameplayConfiguration;

public class RoomCreator {

    public static Room create(SnakeGameEventHandler snakeGameEventHandler) {
        return create(snakeGameEventHandler, new SnakeGameplayConfiguration().snakeGameplayBuilder());
    }

    public static Room create(SnakeGameEventHandler snakeGameEventHandler,
                              SnakeGameplayBuilder gameplayBuilder) {
        return create(snakeGameEventHandler, gameplayBuilder, new PlayersLimit(10));
    }

    public static Room create(SnakeGameEventHandler snakeGameEventHandler,
                              SnakeGameplayBuilder snakeGameplayBuilder,
                              PlayersLimit playersLimit) {
        return new RoomImpl(
                UserRegistryCreator.create(playersLimit),
                LobbyCreator.create(snakeGameEventHandler, snakeGameplayBuilder),
                ChatCreator.create());
    }

    @Value
    public static class PlayersLimit {
        int playersLimit;
    }
}