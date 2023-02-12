package com.noscompany.snake.game.online.websocket;

import com.noscompany.snake.game.online.host.server.dto.ServerParams;
import com.noscompany.snake.game.online.host.server.ports.Websocket;
import com.noscompany.snake.game.online.host.server.ports.WebsocketCreator;
import com.noscompany.snake.game.online.host.server.WebsocketEventHandler;
import io.vavr.control.Try;
import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.nettosphere.Config;
import org.atmosphere.nettosphere.Nettosphere;

class NettosphereWebsocketCreator implements WebsocketCreator {
    static Nettosphere nettosphere;

    public Try<Websocket> create(ServerParams serverParams, WebsocketEventHandler websocketEventHandler) {
        return Try.of(() -> {
            if (nettosphere != null)
                nettosphere.stop();
            nettosphere = createNettosphere(serverParams);
            nettosphere.start();
            SnakeGameRoomWebSocket.websocketEventHandler = websocketEventHandler;
            return new NettosphereWebsocket(nettosphere);
        });
    }

    private Nettosphere createNettosphere(ServerParams serverParams) {
        return new Nettosphere.Builder()
                .config(config(serverParams))
                .build();
    }

    private Config config(ServerParams serverParams) {
        return new Config.Builder()
                .host(serverParams.getHost())
                .port(serverParams.getPort())
                .resource(SnakeGameRoomWebSocket.class)
                .initParam(ApplicationConfig.SCAN_CLASSPATH, "false")
                .initParam(ApplicationConfig.ALLOW_WEBSOCKET_STATUS_CODE_1005_AS_DISCONNECT, "true")
                .initParam(ApplicationConfig.UUIDBROADCASTERCACHE_CLIENT_IDLETIME, "2")
                .initParam(ApplicationConfig.UUIDBROADCASTERCACHE_IDLE_CACHE_INTERVAL, "2")
                .initParam(ApplicationConfig.BROADCASTER_LIFECYCLE_POLICY, "EMPTY_DESTROY")
                .initParam(ApplicationConfig.BROADCASTER_LIFECYCLE_POLICY_IDLETIME, "0")
                .initParam(ApplicationConfig.ANALYTICS, "false")
                .build();
    }
}