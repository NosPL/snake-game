package com.noscompany.snake.game.online.host.room.message.dispatcher;

import com.noscompany.snake.game.online.contract.messages.game.dto.*;
import com.noscompany.snake.game.online.contract.messages.server.ServerGotShutdown;
import com.noscompany.snake.game.online.host.room.Room;
import com.noscompany.snake.game.online.host.room.message.dispatcher.dto.HostId;
import com.noscompany.snake.game.online.host.room.message.dispatcher.dto.RemoteClientId;
import com.noscompany.snake.game.online.host.room.message.dispatcher.ports.Server;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
class RoomCommandHandler implements RoomCommandHandlerForHost, RoomCommandHandlerForRemoteClients {
    private final HostId hostId;
    private final Room room;
    private final Server server;
    private final RoomEventDispatcher eventDispatcher;

    @Override
    public void startServer(int port) {
        server
                .start(port, this)
                .peek(eventDispatcher::sendToHost);
    }

    @Override
    public void sendChatMessage(String messageContent) {
        room
                .sendChatMessage(hostId.getId(), messageContent)
                .peek(eventDispatcher::sendToClientsAndHost)
                .peekLeft(eventDispatcher::sendToHost);
    }

    @Override
    public void cancelGame() {
        room.cancelGame(hostId.getId());
    }

    @Override
    public void changeSnakeDirection(Direction direction) {
        room.changeSnakeDirection(hostId.getId(), direction);
    }

    @Override
    public void pauseGame() {
        room.pauseGame(hostId.getId());
    }

    @Override
    public void resumeGame() {
        room.resumeGame(hostId.getId());
    }

    @Override
    public void startGame() {
        room
                .startGame(hostId.getId())
                .peek(eventDispatcher::sendToHost);
    }

    @Override
    public void changeGameOptions(GridSize gridSize, GameSpeed gameSpeed, Walls walls) {
        room
                .changeGameOptions(hostId.getId(), new GameOptions(gridSize, gameSpeed, walls))
                .peek(eventDispatcher::sendToClientsAndHost)
                .peekLeft(eventDispatcher::sendToHost);
    }

    @Override
    public void freeUpASeat() {
        room
                .freeUpASeat(hostId.getId())
                .peek(eventDispatcher::sendToClientsAndHost)
                .peekLeft(eventDispatcher::sendToHost);
    }

    @Override
    public void takeASeat(PlayerNumber playerNumber) {
        room
                .takeASeat(hostId.getId(), playerNumber)
                .peek(eventDispatcher::sendToClientsAndHost)
                .peekLeft(eventDispatcher::sendToHost);
    }

    @Override
    public void shutdown() {
        eventDispatcher.sendToAllRemoteClients(new ServerGotShutdown());
        server.shutdown();
    }

    @Override
    public void sendChatMessage(RemoteClientId remoteClientId, String messageContent) {
        room
                .sendChatMessage(remoteClientId.getId(), messageContent)
                .peek(eventDispatcher::sendToClientsAndHost)
                .peekLeft(failure -> eventDispatcher.sendToClient(remoteClientId, failure));
    }

    @Override
    public void cancelGame(RemoteClientId remoteClientId) {
        room.cancelGame(remoteClientId.getId());
    }

    @Override
    public void changeDirection(RemoteClientId remoteClientId, Direction direction) {
        room.changeSnakeDirection(remoteClientId.getId(), direction);
    }

    @Override
    public void pauseGame(RemoteClientId remoteClientId) {
        room.pauseGame(remoteClientId.getId());
    }

    @Override
    public void resumeGame(RemoteClientId remoteClientId) {
        room.resumeGame(remoteClientId.getId());
    }

    @Override
    public void startGame(RemoteClientId remoteClientId) {
        room
                .startGame(remoteClientId.getId())
                .peek(failure -> eventDispatcher.sendToClient(remoteClientId, failure));
    }

    @Override
    public void changeGameOptions(RemoteClientId remoteClientId, GameOptions gameOptions) {
        room
                .changeGameOptions(remoteClientId.getId(), gameOptions)
                .peek(eventDispatcher::sendToClientsAndHost)
                .peekLeft(failure -> eventDispatcher.sendToClient(remoteClientId, failure));
    }

    @Override
    public void freeUpSeat(RemoteClientId remoteClientId) {
        room
                .freeUpASeat(remoteClientId.getId())
                .peek(eventDispatcher::sendToClientsAndHost)
                .peekLeft(failure -> eventDispatcher.sendToClient(remoteClientId, failure));
    }

    @Override
    public void takeASeat(RemoteClientId remoteClientId, PlayerNumber playerNumber) {
        room
                .takeASeat(remoteClientId.getId(), playerNumber)
                .peek(eventDispatcher::sendToClientsAndHost)
                .peekLeft(failure -> eventDispatcher.sendToClient(remoteClientId, failure));
    }

    @Override
    public void enterRoom(RemoteClientId remoteClientId, String userName) {
        room
                .enter(remoteClientId.getId(), userName)
                .peek(eventDispatcher::sendToClientsAndHost)
                .peekLeft(failure -> eventDispatcher.sendToClient(remoteClientId, failure));
    }

    @Override
    public void removeClient(RemoteClientId remoteClientId) {
        room
                .removeUserById(remoteClientId.getId())
                .peek(eventDispatcher::sendToClientsAndHost);
    }
}