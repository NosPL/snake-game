package com.noscompany.snake.game.online.host.room.mediator;

import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.room.PlayerName;
import com.noscompany.snake.game.online.contract.messages.room.RoomState;
import com.noscompany.snake.game.online.host.ports.RoomApiForHost;
import com.noscompany.snake.game.online.host.room.Room;
import com.noscompany.snake.game.online.host.server.ports.RoomApiForRemoteClients;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class RoomMediator implements RoomApiForHost, RoomApiForRemoteClients {
    private final Room room;
    private final EventDispatcher eventDispatcher;

    @Override
    public void enter(HostId hostId, PlayerName playerName) {
        room
                .enter(hostId.getId(), playerName.getName())
                .peek(eventDispatcher::sendToClientsAndHost)
                .peekLeft(eventDispatcher::sendToHost);
    }

    @Override
    public void sendChatMessage(HostId hostId, String messageContent) {
        room
                .sendChatMessage(hostId.getId(), messageContent)
                .peek(eventDispatcher::sendToClientsAndHost)
                .peekLeft(eventDispatcher::sendToHost);
    }

    @Override
    public void cancelGame(HostId hostId) {
        room.cancelGame(hostId.getId());
    }

    @Override
    public void changeSnakeDirection(HostId hostId, Direction direction) {
        room.changeSnakeDirection(hostId.getId(), direction);
    }

    @Override
    public void pauseGame(HostId hostId) {
        room.pauseGame(hostId.getId());
    }

    @Override
    public void resumeGame(HostId hostId) {
        room.resumeGame(hostId.getId());
    }

    @Override
    public void startGame(HostId hostId) {
        room
                .startGame(hostId.getId())
                .peek(eventDispatcher::sendToHost);
    }

    @Override
    public void changeGameOptions(HostId hostId, GameOptions gameOptions) {
        room
                .changeGameOptions(hostId.getId(), gameOptions)
                .peek(eventDispatcher::sendToClientsAndHost)
                .peekLeft(eventDispatcher::sendToHost);
    }

    @Override
    public void freeUpASeat(HostId hostId) {
        room
                .freeUpASeat(hostId.getId())
                .peek(eventDispatcher::sendToClientsAndHost)
                .peekLeft(eventDispatcher::sendToHost);
    }

    @Override
    public void takeASeat(HostId hostId, PlayerNumber playerNumber) {
        room
                .takeASeat(hostId.getId(), playerNumber)
                .peek(eventDispatcher::sendToClientsAndHost)
                .peekLeft(eventDispatcher::sendToHost);
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
    public void enterRoom(RemoteClientId remoteClientId, PlayerName playerName) {
        room
                .enter(remoteClientId.getId(), playerName.getName())
                .peek(eventDispatcher::sendToClientsAndHost)
                .peekLeft(failure -> eventDispatcher.sendToClient(remoteClientId, failure));
    }

    @Override
    public void removeClient(RemoteClientId remoteClientId) {
        room
                .removeUserById(remoteClientId.getId())
                .peek(eventDispatcher::sendToClientsAndHost);
    }

    @Override
    public RoomState getRoomState() {
        return room.getState();
    }
}