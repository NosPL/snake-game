package com.noscompany.snake.game.online.host.room.internal.playground;

import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GameSpeed;
import snake.game.gameplay.SnakeGameplay;
import snake.game.gameplay.SnakeGameplayCreator;
import snake.game.gameplay.SnakeGameplayEventHandler;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GridSize;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Walls;

import java.util.Set;

public class PlaygroundCreator {

    public static Playground create(SnakeGameplayEventHandler eventHandler,
                                    SnakeGameplayCreator snakeGameplayCreator) {
        GameCreator gameCreator = new GameCreator(snakeGameplayCreator, eventHandler);
        GameOptions gameOptions = new GameOptions(GridSize._10x10, GameSpeed.x1, Walls.ON);
        SnakeGameplay snakeGame = gameCreator.createGame(Set.of(), gameOptions);
        return new Playground(
                SeatsCreator.create(),
                gameCreator,
                gameOptions,
                snakeGame);
    }
}