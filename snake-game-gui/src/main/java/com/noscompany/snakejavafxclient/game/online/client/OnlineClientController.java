package com.noscompany.snakejavafxclient.game.online.client;

import com.noscompany.snakejavafxclient.commons.AbstractController;
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

    @Override
    protected void doInitialize(URL location, ResourceBundle resources) {
        mainHBox.setOnKeyPressed(new KeyPressedHandler());
        vBox1.setOnKeyPressed(new KeyPressedHandler());
        vBox2.setOnKeyPressed(new KeyPressedHandler());
        vBox3.setOnKeyPressed(new KeyPressedHandler());
    }
}