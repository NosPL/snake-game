package com.noscompany.snake.game.test.client;

import com.noscompany.snake.game.online.client.ClientEventHandler;
import com.noscompany.snake.game.online.client.HostAddress;
import com.noscompany.snake.game.online.client.SnakeOnlineClient;
import com.noscompany.snake.game.online.client.StartingClientError;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.*;
import com.noscompany.snake.game.online.host.server.Server;
import com.noscompany.snake.game.online.contract.messages.server.ServerParams;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
class SnakeOnlineTestClient implements SnakeOnlineClient {
    private final SnakeOnlineClient snakeOnlineClient;
    private final ClientEventHandler clientEventHandler;
    private final Server server;

    @Override
    public void connect(HostAddress hostAddress) {
        server
                .start(serverParams())
                .peek(serverStarted -> snakeOnlineClient.connect(hostAddress))
                .peekLeft(failedToStartServer -> log.error("failed to start server: cause {}", failedToStartServer.getReason()))
                .peekLeft(failedToStartServer -> clientEventHandler.handle(StartingClientError.FAILED_TO_CONNECT_TO_SERVER));
    }
    
    @Override
    public void enterTheRoom(String userName) {
        server
                .start(serverParams())
                .peek(serverStarted -> snakeOnlineClient.enterTheRoom(userName))
                .peekLeft(failedToStartServer -> log.error("failed to start server: cause {}", failedToStartServer.getReason()))
                .peekLeft(failedToStartServer -> clientEventHandler.handle(StartingClientError.FAILED_TO_CONNECT_TO_SERVER));
    }

    @Override
    public void takeASeat(PlayerNumber playerNumber) {
        server
                .start(serverParams())
                .peek(serverStarted -> snakeOnlineClient.takeASeat(playerNumber))
                .peekLeft(failedToStartServer -> log.error("failed to start server: cause {}", failedToStartServer.getReason()))
                .peekLeft(failedToStartServer -> clientEventHandler.handle(StartingClientError.FAILED_TO_CONNECT_TO_SERVER));
    }

    @Override
    public void freeUpASeat() {
        server
                .start(serverParams())
                .peek(serverStarted -> snakeOnlineClient.freeUpASeat())
                .peekLeft(failedToStartServer -> log.error("failed to start server: cause {}", failedToStartServer.getReason()))
                .peekLeft(failedToStartServer -> clientEventHandler.handle(StartingClientError.FAILED_TO_CONNECT_TO_SERVER));
    }

    @Override
    public void changeGameOptions(GridSize gridSize, GameSpeed gameSpeed, Walls walls) {
        server
                .start(serverParams())
                .peek(serverStarted -> snakeOnlineClient.changeGameOptions(gridSize, gameSpeed, walls))
                .peekLeft(failedToStartServer -> log.error("failed to start server: cause {}", failedToStartServer.getReason()))
                .peekLeft(failedToStartServer -> clientEventHandler.handle(StartingClientError.FAILED_TO_CONNECT_TO_SERVER));
    }

    @Override
    public void startGame() {
        server
                .start(serverParams())
                .peek(serverStarted -> snakeOnlineClient.startGame())
                .peekLeft(failedToStartServer -> log.error("failed to start server: cause {}", failedToStartServer.getReason()))
                .peekLeft(failedToStartServer -> clientEventHandler.handle(StartingClientError.FAILED_TO_CONNECT_TO_SERVER));
    }

    @Override
    public void changeSnakeDirection(Direction direction) {
        server
                .start(serverParams())
                .peek(serverStarted -> snakeOnlineClient.changeSnakeDirection(direction))
                .peekLeft(failedToStartServer -> log.error("failed to start server: cause {}", failedToStartServer.getReason()))
                .peekLeft(failedToStartServer -> clientEventHandler.handle(StartingClientError.FAILED_TO_CONNECT_TO_SERVER));
    }

    @Override
    public void cancelGame() {
        server
                .start(serverParams())
                .peek(serverStarted -> snakeOnlineClient.cancelGame())
                .peekLeft(failedToStartServer -> log.error("failed to start server: cause {}", failedToStartServer.getReason()))
                .peekLeft(failedToStartServer -> clientEventHandler.handle(StartingClientError.FAILED_TO_CONNECT_TO_SERVER));
    }

    @Override
    public void pauseGame() {
        server
                .start(serverParams())
                .peek(serverStarted -> snakeOnlineClient.pauseGame())
                .peekLeft(failedToStartServer -> log.error("failed to start server: cause {}", failedToStartServer.getReason()))
                .peekLeft(failedToStartServer -> clientEventHandler.handle(StartingClientError.FAILED_TO_CONNECT_TO_SERVER));
    }

    @Override
    public void resumeGame() {
        server
                .start(serverParams())
                .peek(serverStarted -> snakeOnlineClient.resumeGame())
                .peekLeft(failedToStartServer -> log.error("failed to start server: cause {}", failedToStartServer.getReason()))
                .peekLeft(failedToStartServer -> clientEventHandler.handle(StartingClientError.FAILED_TO_CONNECT_TO_SERVER));
    }

    @Override
    public void sendChatMessage(String message) {
        server
                .start(serverParams())
                .peek(serverStarted -> snakeOnlineClient.sendChatMessage(message))
                .peekLeft(failedToStartServer -> log.error("failed to start server: cause {}", failedToStartServer.getReason()))
                .peekLeft(failedToStartServer -> clientEventHandler.handle(StartingClientError.FAILED_TO_CONNECT_TO_SERVER));
    }

    @Override
    public void disconnect() {
        server.shutdown();
        snakeOnlineClient.disconnect();
    }

    @Override
    public boolean isConnected() {
        return snakeOnlineClient.isConnected();
    }

    private ServerParams serverParams() {
        return new ServerParams("127.0.0.1", "8080");
    }
}