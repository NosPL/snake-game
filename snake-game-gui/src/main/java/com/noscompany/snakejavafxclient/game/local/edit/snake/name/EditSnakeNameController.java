package com.noscompany.snakejavafxclient.game.local.edit.snake.name;

import com.noscompany.snakejavafxclient.commons.AbstractController;
import com.noscompany.snakejavafxclient.commons.Stages;
import com.noscompany.snakejavafxclient.game.GuiGameEventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import snake.game.core.dto.SnakeNumber;

import java.net.URL;
import java.util.ResourceBundle;

public class EditSnakeNameController extends AbstractController {
    private GuiGameEventHandler guiGameEventHandler;
    private SnakeNumber snakeNumber;
    private String currentName;
    @FXML
    private TextField textField;
    @FXML
    private Label errorMessageLabel;

    @FXML
    public void changeName() {
        String newSnakeName = textField.getText();
        if (isValid(newSnakeName)) {
            errorMessageLabel.setVisible(false);
            Stages.getEditSnakeNameStage().close();
            textField.setText("");
            guiGameEventHandler.snakeNameUpdated(snakeNumber, newSnakeName);
        }
        else
            errorMessageLabel.setVisible(true);
    }

    private boolean isValid(String newSnakeName) {
        return newSnakeName.length() >= 1 && newSnakeName.length() <= 10;
    }

    public void init(SnakeNumber snakeNumber, String currentName) {
        this.errorMessageLabel.setVisible(false);
        this.snakeNumber = snakeNumber;
        this.textField.setText(currentName);
    }

    @Override
    protected void doInitialize(URL location, ResourceBundle resources) {
        this.guiGameEventHandler = GuiGameEventHandler.javaFxEventHandler();
        this.textField.setText("");
        this.errorMessageLabel.setVisible(false);
    }
}