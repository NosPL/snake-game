package com.noscompany.snake.game.online.playground;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.playground.PlaygroundState;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;
import static io.vavr.control.Option.of;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
@Getter
@Slf4j
class Seat {
    private final PlayerNumber playerNumber;
    private Option<UserId> userId;
    private Option<UserName> userName;
    private boolean isAdmin;
    private boolean isTaken;

    static Seat create(PlayerNumber playerNumber) {
        return new Seat(playerNumber, Option.none(), Option.none(), false, false);
    }

    Either<FailedToTakeASeat, UserSuccessfullyTookASeat> take(UserId userId, UserName userName) {
        if (isTaken())
            return left(FailedToTakeASeat.seatAlreadyTaken(userId));
        this.userId = Option.of(userId);
        this.userName = of(userName);
        this.isTaken = true;
        return right(new UserSuccessfullyTookASeat(userName, playerNumber));
    }

    Option<UserFreedUpASeat> freeUp() {
        return this.userName
                .peek(userName -> reset())
                .map(userName -> new UserFreedUpASeat(userName, playerNumber));
    }

    private void reset() {
        userId = Option.none();
        userName = Option.none();
        isAdmin = false;
        isTaken = false;
    }

    boolean isTaken() {
        return userName.isDefined();
    }

    boolean isTakenBy(UserId userId) {
        return this.userId.exists(userId::equals);
    }

    Either<FailedToTakeASeat, UserSuccessfullyTookASeat> changeTo(UserId userId, Seat newSeat) {
        if (newSeat.isTaken())
            return left(FailedToTakeASeat.seatAlreadyTaken(userId));
        newSeat.userId = this.userId;
        newSeat.userName = this.userName;
        newSeat.isAdmin = this.isAdmin;
        newSeat.isTaken = true;
        this.reset();
        return right(new UserSuccessfullyTookASeat(newSeat.userName.get(), newSeat.playerNumber));
    }

    void chooseAsAdmin() {
        logSelectedAsAdmin();
        this.isAdmin = true;
    }

    private void logSelectedAsAdmin() {
        log.info("seat {} selected as admin, user id {}, name {}", playerNumber, getUserIdString(), getUserNameString());
    }

    private String getUserIdString() {
        return userId.map(UserId::getId).getOrElse("");
    }

    private String getUserNameString() {
        return userName.map(UserName::getName).getOrElse("");
    }

    PlaygroundState.Seat toDto() {
        return new PlaygroundState.Seat(playerNumber, userName.map(UserName::getName), isAdmin, isTaken);
    }

    @Value
    static class UserFreedUpASeat {
        UserName userName;
        PlayerNumber freedUpSeatNumber;
    }

    @Value
    static class UserSuccessfullyTookASeat {
        UserName userName;
        PlayerNumber playerNumber;
    }
}