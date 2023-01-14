package com.noscompany.snake.game.online.remote.server;

import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.events.*;
import com.noscompany.snake.game.online.contract.messages.lobby.event.*;
import com.noscompany.snake.game.online.contract.messages.room.FailedToEnterRoom;
import com.noscompany.snake.game.online.contract.messages.room.NewUserEnteredRoom;
import com.noscompany.snake.game.online.contract.messages.room.UserLeftRoom;
import com.noscompany.snake.game.online.host.room.mediator.ports.RoomEventHandlerForHost;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class LogHostEventHandler implements RoomEventHandlerForHost {

    @Override
    public void handle(FailedToEnterRoom failedToEnterRoom) {
        log.info("{}", failedToEnterRoom);
    }

    @Override
    public void handle(UserSentChatMessage userSentChatMessage) {
        log.info("{}", userSentChatMessage);
    }

    @Override
    public void handle(GameOptionsChanged gameOptionsChanged) {
        log.info("{}", gameOptionsChanged);
    }

    @Override
    public void handle(FailedToSendChatMessage event) {
        log.info("{}", event);
    }

    @Override
    public void handle(FailedToStartGame event) {
        log.info("{}", event);
    }

    @Override
    public void handle(FailedToChangeGameOptions event) {
        log.info("{}", event);
    }

    @Override
    public void handle(PlayerFreedUpASeat event) {
        log.info("{}", event);
    }

    @Override
    public void handle(FailedToFreeUpSeat event) {
        log.info("{}", event);
    }

    @Override
    public void handle(PlayerTookASeat event) {
        log.info("{}", event);
    }

    @Override
    public void handle(FailedToTakeASeat event) {
        log.info("{}", event);
    }

    @Override
    public void handle(TimeLeftToGameStartHasChanged event) {
        log.info("{}", event);
    }

    @Override
    public void handle(GameStarted event) {
        log.info("{}", event);
    }

    @Override
    public void handle(GameContinues event) {
    }

    @Override
    public void handle(GameFinished event) {
        log.info("{}", event);
    }

    @Override
    public void handle(GameCancelled event) {
        log.info("{}", event);
    }

    @Override
    public void handle(GamePaused event) {
        log.info("{}", event);
    }

    @Override
    public void handle(GameResumed event) {
        log.info("{}", event);
    }

    @Override
    public void handle(NewUserEnteredRoom event) {
        log.info("{}", event);
    }

    @Override
    public void handle(UserLeftRoom event) {
        log.info("{}", event);
    }
}