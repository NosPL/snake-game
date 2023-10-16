package com.noscompany.snake.game.online.local.game.edit.snake.name;

import com.noscompany.snake.game.online.gui.commons.Stages;
import com.noscompany.snake.game.online.local.game.LocalGameStage;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EditSnakeNameStage {
    private static final String LOCAL_GAME_EDIT_SNAKE_NAME_VIEW = "/local/game/edit-snake-name-view.fxml";

    public static Stage get() {
        Stage stage = Stages.getOrCreate(EditSnakeNameController.class, LOCAL_GAME_EDIT_SNAKE_NAME_VIEW);
        if (stage.getOwner() == null) {
            stage.initOwner(LocalGameStage.get());
            stage.initModality(Modality.WINDOW_MODAL);
        }
        return stage;
    }

    public static void remove() {
        Stages.remove(LOCAL_GAME_EDIT_SNAKE_NAME_VIEW);
    }
}