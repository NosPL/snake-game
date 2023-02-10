package com.noscompany.snake.game.online.host.room;

import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptionsChanged;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToStartGame;
import com.noscompany.snake.game.online.contract.messages.room.FailedToEnterRoom;
import com.noscompany.snake.game.online.contract.messages.room.NewUserEnteredRoom;
import com.noscompany.snake.game.online.contract.messages.room.RoomState;
import com.noscompany.snake.game.online.contract.messages.room.UserLeftRoom;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToFreeUpSeat;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerFreedUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerTookASeat;
import io.vavr.control.Either;
import io.vavr.control.Option;

public interface Room {
    Either<FailedToEnterRoom, NewUserEnteredRoom> enter(String userId, String userName);
    Either<FailedToTakeASeat, PlayerTookASeat> takeASeat(String userId, PlayerNumber playerNumber);
    Either<FailedToFreeUpSeat, PlayerFreedUpASeat> freeUpASeat(String userId);
    Either<FailedToChangeGameOptions, GameOptionsChanged> changeGameOptions(String userId, GameOptions gameOptions);
    Option<FailedToStartGame> startGame(String userId);
    void changeSnakeDirection(String userId, Direction direction);
    void cancelGame(String userId);
    void pauseGame(String userId);
    void resumeGame(String userId);
    Either<FailedToSendChatMessage, UserSentChatMessage> sendChatMessage(String userId, String messageContent);
    Option<UserLeftRoom> removeUserById(String userId);
    RoomState getState();
    boolean hasUserWithId(String userId);
    boolean userIsAdmin(String userId);
    boolean userIsSitting(String userId);
    boolean isFull();
}