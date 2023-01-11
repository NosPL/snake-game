package com.noscompany.snake.game.online.host;

import com.noscompany.snake.game.online.contract.messages.game.dto.*;
import com.noscompany.snake.game.online.host.server.dto.IpAddress;
import com.noscompany.snake.game.online.host.server.dto.ServerParams;
import com.noscompany.snake.game.online.host.room.mediator.PlayerName;

public interface SnakeOnlineHost {
    void enter(PlayerName playerName);
    void startServer(ServerParams serverParams, PlayerName playerName);
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