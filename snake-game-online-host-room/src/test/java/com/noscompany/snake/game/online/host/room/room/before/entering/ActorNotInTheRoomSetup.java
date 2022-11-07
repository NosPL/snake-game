package com.noscompany.snake.game.online.host.room.room.before.entering;

import com.noscompany.snake.game.online.host.room.room.commons.RoomTestSetup;
import org.junit.Before;

public class ActorNotInTheRoomSetup extends RoomTestSetup {

    @Before
    public void assertThatActorDidNotEnterTheRoom() {
        assert !room.hasUserWithId(actorId);
    }
}