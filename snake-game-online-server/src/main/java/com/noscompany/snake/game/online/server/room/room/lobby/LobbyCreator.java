package com.noscompany.snake.game.online.server.room.room.lobby;

import io.vavr.control.Option;
import snake.game.core.SnakeGameConfiguration;
import snake.game.core.SnakeGameEventHandler;
import snake.game.core.dto.GameSpeed;
import snake.game.core.dto.GridSize;
import snake.game.core.dto.Walls;

import java.util.HashMap;

public class LobbyCreator {

    public static Lobby create(SnakeGameEventHandler eventHandler,
                               SnakeGameConfiguration snakeGameConfiguration) {
        return new LobbyImpl(
                new HashMap<>(),
                GridSize._10x10,
                GameSpeed.x1,
                Walls.OFF,
                snakeGameConfiguration,
                new NullGame(GridSize._10x10),
                Option.none(),
                eventHandler);
    }
}