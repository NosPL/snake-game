package com.noscompany.snake.game.online.host.room.message.dispatcher;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.events.*;
import com.noscompany.snake.game.online.contract.messages.lobby.event.*;
import com.noscompany.snake.game.online.contract.messages.room.NewUserEnteredRoom;
import com.noscompany.snake.game.online.contract.messages.room.UserLeftRoom;
import com.noscompany.snake.game.online.host.room.message.dispatcher.ports.RoomEventHandlerForHost;
import com.noscompany.snake.game.online.host.room.message.dispatcher.ports.Server;
import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.HashSet;

import static io.vavr.API.Tuple;

@AllArgsConstructor
class TestRoomEventHandlerForHost implements RoomEventHandlerForHost {
    private final Collection<OnlineMessage> messagesForHost;

    @Override
    public void handle(UserSentChatMessage userSentChatMessage) {
        messagesForHost.add(userSentChatMessage);
    }

    @Override
    public void handle(GameOptionsChanged gameOptionsChanged) {
        messagesForHost.add(gameOptionsChanged);
    }

    @Override
    public void handle(FailedToSendChatMessage event) {
        messagesForHost.add(event);
    }

    @Override
    public void handle(FailedToStartGame event) {
        messagesForHost.add(event);
    }

    @Override
    public void handle(FailedToChangeGameOptions event) {
        messagesForHost.add(event);
    }

    @Override
    public void handle(PlayerFreedUpASeat event) {
        messagesForHost.add(event);
    }

    @Override
    public void handle(FailedToFreeUpSeat event) {
        messagesForHost.add(event);
    }

    @Override
    public void handle(PlayerTookASeat event) {
        messagesForHost.add(event);
    }

    @Override
    public void handle(FailedToTakeASeat event) {
        messagesForHost.add(event);
    }

    @Override
    public void handle(TimeLeftToGameStartHasChanged event) {
        messagesForHost.add(event);
    }

    @Override
    public void handle(GameStarted event) {
        messagesForHost.add(event);
    }

    @Override
    public void handle(GameContinues event) {
        messagesForHost.add(event);
    }

    @Override
    public void handle(GameFinished event) {
        messagesForHost.add(event);
    }

    @Override
    public void handle(GameCancelled event) {
        messagesForHost.add(event);
    }

    @Override
    public void handle(GamePaused event) {
        messagesForHost.add(event);
    }

    @Override
    public void handle(GameResumed event) {
        messagesForHost.add(event);
    }

    @Override
    public void handle(NewUserEnteredRoom event) {
        messagesForHost.add(event);
    }

    @Override
    public void handle(UserLeftRoom event) {
        messagesForHost.add(event);
    }

    @Override
    public void handle(Server.StartError startError) {

    }

    boolean messageWasPassed(OnlineMessage onlineMessage) {
        return messagesForHost.contains(onlineMessage);
    }

    static TestRoomEventHandlerForHost create() {
        return new TestRoomEventHandlerForHost(new HashSet<>());
    }
}