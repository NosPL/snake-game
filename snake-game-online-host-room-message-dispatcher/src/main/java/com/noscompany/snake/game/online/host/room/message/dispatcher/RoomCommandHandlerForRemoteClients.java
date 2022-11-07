package com.noscompany.snake.game.online.host.room.message.dispatcher;

import com.noscompany.snake.game.online.contract.messages.game.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.game.dto.GameOptions;
import com.noscompany.snake.game.online.contract.messages.game.dto.PlayerNumber;
import com.noscompany.snake.game.online.host.room.message.dispatcher.dto.RemoteClientId;

public interface RoomCommandHandlerForRemoteClients {
    void sendChatMessage(RemoteClientId remoteClientId, String messageContent);

    void cancelGame(RemoteClientId remoteClientId);

    void changeDirection(RemoteClientId remoteClientId, Direction direction);

    void pauseGame(RemoteClientId remoteClientId);

    void resumeGame(RemoteClientId remoteClientId);

    void startGame(RemoteClientId remoteClientId);

    void changeGameOptions(RemoteClientId remoteClientId, GameOptions gameOptions);

    void freeUpSeat(RemoteClientId remoteClientId);

    void takeASeat(RemoteClientId remoteClientId, PlayerNumber playerNumber);

    void enterRoom(RemoteClientId remoteClientId, String userName);

    void removeClient(RemoteClientId remoteClientId);
}
