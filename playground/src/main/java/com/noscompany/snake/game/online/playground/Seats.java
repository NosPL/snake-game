package com.noscompany.snake.game.online.playground;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.playground.PlaygroundState;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToFreeUpSeat;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;
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
                .map(currentUserSeat -> currentUserSeat.changeTo(userId, desiredSeat))
                .getOrElse(desiredSeat.take(userId, userName))
                .peek(userSuccessfullyTookASeat -> selectAdminIfThereIsNone());
    }

    Either<FailedToFreeUpSeat, Seat.UserFreedUpASeat> freeUpSeat(UserId userId) {
        return findSeatBy(userId)
                .flatMap(Seat::freeUp)
                .peek(userFreedUpASeat -> selectAdminIfThereIsNone())
                .toEither(FailedToFreeUpSeat.userDidNotTakeASeat(userId));
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

    Set<PlayerNumber> getTakenSeatsNumbers() {
        return seatsStream()
                .filter(Seat::isTaken)
                .map(Seat::getPlayerNumber)
                .collect(toSet());
    }

    private void selectAdminIfThereIsNone() {
        if (adminIsSelected()) {
            log.info("admin is already selected, no need to choose one");
            return;
        }
        findAnyTakenPlace().peek(Seat::chooseAsAdmin);
    }

    private boolean adminIsSelected() {
        return seatsStream().anyMatch(Seat::isAdmin);
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