package com.noscompany.snakejavafxclient.game.online.client;

import com.noscompany.snake.game.client.ClientError;
import com.noscompany.snake.game.client.ClientEventHandler;
import com.noscompany.snake.game.client.StartingClientError;
import com.noscompany.snakejavafxclient.commons.Controllers;
import com.noscompany.snakejavafxclient.game.ButtonsController;
import com.noscompany.snakejavafxclient.game.MessageController;
import com.noscompany.snakejavafxclient.game.ScoreboardController;
import com.noscompany.snakejavafxclient.game.grid.controller.GameGridController;
import com.noscompany.snakejavafxclient.game.online.*;
import javafx.application.Platform;

public class GuiClientEventHandler extends GuiLobbyEventHandler implements ClientEventHandler {
    protected final JoinServerController joinServerController;

    public GuiClientEventHandler(OnlineGameOptionsController onlineGameOptionsController, LobbySeatsController lobbySeatsController, GameGridController gameGridController, ChatController chatController, JoinedUsersController joinedUsersController, MessageController messageController, ScoreboardController scoreboardController, ButtonsController buttonsController, JoinServerController joinServerController) {
        super(onlineGameOptionsController, lobbySeatsController, gameGridController, chatController, joinedUsersController, messageController, scoreboardController, buttonsController);
        this.joinServerController = joinServerController;
    }

    public static GuiClientEventHandler instance() {
        return new GuiClientEventHandler(
                Controllers.get(OnlineGameOptionsController.class),
                Controllers.get(LobbySeatsController.class),
                Controllers.get(GameGridController.class),
                Controllers.get(ChatController.class),
                Controllers.get(JoinedUsersController.class),
                Controllers.get(MessageController.class),
                Controllers.get(ScoreboardController.class),
                Controllers.get(ButtonsController.class),
                Controllers.get(JoinServerController.class));
    }

    @Override
    public void connectionEstablished() {
        Platform.runLater(() -> joinServerController.connectionEstablished());
    }

    @Override
    public void handle(ClientError clientError) {
        Platform.runLater(() -> joinServerController.print(clientError));
    }

    @Override
    public void handle(StartingClientError startingClientError) {
        Platform.runLater(() -> joinServerController.print(startingClientError));
    }

    @Override
    public void connectionClosed() {
        Platform.runLater(joinServerController::printConnectionClosed);
    }
}