package com.noscompany.snakejavafxclient.components.local.game.edit.snake.name;

import com.noscompany.message.publisher.Subscription;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.gui.commons.AbstractController;
import com.noscompany.snakejavafxclient.components.local.game.SnakeNameUpdated;
import io.vavr.control.Either;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static com.noscompany.snakejavafxclient.components.local.game.edit.snake.name.FailedToChangeSnakeName.NAME_CANNOT_BE_EMPTY;
import static com.noscompany.snakejavafxclient.components.local.game.edit.snake.name.FailedToChangeSnakeName.NAME_CANNOT_BE_LONGER_THAN_15_SIGNS;

public class EditSnakeNameController extends AbstractController {
    private Consumer<SnakeNameUpdated> consumer;
    private PlayerNumber playerNumber;
    @FXML
    private TextField textField;
    @FXML
    private Label errorMessageLabel;

    @FXML
    public void changeName() {
        var newSnakeName = textField.getText();
        validate(newSnakeName)
                .peekLeft(this::printError)
                .peek(snakeName -> changeName(newSnakeName));
    }

    public void setSnakeNameUpdatedConsumer(Consumer<SnakeNameUpdated> consumer) {
        this.consumer = consumer;
    }

    private void changeName(String newSnakeName) {
        errorMessageLabel.setVisible(false);
        EditSnakeNameStage.get().close();
        textField.setText("");
        consumer.accept(new SnakeNameUpdated(playerNumber, newSnakeName));
    }

    private void printError(FailedToChangeSnakeName error) {
        errorMessageLabel.setText(error.astTest());
        errorMessageLabel.setVisible(true);
    }

    private Either<FailedToChangeSnakeName, String> validate(String newSnakeName) {
        if (newSnakeName.isBlank())
            return Either.left(NAME_CANNOT_BE_EMPTY);
        else if (newSnakeName.codePoints().count() > 15)
            return Either.left(NAME_CANNOT_BE_LONGER_THAN_15_SIGNS);
        else
            return Either.right(newSnakeName);
    }

    void init(PlayerNumber playerNumber, String currentName) {
        this.errorMessageLabel.setVisible(false);
        this.playerNumber = playerNumber;
        this.textField.setText(currentName);
    }

    @Override
    protected void doInitialize(URL location, ResourceBundle resources) {
        this.textField.setText("");
        this.errorMessageLabel.setVisible(false);
        this.textField.setOnKeyPressed(key -> {
            if (key.getCode().equals(KeyCode.ENTER))
                changeName();
        });
    }

    @Override
    public Subscription getSubscription() {
        return new Subscription();
    }
}