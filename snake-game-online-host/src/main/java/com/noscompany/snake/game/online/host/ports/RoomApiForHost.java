package com.noscompany.snake.game.online.host.ports;

import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.*;
import com.noscompany.snake.game.online.contract.messages.room.PlayerName;
import lombok.Value;

public interface RoomApiForHost {
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

    @Value
    class HostId {
        String id;
    }
}
