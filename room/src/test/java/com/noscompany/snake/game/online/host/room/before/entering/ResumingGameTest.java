package com.noscompany.snake.game.online.host.room.before.entering;

import com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToResumeGame;
import io.vavr.control.Option;
import org.junit.Assert;
import org.junit.Test;

public class ResumingGameTest extends ActorNotInTheRoomSetup {

    @Test
    public void actorShouldFailToResumeGame() {
//        WHEN the actor tries to resume game
        var result = room.resumeGame(actorId);
//        THEN he fails due to not being in the room
        var expected = Option.of(FailedToResumeGame.userNotInTheRoom(actorId));
        Assert.assertEquals(expected, result);
    }
}