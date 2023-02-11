package com.noscompany.snakejavafxclient;

public class Launcher {

    public static void main(String[] args) {
        ApplicationProfile.profile = ApplicationProfile.Profile.PROD;
        SnakeGameApplication.main(args);
    }
}