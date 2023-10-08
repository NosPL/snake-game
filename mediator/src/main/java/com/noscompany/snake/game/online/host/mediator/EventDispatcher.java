package com.noscompany.snake.game.online.host.mediator;

import com.noscompany.message.publisher.MessagePublisher;
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
import com.noscompany.snake.game.online.contract.messages.server.FailedToStartServer;
import com.noscompany.snake.game.online.contract.messages.server.ServerGotShutdown;
import com.noscompany.snake.game.online.contract.messages.server.ServerStarted;
import com.noscompany.snake.game.online.host.server.Server;
import com.noscompany.snake.game.online.host.server.dto.RemoteClientId;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import snake.game.gameplay.ports.GameplayEventHandler;

import java.util.Locale;

@AllArgsConstructor
@Slf4j
class EventDispatcher implements GameplayEventHandler {
    private final MessagePublisher messagePublisher;
    private final Server server;

    void sendToClientsAndHost(UserSentChatMessage event) {
        log.info("passing 'user sent chat message' event to server and host");
        server.sendToAllClients(event);
        messagePublisher.publishMessage(event);
    }

    void sendToHost(FailedToSendChatMessage event) {
        log.info("passing 'failed to send chat message' event to host");
        messagePublisher.publishMessage(event);
    }

    void sendToHost(FailedToStartGame event) {
        log.info("passing 'failed to start game' event to host");
        messagePublisher.publishMessage(event);
    }

    void sendToClientsAndHost(GameOptionsChanged event) {
        log.info("passing 'game options changed' event to server and host");
        server.sendToAllClients(event);
        messagePublisher.publishMessage(event);
    }

    void sendToHost(FailedToChangeGameOptions event) {
        log.info("passing 'failed to change game options' event to host");
        messagePublisher.publishMessage(event);
    }

    void sendToClientsAndHost(PlayerFreedUpASeat event) {
        log.info("passing 'player freed up a seat' event to server and host");
        server.sendToAllClients(event);
        messagePublisher.publishMessage(event);
    }

    void sendToHost(FailedToFreeUpSeat event) {
        log.info("passing 'failed to free up a seat' event to host");
        messagePublisher.publishMessage(event);
    }

    void sendToClientsAndHost(PlayerTookASeat event) {
        log.info("passing 'player took a seat' event to server and host");
        server.sendToAllClients(event);
        messagePublisher.publishMessage(event);
    }

    void sendToClientsAndHost(NewUserEnteredRoom event) {
        log.info("passing 'new user entered room' event to server and host");
        server.sendToAllClients(event);
        messagePublisher.publishMessage(event);
    }

    void sendToHost(FailedToTakeASeat event) {
        log.info("passing 'failed to take a seat' event to host");
        messagePublisher.publishMessage(event);
    }

    void sendToClient(RemoteClientId remoteClientId, OnlineMessage onlineMessage) {
        log.info("passing '{}' message to server for remote client with id {}", asString(onlineMessage.getMessageType()), remoteClientId.getId());
        server.sendToClientWithId(remoteClientId, onlineMessage);
    }

    void sendToClientsAndHost(UserLeftRoom event) {
        log.info("passing 'user left room' event to server and host");
        server.sendToAllClients(event);
        messagePublisher.publishMessage(event);
    }

    void sendToHost(FailedToEnterRoom event) {
        log.info("passing 'failed to enter room' event to host");
        messagePublisher.publishMessage(event);
    }

    @Override
    public void handle(GameStartCountdown event) {
        log.info("passing 'time left to game start has changed' event to server and host");
        server.sendToAllClients(event);
        messagePublisher.publishMessage(event);
    }

    @Override
    public void handle(GameStarted event) {
        log.info("passing 'game started' event to server and host");
        server.sendToAllClients(event);
        messagePublisher.publishMessage(event);
    }

    @Override
    public void handle(SnakesMoved event) {
        log.info("passing 'snakes moved' event to server and host");
        server.sendToAllClients(event);
        messagePublisher.publishMessage(event);
    }

    @Override
    public void handle(GameFinished event) {
        log.info("passing 'game finished' event to server and host");
        server.sendToAllClients(event);
        messagePublisher.publishMessage(event);
    }

    @Override
    public void handle(GameCancelled event) {
        log.info("passing 'game cancelled' event to server and host");
        server.sendToAllClients(event);
        messagePublisher.publishMessage(event);
    }

    @Override
    public void handle(GamePaused event) {
        log.info("passing 'game paused' event to server and host");
        server.sendToAllClients(event);
        messagePublisher.publishMessage(event);
    }

    @Override
    public void handle(GameResumed event) {
        log.info("passing 'game resumed' event to server and host");
        server.sendToAllClients(event);
        messagePublisher.publishMessage(event);
    }

    public void sendToHost(ServerStarted event) {
        log.info("passing 'server started' event to host");
        messagePublisher.publishMessage(event);
    }

    public void sendToHost(FailedToStartServer event) {
        log.info("passing 'server failed to start' event to host");
        messagePublisher.publishMessage(event);
    }

    public void sendToClientsAndHost(ServerGotShutdown event) {
        log.info("passing 'server got shutdown' event to host");
        server.sendToAllClients(event);
        messagePublisher.publishMessage(event);
    }

    public void shutdown() {
        server.shutdown();
        messagePublisher.shutdown();
    }

    private String asString(OnlineMessage.MessageType messageType) {
        return messageType.toString().toLowerCase(Locale.ROOT).replace('_', ' ');
    }
}