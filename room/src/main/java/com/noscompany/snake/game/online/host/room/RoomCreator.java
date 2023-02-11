package com.noscompany.snake.game.online.host.room;

import com.noscompany.snake.game.online.contract.messages.room.PlayersLimit;
import snake.game.gameplay.SnakeGameplayCreator;
import snake.game.gameplay.SnakeGameplayEventHandler;

public interface RoomCreator {
    Room createRoom(SnakeGameplayEventHandler snakeGameplayEventHandler,
                           SnakeGameplayCreator snakeGameplayCreator,
                           PlayersLimit playersLimit);

}