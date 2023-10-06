package com.noscompany.snake.game.online.host;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptionsChanged;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.*;
import com.noscompany.snake.game.online.contract.messages.room.FailedToEnterRoom;
import com.noscompany.snake.game.online.contract.messages.room.NewUserEnteredRoom;
import com.noscompany.snake.game.online.contract.messages.room.UserLeftRoom;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToFreeUpSeat;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerFreedUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerTookASeat;
import com.noscompany.snake.game.online.host.server.dto.ServerParams;
import com.noscompany.snake.game.online.host.server.dto.ServerStartError;
import lombok.AllArgsConstructor;

@AllArgsConstructor
final class CheckIfHostIsRecipientOfFailureMessage implements HostEventHandler {
    private final UserId hostId;
    private final HostEventHandler hostEventHandler;

    @Override
    public void handle(ServerStartError serverStartError) {
        hostEventHandler.handle(serverStartError);
    }

    @Override
    public void failedToExecuteActionBecauseServerIsNotRunning() {
        hostEventHandler.failedToExecuteActionBecauseServerIsNotRunning();
    }

    @Override
    public void serverStarted(ServerParams serverParams) {
        hostEventHandler.serverStarted(serverParams);
    }

    @Override
    public void handle(FailedToEnterRoom event) {
        if (event.getUserId().equals(hostId))
            hostEventHandler.handle(event);
    }

    @Override
    public void handle(UserSentChatMessage event) {
        hostEventHandler.handle(event);
    }

    @Override
    public void handle(GameOptionsChanged event) {
        hostEventHandler.handle(event);
    }

    @Override
    public void handle(FailedToSendChatMessage event) {
        if (event.getUserId().equals(hostId))
            hostEventHandler.handle(event);
    }

    @Override
    public void handle(FailedToStartGame event) {
        if (event.getUserId().equals(hostId))
            hostEventHandler.handle(event);
    }

    @Override
    public void handle(FailedToChangeGameOptions event) {
        if (event.getUserId().equals(hostId))
            hostEventHandler.handle(event);
    }

    @Override
    public void handle(PlayerFreedUpASeat event) {
        hostEventHandler.handle(event);
    }

    @Override
    public void handle(FailedToFreeUpSeat event) {
        if (event.getUserId().equals(hostId))
            hostEventHandler.handle(event);
    }

    @Override
    public void handle(PlayerTookASeat event) {
        hostEventHandler.handle(event);
    }

    @Override
    public void handle(FailedToTakeASeat event) {
        hostEventHandler.handle(event);
    }

    @Override
    public void handle(GameStartCountdown event) {
        hostEventHandler.handle(event);
    }

    @Override
    public void handle(GameStarted event) {
        hostEventHandler.handle(event);
    }

    @Override
    public void handle(SnakesMoved event) {
        hostEventHandler.handle(event);
    }

    @Override
    public void handle(GameFinished event) {
        hostEventHandler.handle(event);
    }

    @Override
    public void handle(GameCancelled event) {
        hostEventHandler.handle(event);
    }

    @Override
    public void handle(GamePaused event) {

    }

    @Override
    public void handle(GameResumed event) {

    }

    @Override
    public void handle(NewUserEnteredRoom event) {

    }

    @Override
    public void handle(UserLeftRoom event) {

    }
}