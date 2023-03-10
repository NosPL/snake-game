package com.noscompany.snake.game.online.host.room.before.entering;

import com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToPauseGame;
import io.vavr.control.Option;
import org.junit.Assert;
import org.junit.Test;

public class PausingGameTest extends ActorNotInTheRoomSetup {

    @Test
    public void actorShouldFailToPauseGame() {
//        WHEN the actor tries to pause game
        var result = room.pauseGame(actorId);
//        THEN he fails due to not being in the room
        var expected = Option.of(FailedToPauseGame.userNotInTheRoom());
        Assert.assertEquals(expected, result);
    }
}