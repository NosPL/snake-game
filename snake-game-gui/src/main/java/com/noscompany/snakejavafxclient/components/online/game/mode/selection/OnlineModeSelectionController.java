package com.noscompany.snakejavafxclient.components.online.game.mode.selection;

import com.noscompany.snake.game.online.host.server.dto.ServerParams;
import com.noscompany.snake.game.online.host.room.mediator.PlayerName;
import com.noscompany.snakejavafxclient.components.online.game.client.JoinGameStage;
import com.noscompany.snakejavafxclient.components.online.game.host.SnakeOnlineHostGuiConfiguration;
import com.noscompany.snakejavafxclient.utils.AbstractController;
import javafx.fxml.FXML;
import lombok.SneakyThrows;

public class OnlineModeSelectionController extends AbstractController {

    @FXML
    @SneakyThrows
    public void runHost() {
        OnlineModeSelectionStage.get().close();
        SnakeOnlineHostGuiConfiguration.run(new ServerParams("192.168.43.127", 8080), new PlayerName("game host"));
    }

    @FXML
    @SneakyThrows
    public void runClient() {
        OnlineModeSelectionStage.get().close();
        JoinGameStage.get().show();
    }
}