package com.noscompany.snake.game.online.host.room.internal.lobby;

import com.noscompany.snake.game.online.contract.messages.game.dto.GameSpeed;
import snake.game.gameplay.SnakeGameplay;
import snake.game.gameplay.SnakeGameEventHandler;
import com.noscompany.snake.game.online.contract.messages.game.dto.GameOptions;
import com.noscompany.snake.game.online.contract.messages.game.dto.GridSize;
import com.noscompany.snake.game.online.contract.messages.game.dto.Walls;
import snake.game.gameplay.SnakeGameplayBuilder;

import java.util.Set;

public class LobbyCreator {

    public static Lobby create(SnakeGameEventHandler eventHandler,
                               SnakeGameplayBuilder snakeGameplayBuilder) {
        GameCreator gameCreator = new GameCreator(snakeGameplayBuilder, eventHandler);
        GameOptions gameOptions = new GameOptions(GridSize._10x10, GameSpeed.x1, Walls.ON);
        SnakeGameplay snakeGame = gameCreator.createGame(Set.of(), gameOptions);
        return new Lobby(
                SeatsCreator.create(),
                gameCreator,
                gameOptions,
                snakeGame);
    }
}