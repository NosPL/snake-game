package com.noscompany.snakejavafxclient.components.online.game;

import com.noscompany.snakejavafxclient.utils.AbstractController;
import com.noscompany.snakejavafxclient.components.mode.selection.OnlineModeSelectionStage;
import com.noscompany.snakejavafxclient.components.online.game.client.SnakeOnlineClientConfiguration;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class RoomJoiningController extends AbstractController {
    @FXML
    private TextField roomNameTextField;

    @FXML
    public void joinGame() {
        String roomName = roomNameTextField.getText();
        SnakeOnlineClientConfiguration.run(roomName);
        OnlineModeSelectionStage.get().close();
    }
}