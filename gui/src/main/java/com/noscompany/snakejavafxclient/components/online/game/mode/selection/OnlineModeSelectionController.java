package com.noscompany.snakejavafxclient.components.online.game.mode.selection;

import com.noscompany.snakejavafxclient.components.online.game.client.JoinGameStage;
import com.noscompany.snakejavafxclient.components.online.game.host.SetupHostStage;
import com.noscompany.snake.game.online.gui.commons.AbstractController;
import javafx.fxml.FXML;
import lombok.SneakyThrows;

public class OnlineModeSelectionController extends AbstractController {

    @FXML
    @SneakyThrows
    public void runHost() {
        OnlineModeSelectionStage.get().close();
        SetupHostStage.get().show();
    }

    @FXML
    @SneakyThrows
    public void runClient() {
        OnlineModeSelectionStage.get().close();
        JoinGameStage.get().show();
    }
}