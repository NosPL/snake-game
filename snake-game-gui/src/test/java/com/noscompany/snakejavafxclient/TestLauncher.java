package com.noscompany.snakejavafxclient;

import com.noscompany.snake.game.commons.SnakeOnlineServerConstants;
import com.noscompany.snake.game.online.server.ServerRunner;

public class TestLauncher {

    public static void main(String[] args) {
        ServerRunner.start(SnakeOnlineServerConstants.TEST_HOST, SnakeOnlineServerConstants.PORT);
        ApplicationProfile.setToTest();
        SnakeGameApplication.main(args);
        ServerRunner.stop();
    }
}