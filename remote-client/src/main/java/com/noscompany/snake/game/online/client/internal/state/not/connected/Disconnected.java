package com.noscompany.snake.game.online.client.internal.state.not.connected;

import com.noscompany.snake.game.online.client.SendClientMessageError;
import com.noscompany.snake.game.online.client.ClientEventHandler;
import com.noscompany.snake.game.online.client.HostAddress;
import com.noscompany.snake.game.online.client.StartingClientError;
import com.noscompany.snake.game.online.client.internal.state.ClientState;
import com.noscompany.snake.game.online.client.internal.state.connected.ConnectedClientCreator;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.*;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Disconnected implements ClientState {
    private final ClientEventHandler eventHandler;

    @Override
    public ClientState connect(HostAddress hostAddress) {
        return ConnectedClientCreator
                .create(hostAddress, eventHandler)
                .onSuccess(connected -> eventHandler.connectionEstablished())
                .onFailure(t -> eventHandler.handle(StartingClientError.FAILED_TO_CONNECT_TO_SERVER))
                .getOrElse(this);
    }

    @Override
    public ClientState enterTheRoom(UserName userName) {
        eventHandler.handle(SendClientMessageError.CLIENT_NOT_CONNECTED);
        return this;
    }

    @Override
    public ClientState takeASeat(PlayerNumber playerNumber) {
        eventHandler.handle(SendClientMessageError.CLIENT_NOT_CONNECTED);
        return this;
    }

    @Override
    public ClientState freeUpASeat() {
        eventHandler.handle(SendClientMessageError.CLIENT_NOT_CONNECTED);
        return this;
    }

    @Override
    public ClientState changeGameOptions(GridSize gridSize, GameSpeed gameSpeed, Walls walls) {
        eventHandler.handle(SendClientMessageError.CLIENT_NOT_CONNECTED);
        return this;
    }

    @Override
    public ClientState startGame() {
        eventHandler.handle(SendClientMessageError.CLIENT_NOT_CONNECTED);
        return this;
    }

    @Override
    public ClientState changeSnakeDirection(Direction direction) {
        eventHandler.handle(SendClientMessageError.CLIENT_NOT_CONNECTED);
        return this;
    }

    @Override
    public ClientState cancelGame() {
        eventHandler.handle(SendClientMessageError.CLIENT_NOT_CONNECTED);
        return this;
    }

    @Override
    public ClientState pauseGame() {
        eventHandler.handle(SendClientMessageError.CLIENT_NOT_CONNECTED);
        return this;
    }

    @Override
    public ClientState resumeGame() {
        eventHandler.handle(SendClientMessageError.CLIENT_NOT_CONNECTED);
        return this;
    }

    @Override
    public ClientState sendChatMessage(String message) {
        eventHandler.handle(SendClientMessageError.CLIENT_NOT_CONNECTED);
        return this;
    }

    @Override
    public ClientState closeConnection() {
        eventHandler.handle(SendClientMessageError.CLIENT_NOT_CONNECTED);
        return this;
    }

    @Override
    public boolean isConnected() {
        return false;
    }
}