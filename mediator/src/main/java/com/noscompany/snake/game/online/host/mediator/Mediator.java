package com.noscompany.snake.game.online.host.mediator;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.snake.game.online.contract.messages.chat.SendChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.options.ChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.commands.*;
import com.noscompany.snake.game.online.contract.messages.room.EnterRoom;
import com.noscompany.snake.game.online.contract.messages.room.UserLeftRoom;
import com.noscompany.snake.game.online.contract.messages.room.UserName;
import com.noscompany.snake.game.online.contract.messages.seats.FreeUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.TakeASeat;
import com.noscompany.snake.game.online.contract.messages.host.HostGotShutdown;
import com.noscompany.snake.game.online.contract.messages.server.events.RemoteClientDisconnected;
import com.noscompany.snake.game.online.host.room.Room;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
final class Mediator {
    private final Room room;
    private final MessagePublisher messagePublisher;

    void enterRoom(EnterRoom command) {
        room
                .enter(command.getUserId(), new UserName(command.getUserName()))
                .peek(messagePublisher::publishMessage)
                .peekLeft(messagePublisher::publishMessage);
    }

    Option<UserLeftRoom> remoteClientDisconnected(RemoteClientDisconnected event) {
        return room.leave(event.getRemoteClientId());
    }

    void sendChatMessage(SendChatMessage command) {
        room
                .sendChatMessage(command.getUserId(), command.getMessageContent())
                .peek(messagePublisher::publishMessage)
                .peekLeft(messagePublisher::publishMessage);
    }

    void startGame(StartGame command) {
        room
                .startGame(command.getUserId())
                .peek(messagePublisher::publishMessage);
    }

    void changeSnakeDirection(ChangeSnakeDirection command) {
        room.changeSnakeDirection(command.getUserId(), command.getDirection());
    }

    void pauseGame(PauseGame command) {
        room.pauseGame(command.getUserId());
    }

    void resumeGame(ResumeGame command) {
        room.resumeGame(command.getUserId());
    }

    void cancelGame(CancelGame command) {
        room.cancelGame(command.getUserId());
    }

    void changeGameOptions(ChangeGameOptions command) {
        room
                .changeGameOptions(command.getUserId(), command.getOptions())
                .peek(messagePublisher::publishMessage)
                .peekLeft(messagePublisher::publishMessage);
    }

    void takeASeat(TakeASeat command) {
        room
                .takeASeat(command.getUserId(), command.getPlayerNumber())
                .peek(messagePublisher::publishMessage)
                .peekLeft(messagePublisher::publishMessage);
    }

    void freeUpASeat(FreeUpASeat command) {
        room
                .freeUpASeat(command.getUserId())
                .peek(messagePublisher::publishMessage)
                .peekLeft(messagePublisher::publishMessage);
    }

    void terminateRoom(HostGotShutdown command) {
        room.terminate();
    }
}