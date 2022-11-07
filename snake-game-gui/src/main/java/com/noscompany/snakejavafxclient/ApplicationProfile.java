package com.noscompany.snakejavafxclient;

import com.noscompany.snakejavafxclient.utils.SnakeOnlineServerConstants;
import io.vavr.Function1;

public class ApplicationProfile {
    private static Function1<String, String> serverUrlCreator = SnakeOnlineServerConstants::getUrl;

    public static void setToProd() {
        serverUrlCreator = SnakeOnlineServerConstants::getUrl;
    }

    public static void setToTest() {
        serverUrlCreator = SnakeOnlineServerConstants::getTestUrl;
    }

    public static String getSnakeOnlineServerUrl(String roomName) {
        return serverUrlCreator.apply(roomName);
    }
}