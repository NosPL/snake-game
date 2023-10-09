package com.noscompany.snake.game.online.seats;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
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

    static Seat create(PlayerNumber playerNumber) {
        return new Seat(playerNumber, Option.none(), Option.none());
    }

    Either<FailedToTakeASeat, UserSuccessfullyTookASeat> take(UserId userId, UserName userName) {
        if (isTaken())
            return left(FailedToTakeASeat.seatAlreadyTaken(userId));
        this.userId = Option.of(userId);
        this.userName = of(userName);
        return right(new UserSuccessfullyTookASeat(userId, userName, playerNumber));
    }

    Option<UserFreedUpASeat> freeUp() {
        return this.userName
                .map(userName -> new UserFreedUpASeat(userId.get(), userName, playerNumber))
                .peek(userName -> reset());
    }

    private void reset() {
        userId = Option.none();
        userName = Option.none();
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
        this.reset();
        return right(new UserSuccessfullyTookASeat(userId, newSeat.userName.get(), newSeat.playerNumber));
    }

    com.noscompany.snake.game.online.contract.messages.seats.Seat toDto() {
        return new com.noscompany.snake.game.online.contract.messages.seats.Seat(playerNumber, userId, userName, userId.isDefined());
    }

    @Value
    static class UserFreedUpASeat {
        UserId userId;
        UserName userName;
        PlayerNumber freedUpSeatNumber;
    }

    @Value
    static class UserSuccessfullyTookASeat {
        UserId userId;
        UserName userName;
        PlayerNumber playerNumber;
    }
}