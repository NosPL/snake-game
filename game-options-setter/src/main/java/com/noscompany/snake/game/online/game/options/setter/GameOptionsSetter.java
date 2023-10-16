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

import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;

@AllArgsConstructor
public class GameOptionsSetter {
    private Option<AdminId> adminIdOption;
    @Getter
    private boolean gameRunning;
    @Getter
    private GameOptions gameOptions;

    public InitializeGameOptions newUserEnteredRoom(NewUserEnteredRoom event) {
        return new InitializeGameOptions(event.getUserId(), gameOptions);
    }

    public void playerTookASeat(PlayerTookASeat event) {
        adminGotSet(event.getAdminId());
    }

    public void playerFreedUpASeat(PlayerFreedUpASeat event) {
        event
                .getAdminId()
                .peek(this::adminGotSet)
                .onEmpty(this::adminWasRemoved);
    }

    public void adminWasRemoved() {
        this.adminIdOption = Option.none();
    }

    public void adminGotSet(AdminId adminId) {
        this.adminIdOption = Option.of(adminId);
    }

    public Either<FailedToChangeGameOptions, GameOptionsChanged> changeOptions(ChangeGameOptions command) {
        return changeGameOptions(command.getUserId(), command.getOptions());
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
        this.gameRunning = true;
    }

    public void gameIsNotRunningAnymore() {
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