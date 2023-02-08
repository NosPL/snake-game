package com.noscompany.snake.game.online.host.room.internal.lobby;

import com.noscompany.snake.game.online.contract.messages.game.dto.GameSpeed;
import snake.game.gameplay.SnakeGameplay;
import snake.game.gameplay.SnakeGameplayCreator;
import snake.game.gameplay.SnakeGameplayEventHandler;
import com.noscompany.snake.game.online.contract.messages.game.dto.GameOptions;
import com.noscompany.snake.game.online.contract.messages.game.dto.GridSize;
import com.noscompany.snake.game.online.contract.messages.game.dto.Walls;

import java.util.Set;

public class LobbyCreator {

    public static Lobby create(SnakeGameplayEventHandler eventHandler,
                               SnakeGameplayCreator snakeGameplayCreator) {
        GameCreator gameCreator = new GameCreator(snakeGameplayCreator, eventHandler);
        GameOptions gameOptions = new GameOptions(GridSize._10x10, GameSpeed.x1, Walls.ON);
        SnakeGameplay snakeGame = gameCreator.createGame(Set.of(), gameOptions);
        return new Lobby(
                SeatsCreator.create(),
                gameCreator,
                gameOptions,
                snakeGame);
    }
}