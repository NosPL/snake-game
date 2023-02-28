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
import lombok.extern.slf4j.Slf4j;
import snake.game.gameplay.GameplayEventHandler;

import java.util.Locale;

@AllArgsConstructor
@Slf4j
class EventDispatcher implements GameplayEventHandler {
    private final RoomEventHandlerForHost host;
    private final Server server;

    void sendToClientsAndHost(UserSentChatMessage event) {
        log.info("passing 'user sent chat message' event to server and host");
        server.sendToAllClients(event);
        host.handle(event);
    }

    void sendToHost(FailedToSendChatMessage event) {
        log.info("passing 'failed to send chat message' event to host");
        host.handle(event);
    }

    void sendToHost(FailedToStartGame event) {
        log.info("passing 'failed to start game' event to host");
        host.handle(event);
    }

    void sendToClientsAndHost(GameOptionsChanged event) {
        log.info("passing 'game options changed' event to server and host");
        server.sendToAllClients(event);
        host.handle(event);
    }

    void sendToHost(FailedToChangeGameOptions event) {
        log.info("passing 'failed to change game options' event to host");
        host.handle(event);
    }

    void sendToClientsAndHost(PlayerFreedUpASeat event) {
        log.info("passing 'player freed up a seat' event to server and host");
        server.sendToAllClients(event);
        host.handle(event);
    }

    void sendToHost(FailedToFreeUpSeat event) {
        log.info("passing 'failed to free up a seat' event to host");
        host.handle(event);
    }

    void sendToClientsAndHost(PlayerTookASeat event) {
        log.info("passing 'player took a seat' event to server and host");
        server.sendToAllClients(event);
        host.handle(event);
    }

    void sendToClientsAndHost(NewUserEnteredRoom event) {
        log.info("passing 'new user entered room' event to server and host");
        server.sendToAllClients(event);
        host.handle(event);
    }

    void sendToHost(FailedToTakeASeat event) {
        log.info("passing 'failed to take a seat' event to host");
        host.handle(event);
    }

    void sendToClient(RemoteClientId remoteClientId, OnlineMessage onlineMessage) {
        log.info("passing '{}' message to server for remote client with id {}", asString(onlineMessage.getMessageType()), remoteClientId.getId());
        server.sendToClientWithId(remoteClientId, onlineMessage);
    }

    void sendToClientsAndHost(UserLeftRoom event) {
        log.info("passing 'user left room' event to server and host");
        server.sendToAllClients(event);
        host.handle(event);
    }

    void sendToHost(FailedToEnterRoom failedToEnterRoom) {
        log.info("passing 'failed to enter room' event to host");
        host.handle(failedToEnterRoom);
    }

    @Override
    public void handle(TimeLeftToGameStartHasChanged event) {
        log.info("passing 'time left to game start has changed' event to server and host");
        server.sendToAllClients(event);
        host.handle(event);
    }

    @Override
    public void handle(GameStarted event) {
        log.info("passing 'game started' event to server and host");
        server.sendToAllClients(event);
        host.handle(event);
    }

    @Override
    public void handle(SnakesMoved event) {
        log.info("passing 'snakes moved' event to server and host");
        server.sendToAllClients(event);
        host.handle(event);
    }

    @Override
    public void handle(GameFinished event) {
        log.info("passing 'game finished' event to server and host");
        server.sendToAllClients(event);
        host.handle(event);
    }

    @Override
    public void handle(GameCancelled event) {
        log.info("passing 'game cancelled' event to server and host");
        server.sendToAllClients(event);
        host.handle(event);
    }

    @Override
    public void handle(GamePaused event) {
        log.info("passing 'game paused' event to server and host");
        server.sendToAllClients(event);
        host.handle(event);
    }

    @Override
    public void handle(GameResumed event) {
        log.info("passing 'game resumed' event to server and host");
        server.sendToAllClients(event);
        host.handle(event);
    }

    private String asString(OnlineMessage.MessageType messageType) {
        return messageType.toString().toLowerCase(Locale.ROOT).replace('_', ' ');
    }
}