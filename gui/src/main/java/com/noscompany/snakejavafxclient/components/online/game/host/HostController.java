package com.noscompany.snakejavafxclient.components.online.game.host;

import com.noscompany.message.publisher.Subscription;
import com.noscompany.snakejavafxclient.components.online.game.commons.KeyPressedHandler;
import com.noscompany.snake.game.online.gui.commons.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class HostController extends AbstractController {
    @FXML
    private HBox mainHBox;
    @FXML
    private VBox vBox1;
    @FXML
    private VBox vBox2;
    @FXML
    private VBox vBox3;

    public void set(KeyPressedHandler keyPressedEventHandler) {
        mainHBox.setOnKeyPressed(keyPressedEventHandler);
        vBox1.setOnKeyPressed(keyPressedEventHandler);
        vBox2.setOnKeyPressed(keyPressedEventHandler);
        vBox3.setOnKeyPressed(keyPressedEventHandler);
    }

    @Override
    public Subscription getSubscription() {
        return new Subscription();
    }
}