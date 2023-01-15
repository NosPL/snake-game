package com.noscompany.snake.game.online.host.room;

import com.noscompany.snake.game.online.host.room.internal.chat.ChatCreator;
import com.noscompany.snake.game.online.host.room.internal.lobby.LobbyCreator;
import com.noscompany.snake.game.online.host.room.internal.user.registry.UserRegistryCreator;
import dagger.Module;
import dagger.Provides;
import snake.game.gameplay.SnakeGameEventHandler;
import snake.game.gameplay.SnakeGameplayBuilder;

@Module
public class RoomConfiguration {

    @Provides
    public Room create(SnakeGameEventHandler snakeGameEventHandler, RoomCreator.PlayersLimit playersLimit, SnakeGameplayBuilder snakeGameplayBuilder) {
        return new RoomImpl(
                UserRegistryCreator.create(playersLimit),
                LobbyCreator.create(snakeGameEventHandler, snakeGameplayBuilder),
                ChatCreator.create());
    }
}