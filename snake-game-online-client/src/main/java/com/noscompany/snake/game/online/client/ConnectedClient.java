package com.noscompany.snake.game.online.client;

import com.noscompany.snake.game.commons.messages.commands.chat.SendChatMessage;
import com.noscompany.snake.game.commons.messages.commands.game.*;
import com.noscompany.snake.game.commons.messages.commands.lobby.ChangeGameOptions;
import com.noscompany.snake.game.commons.messages.commands.lobby.FreeUpASeat;
import com.noscompany.snake.game.commons.messages.commands.lobby.TakeASeat;
import com.noscompany.snake.game.commons.messages.commands.room.EnterRoom;
import lombok.AllArgsConstructor;
import snake.game.core.dto.*;

import static com.noscompany.snake.game.online.client.StartingClientError.CONNECTION_ALREADY_ESTABLISHED;

@AllArgsConstructor
class ConnectedClient implements SnakeOnlineClient {
    private final MessageSender messageSender;
    private final ClientEventHandler eventHandler;

    @Override
    public SnakeOnlineClient connect(String roomName) {
        eventHandler.handle(CONNECTION_ALREADY_ESTABLISHED);
        return this;
    }

    @Override
    public SnakeOnlineClient enterTheRoom(String userName) {
        return messageSender.send(new EnterRoom(userName))
                .map(this::handle)
                .getOrElse(this);
    }

    @Override
    public SnakeOnlineClient takeASeat(SnakeNumber snakeNumberNumber) {
        return messageSender.send(new TakeASeat(snakeNumberNumber))
                .map(this::handle)
                .getOrElse(this);
    }

    @Override
    public SnakeOnlineClient freeUpASeat() {
        return messageSender.send(new FreeUpASeat())
                .map(this::handle)
                .getOrElse(this);
    }

    @Override
    public SnakeOnlineClient changeGameOptions(GridSize gridSize, GameSpeed gameSpeed, Walls walls) {
        return messageSender.send(new ChangeGameOptions(gridSize, gameSpeed, walls))
                .map(this::handle)
                .getOrElse(this);
    }

    @Override
    public SnakeOnlineClient startGame() {
        return messageSender.send(new StartGame())
                .map(this::handle)
                .getOrElse(this);
    }

    @Override
    public SnakeOnlineClient changeSnakeDirection(Direction direction) {
        return messageSender.send(new ChangeSnakeDirection(direction))
        .map(this::handle)
                .getOrElse(this);
    }

    @Override
    public SnakeOnlineClient cancelGame() {
        return messageSender.send(new CancelGame())
                .map(this::handle)
                .getOrElse(this);
    }

    @Override
    public SnakeOnlineClient pauseGame() {
        return messageSender.send(new PauseGame())
                .map(this::handle)
                .getOrElse(this);
    }

    @Override
    public SnakeOnlineClient resumeGame() {
        return messageSender.send(new ResumeGame())
                .map(this::handle)
                .getOrElse(this);
    }

    @Override
    public SnakeOnlineClient sendChatMessage(String chatMessageContent) {
        return messageSender.send(new SendChatMessage(chatMessageContent))
                .map(this::handle)
                .getOrElse(this);
    }

    @Override
    public SnakeOnlineClient disconnect() {
        messageSender.closeSocket();
        return new NotConnectedClient(eventHandler);
    }

    private SnakeOnlineClient handle(ClientError clientError) {
        eventHandler.handle(clientError);
        if (clientError == ClientError.CONNECTION_CLOSED)
            return disconnect();
        else
            return this;
    }

    @Override
    public boolean isConnected() {
        return true;
    }
}