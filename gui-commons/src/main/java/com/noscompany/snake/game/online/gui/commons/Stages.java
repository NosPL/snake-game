package com.noscompany.snake.game.online.gui.commons;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Stages {
    private static final Map<String, Stage> stages = new HashMap<>();

    public static void remove(String stageName) {
        stages.remove(stageName);
    }

    public static Stage getOrCreate(Class<?> clazz, String fxmlFilePath) {
        Stage stage = stages.get(fxmlFilePath);
        if (stage != null)
            return stage;
        stage = createStage(clazz, fxmlFilePath);
        stages.put(fxmlFilePath, stage);
        return stage;
    }

    private static Stage createStage(Class<?> clazz, String fxmlFilePath) {
        URL resource = clazz.getResource(fxmlFilePath);
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent parent = null;
        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        return stage;
    }
}