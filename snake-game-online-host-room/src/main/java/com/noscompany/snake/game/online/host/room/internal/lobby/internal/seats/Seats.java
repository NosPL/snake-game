package com.noscompany.snake.game.online.host.room.internal.lobby.internal.seats;

import com.noscompany.snake.game.online.contract.messages.lobby.LobbyState;
import com.noscompany.snake.game.online.contract.messages.lobby.event.FailedToFreeUpSeat;
import com.noscompany.snake.game.online.contract.messages.lobby.event.FailedToTakeASeat;
import com.noscompany.snake.game.online.contract.messages.game.dto.PlayerNumber;
import io.vavr.control.Either;
import io.vavr.control.Option;

import java.util.Set;

public interface Seats {

    Either<FailedToTakeASeat, UserSuccessfullyTookASeat> takeOrChangeSeat(String userName, PlayerNumber playerNumber);

    Either<FailedToFreeUpSeat, UserFreedUpASeat> freeUpSeat(String userName);

    Option<PlayerNumber> getNumberFor(String userName);

    boolean userIsSitting(String userName);

    boolean userIsAdmin(String userName);

    Set<LobbyState.Seat> toDto();

    Set<PlayerNumber> getPlayerNumbers();
}