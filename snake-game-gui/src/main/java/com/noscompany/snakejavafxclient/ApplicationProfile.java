package com.noscompany.snakejavafxclient;

import com.noscompany.snake.game.commons.SnakeOnlineServerConstants;
import io.vavr.Function1;

public class ApplicationProfile {
    private static Function1<String, String> serverUrlCreator = SnakeOnlineServerConstants::getUrl;
    private static String host = SnakeOnlineServerConstants.HOST;

    public static void setToProd() {
        serverUrlCreator = SnakeOnlineServerConstants::getUrl;
        host = SnakeOnlineServerConstants.HOST;
    }

    public static void setToTest() {
        serverUrlCreator = SnakeOnlineServerConstants::getTestUrl;
        host = SnakeOnlineServerConstants.TEST_HOST;
    }

    public static String getSnakeOnlineServerUrl(String roomName) {
        return serverUrlCreator.apply(roomName);
    }

    public static String getHost() {
        return host;
    }
}