package com.noscompany.snake.game.online.host.room.internal.playground;

import com.noscompany.snake.game.online.contract.messages.playground.PlaygroundState;
import com.noscompany.snake.game.online.contract.messages.room.UserName;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.host.room.dto.UserId;
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
            return left(FailedToTakeASeat.seatAlreadyTaken());
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

    Either<FailedToTakeASeat, UserSuccessfullyTookASeat> changeTo(Seat newSeat) {
        if (newSeat.isTaken())
            return left(FailedToTakeASeat.seatAlreadyTaken());
        newSeat.userId = this.userId;
        newSeat.userName = this.userName;
        newSeat.isAdmin = this.isAdmin;
        newSeat.isTaken = true;
        this.reset();
        return right(new UserSuccessfullyTookASeat(newSeat.userName.get(), newSeat.playerNumber));
    }

    void chooseAsAdmin() {
        this.isAdmin = true;
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