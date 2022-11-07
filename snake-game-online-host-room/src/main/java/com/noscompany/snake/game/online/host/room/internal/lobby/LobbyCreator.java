package com.noscompany.snake.game.online.host.room.internal.lobby;

import com.noscompany.snake.game.online.contract.messages.game.dto.GameSpeed;
import com.noscompany.snake.game.online.host.room.internal.lobby.internal.seats.SeatsCreator;
import snake.game.gameplay.SnakeGame;
import snake.game.gameplay.SnakeGameCreator;
import snake.game.gameplay.SnakeGameEventHandler;
import com.noscompany.snake.game.online.contract.messages.game.dto.GameOptions;
import com.noscompany.snake.game.online.contract.messages.game.dto.GridSize;
import com.noscompany.snake.game.online.contract.messages.game.dto.Walls;

import java.util.Set;

public class LobbyCreator {

    public static Lobby create(SnakeGameEventHandler eventHandler,
                               SnakeGameCreator snakeGameCreator) {
        GameCreator gameCreator = new GameCreator(snakeGameCreator, eventHandler);
        GameOptions gameOptions = new GameOptions(GridSize._10x10, GameSpeed.x1, Walls.ON);
        SnakeGame snakeGame = gameCreator.createGame(Set.of(), gameOptions);
        return new LobbyImpl(
                SeatsCreator.create(),
                gameCreator,
                gameOptions,
                snakeGame);
    }
}