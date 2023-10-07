package com.noscompany.snake.game.online.host;

import com.noscompany.snake.game.online.contract.messages.gameplay.dto.*;
import com.noscompany.snake.game.online.contract.messages.room.UserName;
import com.noscompany.snake.game.online.contract.messages.server.ServerParams;

public interface SnakeOnlineHost {
    void startServer(ServerParams serverParams);
    void enterRoom(UserName userName);
    void sendChatMessage(String messageContent);
    void cancelGame();
    void changeSnakeDirection(Direction direction);
    void pauseGame();
    void resumeGame();
    void startGame();
    void changeGameOptions(GridSize gridSize, GameSpeed gameSpeed, Walls walls);
    void freeUpASeat();
    void takeASeat(PlayerNumber playerNumber);
    void shutDownServer();
}