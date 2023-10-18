package com.noscompany.snake.game.online.game.options.setter;


import com.noscompany.message.publisher.Subscription;
import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.game.options.*;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.GameCancelled;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.GameFinished;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.GameStartCountdown;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.GameStarted;
import com.noscompany.snake.game.online.contract.messages.seats.AdminId;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerFreedUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerTookASeat;
import com.noscompany.snake.game.online.contract.messages.user.registry.NewUserEnteredRoom;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;

@Slf4j
@AllArgsConstructor
public class GameOptionsSetter {
    private Option<AdminId> adminIdOption;
    @Getter
    private boolean gameRunning;
    @Getter
    private GameOptions gameOptions;

    public InitializeGameOptions newUserEnteredRoom(NewUserEnteredRoom event) {
        log.info("new user with id {} entered the room, initializing his game options", event.getUserId().getId());
        return new InitializeGameOptions(event.getUserId(), gameOptions);
    }

    public void playerTookASeat(PlayerTookASeat event) {
        log.info("user with id {} took a seat", event.getUserId().getId());
        adminGotSet(event.getAdminId());
    }

    public void playerFreedUpASeat(PlayerFreedUpASeat event) {
        log.info("user with id {} freed up a seat", event.getUserId().getId());
        event
                .getAdminId()
                .peek(this::adminGotSet)
                .onEmpty(this::adminWasRemoved);
    }

    public void adminWasRemoved() {
        adminIdOption
                .peek(currentAdminId -> log.info("admin with id {} got removed", currentAdminId.getId()))
                .peek(currentAdminID -> this.adminIdOption = Option.none())
                .onEmpty(() -> log.info("cannot remove current admin id because it is not defined"));
    }

    public void adminGotSet(AdminId newAdminId) {
        adminIdOption
                .peek(oldAdminId -> logAdminChange(oldAdminId, newAdminId))
                .onEmpty(() -> log.info("no admin defined, setting admin id to {}", newAdminId.getId()));
        this.adminIdOption = Option.of(newAdminId);
    }

    private void logAdminChange(AdminId oldAdminId, AdminId newAdminId) {
        if (oldAdminId.equals(newAdminId))
            log.info("admin id did not change");
        else
            log.info("changing admin id from {} to {}", oldAdminId.getId(), newAdminId.getId());
    }

    public Either<FailedToChangeGameOptions, GameOptionsChanged> changeOptions(ChangeGameOptions command) {
        log.info("user with id {} tries to change game options", command.getUserId().getId());
        return changeGameOptions(command.getUserId(), command.getOptions())
                .peekLeft(failure -> log.info("user failed to change game options, reason: {}", failure.getReason()))
                .peek(success -> log.info("user changed the options"));
    }

    public Either<FailedToChangeGameOptions, GameOptionsChanged> changeGameOptions(UserId userId, GameOptions newOptions) {
        if (!userIsAdmin(userId))
            return left(FailedToChangeGameOptions.requesterIsNotAdmin(userId));
        if (gameRunning)
            return left(FailedToChangeGameOptions.gameIsAlreadyRunning(userId));
        this.gameOptions = newOptions;
        return right(new GameOptionsChanged(gameOptions));
    }

    public void gameIsNowRunning() {
        log.info("game is now running");
        this.gameRunning = true;
    }

    public void gameIsNotRunningAnymore() {
        log.info("game is not running anymore");
        this.gameRunning = false;
    }

    private boolean userIsAdmin(UserId userId) {
        return adminIdOption
                .map(adminId -> adminId.equals(AdminId.of(userId)))
                .getOrElse(false);
    }

    public Subscription getSubscription() {
        return new Subscription()
                .toMessage(NewUserEnteredRoom.class, this::newUserEnteredRoom)
                .toMessage(ChangeGameOptions.class, this::changeOptions)
                .toMessage(PlayerTookASeat.class, this::playerTookASeat)
                .toMessage(PlayerFreedUpASeat.class, this::playerFreedUpASeat)
                .toMessage(GameStartCountdown.class, this::gameStartCountdown)
                .toMessage(GameStarted.class, this::gameStarted)
                .toMessage(GameFinished.class, this::gameFinished)
                .toMessage(GameCancelled.class, this::gameCancelled)
                .subscriberName("game-options-setter");
    }

    public void gameStartCountdown(GameStartCountdown event) {
        gameIsNowRunning();
    }

    public void gameStarted(GameStarted event) {
        gameIsNowRunning();
    }

    public void gameFinished(GameFinished event) {
        gameIsNotRunningAnymore();
    }

    public void gameCancelled(GameCancelled event) {
        gameIsNotRunningAnymore();
    }
}