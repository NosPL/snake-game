package com.noscompany.snake.game.client;

import lombok.AllArgsConstructor;
import snake.game.core.dto.*;

import static com.noscompany.snake.game.client.ClientError.CLIENT_NOT_CONNECTED;

@AllArgsConstructor
class NotConnectedClient implements SnakeOnlineClient {
    private final ClientEventHandler eventHandler;

    @Override
    public SnakeOnlineClient connect(String ip, String port) {
        return RunningClientCreator
                .startClient(ip, port, eventHandler)
                .peek(runningClient -> eventHandler.connectionEstablished())
                .getOrElse(this);
    }

    @Override
    public SnakeOnlineClient takeASeat(SnakeNumber snakeNumberNumber) {
        eventHandler.handle(CLIENT_NOT_CONNECTED);
        return this;
    }

    @Override
    public SnakeOnlineClient freeUpASeat() {
        eventHandler.handle(CLIENT_NOT_CONNECTED);
        return this;
    }

    @Override
    public SnakeOnlineClient changeGameOptions(GridSize gridSize, GameSpeed gameSpeed, Walls walls) {
        eventHandler.handle(CLIENT_NOT_CONNECTED);
        return this;
    }

    @Override
    public SnakeOnlineClient startGame() {
        eventHandler.handle(CLIENT_NOT_CONNECTED);
        return this;
    }

    @Override
    public SnakeOnlineClient changeSnakeDirection(Direction direction) {
        eventHandler.handle(CLIENT_NOT_CONNECTED);
        return this;
    }

    @Override
    public SnakeOnlineClient cancelGame() {
        eventHandler.handle(CLIENT_NOT_CONNECTED);
        return this;
    }

    @Override
    public SnakeOnlineClient pauseGame() {
        eventHandler.handle(CLIENT_NOT_CONNECTED);
        return this;
    }

    @Override
    public SnakeOnlineClient resumeGame() {
        eventHandler.handle(CLIENT_NOT_CONNECTED);
        return this;
    }

    @Override
    public SnakeOnlineClient sendChatMessage(String message) {
        eventHandler.handle(CLIENT_NOT_CONNECTED);
        return this;
    }

    @Override
    public SnakeOnlineClient disconnect() {
        eventHandler.handle(CLIENT_NOT_CONNECTED);
        return this;
    }

    @Override
    public boolean isConnected() {
        return false;
    }
}