package com.noscompany.snake.game.online.client.internal.state.connected;

import com.noscompany.snake.game.online.client.ClientEventHandler;
import com.noscompany.snake.game.online.client.SendClientMessageError;
import com.noscompany.snake.game.online.client.StartingClientError;
import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptionsChanged;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.*;
import com.noscompany.snake.game.online.contract.messages.playground.GameReinitialized;
import com.noscompany.snake.game.online.contract.messages.seats.*;
import com.noscompany.snake.game.online.contract.messages.user.registry.FailedToEnterRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.NewUserEnteredRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserLeftRoom;
import com.noscompany.snake.game.online.contract.messages.playground.InitializePlaygroundStateToRemoteClient;
import lombok.AllArgsConstructor;

import java.util.concurrent.atomic.AtomicReference;

@AllArgsConstructor
final class UpdateUserIdHandler implements ClientEventHandler {
    private final AtomicReference<UserId> userIdAtomicReference;
    private final ClientEventHandler clientEventHandler;

    @Override
    public void connectionEstablished() {
        clientEventHandler.connectionEstablished();
    }

    @Override
    public void handle(InitializePlaygroundStateToRemoteClient command) {
        userIdAtomicReference.set(command.getUserId());
        clientEventHandler.handle(command);
    }

    @Override
    public void handle(GameReinitialized event) {
        clientEventHandler.handle(event);
    }

    @Override
    public void handle(SendClientMessageError sendClientMessageError) {
        clientEventHandler.handle(sendClientMessageError);
    }

    @Override
    public void handle(StartingClientError startingClientError) {
        clientEventHandler.handle(startingClientError);
    }

    @Override
    public void connectionClosed() {
        clientEventHandler.connectionClosed();
    }

    @Override
    public void handle(NewUserEnteredRoom event) {
        clientEventHandler.handle(event);
    }

    @Override
    public void handle(FailedToEnterRoom event) {
        clientEventHandler.handle(event);
    }

    @Override
    public void handle(InitializeSeatsToRemoteClient command) {
        userIdAtomicReference.set(command.getUserId());
        clientEventHandler.handle(command);
    }

    @Override
    public void handle(PlayerTookASeat event) {
        clientEventHandler.handle(event);
    }

    @Override
    public void handle(FailedToTakeASeat event) {
        clientEventHandler.handle(event);
    }

    @Override
    public void handle(PlayerFreedUpASeat event) {
        clientEventHandler.handle(event);
    }

    @Override
    public void handle(FailedToFreeUpSeat event) {
        clientEventHandler.handle(event);
    }

    @Override
    public void handle(GameOptionsChanged event) {
        clientEventHandler.handle(event);
    }

    @Override
    public void handle(FailedToChangeGameOptions event) {
        clientEventHandler.handle(event);
    }

    @Override
    public void handle(FailedToStartGame event) {
        clientEventHandler.handle(event);
    }

    @Override
    public void handle(UserSentChatMessage event) {
        clientEventHandler.handle(event);
    }

    @Override
    public void handle(FailedToSendChatMessage event) {
        clientEventHandler.handle(event);
    }

    @Override
    public void handle(UserLeftRoom event) {
        clientEventHandler.handle(event);
    }

    @Override
    public void handle(GameStartCountdown event) {
        clientEventHandler.handle(event);
    }

    @Override
    public void handle(GameStarted event) {
        clientEventHandler.handle(event);
    }

    @Override
    public void handle(SnakesMoved event) {
        clientEventHandler.handle(event);
    }

    @Override
    public void handle(GameFinished event) {
        clientEventHandler.handle(event);
    }

    @Override
    public void handle(GameCancelled event) {
        clientEventHandler.handle(event);
    }

    @Override
    public void handle(FailedToCancelGame event) {
        clientEventHandler.handle(event);
    }

    @Override
    public void handle(GamePaused event) {
        clientEventHandler.handle(event);
    }

    @Override
    public void handle(FailedToPauseGame event) {
        clientEventHandler.handle(event);
    }

    @Override
    public void handle(GameResumed event) {
        clientEventHandler.handle(event);
    }

    @Override
    public void handle(FailedToResumeGame event) {
        clientEventHandler.handle(event);
    }
}