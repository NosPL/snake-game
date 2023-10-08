package com.noscompany.snake.game.online.host.room;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptionsChanged;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.*;
import com.noscompany.snake.game.online.contract.messages.playground.PlaygroundState;
import com.noscompany.snake.game.online.contract.messages.playground.SendPlaygroundStateToRemoteClient;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToFreeUpSeat;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerFreedUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerTookASeat;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;
import io.vavr.control.Either;
import io.vavr.control.Option;

public interface Room {
    void newUserEnteredRoom(UserId userId, UserName userName);
    Option<PlayerFreedUpASeat> userLeftRoom(UserId userId);
    Either<FailedToTakeASeat, PlayerTookASeat> takeASeat(UserId userId, PlayerNumber playerNumber);
    Either<FailedToFreeUpSeat, PlayerFreedUpASeat> freeUpASeat(UserId userId);
    Either<FailedToChangeGameOptions, GameOptionsChanged> changeGameOptions(UserId userId, GameOptions gameOptions);
    Option<FailedToStartGame> startGame(UserId userId);
    Option<FailedToChangeSnakeDirection> changeSnakeDirection(UserId userId, Direction direction);
    Option<FailedToCancelGame> cancelGame(UserId userId);
    Option<FailedToPauseGame> pauseGame(UserId userId);
    Option<FailedToResumeGame> resumeGame(UserId userId);
    SendPlaygroundStateToRemoteClient newRemoteClientConnected(UserId remoteClientId);
    PlaygroundState getPlaygroundState();
    boolean containsUserWithId(UserId userId);
    boolean userIsAdmin(UserId userId);
    boolean userIsSitting(UserId userId);
    void terminate();
}