package com.noscompany.snake.game.online.client.internal.state.connected;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.snake.game.online.client.RemoteClientIdHolder;
import com.noscompany.snake.game.online.client.SendClientMessageError;
import com.noscompany.snake.game.online.client.HostAddress;
import com.noscompany.snake.game.online.client.internal.state.ClientState;
import com.noscompany.snake.game.online.contract.messages.chat.SendChatMessage;
import com.noscompany.snake.game.online.contract.messages.gameplay.commands.*;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.*;
import com.noscompany.snake.game.online.contract.messages.game.options.ChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.seats.FreeUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.TakeASeat;
import com.noscompany.snake.game.online.contract.messages.user.registry.EnterRoom;
import com.noscompany.snake.game.online.client.internal.state.not.connected.Disconnected;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;
import com.noscompany.snake.game.online.online.contract.serialization.OnlineMessageDeserializer;
import com.noscompany.snake.game.online.online.contract.serialization.OnlineMessageSerializer;
import lombok.AllArgsConstructor;

import static com.noscompany.snake.game.online.client.SendClientMessageError.USER_ID_IS_NOT_INITIALIZED;
import static com.noscompany.snake.game.online.client.StartingClientError.CONNECTION_ALREADY_ESTABLISHED;
import static com.noscompany.snake.game.online.client.RemoteClientIdHolder.userId;

@AllArgsConstructor
public class Connected implements ClientState {
    private final Websocket webSocket;
    private final MessagePublisher messagePublisher;
    private final OnlineMessageDeserializer deserializer;
    private final OnlineMessageSerializer serializer;

    @Override
    public ClientState connect(HostAddress hostAddress) {
        if (webSocket.isConnected()) {
            messagePublisher.publishMessage(CONNECTION_ALREADY_ESTABLISHED);
            return this;
        } else
            return new Disconnected(messagePublisher, deserializer, serializer)
                    .connect(hostAddress);
    }

    @Override
    public ClientState enterTheRoom(UserName userName) {
        return RemoteClientIdHolder
                .userId()
                .map(userId -> new EnterRoom(userId, userName))
                .onEmpty(() -> handleError(USER_ID_IS_NOT_INITIALIZED))
                .flatMap(webSocket::send)
                .map(this::handleError)
                .getOrElse(this);
    }

    @Override
    public ClientState takeASeat(PlayerNumber playerNumber) {
        return RemoteClientIdHolder
                .userId()
                .map(userId -> new TakeASeat(userId().get(), playerNumber))
                .onEmpty(() -> handleError(USER_ID_IS_NOT_INITIALIZED))
                .flatMap(webSocket::send)
                .map(this::handleError)
                .getOrElse(this);
    }

    @Override
    public ClientState freeUpASeat() {
        return RemoteClientIdHolder
                .userId()
                .map(FreeUpASeat::new)
                .onEmpty(() -> handleError(USER_ID_IS_NOT_INITIALIZED))
                .flatMap(webSocket::send)
                .map(this::handleError)
                .getOrElse(this);
    }

    @Override
    public ClientState changeGameOptions(GridSize gridSize, GameSpeed gameSpeed, Walls walls) {
        return RemoteClientIdHolder
                .userId()
                .map(userId -> new ChangeGameOptions(userId, gridSize, gameSpeed, walls))
                .onEmpty(() -> handleError(USER_ID_IS_NOT_INITIALIZED))
                .flatMap(webSocket::send)
                .map(this::handleError)
                .getOrElse(this);
    }

    @Override
    public ClientState startGame() {
        return RemoteClientIdHolder
                .userId()
                .map(StartGame::new)
                .onEmpty(() -> handleError(USER_ID_IS_NOT_INITIALIZED))
                .flatMap(webSocket::send)
                .map(this::handleError)
                .getOrElse(this);
    }

    @Override
    public ClientState changeSnakeDirection(Direction direction) {
        return RemoteClientIdHolder
                .userId()
                .map(userId -> new ChangeSnakeDirection(userId, direction))
                .onEmpty(() -> handleError(USER_ID_IS_NOT_INITIALIZED))
                .flatMap(webSocket::send)
                .map(this::handleError)
                .getOrElse(this);
    }

    @Override
    public ClientState cancelGame() {
        return RemoteClientIdHolder
                .userId()
                .map(CancelGame::new)
                .onEmpty(() -> handleError(USER_ID_IS_NOT_INITIALIZED))
                .flatMap(webSocket::send)
                .map(this::handleError)
                .getOrElse(this);
    }

    @Override
    public ClientState pauseGame() {
        return RemoteClientIdHolder
                .userId()
                .map(PauseGame::new)
                .onEmpty(() -> handleError(USER_ID_IS_NOT_INITIALIZED))
                .flatMap(webSocket::send)
                .map(this::handleError)
                .getOrElse(this);
    }

    @Override
    public ClientState resumeGame() {
        return RemoteClientIdHolder
                .userId()
                .map(ResumeGame::new)
                .onEmpty(() -> handleError(USER_ID_IS_NOT_INITIALIZED))
                .flatMap(webSocket::send)
                .map(this::handleError)
                .getOrElse(this);
    }

    @Override
    public ClientState sendChatMessage(String chatMessageContent) {
        return RemoteClientIdHolder
                .userId()
                .map(userId -> new SendChatMessage(userId, chatMessageContent))
                .onEmpty(() -> handleError(USER_ID_IS_NOT_INITIALIZED))
                .flatMap(webSocket::send)
                .map(this::handleError)
                .getOrElse(this);
    }

    @Override
    public ClientState closeConnection() {
        webSocket.closeConnection();
        return new Disconnected(messagePublisher, deserializer, serializer);
    }

    @Override
    public boolean isConnected() {
        return webSocket.isConnected();
    }

    private ClientState handleError(SendClientMessageError sendClientMessageError) {
        messagePublisher.publishMessage(sendClientMessageError);
        if (sendClientMessageError == SendClientMessageError.CONNECTION_CLOSED)
            return closeConnection();
        else
            return this;
    }
}