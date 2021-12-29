package com.noscompany.snake.game.online.server;

import com.noscompany.snake.game.online.server.available.rooms.AvailableRoomsWebSocket;
import com.noscompany.snake.game.online.server.room.websocket.SnakeGameRoomWebSocket;
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
                .resource(AvailableRoomsWebSocket.class)
                .initParam(ApplicationConfig.SCAN_CLASSPATH, "false")
                .initParam(ApplicationConfig.UUIDBROADCASTERCACHE_CLIENT_IDLETIME, "2")
                .initParam(ApplicationConfig.UUIDBROADCASTERCACHE_IDLE_CACHE_INTERVAL, "2")
                .initParam(ApplicationConfig.BROADCASTER_LIFECYCLE_POLICY, "EMPTY_DESTROY")
                .initParam(ApplicationConfig.BROADCASTER_LIFECYCLE_POLICY_IDLETIME, "0")
                .initParam(ApplicationConfig.ANALYTICS, "false")
                .build();
    }
}