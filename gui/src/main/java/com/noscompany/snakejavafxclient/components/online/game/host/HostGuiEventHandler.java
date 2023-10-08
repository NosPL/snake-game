package com.noscompany.snakejavafxclient.components.online.game.host;

import com.noscompany.message.publisher.Subscription;
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
import com.noscompany.snake.game.online.contract.messages.server.events.FailedToStartServer;
import com.noscompany.snake.game.online.contract.messages.server.events.ServerFailedToSendMessageToRemoteClients;
import com.noscompany.snake.game.online.contract.messages.server.events.ServerGotShutdown;
import com.noscompany.snake.game.online.contract.messages.server.events.ServerStarted;
import com.noscompany.snakejavafxclient.components.commons.game.grid.GameGridController;
import com.noscompany.snakejavafxclient.components.commons.message.MessageController;
import com.noscompany.snakejavafxclient.components.commons.scoreboard.ScoreboardController;
import com.noscompany.snakejavafxclient.components.commons.scpr.buttons.ScprButtonsController;
import com.noscompany.snakejavafxclient.components.online.game.commons.ChatController;
import com.noscompany.snakejavafxclient.components.online.game.commons.JoinedUsersController;
import com.noscompany.snakejavafxclient.components.online.game.commons.LobbySeatsController;
import com.noscompany.snakejavafxclient.components.online.game.commons.OnlineGameOptionsController;
import javafx.application.Platform;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
class HostGuiEventHandler {
    @NonNull private final SetupHostController setupHostController;
    @NonNull private final ServerController serverController;
    @NonNull private final OnlineGameOptionsController onlineGameOptionsController;
    @NonNull private final LobbySeatsController lobbySeatsController;
    @NonNull private final GameGridController gameGridController;
    @NonNull private final ChatController chatController;
    @NonNull private final JoinedUsersController joinedUsersController;
    @NonNull private final MessageController messageController;
    @NonNull private final ScoreboardController scoreboardController;
    @NonNull private final ScprButtonsController scprButtonsController;
    @NonNull private final UserId hostId;

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
        Platform.runLater(() -> joinedUsersController.update(event.getRoomState().getUsers()));
        if (event.getUserId().equals(hostId))
            setupHostController.hostEnteredRoom();
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

    Subscription createSubscription() {
        return new Subscription()
                .subscriberName("host gui event handler")
//                user registry events
                .toMessage(NewUserEnteredRoom.class, this::newUserEnteredRoom)
                .toMessage(FailedToEnterRoom.class, this::failedToEnterRoom)
                .toMessage(UserLeftRoom.class, this::userLeftRoom)
//                chat events
                .toMessage(UserSentChatMessage.class, this::userSentChatMessage)
                .toMessage(FailedToSendChatMessage.class, this::failedToSendChatMessage)
//                game options events
                .toMessage(GameOptionsChanged.class, this::gameOptionsChanged)
                .toMessage(FailedToChangeGameOptions.class, this::failedToChangeGameOptions)
//                seats events
                .toMessage(PlayerTookASeat.class, this::playerTookASeat)
                .toMessage(FailedToTakeASeat.class, this::failedToTakeASeat)
                .toMessage(PlayerFreedUpASeat.class, this::playerFreedUpASeat)
                .toMessage(FailedToFreeUpSeat.class, this::failedToFreeUpSeat)
//                gameplay events
                .toMessage(GameStarted.class, this::gameStarted)
                .toMessage(FailedToStartGame.class, this::failedToStartGame)
                .toMessage(GameStartCountdown.class, this::gameStartCountdown)
                .toMessage(SnakesMoved.class, this::snakesMoved)
                .toMessage(GameFinished.class, this::gameFinished)
                .toMessage(GameCancelled.class, this::gameCancelled)
                .toMessage(GamePaused.class, this::gamePaused)
                .toMessage(GameResumed.class, this::gameResumed)
//                server events
                .toMessage(ServerStarted.class, this::serverStarted)
                .toMessage(FailedToStartServer.class, this::failedToStartServer)
                .toMessage(ServerFailedToSendMessageToRemoteClients.class, this::serverFailedToSendMessageToRemoteClient)
                .toMessage(ServerGotShutdown.class, this::serverGotShutdown);
    }
}