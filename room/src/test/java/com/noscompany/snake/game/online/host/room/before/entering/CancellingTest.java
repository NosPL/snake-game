package com.noscompany.snake.game.online.host.room.before.entering;

import com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToCancelGame;
import io.vavr.control.Option;
import org.junit.Assert;
import org.junit.Test;

public class CancellingTest extends ActorNotInTheRoomSetup {

    @Test
    public void actorShouldFailToCancelGame() {
//        WHEN the actor tries to cancel game
        var result = room.cancelGame(actorId);
//        THEN he fails due to not being in the room
        var expected = Option.of(FailedToCancelGame.userNotInTheRoom());
        Assert.assertEquals(expected, result);
    }
}