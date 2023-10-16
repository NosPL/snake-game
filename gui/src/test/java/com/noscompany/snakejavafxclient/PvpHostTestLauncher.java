package com.noscompany.snakejavafxclient;

import com.noscompany.snake.game.online.gui.commons.ApplicationProfile;

public class PvpHostTestLauncher {

    public static void main(String[] args) {
        ApplicationProfile.profile = ApplicationProfile.Profile.TEST;
        SnakeGameApplication.main(args);
    }
}