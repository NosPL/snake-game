package com.noscompany.snake.game.online.gameplay.supervisor;


import com.noscompany.message.publisher.Subscription;
import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptionsChanged;
import com.noscompany.snake.game.online.contract.messages.gameplay.commands.*;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.*;
import com.noscompany.snake.game.online.contract.messages.host.HostGotShutdown;
import com.noscompany.snake.game.online.contract.messages.playground.GameReinitialized;
import com.noscompany.snake.game.online.contract.messages.playground.InitializeGame;
import com.noscompany.snake.game.online.contract.messages.playground.PlaygroundState;
import com.noscompany.snake.game.online.contract.messages.seats.AdminId;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerFreedUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerTookASeat;
import com.noscompany.snake.game.online.contract.messages.user.registry.NewUserEnteredRoom;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import snake.game.gameplay.Gameplay;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static io.vavr.control.Option.of;

@AllArgsConstructor
public class GameplaySupervisor {
    private Map<UserId, PlayerNumber> playerNumbersByIds;
    private Option<AdminId> adminIdOption;
    private final GameCreatorAdapter gameCreatorAdapter;
    private GameOptions gameOptions;
    private Gameplay gameplay;

    public InitializeGame newUserEnteredRoom(UserId userId) {
        return new InitializeGame(userId, getPlaygroundState());
    }

    public GameReinitialized playerTookASeat(UserId userId, PlayerNumber playerNumber, AdminId adminId) {
        playerNumbersByIds.put(userId, playerNumber);
        adminIdOption = Option.of(adminId);
        recreateGameIfItIsNotRunning();
        return new GameReinitialized(gameplay.getGameState());
    }

    public GameReinitialized playerFreedUpASeat(UserId userId, Option<AdminId> adminId) {
        adminIdOption = adminId;
        Option
                .of(playerNumbersByIds.remove(userId))
                .peek(gameplay::killSnake);
        recreateGameIfItIsNotRunning();
        return new GameReinitialized(gameplay.getGameState());
    }

    public GameReinitialized gameOptionsChanged(GameOptionsChanged event) {
        this.gameOptions = event.getGameOptions();
        recreateGame();
        return new GameReinitialized(gameplay.getGameState());
    }

    public Option<FailedToStartGame> startGame(UserId userId) {
        if (!userIsAdmin(userId))
            return of(FailedToStartGame.requesterIsNotAdmin(userId));
        if (gameplay.isRunning())
            return of(FailedToStartGame.gameIsAlreadyRunning(userId));
        recreateGame();
        gameplay.start();
        return Option.none();
    }

    public Option<FailedToChangeSnakeDirection> changeSnakeDirection(UserId userId, Direction direction) {
        if (!gameplay.isRunning())
            return Option.of(FailedToChangeSnakeDirection.gameNotStarted(userId));
        return findPlayerNameBy(userId)
                .toEither(FailedToChangeSnakeDirection.playerDidNotTakeASeat(userId))
                .peek(playerNumber -> gameplay.changeSnakeDirection(playerNumber, direction))
                .swap().toOption();
    }

    private Option<PlayerNumber> findPlayerNameBy(UserId userId) {
        return Option.of(playerNumbersByIds.get(userId));
    }

    public Option<FailedToCancelGame> cancelGame(UserId userId) {
        if (!userIsAdmin(userId))
            return Option.of(FailedToCancelGame.playerIsNotAdmin(userId));
        if (!gameplay.isRunning())
            return Option.of(FailedToCancelGame.gameNotStarted(userId));
        gameplay.cancel();
        return Option.none();
    }

    public Option<FailedToPauseGame> pauseGame(UserId userId) {
        if (!userIsAdmin(userId))
            return Option.of(FailedToPauseGame.playerIsNotAdmin(userId));
        if (!gameplay.isRunning())
            return Option.of(FailedToPauseGame.gameNotStarted(userId));
        gameplay.pause();
        return Option.none();
    }

    public Option<FailedToResumeGame> resumeGame(UserId userId) {
        if (!userIsAdmin(userId))
            return Option.of(FailedToResumeGame.playerIsNotAdmin(userId));
        if (!gameplay.isRunning())
            return Option.of(FailedToResumeGame.gameNotStarted(userId));
        gameplay.resume();
        return Option.none();
    }

    private boolean userIsAdmin(UserId userId) {
        return adminIdOption
                .map(adminId -> adminId.equals(AdminId.of(userId)))
                .getOrElse(false);
    }

    private void recreateGameIfItIsNotRunning() {
        if (!gameplay.isRunning())
            recreateGame();
    }

    public PlaygroundState getPlaygroundState() {
        return new PlaygroundState(gameOptions, gameplay.isRunning(), gameplay.getGameState());
    }

    private void recreateGame() {
        gameplay = gameCreatorAdapter.createGame(new HashSet<>(playerNumbersByIds.values()), gameOptions);
    }

    public void terminate() {
        gameplay.cancel();
        adminIdOption = Option.none();
        playerNumbersByIds = new HashMap<>();
    }

    public boolean gameIsRunning() {
        return gameplay.isRunning();
    }
    
    public Subscription getSubscription() {
        return new Subscription()
//                user registry events
                .toMessage(NewUserEnteredRoom.class, (NewUserEnteredRoom event) -> newUserEnteredRoom(event.getUserId()))
//                seats events
                .toMessage(PlayerTookASeat.class, (PlayerTookASeat event) -> playerTookASeat(event.getUserId(), event.getPlayerNumber(), event.getAdminId()))
                .toMessage(PlayerFreedUpASeat.class, (PlayerFreedUpASeat event) -> playerFreedUpASeat(event.getUserId(), event.getAdminId()))
//                game options commands
                .toMessage(GameOptionsChanged.class, this::gameOptionsChanged)
//                gameplay commands
                .toMessage(StartGame.class, (StartGame msg) -> startGame(msg.getUserId()))
                .toMessage(ChangeSnakeDirection.class, (ChangeSnakeDirection msg) -> changeSnakeDirection(msg.getUserId(), msg.getDirection()))
                .toMessage(CancelGame.class, (CancelGame msg) -> cancelGame(msg.getUserId()))
                .toMessage(PauseGame.class, (PauseGame msg) -> pauseGame(msg.getUserId()))
                .toMessage(ResumeGame.class, (ResumeGame msg) -> resumeGame(msg.getUserId()))
//                host events
                .toMessage(HostGotShutdown.class, (HostGotShutdown msg) -> terminate())
                .subscriberName("playground");
    }
}