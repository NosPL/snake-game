package com.noscompany.snake.game.online.host.room.message.dispatcher;

import com.noscompany.snake.game.online.contract.messages.game.dto.*;

public interface RoomCommandHandlerForHost {
    void startServer(int port);

    void sendChatMessage(String messageContent);

    void cancelGame();

    void changeSnakeDirection(Direction direction);

    void pauseGame();

    void resumeGame();

    void startGame();

    void changeGameOptions(GridSize gridSize, GameSpeed gameSpeed, Walls walls);

    void freeUpASeat();

    void takeASeat(PlayerNumber playerNumber);

    void shutdown();
}
