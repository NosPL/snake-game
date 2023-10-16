package com.noscompany.snake.game.online.seats;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.seats.*;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;
import com.noscompany.snake.game.online.seats.Seat.UserSuccessfullyTookASeat;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import static io.vavr.control.Option.ofOptional;
import static java.util.stream.Collectors.toSet;
import static lombok.AccessLevel.PACKAGE;

@AllArgsConstructor(access = PACKAGE)
@Slf4j
public final class Seats {
    private final Map<UserId, UserName> userRegistry;
    private final Map<PlayerNumber, Seat> seats;
    private Option<AdminId> adminIdOption;
    private final AtomicBoolean gameIsRunning;

    public Option<AdminId> getAdminId() {
        return adminIdOption;
    }

    public InitializeSeats newUserEnteredRoom(UserId userId, UserName userName) {
        userRegistry.put(userId, userName);
        return new InitializeSeats(userId, adminIdOption, toDto());
    }

    public Option<PlayerFreedUpASeat> userLeftRoom(UserId userId) {
        return freeUpSeat(userId)
                .peekLeft(success -> userRegistry.remove(userId))
                .toOption();
    }

    public Either<FailedToTakeASeat, PlayerTookASeat> takeOrChangeSeat(UserId userId, PlayerNumber seatNumber) {
        if (gameIsRunning.get())
            return Either.left(FailedToTakeASeat.gameAlreadyRunning(userId));
        var desiredSeat = seats.get(seatNumber);
        return changeSeat(userId, desiredSeat)
                .getOrElse(takeASeat(userId, desiredSeat))
                .peek(success -> selectAdminIfThereIsNone())
                .map(this::toResult);
    }

    private Option<Either<FailedToTakeASeat, UserSuccessfullyTookASeat>> changeSeat(UserId userId, Seat desiredSeat) {
        return findSeatBy(userId)
                .map(currentUserSeat -> currentUserSeat.changeTo(userId, desiredSeat));
    }

    private Either<FailedToTakeASeat, UserSuccessfullyTookASeat> takeASeat(UserId userId, Seat desiredSeat) {
        return findUserName(userId)
                .toEither(FailedToTakeASeat.userNotInTheRoom(userId))
                .flatMap(userName -> desiredSeat.take(userId, userName));
    }

    private PlayerTookASeat toResult(UserSuccessfullyTookASeat result) {
        return new PlayerTookASeat(result.getUserId(), result.getUserName(), result.getPlayerNumber(), adminIdOption.get(), toDto());
    }

    private Option<UserName> findUserName(UserId userId) {
        return Option
                .of(userRegistry.get(userId));
    }

    public Either<FailedToFreeUpSeat, PlayerFreedUpASeat> freeUpSeat(UserId userId) {
        if (!userRegistry.containsKey(userId))
            return Either.left(FailedToFreeUpSeat.userNotInTheRoom(userId));
        return findSeatBy(userId)
                .flatMap(Seat::freeUp)
                .peek(this::updateAdmin)
                .toEither(FailedToFreeUpSeat.userDidNotTakeASeat(userId))
                .map(userFreedUpASeat -> toResult(userId, userFreedUpASeat));
    }

    private void updateAdmin(Seat.UserFreedUpASeat userFreedUpASeat) {
        if (userIsAdmin(userFreedUpASeat.getUserId()))
            selectNewAdmin();
    }

    private PlayerFreedUpASeat toResult(UserId userId, Seat.UserFreedUpASeat userFreedUpASeat) {
        return new PlayerFreedUpASeat(userId, userFreedUpASeat.getFreedUpSeatNumber(), adminIdOption, toDto());
    }

    public boolean userIsAdmin(UserId userId) {
        return adminIdOption
                .map(AdminId::getId)
                .map(adminId -> userId.getId().equals(adminId))
                .getOrElse(false);
    }

    public void terminate() {
        userRegistry.clear();
        seats.values().forEach(Seat::freeUp);
    }

    public Set<com.noscompany.snake.game.online.contract.messages.seats.Seat> toDto() {
        return seatsStream()
                .map(Seat::toDto)
                .collect(toSet());
    }

    private void selectAdminIfThereIsNone() {
        if (adminIdOption.isDefined()) {
            log.info("admin is already selected, no need to choose one");
            return;
        }
        selectNewAdmin();
    }

    private void selectNewAdmin() {
        findAnyTakenPlace()
                .peek(this::logSelectedAsAdmin)
                .flatMap(Seat::getUserId)
                .peek(userId -> adminIdOption = Option.of(AdminId.of(userId)))
                .onEmpty(() -> adminIdOption = Option.none());
    }

    private void logSelectedAsAdmin(Seat seat) {
        log.info("seat {} selected as admin, user id {}, name {}", seat.getPlayerNumber(), getUserIdString(seat), getUserNameString(seat));
    }

    private String getUserIdString(Seat seat) {
        return seat.getUserId().map(UserId::getId).getOrElse("");
    }

    private String getUserNameString(Seat seat) {
        return seat.getUserName().map(UserName::getName).getOrElse("");
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

    public boolean userIsSitting(UserId userId) {
        return findSeatBy(userId).isDefined();
    }

    public void gameStarted() {
        gameIsRunning.set(true);
    }

    public void gameFinished() {
        gameIsRunning.set(false);
    }

    public boolean adminIsChosen() {
        return adminIdOption.isDefined();
    }
}