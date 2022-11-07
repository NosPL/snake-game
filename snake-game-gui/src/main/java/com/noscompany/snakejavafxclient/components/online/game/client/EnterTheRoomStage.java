package com.noscompany.snakejavafxclient.components.online.game.client;

import com.noscompany.snakejavafxclient.utils.Controllers;
import com.noscompany.snakejavafxclient.utils.Stages;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class EnterTheRoomStage {
    private static final String ENTER_THE_ROOM_VIEW = "enter-the-room-view.fxml";

    public static Stage get() {
        Stage stage = Stages.getOrCreate(EnterTheRoomController.class, ENTER_THE_ROOM_VIEW);
        if (stage.getOwner() == null) {
            stage.initOwner(SnakeOnlineClientStage.get());
            stage.initModality(Modality.WINDOW_MODAL);
        }
        stage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, event -> {
            boolean userInTheRoom = Controllers.get(EnterTheRoomController.class).isUserInTheRoom();
            if (!userInTheRoom)
                event.consume();
        });
        return stage;
    }
}