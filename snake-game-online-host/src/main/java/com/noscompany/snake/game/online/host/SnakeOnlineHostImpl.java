package com.noscompany.snake.game.online.host;

import com.noscompany.snake.game.online.contract.messages.game.dto.*;
import com.noscompany.snake.game.online.host.server.Server;
import com.noscompany.snake.game.online.host.server.dto.IpAddress;
import com.noscompany.snake.game.online.host.server.dto.ServerParams;
import com.noscompany.snake.game.online.host.room.mediator.PlayerName;
import com.noscompany.snake.game.online.host.room.mediator.RoomMediatorForHost;
import com.noscompany.snake.game.online.host.room.mediator.RoomMediatorForRemoteClients;
import com.noscompany.snake.game.online.host.room.mediator.dto.HostId;
import com.noscompany.snake.game.online.host.server.dto.ServerStartError;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class SnakeOnlineHostImpl implements SnakeOnlineHost {
    private final HostId hostId;
    private final Server server;
    private final ServerEventHandler serverEventHandler;
    private final RoomMediatorForHost roomMediatorForHost;
    private final RoomMediatorForRemoteClients roomMediatorForRemoteClients;

    @Override
    public void startServer(ServerParams serverParams, PlayerName playerName) {
        Option<ServerStartError> startServerResult = server.start(serverParams, roomMediatorForRemoteClients);
        startServerResult
                .peek(serverEventHandler::handle)
                .onEmpty(() -> {
                    serverEventHandler.serverStarted(serverParams);
                    roomMediatorForHost.enter(hostId, playerName);
                });
    }

    @Override
    public void sendChatMessage(String messageContent) {
        if (!server.isRunning())
            serverEventHandler.failedToExecuteActionBecauseServerIsNotRunning();
        roomMediatorForHost.sendChatMessage(hostId, messageContent);
    }

    @Override
    public void cancelGame() {
        if (!server.isRunning())
            serverEventHandler.failedToExecuteActionBecauseServerIsNotRunning();
        roomMediatorForHost.cancelGame(hostId);
    }

    @Override
    public void changeSnakeDirection(Direction direction) {
        if (!server.isRunning())
            serverEventHandler.failedToExecuteActionBecauseServerIsNotRunning();
        roomMediatorForHost.changeSnakeDirection(hostId, direction);
    }

    @Override
    public void pauseGame() {
        if (!server.isRunning())
            serverEventHandler.failedToExecuteActionBecauseServerIsNotRunning();
        roomMediatorForHost.pauseGame(hostId);
    }

    @Override
    public void resumeGame() {
        if (!server.isRunning())
            serverEventHandler.failedToExecuteActionBecauseServerIsNotRunning();
        roomMediatorForHost.resumeGame(hostId);
    }

    @Override
    public void startGame() {
        if (!server.isRunning())
            serverEventHandler.failedToExecuteActionBecauseServerIsNotRunning();
        roomMediatorForHost.startGame(hostId);
    }

    @Override
    public void changeGameOptions(GameOptions gameOptions) {
        if (!server.isRunning())
            serverEventHandler.failedToExecuteActionBecauseServerIsNotRunning();
        roomMediatorForHost.changeGameOptions(hostId, gameOptions);
    }

    @Override
    public void changeGameOptions(GridSize gridSize, GameSpeed gameSpeed, Walls walls) {
        changeGameOptions(new GameOptions(gridSize, gameSpeed, walls));
    }

    @Override
    public void freeUpASeat() {
        if (!server.isRunning())
            serverEventHandler.failedToExecuteActionBecauseServerIsNotRunning();
        roomMediatorForHost.freeUpASeat(hostId);
    }

    @Override
    public void takeASeat(PlayerNumber playerNumber) {
        if (!server.isRunning())
            serverEventHandler.failedToExecuteActionBecauseServerIsNotRunning();
        roomMediatorForHost.takeASeat(hostId, playerNumber);
    }

    @Override
    public void shutDownServer() {
        server.shutdown();
    }

}