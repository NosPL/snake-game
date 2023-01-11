package com.noscompany.snakejavafxclient.components.online.game.client;

import com.noscompany.snakejavafxclient.utils.Controllers;
import com.noscompany.snakejavafxclient.utils.Stages;
import com.noscompany.snakejavafxclient.components.mode.selection.GameModeSelectionStage;
import javafx.stage.Stage;

public class SnakeOnlineClientStage {
    private static final String SNAKE_ONLINE_CLIENT_VIEW = "snake-online-client-view.fxml";

    public static Stage get() {
        Stage stage = Stages.getOrCreate(OnlineClientController.class, SNAKE_ONLINE_CLIENT_VIEW);
        stage.setOnCloseRequest(e -> {
            Controllers.get(OnlineClientController.class).disconnectClient();
            GameModeSelectionStage.get().show();
            Stages.remove(SNAKE_ONLINE_CLIENT_VIEW);
        });
        return stage;
    }

    public static void remove() {
        Stages.remove(SNAKE_ONLINE_CLIENT_VIEW);
    }
}