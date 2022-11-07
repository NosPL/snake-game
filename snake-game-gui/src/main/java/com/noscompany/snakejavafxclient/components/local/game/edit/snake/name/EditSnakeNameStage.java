package com.noscompany.snakejavafxclient.components.local.game.edit.snake.name;

import com.noscompany.snakejavafxclient.utils.Stages;
import com.noscompany.snakejavafxclient.components.local.game.LocalGameStage;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EditSnakeNameStage {
    private static final String LOCAL_GAME_EDIT_SNAKE_NAME_VIEW = "local-game-edit-snake-name-view.fxml";

    public static Stage get() {
        Stage stage = Stages.getOrCreate(EditSnakeNameController.class, LOCAL_GAME_EDIT_SNAKE_NAME_VIEW);
        if (stage.getOwner() == null) {
            stage.initOwner(LocalGameStage.get());
            stage.initModality(Modality.WINDOW_MODAL);
        }
        return stage;
    }
}