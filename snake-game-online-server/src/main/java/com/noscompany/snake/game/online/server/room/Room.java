package com.noscompany.snake.game.online.server.room;

import com.noscompany.snake.game.commons.messages.events.chat.ChatMessageReceived;
import com.noscompany.snake.game.commons.messages.events.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.commons.messages.events.lobby.*;
import com.noscompany.snake.game.commons.messages.events.room.FailedToEnterRoom;
import com.noscompany.snake.game.commons.messages.events.room.NewUserEnteredRoom;
import com.noscompany.snake.game.commons.messages.events.room.UserLeftRoom;
import io.vavr.control.Either;
import io.vavr.control.Option;
import snake.game.core.dto.*;

public interface Room {

    Either<FailedToEnterRoom, NewUserEnteredRoom> enter(String userId, String userName);

    Either<FailedToTakeASeat, PlayerTookASeat> takeASeat(String userId, SnakeNumber snakeNumberNumber);

    Either<FailedToFreeUpSeat, PlayerFreedUpASeat> freeUpASeat(String userId);

    Either<FailedToChangeGameOptions, GameOptionsChanged> changeGameOptions(String userId,
                                                                            GridSize gridSize,
                                                                            GameSpeed gameSpeed,
                                                                            Walls walls);

    Option<FailedToStartGame> startGame(String userId);

    void changeSnakeDirection(String userId, Direction direction);

    void cancelGame(String userId);

    void pauseGame(String userId);

    void resumeGame(String userId);

    Either<FailedToSendChatMessage, ChatMessageReceived> sendChatMessage(String userId, String message);

    Option<UserLeftRoom> removeUser(String userId);
}