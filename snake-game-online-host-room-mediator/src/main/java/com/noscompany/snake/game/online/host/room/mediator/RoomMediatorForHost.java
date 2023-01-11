package com.noscompany.snake.game.online.host.room.mediator;

import com.noscompany.snake.game.online.contract.messages.game.dto.*;
import com.noscompany.snake.game.online.host.room.mediator.dto.HostId;

public interface RoomMediatorForHost {
    void enter(HostId hostId, PlayerName playerName);
    void sendChatMessage(HostId hostId, String messageContent);
    void cancelGame(HostId hostId);
    void changeSnakeDirection(HostId hostId, Direction direction);
    void pauseGame(HostId hostId);
    void resumeGame(HostId hostId);
    void startGame(HostId hostId);
    void changeGameOptions(HostId hostId, GameOptions gameOptions);
    void freeUpASeat(HostId hostId);
    void takeASeat(HostId hostId, PlayerNumber playerNumber);
}
