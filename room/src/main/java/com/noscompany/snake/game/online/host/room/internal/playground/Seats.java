package com.noscompany.snake.game.online.host.room.internal.playground;

import com.noscompany.snake.game.online.contract.messages.playground.PlaygroundState;
import com.noscompany.snake.game.online.contract.messages.room.UserName;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToFreeUpSeat;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.host.room.dto.UserId;
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

    Either<FailedToTakeASeat, Seat.UserSuccessfullyTookASeat> takeOrChangeSeat(UserId userId, UserName userName, PlayerNumber seatNumber) {
        Seat desiredSeat = seats.get(seatNumber);
        return findSeatBy(userId)
                .map(currentUserSeat -> currentUserSeat.changeTo(desiredSeat))
                .getOrElse(desiredSeat.take(userId, userName))
                .peek(userSuccessfullyTookASeat -> chooseAdminIfNeeded());
    }

    Either<FailedToFreeUpSeat, Seat.UserFreedUpASeat> freeUpSeat(UserId userId) {
        return findSeatBy(userId)
                .flatMap(Seat::freeUp)
                .peek(userFreedUpASeat -> chooseAdminIfNeeded())
                .toEither(FailedToFreeUpSeat.userDidNotTakeASeat());
    }

    Option<PlayerNumber> getNumberFor(UserId userId) {
        return findSeatBy(userId)
                .map(Seat::getPlayerNumber);
    }

    boolean userIsSitting(UserId userId) {
        return findSeatBy(userId).exists(Seat::isTaken);
    }

    boolean userIsAdmin(UserId userId) {
        return findSeatBy(userId).exists(Seat::isAdmin);
    }

    Set<PlaygroundState.Seat> toDto() {
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

    private Option<Seat> findSeatBy(UserId userId) {
        return Option.ofOptional(
                seatsStream()
                        .filter(seat -> seat.isTakenBy(userId))
                        .findFirst());

    }
}