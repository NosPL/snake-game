package com.noscompany.snake.game.online.playground.test.after.entering;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToFreeUpSeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerFreedUpASeat;
import io.vavr.control.Option;
import org.junit.Assert;
import org.junit.Test;

public class FreeUpASeatTest extends ActorEnteredTheRoomSetup {

    @Test
    public void actorShouldFreeUpASeatAfterTakingASeat() {
//        GIVEN that the actor took a seat
        var playerNumber = freeSeatNumber();
        assert isSuccess(playground.takeASeat(actorId, playerNumber));
//        WHEN he tries to free up a seat
        var result = playground.freeUpASeat(actorId);
//        THEN he succeeds
        var expected = success(playerFreedUpASeat(actorId, actorName.getName(), playerNumber));
        Assert.assertEquals(expected, result);
    }

    private PlayerFreedUpASeat playerFreedUpASeat(UserId userId, String userName, PlayerNumber playerNumber) {
        return new PlayerFreedUpASeat(userId, userName, playerNumber, lobbyState());
    }

    @Test
    public void actorShouldFailToFreeUpASeatWithoutTakingASeatFirst() {
//        GIVEN that the actor did not take a seat
        assert !playground.userIsSitting(actorId);
//        WHEN he tries to free up a seat
        var result = playground.freeUpASeat(actorId);
//        THEN he fails due to not being in the room
        var expected = failure(FailedToFreeUpSeat.userDidNotTakeASeat(actorId));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorWhoTookASeatShouldFreeItUpAfterLeavingTheRoom() {
//        GIVEN that the actor took a seat
        var playerNumber = freeSeatNumber();
        assert isSuccess(playground.takeASeat(actorId, playerNumber));
//        WHEN he tries to free up a seat
        var result = playground.userLeftRoom(actorId);
//        THEN he succeeds
        var expected = Option.of(playerFreedUpASeat(actorId, actorName.getName(), playerNumber));
        Assert.assertEquals(expected, result);
    }
}