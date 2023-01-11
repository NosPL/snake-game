package com.noscompany.snake.game.online.host.server.nettosphere.internal.state.running;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noscompany.snake.game.online.contract.object.mapper.ObjectMapperCreator;
import com.noscompany.snake.game.online.host.server.dto.ServerParams;
import com.noscompany.snake.game.online.host.room.mediator.RoomMediatorForRemoteClients;
import com.noscompany.snake.game.online.host.server.nettosphere.internal.state.ServerState;
import io.vavr.control.Try;
import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.nettosphere.Config;
import org.atmosphere.nettosphere.Nettosphere;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

public class RunningServerStateCreator {

    public static Try<ServerState> createRunningServer(ServerParams serverParams, RoomMediatorForRemoteClients roomMediatorForRemoteClients) {
        try {
            RoomMediatorHolder.roomMediatorForRemoteClients = roomMediatorForRemoteClients;
            ObjectMapper objectMapper = ObjectMapperCreator.createInstance();
            Nettosphere nettosphere = createNettosphere(serverParams.getHost(), serverParams.getPort());
            nettosphere.start();
            Broadcaster broadcaster = nettosphere.framework().getBroadcasterFactory().lookup("room");
            requireNonNull(broadcaster);
            RunningServerState runningServerState = new RunningServerState(nettosphere, broadcaster, objectMapper);
            return Try.success(runningServerState);
        } catch (Throwable t) {
            return Try.failure(t);
        }

    }

    public static Nettosphere createNettosphere(String host, int port) {
        return new Nettosphere.Builder()
                .config(config(host, port))
                .build();
    }

    @NotNull
    private static Config config(String host, int port) {
        Config config = configBuilder()
                .host(host)
                .port(port)
                .build();
        System.out.println(config.host());
        return config;
    }

    private static Config.Builder configBuilder() {
        return new Config.Builder()
                .resource(SnakeGameRoomWebSocket.class)
                .initParam(ApplicationConfig.SCAN_CLASSPATH, "false")
                .initParam(ApplicationConfig.ALLOW_WEBSOCKET_STATUS_CODE_1005_AS_DISCONNECT, "true")
                .initParam(ApplicationConfig.UUIDBROADCASTERCACHE_CLIENT_IDLETIME, "2")
                .initParam(ApplicationConfig.UUIDBROADCASTERCACHE_IDLE_CACHE_INTERVAL, "2")
                .initParam(ApplicationConfig.BROADCASTER_LIFECYCLE_POLICY, "EMPTY_DESTROY")
                .initParam(ApplicationConfig.BROADCASTER_LIFECYCLE_POLICY_IDLETIME, "0")
                .initParam(ApplicationConfig.ANALYTICS, "false");
    }
}