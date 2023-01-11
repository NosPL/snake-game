package com.noscompany.snakejavafxclient.components.online.game.client;

import com.noscompany.snakejavafxclient.components.mode.selection.GameModeSelectionStage;
import com.noscompany.snakejavafxclient.utils.Stages;
import javafx.stage.Stage;

public class JoinGameStage {
    private static final String ONLINE_ENTER_ROOM_NAME_VIEW = "join-game-view.fxml";

    public static Stage get() {
        Stage stage = Stages.getOrCreate(JoinGameController.class, ONLINE_ENTER_ROOM_NAME_VIEW);
        stage.setOnCloseRequest(e -> {
            GameModeSelectionStage.get().show();
            Stages.remove(ONLINE_ENTER_ROOM_NAME_VIEW);
        });
        return stage;
    }
}