package com.noscompany.snakejavafxclient.components.mode.selection;

import com.noscompany.message.publisher.Subscription;
import com.noscompany.snakejavafxclient.components.online.game.mode.selection.OnlineModeSelectionStage;
import com.noscompany.snake.game.online.gui.commons.AbstractController;
import com.noscompany.snake.game.online.local.game.LocalGameConfiguration;
import javafx.fxml.FXML;
import lombok.SneakyThrows;

public class GameModeSelectionController extends AbstractController {

    @FXML
    @SneakyThrows
    public void selectLocalMode() {
        GameModeSelectionStage.get().close();
        LocalGameConfiguration.run(() -> GameModeSelectionStage.get().show());
    }

    @FXML
    @SneakyThrows
    public void selectOnlineMode() {
        GameModeSelectionStage.get().close();
        OnlineModeSelectionStage.get().show();
    }

    @Override
    public Subscription getSubscription() {
        return new Subscription();
    }
}