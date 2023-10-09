package com.noscompany.snake.game.online.client;

import com.noscompany.snake.game.online.contract.messages.gameplay.dto.*;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;

public interface SnakeOnlineClient {

    void connect(HostAddress hostAddress);

    void enterTheRoom(UserName userName);

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