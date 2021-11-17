package com.noscompany.snake.game.server.local.api;

import lombok.AllArgsConstructor;
import snake.game.core.dto.*;

@AllArgsConstructor
class NotRunningServer implements SnakeServer {
    private final SnakeServerEventHandler eventHandler;

    @Override
    public SnakeServer addUser(String userId) {
        eventHandler.handle(ServerError.SERVER_NOT_RUNNING);
        return this;
    }

    @Override
    public SnakeServer removeUser(String userId) {
        eventHandler.handle(ServerError.SERVER_NOT_RUNNING);
        return this;
    }

    @Override
    public SnakeServer startServer(String ipv4Address, String port) {
        return RunningServerCreator
                .create(ipv4Address, port, eventHandler)
                .peek(runningServer -> eventHandler.serverStarted())
                .getOrElse(this);
    }

    @Override
    public SnakeServer takeASeat(String userId, SnakeNumber snakeNumberNumber) {
        eventHandler.handle(ServerError.SERVER_NOT_RUNNING);
        return this;
    }

    @Override
    public SnakeServer freeUpASeat(String userId) {
        eventHandler.handle(ServerError.SERVER_NOT_RUNNING);
        return this;
    }

    @Override
    public SnakeServer changeGameOptions(String userId, GridSize gridSize, GameSpeed gameSpeed, Walls walls) {
        eventHandler.handle(ServerError.SERVER_NOT_RUNNING);
        return this;
    }

    @Override
    public SnakeServer startGame(String userId) {
        eventHandler.handle(ServerError.SERVER_NOT_RUNNING);
        return this;
    }

    @Override
    public SnakeServer changeSnakeDirection(String userId, Direction direction) {
        eventHandler.handle(ServerError.SERVER_NOT_RUNNING);
        return this;
    }

    @Override
    public SnakeServer cancelGame(String userId) {
        eventHandler.handle(ServerError.SERVER_NOT_RUNNING);
        return this;
    }

    @Override
    public SnakeServer pauseGame(String userId) {
        eventHandler.handle(ServerError.SERVER_NOT_RUNNING);
        return this;
    }

    @Override
    public SnakeServer resumeGame(String userId) {
        eventHandler.handle(ServerError.SERVER_NOT_RUNNING);
        return this;
    }

    @Override
    public SnakeServer sendChatMessage(String userId, String messageContent) {
        eventHandler.handle(ServerError.SERVER_NOT_RUNNING);
        return this;
    }

    @Override
    public SnakeServer closeServer() {
        eventHandler.handle(ServerError.SERVER_NOT_RUNNING);
        return this;
    }

    @Override
    public boolean isRunning() {
        return false;
    }
}