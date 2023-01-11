package com.noscompany.snakejavafxclient.components.online.game.client;

import com.noscompany.snake.game.online.client.SnakeOnlineClient;
import com.noscompany.snakejavafxclient.utils.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class OnlineClientController extends AbstractController {
    @FXML
    private HBox mainHBox;
    @FXML
    private VBox vBox1;
    @FXML
    private VBox vBox2;
    @FXML
    private VBox vBox3;
    private SnakeOnlineClient snakeOnlineClient;

    public void setSnakeOnlineClient(SnakeOnlineClient snakeOnlineClient) {
        this.snakeOnlineClient = snakeOnlineClient;
    }

    public void disconnectClient() {
        snakeOnlineClient.disconnect();
    }

    @Override
    protected void doInitialize(URL location, ResourceBundle resources) {
        mainHBox.setOnKeyPressed(new KeyPressedHandler());
        vBox1.setOnKeyPressed(new KeyPressedHandler());
        vBox2.setOnKeyPressed(new KeyPressedHandler());
        vBox3.setOnKeyPressed(new KeyPressedHandler());
    }
}