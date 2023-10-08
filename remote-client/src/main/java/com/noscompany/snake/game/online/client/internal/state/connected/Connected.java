package com.noscompany.snake.game.online.client.internal.state.connected;

import com.noscompany.snake.game.online.client.SendClientMessageError;
import com.noscompany.snake.game.online.client.ClientEventHandler;
import com.noscompany.snake.game.online.client.HostAddress;
import com.noscompany.snake.game.online.client.StartingClientError;
import com.noscompany.snake.game.online.client.internal.state.ClientState;
import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.chat.SendChatMessage;
import com.noscompany.snake.game.online.contract.messages.gameplay.commands.*;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.*;
import com.noscompany.snake.game.online.contract.messages.game.options.ChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.seats.FreeUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.TakeASeat;
import com.noscompany.snake.game.online.contract.messages.room.EnterRoom;
import com.noscompany.snake.game.online.client.internal.state.not.connected.Disconnected;
import lombok.AllArgsConstructor;

import java.util.concurrent.atomic.AtomicReference;

@AllArgsConstructor
public class Connected implements ClientState {
    private final MessageSender messageSender;
    private final ClientEventHandler eventHandler;
    private final AtomicReference<UserId> userId;

    @Override
    public ClientState connect(HostAddress hostAddress) {
        if (messageSender.isConnected()) {
            eventHandler.handle(StartingClientError.CONNECTION_ALREADY_ESTABLISHED);
            return this;
        } else
            return new Disconnected(eventHandler).connect(hostAddress);
    }

    @Override
    public ClientState enterTheRoom(String userName) {
        return messageSender
                .send(new EnterRoom(userId.get(), userName))
                .map(this::handleError)
                .getOrElse(this);
    }

    @Override
    public ClientState takeASeat(PlayerNumber playerNumber) {
        return messageSender
                .send(new TakeASeat(userId.get(), playerNumber))
                .map(this::handleError)
                .getOrElse(this);
    }

    @Override
    public ClientState freeUpASeat() {
        return messageSender
                .send(new FreeUpASeat(userId.get()))
                .map(this::handleError)
                .getOrElse(this);
    }

    @Override
    public ClientState changeGameOptions(GridSize gridSize, GameSpeed gameSpeed, Walls walls) {
        return messageSender
                .send(new ChangeGameOptions(userId.get(), gridSize, gameSpeed, walls))
                .map(this::handleError)
                .getOrElse(this);
    }

    @Override
    public ClientState startGame() {
        return messageSender
                .send(new StartGame(userId.get()))
                .map(this::handleError)
                .getOrElse(this);
    }

    @Override
    public ClientState changeSnakeDirection(Direction direction) {
        return messageSender
                .send(new ChangeSnakeDirection(userId.get(), direction))
                .map(this::handleError)
                .getOrElse(this);
    }

    @Override
    public ClientState cancelGame() {
        return messageSender
                .send(new CancelGame(userId.get()))
                .map(this::handleError)
                .getOrElse(this);
    }

    @Override
    public ClientState pauseGame() {
        return messageSender
                .send(new PauseGame(userId.get()))
                .map(this::handleError)
                .getOrElse(this);
    }

    @Override
    public ClientState resumeGame() {
        return messageSender
                .send(new ResumeGame(userId.get()))
                .map(this::handleError)
                .getOrElse(this);
    }

    @Override
    public ClientState sendChatMessage(String chatMessageContent) {
        return messageSender
                .send(new SendChatMessage(userId.get(), chatMessageContent))
                .map(this::handleError)
                .getOrElse(this);
    }

    @Override
    public ClientState closeConnection() {
        messageSender.closeConnection();
        return new Disconnected(eventHandler);
    }

    @Override
    public boolean isConnected() {
        return messageSender.isConnected();
    }

    private ClientState handleError(SendClientMessageError sendClientMessageError) {
        eventHandler.handle(sendClientMessageError);
        if (sendClientMessageError == SendClientMessageError.CONNECTION_CLOSED)
            return closeConnection();
        else
            return this;
    }
}