package com.noscompany.snakejavafxclient.components.mode.selection;

import com.noscompany.snakejavafxclient.utils.Stages;
import com.noscompany.snakejavafxclient.components.online.game.RoomJoiningController;
import javafx.stage.Stage;

public class OnlineModeSelectionStage {
    private static final String ONLINE_MODE_SELECTION_VIEW = "online-mode-selection-view.fxml";

    public static Stage get() {
        Stage stage = Stages.getOrCreate(RoomJoiningController.class, ONLINE_MODE_SELECTION_VIEW);
        stage.setOnCloseRequest(e -> {
            GameModeSelectionStage.get().show();
            Stages.remove(ONLINE_MODE_SELECTION_VIEW);
        });
        return stage;
    }
}