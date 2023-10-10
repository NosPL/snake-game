package com.noscompany.snakejavafxclient.components.online.game.client;

import com.noscompany.snake.game.online.client.SnakeOnlineClient;
import com.noscompany.snake.game.online.client.SnakeOnlineClientConfiguration;
import com.noscompany.snake.game.test.client.SnakeOnlineTestClientConfiguration;
import com.noscompany.snakejavafxclient.ApplicationProfile;
import com.noscompany.snakejavafxclient.components.commons.scpr.buttons.ScprButtonsController;
import com.noscompany.snakejavafxclient.components.online.game.commons.*;
import com.noscompany.snakejavafxclient.utils.Controllers;

class SnakeOnlineGuiClientConfiguration {

    static SnakeOnlineClient createOnlineClient() {
        GuiOnlineClientEventHandler eventHandler = GuiOnlineClientEventHandler.instance();
        SnakeOnlineClient snakeOnlineClient = getSnakeOnlineClient(eventHandler);
        SnakeOnlineClientStage.get().getScene().setOnKeyPressed(e -> new KeyPressedHandler(snakeOnlineClient::changeSnakeDirection));
        setControllers(snakeOnlineClient);
        return snakeOnlineClient;
    }

    private static SnakeOnlineClient getSnakeOnlineClient(GuiOnlineClientEventHandler eventHandler) {
        if (ApplicationProfile.profile == ApplicationProfile.Profile.PROD)
            return new SnakeOnlineClientConfiguration().create(eventHandler);
        else
            return new SnakeOnlineTestClientConfiguration().snakeOnlineTestClient(eventHandler);
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
        SnakeOnlineClientStage
                .get()
                .setOnCloseRequest(w -> Controllers.get(FleetingMessageController.class).shutdown());
    }
}