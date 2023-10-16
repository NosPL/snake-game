package com.noscompany.snake.game.online.remote.client.gui;

import com.noscompany.snake.game.online.gui.commons.Stages;
import javafx.stage.Stage;

public class JoinGameStage {
    private static final String JOIN_GAME_VIEW = "/remote/client/gui/join-game-view.fxml";

    public static Stage get() {
        Stage stage = Stages.getOrCreate(JoinGameController.class, JOIN_GAME_VIEW);
        return stage;
    }

    public static void remove() {
        Stages.remove(JOIN_GAME_VIEW);
    }
}