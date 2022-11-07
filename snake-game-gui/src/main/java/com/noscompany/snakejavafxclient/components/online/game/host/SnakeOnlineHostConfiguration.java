package com.noscompany.snakejavafxclient.components.online.game.host;

import com.noscompany.snake.game.online.client.SnakeOnlineClient;
import com.noscompany.snake.game.online.client.SnakeOnlineClientCreator;
import com.noscompany.snakejavafxclient.ApplicationProfile;
import com.noscompany.snakejavafxclient.components.commons.scpr.buttons.ScprButtonsController;
import com.noscompany.snakejavafxclient.components.mode.selection.GameModeSelectionStage;
import com.noscompany.snakejavafxclient.components.online.game.KeyPressedHandler;
import com.noscompany.snakejavafxclient.components.online.game.client.*;
import com.noscompany.snakejavafxclient.components.online.game.commons.ChatController;
import com.noscompany.snakejavafxclient.components.online.game.commons.GuiOnlineClientEventHandler;
import com.noscompany.snakejavafxclient.components.online.game.commons.LobbySeatsController;
import com.noscompany.snakejavafxclient.components.online.game.commons.OnlineGameOptionsController;
import com.noscompany.snakejavafxclient.utils.Controllers;
import javafx.stage.Stage;

public class SnakeOnlineHostConfiguration {

    public static void run(String roomName) {
        EnterTheRoomStage.get();
        String serverUrl = ApplicationProfile.getSnakeOnlineServerUrl(roomName);
        Stage snakeOnlineClientStage = SnakeOnlineClientStage.get();
        GuiOnlineClientEventHandler eventHandler = GuiOnlineClientEventHandler.instance();
        SnakeOnlineClient snakeOnlineClient = SnakeOnlineClientCreator.createClient(eventHandler);
        snakeOnlineClient.connect(serverUrl);
        SnakeMoving.set(snakeOnlineClient);
        setStage(snakeOnlineClientStage, snakeOnlineClient);
        setControllers(snakeOnlineClient);
    }

    private static void setStage(Stage snakeClientStage, SnakeOnlineClient snakeOnlineClient) {
        snakeClientStage.getScene().setOnKeyPressed(e -> new KeyPressedHandler());
        snakeClientStage.setOnCloseRequest(e -> {
            snakeOnlineClient.disconnect();
            SnakeOnlineClientStage.remove();
            GameModeSelectionStage.get().show();
        });
    }

    private static void setControllers(SnakeOnlineClient snakeOnlineClient) {
        Controllers
                .get(EnterTheRoomController.class)
                .onEnterTheRoomButtonPress(snakeOnlineClient::enterTheRoom);
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
    }
}