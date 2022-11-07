package com.noscompany.snakejavafxclient.components.online.game.commons;

import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.events.*;
import com.noscompany.snake.game.online.contract.messages.lobby.LobbyState;
import com.noscompany.snake.game.online.contract.messages.lobby.event.*;
import com.noscompany.snake.game.online.contract.messages.room.FailedToConnectToRoom;
import com.noscompany.snake.game.online.contract.messages.room.FailedToEnterRoom;
import com.noscompany.snake.game.online.contract.messages.room.NewUserEnteredRoom;
import com.noscompany.snake.game.online.contract.messages.room.UserLeftRoom;
import com.noscompany.snakejavafxclient.components.online.game.client.EnterTheRoomController;
import com.noscompany.snakejavafxclient.components.online.game.client.EnterTheRoomStage;
import com.noscompany.snakejavafxclient.components.online.game.client.SnakeMoving;
import com.noscompany.snakejavafxclient.components.online.game.client.SnakeOnlineClientStage;
import com.noscompany.snakejavafxclient.utils.Controllers;
import com.noscompany.snake.game.online.client.ClientError;
import com.noscompany.snake.game.online.client.ClientEventHandler;
import com.noscompany.snake.game.online.client.StartingClientError;
import com.noscompany.snakejavafxclient.components.commons.scpr.buttons.ScprButtonsController;
import com.noscompany.snakejavafxclient.components.commons.message.MessageController;
import com.noscompany.snakejavafxclient.components.commons.game.grid.GameGridController;
import com.noscompany.snakejavafxclient.components.commons.scoreboard.ScoreboardController;
import com.noscompany.snakejavafxclient.components.mode.selection.OnlineModeSelectionStage;
import javafx.application.Platform;
import lombok.AllArgsConstructor;

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
    private final ScprButtonsController scprButtonsController;

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
                Controllers.get(ScprButtonsController.class));
    }

    @Override
    public void handle(GameOptionsChanged event) {
        Platform.runLater(() -> update(event.getLobbyState()));
    }

    @Override
    public void handle(PlayerTookASeat event) {
        Platform.runLater(() -> update(event.getLobbyState()));
    }

    @Override
    public void handle(PlayerFreedUpASeat event) {
        Platform.runLater(() -> update(event.getLobbyState()));
    }

    @Override
    public void handle(FailedToStartGame event) {
        Platform.runLater(() -> {});
    }

    @Override
    public void handle(FailedToTakeASeat event) {
        Platform.runLater(() -> {});
    }

    @Override
    public void handle(FailedToChangeGameOptions event) {
        Platform.runLater(() -> {});
    }

    @Override
    public void handle(UserSentChatMessage event) {
        Platform.runLater(() -> chatController.print(event));
    }

    @Override
    public void handle(TimeLeftToGameStartHasChanged event) {
        Platform.runLater(() -> {
            SnakeMoving.gameIsRunning();
            gameGridController.initializeGrid(event.getGridSize(), event.getWalls());
            gameGridController.updateGrid(event.getSnakes(), event.getFoodPosition());
            onlineGameOptionsController.disable();
            messageController.printSecondsLeftToStart(event.getSecondsLeft());
            scoreboardController.clear();
            scprButtonsController.disableStart();
            scprButtonsController.enableCancel();
            scprButtonsController.enablePause();
        });
    }

    @Override
    public void handle(GameStarted event) {
        Platform.runLater(() -> {
            SnakeMoving.gameIsRunning();
            gameGridController.initializeGrid(event.getGridSize(), event.getWalls());
            gameGridController.updateGrid(event.getSnakes(), event.getFoodPosition());
            onlineGameOptionsController.disable();
            scoreboardController.print(event.getScore());
            scprButtonsController.disableStart();
            scprButtonsController.enableCancel();
            scprButtonsController.enablePause();
            messageController.clear();
        });
    }

    @Override
    public void handle(GameContinues event) {
        Platform.runLater(() -> {
            SnakeMoving.gameIsRunning();
            gameGridController.updateGrid(event.getSnakes(), event.getFoodPosition());
            scoreboardController.print(event.getScore());
        });
    }

    @Override
    public void handle(GameFinished event) {
        Platform.runLater(() -> {
            SnakeMoving.gameIsNotRunning();
            gameGridController.updateGrid(event.getSnakes(), event.getFoodPosition());
            onlineGameOptionsController.enable();
            messageController.printGameFinished();
            scoreboardController.print(event.getScore());
            scprButtonsController.enableStart();
            scprButtonsController.disableCancel();
            scprButtonsController.disableResume();
            scprButtonsController.disablePause();
        });
    }

    @Override
    public void handle(GameCancelled event) {
        Platform.runLater(() -> {
            SnakeMoving.gameIsNotRunning();
            onlineGameOptionsController.enable();
            messageController.printGameCanceled();
            scprButtonsController.enableStart();
            scprButtonsController.disableCancel();
            scprButtonsController.disableResume();
            scprButtonsController.disablePause();
        });
    }

    @Override
    public void handle(GamePaused event) {
        Platform.runLater(() -> {
            SnakeMoving.gameIsRunning();
            messageController.printGamePaused();
            scprButtonsController.enableResume();
            scprButtonsController.disablePause();
        });
    }

    @Override
    public void handle(GameResumed event) {
        Platform.runLater(() -> {
            SnakeMoving.gameIsRunning();
            messageController.printGameResumed();
            scprButtonsController.disableResume();
            scprButtonsController.enablePause();
        });
    }

    @Override
    public void connectionEstablished() {
        SnakeOnlineClientStage.get().show();
        EnterTheRoomStage.get().show();
    }

    @Override
    public void handle(ClientError clientError) {
        Platform.runLater(() -> {
        });
    }

    @Override
    public void handle(StartingClientError startingClientError) {
        Platform.runLater(() -> {
        });
    }

    @Override
    public void connectionClosed() {
        Platform.runLater(() -> {
            SnakeOnlineClientStage.get().close();
            OnlineModeSelectionStage.get().show();
        });
    }

    @Override
    public void handle(NewUserEnteredRoom event) {
        Platform.runLater(() -> {
            enterTheRoomController.handle(event);
            joinedUsersController.update(event.getConnectedUsers());
        });
    }

    @Override
    public void handle(FailedToConnectToRoom event) {
        Platform.runLater(() -> enterTheRoomController.handle(event));
    }

    @Override
    public void handle(FailedToEnterRoom event) {
        Platform.runLater(() -> enterTheRoomController.handle(event));
    }

    @Override
    public void handle(FailedToFreeUpSeat event) {
        Platform.runLater(() -> {
        });
    }

    @Override
    public void handle(FailedToSendChatMessage event) {
        Platform.runLater(() -> {
        });
    }

    @Override
    public void handle(UserLeftRoom event) {
        Platform.runLater(() -> {
            joinedUsersController.update(event.getUsersList());
            event.getPlayerFreedUpASeat().peek(this::handle);
        });
    }

    private void update(LobbyState lobbyState) {
        onlineGameOptionsController.update(lobbyState);
        lobbySeatsController.update(lobbyState);
        scoreboardController.update(lobbyState);
        gameGridController.update(lobbyState.getGameState());
        messageController.clear();
    }
}