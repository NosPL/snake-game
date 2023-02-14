package com.noscompany.snake.game.online.host.room;

import com.noscompany.snake.game.online.contract.messages.room.PlayersLimit;
import com.noscompany.snake.game.online.host.room.internal.chat.ChatCreator;
import com.noscompany.snake.game.online.host.room.internal.playground.PlaygroundCreator;
import com.noscompany.snake.game.online.host.room.internal.user.registry.UserRegistryCreator;
import snake.game.gameplay.GameplayCreator;
import snake.game.gameplay.GameplayEventHandler;

class RoomCreatorImpl implements RoomCreator{

    public Room createRoom(GameplayEventHandler gameplayEventHandler,
                           GameplayCreator gameplayCreator,
                           PlayersLimit playersLimit) {
        return new RoomImpl(
                UserRegistryCreator.create(playersLimit),
                PlaygroundCreator.create(gameplayEventHandler, gameplayCreator),
                ChatCreator.create());
    }

}