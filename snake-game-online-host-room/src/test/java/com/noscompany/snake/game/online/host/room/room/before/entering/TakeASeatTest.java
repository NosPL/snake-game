package com.noscompany.snake.game.online.host.room.room.before.entering;

import com.noscompany.snake.game.online.contract.messages.lobby.event.FailedToTakeASeat;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TakeASeatTest extends ActorNotInTheRoomSetup {

    @Test
    public void actorShouldFailToTakeAFreeSeat() {
//        WHEN the actor tries to take a seat
        var result = room.takeASeat(actorId, freeSeatNumber());
//        THEN he fails due to not being in the room
        var expected = failure(FailedToTakeASeat.userNotInTheRoom());
        assertEquals(expected, result);
    }
}