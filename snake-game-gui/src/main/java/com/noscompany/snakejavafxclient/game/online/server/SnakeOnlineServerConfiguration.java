package com.noscompany.snakejavafxclient.game.online.server;

import com.noscompany.snake.game.server.local.api.SnakeServerCreator;
import com.noscompany.snakejavafxclient.commons.Controllers;
import com.noscompany.snakejavafxclient.commons.Stages;
import com.noscompany.snakejavafxclient.game.ButtonsController;
import com.noscompany.snakejavafxclient.game.online.ChatController;
import com.noscompany.snakejavafxclient.game.online.LobbySeatsController;
import com.noscompany.snakejavafxclient.game.online.OnlineGameOptionsController;
import javafx.stage.Stage;

public class SnakeOnlineServerConfiguration {

    public static void run(String userId) {
        Stage snakeServerStage = Stages.getSnakeServerStage();
        var eventHandler = GuiServerEventHandler.instance();
        var snakeServer = SnakeServerCreator.instance(eventHandler);
        var snakeServerWrapper = new SnakeServerUserIdWrapper(userId, snakeServer);
        setStage(snakeServerStage, snakeServerWrapper);
        setControllers(snakeServerWrapper);
        snakeServerStage.show();
    }

    private static void setStage(Stage snakeServerStage,
                                 SnakeServerUserIdWrapper snakeServer) {
        snakeServerStage.getScene().setOnKeyPressed(e -> new KeyPressedHandler(snakeServer));
        snakeServerStage.setOnCloseRequest(e -> {
            snakeServer.closeServer();
            Stages.removeSnakeOnlineServerStage();
            Stages.getGameModeSelectionStage().show();
        });
    }

    private static void setControllers(SnakeServerUserIdWrapper snakeServer) {
        Controllers.get(StartServerController.class)
                .onStopServer(snakeServer::closeServer)
                .onStartServer((ip, port) -> {
                    snakeServer.startServer(ip, port);
                    snakeServer.addUser();
                });
        Controllers.get(OnlineGameOptionsController.class)
                .onGameOptionsChanged(snakeServer::changeGameOptions);
        Controllers.get(LobbySeatsController.class)
                .onTakeASeat(snakeServer::takeASeat)
                .onFreeUpASeat(snakeServer::freeUpASeat);
        Controllers.get(ChatController.class)
                .onSendChatMessage(snakeServer::sendChatMessage);
        Controllers.get(ButtonsController.class)
                .onStart(snakeServer::startGame)
                .onCancel(snakeServer::cancelGame)
                .onPause(snakeServer::pauseGame)
                .onResume(snakeServer::resumeGame);
    }
}