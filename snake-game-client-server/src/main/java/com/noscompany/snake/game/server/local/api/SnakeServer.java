package com.noscompany.snake.game.server.local.api;

import snake.game.core.dto.*;

public interface SnakeServer {

    SnakeServer addUser(String userId);

    SnakeServer removeUser(String userId);

    SnakeServer startServer(String ipv4Address, String port);

    SnakeServer takeASeat(String userId, SnakeNumber snakeNumberNumber);

    SnakeServer freeUpASeat(String userId);

    SnakeServer changeGameOptions(String userId, GridSize gridSize, GameSpeed gameSpeed, Walls walls);

    SnakeServer startGame(String userId);

    SnakeServer changeSnakeDirection(String userId, Direction direction);

    SnakeServer pauseGame(String userId);

    SnakeServer resumeGame(String userId);

    SnakeServer cancelGame(String userId);

    SnakeServer sendChatMessage(String userId, String messageContent);

    SnakeServer closeServer();

    boolean isRunning();
}