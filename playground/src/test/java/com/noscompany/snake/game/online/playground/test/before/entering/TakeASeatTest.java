package com.noscompany.snake.game.online.playground.test.before.entering;

import com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat;
import org.junit.Assert;
import org.junit.Test;

public class TakeASeatTest extends ActorNotInTheRoomSetup {

    @Test
    public void actorShouldFailToTakeAFreeSeat() {
//        WHEN the actor tries to take a seat
        var result = playground.takeASeat(actorId, freeSeatNumber());
//        THEN he fails due to not being in the room
        var expected = failure(FailedToTakeASeat.userNotInTheRoom(actorId));
        Assert.assertEquals(expected, result);
    }
}