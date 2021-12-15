package com.noscompany.snakejavafxclient.game.local.edit.snake.name;

import com.noscompany.snakejavafxclient.commons.Controllers;
import com.noscompany.snakejavafxclient.commons.Stages;
import javafx.stage.Stage;
import snake.game.core.dto.SnakeNumber;

public class EditSnakeNameConfiguration {

    public static void run(SnakeNumber snakeNumber, String currentName) {
        Stage stage = Stages.getEditSnakeNameStage();
        var editSnakeNameController = Controllers.get(EditSnakeNameController.class);
        editSnakeNameController.init(snakeNumber, currentName);
        stage.show();
    }
}