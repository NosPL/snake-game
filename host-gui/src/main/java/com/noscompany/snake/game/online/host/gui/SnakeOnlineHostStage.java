package com.noscompany.snake.game.online.host.gui;

import com.noscompany.snake.game.online.gui.commons.Stages;
import javafx.stage.Stage;

public class SnakeOnlineHostStage {
    private static final String SNAKE_ONLINE_HOST_VIEW = "/host/gui/snake-online-host-view.fxml";

    public static Stage get() {
        return Stages.getOrCreate(HostController.class, SNAKE_ONLINE_HOST_VIEW);
    }

    public static void remove() {
        Stages.remove(SNAKE_ONLINE_HOST_VIEW);
    }
}