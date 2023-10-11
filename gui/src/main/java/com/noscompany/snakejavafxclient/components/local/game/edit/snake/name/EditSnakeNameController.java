package com.noscompany.snakejavafxclient.components.local.game.edit.snake.name;

import com.noscompany.snake.game.online.gui.commons.AbstractController;
import com.noscompany.snakejavafxclient.components.local.game.GuiLocalGameEventHandler;
import io.vavr.control.Option;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;

import java.net.URL;
import java.util.ResourceBundle;

public class EditSnakeNameController extends AbstractController {
    private static final String TOO_LONG_NAME_MESSAGE = "NAME CANNOT BE LONGER THAN 15 SIGNS";
    private static final String BLANK_NAME_MESSAGE = "NAME CANNOT BE EMPTY";
    private GuiLocalGameEventHandler guiGameEventHandler;
    private PlayerNumber playerNumber;
    private String currentName;
    @FXML
    private TextField textField;
    @FXML
    private Label errorMessageLabel;

    @FXML
    public void changeName() {
        String newSnakeName = textField.getText();
        getErrorMessage(newSnakeName)
                .peek(this::printError)
                .onEmpty(() -> changeName(newSnakeName));
    }

    private void changeName(String newSnakeName) {
        errorMessageLabel.setVisible(false);
        EditSnakeNameStage.get().close();
        textField.setText("");
        guiGameEventHandler.snakeNameUpdated(playerNumber, newSnakeName);
    }

    private void printError(String errorMessage) {
        errorMessageLabel.setText(errorMessage);
        errorMessageLabel.setVisible(true);
    }

    private Option<String> getErrorMessage(String newSnakeName) {
        if (newSnakeName.isBlank())
            return Option.of(BLANK_NAME_MESSAGE);
        else if (newSnakeName.codePoints().count() > 15)
            return Option.of(TOO_LONG_NAME_MESSAGE);
        else
            return Option.none();
    }

    public void init(PlayerNumber playerNumber, String currentName) {
        this.errorMessageLabel.setVisible(false);
        this.playerNumber = playerNumber;
        this.textField.setText(currentName);
    }

    @Override
    protected void doInitialize(URL location, ResourceBundle resources) {
        this.guiGameEventHandler = GuiLocalGameEventHandler.javaFxEventHandler();
        this.textField.setText("");
        this.errorMessageLabel.setVisible(false);
        this.textField.setOnKeyPressed(key -> {
            if (key.getCode().equals(KeyCode.ENTER))
                changeName();
        });
    }
}