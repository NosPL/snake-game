package com.noscompany.snakejavafxclient.commons;

import com.noscompany.snakejavafxclient.game.local.LocalSnakeGame;
import com.noscompany.snakejavafxclient.mode.selection.GameModeSelectionController;
import com.noscompany.snakejavafxclient.game.online.OnlineModeSelectionController;
import com.noscompany.snakejavafxclient.game.online.client.OnlineClientController;
import com.noscompany.snakejavafxclient.game.online.server.OnlineServerController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

public class Stages {
    private static final Map<String, Stage> stages = new HashMap<>();
    private static final String GAME_MODE_SELECTION_VIEW = "game-mode-selection-view.fxml";
    private static final String ONLINE_MODE_SELECTION_VIEW = "online-mode-selection-view.fxml";
    private static final String SNAKE_ONLINE_SERVER_VIEW = "snake-online-server-view.fxml";
    private static final String SNAKE_ONLINE_CLIENT_VIEW = "snake-online-client-view.fxml";
    private static final String LOCAL_GAME_VIEW = "local-game-view.fxml";

    public static Stage getGameModeSelectionStage() {
        Stage stage = get(GameModeSelectionController.class, GAME_MODE_SELECTION_VIEW);
        stage.setOnCloseRequest(e -> Platform.exit());
        return stage;
    }

    public static Stage getLocalGameStage() {
        final Stage stage = get(LocalSnakeGame.class, LOCAL_GAME_VIEW);
        stage.setOnCloseRequest(e -> {
            getGameModeSelectionStage().show();
            stages.remove(LOCAL_GAME_VIEW);
        });
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

    public static Stage getSnakeServerStage() {
        Stage stage = get(OnlineServerController.class, SNAKE_ONLINE_SERVER_VIEW);
        stage.setOnCloseRequest(e -> {
            getGameModeSelectionStage().show();
            stages.remove(SNAKE_ONLINE_SERVER_VIEW);
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

    public static void removeLocalGameStage() {
        stages.remove(LOCAL_GAME_VIEW);
    }

    public static void removeSnakeOnlineServerStage() {
        stages.remove(SNAKE_ONLINE_SERVER_VIEW);
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
        FXMLLoader fxmlLoader = new FXMLLoader(clazz.getResource(fxmlFilePath));
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        return stage;
    }
}