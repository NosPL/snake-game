package com.noscompany.snake.game.online.host.mediator;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptionsChanged;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.*;
import com.noscompany.snake.game.online.contract.messages.room.FailedToEnterRoom;
import com.noscompany.snake.game.online.contract.messages.room.NewUserEnteredRoom;
import com.noscompany.snake.game.online.contract.messages.room.UserLeftRoom;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToFreeUpSeat;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerFreedUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerTookASeat;
import com.noscompany.snake.game.online.host.RoomEventHandlerForHost;
import com.noscompany.snake.game.online.host.server.RoomEventHandlerForRemoteClients;
import com.noscompany.snake.game.online.host.server.dto.RemoteClientId;
import lombok.AllArgsConstructor;
import snake.game.gameplay.GameplayEventHandler;

@AllArgsConstructor
class EventDispatcher implements GameplayEventHandler {
    private final RoomEventHandlerForHost host;
    private final RoomEventHandlerForRemoteClients remoteClients;

    void sendToClientsAndHost(UserSentChatMessage event) {
        remoteClients.sendToAllClients(event);
        host.handle(event);
    }

    void sendToHost(FailedToSendChatMessage event) {
        host.handle(event);
    }

    void sendToHost(FailedToStartGame event) {
        host.handle(event);
    }

    void sendToClientsAndHost(GameOptionsChanged event) {
        remoteClients.sendToAllClients(event);
        host.handle(event);
    }

    void sendToHost(FailedToChangeGameOptions event) {
        host.handle(event);
    }

    void sendToClientsAndHost(PlayerFreedUpASeat event) {
        remoteClients.sendToAllClients(event);
        host.handle(event);
    }

    void sendToHost(FailedToFreeUpSeat event) {
        host.handle(event);
    }

    void sendToClientsAndHost(PlayerTookASeat event) {
        remoteClients.sendToAllClients(event);
        host.handle(event);
    }

    void sendToClientsAndHost(NewUserEnteredRoom event) {
        remoteClients.sendToAllClients(event);
        host.handle(event);
    }

    void sendToHost(FailedToTakeASeat event) {
        host.handle(event);
    }

    void sendToClient(RemoteClientId remoteClientId, OnlineMessage onlineMessage) {
        remoteClients.sendToClientWithId(remoteClientId, onlineMessage);
    }

    void sendToClientsAndHost(UserLeftRoom event) {
        remoteClients.sendToAllClients(event);
        host.handle(event);
    }

    void sendToHost(FailedToEnterRoom failedToEnterRoom) {
        host.handle(failedToEnterRoom);
    }

    @Override
    public void handle(TimeLeftToGameStartHasChanged event) {
        remoteClients.sendToAllClients(event);
        host.handle(event);
    }

    @Override
    public void handle(GameStarted event) {
        remoteClients.sendToAllClients(event);
        host.handle(event);
    }

    @Override
    public void handle(SnakesMoved event) {
        remoteClients.sendToAllClients(event);
        host.handle(event);
    }

    @Override
    public void handle(GameFinished event) {
        remoteClients.sendToAllClients(event);
        host.handle(event);
    }

    @Override
    public void handle(GameCancelled event) {
        remoteClients.sendToAllClients(event);
        host.handle(event);
    }

    @Override
    public void handle(GamePaused event) {
        remoteClients.sendToAllClients(event);
        host.handle(event);
    }

    @Override
    public void handle(GameResumed event) {
        remoteClients.sendToAllClients(event);
        host.handle(event);
    }
}