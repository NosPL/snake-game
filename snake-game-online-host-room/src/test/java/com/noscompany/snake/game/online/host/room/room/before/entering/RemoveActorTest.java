package com.noscompany.snake.game.online.host.room.room.before.entering;

import org.junit.Test;

public class RemoveActorTest extends ActorNotInTheRoomSetup {

    @Test
    public void removingActorThatDidNotEnterTheRoomShouldNotProduceAnyEvent() {
//        WHEN someone tries to remove the actor from the room
        var possibleEvent = room.removeUserById(actorId);
//        THEN nothing happens
        assert possibleEvent.isEmpty();
    }
}