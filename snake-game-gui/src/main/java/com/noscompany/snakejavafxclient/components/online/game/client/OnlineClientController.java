package com.noscompany.snakejavafxclient.components.online.game.client;

import com.noscompany.snake.game.online.client.SnakeOnlineClient;
import com.noscompany.snakejavafxclient.components.online.game.commons.KeyPressedHandler;
import com.noscompany.snakejavafxclient.utils.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
        mainHBox.setOnKeyPressed(new KeyPressedHandler(snakeOnlineClient::changeSnakeDirection));
        vBox1.setOnKeyPressed(new KeyPressedHandler(snakeOnlineClient::changeSnakeDirection));
        vBox2.setOnKeyPressed(new KeyPressedHandler(snakeOnlineClient::changeSnakeDirection));
        vBox3.setOnKeyPressed(new KeyPressedHandler(snakeOnlineClient::changeSnakeDirection));
        this.snakeOnlineClient = snakeOnlineClient;
    }

    public void disconnectClient() {
        if (snakeOnlineClient != null)
            snakeOnlineClient.disconnect();
    }
}