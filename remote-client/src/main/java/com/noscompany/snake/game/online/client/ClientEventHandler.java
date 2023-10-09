package com.noscompany.snake.game.online.client;

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

public interface ClientEventHandler {

    void connectionEstablished();

    void handle(InitializePlaygroundStateToRemoteClient initializePlaygroundStateToRemoteClient);

    void handle(GameReinitialized gameReinitialized);

    void handle(SendClientMessageError sendClientMessageError);

    void handle(StartingClientError startingClientError);

    void connectionClosed();

    void handle(NewUserEnteredRoom event);

    void handle(FailedToEnterRoom event);

    void handle(InitializeSeatsToRemoteClient initializePlaygroundStateToRemoteClient);

    void handle(PlayerTookASeat event);

    void handle(FailedToTakeASeat event);

    void handle(PlayerFreedUpASeat event);

    void handle(FailedToFreeUpSeat event);

    void handle(GameOptionsChanged event);

    void handle(FailedToChangeGameOptions event);

    void handle(FailedToStartGame event);

    void handle(UserSentChatMessage event);

    void handle(FailedToSendChatMessage event);

    void handle(UserLeftRoom event);

    void handle(GameStartCountdown event);

    void handle(GameStarted event);

    void handle(SnakesMoved event);

    void handle(GameFinished event);

    void handle(GameCancelled event);

    void handle(GamePaused event);

    void handle(GameResumed event);
}