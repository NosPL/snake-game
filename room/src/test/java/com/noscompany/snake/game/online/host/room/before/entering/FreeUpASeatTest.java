package com.noscompany.snake.game.online.host.room.before.entering;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToFreeUpSeat;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FreeUpASeatTest extends ActorNotInTheRoomSetup {

    @Test
    public void actorShouldFailToFreeUpASeat() {
//        WHEN the actor tries to free up a seat
        UserId randomUserId = randomUserId();
        var result = room.freeUpASeat(randomUserId);
//        THEN he fails due to not being in the room
        var expected = failure(FailedToFreeUpSeat.userNotInTheRoom(randomUserId));
        Assert.assertEquals(expected, result);
    }
}