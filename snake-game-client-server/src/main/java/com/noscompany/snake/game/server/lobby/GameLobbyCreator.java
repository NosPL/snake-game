package com.noscompany.snake.game.server.lobby;

import com.noscompany.snake.game.server.message.sender.MessageSender;
import io.vavr.control.Option;
import snake.game.core.SnakeGameEventHandler;
import snake.game.core.dto.GameSpeed;
import snake.game.core.dto.GridSize;
import snake.game.core.dto.Walls;

import java.util.HashMap;

public class GameLobbyCreator {

    public static GameLobby gameLobby(SnakeGameEventHandler eventHandler,
                                      MessageSender messageSender) {
        var gameLobby = new GameLobbyImpl(
                new HashMap<>(),
                GridSize._10x10,
                GameSpeed.x1,
                Walls.OFF,
                new NullGame(GridSize._10x10),
                Option.none(),
                new SendToAllEventsDecorator(eventHandler, messageSender));
        return gameLobby;
    }
}