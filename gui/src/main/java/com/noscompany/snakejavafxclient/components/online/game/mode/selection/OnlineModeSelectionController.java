package com.noscompany.snakejavafxclient.components.online.game.mode.selection;

import com.noscompany.message.publisher.Subscription;
import com.noscompany.snake.game.online.gui.commons.AbstractController;
import com.noscompany.snake.game.online.remote.client.gui.SnakeOnlineGuiClientConfiguration;
import com.noscompany.snakejavafxclient.components.mode.selection.GameModeSelectionStage;
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
        new SnakeOnlineGuiClientConfiguration().configure(() -> GameModeSelectionStage.get().show());
    }

    @Override
    public Subscription getSubscription() {
        return new Subscription();
    }
}