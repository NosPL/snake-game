package com.noscompany.snakejavafxclient.components.online.game.client;

import com.noscompany.snake.game.online.client.*;
import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptionsChanged;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.*;
import com.noscompany.snake.game.online.contract.messages.playground.GameReinitialized;
import com.noscompany.snake.game.online.contract.messages.playground.PlaygroundState;
import com.noscompany.snake.game.online.contract.messages.seats.*;
import com.noscompany.snake.game.online.contract.messages.playground.InitializePlaygroundToRemoteClient;
import com.noscompany.snake.game.online.contract.messages.server.events.ServerGotShutdown;
import com.noscompany.snake.game.online.contract.messages.user.registry.FailedToEnterRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.NewUserEnteredRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserLeftRoom;
import com.noscompany.snakejavafxclient.components.online.game.commons.*;
import com.noscompany.snakejavafxclient.utils.Controllers;
import com.noscompany.snakejavafxclient.components.commons.message.MessageController;
import com.noscompany.snakejavafxclient.components.commons.game.grid.GameGridController;
import com.noscompany.snakejavafxclient.components.commons.scoreboard.ScoreboardController;
import io.vavr.control.Option;
import javafx.application.Platform;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class GuiOnlineClientEventHandler implements ClientEventHandler {
    @NonNull private final FleetingMessageController fleetingMessageController;
    @NonNull private final JoinGameController joinGameController;
    @NonNull private final OnlineGameOptionsController onlineGameOptionsController;
    @NonNull private final LobbySeatsController lobbySeatsController;
    @NonNull private final GameGridController gameGridController;
    @NonNull private final ChatController chatController;
    @NonNull private final JoinedUsersController joinedUsersController;
    @NonNull private final MessageController messageController;
    @NonNull private final ScoreboardController scoreboardController;

    public static GuiOnlineClientEventHandler instance() {
        JoinGameStage.get();
        SnakeOnlineClientStage.get();
        return new GuiOnlineClientEventHandler(
                Controllers.get(FleetingMessageController.class),
                Controllers.get(JoinGameController.class),
                Controllers.get(OnlineGameOptionsController.class),
                Controllers.get(LobbySeatsController.class),
                Controllers.get(GameGridController.class),
                Controllers.get(ChatController.class),
                Controllers.get(JoinedUsersController.class),
                Controllers.get(MessageController.class),
                Controllers.get(ScoreboardController.class));
    }

    @Override
    public void playerTookASeat(PlayerTookASeat event) {
        Platform.runLater(() -> {
            gameGridController.handle(event);
            lobbySeatsController.update(event.getSeats(), Option.of(event.getAdminId()));
        });
    }

    @Override
    public void failedToTakeASeat(FailedToTakeASeat event) {
        Platform.runLater(() -> fleetingMessageController.print(event.getReason()));
    }

    @Override
    public void playerFreedUpASeat(PlayerFreedUpASeat event) {
        Platform.runLater(() -> {
            gameGridController.handle(event);
            lobbySeatsController.update(event.getSeats(), event.getAdminId());
        });
    }

    @Override
    public void gameOptionsChanged(GameOptionsChanged event) {
        Platform.runLater(() -> {
            gameGridController.handle(event);
            update(event.getPlaygroundState());
        });
    }

    @Override
    public void failedToChangeGameOptions(FailedToChangeGameOptions event) {
        Platform.runLater(() -> fleetingMessageController.print(event.getReason()));
    }

    @Override
    public void failedToStartGame(FailedToStartGame event) {
        Platform.runLater(() -> fleetingMessageController.print(event.getReason()));
    }

    @Override
    public void failedToPauseGame(FailedToPauseGame event) {
        Platform.runLater(() -> fleetingMessageController.print(event.getReason()));
    }

    @Override
    public void failedToResumeGame(FailedToResumeGame event) {
        Platform.runLater(() -> fleetingMessageController.print(event.getReason()));
    }

    @Override
    public void serverGotShutdown(ServerGotShutdown event) {
        Platform.runLater(() -> {
            SnakeOnlineClientStage.get().close();
            joinGameController.connectionClosed();
        });
    }

    @Override
    public void failedToCancelGame(FailedToCancelGame event) {
        Platform.runLater(() -> fleetingMessageController.print(event.getReason()));
    }

    @Override
    public void userSentChatMessage(UserSentChatMessage event) {
        Platform.runLater(() -> chatController.print(event));
    }

    @Override
    public void gameStartCountdown(GameStartCountdown event) {
        Platform.runLater(() -> {
            gameGridController.handle(event);
            onlineGameOptionsController.disable();
            messageController.printSecondsLeftToStart(event.getSecondsLeft());
            scoreboardController.clear();
        });
    }

    @Override
    public void gameStarted(GameStarted event) {
        Platform.runLater(() -> {
            gameGridController.handle(event);
            onlineGameOptionsController.disable();
            scoreboardController.print(event.getScore());
            messageController.clear();
        });
    }

    @Override
    public void snakesMoved(SnakesMoved event) {
        Platform.runLater(() -> {
            gameGridController.handle(event);
            scoreboardController.print(event.getScore());
        });
    }

    @Override
    public void gameFinished(GameFinished event) {
        Platform.runLater(() -> {
            gameGridController.handle(event);
            onlineGameOptionsController.enable();
            messageController.printGameFinished();
            scoreboardController.print(event.getScore());
        });
    }

    @Override
    public void gameCancelled(GameCancelled event) {
        Platform.runLater(() -> {
            onlineGameOptionsController.enable();
            messageController.printGameCanceled();
        });
    }

    @Override
    public void gamePaused(GamePaused event) {
        Platform.runLater(messageController::printGamePaused);
    }

    @Override
    public void gameResumed(GameResumed event) {
        Platform.runLater(messageController::printGameResumed);
    }

    @Override
    public void connectionEstablished(ConnectionEstablished event) {
        Platform.runLater(joinGameController::connectionEstablished);
    }

    @Override
    public void initializePlayground(InitializePlaygroundToRemoteClient command) {
        Platform.runLater(() -> {
            gameGridController.handle(command);
            update(command.getPlaygroundState());
            joinGameController.enterRoom();
        });
    }

    @Override
    public void gameReinitialized(GameReinitialized event) {
        Platform.runLater(() -> {
            scoreboardController.update(event.getGameState());
            gameGridController.handle(event);
        });
    }

    @Override
    public void sendClientMessage(SendClientMessageError sendClientMessageError) {
        Platform.runLater(() -> joinGameController.handle(sendClientMessageError));
    }

    @Override
    public void startingClientError(StartingClientError startingClientError) {
        Platform.runLater(() -> joinGameController.handle(startingClientError));
    }

    @Override
    public void connectionClosed(ConnectionClosed event) {
        Platform.runLater(() -> {
            SnakeOnlineClientStage.get().close();
            joinGameController.connectionClosed();
        });
    }

    @Override
    public void newUserEnteredRoom(NewUserEnteredRoom event) {
        Platform.runLater(() -> {
            joinGameController.handle(event);
            joinedUsersController.update(event.getUsersInTheRoom());
        });
    }

    @Override
    public void failedToEnterRoom(FailedToEnterRoom event) {
        Platform.runLater(() -> joinGameController.handle(event));
    }

    @Override
    public void initializeSeats(InitializeSeatsToRemoteClient command) {
        lobbySeatsController.update(command.getSeats(), command.getAdminIdOption());
    }

    @Override
    public void failedToFreeUpASeat(FailedToFreeUpSeat event) {
        Platform.runLater(() -> fleetingMessageController.print(event.getReason()));
    }

    @Override
    public void failedToSendChatMessage(FailedToSendChatMessage event) {
        Platform.runLater(() -> fleetingMessageController.print(event.getReason()));
    }

    @Override
    public void userLeftRoom(UserLeftRoom event) {
        Platform.runLater(() -> joinedUsersController.update(event.getUsersInTheRoom()));
    }

    private void update(PlaygroundState playgroundState) {
        onlineGameOptionsController.update(playgroundState.getGameOptions());
        scoreboardController.update(playgroundState.getGameState());
        messageController.clear();
    }
}