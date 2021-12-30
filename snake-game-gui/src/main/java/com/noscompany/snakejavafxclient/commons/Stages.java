package com.noscompany.snakejavafxclient.commons;

import com.noscompany.snakejavafxclient.game.local.LocalSnakeGame;
import com.noscompany.snakejavafxclient.game.local.edit.snake.name.EditSnakeNameController;
import com.noscompany.snakejavafxclient.game.online.OnlineModeSelectionController;
import com.noscompany.snakejavafxclient.game.online.client.EnterTheRoomController;
import com.noscompany.snakejavafxclient.game.online.client.OnlineClientController;
import com.noscompany.snakejavafxclient.mode.selection.GameModeSelectionController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Stages {
    private static final Map<String, Stage> stages = new HashMap<>();
    private static final String GAME_MODE_SELECTION_VIEW = "game-mode-selection-view.fxml";
    private static final String ONLINE_MODE_SELECTION_VIEW = "online-mode-selection-view.fxml";
    private static final String SNAKE_ONLINE_CLIENT_VIEW = "snake-online-client-view.fxml";
    private static final String ENTER_THE_ROOM_VIEW = "enter-the-room-view.fxml";
    private static final String LOCAL_GAME_VIEW = "local-game-view.fxml";
    private static final String LOCAL_GAME_EDIT_SNAKE_NAME_VIEW = "local-game-edit-snake-name-view.fxml";

    public static Stage getGameModeSelectionStage() {
        Stage stage = get(GameModeSelectionController.class, GAME_MODE_SELECTION_VIEW);
        stage.setOnCloseRequest(e -> Platform.exit());
        return stage;
    }

    public static Stage getLocalGameStage() {
        final Stage stage = get(LocalSnakeGame.class, LOCAL_GAME_VIEW);
        stage.setOnCloseRequest(e -> {
            stages.remove(LOCAL_GAME_VIEW);
            stages.remove(LOCAL_GAME_EDIT_SNAKE_NAME_VIEW);
            getGameModeSelectionStage().show();
        });
        return stage;
    }

    public static Stage getEditSnakeNameStage() {
        Stage stage = get(EditSnakeNameController.class, LOCAL_GAME_EDIT_SNAKE_NAME_VIEW);
        if (stage.getOwner() == null) {
            stage.initOwner(getLocalGameStage());
            stage.initModality(Modality.WINDOW_MODAL);
        }
        return stage;
    }

    public static Stage getOnlineModeSelectionStage() {
        Stage stage = get(OnlineModeSelectionController.class, ONLINE_MODE_SELECTION_VIEW);
        stage.setOnCloseRequest(e -> {
            getGameModeSelectionStage().show();
            stages.remove(ONLINE_MODE_SELECTION_VIEW);
        });
        return stage;
    }

    public static Stage getSnakeOnlineClientStage() {
        Stage stage = get(OnlineClientController.class, SNAKE_ONLINE_CLIENT_VIEW);
        stage.setOnCloseRequest(e -> {
            getGameModeSelectionStage().show();
            stages.remove(SNAKE_ONLINE_CLIENT_VIEW);
        });
        return stage;
    }

    public static Stage getEnterRoomStage() {
        Stage stage = get(EnterTheRoomController.class, ENTER_THE_ROOM_VIEW);
        if (stage.getOwner() == null) {
            stage.initOwner(getSnakeOnlineClientStage());
            stage.initModality(Modality.WINDOW_MODAL);
        }
        stage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, event -> {
            boolean userInTheRoom = Controllers.get(EnterTheRoomController.class).isUserInTheRoom();
            if (!userInTheRoom)
                event.consume();
        });
        return stage;
    }

    public static void removeLocalGameStage() {
        stages.remove(LOCAL_GAME_VIEW);
    }

    public static void removeSnakeOnlineClientStage() {
        stages.remove(SNAKE_ONLINE_CLIENT_VIEW);
    }

    private static Stage get(Class<?> clazz, String fxmlFilePath) {
        Stage stage = stages.get(fxmlFilePath);
        if (stage != null)
            return stage;
        stage = createStage(clazz, fxmlFilePath);
        stages.put(fxmlFilePath, stage);
        return stage;
    }

    @SneakyThrows
    private static Stage createStage(Class<?> clazz, String fxmlFilePath) {
        URL resource = clazz.getResource(fxmlFilePath);
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        return stage;
    }
}