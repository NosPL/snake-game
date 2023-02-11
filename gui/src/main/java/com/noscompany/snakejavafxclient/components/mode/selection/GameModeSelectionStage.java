package com.noscompany.snakejavafxclient.components.mode.selection;

import com.noscompany.snakejavafxclient.utils.Stages;
import javafx.application.Platform;
import javafx.stage.Stage;

public class GameModeSelectionStage {
    private static final String GAME_MODE_SELECTION_VIEW = "game-mode-selection-view.fxml";

    public static Stage get() {
        Stage stage = Stages.getOrCreate(GameModeSelectionController.class, GAME_MODE_SELECTION_VIEW);
        stage.setOnCloseRequest(e -> Platform.exit());
        return stage;
    }
}