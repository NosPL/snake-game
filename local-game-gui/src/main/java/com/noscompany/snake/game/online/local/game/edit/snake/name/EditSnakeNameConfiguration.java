package com.noscompany.snake.game.online.local.game.edit.snake.name;

import com.noscompany.snake.game.online.gui.commons.Controllers;
import javafx.stage.Stage;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;

public class EditSnakeNameConfiguration {

    public static void run(PlayerNumber playerNumber, String currentName) {
        Stage stage = EditSnakeNameStage.get();
        Controllers.get(EditSnakeNameController.class).init(playerNumber, currentName);
        stage.show();
    }
}