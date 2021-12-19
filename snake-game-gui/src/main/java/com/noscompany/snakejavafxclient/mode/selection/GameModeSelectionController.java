package com.noscompany.snakejavafxclient.mode.selection;

import com.noscompany.snake.game.online.client.SnakeOnlineClient;
import com.noscompany.snake.game.online.client.SnakeOnlineClientCreator;
import com.noscompany.snakejavafxclient.commons.Stages;
import com.noscompany.snakejavafxclient.commons.AbstractController;
import com.noscompany.snakejavafxclient.game.local.LocalGameConfiguration;
import javafx.fxml.FXML;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.ResourceBundle;

public class GameModeSelectionController extends AbstractController {

    @FXML
    @SneakyThrows
    public void selectLocalMode() {
        Stages.getGameModeSelectionStage().close();
        LocalGameConfiguration.run();
    }

    @FXML
    @SneakyThrows
    public void selectOnlineMode() {
        Stages.getGameModeSelectionStage().close();
        Stages.getOnlineModeSelectionStage().show();
    }
}