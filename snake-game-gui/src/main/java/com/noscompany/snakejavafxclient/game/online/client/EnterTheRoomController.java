package com.noscompany.snakejavafxclient.game.online.client;

import com.noscompany.snake.game.commons.messages.events.room.FailedToEnterRoom;
import com.noscompany.snake.game.commons.messages.events.room.NewUserEnteredRoom;
import com.noscompany.snakejavafxclient.commons.AbstractController;
import com.noscompany.snakejavafxclient.commons.Stages;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import lombok.Getter;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class EnterTheRoomController extends AbstractController {
    @FXML
    private TextField userNameTextField;
    @FXML
    private Label errorMessageLabel;
    private Consumer<String> consumer;
    private String currentUserName;
    @Getter
    private boolean userInTheRoom = false;

    @Override
    protected void doInitialize(URL location, ResourceBundle resources) {
        errorMessageLabel.setTextFill(Color.RED);
        errorMessageLabel.setText("");
        userNameTextField.setText("");
        currentUserName = "";
    }

    public void set(Consumer<String> consumer) {
        this.consumer = consumer;
    }

    @FXML
    public void enterTheRoom() {
        String userName = userNameTextField.getText();
        if (userName.isBlank()) {
            errorMessageLabel.setText("user name is blank");
        } else {
            consumer.accept(userName);
            this.currentUserName = userName;
        }
    }

    public void handle(NewUserEnteredRoom enteredRoom) {
        String userName = enteredRoom.getUserName();
        if (currentUserName.equalsIgnoreCase(userName)) {
            Stages.getEnterRoomStage().close();
            userInTheRoom = true;
            SnakeMoving.userInTheRoom();
        }
    }

    public void userNotInTheRoom() {
        Stages.getEnterRoomStage().show();
        userInTheRoom = false;
        errorMessageLabel.setText("you are not in the room");
    }

    public void handle(FailedToEnterRoom event) {
        userInTheRoom = false;
        SnakeMoving.userNotInTheRoom();
        errorMessageLabel.setText(event.getMessageType().toString());
    }
}