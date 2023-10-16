package com.noscompany.snakejavafxclient;

import com.noscompany.snake.game.online.gui.commons.ApplicationProfile;

public class Launcher {

    public static void main(String[] args) {
        ApplicationProfile.profile = ApplicationProfile.Profile.PROD;
        SnakeGameApplication.main(args);
    }
}