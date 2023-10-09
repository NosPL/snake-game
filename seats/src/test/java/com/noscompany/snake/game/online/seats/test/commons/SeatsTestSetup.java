package com.noscompany.snake.game.online.seats.test.commons;

import com.noscompany.message.publisher.utils.NullMessagePublisher;
import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.seats.Seat;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;
import com.noscompany.snake.game.online.seats.Seats;
import com.noscompany.snake.game.online.seats.SeatsConfiguration;
import io.vavr.control.Either;
import org.junit.Before;

public class SeatsTestSetup {
    protected Seats seats;
    protected UserId actorId;
    protected UserName actorName;

    @Before
    public void init() {
        seats = new SeatsConfiguration().create(new NullMessagePublisher());
        actorId = UserId.random();
        actorName = UserName.random();
    }

    protected <L, R> Either<L, R> failure(L failure) {
        return Either.left(failure);
    }

    protected <L, R> Either<L, R> success(R success) {
        return Either.right(success);
    }

    protected <L, R> boolean isSuccess(Either either) {
        return either.isRight();
    }

    protected PlayerNumber freeSeatNumber() {
        return seats
                .toDto()
                .stream()
                .filter(seat -> !seat.isTaken())
                .map(Seat::getPlayerNumber)
                .findAny().get();
    }

    protected boolean adminIsChosen() {
        return seats.adminIsChosen();
    }

    protected Either someRandomUserTakesASeat() {
        return takeASeatWithRandomUser(freeSeatNumber());
    }

    protected Either takeASeatWithRandomUser(PlayerNumber playerNumber) {
        var userId = UserId.random();
        var userName = UserName.random();
        seats.newUserEnteredRoom(userId, userName);
        return seats.takeOrChangeSeat(userId, playerNumber);
    }
}