package com.noscompany.snake.game.server;

import snake.game.core.dto.*;

public interface SnakeServer {
    SnakeServer takeASeat(SnakeNumber snakeNumberNumber);

    SnakeServer freeUpASeat();

    SnakeServer changeGameOptions(GridSize gridSize, GameSpeed gameSpeed, Walls walls);

    SnakeServer startGame();

    SnakeServer changeSnakeDirection(Direction direction);

    SnakeServer cancelGame();

    SnakeServer pauseGame();

    SnakeServer resumeGame();

    SnakeServer sendChatMessage(String messageContent);

    SnakeServer closeServer();
}
