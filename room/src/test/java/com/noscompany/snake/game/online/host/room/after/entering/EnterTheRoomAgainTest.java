package com.noscompany.snake.game.online.host.room.after.entering;

import org.junit.Assert;
import org.junit.Test;

import static com.noscompany.snake.game.online.contract.messages.room.FailedToEnterRoom.userAlreadyInTheRoom;
import static org.junit.Assert.assertEquals;

public class EnterTheRoomAgainTest extends ActorEnteredTheRoomSetup {

    @Test
    public void actorShouldFailToEnterTheRoomForTheSecondTimeWithADifferentName() {
//        WHEN the actor tries to enter the room again with a different name
        var result = room.enter(actorId, randomValidUserName());
//        THEN he fails because he entered the room already
        var expected = failure(userAlreadyInTheRoom(actorId));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToEnterTheRoomForTheSecondTimeWithTheSameName() {
//        WHEN the actor tries to enter the room again with the same name
        var result = room.enter(actorId, actorName);
//        THEN he fails due to being in the room already
        var expected = failure(userAlreadyInTheRoom(actorId));
        Assert.assertEquals(expected, result);
    }
}