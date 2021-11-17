package com.noscompany.snake.game.server.atmosphere;

import com.noscompany.snake.game.commons.messages.commands.decoder.CommandDecoder;
import com.noscompany.snake.game.commons.messages.events.lobby.ChatMessageReceived;
import com.noscompany.snake.game.server.connected.users.ConnectedUsers;
import com.noscompany.snake.game.server.lobby.GameLobby;
import com.noscompany.snake.game.server.message.sender.MessageSender;
import com.noscompany.snake.game.server.local.api.SnakeServerEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.config.service.*;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import snake.game.core.dto.GameSpeed;
import snake.game.core.dto.GridSize;
import snake.game.core.dto.SnakeNumber;
import snake.game.core.dto.Walls;


@Slf4j
@ManagedService(path = "/snake")
public class AtmosphereEndpoint {
    private GameLobby gameLobby;
    private MessageSender messageSender;
    private SnakeServerEventHandler eventHandler;
    private ConnectedUsers connectedUsers;
    private CommandDecoder commandDecoder = new CommandDecoder();

    @Ready
    public void onConnect(AtmosphereResource r) {
        log.info("new user connected: " + r.uuid());
        var userId = r.uuid();
        connectedUsers.addNewUser(userId)
                .peek(messageSender::sendToAll)
                .peek(eventHandler::handle);
    }

    @Message
    public void onMessage(AtmosphereResource user, String jsonString) {
        log.info("received message, " + user.uuid() + ": " + jsonString);
        commandDecoder.onChangeSnakeDirection(user, jsonString, gameLobby::changeSnakeDirection);
        commandDecoder.onCancelGame(user, jsonString, gameLobby::cancelGame);
        commandDecoder.onPauseGame(user, jsonString, gameLobby::pauseGame);
        commandDecoder.onResumeGame(user, jsonString, gameLobby::resumeGame);
        commandDecoder.onStartNewGame(user, jsonString, this::startGame);
        commandDecoder.onFreeUpASeat(user, jsonString, this::freeUpASeat);
        commandDecoder.onChangeGameOptions(user, jsonString, this::changeGameOptions);
        commandDecoder.onTakeASeat(user, jsonString, this::takeASeat);
        commandDecoder.onSendChatMessage(user, jsonString, this::ChatMessageReceived);
    }

    @Disconnect
    public void onDisconnect(AtmosphereResourceEvent re) {
        var resource = re.getResource();
        var userId = resource.uuid();
        log.info("user disconnected: " + userId);
        gameLobby.userLeft(userId)
                .peek(messageSender::sendToAll)
                .peek(eventHandler::handle);
        connectedUsers.removeUser(userId)
                .peek(messageSender::sendToAll)
                .peek(eventHandler::handle);
    }

    private void ChatMessageReceived(String messageAuthor, String messageContent) {
        var event = new ChatMessageReceived(messageAuthor, messageContent);
        messageSender.sendToAll(event);
        eventHandler.handle(event);
    }

    private void startGame(String userId) {
        gameLobby
                .startGame(userId)
                .peek(failedToStartGame -> messageSender.sendTo(userId, failedToStartGame));
    }

    private void takeASeat(String userId, SnakeNumber snakeNumber) {
        gameLobby
                .takeASeat(userId, snakeNumber)
                .peek(messageSender::sendToAll)
                .peek(eventHandler::handle)
                .peekLeft(failedToTakeASeat -> messageSender.sendTo(userId, failedToTakeASeat));
    }

    private void freeUpASeat(String userId) {
        gameLobby
                .freeUpASeat(userId)
                .peek(messageSender::sendToAll)
                .peek(eventHandler::handle);
    }

    private void changeGameOptions(String userId, GridSize gridSize, GameSpeed gameSpeed, Walls walls) {
        gameLobby
                .changeGameOptions(userId, gridSize, gameSpeed, walls)
                .peek(messageSender::sendToAll)
                .peek(eventHandler::handle)
                .peekLeft(failedToChangeGameOptions -> messageSender.sendTo(userId, failedToChangeGameOptions));
    }

    public void set(GameLobby gameLobby) {
        this.gameLobby = gameLobby;
    }

    public void set(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public void set(SnakeServerEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    public void set(ConnectedUsers connectedUsers) {
        this.connectedUsers = connectedUsers;
    }
}