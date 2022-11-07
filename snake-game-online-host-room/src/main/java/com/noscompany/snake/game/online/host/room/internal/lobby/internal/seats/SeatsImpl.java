package com.noscompany.snake.game.online.host.room.internal.lobby.internal.seats;

import com.noscompany.snake.game.online.contract.messages.lobby.LobbyState;
import com.noscompany.snake.game.online.contract.messages.lobby.event.FailedToFreeUpSeat;
import com.noscompany.snake.game.online.contract.messages.lobby.event.FailedToTakeASeat;
import com.noscompany.snake.game.online.contract.messages.game.dto.PlayerNumber;
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
class SeatsImpl implements Seats {
    private final Map<PlayerNumber, Seat> seats;

    @Override
    public Either<FailedToTakeASeat, UserSuccessfullyTookASeat> takeOrChangeSeat(String userName, PlayerNumber seatNumber) {
        Seat desiredSeat = seats.get(seatNumber);
        return findSeatBy(userName)
                .map(currentUserSeat -> currentUserSeat.changeTo(desiredSeat))
                .getOrElse(desiredSeat.take(userName))
                .peek(userSuccessfullyTookASeat -> chooseAdminIfNeeded());
    }

    @Override
    public Either<FailedToFreeUpSeat, UserFreedUpASeat> freeUpSeat(String userName) {
        return findSeatBy(userName)
                .flatMap(Seat::freeUp)
                .peek(userFreedUpASeat -> chooseAdminIfNeeded())
                .toEither(FailedToFreeUpSeat.userDidNotTakeASeat());
    }

    @Override
    public Option<PlayerNumber> getNumberFor(String userName) {
        return findSeatBy(userName)
                .map(Seat::getPlayerNumber);
    }

    @Override
    public boolean userIsSitting(String userName) {
        return findSeatBy(userName).exists(Seat::isTaken);
    }

    @Override
    public boolean userIsAdmin(String userName) {
        return findSeatBy(userName).exists(Seat::isAdmin);
    }

    @Override
    public Set<LobbyState.Seat> toDto() {
        return seatsStream()
                .map(Seat::toDto)
                .collect(toSet());
    }

    @Override
    public Set<PlayerNumber> getPlayerNumbers() {
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