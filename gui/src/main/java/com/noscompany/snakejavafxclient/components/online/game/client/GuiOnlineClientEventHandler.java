package com.noscompany.snakejavafxclient.components.online.game.client;

import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptionsChanged;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.*;
import com.noscompany.snake.game.online.contract.messages.playground.PlaygroundState;
import com.noscompany.snake.game.online.contract.messages.room.*;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToFreeUpSeat;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerFreedUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerTookASeat;
import com.noscompany.snake.game.online.contract.messages.server.InitializeRemoteClientState;
import com.noscompany.snakejavafxclient.components.online.game.commons.*;
import com.noscompany.snakejavafxclient.utils.Controllers;
import com.noscompany.snake.game.online.client.SendClientMessageError;
import com.noscompany.snake.game.online.client.ClientEventHandler;
import com.noscompany.snake.game.online.client.StartingClientError;
import com.noscompany.snakejavafxclient.components.commons.scpr.buttons.ScprButtonsController;
import com.noscompany.snakejavafxclient.components.commons.message.MessageController;
import com.noscompany.snakejavafxclient.components.commons.game.grid.GameGridController;
import com.noscompany.snakejavafxclient.components.commons.scoreboard.ScoreboardController;
import javafx.application.Platform;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GuiOnlineClientEventHandler implements ClientEventHandler {
    private final JoinGameController joinGameController;
    private final OnlineGameOptionsController onlineGameOptionsController;
    private final LobbySeatsController lobbySeatsController;
    private final GameGridController gameGridController;
    private final ChatController chatController;
    private final JoinedUsersController joinedUsersController;
    private final MessageController messageController;
    private final ScoreboardController scoreboardController;
    private final ScprButtonsController scprButtonsController;

    public static GuiOnlineClientEventHandler instance() {
        JoinGameStage.get();
        SnakeOnlineClientStage.get();
        return new GuiOnlineClientEventHandler(
                Controllers.get(JoinGameController.class),
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
        Platform.runLater(() -> update(event.getPlaygroundState()));
    }

    @Override
    public void handle(PlayerTookASeat event) {
        Platform.runLater(() -> update(event.getPlaygroundState()));
    }

    @Override
    public void handle(PlayerFreedUpASeat event) {
        Platform.runLater(() -> update(event.getPlaygroundState()));
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
    public void handle(SnakesMoved event) {
        Platform.runLater(() -> {
            gameGridController.updateGrid(event.getSnakes(), event.getFoodPosition());
            scoreboardController.print(event.getScore());
        });
    }

    @Override
    public void handle(GameFinished event) {
        Platform.runLater(() -> {
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
            messageController.printGamePaused();
            scprButtonsController.enableResume();
            scprButtonsController.disablePause();
        });
    }

    @Override
    public void handle(GameResumed event) {
        Platform.runLater(() -> {
            messageController.printGameResumed();
            scprButtonsController.disableResume();
            scprButtonsController.enablePause();
        });
    }

    @Override
    public void connectionEstablished() {
        Platform.runLater(() -> {
            joinGameController.connectionEstablished();
        });
    }

    @Override
    public void handle(InitializeRemoteClientState event) {
        Platform.runLater(() -> {
            RoomState roomState = event.getRoomState();
            joinedUsersController.update(roomState.getUsers());
            update(roomState.getPlaygroundState());
            joinGameController.enterRoom();
        });
    }

    @Override
    public void handle(SendClientMessageError sendClientMessageError) {
        Platform.runLater(() -> {
            joinGameController.handle(sendClientMessageError);
        });
    }

    @Override
    public void handle(StartingClientError startingClientError) {
        Platform.runLater(() -> {
            joinGameController.handle(startingClientError);
        });
    }

    @Override
    public void connectionClosed() {
        Platform.runLater(() -> {
            SnakeOnlineClientStage.get().close();
            joinGameController.connectionClosed();
        });
    }

    @Override
    public void handle(NewUserEnteredRoom event) {
        Platform.runLater(() -> {
            RoomState roomState = event.getRoomState();
            joinGameController.handle(event);
            joinedUsersController.update(roomState.getUsers());
            update(roomState.getPlaygroundState());
        });
    }

    @Override
    public void handle(FailedToConnectToRoom event) {
        Platform.runLater(() -> {});
    }

    @Override
    public void handle(FailedToEnterRoom event) {
        Platform.runLater(() -> joinGameController.handle(event));
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

    private void update(PlaygroundState playgroundState) {
        onlineGameOptionsController.update(playgroundState.getGameOptions());
        lobbySeatsController.update(playgroundState.getSeats());
        scoreboardController.update(playgroundState);
        gameGridController.update(playgroundState.getGameState());
        messageController.clear();
    }
}