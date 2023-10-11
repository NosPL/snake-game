package com.noscompany.snake.game.online.seats.test;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerTookASeat;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat.seatAlreadyTaken;
import static org.junit.Assert.assertEquals;

public class TakeASeatTest extends TestSetup {

    @Test
    public void actorShouldTakeAFreeSeat() {
//        GIVEN that the actor did not take a seat
        assert !seats.userIsSitting(actorId);
//        WHEN he tries to take any free seat
        var freeSeatNumber = freeSeatNumber();
        var result = seats.takeOrChangeSeat(actorId, freeSeatNumber);
//        THEN he succeeds
        var expected = success(playerTookASeat(actorId, actorName, freeSeatNumber));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToTakeSeatAfterLeavingRoom() {
//        GIVEN that actor left the room
        seats.userLeftRoom(actorId);
//        WHEN the actor tries to take a seat
        var result = seats.takeOrChangeSeat(actorId, freeSeatNumber());
//        THEN he fails due to not being in the room
        var expected = failure(FailedToTakeASeat.userNotInTheRoom(actorId));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToTakeASeatThatIsAlreadyTaken() {
//        GIVEN that some user took some seat
        var takenSeatNumber = freeSeatNumber();
        assert isSuccess(randomUserTakesASeat(takenSeatNumber));
//        WHEN the actor tries to take this taken seat
        var result = seats.takeOrChangeSeat(actorId, takenSeatNumber);
//        THEN he fails due to seat being already taken
        var expected = failure(seatAlreadyTaken(actorId));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToTakeASeatIfGameIsRunning() {
//        GIVEN that game was started
        seats.gameStarted();
//        WHEN the actor tries to take a seat
        var result = seats.takeOrChangeSeat(actorId, freeSeatNumber());
//        THEN he fails due to not being in the room
        var expected = failure(FailedToTakeASeat.gameAlreadyRunning(actorId));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorShouldTakeASeatIfGameWasFinished() {
//        GIVEN that game was started
        seats.gameStarted();
//        AND game was finished
        seats.gameFinished();
//        WHEN the actor tries to take a seat
        var seatNumber = freeSeatNumber();
        var result = seats.takeOrChangeSeat(actorId, seatNumber);
//        THEN he fails due to not being in the room
        var expected = success(playerTookASeat(actorId, actorName, seatNumber));
        Assert.assertEquals(expected, result);
    }

    private PlayerTookASeat playerTookASeat(UserId actorId, UserName userName, PlayerNumber playerNumber) {
        return new PlayerTookASeat(actorId, userName, playerNumber, seats.getAdminId().get(), seats.toDto());
    }

    @Before
    public void AfterEnteringRoomInit() {
    }
}