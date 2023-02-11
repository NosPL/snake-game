package com.noscompany.snake.game.online.host.room;

import com.noscompany.snake.game.online.contract.messages.room.PlayersLimit;
import com.noscompany.snake.game.online.host.room.internal.chat.ChatCreator;
import com.noscompany.snake.game.online.host.room.internal.lobby.LobbyCreator;
import com.noscompany.snake.game.online.host.room.internal.user.registry.UserRegistryCreator;
import snake.game.gameplay.SnakeGameplayCreator;
import snake.game.gameplay.SnakeGameplayEventHandler;

class RoomCreatorImpl implements RoomCreator{

    public Room createRoom(SnakeGameplayEventHandler snakeGameplayEventHandler,
                     SnakeGameplayCreator snakeGameplayCreator,
                     PlayersLimit playersLimit) {
        return new RoomImpl(
                UserRegistryCreator.create(playersLimit),
                LobbyCreator.create(snakeGameplayEventHandler, snakeGameplayCreator),
                ChatCreator.create());
    }

}