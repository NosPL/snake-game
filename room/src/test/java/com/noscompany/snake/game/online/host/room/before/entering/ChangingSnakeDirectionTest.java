package com.noscompany.snake.game.online.host.room.before.entering;

import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToChangeSnakeDirection;
import io.vavr.control.Option;
import org.junit.Assert;
import org.junit.Test;

public class ChangingSnakeDirectionTest extends ActorNotInTheRoomSetup {

    @Test
    public void actorShouldFailToChangeSnakeDirection() {
//        WHEN the actor tries to change snake direction
        var result = room.changeSnakeDirection(actorId, Direction.DOWN);
//        THEN he fails due to not being in the room
        var expected = Option.of(FailedToChangeSnakeDirection.userNotInTheRoom());
        Assert.assertEquals(expected, result);
    }
}