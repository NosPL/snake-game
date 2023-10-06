package com.noscompany.snake.game.online.host.mediator;

import com.noscompany.snake.game.online.contract.messages.chat.SendChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.options.ChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.commands.*;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.room.EnterRoom;
import com.noscompany.snake.game.online.contract.messages.room.UserName;
import com.noscompany.snake.game.online.contract.messages.seats.FreeUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.TakeASeat;
import com.noscompany.snake.game.online.contract.messages.server.InitializeRemoteClientState;
import com.noscompany.snake.game.online.host.room.Room;
import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.host.server.dto.RemoteClientId;
import com.noscompany.snake.game.online.host.server.ports.RoomApiForRemoteClients;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
class CommandHandler implements Mediator  {
    private final Room room;
    private final EventDispatcher eventDispatcher;

    public void enter(EnterRoom command) {
        room
                .enter(command.getUserId(), new UserName(command.getUserName()))
                .peek(eventDispatcher::sendToClientsAndHost)
                .peekLeft(eventDispatcher::sendToHost);
    }

    public void sendChatMessage(SendChatMessage command) {
        room
                .sendChatMessage(command.getUserId(), command.getMessageContent())
                .peek(eventDispatcher::sendToClientsAndHost)
                .peekLeft(eventDispatcher::sendToHost);
    }

    public void cancelGame(CancelGame command) {
        room.cancelGame(command.getUserId());
    }

    public void changeSnakeDirection(ChangeSnakeDirection command) {
        room.changeSnakeDirection(command.getUserId(), command.getDirection());
    }

    public void pauseGame(PauseGame command) {
        room.pauseGame(command.getUserId());
    }

    public void resumeGame(ResumeGame command) {
        room.resumeGame(command.getUserId());
    }

    public void startGame(StartGame command) {
        room
                .startGame(command.getUserId())
                .peek(eventDispatcher::sendToHost);
    }

    public void changeGameOptions(ChangeGameOptions command) {
        room
                .changeGameOptions(command.getUserId(), command.getOptions())
                .peek(eventDispatcher::sendToClientsAndHost)
                .peekLeft(eventDispatcher::sendToHost);
    }

    public void freeUpASeat(FreeUpASeat command) {
        room
                .freeUpASeat(command.getUserId())
                .peek(eventDispatcher::sendToClientsAndHost)
                .peekLeft(eventDispatcher::sendToHost);
    }

    public void takeASeat(TakeASeat command) {
        room
                .takeASeat(command.getUserId(), command.getPlayerNumber())
                .peek(eventDispatcher::sendToClientsAndHost)
                .peekLeft(eventDispatcher::sendToHost);
    }

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
        var userId = new UserId(remoteClientId.getId());
        var initializeRemoteClientState = new InitializeRemoteClientState(userId, room.getState());
        eventDispatcher.sendToClient(remoteClientId, initializeRemoteClientState);
    }

    private UserId toUserId(RemoteClientId remoteClientId) {
        return new UserId(remoteClientId.getId());
    }
}