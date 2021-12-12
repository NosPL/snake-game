package com.noscompany.snakejavafxclient.game.online;

import com.noscompany.snake.game.commons.messages.events.lobby.*;
import com.noscompany.snakejavafxclient.game.ButtonsController;
import com.noscompany.snakejavafxclient.game.MessageController;
import com.noscompany.snakejavafxclient.game.scoreboard.controller.ScoreboardController;
import com.noscompany.snakejavafxclient.game.grid.controller.GameGridController;
import javafx.application.Platform;
import lombok.AllArgsConstructor;
import snake.game.core.SnakeGameEventHandler;
import snake.game.core.events.*;

@AllArgsConstructor
public class GuiLobbyEventHandler implements LobbyEventHandler, SnakeGameEventHandler {
    protected final OnlineGameOptionsController onlineGameOptionsController;
    protected final LobbySeatsController lobbySeatsController;
    protected final GameGridController gameGridController;
    protected final ChatController chatController;
    protected final JoinedUsersController joinedUsersController;
    protected final MessageController messageController;
    protected final ScoreboardController scoreboardController;
    protected final ButtonsController buttonsController;


    @Override
    public void handle(NewUserAdded event) {
        Platform.runLater(() -> {
            joinedUsersController.add(event.getUserId());
            gameGridController.update(event.getGameLobbyState().getGameState());
        });
    }

    @Override
    public void handle(NewUserConnectedAsAdmin event) {
        Platform.runLater(() ->
                joinedUsersController.add(event.getUserId()));
    }

    @Override
    public void handle(UserRemoved event) {
        Platform.runLater(() ->
                joinedUsersController.remove(event.getUserId()));
    }

    @Override
    public void handle(GameOptionsChanged event) {
        Platform.runLater(() -> {
            onlineGameOptionsController.update(event.getGameSpeed(), event.getGridSize(), event.getWalls());
            gameGridController.update(event.getGameLobbyState().getGameState());
        });

    }

    @Override
    public void handle(PlayerTookASeat event) {
        Platform.runLater(() -> {
            lobbySeatsController.updateSeats(event.getGameLobbyState().getJoinedPlayers());
            gameGridController.update(event.getGameLobbyState().getGameState());
        });
    }

    @Override
    public void handle(PlayerFreedUpASeat event) {
        Platform.runLater(() -> {
            messageController.clear();
            lobbySeatsController.seatFreedUp(event.getFreedUpSnakeNumber());
            gameGridController.update(event.getGameLobbyState().getGameState());
        });
    }

    @Override
    public void handle(FailedToStartGame event) {
        Platform.runLater(() -> messageController.print(event.getReason()));
    }

    @Override
    public void handle(FailedToTakeASeat event) {
        Platform.runLater(() -> messageController.print(event.getReason()));
    }

    @Override
    public void handle(FailedToChangeGameOptions event) {
        Platform.runLater(() -> {
            messageController.print(event.getReason());
            onlineGameOptionsController.update(event.getOldGameSpeedOption(), event.getOldGridSizeOption(), event.getOldWallsOption());
        });
    }

    @Override
    public void handle(ChatMessageReceived event) {
        Platform.runLater(() ->
                chatController
                        .messageReceived(
                                event.getChatMessageAuthor(),
                                event.getChatMessageContent()));
    }

    @Override
    public void handle(TimeLeftToGameStartHasChanged event) {
        Platform.runLater(() -> {
            gameGridController.initializeGrid(event.getGridSize());
            gameGridController.updateGrid(event.getSnakes(), event.getFoodPoint());
            onlineGameOptionsController.disable();
            messageController.printSecondsLeftToStart(event.getSecondsLeft());
            scoreboardController.clear();
            buttonsController.disableStart();
            buttonsController.enableCancel();
            buttonsController.enablePause();
        });
    }

    @Override
    public void handle(GameStarted event) {
        Platform.runLater(() -> {
            gameGridController.initializeGrid(event.getGridSize());
            gameGridController.updateGrid(event.getSnakes(), event.getFoodPoint());
            onlineGameOptionsController.disable();
            scoreboardController.print(event.getScore());
            buttonsController.disableStart();
            buttonsController.enableCancel();
            buttonsController.enablePause();
            messageController.clear();
        });
    }

    @Override
    public void handle(GameContinues event) {
        Platform.runLater(() -> {
            gameGridController.updateGrid(event.getSnakes(), event.getFoodPoint());
            scoreboardController.print(event.getScore());
        });
    }

    @Override
    public void handle(GameFinished event) {
        Platform.runLater(() -> {
            gameGridController.updateGrid(event.getSnakes(), event.getFoodPoint());
            onlineGameOptionsController.enable();
            messageController.printGameFinished();
            scoreboardController.print(event.getScore());
            buttonsController.enableStart();
            buttonsController.disableCancel();
            buttonsController.disableResume();
            buttonsController.disablePause();
        });
    }

    @Override
    public void handle(GameCancelled event) {
        Platform.runLater(() -> {
            onlineGameOptionsController.enable();
            messageController.printGameCanceled();
            buttonsController.enableStart();
            buttonsController.disableCancel();
            buttonsController.disableResume();
            buttonsController.disablePause();
        });
    }

    @Override
    public void handle(GamePaused event) {
        Platform.runLater(() -> {
            messageController.printGamePaused();
            buttonsController.enableResume();
            buttonsController.disablePause();
        });
    }

    @Override
    public void handle(GameResumed event) {
        Platform.runLater(() -> {
            messageController.printGameResumed();
            buttonsController.disableResume();
            buttonsController.enablePause();
        });
    }
}