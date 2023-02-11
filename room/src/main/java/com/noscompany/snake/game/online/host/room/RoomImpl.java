package com.noscompany.snake.game.online.host.room;

import com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptionsChanged;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToStartGame;
import com.noscompany.snake.game.online.contract.messages.room.RoomState;
import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.room.FailedToEnterRoom;
import com.noscompany.snake.game.online.contract.messages.room.NewUserEnteredRoom;
import com.noscompany.snake.game.online.contract.messages.room.UserLeftRoom;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToFreeUpSeat;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerFreedUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerTookASeat;
import com.noscompany.snake.game.online.host.room.internal.chat.Chat;
import com.noscompany.snake.game.online.host.room.internal.playground.Playground;
import com.noscompany.snake.game.online.host.room.internal.user.registry.UserRegistry;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;

import java.util.function.Function;

@AllArgsConstructor
class RoomImpl implements Room {
    private final UserRegistry userRegistry;
    private final Playground playground;
    private final Chat chat;

    @Override
    public Either<FailedToEnterRoom, NewUserEnteredRoom> enter(String userId, String userName) {
        return userRegistry
                .registerNewUser(userId, userName)
                .toEither(new NewUserEnteredRoom(userName, getState()))
                .swap();
    }

    @Override
    public Either<FailedToTakeASeat, PlayerTookASeat> takeASeat(String userId, PlayerNumber playerNumber) {
        return userRegistry
                .findUserNameById(userId)
                .toEither(FailedToTakeASeat.userNotInTheRoom())
                .flatMap(userName -> playground.takeASeat(userName, playerNumber));
    }

    @Override
    public Either<FailedToFreeUpSeat, PlayerFreedUpASeat> freeUpASeat(String userId) {
        return userRegistry
                .findUserNameById(userId)
                .toEither(FailedToFreeUpSeat.userNotInTheRoom())
                .flatMap(playground::freeUpASeat);
    }

    @Override
    public Either<FailedToChangeGameOptions, GameOptionsChanged> changeGameOptions(String userId, GameOptions gameOptions) {
        return userRegistry
                .findUserNameById(userId)
                .toEither(FailedToChangeGameOptions.userNotInTheRoom())
                .flatMap(userName -> playground.changeGameOptions(userName, gameOptions));
    }

    @Override
    public Option<FailedToStartGame> startGame(String userId) {
        return userRegistry
                .findUserNameById(userId)
                .toEither(FailedToStartGame.userIsNotInTheRoom())
                .map(playground::startGame)
                .fold(Option::of, Function.identity());
    }

    @Override
    public void changeSnakeDirection(String userId, Direction direction) {
        userRegistry
                .findUserNameById(userId)
                .peek(userName -> playground.changeSnakeDirection(userName, direction));
    }

    @Override
    public void cancelGame(String userId) {
        userRegistry
                .findUserNameById(userId)
                .peek(playground::cancelGame);
    }

    @Override
    public void pauseGame(String userId) {
        userRegistry
                .findUserNameById(userId)
                .peek(playground::pauseGame);
    }

    @Override
    public void resumeGame(String userId) {
        userRegistry
                .findUserNameById(userId)
                .peek(playground::resumeGame);
    }

    @Override
    public Either<FailedToSendChatMessage, UserSentChatMessage> sendChatMessage(String userId, String messageContent) {
        return userRegistry
                .findUserNameById(userId)
                .toEither(FailedToSendChatMessage.userIsNotInTheRoom())
                .flatMap(userName -> chat.sendMessage(userName, messageContent));
    }

    @Override
    public Option<UserLeftRoom> leave(String userId) {
        return userRegistry
                .removeUser(userId)
                .map(UserRegistry.UserRemoved::getUserName)
                .map(this::userLeftTheRoom);
    }

    private UserLeftRoom userLeftTheRoom(String userName) {
        return new UserLeftRoom(
                userName,
                userRegistry.getUserNames(),
                playground.freeUpASeat(userName).toOption());
    }

    @Override
    public boolean hasUserWithId(String userId) {
        return userRegistry.containsId(userId);
    }

    @Override
    public boolean userIsAdmin(String userId) {
        return userRegistry
                .findUserNameById(userId)
                .exists(playground::userIsAdmin);
    }

    @Override
    public boolean userIsSitting(String userId) {
        return userRegistry
                .findUserNameById(userId)
                .exists(playground::userTookASeat);
    }

    @Override
    public RoomState getState() {
        return new RoomState(
                isFull(),
                userRegistry.getUserNames(),
                playground.getPlaygroundState());
    }

    @Override
    public boolean isFull() {
        return userRegistry.isFull();
    }
}