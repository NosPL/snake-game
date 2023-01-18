package com.noscompany.snake.game.online.host.server.internal.state.running;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noscompany.snake.game.online.contract.object.mapper.ObjectMapperCreator;
import com.noscompany.snake.game.online.host.server.dto.ServerParams;
import com.noscompany.snake.game.online.host.room.mediator.RoomMediatorForRemoteClients;
import com.noscompany.snake.game.online.host.server.internal.state.ServerState;
import io.vavr.control.Try;
import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.nettosphere.Config;
import org.atmosphere.nettosphere.Nettosphere;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

public class RunningServerStateCreator {

    public static Try<ServerState> createRunningServer(ServerParams serverParams, RoomMediatorForRemoteClients roomMediatorForRemoteClients) {
        return Try.of(() -> {
            SnakeGameRoomWebSocket.roomMediatorForRemoteClients = roomMediatorForRemoteClients;
            ObjectMapper objectMapper = ObjectMapperCreator.createInstance();
            Nettosphere nettosphere = createNettosphere(serverParams);
            nettosphere.start();
            return new RunningServerState(nettosphere, objectMapper);
        });
    }

    private static Nettosphere createNettosphere(ServerParams serverParams) {
        return new Nettosphere.Builder()
                .config(config(serverParams))
                .build();
    }

    private static Config config(ServerParams serverParams) {
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