package com.noscompany.snake.game.online.host.mediator;

import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.room.UserName;
import com.noscompany.snake.game.online.host.server.dto.RemoteClientId;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;

@Slf4j
@AllArgsConstructor
class CommandQueue implements Mediator {
    private final ExecutorService executor;
    private final CommandHandler commandHandler;

    @Override
    public void enter(HostId hostId, UserName userName) {
        log.info("host puts 'enter room' command in queue");
        executor.submit(() -> commandHandler.enter(hostId, userName));
    }

    @Override
    public void sendChatMessage(HostId hostId, String messageContent) {
        log.info("host puts 'send chat message' command in queue");
        executor.submit(() -> commandHandler.sendChatMessage(hostId, messageContent));
    }

    @Override
    public void cancelGame(HostId hostId) {
        log.info("host puts 'cancel game' command in queue");
        executor.submit(() -> commandHandler.cancelGame(hostId));
    }

    @Override
    public void changeSnakeDirection(HostId hostId, Direction direction) {
        log.debug("host puts 'change snake direction' command in queue");
        executor.submit(() -> commandHandler.changeSnakeDirection(hostId, direction));
    }

    @Override
    public void pauseGame(HostId hostId) {
        log.info("host puts 'pause game' command in queue");
        executor.submit(() -> commandHandler.pauseGame(hostId));
    }

    @Override
    public void resumeGame(HostId hostId) {
        log.info("host puts 'resume game' command in queue");
        executor.submit(() -> commandHandler.resumeGame(hostId));
    }

    @Override
    public void startGame(HostId hostId) {
        log.info("host puts 'start game' command in queue");
        executor.submit(() -> commandHandler.startGame(hostId));
    }

    @Override
    public void changeGameOptions(HostId hostId, GameOptions gameOptions) {
        log.info("host puts 'change game options' command in queue");
        executor.submit(() -> commandHandler.changeGameOptions(hostId, gameOptions));
    }

    @Override
    public void freeUpASeat(HostId hostId) {
        log.info("host puts 'free up a seat' command in queue");
        executor.submit(() -> commandHandler.freeUpASeat(hostId));
    }

    @Override
    public void takeASeat(HostId hostId, PlayerNumber playerNumber) {
        log.info("host puts 'take a seat' command in queue");
        executor.submit(() -> commandHandler.takeASeat(hostId, playerNumber));
    }

    @Override
    public void shutdown() {
        log.info("shutting mediator command queue down");
        executor.shutdown();
        commandHandler.shutdown();
    }

    @Override
    public void sendChatMessage(RemoteClientId remoteClientId, String messageContent) {
        log.info("remote client with id {} puts 'send chat message' command in queue", remoteClientId.getId());
        executor.submit(() -> commandHandler.sendChatMessage(remoteClientId, messageContent));
    }

    @Override
    public void cancelGame(RemoteClientId remoteClientId) {
        log.info("remote client with id {} puts 'cancel game' command in queue", remoteClientId.getId());
        executor.submit(() -> commandHandler.cancelGame(remoteClientId));
    }

    @Override
    public void changeDirection(RemoteClientId remoteClientId, Direction direction) {
        log.debug("remote client with id {} puts 'change snake direction' command in queue", remoteClientId.getId());
        executor.submit(() -> commandHandler.changeDirection(remoteClientId, direction));
    }

    @Override
    public void pauseGame(RemoteClientId remoteClientId) {
        log.info("remote client with id {} puts 'pause game' command in queue", remoteClientId.getId());
        executor.submit(() -> commandHandler.pauseGame(remoteClientId));
    }

    @Override
    public void resumeGame(RemoteClientId remoteClientId) {
        log.info("remote client with id {} puts 'resume game' command in queue", remoteClientId.getId());
        executor.submit(() -> commandHandler.resumeGame(remoteClientId));
    }

    @Override
    public void startGame(RemoteClientId remoteClientId) {
        log.info("remote client with id {} puts 'start game' command in queue", remoteClientId.getId());
        executor.submit(() -> commandHandler.startGame(remoteClientId));
    }

    @Override
    public void changeGameOptions(RemoteClientId remoteClientId, GameOptions gameOptions) {
        log.info("remote client with id {} puts 'change game options' command in queue", remoteClientId.getId());
        executor.submit(() -> commandHandler.changeGameOptions(remoteClientId, gameOptions));
    }

    @Override
    public void freeUpSeat(RemoteClientId remoteClientId) {
        log.info("remote client with id {} puts 'free up a seat' command in queue", remoteClientId.getId());
        executor.submit(() -> commandHandler.freeUpSeat(remoteClientId));
    }

    @Override
    public void takeASeat(RemoteClientId remoteClientId, PlayerNumber playerNumber) {
        log.info("remote client with id {} puts 'take a seat' command in queue", remoteClientId.getId());
        executor.submit(() -> commandHandler.takeASeat(remoteClientId, playerNumber));
    }

    @Override
    public void enterRoom(RemoteClientId remoteClientId, UserName userName) {
        log.info("remote client with id {} puts 'enter room' command in queue", remoteClientId.getId());
        executor.submit(() -> commandHandler.enterRoom(remoteClientId, userName));
    }

    @Override
    public void leaveRoom(RemoteClientId remoteClientId) {
        log.info("remote client with id {} puts 'leave room' command in queue", remoteClientId.getId());
        executor.submit(() -> commandHandler.leaveRoom(remoteClientId));
    }

    @Override
    public void initializeClientState(RemoteClientId remoteClientId) {
        log.info("remote client with id {} puts 'initialize client state' command in queue", remoteClientId.getId());
        executor.submit(() -> commandHandler.initializeClientState(remoteClientId));
    }
}