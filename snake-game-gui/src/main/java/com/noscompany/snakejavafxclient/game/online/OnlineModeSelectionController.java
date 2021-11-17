package com.noscompany.snakejavafxclient.game.online;

import com.noscompany.snakejavafxclient.commons.Stages;
import com.noscompany.snakejavafxclient.commons.AbstractController;
import com.noscompany.snakejavafxclient.game.online.server.SnakeOnlineServerConfiguration;
import javafx.fxml.FXML;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

public class OnlineModeSelectionController extends AbstractController {

    @FXML
    @SneakyThrows
    public void createGame() {
        Stages.getOnlineModeSelectionStage().close();
        SnakeOnlineServerConfiguration.run(UUID.randomUUID().toString());
    }

    @FXML
    public void joinGame() {
        Stages.getOnlineModeSelectionStage().close();
        Stages.getSnakeOnlineClientStage().show();
    }

    @Override
    protected void doInitialize(URL location, ResourceBundle resources) {

    }
}