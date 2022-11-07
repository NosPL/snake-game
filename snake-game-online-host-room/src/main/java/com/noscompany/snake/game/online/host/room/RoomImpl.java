package com.noscompany.snake.game.online.host.room;

import com.noscompany.snake.game.online.contract.messages.game.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.game.dto.GameOptions;
import com.noscompany.snake.game.online.contract.messages.game.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.lobby.event.*;
import com.noscompany.snake.game.online.contract.messages.room.RoomState;
import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.room.FailedToEnterRoom;
import com.noscompany.snake.game.online.contract.messages.room.NewUserEnteredRoom;
import com.noscompany.snake.game.online.contract.messages.room.UserLeftRoom;
import com.noscompany.snake.game.online.host.room.internal.chat.Chat;
import com.noscompany.snake.game.online.host.room.internal.lobby.Lobby;
import com.noscompany.snake.game.online.host.room.internal.user.registry.UserRegistry;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;

import java.util.function.Function;

@AllArgsConstructor
class RoomImpl implements Room {
    private final int userLimit;
    private final UserRegistry userRegistry;
    private final Lobby lobby;
    private final Chat chat;

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
    public Either<FailedToTakeASeat, PlayerTookASeat> takeASeat(String userId, PlayerNumber playerNumber) {
        return userRegistry
                .findUserNameById(userId)
                .toEither(FailedToTakeASeat.userNotInTheRoom())
                .flatMap(userName -> lobby.takeASeat(userName, playerNumber));
    }

    @Override
    public Either<FailedToFreeUpSeat, PlayerFreedUpASeat> freeUpASeat(String userId) {
        return userRegistry
                .findUserNameById(userId)
                .toEither(FailedToFreeUpSeat.userNotInTheRoom())
                .flatMap(lobby::freeUpASeat);
    }

    @Override
    public Either<FailedToChangeGameOptions, GameOptionsChanged> changeGameOptions(String userId, GameOptions gameOptions) {
        return userRegistry
                .findUserNameById(userId)
                .toEither(FailedToChangeGameOptions.userNotInTheRoom())
                .flatMap(userName -> lobby.changeGameOptions(userName, gameOptions));
    }

    @Override
    public Option<FailedToStartGame> startGame(String userId) {
        return userRegistry
                .findUserNameById(userId)
                .toEither(FailedToStartGame.userIsNotInTheRoom())
                .map(lobby::startGame)
                .mapLeft(Option::of)
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
                .flatMap(userName -> chat.sendMessage(userName, messageContent));
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
    public boolean hasUserWithId(String userId) {
        return userRegistry.containsId(userId);
    }

    @Override
    public boolean userIsAdmin(String userId) {
        return userRegistry
                .findUserNameById(userId)
                .exists(lobby::userIsAdmin);
    }

    @Override
    public boolean userIsSitting(String userId) {
        return userRegistry
                .findUserNameById(userId)
                .exists(lobby::userIsSitting);
    }

    @Override
    public RoomState getState() {
        return new RoomState(
                isFull(),
                userRegistry.getUserNames(),
                lobby.getLobbyState());
    }

    @Override
    public boolean isFull() {
        return userRegistry.usersCount() >= userLimit;
    }
}