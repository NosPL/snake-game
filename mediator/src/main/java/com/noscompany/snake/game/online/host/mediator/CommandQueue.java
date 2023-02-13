package com.noscompany.snake.game.online.host.mediator;

import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.room.PlayerName;
import com.noscompany.snake.game.online.host.server.dto.RemoteClientId;
import lombok.AllArgsConstructor;

import java.util.concurrent.ExecutorService;

@AllArgsConstructor
class CommandQueue implements Mediator {
    private final ExecutorService executor;
    private final CommandHandler commandHandler;

    @Override
    public void enter(HostId hostId, PlayerName playerName) {
        executor.submit(() -> commandHandler.enter(hostId, playerName));
    }

    @Override
    public void sendChatMessage(HostId hostId, String messageContent) {
        executor.submit(() -> commandHandler.sendChatMessage(hostId, messageContent));
    }

    @Override
    public void cancelGame(HostId hostId) {
        executor.submit(() -> commandHandler.cancelGame(hostId));
    }

    @Override
    public void changeSnakeDirection(HostId hostId, Direction direction) {
        executor.submit(() -> commandHandler.changeSnakeDirection(hostId, direction));
    }

    @Override
    public void pauseGame(HostId hostId) {
        executor.submit(() -> commandHandler.pauseGame(hostId));
    }

    @Override
    public void resumeGame(HostId hostId) {
        executor.submit(() -> commandHandler.resumeGame(hostId));
    }

    @Override
    public void startGame(HostId hostId) {
        executor.submit(() -> commandHandler.startGame(hostId));
    }

    @Override
    public void changeGameOptions(HostId hostId, GameOptions gameOptions) {
        executor.submit(() -> commandHandler.changeGameOptions(hostId, gameOptions));
    }

    @Override
    public void freeUpASeat(HostId hostId) {
        executor.submit(() -> commandHandler.freeUpASeat(hostId));
    }

    @Override
    public void takeASeat(HostId hostId, PlayerNumber playerNumber) {
        executor.submit(() -> commandHandler.takeASeat(hostId, playerNumber));
    }

    @Override
    public void sendChatMessage(RemoteClientId remoteClientId, String messageContent) {
        executor.submit(() -> commandHandler.sendChatMessage(remoteClientId, messageContent));
    }

    @Override
    public void cancelGame(RemoteClientId remoteClientId) {
        executor.submit(() -> commandHandler.cancelGame(remoteClientId));
    }

    @Override
    public void changeDirection(RemoteClientId remoteClientId, Direction direction) {
        executor.submit(() -> commandHandler.changeDirection(remoteClientId, direction));
    }

    @Override
    public void pauseGame(RemoteClientId remoteClientId) {
        executor.submit(() -> commandHandler.pauseGame(remoteClientId));
    }

    @Override
    public void resumeGame(RemoteClientId remoteClientId) {
        executor.submit(() -> commandHandler.resumeGame(remoteClientId));
    }

    @Override
    public void startGame(RemoteClientId remoteClientId) {
        executor.submit(() -> commandHandler.startGame(remoteClientId));
    }

    @Override
    public void changeGameOptions(RemoteClientId remoteClientId, GameOptions gameOptions) {
        executor.submit(() -> commandHandler.changeGameOptions(remoteClientId, gameOptions));
    }

    @Override
    public void freeUpSeat(RemoteClientId remoteClientId) {
        executor.submit(() -> commandHandler.freeUpSeat(remoteClientId));
    }

    @Override
    public void takeASeat(RemoteClientId remoteClientId, PlayerNumber playerNumber) {
        executor.submit(() -> commandHandler.takeASeat(remoteClientId, playerNumber));
    }

    @Override
    public void enterRoom(RemoteClientId remoteClientId, PlayerName playerName) {
        executor.submit(() -> commandHandler.enterRoom(remoteClientId, playerName));
    }

    @Override
    public void leaveRoom(RemoteClientId remoteClientId) {
        executor.submit(() -> commandHandler.leaveRoom(remoteClientId));
    }

    @Override
    public void initializeClientState(RemoteClientId remoteClientId) {
        executor.submit(() -> commandHandler.initializeClientState(remoteClientId));
    }
}