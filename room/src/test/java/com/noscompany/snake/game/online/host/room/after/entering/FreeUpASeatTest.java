package com.noscompany.snake.game.online.host.room.after.entering;

import com.noscompany.snake.game.online.contract.messages.seats.FailedToFreeUpSeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerFreedUpASeat;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FreeUpASeatTest extends ActorEnteredTheRoomSetup {

    @Test
    public void actorShouldFreeUpASeatAfterTakingASeat() {
//        GIVEN that the actor took a seat
        var playerNumber = freeSeatNumber();
        assert isSuccess(room.takeASeat(actorId, playerNumber));
//        WHEN he tries to free up a seat
        var result = room.freeUpASeat(actorId);
//        THEN he succeeds
        var expected = success(playerFreedUpASeat(actorName, playerNumber));
        Assert.assertEquals(expected, result);
    }

    private PlayerFreedUpASeat playerFreedUpASeat(String userName, PlayerNumber playerNumber) {
        return new PlayerFreedUpASeat(userName, playerNumber, lobbyState());
    }

    @Test
    public void actorShouldFailToFreeUpASeatWithoutTakingASeatFirst() {
//        GIVEN that the actor did not take a seat
        assert !room.userIsSitting(actorId);
//        WHEN he tries to free up a seat
        var result = room.freeUpASeat(actorId);
//        THEN he fails due to not being in the room
        var expected = failure(FailedToFreeUpSeat.userDidNotTakeASeat());
        Assert.assertEquals(expected, result);
    }
}