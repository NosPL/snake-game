package com.noscompany.snake.game.online.client.internal.state.connected;

import com.noscompany.snake.game.online.client.ClientError;
import com.noscompany.snake.game.online.client.ClientEventHandler;
import com.noscompany.snake.game.online.client.HostAddress;
import com.noscompany.snake.game.online.client.StartingClientError;
import com.noscompany.snake.game.online.client.internal.state.ClientState;
import com.noscompany.snake.game.online.contract.messages.chat.SendChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.commands.*;
import com.noscompany.snake.game.online.contract.messages.game.dto.*;
import com.noscompany.snake.game.online.contract.messages.lobby.command.ChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.lobby.command.FreeUpASeat;
import com.noscompany.snake.game.online.contract.messages.lobby.command.TakeASeat;
import com.noscompany.snake.game.online.contract.messages.room.EnterRoom;
import com.noscompany.snake.game.online.client.internal.state.not.connected.Disconnected;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Connected implements ClientState {
    private final MessageSender messageSender;
    private final ClientEventHandler eventHandler;

    @Override
    public ClientState connect(HostAddress hostAddress) {
        eventHandler.handle(StartingClientError.CONNECTION_ALREADY_ESTABLISHED);
        return this;
    }

    @Override
    public ClientState enterTheRoom(String userName) {
        return messageSender
                .send(new EnterRoom(userName))
                .map(this::handleError)
                .getOrElse(this);
    }

    @Override
    public ClientState takeASeat(PlayerNumber playerNumber) {
        return messageSender
                .send(new TakeASeat(playerNumber))
                .map(this::handleError)
                .getOrElse(this);
    }

    @Override
    public ClientState freeUpASeat() {
        return messageSender
                .send(new FreeUpASeat())
                .map(this::handleError)
                .getOrElse(this);
    }

    @Override
    public ClientState changeGameOptions(GridSize gridSize, GameSpeed gameSpeed, Walls walls) {
        return messageSender
                .send(new ChangeGameOptions(gridSize, gameSpeed, walls))
                .map(this::handleError)
                .getOrElse(this);
    }

    @Override
    public ClientState startGame() {
        return messageSender
                .send(new StartGame())
                .map(this::handleError)
                .getOrElse(this);
    }

    @Override
    public ClientState changeSnakeDirection(Direction direction) {
        return messageSender
                .send(new ChangeSnakeDirection(direction))
                .map(this::handleError)
                .getOrElse(this);
    }

    @Override
    public ClientState cancelGame() {
        return messageSender
                .send(new CancelGame())
                .map(this::handleError)
                .getOrElse(this);
    }

    @Override
    public ClientState pauseGame() {
        return messageSender
                .send(new PauseGame())
                .map(this::handleError)
                .getOrElse(this);
    }

    @Override
    public ClientState resumeGame() {
        return messageSender
                .send(new ResumeGame())
                .map(this::handleError)
                .getOrElse(this);
    }

    @Override
    public ClientState sendChatMessage(String chatMessageContent) {
        return messageSender
                .send(new SendChatMessage(chatMessageContent))
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
        return true;
    }

    private ClientState handleError(ClientError clientError) {
        eventHandler.handle(clientError);
        if (clientError == ClientError.CONNECTION_CLOSED)
            return closeConnection();
        else
            return this;
    }
}