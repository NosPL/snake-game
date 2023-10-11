package com.noscompany.snake.game.online.client;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptionsChanged;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.*;
import com.noscompany.snake.game.online.contract.messages.playground.GameReinitialized;
import com.noscompany.snake.game.online.contract.messages.playground.InitializePlaygroundToRemoteClient;
import com.noscompany.snake.game.online.contract.messages.seats.*;
import com.noscompany.snake.game.online.contract.messages.server.events.ServerGotShutdown;
import com.noscompany.snake.game.online.contract.messages.user.registry.FailedToEnterRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.NewUserEnteredRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserLeftRoom;
import lombok.AllArgsConstructor;

import java.util.concurrent.atomic.AtomicReference;

@AllArgsConstructor
final class UpdateUserIdHandler implements ClientEventHandler {
    private final AtomicReference<UserId> userIdAtomicReference;
    private final ClientEventHandler clientEventHandler;

    @Override
    public void initializePlayground(InitializePlaygroundToRemoteClient command) {
        userIdAtomicReference.set(command.getUserId());
        clientEventHandler.initializePlayground(command);
    }

    @Override
    public void gameReinitialized(GameReinitialized event) {
        clientEventHandler.gameReinitialized(event);
    }

    @Override
    public void connectionEstablished(ConnectionEstablished event) {
        clientEventHandler.connectionEstablished(event);
    }

    @Override
    public void sendClientMessage(SendClientMessageError sendClientMessageError) {
        clientEventHandler.sendClientMessage(sendClientMessageError);
    }

    @Override
    public void startingClientError(StartingClientError startingClientError) {
        clientEventHandler.startingClientError(startingClientError);
    }

    @Override
    public void connectionClosed(ConnectionClosed event) {
        clientEventHandler.connectionClosed(event);
    }

    @Override
    public void newUserEnteredRoom(NewUserEnteredRoom event) {
        clientEventHandler.newUserEnteredRoom(event);
    }

    @Override
    public void failedToEnterRoom(FailedToEnterRoom event) {
        clientEventHandler.failedToEnterRoom(event);
    }

    @Override
    public void initializeSeats(InitializeSeatsToRemoteClient command) {
        userIdAtomicReference.set(command.getUserId());
        clientEventHandler.initializeSeats(command);
    }

    @Override
    public void playerTookASeat(PlayerTookASeat event) {
        clientEventHandler.playerTookASeat(event);
    }

    @Override
    public void failedToTakeASeat(FailedToTakeASeat event) {
        clientEventHandler.failedToTakeASeat(event);
    }

    @Override
    public void playerFreedUpASeat(PlayerFreedUpASeat event) {
        clientEventHandler.playerFreedUpASeat(event);
    }

    @Override
    public void failedToFreeUpASeat(FailedToFreeUpSeat event) {
        clientEventHandler.failedToFreeUpASeat(event);
    }

    @Override
    public void gameOptionsChanged(GameOptionsChanged event) {
        clientEventHandler.gameOptionsChanged(event);
    }

    @Override
    public void failedToChangeGameOptions(FailedToChangeGameOptions event) {
        clientEventHandler.failedToChangeGameOptions(event);
    }

    @Override
    public void failedToStartGame(FailedToStartGame event) {
        clientEventHandler.failedToStartGame(event);
    }

    @Override
    public void userSentChatMessage(UserSentChatMessage event) {
        clientEventHandler.userSentChatMessage(event);
    }

    @Override
    public void failedToSendChatMessage(FailedToSendChatMessage event) {
        clientEventHandler.failedToSendChatMessage(event);
    }

    @Override
    public void userLeftRoom(UserLeftRoom event) {
        clientEventHandler.userLeftRoom(event);
    }

    @Override
    public void gameStartCountdown(GameStartCountdown event) {
        clientEventHandler.gameStartCountdown(event);
    }

    @Override
    public void gameStarted(GameStarted event) {
        clientEventHandler.gameStarted(event);
    }

    @Override
    public void snakesMoved(SnakesMoved event) {
        clientEventHandler.snakesMoved(event);
    }

    @Override
    public void gameFinished(GameFinished event) {
        clientEventHandler.gameFinished(event);
    }

    @Override
    public void gameCancelled(GameCancelled event) {
        clientEventHandler.gameCancelled(event);
    }

    @Override
    public void failedToCancelGame(FailedToCancelGame event) {
        clientEventHandler.failedToCancelGame(event);
    }

    @Override
    public void gamePaused(GamePaused event) {
        clientEventHandler.gamePaused(event);
    }

    @Override
    public void failedToPauseGame(FailedToPauseGame event) {
        clientEventHandler.failedToPauseGame(event);
    }

    @Override
    public void gameResumed(GameResumed event) {
        clientEventHandler.gameResumed(event);
    }

    @Override
    public void failedToResumeGame(FailedToResumeGame event) {
        clientEventHandler.failedToResumeGame(event);
    }

    @Override
    public void serverGotShutdown(ServerGotShutdown event) {
        clientEventHandler.serverGotShutdown(event);
    }
}