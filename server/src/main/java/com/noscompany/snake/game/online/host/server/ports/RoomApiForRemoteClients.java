package com.noscompany.snake.game.online.host.server.ports;

import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.room.PlayerName;
import com.noscompany.snake.game.online.contract.messages.room.RoomState;
import lombok.Value;

public interface RoomApiForRemoteClients {
    void sendChatMessage(RemoteClientId remoteClientId, String messageContent);
    void cancelGame(RemoteClientId remoteClientId);
    void changeDirection(RemoteClientId remoteClientId, Direction direction);
    void pauseGame(RemoteClientId remoteClientId);
    void resumeGame(RemoteClientId remoteClientId);
    void startGame(RemoteClientId remoteClientId);
    void changeGameOptions(RemoteClientId remoteClientId, GameOptions gameOptions);
    void freeUpSeat(RemoteClientId remoteClientId);
    void takeASeat(RemoteClientId remoteClientId, PlayerNumber playerNumber);
    void enterRoom(RemoteClientId remoteClientId, PlayerName playerName);
    void removeClient(RemoteClientId remoteClientId);

    RoomState getRoomState();

    @Value
    class RemoteClientId {
        String id;
    }
}
