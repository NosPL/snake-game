package com.noscompany.snakejavafxclient.game.online.client;

import com.noscompany.snake.game.client.ClientError;
import com.noscompany.snake.game.client.StartingClientError;
import com.noscompany.snakejavafxclient.commons.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.function.BiConsumer;

public class JoinServerController extends AbstractController {
    @FXML
    private TextField ipv4TextField;
    @FXML
    private TextField portTextField;
    @FXML
    private Label clientMessageLabel;
    private BiConsumer<String, String> joinServerAction = (ip, port) -> {
    };
    private Runnable leaveServerAction = () -> {
    };

    public JoinServerController onJoinServer(BiConsumer<String, String> joinServerAction) {
        this.joinServerAction = joinServerAction;
        return this;
    }

    public JoinServerController onLeaveServer(Runnable leaveServerAction) {
        this.leaveServerAction = leaveServerAction;
        return this;
    }

    @FXML
    public void joinServer() {
        var ip = ipv4TextField.getText();
        var port = portTextField.getText();
        joinServerAction.accept(ip, port);
    }

    @FXML
    public void leaveServer() {
        leaveServerAction.run();
    }

    public void connectionEstablished() {
        clientMessageLabel.setText("CONNECTION ESTABLISHED");
    }

    public void print(ClientError error) {
        clientMessageLabel.setText(error.toString());
    }

    public void print(StartingClientError error) {
        clientMessageLabel.setText(error.toString());
    }

    public void printConnectionClosed() {
        clientMessageLabel.setText("CONNECTION CLOSED");
    }
}