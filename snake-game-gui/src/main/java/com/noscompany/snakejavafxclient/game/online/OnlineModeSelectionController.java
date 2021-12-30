package com.noscompany.snakejavafxclient.game.online;

import com.noscompany.snakejavafxclient.commons.AbstractController;
import com.noscompany.snakejavafxclient.commons.Stages;
import com.noscompany.snakejavafxclient.game.online.client.SnakeOnlineClientConfiguration;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class OnlineModeSelectionController extends AbstractController {
    @FXML
    private TextField roomNameTextField;

    @FXML
    public void joinGame() {
        String roomName = roomNameTextField.getText();
        SnakeOnlineClientConfiguration.run(roomName);
        Stages.getOnlineModeSelectionStage().close();
    }
}