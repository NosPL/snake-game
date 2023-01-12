package com.noscompany.snakejavafxclient.components.online.game.host;

import com.noscompany.snakejavafxclient.components.mode.selection.GameModeSelectionStage;
import com.noscompany.snakejavafxclient.components.online.game.mode.selection.OnlineModeSelectionStage;
import com.noscompany.snakejavafxclient.utils.Stages;
import javafx.stage.Stage;

public class SetupHostStage {
    private static final String SETUP_HOST_VIEW = "setup-host-view.fxml";

    public static Stage get() {
        Stage stage = Stages.getOrCreate(SetupHostStage.class, SETUP_HOST_VIEW);
        stage.setOnCloseRequest(e -> {
            GameModeSelectionStage.get().show();
            Stages.remove(SETUP_HOST_VIEW);
        });
        return stage;
    }

    public static void remove() {
        Stages.remove(SETUP_HOST_VIEW);
    }
}