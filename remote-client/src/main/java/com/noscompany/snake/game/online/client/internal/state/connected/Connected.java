package com.noscompany.snake.game.online.client.internal.state.connected;

import com.noscompany.message.publisher.MessagePublisher;
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
import com.noscompany.snake.game.online.contract.messages.user.registry.EnterRoom;
import com.noscompany.snake.game.online.client.internal.state.not.connected.Disconnected;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;
import lombok.AllArgsConstructor;

import java.util.concurrent.atomic.AtomicReference;

import static com.noscompany.snake.game.online.client.StartingClientError.CONNECTION_ALREADY_ESTABLISHED;

@AllArgsConstructor
public class Connected implements ClientState {
    private final MessageSender messageSender;
    private final MessagePublisher messagePublisher;
    private final AtomicReference<UserId> userId;

    @Override
    public ClientState connect(HostAddress hostAddress) {
        if (messageSender.isConnected()) {
            messagePublisher.publishMessage(CONNECTION_ALREADY_ESTABLISHED);
            return this;
        } else
            return new Disconnected(messagePublisher).connect(hostAddress);
    }

    @Override
    public ClientState enterTheRoom(UserName userName) {
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
        return new Disconnected(messagePublisher);
    }

    @Override
    public boolean isConnected() {
        return messageSender.isConnected();
    }

    private ClientState handleError(SendClientMessageError sendClientMessageError) {
        messagePublisher.publishMessage(sendClientMessageError);
        if (sendClientMessageError == SendClientMessageError.CONNECTION_CLOSED)
            return closeConnection();
        else
            return this;
    }
}