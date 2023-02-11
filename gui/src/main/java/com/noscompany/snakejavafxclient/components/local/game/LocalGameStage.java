package com.noscompany.snakejavafxclient.components.local.game;

import com.noscompany.snakejavafxclient.utils.Stages;
import com.noscompany.snakejavafxclient.components.mode.selection.GameModeSelectionStage;
import javafx.stage.Stage;

public class LocalGameStage {
    private static final String LOCAL_GAME_VIEW = "local-game-view.fxml";

    public static Stage get() {
        final Stage stage = Stages.getOrCreate(LocalSnakeGame.class, LOCAL_GAME_VIEW);
        stage.setOnCloseRequest(e -> {
            Stages.remove(LOCAL_GAME_VIEW);
            LocalGameStage.remove();
            GameModeSelectionStage.get().show();
        });
        return stage;
    }

    public static void remove() {
        Stages.remove(LOCAL_GAME_VIEW);
    }
}