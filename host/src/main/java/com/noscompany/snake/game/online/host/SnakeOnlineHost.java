package com.noscompany.snake.game.online.host;

import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.*;
import com.noscompany.snake.game.online.host.server.dto.ServerParams;
import com.noscompany.snake.game.online.contract.messages.room.UserName;

public interface SnakeOnlineHost {
    void startServer(ServerParams serverParams, UserName userName);
    void sendChatMessage(String messageContent);
    void cancelGame();
    void changeSnakeDirection(Direction direction);
    void pauseGame();
    void resumeGame();
    void startGame();
    void changeGameOptions(GameOptions gameOptions);
    void changeGameOptions(GridSize gridSize, GameSpeed gameSpeed, Walls walls);
    void freeUpASeat();
    void takeASeat(PlayerNumber playerNumber);
    void shutDownServer();
}