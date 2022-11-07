package com.noscompany.snake.game.online.client;

import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.events.*;
import com.noscompany.snake.game.online.contract.messages.lobby.event.*;
import com.noscompany.snake.game.online.contract.messages.room.FailedToConnectToRoom;
import com.noscompany.snake.game.online.contract.messages.room.FailedToEnterRoom;
import com.noscompany.snake.game.online.contract.messages.room.NewUserEnteredRoom;
import com.noscompany.snake.game.online.contract.messages.room.UserLeftRoom;

public interface ClientEventHandler {

    void connectionEstablished();

    void handle(ClientError clientError);

    void handle(StartingClientError startingClientError);

    void connectionClosed();

    void handle(FailedToConnectToRoom event);

    void handle(NewUserEnteredRoom event);

    void handle(FailedToEnterRoom event);

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

    void handle(TimeLeftToGameStartHasChanged event);

    void handle(GameStarted event);

    void handle(GameContinues event);

    void handle(GameFinished event);

    void handle(GameCancelled event);

    void handle(GamePaused event);

    void handle(GameResumed event);
}