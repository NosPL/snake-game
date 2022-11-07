package com.noscompany.snakejavafxclient.components.local.game.edit.snake.name;

import com.noscompany.snakejavafxclient.utils.Controllers;
import javafx.stage.Stage;
import com.noscompany.snake.game.online.contract.messages.game.dto.PlayerNumber;

public class EditSnakeNameConfiguration {

    public static void run(PlayerNumber playerNumber, String currentName) {
        Stage stage = EditSnakeNameStage.get();
        var editSnakeNameController = Controllers.get(EditSnakeNameController.class);
        editSnakeNameController.init(playerNumber, currentName);
        stage.show();
    }
}