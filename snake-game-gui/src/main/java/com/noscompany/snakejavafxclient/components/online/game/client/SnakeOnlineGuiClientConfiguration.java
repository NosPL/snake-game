package com.noscompany.snakejavafxclient.components.online.game.client;

import com.noscompany.snake.game.online.client.SnakeOnlineClient;
import com.noscompany.snake.game.online.client.SnakeOnlineClientConfiguration;
import com.noscompany.snake.game.test.client.SnakeOnlineTestClientConfiguration;
import com.noscompany.snakejavafxclient.ApplicationProfile;
import com.noscompany.snakejavafxclient.components.commons.scpr.buttons.ScprButtonsController;
import com.noscompany.snakejavafxclient.components.mode.selection.GameModeSelectionStage;
import com.noscompany.snakejavafxclient.components.online.game.commons.ChatController;
import com.noscompany.snakejavafxclient.components.online.game.commons.LobbySeatsController;
import com.noscompany.snakejavafxclient.components.online.game.commons.OnlineGameOptionsController;
import com.noscompany.snakejavafxclient.utils.Controllers;
import javafx.stage.Stage;

public class SnakeOnlineGuiClientConfiguration {

    public static SnakeOnlineClient createOnlineClient() {
        Stage snakeOnlineClientStage = SnakeOnlineClientStage.get();
        GuiOnlineClientEventHandler eventHandler = GuiOnlineClientEventHandler.instance();
        SnakeOnlineClient snakeOnlineClient = getSnakeOnlineClient(eventHandler);
        setStage(snakeOnlineClientStage, snakeOnlineClient);
        setControllers(snakeOnlineClient);
        return snakeOnlineClient;
    }

    private static SnakeOnlineClient getSnakeOnlineClient(GuiOnlineClientEventHandler eventHandler) {
        if (ApplicationProfile.profile == ApplicationProfile.Profile.PROD)
            return new SnakeOnlineClientConfiguration().create(eventHandler);
        else
            return new SnakeOnlineTestClientConfiguration().snakeOnlineTestClient(eventHandler);
    }

    private static void setStage(Stage snakeClientStage, SnakeOnlineClient snakeOnlineClient) {
        snakeClientStage.getScene().setOnKeyPressed(e -> new KeyPressedHandler(snakeOnlineClient));
        snakeClientStage.setOnCloseRequest(e -> {
            snakeOnlineClient.disconnect();
            SnakeOnlineClientStage.remove();
            GameModeSelectionStage.get().show();
        });
    }

    private static void setControllers(SnakeOnlineClient snakeOnlineClient) {
        Controllers
                .get(OnlineGameOptionsController.class)
                .onGameOptionsChanged(snakeOnlineClient::changeGameOptions);
        Controllers
                .get(LobbySeatsController.class)
                .onTakeASeatButtonPress(snakeOnlineClient::takeASeat)
                .onFreeUpASeatButtonPress(snakeOnlineClient::freeUpASeat);
        Controllers
                .get(ChatController.class)
                .onSendChatMessageButtonPress(snakeOnlineClient::sendChatMessage);
        Controllers
                .get(ScprButtonsController.class)
                .onStartButtonPress(snakeOnlineClient::startGame)
                .onCancelButtonPress(snakeOnlineClient::cancelGame)
                .onPauseButtonPress(snakeOnlineClient::pauseGame)
                .onResumeButtonPress(snakeOnlineClient::resumeGame);
        Controllers
                .get(OnlineClientController.class)
                .setSnakeOnlineClient(snakeOnlineClient);
    }
}