package com.noscompany.snake.game.online.host.room;

import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptionsChanged;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToStartGame;
import com.noscompany.snake.game.online.contract.messages.room.*;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToFreeUpSeat;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerFreedUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerTookASeat;
import com.noscompany.snake.game.online.host.room.dto.UserId;
import io.vavr.control.Either;
import io.vavr.control.Option;

public interface Room {
    Either<FailedToEnterRoom, NewUserEnteredRoom> enter(UserId userId, UserName userName);
    Either<FailedToTakeASeat, PlayerTookASeat> takeASeat(UserId userId, PlayerNumber playerNumber);
    Either<FailedToFreeUpSeat, PlayerFreedUpASeat> freeUpASeat(UserId userId);
    Either<FailedToChangeGameOptions, GameOptionsChanged> changeGameOptions(UserId userId, GameOptions gameOptions);
    Option<FailedToStartGame> startGame(UserId userId);
    void changeSnakeDirection(UserId userId, Direction direction);
    void cancelGame(UserId userId);
    void pauseGame(UserId userId);
    void resumeGame(UserId userId);
    Either<FailedToSendChatMessage, UserSentChatMessage> sendChatMessage(UserId userId, String messageContent);
    Option<UserLeftRoom> leave(UserId userId);
    RoomState getState();
    boolean hasUserWithId(UserId userId);
    boolean userIsAdmin(UserId userId);
    boolean userIsSitting(UserId userId);
    boolean isFull();
}