package com.noscompany.snake.game.online.host.room;

import com.noscompany.snake.game.online.contract.messages.room.UsersCountLimit;
import snake.game.gameplay.GameplayCreator;
import snake.game.gameplay.ports.GameplayEventHandler;

public interface RoomCreator {
    Room createRoom(GameplayEventHandler gameplayEventHandler,
                    GameplayCreator gameplayCreator,
                    UsersCountLimit usersCountLimit);

}