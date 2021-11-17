package com.noscompany.snake.game.server;

import com.noscompany.snake.game.commons.messages.events.lobby.ChatMessageReceived;
import com.noscompany.snake.game.server.lobby.GameLobby;
import com.noscompany.snake.game.server.message.sender.MessageSender;
import lombok.AllArgsConstructor;
import snake.game.core.dto.*;

import static com.noscompany.snake.game.server.ServerError.SERVER_NOT_RUNNING;

@AllArgsConstructor
class SnakeServerImpl implements SnakeServer {
    private SnakeServer snakeServer;

    static SnakeServer runningServer(
            String userId,
            GameLobby gameLobby,
            SnakeServerEventHandler eventHandler,
            MessageSender messageSender) {
        return new SnakeServerImpl(new RunningServer(userId, gameLobby, eventHandler, messageSender));
    }

    @Override
    public SnakeServer takeASeat(SnakeNumber snakeNumberNumber) {
        try {
            snakeServer = snakeServer.takeASeat(snakeNumberNumber);
            return this;
        } catch (Exception e) {
            return closeServer();
        }
    }

    @Override
    public SnakeServer freeUpASeat() {
        try {
            snakeServer = snakeServer.freeUpASeat();
            return this;
        } catch (Exception e) {
            return closeServer();
        }
    }

    @Override
    public SnakeServer changeGameOptions(GridSize gridSize, GameSpeed gameSpeed, Walls walls) {
        try {
            snakeServer = snakeServer.changeGameOptions(gridSize, gameSpeed, walls);
            return this;
        } catch (Exception e) {
            return closeServer();
        }
    }

    @Override
    public SnakeServer startGame() {
        try {
            snakeServer = snakeServer.startGame();
            return this;
        } catch (Exception e) {
            return closeServer();
        }
    }

    @Override
    public SnakeServer changeSnakeDirection(Direction direction) {
        try {
            snakeServer = snakeServer.changeSnakeDirection(direction);
            return this;
        } catch (Exception e) {
            return closeServer();
        }
    }

    @Override
    public SnakeServer cancelGame() {
        try {
            snakeServer = snakeServer.cancelGame();
            return this;
        } catch (Exception e) {
            return closeServer();
        }
    }

    @Override
    public SnakeServer pauseGame() {
        try {
            snakeServer = snakeServer.pauseGame();
            return this;
        } catch (Exception e) {
            return closeServer();
        }
    }

    @Override
    public SnakeServer resumeGame() {
        try {
            snakeServer = snakeServer.resumeGame();
            return this;
        } catch (Exception e) {
            return closeServer();
        }
    }

    @Override
    public SnakeServer sendChatMessage(String messageContent) {
        try {
            snakeServer = snakeServer.sendChatMessage(messageContent);
            return this;
        } catch (Exception e) {
            return closeServer();
        }
    }

    @Override
    public SnakeServer closeServer() {
        snakeServer = snakeServer.closeServer();
        return this;
    }

    @AllArgsConstructor
    private static class RunningServer implements SnakeServer {
        private final String userId;
        private final GameLobby gameLobby;
        private final SnakeServerEventHandler eventHandler;
        private final MessageSender messageSender;

        @Override
        public SnakeServer takeASeat(SnakeNumber snakeNumberNumber) {
            gameLobby.takeASeat(userId, snakeNumberNumber)
                    .peek(messageSender::sendToAll)
                    .peek(eventHandler::handle)
                    .peekLeft(eventHandler::handle);
            return this;
        }

        @Override
        public SnakeServer freeUpASeat() {
            gameLobby.freeUpASeat(userId)
                    .peek(messageSender::sendToAll)
                    .peek(eventHandler::handle);
            return this;
        }

        @Override
        public SnakeServer changeGameOptions(GridSize gridSize, GameSpeed gameSpeed, Walls walls) {
            gameLobby.changeGameOptions(userId, gridSize, gameSpeed, walls)
                    .peek(messageSender::sendToAll)
                    .peek(eventHandler::handle)
                    .peekLeft(eventHandler::handle);
            return this;
        }

        @Override
        public SnakeServer startGame() {
            gameLobby
                    .startGame(userId)
                    .peek(eventHandler::handle);
            return this;
        }

        @Override
        public SnakeServer changeSnakeDirection(Direction direction) {
            gameLobby.changeSnakeDirection(userId, direction);
            return this;
        }

        @Override
        public SnakeServer cancelGame() {
            gameLobby.cancelGame(userId);
            return this;
        }

        @Override
        public SnakeServer pauseGame() {
            gameLobby.pauseGame(userId);
            return this;
        }

        @Override
        public SnakeServer resumeGame() {
            gameLobby.resumeGame(userId);
            return this;
        }

        @Override
        public SnakeServer sendChatMessage(String messageContent) {
            var event = new ChatMessageReceived(userId, messageContent);
            messageSender.sendToAll(event);
            eventHandler.handle(event);
            return this;
        }

        @Override
        public SnakeServer closeServer() {
            try {
                gameLobby.cancelGame();
                eventHandler.serverClosed();
            } catch (Exception e) {
            }
            return new ClosedServer(eventHandler);
        }
    }

    @AllArgsConstructor
    private static class ClosedServer implements SnakeServer {
        private final SnakeServerEventHandler eventHandler;

        @Override
        public SnakeServer takeASeat(SnakeNumber snakeNumberNumber) {
            eventHandler.handle(SERVER_NOT_RUNNING);
            return this;
        }

        @Override
        public SnakeServer freeUpASeat() {
            eventHandler.handle(SERVER_NOT_RUNNING);
            return this;
        }

        @Override
        public SnakeServer changeGameOptions(GridSize gridSize, GameSpeed gameSpeed, Walls walls) {
            eventHandler.handle(SERVER_NOT_RUNNING);
            return this;
        }

        @Override
        public SnakeServer startGame() {
            eventHandler.handle(SERVER_NOT_RUNNING);
            return this;
        }

        @Override
        public SnakeServer changeSnakeDirection(Direction direction) {
            eventHandler.handle(SERVER_NOT_RUNNING);
            return this;
        }

        @Override
        public SnakeServer cancelGame() {
            eventHandler.handle(SERVER_NOT_RUNNING);
            return this;
        }

        @Override
        public SnakeServer pauseGame() {
            eventHandler.handle(SERVER_NOT_RUNNING);
            return this;
        }

        @Override
        public SnakeServer resumeGame() {
            eventHandler.handle(SERVER_NOT_RUNNING);
            return this;
        }

        @Override
        public SnakeServer sendChatMessage(String messageContent) {
            eventHandler.handle(SERVER_NOT_RUNNING);
            return this;
        }

        @Override
        public SnakeServer closeServer() {
            eventHandler.handle(SERVER_NOT_RUNNING);
            return this;
        }
    }
}