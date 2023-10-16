package com.noscompany.snake.game.online.host.gui;

import com.noscompany.snake.game.online.gui.commons.Stages;
import javafx.stage.Stage;

public class SetupHostStage {
    private static final String SETUP_HOST_VIEW = "/host/gui/setup-host-view.fxml";

    public static Stage get() {
        return Stages.getOrCreate(SetupHostStage.class, SETUP_HOST_VIEW);
    }

    public static void remove() {
        Stages.remove(SETUP_HOST_VIEW);
    }
}