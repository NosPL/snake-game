package com.noscompany.snake.game.online.host.room.internal.playground;

import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GameSpeed;
import snake.game.gameplay.Gameplay;
import snake.game.gameplay.GameplayCreator;
import snake.game.gameplay.ports.GameplayEventHandler;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GridSize;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Walls;

import java.util.Set;

public class PlaygroundCreator {

    public static Playground create(GameplayEventHandler eventHandler,
                                    GameplayCreator gameplayCreator) {
        GameCreatorAdapter gameCreatorAdapter = new GameCreatorAdapter(gameplayCreator, eventHandler);
        GameOptions gameOptions = new GameOptions(GridSize._10x10, GameSpeed.x1, Walls.ON);
        Gameplay snakeGame = gameCreatorAdapter.createGame(Set.of(), gameOptions);
        return new Playground(
                SeatsCreator.create(),
                gameCreatorAdapter,
                gameOptions,
                snakeGame);
    }
}