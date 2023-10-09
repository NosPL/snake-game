package com.noscompany.snake.game.online.playground.test.before.entering;

import com.noscompany.snake.game.online.playground.test.commons.PlaygroundTestSetup;
import org.junit.Before;

public class ActorNotInTheRoomSetup extends PlaygroundTestSetup {

    @Before
    public void assertThatActorDidNotEnterTheRoom() {
        assert !playground.containsUserWithId(actorId);
    }
}