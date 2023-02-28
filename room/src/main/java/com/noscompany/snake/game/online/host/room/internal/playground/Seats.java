package com.noscompany.snake.game.online.host.room.internal.playground;

import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.playground.PlaygroundState;
import com.noscompany.snake.game.online.contract.messages.room.UserName;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToFreeUpSeat;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat;
import com.noscompany.snake.game.online.host.room.dto.UserId;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static io.vavr.control.Option.ofOptional;
import static java.util.stream.Collectors.toSet;
import static lombok.AccessLevel.PACKAGE;

@AllArgsConstructor(access = PACKAGE)
@Slf4j
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
        getAdminSeat()
                .peek(this::logAdminIsAlreadyChosen)
                .onEmpty(() -> log.info("room has no admin, going to choose one"))
                .onEmpty(this::chooseNewAdmin);

    }

    private void chooseNewAdmin() {
        findAnyTakenPlace()
                .peek(Seat::chooseAsAdmin)
                .onEmpty(() -> log.info("all places are free, no admin has been selected"));
    }

    private void logAdminIsAlreadyChosen(Seat seat) {
        String userId = seat.getUserId().map(UserId::getId).getOrElse("");
        log.info("no need to choose new admin, current admin id {}, player number {}", userId, seat.getPlayerNumber());
    }

    private Option<Seat> getAdminSeat() {
        return ofOptional(seatsStream().filter(Seat::isAdmin).findAny());
    }

    private Option<Seat> findAnyTakenPlace() {
        return ofOptional(seatsStream()
                .filter(Seat::isTaken)
                .findAny());
    }

    private Stream<Seat> seatsStream() {
        return seats
                .values()
                .stream();
    }

    private Option<Seat> findSeatBy(UserId userId) {
        return ofOptional(
                seatsStream()
                        .filter(seat -> seat.isTakenBy(userId))
                        .findFirst());

    }
}