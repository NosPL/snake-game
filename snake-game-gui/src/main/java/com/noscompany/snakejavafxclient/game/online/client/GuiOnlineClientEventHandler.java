package com.noscompany.snakejavafxclient.game.online.client;

import com.noscompany.snake.game.commons.messages.dto.GameLobbyState;
import com.noscompany.snake.game.commons.messages.events.chat.ChatMessageReceived;
import com.noscompany.snake.game.commons.messages.events.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.commons.messages.events.lobby.*;
import com.noscompany.snake.game.commons.messages.events.room.FailedToEnterRoom;
import com.noscompany.snake.game.commons.messages.events.room.NewUserEnteredRoom;
import com.noscompany.snake.game.commons.messages.events.room.UserLeftRoom;
import com.noscompany.snake.game.online.client.ClientError;
import com.noscompany.snake.game.online.client.ClientEventHandler;
import com.noscompany.snake.game.online.client.StartingClientError;
import com.noscompany.snakejavafxclient.commons.Controllers;
import com.noscompany.snakejavafxclient.commons.Stages;
import com.noscompany.snakejavafxclient.game.ButtonsController;
import com.noscompany.snakejavafxclient.game.MessageController;
import com.noscompany.snakejavafxclient.game.grid.controller.GameGridController;
import com.noscompany.snakejavafxclient.game.scoreboard.controller.ScoreboardController;
import javafx.application.Platform;
import lombok.AllArgsConstructor;
import snake.game.core.events.*;

@AllArgsConstructor
public class GuiOnlineClientEventHandler implements ClientEventHandler {
    private final EnterTheRoomController enterTheRoomController;
    private final OnlineGameOptionsController onlineGameOptionsController;
    private final LobbySeatsController lobbySeatsController;
    private final GameGridController gameGridController;
    private final ChatController chatController;
    private final JoinedUsersController joinedUsersController;
    private final MessageController messageController;
    private final ScoreboardController scoreboardController;
    private final ButtonsController buttonsController;

    public static GuiOnlineClientEventHandler instance() {
        return new GuiOnlineClientEventHandler(
                Controllers.get(EnterTheRoomController.class),
                Controllers.get(OnlineGameOptionsController.class),
                Controllers.get(LobbySeatsController.class),
                Controllers.get(GameGridController.class),
                Controllers.get(ChatController.class),
                Controllers.get(JoinedUsersController.class),
                Controllers.get(MessageController.class),
                Controllers.get(ScoreboardController.class),
                Controllers.get(ButtonsController.class));
    }

    @Override
    public void handle(GameOptionsChanged event) {
        Platform.runLater(() -> update(event.getGameLobbyState()));

    }

    @Override
    public void handle(PlayerTookASeat event) {
        Platform.runLater(() -> update(event.getGameLobbyState()));
    }

    @Override
    public void handle(PlayerFreedUpASeat event) {
        Platform.runLater(() -> update(event.getGameLobbyState()));
    }

    @Override
    public void handle(FailedToStartGame event) {
        Platform.runLater(() -> update(event.getLobbyState()));
    }

    @Override
    public void handle(FailedToTakeASeat event) {
        Platform.runLater(() -> update(event.getLobbyState()));
    }

    @Override
    public void handle(FailedToChangeGameOptions event) {
        Platform.runLater(() -> update(event.getGameLobbyState()));
    }

    @Override
    public void handle(ChatMessageReceived event) {
        Platform.runLater(() -> chatController.print(event));
    }

    @Override
    public void handle(TimeLeftToGameStartHasChanged event) {
        Platform.runLater(() -> {
            SnakeMoving.gameIsRunning();
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
            SnakeMoving.gameIsRunning();
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
            SnakeMoving.gameIsRunning();
            gameGridController.updateGrid(event.getSnakes(), event.getFoodPoint());
            scoreboardController.print(event.getScore());
        });
    }

    @Override
    public void handle(GameFinished event) {
        Platform.runLater(() -> {
            SnakeMoving.gameIsNotRunning();
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
            SnakeMoving.gameIsNotRunning();
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
            SnakeMoving.gameIsRunning();
            messageController.printGamePaused();
            buttonsController.enableResume();
            buttonsController.disablePause();
        });
    }

    @Override
    public void handle(GameResumed event) {
        Platform.runLater(() -> {
            SnakeMoving.gameIsRunning();
            messageController.printGameResumed();
            buttonsController.disableResume();
            buttonsController.enablePause();
        });
    }

    @Override
    public void connectionEstablished() {
        Stages.getSnakeOnlineClientStage().show();
        Stages.getEnterRoomStage().show();
    }

    @Override
    public void handle(ClientError clientError) {
        Platform.runLater(() -> {});
    }

    @Override
    public void handle(StartingClientError startingClientError) {
        Platform.runLater(() -> {});
    }

    @Override
    public void connectionClosed() {
        Stages.getSnakeOnlineClientStage().close();
    }

    @Override
    public void handle(NewUserEnteredRoom event) {
        Platform.runLater(() -> {
            enterTheRoomController.handle(event);
            joinedUsersController.update(event.getConnectedUsers());
        });
    }

    private void update(GameLobbyState gameLobbyState) {
        onlineGameOptionsController.update(gameLobbyState);
        lobbySeatsController.update(gameLobbyState);
        scoreboardController.update(gameLobbyState);
        gameGridController.update(gameLobbyState.getGameState());
        messageController.clear();
    }

    @Override
    public void handle(FailedToEnterRoom event) {
        Platform.runLater(() -> enterTheRoomController.handle(event));
    }

    @Override
    public void handle(FailedToFreeUpSeat event) {
        Platform.runLater(() -> {});
    }

    @Override
    public void handle(FailedToSendChatMessage event) {
        Platform.runLater(() -> {});
    }

    @Override
    public void handle(UserLeftRoom event) {
        Platform.runLater(() -> {
            joinedUsersController.update(event.getUsersList());
            event.getPlayerFreedUpASeat().peek(this::handle);
        });
    }
}