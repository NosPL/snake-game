package com.noscompany.snake.game.online.playground.test.before.entering;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToFreeUpSeat;
import io.vavr.control.Option;
import org.junit.Assert;
import org.junit.Test;

public class FreeUpASeatTest extends ActorNotInTheRoomSetup {

    @Test
    public void actorShouldFailToFreeUpASeat() {
//        WHEN the actor tries to free up a seat
        UserId randomUserId = randomUserId();
        var result = playground.freeUpASeat(randomUserId);
//        THEN he fails due to not being in the room
        var expected = failure(FailedToFreeUpSeat.userNotInTheRoom(randomUserId));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void reactionToUserLeavingTheRoomShouldBeEmpty() {
//        WHEN actor left the room
        var result = playground.userLeftRoom(actorId);
//        THEN nothing happens
        var expected = Option.none();
        Assert.assertEquals(expected, result);
    }
}