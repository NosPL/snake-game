package com.noscompany.snakejavafxclient.components.online.game.host;

import com.noscompany.snake.game.online.host.HostEventHandler;
import com.noscompany.snake.game.online.host.SnakeOnlineHost;
import com.noscompany.snake.game.online.host.dependency.configurator.SnakeOnlineHostDependencyConfigurator;
import com.noscompany.snake.game.online.contract.messages.room.UsersCountLimit;
import com.noscompany.snakejavafxclient.components.commons.scpr.buttons.ScprButtonsController;
import com.noscompany.snakejavafxclient.components.mode.selection.GameModeSelectionStage;
import com.noscompany.snakejavafxclient.components.online.game.commons.ChatController;
import com.noscompany.snakejavafxclient.components.online.game.commons.KeyPressedHandler;
import com.noscompany.snakejavafxclient.components.online.game.commons.LobbySeatsController;
import com.noscompany.snakejavafxclient.components.online.game.commons.OnlineGameOptionsController;
import com.noscompany.snakejavafxclient.utils.Controllers;
import javafx.stage.Stage;

public class SnakeOnlineHostGuiConfiguration {

    public static SnakeOnlineHost createConfiguredHost(UsersCountLimit usersCountLimit) {
        Stage snakeOnlineHostStage = SnakeOnlineHostStage.get();
        HostEventHandler hostEventHandler = GuiOnlineHostEventHandler.instance();
        SnakeOnlineHost snakeOnlineHost = new SnakeOnlineHostDependencyConfigurator().snakeOnlineHost(hostEventHandler, usersCountLimit);
        setStage(snakeOnlineHostStage, snakeOnlineHost);
        setControllers(snakeOnlineHost);
        return snakeOnlineHost;
    }

    private static void setStage(Stage snakeOnlineHostStage, SnakeOnlineHost snakeOnlineHost) {
        var keyEventEventHandler = new KeyPressedHandler(snakeOnlineHost::changeSnakeDirection);
        Controllers.get(HostController.class).set(keyEventEventHandler);
        snakeOnlineHostStage.getScene().setOnKeyPressed(keyEventEventHandler);
        snakeOnlineHostStage.setOnCloseRequest(e -> {
            snakeOnlineHost.shutDownServer();
            SnakeOnlineHostStage.remove();
            GameModeSelectionStage.get().show();
            SnakeOnlineHostStage.remove();
        });
    }

    private static void setControllers(SnakeOnlineHost snakeOnlineHost) {
        Controllers
                .get(OnlineGameOptionsController.class)
                .onGameOptionsChanged(snakeOnlineHost::changeGameOptions);
        Controllers
                .get(LobbySeatsController.class)
                .onTakeASeatButtonPress(snakeOnlineHost::takeASeat)
                .onFreeUpASeatButtonPress(snakeOnlineHost::freeUpASeat);
        Controllers
                .get(ChatController.class)
                .onSendChatMessageButtonPress(snakeOnlineHost::sendChatMessage);
        Controllers
                .get(ScprButtonsController.class)
                .onStartButtonPress(snakeOnlineHost::startGame)
                .onCancelButtonPress(snakeOnlineHost::cancelGame)
                .onPauseButtonPress(snakeOnlineHost::pauseGame)
                .onResumeButtonPress(snakeOnlineHost::resumeGame);
    }
}