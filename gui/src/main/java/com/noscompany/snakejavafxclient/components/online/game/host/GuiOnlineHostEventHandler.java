package com.noscompany.snakejavafxclient.components.online.game.host;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptionsChanged;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.*;
import com.noscompany.snake.game.online.contract.messages.playground.PlaygroundState;
import com.noscompany.snake.game.online.contract.messages.room.FailedToEnterRoom;
import com.noscompany.snake.game.online.contract.messages.room.NewUserEnteredRoom;
import com.noscompany.snake.game.online.contract.messages.room.UserLeftRoom;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToFreeUpSeat;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerFreedUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerTookASeat;
import com.noscompany.snake.game.online.contract.messages.server.FailedToStartServer;
import com.noscompany.snake.game.online.contract.messages.server.ServerFailedToSendMessageToRemoteClients;
import com.noscompany.snake.game.online.contract.messages.server.ServerGotShutdown;
import com.noscompany.snake.game.online.contract.messages.server.ServerStarted;
import com.noscompany.snakejavafxclient.components.commons.game.grid.GameGridController;
import com.noscompany.snakejavafxclient.components.commons.message.MessageController;
import com.noscompany.snakejavafxclient.components.commons.scoreboard.ScoreboardController;
import com.noscompany.snakejavafxclient.components.commons.scpr.buttons.ScprButtonsController;
import com.noscompany.snakejavafxclient.components.online.game.commons.ChatController;
import com.noscompany.snakejavafxclient.components.online.game.commons.JoinedUsersController;
import com.noscompany.snakejavafxclient.components.online.game.commons.LobbySeatsController;
import com.noscompany.snakejavafxclient.components.online.game.commons.OnlineGameOptionsController;
import com.noscompany.snakejavafxclient.utils.Controllers;
import javafx.application.Platform;
import lombok.AllArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
class GuiOnlineHostEventHandler {
    private final SetupHostController setupHostController;
    private final ServerController serverController;
    private final OnlineGameOptionsController onlineGameOptionsController;
    private final LobbySeatsController lobbySeatsController;
    private final GameGridController gameGridController;
    private final ChatController chatController;
    private final JoinedUsersController joinedUsersController;
    private final MessageController messageController;
    private final ScoreboardController scoreboardController;
    private final ScprButtonsController scprButtonsController;
    private final UserId hostId;

    static GuiOnlineHostEventHandler instance(UserId userId) {
        return new GuiOnlineHostEventHandler(
                Controllers.get(SetupHostController.class),
                Controllers.get(ServerController.class),
                Controllers.get(OnlineGameOptionsController.class),
                Controllers.get(LobbySeatsController.class),
                Controllers.get(GameGridController.class),
                Controllers.get(ChatController.class),
                Controllers.get(JoinedUsersController.class),
                Controllers.get(MessageController.class),
                Controllers.get(ScoreboardController.class),
                Controllers.get(ScprButtonsController.class),
                userId);
    }

    public void gameOptionsChanged(GameOptionsChanged event) {
        Platform.runLater(() -> update(event.getPlaygroundState()));
    }

    public void playerTookASeat(PlayerTookASeat event) {
        Platform.runLater(() -> update(event.getPlaygroundState()));
    }

    public void playerFreedUpASeat(PlayerFreedUpASeat event) {
        Platform.runLater(() -> update(event.getPlaygroundState()));
    }

    public void failedToStartGame(FailedToStartGame event) {
        if (event.getUserId().equals(hostId))
            Platform.runLater(() -> {
            });
    }

    public void failedToTakeASeat(FailedToTakeASeat event) {
        if (event.getUserId().equals(hostId))
            Platform.runLater(() -> {
            });
    }

    public void failedToChangeGameOptions(FailedToChangeGameOptions event) {
        if (event.getUserId().equals(hostId))
            Platform.runLater(() -> {
            });
    }

    public void userSentChatMessage(UserSentChatMessage event) {
        Platform.runLater(() -> chatController.print(event));
    }

    public void gameStartCountdown(GameStartCountdown event) {
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

    public void gameStarted(GameStarted event) {
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

    public void snakesMoved(SnakesMoved event) {
        Platform.runLater(() -> {
            gameGridController.updateGrid(event.getSnakes(), event.getFoodPosition());
            scoreboardController.print(event.getScore());
        });
    }

    public void gameFinished(GameFinished event) {
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

    public void gameCancelled(GameCancelled event) {
        Platform.runLater(() -> {
            onlineGameOptionsController.enable();
            messageController.printGameCanceled();
            scprButtonsController.enableStart();
            scprButtonsController.disableCancel();
            scprButtonsController.disableResume();
            scprButtonsController.disablePause();
        });
    }

    public void gamePaused(GamePaused event) {
        Platform.runLater(() -> {
            messageController.printGamePaused();
            scprButtonsController.enableResume();
            scprButtonsController.disablePause();
        });
    }

    public void gameResumed(GameResumed event) {
        Platform.runLater(() -> {
            messageController.printGameResumed();
            scprButtonsController.disableResume();
            scprButtonsController.enablePause();
        });
    }

    public void newUserEnteredRoom(NewUserEnteredRoom event) {
        if (event.getUserId().equals(hostId))
            hostEnteredRoom();
        else
            Platform.runLater(() -> joinedUsersController.update(event.getRoomState().getUsers()));
    }

    public void failedToEnterRoom(FailedToEnterRoom event) {
        if (event.getUserId().equals(hostId))
            Platform.runLater(() -> setupHostController.handle(event));
    }

    public void failedToFreeUpSeat(FailedToFreeUpSeat event) {
        if (event.getUserId().equals(hostId))
            Platform.runLater(() -> {
            });
    }

    public void failedToSendChatMessage(FailedToSendChatMessage event) {
        if (event.getUserId().equals(hostId))
            Platform.runLater(() -> {
            });
    }

    public void userLeftRoom(UserLeftRoom event) {
        Platform.runLater(() -> {
            joinedUsersController.update(event.getUsersList());
            event.getPlayerFreedUpASeat().peek(this::playerFreedUpASeat);
        });
    }

    public void serverGotShutdown(ServerGotShutdown event) {
        Platform.runLater(() -> SnakeOnlineHostStage.get().close());
    }

    private void hostEnteredRoom() {
        setupHostController.hostEnteredRoom();
    }

    private void update(PlaygroundState playgroundState) {
        onlineGameOptionsController.update(playgroundState.getGameOptions());
        lobbySeatsController.update(playgroundState.getSeats());
        scoreboardController.update(playgroundState);
        gameGridController.update(playgroundState.getGameState());
        messageController.clear();
    }

    public void failedToStartServer(FailedToStartServer serverFailedToStart) {
        Platform.runLater(() -> setupHostController.handle(serverFailedToStart));
    }

    public void serverFailedToSendMessageToRemoteClient(ServerFailedToSendMessageToRemoteClients event) {
        Platform.runLater(() -> serverController.handle(event));
    }

    public void serverStarted(ServerStarted serverStarted) {
        Platform.runLater(() -> {
            serverController.serverStarted(serverStarted.getServerParams());
            setupHostController.handle(serverStarted);
        });
    }
}