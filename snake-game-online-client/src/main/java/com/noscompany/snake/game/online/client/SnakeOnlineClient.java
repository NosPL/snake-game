package com.noscompany.snake.game.online.client;

import snake.game.core.dto.*;

public interface SnakeOnlineClient {

    SnakeOnlineClient connect(String roomName);

    SnakeOnlineClient enterTheRoom(String userName);

    SnakeOnlineClient takeASeat(SnakeNumber snakeNumberNumber);

    SnakeOnlineClient freeUpASeat();

    SnakeOnlineClient changeGameOptions(GridSize gridSize, GameSpeed gameSpeed, Walls walls);

    SnakeOnlineClient startGame();

    SnakeOnlineClient changeSnakeDirection(Direction direction);

    SnakeOnlineClient cancelGame();

    SnakeOnlineClient pauseGame();

    SnakeOnlineClient resumeGame();

    SnakeOnlineClient sendChatMessage(String message);

    SnakeOnlineClient disconnect();

    boolean isConnected();
}