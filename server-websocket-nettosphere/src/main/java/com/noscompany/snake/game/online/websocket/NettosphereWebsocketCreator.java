package com.noscompany.snake.game.online.websocket;

import com.noscompany.snake.game.online.contract.messages.server.events.FailedToStartServer;
import com.noscompany.snake.game.online.contract.messages.server.ServerParams;
import com.noscompany.snake.game.online.host.server.WebsocketEventHandler;
import com.noscompany.snake.game.online.host.server.ports.Websocket;
import com.noscompany.snake.game.online.host.server.ports.WebsocketCreator;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.nettosphere.Config;
import org.atmosphere.nettosphere.Nettosphere;

import static com.noscompany.snake.game.online.contract.messages.server.events.FailedToStartServer.Reason.INCORRECT_IP_ADDRESS_FORMAT;
import static com.noscompany.snake.game.online.contract.messages.server.events.FailedToStartServer.Reason.PORT_IS_NOT_A_NUMBER;

@Slf4j
class NettosphereWebsocketCreator implements WebsocketCreator {
    static Nettosphere nettosphere;

    public Either<FailedToStartServer, Websocket> create(ServerParams serverParams, WebsocketEventHandler websocketEventHandler) {
        try {
            if (nettosphere != null)
                nettosphere.stop();
            if (!hostAddressHasCorrectFormat(serverParams.getIpAddress()))
                return Either.left(new FailedToStartServer(INCORRECT_IP_ADDRESS_FORMAT));
            if (!portIsANumber(serverParams.getPort()))
                return Either.left(new FailedToStartServer(PORT_IS_NOT_A_NUMBER));
            var ipAddress = serverParams.getIpAddress();
            var port = Integer.parseInt(serverParams.getPort());
            nettosphere = createNettosphere(ipAddress, port);
            nettosphere.start();
            SnakeGameRoomWebSocket.websocketEventHandler = websocketEventHandler;
            return Either.right(new NettosphereWebsocket(nettosphere));
        } catch (Throwable t) {
            log.warn("failed to start websocket server, cause: ", t);
            return Either.left(new FailedToStartServer(FailedToStartServer.Reason.OTHER));
        }
    }

    private boolean hostAddressHasCorrectFormat(String ipAddress) {
        if (ipAddress == null)
            return false;
        else
            return InetAddressValidator.getInstance().isValid(ipAddress);
    }

    private boolean portIsANumber(String port) {
        try {
            Integer.parseInt(port);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    private Nettosphere createNettosphere(String host, int port) {
        return new Nettosphere.Builder()
                .config(config(host, port))
                .build();
    }

    private Config config(String host, int port) {
        return new Config.Builder()
                .host(host)
                .port(port)
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