package com.noscompany.snake.game.online.server.room;

import com.noscompany.snake.game.commons.messages.events.chat.ChatMessageReceived;
import com.noscompany.snake.game.commons.messages.events.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.commons.messages.events.lobby.*;
import com.noscompany.snake.game.commons.messages.events.room.FailedToEnterRoom;
import com.noscompany.snake.game.commons.messages.events.room.NewUserEnteredRoom;
import com.noscompany.snake.game.commons.messages.events.room.UserLeftRoom;
import com.noscompany.snake.game.online.server.room.lobby.GameLobby;
import com.noscompany.snake.game.online.server.room.users.ConnectedUsers;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import snake.game.core.dto.*;

@AllArgsConstructor
class RoomImpl implements Room {
    private final ConnectedUsers connectedUsers;
    private final GameLobby lobby;

    @Override
    public Either<FailedToEnterRoom, NewUserEnteredRoom> enter(String userId, String userName) {
        return connectedUsers.add(userId, userName)
                .map(ConnectedUsers.UserNameAlreadyInUse::getUserName)
                .map(FailedToEnterRoom::userNameAlreadyInUse)
                .toEither(new NewUserEnteredRoom(userName, connectedUsers.getUserNames()))
                .swap();
    }

    @Override
    public Either<FailedToTakeASeat, PlayerTookASeat> takeASeat(String userId, SnakeNumber snakeNumberNumber) {
        return connectedUsers.getUserName(userId)
                .mapLeft(userNameNotFound -> FailedToTakeASeat.userNotInTheRoom(lobby.getLobbyState()))
                .flatMap(userName -> lobby.takeASeat(userName, snakeNumberNumber));
    }

    @Override
    public Either<FailedToFreeUpSeat, PlayerFreedUpASeat> freeUpASeat(String userId) {
        return connectedUsers.getUserName(userId)
                .mapLeft(userNameNotFound -> FailedToFreeUpSeat.userNotInTheRoom())
                .flatMap(lobby::freeUpASeat);
    }

    @Override
    public Either<FailedToChangeGameOptions, GameOptionsChanged> changeGameOptions(String userId, GridSize gridSize, GameSpeed gameSpeed, Walls walls) {
        return connectedUsers.getUserName(userId)
                .mapLeft(userNameNotFound -> FailedToChangeGameOptions.userNotInTheRoom(lobby.getLobbyState()))
                .flatMap(userName -> lobby.changeGameOptions(userName, gridSize, gameSpeed, walls));
    }

    @Override
    public Option<FailedToStartGame> startGame(String userId) {
        return connectedUsers.getUserName(userId)
                .toOption()
                .flatMap(lobby::startGame);
    }

    @Override
    public void changeSnakeDirection(String userId, Direction direction) {
        connectedUsers.getUserName(userId)
                        .peek(userName -> lobby.changeSnakeDirection(userName, direction));
    }

    @Override
    public void cancelGame(String userId) {
        connectedUsers.getUserName(userId)
                .peek(lobby::cancelGame);
    }

    @Override
    public void pauseGame(String userId) {
        connectedUsers.getUserName(userId)
                .peek(lobby::pauseGame);
    }

    @Override
    public void resumeGame(String userId) {
        connectedUsers.getUserName(userId)
                .peek(lobby::resumeGame);
    }

    @Override
    public Either<FailedToSendChatMessage, ChatMessageReceived> sendChatMessage(String userId, String message) {
        return connectedUsers.getUserName(userId)
                .mapLeft(userNameNotFound -> FailedToSendChatMessage.userIsNotInTheRoom())
                .flatMap(userName -> validate(userName, message));
    }

    private Either<FailedToSendChatMessage, ChatMessageReceived> validate(String userName, String message) {
        return Either.right(new ChatMessageReceived(userName, message));
    }

    @Override
    public Option<UserLeftRoom> removeUser(String userId) {
        return connectedUsers.removeUser(userId)
                .map(ConnectedUsers.UserRemoved::getUserName)
                .map(this::userLeftTheRoom);
    }

    private UserLeftRoom userLeftTheRoom(String userName) {
        return new UserLeftRoom(
                userName,
                connectedUsers.getUserNames(),
                lobby.freeUpASeat(userName).toOption());
    }
}