package com.noscompany.snake.game.online.gui.commons;

import com.noscompany.message.publisher.Subscription;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class AbstractController implements Initializable {

    @Override
    public final void initialize(URL location, ResourceBundle resources) {
        doInitialize(location, resources);
        Controllers.put(this);
    }
    protected void doInitialize(URL location, ResourceBundle resources) {
    }

    public abstract Subscription getSubscription();
}