package com.noscompany.snake.game.online.host.room.internal.lobby;

import com.noscompany.snake.game.online.contract.messages.lobby.LobbyState;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToFreeUpSeat;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static lombok.AccessLevel.PACKAGE;

@AllArgsConstructor(access = PACKAGE)
class Seats {
    private final Map<PlayerNumber, Seat> seats;

    Either<FailedToTakeASeat, Seat.UserSuccessfullyTookASeat> takeOrChangeSeat(String userName, PlayerNumber seatNumber) {
        Seat desiredSeat = seats.get(seatNumber);
        return findSeatBy(userName)
                .map(currentUserSeat -> currentUserSeat.changeTo(desiredSeat))
                .getOrElse(desiredSeat.take(userName))
                .peek(userSuccessfullyTookASeat -> chooseAdminIfNeeded());
    }

    Either<FailedToFreeUpSeat, Seat.UserFreedUpASeat> freeUpSeat(String userName) {
        return findSeatBy(userName)
                .flatMap(Seat::freeUp)
                .peek(userFreedUpASeat -> chooseAdminIfNeeded())
                .toEither(FailedToFreeUpSeat.userDidNotTakeASeat());
    }

    Option<PlayerNumber> getNumberFor(String userName) {
        return findSeatBy(userName)
                .map(Seat::getPlayerNumber);
    }

    boolean userIsSitting(String userName) {
        return findSeatBy(userName).exists(Seat::isTaken);
    }

    boolean userIsAdmin(String userName) {
        return findSeatBy(userName).exists(Seat::isAdmin);
    }

    Set<LobbyState.Seat> toDto() {
        return seatsStream()
                .map(Seat::toDto)
                .collect(toSet());
    }

    Set<PlayerNumber> getPlayerNumbers() {
        return seatsStream()
                .filter(Seat::isTaken)
                .map(Seat::getPlayerNumber)
                .collect(toSet());
    }

    private void chooseAdminIfNeeded() {
        if (adminIsNotChosen())
            findAnyTakenPlace()
                    .ifPresent(Seat::chooseAsAdmin);
    }

    private boolean adminIsNotChosen() {
        return seatsStream().noneMatch(Seat::isAdmin);
    }

    private Optional<Seat> findAnyTakenPlace() {
        return seatsStream()
                        .filter(Seat::isTaken)
                        .findAny();
    }

    private Stream<Seat> seatsStream() {
        return seats
                .values()
                .stream();
    }

    private Option<Seat> findSeatBy(String userName) {
        return Option.ofOptional(
                seatsStream()
                        .filter(seat -> seat.isTakenBy(userName))
                        .findFirst());

    }
}