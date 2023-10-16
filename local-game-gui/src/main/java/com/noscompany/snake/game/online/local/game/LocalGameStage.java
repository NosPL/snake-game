package com.noscompany.snake.game.online.local.game;

import com.noscompany.snake.game.online.gui.commons.Stages;
import javafx.stage.Stage;

public class LocalGameStage {
    private static final String LOCAL_GAME_VIEW = "/local/game/local-game-view.fxml";

    public static Stage get() {
        final Stage stage = Stages.getOrCreate(LocalSnakeGame.class, LOCAL_GAME_VIEW);
        stage.setOnCloseRequest(e -> {
            Stages.remove(LOCAL_GAME_VIEW);
            LocalGameStage.remove();
        });
        return stage;
    }

    public static void remove() {
        Stages.remove(LOCAL_GAME_VIEW);
    }
}