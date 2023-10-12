 package com.noscompany.snake.game.online.chat.gui.test;

 import javafx.application.Application;
 import javafx.scene.Scene;
 import javafx.stage.Stage;

public final class GuiApp extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        var chat = new ChatGuiTestConfiguration().create();
        primaryStage.setScene(new Scene(chat));
        primaryStage.show();
    }
}