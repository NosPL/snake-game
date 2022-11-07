package com.noscompany.snake.game.online.host.server;

import com.noscompany.snake.game.online.host.server.websocket.SnakeGameRoomWebSocket;
import com.noscompany.snake.game.online.host.server.available.rooms.AvailableRoomsWebSocket;
import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.nettosphere.Config;
import org.atmosphere.nettosphere.Nettosphere;

public class ServerRunner {
    private static Nettosphere nettosphere;

    public static void start(String host, int port) {
        if (nettosphere != null && nettosphere.isStarted())
            return;
        Config config = configBuilder()
                .host(host)
                .port(port)
                .build();
        nettosphere = new Nettosphere.Builder()
                .config(config)
                .build();
        nettosphere.start();
    }

    private static Config.Builder configBuilder() {
        return new Config.Builder()
                .resource(SnakeGameRoomWebSocket.class)
                .resource(AvailableRoomsWebSocket.class)
                .initParam(ApplicationConfig.SCAN_CLASSPATH, "false")
                .initParam(ApplicationConfig.ALLOW_WEBSOCKET_STATUS_CODE_1005_AS_DISCONNECT, "true")
                .initParam(ApplicationConfig.UUIDBROADCASTERCACHE_CLIENT_IDLETIME, "2")
                .initParam(ApplicationConfig.UUIDBROADCASTERCACHE_IDLE_CACHE_INTERVAL, "2")
                .initParam(ApplicationConfig.BROADCASTER_LIFECYCLE_POLICY, "EMPTY_DESTROY")
                .initParam(ApplicationConfig.BROADCASTER_LIFECYCLE_POLICY_IDLETIME, "0")
                .initParam(ApplicationConfig.ANALYTICS, "false");
    }

    public static void stop() {
        if (nettosphere != null)
            nettosphere.stop();
    }
}