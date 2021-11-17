package com.noscompany.snake.game.server.local.api;

import com.noscompany.snake.game.commons.messages.events.lobby.ChatMessageReceived;
import com.noscompany.snake.game.server.connected.users.ConnectedUsers;
import com.noscompany.snake.game.server.lobby.GameLobby;
import com.noscompany.snake.game.server.message.sender.MessageSender;
import lombok.AllArgsConstructor;
import org.atmosphere.nettosphere.Nettosphere;
import snake.game.core.dto.*;

@AllArgsConstructor
class RunningServer implements SnakeServer {
    private final GameLobby gameLobby;
    private final SnakeServerEventHandler eventHandler;
    private final MessageSender messageSender;
    private final ConnectedUsers connectedUsers;
    private final Nettosphere nettosphere;

    @Override
    public SnakeServer addUser(String userId) {
        connectedUsers.addNewUser(userId);
        return this;
    }

    @Override
    public SnakeServer removeUser(String userId) {
        connectedUsers.removeUser(userId);
        return this;
    }

    @Override
    public SnakeServer startServer(String ipv4Address, String port) {
        eventHandler.handle(StartingServerError.SERVER_ALREADY_RUNNING);
        return this;
    }



    @Override
    public SnakeServer takeASeat(String userId, SnakeNumber snakeNumberNumber) {
        gameLobby.takeASeat(userId, snakeNumberNumber)
                .peek(messageSender::sendToAll)
                .peek(eventHandler::handle)
                .peekLeft(eventHandler::handle);
        return this;
    }

    @Override
    public SnakeServer freeUpASeat(String userId) {
        gameLobby.freeUpASeat(userId)
                .peek(messageSender::sendToAll)
                .peek(eventHandler::handle);
        return this;
    }

    @Override
    public SnakeServer changeGameOptions(String userId, GridSize gridSize, GameSpeed gameSpeed, Walls walls) {
        gameLobby.changeGameOptions(userId, gridSize, gameSpeed, walls)
                .peek(messageSender::sendToAll)
                .peek(eventHandler::handle)
                .peekLeft(eventHandler::handle);
        return this;
    }

    @Override
    public SnakeServer startGame(String userId) {
        gameLobby
                .startGame(userId)
                .peek(eventHandler::handle);
        return this;
    }

    @Override
    public SnakeServer changeSnakeDirection(String userId, Direction direction) {
        gameLobby.changeSnakeDirection(userId, direction);
        return this;
    }

    @Override
    public SnakeServer cancelGame(String userId) {
        gameLobby.cancelGame(userId);
        return this;
    }

    @Override
    public SnakeServer pauseGame(String userId) {
        gameLobby.pauseGame(userId);
        return this;
    }

    @Override
    public SnakeServer resumeGame(String userId) {
        gameLobby.resumeGame(userId);
        return this;
    }

    @Override
    public SnakeServer sendChatMessage(String userId, String messageContent) {
        var event = new ChatMessageReceived(userId, messageContent);
        messageSender.sendToAll(event);
        eventHandler.handle(event);
        return this;
    }

    @Override
    public SnakeServer closeServer() {
        try {
            nettosphere.stop();
            gameLobby.cancelGame();
            eventHandler.serverClosed();
        } catch (Exception e) {
        }
        return new NotRunningServer(eventHandler);
    }

    @Override
    public boolean isRunning() {
        return true;
    }
}