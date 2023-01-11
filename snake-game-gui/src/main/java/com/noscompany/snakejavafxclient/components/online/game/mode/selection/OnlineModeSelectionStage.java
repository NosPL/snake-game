package com.noscompany.snakejavafxclient.components.online.game.mode.selection;

import com.noscompany.snakejavafxclient.components.mode.selection.GameModeSelectionStage;
import com.noscompany.snakejavafxclient.utils.Stages;
import javafx.stage.Stage;

public class OnlineModeSelectionStage {
    private static final String ONLINE_MODE_SELECTION_VIEW = "online-select-mode-view.fxml";

    public static Stage get() {
        Stage stage = Stages.getOrCreate(OnlineModeSelectionController.class, ONLINE_MODE_SELECTION_VIEW);
        stage.setOnCloseRequest(e -> {
            GameModeSelectionStage.get().show();
            Stages.remove(ONLINE_MODE_SELECTION_VIEW);
        });
        return stage;
    }
}