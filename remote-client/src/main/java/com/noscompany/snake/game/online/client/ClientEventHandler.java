package com.noscompany.snake.game.online.client;

import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptionsChanged;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.*;
import com.noscompany.snake.game.online.contract.messages.playground.GameReinitialized;
import com.noscompany.snake.game.online.contract.messages.playground.InitializeGame;
import com.noscompany.snake.game.online.contract.messages.seats.*;
import com.noscompany.snake.game.online.contract.messages.server.events.ServerGotShutdown;
import com.noscompany.snake.game.online.contract.messages.user.registry.FailedToEnterRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.NewUserEnteredRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserLeftRoom;

public interface ClientEventHandler {

    void connectionEstablished(ConnectionEstablished event);

    void sendClientMessage(SendClientMessageError sendClientMessageError);

    void startingClientError(StartingClientError startingClientError);

    void connectionClosed(ConnectionClosed event);

    void newUserEnteredRoom(NewUserEnteredRoom event);

    void failedToEnterRoom(FailedToEnterRoom event);

    void userLeftRoom(UserLeftRoom event);

    void initializeSeats(InitializeSeats event);

    void playerTookASeat(PlayerTookASeat event);

    void failedToTakeASeat(FailedToTakeASeat event);

    void playerFreedUpASeat(PlayerFreedUpASeat event);

    void failedToFreeUpASeat(FailedToFreeUpSeat event);

    void initializePlayground(InitializeGame event);

    void gameReinitialized(GameReinitialized event);

    void gameOptionsChanged(GameOptionsChanged event);

    void failedToChangeGameOptions(FailedToChangeGameOptions event);

    void userSentChatMessage(UserSentChatMessage event);

    void failedToSendChatMessage(FailedToSendChatMessage event);

    void failedToStartGame(FailedToStartGame event);

    void gameStartCountdown(GameStartCountdown event);

    void gameStarted(GameStarted event);

    void snakesMoved(SnakesMoved event);

    void gameFinished(GameFinished event);

    void gameCancelled(GameCancelled event);

    void failedToCancelGame(FailedToCancelGame event);

    void gamePaused(GamePaused event);

    void failedToPauseGame(FailedToPauseGame event);

    void gameResumed(GameResumed event);

    void failedToResumeGame(FailedToResumeGame event);

    void serverGotShutdown(ServerGotShutdown event);
}