package com.noscompany.snake.game.online.host.room;

import com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptionsChanged;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToStartGame;
import com.noscompany.snake.game.online.contract.messages.room.*;
import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToFreeUpSeat;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerFreedUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerTookASeat;
import com.noscompany.snake.game.online.host.room.dto.UserId;
import com.noscompany.snake.game.online.host.room.internal.chat.Chat;
import com.noscompany.snake.game.online.host.room.internal.playground.Playground;
import com.noscompany.snake.game.online.host.room.internal.user.registry.UserRegistry;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;

import java.util.function.Function;
import java.util.stream.Collectors;

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
                .toEither(new NewUserEnteredRoom(userName.getName(), getState()))
                .swap();
    }

    @Override
    public Either<FailedToTakeASeat, PlayerTookASeat> takeASeat(UserId userId, PlayerNumber playerNumber) {
        return userRegistry
                .findUserNameById(userId)
                .toEither(FailedToTakeASeat.userNotInTheRoom())
                .flatMap(userName -> playground.takeASeat(userName, playerNumber));
    }

    @Override
    public Either<FailedToFreeUpSeat, PlayerFreedUpASeat> freeUpASeat(UserId userId) {
        return userRegistry
                .findUserNameById(userId)
                .toEither(FailedToFreeUpSeat.userNotInTheRoom())
                .flatMap(playground::freeUpASeat);
    }

    @Override
    public Either<FailedToChangeGameOptions, GameOptionsChanged> changeGameOptions(UserId userId, GameOptions gameOptions) {
        return userRegistry
                .findUserNameById(userId)
                .toEither(FailedToChangeGameOptions.userNotInTheRoom())
                .flatMap(userName -> playground.changeGameOptions(userName, gameOptions));
    }

    @Override
    public Option<FailedToStartGame> startGame(UserId userId) {
        return userRegistry
                .findUserNameById(userId)
                .toEither(FailedToStartGame.userIsNotInTheRoom())
                .map(playground::startGame)
                .fold(Option::of, Function.identity());
    }

    @Override
    public void changeSnakeDirection(UserId userId, Direction direction) {
        userRegistry
                .findUserNameById(userId)
                .peek(userName -> playground.changeSnakeDirection(userName, direction));
    }

    @Override
    public void cancelGame(UserId userId) {
        userRegistry
                .findUserNameById(userId)
                .peek(playground::cancelGame);
    }

    @Override
    public void pauseGame(UserId userId) {
        userRegistry
                .findUserNameById(userId)
                .peek(playground::pauseGame);
    }

    @Override
    public void resumeGame(UserId userId) {
        userRegistry
                .findUserNameById(userId)
                .peek(playground::resumeGame);
    }

    @Override
    public Either<FailedToSendChatMessage, UserSentChatMessage> sendChatMessage(UserId userId, String messageContent) {
        return userRegistry
                .findUserNameById(userId)
                .toEither(FailedToSendChatMessage.userIsNotInTheRoom())
                .flatMap(userName -> chat.sendMessage(userName, messageContent));
    }

    @Override
    public Option<UserLeftRoom> leave(UserId userId) {
        return userRegistry
                .removeUser(userId)
                .map(UserRegistry.UserRemoved::getUserName)
                .map(this::userLeftTheRoom);
    }

    private UserLeftRoom userLeftTheRoom(UserName userName) {
        return new UserLeftRoom(
                userName.getName(),
                userRegistry.getUserNames().stream().map(UserName::getName).collect(toSet()),
                playground.freeUpASeat(userName).toOption());
    }

    @Override
    public boolean hasUserWithId(UserId userId) {
        return userRegistry.containsId(userId);
    }

    @Override
    public boolean userIsAdmin(UserId userId) {
        return userRegistry
                .findUserNameById(userId)
                .exists(playground::userIsAdmin);
    }

    @Override
    public boolean userIsSitting(UserId userId) {
        return userRegistry
                .findUserNameById(userId)
                .exists(playground::userTookASeat);
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
}