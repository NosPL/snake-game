package com.noscompany.snake.game.test.client;

import com.noscompany.snake.game.online.client.ClientEventHandler;
import com.noscompany.snake.game.online.client.HostAddress;
import com.noscompany.snake.game.online.client.SnakeOnlineClient;
import com.noscompany.snake.game.online.client.StartingClientError;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.*;
import com.noscompany.snake.game.online.host.mediator.Mediator;
import com.noscompany.snake.game.online.host.server.Server;
import com.noscompany.snake.game.online.host.server.dto.ServerParams;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class SnakeOnlineTestClient implements SnakeOnlineClient {
    private final SnakeOnlineClient snakeOnlineClient;
    private final ClientEventHandler clientEventHandler;
    private final Server server;
    private final Mediator mediator;
    private final Option<HostAddress> hostAddressOption;

    @Override
    public void connect(HostAddress hostAddress) {
        server
                .start(serverParams(), mediator)
                .peek(serverStartError -> clientEventHandler.handle(StartingClientError.FAILED_TO_CONNECT_TO_SERVER))
                .onEmpty(() -> snakeOnlineClient.connect(hostAddress));

    }
    
    @Override
    public void enterTheRoom(String userName) {
        server
                .start(serverParams(), mediator)
                .peek(serverStartError -> clientEventHandler.handle(StartingClientError.FAILED_TO_CONNECT_TO_SERVER))
                .onEmpty(() -> snakeOnlineClient.enterTheRoom(userName));
    }

    @Override
    public void takeASeat(PlayerNumber playerNumber) {
        server
                .start(serverParams(), mediator)
                .peek(serverStartError -> clientEventHandler.handle(StartingClientError.FAILED_TO_CONNECT_TO_SERVER))
                .onEmpty(() -> snakeOnlineClient.takeASeat(playerNumber));
    }

    @Override
    public void freeUpASeat() {
        server
                .start(serverParams(), mediator)
                .peek(serverStartError -> clientEventHandler.handle(StartingClientError.FAILED_TO_CONNECT_TO_SERVER))
                .onEmpty(snakeOnlineClient::freeUpASeat);
    }

    @Override
    public void changeGameOptions(GridSize gridSize, GameSpeed gameSpeed, Walls walls) {
        server
                .start(serverParams(), mediator)
                .peek(serverStartError -> clientEventHandler.handle(StartingClientError.FAILED_TO_CONNECT_TO_SERVER))
                .onEmpty(() -> snakeOnlineClient.changeGameOptions(gridSize, gameSpeed, walls));
    }

    @Override
    public void startGame() {
        server
                .start(serverParams(), mediator)
                .peek(serverStartError -> clientEventHandler.handle(StartingClientError.FAILED_TO_CONNECT_TO_SERVER))
                .onEmpty(snakeOnlineClient::startGame);
    }

    @Override
    public void changeSnakeDirection(Direction direction) {
        server
                .start(serverParams(), mediator)
                .peek(serverStartError -> clientEventHandler.handle(StartingClientError.FAILED_TO_CONNECT_TO_SERVER))
                .onEmpty(() -> snakeOnlineClient.changeSnakeDirection(direction));
    }

    @Override
    public void cancelGame() {
        server
                .start(serverParams(), mediator)
                .peek(serverStartError -> clientEventHandler.handle(StartingClientError.FAILED_TO_CONNECT_TO_SERVER))
                .onEmpty(snakeOnlineClient::cancelGame);
    }

    @Override
    public void pauseGame() {
        server
                .start(serverParams(), mediator)
                .peek(serverStartError -> clientEventHandler.handle(StartingClientError.FAILED_TO_CONNECT_TO_SERVER))
                .onEmpty(snakeOnlineClient::pauseGame);
    }

    @Override
    public void resumeGame() {
        server
                .start(serverParams(), mediator)
                .peek(serverStartError -> clientEventHandler.handle(StartingClientError.FAILED_TO_CONNECT_TO_SERVER))
                .onEmpty(snakeOnlineClient::resumeGame);
    }

    @Override
    public void sendChatMessage(String message) {
        server
                .start(serverParams(), mediator)
                .peek(serverStartError -> clientEventHandler.handle(StartingClientError.FAILED_TO_CONNECT_TO_SERVER))
                .onEmpty(() -> snakeOnlineClient.sendChatMessage(message));
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
        return hostAddressOption
                .map(this::toServerParams)
                .getOrElse(this::defaultServerParams);
    }

    private ServerParams toServerParams(HostAddress hostAddress) {
        String[] splitAddress = hostAddress.getAddress().split(":");
        if (splitAddress.length != 2)
            return defaultServerParams();
        return new ServerParams(splitAddress[0], toPort(splitAddress[1]));
    }

    private int toPort(String port) {
        return Try
                .of(() -> Integer.valueOf(port))
                .getOrElse(0);
    }

    private ServerParams defaultServerParams() {
        return new ServerParams("127.0.0.1", 8080);
    }
}