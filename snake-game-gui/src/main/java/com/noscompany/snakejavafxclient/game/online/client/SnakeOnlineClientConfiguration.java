package com.noscompany.snakejavafxclient.game.online.client;

import com.noscompany.snake.game.online.client.SnakeOnlineClient;
import com.noscompany.snake.game.online.client.SnakeOnlineClientCreator;
import com.noscompany.snakejavafxclient.ApplicationProfile;
import com.noscompany.snakejavafxclient.commons.Controllers;
import com.noscompany.snakejavafxclient.commons.Stages;
import com.noscompany.snakejavafxclient.game.ButtonsController;
import javafx.stage.Stage;

public class SnakeOnlineClientConfiguration {

    public static void run(String roomName) {
        Stages.getEnterRoomStage();
        Stage snakeOnlineClientStage = Stages.getSnakeOnlineClientStage();
        String serverUrl = ApplicationProfile.getSnakeOnlineServerUrl(roomName);
        SnakeOnlineClient snakeOnlineClient = SnakeOnlineClientCreator
                .createClient(GuiOnlineClientEventHandler.instance())
                .connect(serverUrl);
        SnakeMoving.set(snakeOnlineClient);
        setStage(snakeOnlineClientStage, snakeOnlineClient);
        setControllers(snakeOnlineClient);
    }

    private static void setStage(Stage snakeClientStage, SnakeOnlineClient snakeOnlineClient) {
        snakeClientStage.getScene().setOnKeyPressed(e -> new KeyPressedHandler());
        snakeClientStage.setOnCloseRequest(e -> {
            snakeOnlineClient.disconnect();
            Stages.removeSnakeOnlineClientStage();
            Stages.getGameModeSelectionStage().show();
        });
    }

    private static void setControllers(SnakeOnlineClient snakeOnlineClient) {
        Controllers.get(EnterTheRoomController.class)
                .set(snakeOnlineClient::enterTheRoom);
        Controllers.get(OnlineGameOptionsController.class)
                .onGameOptionsChanged(snakeOnlineClient::changeGameOptions);
        Controllers.get(LobbySeatsController.class)
                .onTakeASeat(snakeOnlineClient::takeASeat)
                .onFreeUpASeat(snakeOnlineClient::freeUpASeat);
        Controllers.get(ChatController.class)
                .onSendChatMessage(snakeOnlineClient::sendChatMessage);
        Controllers.get(ButtonsController.class)
                .onStart(snakeOnlineClient::startGame)
                .onCancel(snakeOnlineClient::cancelGame)
                .onPause(snakeOnlineClient::pauseGame)
                .onResume(snakeOnlineClient::resumeGame);
    }
}