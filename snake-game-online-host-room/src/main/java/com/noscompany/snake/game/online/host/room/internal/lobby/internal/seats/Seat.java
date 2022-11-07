package com.noscompany.snake.game.online.host.room.internal.lobby.internal.seats;

import com.noscompany.snake.game.online.contract.messages.lobby.LobbyState;
import com.noscompany.snake.game.online.contract.messages.lobby.event.FailedToTakeASeat;
import com.noscompany.snake.game.online.contract.messages.game.dto.PlayerNumber;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static io.vavr.control.Either.left;
import static io.vavr.control.Option.of;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
@Getter
class Seat {
    private final PlayerNumber playerNumber;
    private volatile Option<String> userName;
    private volatile boolean isAdmin;
    private volatile boolean isTaken;

    static Seat create(PlayerNumber playerNumber) {
        return new Seat(playerNumber, Option.none(), false, false);
    }

    Either<FailedToTakeASeat, UserSuccessfullyTookASeat> take(String userName) {
        if (isTaken())
            return left(FailedToTakeASeat.seatAlreadyTaken());
        this.userName = of(userName);
        this.isTaken = true;
        return Either.right(new UserSuccessfullyTookASeat(userName, playerNumber));
    }

    Option<UserFreedUpASeat> freeUp() {
        return this.userName
                .peek(userName -> reset())
                .map(userName -> new UserFreedUpASeat(userName, playerNumber));
    }

    private void reset() {
        userName = Option.none();
        isAdmin = false;
        isTaken = false;
    }

    boolean isTaken() {
        return userName.isDefined();
    }

    boolean isTakenBy(String userName) {
        return this.userName.exists(userName::equals);
    }

    Either<FailedToTakeASeat, UserSuccessfullyTookASeat> changeTo(Seat newSeat) {
        if (newSeat.isTaken())
            return left(FailedToTakeASeat.seatAlreadyTaken());
        newSeat.userName = this.userName;
        newSeat.isAdmin = this.isAdmin;
        this.reset();
        return Either.right(new UserSuccessfullyTookASeat(newSeat.userName.get(), newSeat.playerNumber));
    }

    void chooseAsAdmin() {
        this.isAdmin = true;
    }

    LobbyState.Seat toDto() {
        return new LobbyState.Seat(playerNumber, userName, isAdmin, isTaken);
    }
}