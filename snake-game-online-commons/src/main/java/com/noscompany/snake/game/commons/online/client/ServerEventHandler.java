package com.noscompany.snake.game.commons.online.client;

import com.noscompany.snake.game.commons.messages.events.chat.UserSentChatMessage;
import com.noscompany.snake.game.commons.messages.events.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.commons.messages.events.lobby.*;
import com.noscompany.snake.game.commons.messages.events.room.FailedToEnterRoom;
import com.noscompany.snake.game.commons.messages.events.room.NewUserEnteredRoom;
import com.noscompany.snake.game.commons.messages.events.room.UserLeftRoom;
import snake.game.core.SnakeGameEventHandler;

public interface ServerEventHandler extends SnakeGameEventHandler {

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
}