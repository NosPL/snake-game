package com.noscompany.snake.game.online.host.room.after.entering;

import com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerTookASeat;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import org.junit.Assert;
import org.junit.Test;

import static com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat.seatAlreadyTaken;
import static org.junit.Assert.assertEquals;

public class TakeASeatTest extends ActorEnteredTheRoomSetup {

    @Test
    public void actorShouldTakeAFreeLobbySeat() {
//        GIVEN that the actor did not take a seat
        assert !room.userIsSitting(actorId);
//        WHEN he tries to take any free seat
        var freeSeatNumber = freeSeatNumber();
        var result = room.takeASeat(actorId, freeSeatNumber);
//        THEN he succeeds
        var expected = success(playerTookASeat(actorName.getName(), freeSeatNumber));
        Assert.assertEquals(expected, result);
    }

    private PlayerTookASeat playerTookASeat(String userName, PlayerNumber playerNumber) {
        return new PlayerTookASeat(userName, playerNumber, lobbyState());
    }

    @Test
    public void actorShouldFailToTakeASeatThatIsAlreadyTaken() {
//        GIVEN that some user took some seat
        var takenSeatNumber = freeSeatNumber();
        assert isSuccess(takeASeatWithRandomUser(takenSeatNumber));
//        WHEN the actor tries to take this taken seat
        var result = room.takeASeat(actorId, takenSeatNumber);
//        THEN he fails due to seat being already taken
        var expected = failure(seatAlreadyTaken());
        Assert.assertEquals(expected, result);
    }

    @Test
    public void userShouldFailToTakeAFreeSeatIfGameIsRunning() {
//        GIVEN that the actor took a seat
        assert isSuccess(room.takeASeat(actorId, freeSeatNumber()));
//        and game is running
        room.startGame(actorId);
        assert gameIsRunning();
//        WHEN some other user tries to take the seat
        var result = someRandomUserTakesASeat();
//        THEN he fails because game is running
        var expected = failure(FailedToTakeASeat.gameAlreadyRunning());
        assertEquals(expected, result);
    }
}