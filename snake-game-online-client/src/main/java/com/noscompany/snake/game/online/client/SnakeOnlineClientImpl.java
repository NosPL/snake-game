package com.noscompany.snake.game.online.client;

import com.noscompany.snake.game.online.client.internal.state.ClientState;
import com.noscompany.snake.game.online.contract.messages.game.dto.*;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class SnakeOnlineClientImpl implements SnakeOnlineClient {
    private ClientState clientState;

    @Override
    public void connect(String roomName) {
        clientState = clientState.connect(roomName);
    }

    @Override
    public void enterTheRoom(String userName) {
        clientState = clientState.enterTheRoom(userName);
    }

    @Override
    public void takeASeat(PlayerNumber playerNumber) {
        clientState = clientState.takeASeat(playerNumber);
    }

    @Override
    public void freeUpASeat() {
        clientState = clientState.freeUpASeat();
    }

    @Override
    public void changeGameOptions(GridSize gridSize, GameSpeed gameSpeed, Walls walls) {
        clientState = clientState.changeGameOptions(gridSize, gameSpeed, walls);
    }

    @Override
    public void startGame() {
        clientState = clientState.startGame();
    }

    @Override
    public void changeSnakeDirection(Direction direction) {
        clientState = clientState.changeSnakeDirection(direction);
    }

    @Override
    public void cancelGame() {
        clientState = clientState.cancelGame();
    }

    @Override
    public void pauseGame() {
        clientState = clientState.pauseGame();
    }

    @Override
    public void resumeGame() {
        clientState = clientState.resumeGame();
    }

    @Override
    public void sendChatMessage(String message) {
        clientState = clientState.sendChatMessage(message);
    }

    @Override
    public void disconnect() {
        clientState = clientState.closeConnection();
    }

    @Override
    public boolean isConnected() {
        return clientState.isConnected();
    }
}