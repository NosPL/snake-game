package com.noscompany.snakejavafxclient.game.online.server;

import com.noscompany.snake.game.server.local.api.ServerError;
import com.noscompany.snake.game.server.local.api.SnakeServerEventHandler;
import com.noscompany.snake.game.server.local.api.StartingServerError;
import com.noscompany.snakejavafxclient.commons.Controllers;
import com.noscompany.snakejavafxclient.game.ButtonsController;
import com.noscompany.snakejavafxclient.game.MessageController;
import com.noscompany.snakejavafxclient.game.ScoreboardController;
import com.noscompany.snakejavafxclient.game.grid.controller.GameGridController;
import com.noscompany.snakejavafxclient.game.online.*;
import javafx.application.Platform;

public class GuiServerEventHandler extends GuiLobbyEventHandler implements SnakeServerEventHandler {
    protected final StartServerController startServerController;

    public GuiServerEventHandler(OnlineGameOptionsController onlineGameOptionsController, LobbySeatsController lobbySeatsController, GameGridController gameGridController, ChatController chatController, JoinedUsersController joinedUsersController, MessageController messageController, ScoreboardController scoreboardController, ButtonsController buttonsController, StartServerController startServerController) {
        super(onlineGameOptionsController, lobbySeatsController, gameGridController, chatController, joinedUsersController, messageController, scoreboardController, buttonsController);
        this.startServerController = startServerController;
    }

    public static GuiServerEventHandler instance() {
        return new GuiServerEventHandler(
                Controllers.get(OnlineGameOptionsController.class),
                Controllers.get(LobbySeatsController.class),
                Controllers.get(GameGridController.class),
                Controllers.get(ChatController.class),
                Controllers.get(JoinedUsersController.class),
                Controllers.get(MessageController.class),
                Controllers.get(ScoreboardController.class),
                Controllers.get(ButtonsController.class),
                Controllers.get(StartServerController.class));
    }

    @Override
    public void serverStarted() {
        Platform.runLater(() -> startServerController.printServerStarted());
    }

    @Override
    public void handle(StartingServerError error) {
        Platform.runLater(() -> startServerController.print(error));
    }

    @Override
    public void handle(ServerError error) {
        Platform.runLater(() -> startServerController.print(error));
    }

    @Override
    public void serverClosed() {
        Platform.runLater(startServerController::printServerClosed);
    }
}