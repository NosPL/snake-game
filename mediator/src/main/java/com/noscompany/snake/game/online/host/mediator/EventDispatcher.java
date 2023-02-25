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
import com.noscompany.snake.game.online.host.server.Server;
import com.noscompany.snake.game.online.host.server.dto.RemoteClientId;
import lombok.AllArgsConstructor;
import snake.game.gameplay.GameplayEventHandler;

@AllArgsConstructor
class EventDispatcher implements GameplayEventHandler {
    private final RoomEventHandlerForHost host;
    private final Server server;

    void sendToClientsAndHost(UserSentChatMessage event) {
        server.sendToAllClients(event);
        host.handle(event);
    }

    void sendToHost(FailedToSendChatMessage event) {
        host.handle(event);
    }

    void sendToHost(FailedToStartGame event) {
        host.handle(event);
    }

    void sendToClientsAndHost(GameOptionsChanged event) {
        server.sendToAllClients(event);
        host.handle(event);
    }

    void sendToHost(FailedToChangeGameOptions event) {
        host.handle(event);
    }

    void sendToClientsAndHost(PlayerFreedUpASeat event) {
        server.sendToAllClients(event);
        host.handle(event);
    }

    void sendToHost(FailedToFreeUpSeat event) {
        host.handle(event);
    }

    void sendToClientsAndHost(PlayerTookASeat event) {
        server.sendToAllClients(event);
        host.handle(event);
    }

    void sendToClientsAndHost(NewUserEnteredRoom event) {
        server.sendToAllClients(event);
        host.handle(event);
    }

    void sendToHost(FailedToTakeASeat event) {
        host.handle(event);
    }

    void sendToClient(RemoteClientId remoteClientId, OnlineMessage onlineMessage) {
        server.sendToClientWithId(remoteClientId, onlineMessage);
    }

    void sendToClientsAndHost(UserLeftRoom event) {
        server.sendToAllClients(event);
        host.handle(event);
    }

    void sendToHost(FailedToEnterRoom failedToEnterRoom) {
        host.handle(failedToEnterRoom);
    }

    @Override
    public void handle(TimeLeftToGameStartHasChanged event) {
        server.sendToAllClients(event);
        host.handle(event);
    }

    @Override
    public void handle(GameStarted event) {
        server.sendToAllClients(event);
        host.handle(event);
    }

    @Override
    public void handle(SnakesMoved event) {
        server.sendToAllClients(event);
        host.handle(event);
    }

    @Override
    public void handle(GameFinished event) {
        server.sendToAllClients(event);
        host.handle(event);
    }

    @Override
    public void handle(GameCancelled event) {
        server.sendToAllClients(event);
        host.handle(event);
    }

    @Override
    public void handle(GamePaused event) {
        server.sendToAllClients(event);
        host.handle(event);
    }

    @Override
    public void handle(GameResumed event) {
        server.sendToAllClients(event);
        host.handle(event);
    }
}