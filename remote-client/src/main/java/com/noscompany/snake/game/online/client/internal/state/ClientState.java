package com.noscompany.snake.game.online.client.internal.state;

import com.noscompany.snake.game.online.client.HostAddress;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.*;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;

public interface ClientState {

    ClientState connect(HostAddress hostAddress);

    ClientState enterTheRoom(UserName userName);

    ClientState takeASeat(PlayerNumber playerNumber);

    ClientState freeUpASeat();

    ClientState changeGameOptions(GridSize gridSize, GameSpeed gameSpeed, Walls walls);

    ClientState startGame();

    ClientState changeSnakeDirection(Direction direction);

    ClientState cancelGame();

    ClientState pauseGame();

    ClientState resumeGame();

    ClientState sendChatMessage(String message);

    ClientState closeConnection();

    boolean isConnected();
}