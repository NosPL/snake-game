package com.noscompany.snakejavafxclient;

public class PvpHostTestLauncher {

    public static void main(String[] args) {
        ApplicationProfile.profile = ApplicationProfile.Profile.TEST;
        SnakeGameApplication.main(args);
    }
}