package com.noscompany.snake.game.online.client;

import com.noscompany.snake.game.online.contract.messages.game.dto.*;

public interface SnakeOnlineClient {

    void connect(String roomName);

    void enterTheRoom(String userName);

    void takeASeat(PlayerNumber playerNumber);

    void freeUpASeat();

    void changeGameOptions(GridSize gridSize, GameSpeed gameSpeed, Walls walls);

    void startGame();

    void changeSnakeDirection(Direction direction);

    void cancelGame();

    void pauseGame();

    void resumeGame();

    void sendChatMessage(String message);

    void disconnect();

    boolean isConnected();
}