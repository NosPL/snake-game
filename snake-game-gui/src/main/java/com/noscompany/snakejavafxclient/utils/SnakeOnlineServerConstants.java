package com.noscompany.snakejavafxclient.utils;

public class SnakeOnlineServerConstants {
    public static final String PROTOCOL = "ws://";
    public static final String HOST = "foo.bar";
    public static final String TEST_HOST = "127.0.0.1";
    public static final int PORT = 8080;
    public static final String WEBSOCKET_URL = "/room/";

    public static String getUrl(String roomName) {
        return PROTOCOL + HOST + ":" + PORT + WEBSOCKET_URL + roomName;
    }

    public static String getTestUrl(String roomName) {
        return PROTOCOL + TEST_HOST + ":" + PORT + WEBSOCKET_URL + roomName;
    }
}