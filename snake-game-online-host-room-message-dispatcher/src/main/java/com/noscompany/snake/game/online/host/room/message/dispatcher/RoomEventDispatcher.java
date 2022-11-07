package com.noscompany.snake.game.online.host.room.message.dispatcher;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.events.*;
import com.noscompany.snake.game.online.contract.messages.lobby.event.*;
import com.noscompany.snake.game.online.contract.messages.room.NewUserEnteredRoom;
import com.noscompany.snake.game.online.contract.messages.room.UserLeftRoom;
import com.noscompany.snake.game.online.host.room.message.dispatcher.dto.RemoteClientId;
import com.noscompany.snake.game.online.host.room.message.dispatcher.ports.RoomEventHandlerForHost;
import com.noscompany.snake.game.online.host.room.message.dispatcher.ports.RoomEventHandlerForRemoteClients;
import com.noscompany.snake.game.online.host.room.message.dispatcher.ports.Server;
import lombok.AllArgsConstructor;
import snake.game.gameplay.SnakeGameEventHandler;

@AllArgsConstructor
class RoomEventDispatcher implements SnakeGameEventHandler {
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

    void sendToAllRemoteClients(OnlineMessage onlineMessage) {
        remoteClients.sendToAllClients(onlineMessage);
    }

    public void sendToClientsAndHost(UserLeftRoom event) {
        remoteClients.sendToAllClients(event);
        host.handle(event);
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
    public void handle(GameContinues event) {
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

    public void sendToHost(Server.StartError startError) {
        host.handle(startError);
    }
}