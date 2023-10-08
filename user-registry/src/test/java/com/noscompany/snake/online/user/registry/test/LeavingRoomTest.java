package com.noscompany.snake.online.user.registry.test;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserLeftRoom;
import io.vavr.control.Option;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public final class LeavingRoomTest extends Setup {

    @Test
    public void leavingRoomWithoutEnteringItBeforeShouldReturnNoEvent() {
//        WHEN actor tries to leave without entering first
        var possibleEvent = userRegistry.leaveTheRoom(UserId.random());
//        THEN nothing happens
        assert possibleEvent.isEmpty();
    }

    @Test
    public void leavingRoomAfterEnteringAndLeavingShouldReturnNoEvent() {
//        GIVEN that actor entered room
        assert userRegistry.enterRoom(actorId, correctUniqueUsername()).isRight();
//        AND actor left the room
        assert userRegistry.leaveTheRoom(actorId).isDefined();
//        WHEN actor tries to leave without entering first
        var possibleEvent = userRegistry.leaveTheRoom(UserId.random());
//        THEN nothing happens
        assert possibleEvent.isEmpty();
    }

    @Test
    public void leaveRoomHappyPath() {
//        GIVEN that actor entered room
        var userName = correctUniqueUsername();
        assert userRegistry.enterRoom(actorId, userName).isRight();
//        WHEN actor tries to leave
        var result = userRegistry.leaveTheRoom(actorId);
//        THEN he succeeds
        var expected = Option.of(new UserLeftRoom(actorId, userName, Set.of()));
        Assert.assertEquals(expected, result);
    }
}