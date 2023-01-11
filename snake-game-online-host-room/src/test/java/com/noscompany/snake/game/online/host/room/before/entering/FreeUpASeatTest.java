package com.noscompany.snake.game.online.host.room.before.entering;

import com.noscompany.snake.game.online.contract.messages.lobby.event.FailedToFreeUpSeat;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FreeUpASeatTest extends ActorNotInTheRoomSetup {

    @Test
    public void actorShouldFailToFreeUpASeat() {
//        WHEN the actor tries to free up a seat
        var result = room.freeUpASeat(randomUserId());
//        THEN he fails due to not being in the room
        var expected = failure(FailedToFreeUpSeat.userNotInTheRoom());
        Assert.assertEquals(expected, result);
    }
}