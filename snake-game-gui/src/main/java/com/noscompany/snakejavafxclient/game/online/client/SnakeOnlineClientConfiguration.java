package com.noscompany.snakejavafxclient.game.online.client;

import com.noscompany.snake.game.client.SnakeOnlineClient;
import com.noscompany.snake.game.client.SnakeOnlineClientCreator;
import com.noscompany.snakejavafxclient.commons.Controllers;
import com.noscompany.snakejavafxclient.commons.Stages;
import com.noscompany.snakejavafxclient.game.ButtonsController;
import com.noscompany.snakejavafxclient.game.online.ChatController;
import com.noscompany.snakejavafxclient.game.online.LobbySeatsController;
import com.noscompany.snakejavafxclient.game.online.OnlineGameOptionsController;
import javafx.stage.Stage;

public class SnakeOnlineClientConfiguration {

    public static void run(String userId) {
        Stage snakeServerStage = Stages.getSnakeOnlineClientStage();
        var eventHandler = GuiClientEventHandler.instance();
        var snakeOnlineClient = SnakeOnlineClientCreator.createClient(eventHandler);
        setStage(snakeServerStage, snakeOnlineClient);
        setControllers(snakeOnlineClient);
        snakeServerStage.show();
    }

    private static void setStage(Stage snakeClientStage,
                                 SnakeOnlineClient snakeOnlineClient) {
        snakeClientStage.getScene().setOnKeyPressed(e -> new KeyPressedHandler(snakeOnlineClient));
        snakeClientStage.setOnCloseRequest(e -> {
            snakeOnlineClient.disconnect();
            Stages.removeSnakeOnlineServerStage();
            Stages.getGameModeSelectionStage().show();
        });
    }

    private static void setControllers(SnakeOnlineClient snakeOnlineClient) {
        Controllers.get(JoinServerController.class)
                .onJoinServer(snakeOnlineClient::connect)
                .onLeaveServer(snakeOnlineClient::disconnect);
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