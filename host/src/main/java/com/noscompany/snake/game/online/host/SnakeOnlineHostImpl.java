package com.noscompany.snake.game.online.host;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.chat.SendChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.options.ChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.commands.*;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.*;
import com.noscompany.snake.game.online.contract.messages.room.EnterRoom;
import com.noscompany.snake.game.online.contract.messages.seats.FreeUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.TakeASeat;
import com.noscompany.snake.game.online.host.server.Server;
import com.noscompany.snake.game.online.host.server.dto.ServerParams;
import com.noscompany.snake.game.online.contract.messages.room.UserName;
import com.noscompany.snake.game.online.host.ports.RoomApiForHost;
import com.noscompany.snake.game.online.host.server.ports.RoomApiForRemoteClients;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class SnakeOnlineHostImpl implements SnakeOnlineHost {
    private final UserId hostId;
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
                    roomApiForHost.enter(new EnterRoom(hostId, userName.getName()));
                });
    }

    @Override
    public void sendChatMessage(String messageContent) {
        if (!server.isRunning())
            hostEventHandler.failedToExecuteActionBecauseServerIsNotRunning();
        roomApiForHost.sendChatMessage(new SendChatMessage(hostId, messageContent));
    }

    @Override
    public void cancelGame() {
        if (!server.isRunning())
            hostEventHandler.failedToExecuteActionBecauseServerIsNotRunning();
        roomApiForHost.cancelGame(new CancelGame(hostId));
    }

    @Override
    public void changeSnakeDirection(Direction direction) {
        if (!server.isRunning())
            hostEventHandler.failedToExecuteActionBecauseServerIsNotRunning();
        roomApiForHost.changeSnakeDirection(new ChangeSnakeDirection(hostId, direction));
    }

    @Override
    public void pauseGame() {
        if (!server.isRunning())
            hostEventHandler.failedToExecuteActionBecauseServerIsNotRunning();
        roomApiForHost.pauseGame(new PauseGame(hostId));
    }

    @Override
    public void resumeGame() {
        if (!server.isRunning())
            hostEventHandler.failedToExecuteActionBecauseServerIsNotRunning();
        roomApiForHost.resumeGame(new ResumeGame(hostId));
    }

    @Override
    public void startGame() {
        if (!server.isRunning())
            hostEventHandler.failedToExecuteActionBecauseServerIsNotRunning();
        roomApiForHost.startGame(new StartGame(hostId));
    }

    @Override
    public void changeGameOptions(GameOptions options) {
        changeGameOptions(options.getGridSize(), options.getGameSpeed(), options.getWalls());
    }

    @Override
    public void changeGameOptions(GridSize gridSize, GameSpeed gameSpeed, Walls walls) {
        if (!server.isRunning())
            hostEventHandler.failedToExecuteActionBecauseServerIsNotRunning();
        roomApiForHost.changeGameOptions(new ChangeGameOptions(hostId, gridSize, gameSpeed, walls));
    }

    @Override
    public void freeUpASeat() {
        if (!server.isRunning())
            hostEventHandler.failedToExecuteActionBecauseServerIsNotRunning();
        roomApiForHost.freeUpASeat(new FreeUpASeat(hostId));
    }

    @Override
    public void takeASeat(PlayerNumber playerNumber) {
        if (!server.isRunning())
            hostEventHandler.failedToExecuteActionBecauseServerIsNotRunning();
        roomApiForHost.takeASeat(new TakeASeat(hostId, playerNumber));
    }

    @Override
    public void shutDownServer() {
        server.shutdown();
        roomApiForHost.shutdown();
    }
}