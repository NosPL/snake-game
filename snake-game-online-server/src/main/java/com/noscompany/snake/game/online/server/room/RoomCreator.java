package com.noscompany.snake.game.online.server.room;

import com.noscompany.snake.game.online.server.room.lobby.GameLobbyCreator;
import com.noscompany.snake.game.online.server.room.users.UsersCreator;
import snake.game.core.SnakeGameEventHandler;

public class RoomCreator {
    public static Room create(SnakeGameEventHandler snakeGameEventHandler) {
        return new RoomImpl(
                UsersCreator.create(),
                GameLobbyCreator.gameLobby(snakeGameEventHandler));
    }
}