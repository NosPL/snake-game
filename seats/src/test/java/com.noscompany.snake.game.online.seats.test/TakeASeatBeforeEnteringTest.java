package com.noscompany.snake.game.online.seats.test;

import com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat;
import com.noscompany.snake.game.online.seats.test.commons.SeatsTestSetup;
import org.junit.Assert;
import org.junit.Test;

public class TakeASeatBeforeEnteringTest extends SeatsTestSetup {

    @Test
    public void actorShouldFailToTakeAFreeSeat() {
//        WHEN the actor tries to take a seat
        var result = seats.takeOrChangeSeat(actorId, freeSeatNumber());
//        THEN he fails due to not being in the room
        var expected = failure(FailedToTakeASeat.userNotInTheRoom(actorId));
        Assert.assertEquals(expected, result);
    }
}