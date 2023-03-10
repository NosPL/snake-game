package com.noscompany.snake.game.online.host.room.before.entering;

import org.junit.Test;

public class LeavingRoomTest extends ActorNotInTheRoomSetup {

    @Test
    public void leavingRoomWithoutEnteringItBeforeShouldReturnNoEvent() {
//        WHEN someone tries to remove the actor from the room
        var possibleEvent = room.leave(actorId);
//        THEN nothing happens
        assert possibleEvent.isEmpty();
    }
}