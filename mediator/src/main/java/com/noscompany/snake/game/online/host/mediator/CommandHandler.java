package com.noscompany.snake.game.online.host.mediator;

import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.room.UserName;
import com.noscompany.snake.game.online.contract.messages.server.InitializeRemoteClientState;
import com.noscompany.snake.game.online.host.room.Room;
import com.noscompany.snake.game.online.host.room.dto.UserId;
import com.noscompany.snake.game.online.host.server.dto.RemoteClientId;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
class CommandHandler implements Mediator {
    private final Room room;
    private final EventDispatcher eventDispatcher;

    @Override
    public void enter(HostId hostId, UserName userName) {
        room
                .enter(toUserId(hostId), userName)
                .peek(eventDispatcher::sendToClientsAndHost)
                .peekLeft(eventDispatcher::sendToHost);
    }

    @Override
    public void sendChatMessage(HostId hostId, String messageContent) {
        room
                .sendChatMessage(toUserId(hostId), messageContent)
                .peek(eventDispatcher::sendToClientsAndHost)
                .peekLeft(eventDispatcher::sendToHost);
    }

    @Override
    public void cancelGame(HostId hostId) {
        room.cancelGame(toUserId(hostId));
    }

    @Override
    public void changeSnakeDirection(HostId hostId, Direction direction) {
        room.changeSnakeDirection(toUserId(hostId), direction);
    }

    @Override
    public void pauseGame(HostId hostId) {
        room.pauseGame(toUserId(hostId));
    }

    @Override
    public void resumeGame(HostId hostId) {
        room.resumeGame(toUserId(hostId));
    }

    @Override
    public void startGame(HostId hostId) {
        room
                .startGame(toUserId(hostId))
                .peek(eventDispatcher::sendToHost);
    }

    @Override
    public void changeGameOptions(HostId hostId, GameOptions gameOptions) {
        room
                .changeGameOptions(toUserId(hostId), gameOptions)
                .peek(eventDispatcher::sendToClientsAndHost)
                .peekLeft(eventDispatcher::sendToHost);
    }

    @Override
    public void freeUpASeat(HostId hostId) {
        room
                .freeUpASeat(toUserId(hostId))
                .peek(eventDispatcher::sendToClientsAndHost)
                .peekLeft(eventDispatcher::sendToHost);
    }

    @Override
    public void takeASeat(HostId hostId, PlayerNumber playerNumber) {
        room
                .takeASeat(toUserId(hostId), playerNumber)
                .peek(eventDispatcher::sendToClientsAndHost)
                .peekLeft(eventDispatcher::sendToHost);
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void sendChatMessage(RemoteClientId remoteClientId, String messageContent) {
        room
                .sendChatMessage(toUserId(remoteClientId), messageContent)
                .peek(eventDispatcher::sendToClientsAndHost)
                .peekLeft(failure -> eventDispatcher.sendToClient(remoteClientId, failure));
    }

    @Override
    public void cancelGame(RemoteClientId remoteClientId) {
        room.cancelGame(toUserId(remoteClientId));
    }

    @Override
    public void changeDirection(RemoteClientId remoteClientId, Direction direction) {
        room.changeSnakeDirection(toUserId(remoteClientId), direction);
    }

    @Override
    public void pauseGame(RemoteClientId remoteClientId) {
        room.pauseGame(toUserId(remoteClientId));
    }

    @Override
    public void resumeGame(RemoteClientId remoteClientId) {
        room.resumeGame(toUserId(remoteClientId));
    }

    @Override
    public void startGame(RemoteClientId remoteClientId) {
        room
                .startGame(toUserId(remoteClientId))
                .peek(failure -> eventDispatcher.sendToClient(remoteClientId, failure));
    }

    @Override
    public void changeGameOptions(RemoteClientId remoteClientId, GameOptions gameOptions) {
        room
                .changeGameOptions(toUserId(remoteClientId), gameOptions)
                .peek(eventDispatcher::sendToClientsAndHost)
                .peekLeft(failure -> eventDispatcher.sendToClient(remoteClientId, failure));
    }

    @Override
    public void freeUpSeat(RemoteClientId remoteClientId) {
        room
                .freeUpASeat(toUserId(remoteClientId))
                .peek(eventDispatcher::sendToClientsAndHost)
                .peekLeft(failure -> eventDispatcher.sendToClient(remoteClientId, failure));
    }

    @Override
    public void takeASeat(RemoteClientId remoteClientId, PlayerNumber playerNumber) {
        room
                .takeASeat(toUserId(remoteClientId), playerNumber)
                .peek(eventDispatcher::sendToClientsAndHost)
                .peekLeft(failure -> eventDispatcher.sendToClient(remoteClientId, failure));
    }

    @Override
    public void enterRoom(RemoteClientId remoteClientId, UserName userName) {
        room
                .enter(toUserId(remoteClientId), userName)
                .peek(eventDispatcher::sendToClientsAndHost)
                .peekLeft(failure -> eventDispatcher.sendToClient(remoteClientId, failure));
    }

    @Override
    public void leaveRoom(RemoteClientId remoteClientId) {
        room
                .leave(toUserId(remoteClientId))
                .peek(eventDispatcher::sendToClientsAndHost);
    }

    @Override
    public void initializeClientState(RemoteClientId remoteClientId) {
        var initializeRemoteClientState = new InitializeRemoteClientState(room.getState());
        eventDispatcher.sendToClient(remoteClientId, initializeRemoteClientState);
    }

    private UserId toUserId(HostId hostId) {
        return new UserId(hostId.getId());
    }

    private UserId toUserId(RemoteClientId remoteClientId) {
        return new UserId(remoteClientId.getId());
    }
}