package com.noscompany.snakejavafxclient.components.online.game.host;

import com.noscompany.snakejavafxclient.components.mode.selection.GameModeSelectionStage;
import com.noscompany.snakejavafxclient.components.online.game.client.OnlineClientController;
import com.noscompany.snakejavafxclient.utils.Stages;
import javafx.stage.Stage;

public class SnakeOnlineHostStage {
    private static final String SNAKE_ONLINE_HOST_VIEW = "snake-online-host-view.fxml";

    public static Stage get() {
        Stage stage = Stages.getOrCreate(OnlineClientController.class, SNAKE_ONLINE_HOST_VIEW);
        stage.setOnCloseRequest(e -> {
            GameModeSelectionStage.get().show();
            Stages.remove(SNAKE_ONLINE_HOST_VIEW);
        });
        return stage;
    }

    public static void remove() {
        Stages.remove(SNAKE_ONLINE_HOST_VIEW);
    }
}