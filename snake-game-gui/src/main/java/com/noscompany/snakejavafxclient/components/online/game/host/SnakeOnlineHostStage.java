package com.noscompany.snakejavafxclient.components.online.game.host;

import com.noscompany.snakejavafxclient.components.online.game.mode.selection.OnlineModeSelectionStage;
import com.noscompany.snakejavafxclient.utils.Stages;
import javafx.stage.Stage;

public class SnakeOnlineHostStage {
    private static final String SNAKE_ONLINE_HOST_VIEW = "snake-online-host-view.fxml";

    public static Stage get() {
        Stage stage = Stages.getOrCreate(HostController.class, SNAKE_ONLINE_HOST_VIEW);
        stage.setOnCloseRequest(e -> {
            OnlineModeSelectionStage.get().show();
            Stages.remove(SNAKE_ONLINE_HOST_VIEW);
        });
        return stage;
    }

    public static void remove() {
        Stages.remove(SNAKE_ONLINE_HOST_VIEW);
    }
}