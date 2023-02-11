package com.noscompany.snake.game.online.host;

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

    void handle(SnakesMoved event);

    void handle(GameFinished event);

    void handle(GameCancelled event);

    void handle(GamePaused event);

    void handle(GameResumed event);

    void handle(NewUserEnteredRoom event);

    void handle(UserLeftRoom event);
}