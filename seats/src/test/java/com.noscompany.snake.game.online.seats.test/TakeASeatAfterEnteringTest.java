package com.noscompany.snake.game.online.seats.test;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerTookASeat;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;
import com.noscompany.snake.game.online.seats.test.commons.AfterEnteringRoom;
import com.noscompany.snake.game.online.seats.test.commons.SeatsTestSetup;
import org.junit.Assert;
import org.junit.Test;

import static com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat.seatAlreadyTaken;
import static org.junit.Assert.assertEquals;

public class TakeASeatAfterEnteringTest extends AfterEnteringRoom {

    @Test
    public void actorShouldTakeAFreeLobbySeat() {
//        GIVEN that the actor did not take a seat
        assert !seats.userIsSitting(actorId);
//        WHEN he tries to take any free seat
        var freeSeatNumber = freeSeatNumber();
        var result = seats.takeOrChangeSeat(actorId, freeSeatNumber);
//        THEN he succeeds
        var expected = success(playerTookASeat(actorId, actorName, freeSeatNumber));
        Assert.assertEquals(expected, result);
    }

    private PlayerTookASeat playerTookASeat(UserId actorId, UserName userName, PlayerNumber playerNumber) {
        return new PlayerTookASeat(actorId, userName, playerNumber, seats.getAdminId().get(), seats.toDto());
    }

    @Test
    public void actorShouldFailToTakeASeatThatIsAlreadyTaken() {
//        GIVEN that some user took some seat
        var takenSeatNumber = freeSeatNumber();
        assert isSuccess(takeASeatWithRandomUser(takenSeatNumber));
//        WHEN the actor tries to take this taken seat
        var result = seats.takeOrChangeSeat(actorId, takenSeatNumber);
//        THEN he fails due to seat being already taken
        var expected = failure(seatAlreadyTaken(actorId));
        Assert.assertEquals(expected, result);
    }
}