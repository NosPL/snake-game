package com.noscompany.snakejavafxclient.components.online.game.client;

import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptionsChanged;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.*;
import com.noscompany.snake.game.online.contract.messages.playground.GameReinitialized;
import com.noscompany.snake.game.online.contract.messages.playground.PlaygroundState;
import com.noscompany.snake.game.online.contract.messages.seats.*;
import com.noscompany.snake.game.online.contract.messages.playground.InitializePlaygroundStateToRemoteClient;
import com.noscompany.snake.game.online.contract.messages.user.registry.FailedToEnterRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.NewUserEnteredRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserLeftRoom;
import com.noscompany.snakejavafxclient.components.online.game.commons.*;
import com.noscompany.snakejavafxclient.utils.Controllers;
import com.noscompany.snake.game.online.client.SendClientMessageError;
import com.noscompany.snake.game.online.client.ClientEventHandler;
import com.noscompany.snake.game.online.client.StartingClientError;
import com.noscompany.snakejavafxclient.components.commons.scpr.buttons.ScprButtonsController;
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
    @NonNull private final ScprButtonsController scprButtonsController;

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
                Controllers.get(ScoreboardController.class),
                Controllers.get(ScprButtonsController.class));
    }

    @Override
    public void handle(PlayerTookASeat event) {
        Platform.runLater(() -> {
            gameGridController.handle(event);
            lobbySeatsController.update(event.getSeats(), Option.of(event.getAdminId()));
        });
    }

    @Override
    public void handle(FailedToTakeASeat event) {
        Platform.runLater(() -> fleetingMessageController.print(event.getReason()));
    }

    @Override
    public void handle(PlayerFreedUpASeat event) {
        Platform.runLater(() -> {
            gameGridController.handle(event);
            lobbySeatsController.update(event.getSeats(), event.getAdminId());
        });
    }

    @Override
    public void handle(GameOptionsChanged event) {
        Platform.runLater(() -> {
            gameGridController.handle(event);
            update(event.getPlaygroundState());
        });
    }

    @Override
    public void handle(FailedToChangeGameOptions event) {
        Platform.runLater(() -> fleetingMessageController.print(event.getReason()));
    }

    @Override
    public void handle(FailedToStartGame event) {
        Platform.runLater(() -> fleetingMessageController.print(event.getReason()));
    }

    @Override
    public void handle(FailedToPauseGame event) {
        Platform.runLater(() -> fleetingMessageController.print(event.getReason()));
    }

    @Override
    public void handle(FailedToResumeGame event) {
        Platform.runLater(() -> fleetingMessageController.print(event.getReason()));
    }

    @Override
    public void handle(FailedToCancelGame event) {
        Platform.runLater(() -> fleetingMessageController.print(event.getReason()));
    }

    @Override
    public void handle(UserSentChatMessage event) {
        Platform.runLater(() -> chatController.print(event));
    }

    @Override
    public void handle(GameStartCountdown event) {
        Platform.runLater(() -> {
            gameGridController.handle(event);
            onlineGameOptionsController.disable();
            messageController.printSecondsLeftToStart(event.getSecondsLeft());
            scoreboardController.clear();
        });
    }

    @Override
    public void handle(GameStarted event) {
        Platform.runLater(() -> {
            gameGridController.handle(event);
            onlineGameOptionsController.disable();
            scoreboardController.print(event.getScore());
            messageController.clear();
        });
    }

    @Override
    public void handle(SnakesMoved event) {
        Platform.runLater(() -> {
            gameGridController.handle(event);
            scoreboardController.print(event.getScore());
        });
    }

    @Override
    public void handle(GameFinished event) {
        Platform.runLater(() -> {
            gameGridController.handle(event);
            onlineGameOptionsController.enable();
            messageController.printGameFinished();
            scoreboardController.print(event.getScore());
        });
    }

    @Override
    public void handle(GameCancelled event) {
        Platform.runLater(() -> {
            onlineGameOptionsController.enable();
            messageController.printGameCanceled();
        });
    }

    @Override
    public void handle(GamePaused event) {
        Platform.runLater(messageController::printGamePaused);
    }

    @Override
    public void handle(GameResumed event) {
        Platform.runLater(messageController::printGameResumed);
    }

    @Override
    public void connectionEstablished() {
        Platform.runLater(joinGameController::connectionEstablished);
    }

    @Override
    public void handle(InitializePlaygroundStateToRemoteClient command) {
        Platform.runLater(() -> {
            gameGridController.handle(command);
            update(command.getPlaygroundState());
            joinGameController.enterRoom();
        });
    }

    @Override
    public void handle(GameReinitialized event) {
        Platform.runLater(() -> {
            scoreboardController.update(event.getGameState());
            gameGridController.handle(event);
        });
    }

    @Override
    public void handle(SendClientMessageError sendClientMessageError) {
        Platform.runLater(() -> joinGameController.handle(sendClientMessageError));
    }

    @Override
    public void handle(StartingClientError startingClientError) {
        Platform.runLater(() -> joinGameController.handle(startingClientError));
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
            joinGameController.handle(event);
            joinedUsersController.update(event.getUsersInTheRoom());
        });
    }

    @Override
    public void handle(FailedToEnterRoom event) {
        Platform.runLater(() -> joinGameController.handle(event));
    }

    @Override
    public void handle(InitializeSeatsToRemoteClient command) {
        lobbySeatsController.update(command.getSeats(), command.getAdminIdOption());
    }

    @Override
    public void handle(FailedToFreeUpSeat event) {
        Platform.runLater(() -> fleetingMessageController.print(event.getReason()));
    }

    @Override
    public void handle(FailedToSendChatMessage event) {
        Platform.runLater(() -> fleetingMessageController.print(event.getReason()));
    }

    @Override
    public void handle(UserLeftRoom event) {
        Platform.runLater(() -> joinedUsersController.update(event.getUsersInTheRoom()));
    }

    private void update(PlaygroundState playgroundState) {
        onlineGameOptionsController.update(playgroundState.getGameOptions());
        scoreboardController.update(playgroundState.getGameState());
        messageController.clear();
    }
}