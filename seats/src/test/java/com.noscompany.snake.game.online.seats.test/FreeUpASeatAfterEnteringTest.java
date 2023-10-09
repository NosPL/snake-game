package com.noscompany.snake.game.online.seats.test;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToFreeUpSeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerFreedUpASeat;
import io.vavr.control.Option;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FreeUpASeatAfterEnteringTest extends TestSetup {

    @Test
    public void actorShouldFreeUpASeatAfterTakingASeat() {
//        GIVEN that the actor took a seat
        var playerNumber = freeSeatNumber();
        assert isSuccess(seats.takeOrChangeSeat(actorId, playerNumber));
//        WHEN he tries to free up a seat
        var result = seats.freeUpSeat(actorId);
//        THEN he succeeds
        var expected = success(playerFreedUpASeat(actorId, playerNumber));
        Assert.assertEquals(expected, result);
    }

    private PlayerFreedUpASeat playerFreedUpASeat(UserId userId, PlayerNumber playerNumber) {
        return new PlayerFreedUpASeat(userId, playerNumber, seats.getAdminId(), seats.toDto());
    }

    @Test
    public void actorShouldFailToFreeUpASeatWithoutTakingASeatFirst() {
//        GIVEN that the actor did not take a seat
        assert !seats.userIsSitting(actorId);
//        WHEN he tries to free up a seat
        var result = seats.freeUpSeat(actorId);
//        THEN he fails due to not being in the room
        var expected = failure(FailedToFreeUpSeat.userDidNotTakeASeat(actorId));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorWhoTookASeatShouldFreeItUpAfterLeavingTheRoom() {
//        GIVEN that the actor took a seat
        var playerNumber = freeSeatNumber();
        assert isSuccess(seats.takeOrChangeSeat(actorId, playerNumber));
//        WHEN he tries to free up a seat
        var result = seats.userLeftRoom(actorId);
//        THEN he succeeds
        var expected = Option.of(playerFreedUpASeat(actorId, playerNumber));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToFreeUpASeat() {
//        WHEN the actor tries to free up a seat
        UserId randomUserId = UserId.random();
        var result = seats.freeUpSeat(randomUserId);
//        THEN he fails due to not being in the room
        var expected = failure(FailedToFreeUpSeat.userNotInTheRoom(randomUserId));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void reactionToUserLeavingTheRoomShouldBeEmpty() {
//        WHEN actor left the room
        var result = seats.userLeftRoom(actorId);
//        THEN nothing happens
        var expected = Option.none();
        Assert.assertEquals(expected, result);
    }

    @Before
    public void AfterEnteringRoomInit() {
    }
}