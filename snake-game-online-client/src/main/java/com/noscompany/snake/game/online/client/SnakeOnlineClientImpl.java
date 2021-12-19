package com.noscompany.snake.game.online.client;

import lombok.AllArgsConstructor;
import snake.game.core.dto.*;

@AllArgsConstructor
class SnakeOnlineClientImpl implements SnakeOnlineClient {
    private SnakeOnlineClient snakeOnlineClient;

    @Override
    public SnakeOnlineClient connect(String roomName) {
        snakeOnlineClient = snakeOnlineClient.connect(roomName);
        return this;
    }

    @Override
    public SnakeOnlineClient enterTheRoom(String userName) {
        snakeOnlineClient = snakeOnlineClient.enterTheRoom(userName);
        return this;
    }

    @Override
    public SnakeOnlineClient takeASeat(SnakeNumber snakeNumberNumber) {
        snakeOnlineClient = snakeOnlineClient.takeASeat(snakeNumberNumber);
        return this;
    }

    @Override
    public SnakeOnlineClient freeUpASeat() {
        snakeOnlineClient = snakeOnlineClient.freeUpASeat();
        return this;
    }

    @Override
    public SnakeOnlineClient changeGameOptions(GridSize gridSize, GameSpeed gameSpeed, Walls walls) {
        snakeOnlineClient = snakeOnlineClient.changeGameOptions(gridSize, gameSpeed, walls);
        return this;
    }

    @Override
    public SnakeOnlineClient startGame() {
        snakeOnlineClient = snakeOnlineClient.startGame();
        return this;
    }

    @Override
    public SnakeOnlineClient changeSnakeDirection(Direction direction) {
        snakeOnlineClient = snakeOnlineClient.changeSnakeDirection(direction);
        return this;
    }

    @Override
    public SnakeOnlineClient cancelGame() {
        snakeOnlineClient = snakeOnlineClient.cancelGame();
        return this;
    }

    @Override
    public SnakeOnlineClient pauseGame() {
        snakeOnlineClient = snakeOnlineClient.pauseGame();
        return this;
    }

    @Override
    public SnakeOnlineClient resumeGame() {
        snakeOnlineClient = snakeOnlineClient.resumeGame();
        return this;
    }

    @Override
    public SnakeOnlineClient sendChatMessage(String message) {
        snakeOnlineClient = snakeOnlineClient.sendChatMessage(message);
        return this;
    }

    @Override
    public SnakeOnlineClient disconnect() {
        snakeOnlineClient = snakeOnlineClient.disconnect();
        return this;
    }

    @Override
    public boolean isConnected() {
        return snakeOnlineClient.isConnected();
    }
}