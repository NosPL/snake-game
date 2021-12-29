package com.noscompany.snake.game.online.server.room.room;

import com.noscompany.snake.game.commons.messages.dto.RoomState;
import com.noscompany.snake.game.commons.messages.events.chat.UserSentChatMessage;
import com.noscompany.snake.game.commons.messages.events.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.commons.messages.events.lobby.*;
import com.noscompany.snake.game.commons.messages.events.room.FailedToEnterRoom;
import com.noscompany.snake.game.commons.messages.events.room.NewUserEnteredRoom;
import com.noscompany.snake.game.commons.messages.events.room.UserLeftRoom;
import com.noscompany.snake.game.online.server.room.room.lobby.Lobby;
import com.noscompany.snake.game.online.server.room.room.user.registry.UserRegistry;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import snake.game.core.dto.*;

import java.util.function.Function;

@AllArgsConstructor
class RoomImpl implements Room {
    private final int userLimit;
    private final UserRegistry userRegistry;
    private final Lobby lobby;

    @Override
    public Either<FailedToEnterRoom, NewUserEnteredRoom> enter(String userId, String userName) {
        if (userRegistry.getUserCount() >= userLimit)
            return Either.left(FailedToEnterRoom.roomIsFull(userName));
        return userRegistry
                .registerNewUser(userId, userName)
                .toEither(new NewUserEnteredRoom(userName, userRegistry.getUserNames()))
                .swap();
    }

    @Override
    public Either<FailedToTakeASeat, PlayerTookASeat> takeASeat(String userId, SnakeNumber snakeNumberNumber) {
        return userRegistry
                .findUserNameById(userId)
                .toEither(FailedToTakeASeat.userNotInTheRoom(lobby.getLobbyState()))
                .flatMap(userName -> lobby.takeASeat(userName, snakeNumberNumber));
    }

    @Override
    public Either<FailedToFreeUpSeat, PlayerFreedUpASeat> freeUpASeat(String userId) {
        return userRegistry
                .findUserNameById(userId)
                .toEither(FailedToFreeUpSeat.userNotInTheRoom())
                .flatMap(lobby::freeUpASeat);
    }

    @Override
    public Either<FailedToChangeGameOptions, GameOptionsChanged> changeGameOptions(String userId, GridSize gridSize, GameSpeed gameSpeed, Walls walls) {
        return userRegistry
                .findUserNameById(userId)
                .toEither(FailedToChangeGameOptions.userNotInTheRoom())
                .flatMap(userName -> lobby.changeGameOptions(userName, gridSize, gameSpeed, walls));
    }

    @Override
    public Option<FailedToStartGame> startGame(String userId) {
        return userRegistry
                .findUserNameById(userId)
                .toEither(FailedToStartGame.userIsNotInTheRoom(lobby.getLobbyState()))
                .mapLeft(Option::of)
                .map(lobby::startGame)
                .fold(Function.identity(), Function.identity());
    }

    @Override
    public void changeSnakeDirection(String userId, Direction direction) {
        userRegistry
                .findUserNameById(userId)
                .peek(userName -> lobby.changeSnakeDirection(userName, direction));
    }

    @Override
    public void cancelGame(String userId) {
        userRegistry
                .findUserNameById(userId)
                .peek(lobby::cancelGame);
    }

    @Override
    public void pauseGame(String userId) {
        userRegistry
                .findUserNameById(userId)
                .peek(lobby::pauseGame);
    }

    @Override
    public void resumeGame(String userId) {
        userRegistry
                .findUserNameById(userId)
                .peek(lobby::resumeGame);
    }

    @Override
    public Either<FailedToSendChatMessage, UserSentChatMessage> sendChatMessage(String userId, String messageContent) {
        return userRegistry
                .findUserNameById(userId)
                .toEither(FailedToSendChatMessage.userIsNotInTheRoom())
                .flatMap(userName -> validate(userName, messageContent));
    }

    private Either<FailedToSendChatMessage, UserSentChatMessage> validate(String userName, String message) {
        return Either.right(new UserSentChatMessage(userName, message));
    }

    @Override
    public Option<UserLeftRoom> removeUserById(String userId) {
        return userRegistry
                .removeUser(userId)
                .map(UserRegistry.UserRemoved::getUserName)
                .map(this::userLeftTheRoom);
    }

    private UserLeftRoom userLeftTheRoom(String userName) {
        return new UserLeftRoom(
                userName,
                userRegistry.getUserNames(),
                lobby.freeUpASeat(userName).toOption());
    }

    @Override
    public boolean containsUserWithId(String userId) {
        return userRegistry.containsId(userId);
    }

    @Override
    public RoomState getState() {
        return new RoomState(
                userLimit,
                userRegistry.getUserNames(),
                lobby.getLobbyState());
    }
}