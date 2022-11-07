package com.noscompany.snakejavafxclient.components.mode.selection;

import com.noscompany.snakejavafxclient.utils.AbstractController;
import com.noscompany.snakejavafxclient.components.local.game.LocalGameConfiguration;
import javafx.fxml.FXML;
import lombok.SneakyThrows;

public class GameModeSelectionController extends AbstractController {

    @FXML
    @SneakyThrows
    public void selectLocalMode() {
        GameModeSelectionStage.get().close();
        LocalGameConfiguration.run();
    }

    @FXML
    @SneakyThrows
    public void selectOnlineMode() {
        GameModeSelectionStage.get().close();
        OnlineModeSelectionStage.get().show();
    }
}