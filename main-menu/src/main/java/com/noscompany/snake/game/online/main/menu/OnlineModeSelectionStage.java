package com.noscompany.snake.game.online.main.menu;

import com.noscompany.snake.game.online.gui.commons.Stages;
import javafx.stage.Stage;

public class OnlineModeSelectionStage {
    private static final String ONLINE_MODE_SELECTION_VIEW = "/main/menu/online-select-mode-view.fxml";

    public static Stage get() {
        Stage stage = Stages.getOrCreate(OnlineModeSelectionController.class, ONLINE_MODE_SELECTION_VIEW);
        stage.setOnCloseRequest(e -> {
            GameModeSelectionStage.get().show();
            Stages.remove(ONLINE_MODE_SELECTION_VIEW);
        });
        return stage;
    }
}