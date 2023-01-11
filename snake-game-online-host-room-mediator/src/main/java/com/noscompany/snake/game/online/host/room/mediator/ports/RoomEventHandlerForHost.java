package com.noscompany.snake.game.online.host.room.mediator.ports;

import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.events.*;
import com.noscompany.snake.game.online.contract.messages.lobby.event.*;
import com.noscompany.snake.game.online.contract.messages.room.FailedToEnterRoom;
import com.noscompany.snake.game.online.contract.messages.room.NewUserEnteredRoom;
import com.noscompany.snake.game.online.contract.messages.room.UserLeftRoom;

public interface RoomEventHandlerForHost {

    void handle(FailedToEnterRoom failedToEnterRoom);

    void handle(UserSentChatMessage userSentChatMessage);

    void handle(GameOptionsChanged gameOptionsChanged);

    void handle(FailedToSendChatMessage event);

    void handle(FailedToStartGame event);

    void handle(FailedToChangeGameOptions event);

    void handle(PlayerFreedUpASeat event);

    void handle(FailedToFreeUpSeat event);

    void handle(PlayerTookASeat event);

    void handle(FailedToTakeASeat event);

    void handle(TimeLeftToGameStartHasChanged event);

    void handle(GameStarted event);

    void handle(GameContinues event);

    void handle(GameFinished event);

    void handle(GameCancelled event);

    void handle(GamePaused event);

    void handle(GameResumed event);

    void handle(NewUserEnteredRoom event);

    void handle(UserLeftRoom event);
}