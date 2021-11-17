package com.noscompany.snakejavafxclient.game.online.server;

import com.noscompany.snake.game.server.local.api.ServerError;
import com.noscompany.snake.game.server.local.api.StartingServerError;
import com.noscompany.snakejavafxclient.commons.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.function.BiConsumer;

public class StartServerController extends AbstractController {
    @FXML
    private TextField ipv4TextField;
    @FXML
    private TextField portTextField;
    @FXML
    private Label serverMessageLabel;
    @FXML
    private Button startServerButton;
    private BiConsumer<String, String> startServerAction = (ip, port) -> {
    };
    private Runnable stopServerAction = () -> {
    };

    public StartServerController onStartServer(BiConsumer<String, String> startServerAction) {
        this.startServerAction = startServerAction;
        return this;
    }

    public StartServerController onStopServer(Runnable stopServerAction) {
        this.stopServerAction = stopServerAction;
        return this;
    }

    @FXML
    public void startServer() {
        String ip = ipv4TextField.getText();
        var port = portTextField.getText();
        startServerAction.accept(ip, port);
    }

    @FXML
    public void closeServer() {
        stopServerAction.run();
    }

    public void print(StartingServerError error) {
        serverMessageLabel.setText(error.toString());
    }

    public void printServerClosed() {
        serverMessageLabel.setText("SERVER CLOSED");
    }

    public void print(ServerError error) {
        serverMessageLabel.setText(error.toString());
    }

    public void printServerStarted() {
        serverMessageLabel.setText("SERVER STARTED");
    }
}