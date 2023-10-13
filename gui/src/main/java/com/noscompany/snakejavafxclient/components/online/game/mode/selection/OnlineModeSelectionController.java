package com.noscompany.snakejavafxclient.components.online.game.mode.selection;

import com.noscompany.message.publisher.Subscription;
import com.noscompany.snakejavafxclient.components.online.game.client.SnakeOnlineGuiClientConfiguration;
import com.noscompany.snake.game.online.gui.commons.AbstractController;
import com.noscompany.snakejavafxclient.components.online.game.host.SnakeOnlineHostGuiConfiguration;
import javafx.fxml.FXML;
import lombok.SneakyThrows;

public class OnlineModeSelectionController extends AbstractController {

    @FXML
    @SneakyThrows
    public void runHost() {
        OnlineModeSelectionStage.get().close();
        new SnakeOnlineHostGuiConfiguration().configure();
    }

    @FXML
    @SneakyThrows
    public void runClient() {
        OnlineModeSelectionStage.get().close();
        new SnakeOnlineGuiClientConfiguration().configure();
    }

    @Override
    public Subscription getSubscription() {
        return new Subscription();
    }
}