package com.noscompany.snake.game.online.server;

import com.noscompany.snake.game.online.server.websocket.SnakeGameRoomWebSocket;
import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.nettosphere.Config;
import org.atmosphere.nettosphere.Nettosphere;

public class ServerRunner {
    private static Nettosphere nettosphere;

    public static void start(String host, int port) {
        nettosphere = new Nettosphere.Builder()
                .config(config(host, port))
                .build();
        nettosphere.start();
    }

    private static Config config(String host, int port) {
        return new Config.Builder()
                .resource(SnakeGameRoomWebSocket.class)
                .initParam(ApplicationConfig.SCAN_CLASSPATH, "false")
                .host(host)
                .port(port)
                .build();
    }

    public static void stop() {
        if (nettosphere != null)
            nettosphere.stop();
    }
}