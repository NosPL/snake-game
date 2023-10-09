package com.noscompany.snake.game.online.playground.test.after.entering;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerTookASeat;
import org.junit.Assert;
import org.junit.Test;

import static com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat.seatAlreadyTaken;
import static org.junit.Assert.assertEquals;

public class TakeASeatTest extends ActorEnteredTheRoomSetup {

    @Test
    public void actorShouldTakeAFreeLobbySeat() {
//        GIVEN that the actor did not take a seat
        assert !playground.userIsSitting(actorId);
//        WHEN he tries to take any free seat
        var freeSeatNumber = freeSeatNumber();
        var result = playground.takeASeat(actorId, freeSeatNumber);
//        THEN he succeeds
        var expected = success(playerTookASeat(actorId, actorName.getName(), freeSeatNumber));
        Assert.assertEquals(expected, result);
    }

    private PlayerTookASeat playerTookASeat(UserId actorId, String userName, PlayerNumber playerNumber) {
        return new PlayerTookASeat(actorId, userName, playerNumber, lobbyState());
    }

    @Test
    public void actorShouldFailToTakeASeatThatIsAlreadyTaken() {
//        GIVEN that some user took some seat
        var takenSeatNumber = freeSeatNumber();
        assert isSuccess(takeASeatWithRandomUser(takenSeatNumber));
//        WHEN the actor tries to take this taken seat
        var result = playground.takeASeat(actorId, takenSeatNumber);
//        THEN he fails due to seat being already taken
        var expected = failure(seatAlreadyTaken(actorId));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void userShouldFailToTakeAFreeSeatIfGameIsRunning() {
//        GIVEN that the actor took a seat
        assert isSuccess(playground.takeASeat(actorId, freeSeatNumber()));
//        and game is running
        playground.startGame(actorId);
        assert gameIsRunning();
//        WHEN some other user tries to take the seat
        var result = someRandomUserTakesASeat();
//        THEN he fails because game is running
        var expected = failure(FailedToTakeASeat.gameAlreadyRunning(result.getLeft().getUserId()));
        assertEquals(expected, result);
    }
}