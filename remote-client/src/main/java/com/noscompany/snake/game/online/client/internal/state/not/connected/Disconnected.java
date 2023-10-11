package com.noscompany.snake.game.online.client.internal.state.not.connected;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.snake.game.online.client.ConnectionEstablished;
import com.noscompany.snake.game.online.client.HostAddress;
import com.noscompany.snake.game.online.client.internal.state.ClientState;
import com.noscompany.snake.game.online.client.internal.state.connected.ConnectedClientCreator;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.*;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;
import lombok.AllArgsConstructor;

import static com.noscompany.snake.game.online.client.SendClientMessageError.CLIENT_NOT_CONNECTED;
import static com.noscompany.snake.game.online.client.StartingClientError.FAILED_TO_CONNECT_TO_SERVER;

@AllArgsConstructor
public class Disconnected implements ClientState {
    private final MessagePublisher messagePublisher;

    @Override
    public ClientState connect(HostAddress hostAddress) {
        return ConnectedClientCreator
                .create(hostAddress, messagePublisher)
                .onSuccess(connected -> messagePublisher.publishMessage(new ConnectionEstablished()))
                .onFailure(t -> messagePublisher.publishMessage(FAILED_TO_CONNECT_TO_SERVER))
                .getOrElse(this);
    }

    @Override
    public ClientState enterTheRoom(UserName userName) {
        messagePublisher.publishMessage(CLIENT_NOT_CONNECTED);
        return this;
    }

    @Override
    public ClientState takeASeat(PlayerNumber playerNumber) {
        messagePublisher.publishMessage(CLIENT_NOT_CONNECTED);
        return this;
    }

    @Override
    public ClientState freeUpASeat() {
        messagePublisher.publishMessage(CLIENT_NOT_CONNECTED);
        return this;
    }

    @Override
    public ClientState changeGameOptions(GridSize gridSize, GameSpeed gameSpeed, Walls walls) {
        messagePublisher.publishMessage(CLIENT_NOT_CONNECTED);
        return this;
    }

    @Override
    public ClientState startGame() {
        messagePublisher.publishMessage(CLIENT_NOT_CONNECTED);
        return this;
    }

    @Override
    public ClientState changeSnakeDirection(Direction direction) {
        messagePublisher.publishMessage(CLIENT_NOT_CONNECTED);
        return this;
    }

    @Override
    public ClientState cancelGame() {
        messagePublisher.publishMessage(CLIENT_NOT_CONNECTED);
        return this;
    }

    @Override
    public ClientState pauseGame() {
        messagePublisher.publishMessage(CLIENT_NOT_CONNECTED);
        return this;
    }

    @Override
    public ClientState resumeGame() {
        messagePublisher.publishMessage(CLIENT_NOT_CONNECTED);
        return this;
    }

    @Override
    public ClientState sendChatMessage(String message) {
        messagePublisher.publishMessage(CLIENT_NOT_CONNECTED);
        return this;
    }

    @Override
    public ClientState closeConnection() {
        messagePublisher.publishMessage(CLIENT_NOT_CONNECTED);
        return this;
    }

    @Override
    public boolean isConnected() {
        return false;
    }
}