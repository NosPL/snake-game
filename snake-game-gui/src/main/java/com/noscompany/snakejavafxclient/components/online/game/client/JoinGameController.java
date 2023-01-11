package com.noscompany.snakejavafxclient.components.online.game.client;

import com.noscompany.snake.game.online.client.ClientError;
import com.noscompany.snake.game.online.client.StartingClientError;
import com.noscompany.snakejavafxclient.utils.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class JoinGameController extends AbstractController {
    @FXML private TextField ip1TextField;
    @FXML private TextField ip2TextField;
    @FXML private TextField ip3TextField;
    @FXML private TextField ip4TextField;
    @FXML private TextField portTextField;
    @FXML private Label errorMessageLabel;
    private final HostAddressCreator hostAddressCreator = new HostAddressCreator();

    @FXML
    public void joinGame() {
        hostAddressCreator
                .create(
                        ip1TextField.getText(),
                        ip2TextField.getText(),
                        ip3TextField.getText(),
                        ip4TextField.getText(),
                        portTextField.getText())
                .peek(SnakeOnlineGuiClientConfiguration::run)
                .peekLeft(this::handle);
    }

    private void handle(HostAddressCreator.Error error) {
        String message = error
                .toString()
                .replace("_", " ")
                .toLowerCase();
        errorMessageLabel.setText(message);
    }

    public void handle(ClientError clientError) {
        String message = clientError
                .toString()
                .replace("_", " ")
                .toLowerCase();
        errorMessageLabel.setText(message);
    }

    public void handle(StartingClientError startingClientError) {
        String message = startingClientError
                .toString()
                .replace("_", " ")
                .toLowerCase();
        errorMessageLabel.setText(message);
    }

    public void connectionEstablished() {
        JoinGameStage.get().close();
        EnterTheRoomStage.get().show();
    }
}