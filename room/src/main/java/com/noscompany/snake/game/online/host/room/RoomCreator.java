package com.noscompany.snake.game.online.host.room;

import com.noscompany.snake.game.online.contract.messages.room.PlayersLimit;
import snake.game.gameplay.GameplayCreator;
import snake.game.gameplay.GameplayEventHandler;

public interface RoomCreator {
    Room createRoom(GameplayEventHandler gameplayEventHandler,
                    GameplayCreator gameplayCreator,
                    PlayersLimit playersLimit);

}