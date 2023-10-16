package com.noscompany.snake.game.online.remote.client.gui;

import com.noscompany.snake.game.online.gui.commons.Stages;
import javafx.stage.Stage;

public class SnakeOnlineClientStage {
    private static final String SNAKE_ONLINE_CLIENT_VIEW = "/remote/client/gui/snake-online-client-view.fxml";

    public static Stage get() {
        Stage stage = Stages.getOrCreate(OnlineClientController.class, SNAKE_ONLINE_CLIENT_VIEW);
        return stage;
    }

    public static void remove() {
        Stages.remove(SNAKE_ONLINE_CLIENT_VIEW);
    }
}