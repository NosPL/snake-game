package com.noscompany.snakejavafxclient.components.online.game.client;

import com.noscompany.snakejavafxclient.components.mode.selection.GameModeSelectionStage;
import com.noscompany.snake.game.online.gui.commons.Controllers;
import com.noscompany.snake.game.online.gui.commons.Stages;
import javafx.stage.Stage;

public class JoinGameStage {
    private static final String JOIN_GAME_VIEW = "join-game-view.fxml";

    public static Stage get() {
        Stage stage = Stages.getOrCreate(JoinGameController.class, JOIN_GAME_VIEW);
        stage.setOnCloseRequest(e -> {
            Controllers.get(JoinGameController.class).disconnect();
            GameModeSelectionStage.get().show();
            Stages.remove(JOIN_GAME_VIEW);
        });
        return stage;
    }

    public static void remove() {
        Stages.remove(JOIN_GAME_VIEW);
    }
}