package com.noscompany.snake.game.online.host.room;

import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptionsChanged;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.*;
import com.noscompany.snake.game.online.contract.messages.room.*;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToFreeUpSeat;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerFreedUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerTookASeat;
import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.host.room.internal.chat.Chat;
import com.noscompany.snake.game.online.host.room.internal.playground.Playground;
import com.noscompany.snake.game.online.host.room.internal.user.registry.UserRegistry;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;

import static io.vavr.control.Either.left;
import static java.util.stream.Collectors.toSet;

@AllArgsConstructor
class RoomFacade implements Room {
    private final UserRegistry userRegistry;
    private final Playground playground;
    private final Chat chat;

    @Override
    public Either<FailedToEnterRoom, NewUserEnteredRoom> enter(UserId userId, UserName userName) {
        return userRegistry
                .registerNewUser(userId, userName)
                .toEither(new NewUserEnteredRoom(userId, userName.getName(), getState()))
                .swap();
    }

    @Override
    public Either<FailedToTakeASeat, PlayerTookASeat> takeASeat(UserId userId, PlayerNumber playerNumber) {
        return userRegistry
                .findUserNameById(userId)
                .toEither(FailedToTakeASeat.userNotInTheRoom(userId))
                .flatMap(userName -> playground.takeASeat(userId, userName, playerNumber));
    }

    @Override
    public Either<FailedToFreeUpSeat, PlayerFreedUpASeat> freeUpASeat(UserId userId) {
        if (!userRegistry.containsId(userId))
            return left(FailedToFreeUpSeat.userNotInTheRoom(userId));
        return playground.freeUpASeat(userId);
    }

    @Override
    public Either<FailedToChangeGameOptions, GameOptionsChanged> changeGameOptions(UserId userId, GameOptions gameOptions) {
        if (!userRegistry.containsId(userId))
            return left(FailedToChangeGameOptions.userNotInTheRoom(userId));
        return playground.changeGameOptions(userId, gameOptions);
    }

    @Override
    public Option<FailedToStartGame> startGame(UserId userId) {
        if (!userRegistry.containsId(userId))
            return Option.of(FailedToStartGame.userIsNotInTheRoom(userId));
        return playground.startGame(userId);
    }

    @Override
    public Option<FailedToChangeSnakeDirection> changeSnakeDirection(UserId userId, Direction direction) {
        if (!userRegistry.containsId(userId))
            return Option.of(FailedToChangeSnakeDirection.userNotInTheRoom(userId));
        return playground.changeSnakeDirection(userId, direction);
    }

    @Override
    public Option<FailedToCancelGame> cancelGame(UserId userId) {
        if (!userRegistry.containsId(userId))
            return Option.of(FailedToCancelGame.userNotInTheRoom(userId));
        return playground.cancelGame(userId);
    }

    @Override
    public Option<FailedToPauseGame> pauseGame(UserId userId) {
        if (!userRegistry.containsId(userId))
            return Option.of(FailedToPauseGame.userNotInTheRoom(userId));
        return playground.pauseGame(userId);
    }

    @Override
    public Option<FailedToResumeGame> resumeGame(UserId userId) {
        if (!userRegistry.containsId(userId))
            return Option.of(FailedToResumeGame.userNotInTheRoom(userId));
        return playground.resumeGame(userId);
    }

    @Override
    public Either<FailedToSendChatMessage, UserSentChatMessage> sendChatMessage(UserId userId, String messageContent) {
        return userRegistry
                .findUserNameById(userId)
                .toEither(FailedToSendChatMessage.userIsNotInTheRoom(userId))
                .flatMap(userName -> chat.sendMessage(userId, userName, messageContent));
    }

    @Override
    public Option<UserLeftRoom> leave(UserId userId) {
        return userRegistry
                .removeUser(userId)
                .map(userRemoved -> userLeftTheRoom(userId, userRemoved));
    }

    private UserLeftRoom userLeftTheRoom(UserId userId, UserRegistry.UserRemoved userRemoved) {
        return new UserLeftRoom(
                userId,
                userRemoved.getUserName().getName(),
                userRegistry.getUserNames().stream().map(UserName::getName).collect(toSet()),
                playground.freeUpASeat(userRemoved.getUserId()).toOption());
    }

    @Override
    public boolean hasUserWithId(UserId userId) {
        return userRegistry.containsId(userId);
    }

    @Override
    public boolean userIsAdmin(UserId userId) {
        return playground.userIsAdmin(userId);
    }

    @Override
    public boolean userIsSitting(UserId userId) {
        return playground.userTookASeat(userId);
    }

    @Override
    public RoomState getState() {
        return new RoomState(
                isFull(),
                userRegistry.getUserNames().stream().map(UserName::getName).collect(toSet()),
                playground.getPlaygroundState());
    }

    @Override
    public boolean isFull() {
        return userRegistry.isFull();
    }

    @Override
    public void terminate() {
        userRegistry.removeAllUsers();
        playground.cancelGame();
    }
}