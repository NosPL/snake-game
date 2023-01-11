package com.noscompany.snakejavafxclient.components.online.game.host;

import com.noscompany.snake.game.online.host.SnakeOnlineHost;
import com.noscompany.snake.game.online.host.server.dto.ServerParams;
import com.noscompany.snake.game.online.host.room.mediator.PlayerName;
import com.noscompany.snakejavafxclient.components.commons.scpr.buttons.ScprButtonsController;
import com.noscompany.snakejavafxclient.components.mode.selection.GameModeSelectionStage;
import com.noscompany.snakejavafxclient.components.online.game.commons.ChatController;
import com.noscompany.snakejavafxclient.components.online.game.commons.LobbySeatsController;
import com.noscompany.snakejavafxclient.components.online.game.commons.OnlineGameOptionsController;
import com.noscompany.snakejavafxclient.utils.Controllers;
import javafx.stage.Stage;

public class SnakeOnlineHostGuiConfiguration {

    public static void run(ServerParams serverParams, PlayerName playerName) {
        Stage snakeOnlineHostStage = SnakeOnlineHostStage.get();
        SnakeOnlineHost snakeOnlineHost = OnlineHostCreator.create();
        setStage(snakeOnlineHostStage, snakeOnlineHost);
        setControllers(snakeOnlineHost, serverParams, playerName);
        snakeOnlineHostStage.show();
    }

    private static void setStage(Stage snakeOnlineHostStage, SnakeOnlineHost snakeOnlineHost) {
        KeyPressedHandler keyEventEventHandler = new KeyPressedHandler(snakeOnlineHost);
        Controllers.get(HostController.class).set(keyEventEventHandler);
        snakeOnlineHostStage.getScene().setOnKeyPressed(keyEventEventHandler);
        snakeOnlineHostStage.setOnCloseRequest(e -> {
            snakeOnlineHost.shutDownServer();
            SnakeOnlineHostStage.remove();
            GameModeSelectionStage.get().show();
        });
    }

    private static void setControllers(SnakeOnlineHost snakeOnlineHost, ServerParams serverParams, PlayerName playerName) {
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
        ServerController serverController = Controllers.get(ServerController.class);
        serverController
                .onStartServer(() -> {
                    snakeOnlineHost.startServer(serverParams, playerName);
                })
                .onShutdownServer(snakeOnlineHost::shutDownServer);
    }
}