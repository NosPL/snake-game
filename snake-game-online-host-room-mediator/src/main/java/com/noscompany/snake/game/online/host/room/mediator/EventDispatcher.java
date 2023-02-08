package com.noscompany.snake.game.online.host.room.mediator;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.contract.messages.lobby.event.*;
import com.noscompany.snake.game.online.contract.messages.room.FailedToEnterRoom;
import com.noscompany.snake.game.online.contract.messages.room.NewUserEnteredRoom;
import com.noscompany.snake.game.online.contract.messages.room.UserLeftRoom;
import com.noscompany.snake.game.online.host.RoomEventHandlerForHost;
import com.noscompany.snake.game.online.host.server.RoomEventHandlerForRemoteClients;
import com.noscompany.snake.game.online.host.server.ports.RoomApiForRemoteClients;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class EventDispatcher {
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

    void sendToClient(RoomApiForRemoteClients.RemoteClientId remoteClientId, OnlineMessage onlineMessage) {
        remoteClients.sendToClientWithId(remoteClientId, onlineMessage);
    }

    public void sendToClientsAndHost(UserLeftRoom event) {
        remoteClients.sendToAllClients(event);
        host.handle(event);
    }

    public void sendToHost(FailedToEnterRoom failedToEnterRoom) {
        host.handle(failedToEnterRoom);
    }
}