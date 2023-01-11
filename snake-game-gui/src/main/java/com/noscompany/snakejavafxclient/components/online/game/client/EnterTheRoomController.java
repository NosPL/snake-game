package com.noscompany.snakejavafxclient.components.online.game.client;

import com.noscompany.snake.game.online.contract.messages.room.FailedToConnectToRoom;
import com.noscompany.snake.game.online.contract.messages.room.FailedToEnterRoom;
import com.noscompany.snake.game.online.contract.messages.room.NewUserEnteredRoom;
import com.noscompany.snakejavafxclient.utils.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import lombok.Getter;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static com.noscompany.snake.game.online.contract.messages.room.FailedToEnterRoom.Reason.USER_ALREADY_IN_THE_ROOM;

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

    public void onEnterTheRoomButtonPress(Consumer<String> consumer) {
        this.consumer = consumer;
    }

    @FXML
    public void enterTheRoom() {
        String userName = userNameTextField.getText();
        consumer.accept(userName);
        currentUserName = userName;
    }

    public void handle(NewUserEnteredRoom enteredRoom) {
        String userName = enteredRoom.getUserName();
        if (currentUserName.equals(userName)) {
            EnterTheRoomStage.get().close();
            userInTheRoom = true;
            SnakeMoving.userInTheRoom();
            SnakeOnlineClientStage.get().show();
        }
    }

    public void userNotInTheRoom(String reason) {
        EnterTheRoomStage.get().show();
        currentUserName = "";
        userInTheRoom = false;
        errorMessageLabel.setText(reason);
    }

    public void handle(FailedToEnterRoom event) {
        if (event.getReason() == USER_ALREADY_IN_THE_ROOM)
            return;
        userNotInTheRoom(event.getReason().toString());
    }

    public void handle(FailedToConnectToRoom event) {
        userNotInTheRoom(event.getReason().toString());
    }
}