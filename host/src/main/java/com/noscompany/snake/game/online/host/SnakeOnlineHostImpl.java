package com.noscompany.snake.game.online.host;

import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.*;
import com.noscompany.snake.game.online.host.server.Server;
import com.noscompany.snake.game.online.host.server.dto.ServerParams;
import com.noscompany.snake.game.online.contract.messages.room.UserName;
import com.noscompany.snake.game.online.host.ports.RoomApiForHost;
import com.noscompany.snake.game.online.host.server.ports.RoomApiForRemoteClients;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class SnakeOnlineHostImpl implements SnakeOnlineHost {
    private final RoomApiForHost.HostId hostId;
    private final Server server;
    private final HostEventHandler hostEventHandler;
    private final RoomApiForHost roomApiForHost;
    private final RoomApiForRemoteClients roomApiForRemoteClients;

    @Override
    public void startServer(ServerParams serverParams, UserName userName) {
        server
                .start(serverParams, roomApiForRemoteClients)
                .peek(hostEventHandler::handle)
                .onEmpty(() -> {
                    hostEventHandler.serverStarted(serverParams);
                    roomApiForHost.enter(hostId, userName);
                });
    }

    @Override
    public void sendChatMessage(String messageContent) {
        if (!server.isRunning())
            hostEventHandler.failedToExecuteActionBecauseServerIsNotRunning();
        roomApiForHost.sendChatMessage(hostId, messageContent);
    }

    @Override
    public void cancelGame() {
        if (!server.isRunning())
            hostEventHandler.failedToExecuteActionBecauseServerIsNotRunning();
        roomApiForHost.cancelGame(hostId);
    }

    @Override
    public void changeSnakeDirection(Direction direction) {
        if (!server.isRunning())
            hostEventHandler.failedToExecuteActionBecauseServerIsNotRunning();
        roomApiForHost.changeSnakeDirection(hostId, direction);
    }

    @Override
    public void pauseGame() {
        if (!server.isRunning())
            hostEventHandler.failedToExecuteActionBecauseServerIsNotRunning();
        roomApiForHost.pauseGame(hostId);
    }

    @Override
    public void resumeGame() {
        if (!server.isRunning())
            hostEventHandler.failedToExecuteActionBecauseServerIsNotRunning();
        roomApiForHost.resumeGame(hostId);
    }

    @Override
    public void startGame() {
        if (!server.isRunning())
            hostEventHandler.failedToExecuteActionBecauseServerIsNotRunning();
        roomApiForHost.startGame(hostId);
    }

    @Override
    public void changeGameOptions(GameOptions gameOptions) {
        if (!server.isRunning())
            hostEventHandler.failedToExecuteActionBecauseServerIsNotRunning();
        roomApiForHost.changeGameOptions(hostId, gameOptions);
    }

    @Override
    public void changeGameOptions(GridSize gridSize, GameSpeed gameSpeed, Walls walls) {
        changeGameOptions(new GameOptions(gridSize, gameSpeed, walls));
    }

    @Override
    public void freeUpASeat() {
        if (!server.isRunning())
            hostEventHandler.failedToExecuteActionBecauseServerIsNotRunning();
        roomApiForHost.freeUpASeat(hostId);
    }

    @Override
    public void takeASeat(PlayerNumber playerNumber) {
        if (!server.isRunning())
            hostEventHandler.failedToExecuteActionBecauseServerIsNotRunning();
        roomApiForHost.takeASeat(hostId, playerNumber);
    }

    @Override
    public void shutDownServer() {
        server.shutdown();
        roomApiForHost.shutdown();
    }
}