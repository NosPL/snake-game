package com.noscompany.snake.game.online.server;

import com.noscompany.snake.game.commons.SnakeOnlineServerConstants;
import com.noscompany.snake.game.online.server.websocket.SnakeGameRoomWebSocket;
import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.nettosphere.Config;
import org.atmosphere.nettosphere.Nettosphere;

public class Main {

    public static void main(String[] args) {
        new Nettosphere.Builder()
                .config(config())
                .build()
                .start();
    }

    private static Config config() {
        return new Config.Builder()
                .resource(SnakeGameRoomWebSocket.class)
                .initParam(ApplicationConfig.SCAN_CLASSPATH, "false")
                .host(SnakeOnlineServerConstants.HOST)
                .port(SnakeOnlineServerConstants.PORT)
                .build();
    }
}