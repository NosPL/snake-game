package com.noscompany.snake.game.server.local.api;

import lombok.AllArgsConstructor;
import snake.game.core.dto.*;

@AllArgsConstructor
class SnakeServerImpl implements SnakeServer {
    private SnakeServer snakeServerState;

    @Override
    public SnakeServer addUser(String userId) {
        snakeServerState = snakeServerState.addUser(userId);
        return this;
    }

    @Override
    public SnakeServer removeUser(String userId) {
        snakeServerState = snakeServerState.removeUser(userId);
        return this;
    }

    @Override
    public SnakeServer startServer(String ipv4Address, String port) {
        snakeServerState = snakeServerState.startServer(ipv4Address, port);
        return this;
    }

    @Override
    public SnakeServer takeASeat(String userId, SnakeNumber snakeNumberNumber) {
        try {
            snakeServerState = snakeServerState.takeASeat(userId, snakeNumberNumber);
            return this;
        } catch (Exception e) {
            return closeServer();
        }
    }

    @Override
    public SnakeServer freeUpASeat(String userId) {
        try {
            snakeServerState = snakeServerState.freeUpASeat(userId);
            return this;
        } catch (Exception e) {
            return closeServer();
        }
    }

    @Override
    public SnakeServer changeGameOptions(String userId, GridSize gridSize, GameSpeed gameSpeed, Walls walls) {
        try {
            snakeServerState = snakeServerState.changeGameOptions(userId, gridSize, gameSpeed, walls);
            return this;
        } catch (Exception e) {
            return closeServer();
        }
    }

    @Override
    public SnakeServer startGame(String userId) {
        try {
            snakeServerState = snakeServerState.startGame(userId);
            return this;
        } catch (Exception e) {
            return closeServer();
        }
    }

    @Override
    public SnakeServer changeSnakeDirection(String userId, Direction direction) {
        try {
            snakeServerState = snakeServerState.changeSnakeDirection(userId, direction);
            return this;
        } catch (Exception e) {
            return closeServer();
        }
    }

    @Override
    public SnakeServer cancelGame(String userId) {
        try {
            snakeServerState = snakeServerState.cancelGame(userId);
            return this;
        } catch (Exception e) {
            return closeServer();
        }
    }

    @Override
    public SnakeServer pauseGame(String userId) {
        try {
            snakeServerState = snakeServerState.pauseGame(userId);
            return this;
        } catch (Exception e) {
            return closeServer();
        }
    }

    @Override
    public SnakeServer resumeGame(String userId) {
        try {
            snakeServerState = snakeServerState.resumeGame(userId);
            return this;
        } catch (Exception e) {
            return closeServer();
        }
    }

    @Override
    public SnakeServer sendChatMessage(String userId, String messageContent) {
        try {
            snakeServerState = snakeServerState.sendChatMessage(userId, messageContent);
            return this;
        } catch (Exception e) {
            return closeServer();
        }
    }

    @Override
    public SnakeServer closeServer() {
        snakeServerState = snakeServerState.closeServer();
        return this;
    }

    @Override
    public boolean isRunning() {
        return snakeServerState.isRunning();
    }
}