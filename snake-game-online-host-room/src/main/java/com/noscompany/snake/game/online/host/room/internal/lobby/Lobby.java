package com.noscompany.snake.game.online.host.room.internal.lobby;

import com.noscompany.snake.game.online.contract.messages.game.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.game.dto.GameOptions;
import com.noscompany.snake.game.online.contract.messages.game.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.lobby.LobbyState;
import com.noscompany.snake.game.online.contract.messages.lobby.event.*;
import io.vavr.control.Either;
import io.vavr.control.Option;

public interface Lobby {

    Either<FailedToTakeASeat, PlayerTookASeat> takeASeat(String userName, PlayerNumber playerNumber);

    Either<FailedToFreeUpSeat, PlayerFreedUpASeat> freeUpASeat(String userName);

    Either<FailedToChangeGameOptions, GameOptionsChanged> changeGameOptions(String userName,
                                                                            GameOptions gameOptions);

    Option<FailedToStartGame> startGame(String userName);

    void changeSnakeDirection(String userName, Direction direction);

    void cancelGame(String userName);

    void pauseGame(String userName);

    void resumeGame(String userName);

    LobbyState getLobbyState();

    boolean userIsSitting(String userName);

    boolean userIsAdmin(String userName);
}