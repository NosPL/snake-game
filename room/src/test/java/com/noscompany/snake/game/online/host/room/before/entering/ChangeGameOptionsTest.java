package com.noscompany.snake.game.online.host.room.before.entering;

import com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ChangeGameOptionsTest extends ActorNotInTheRoomSetup {

    @Test
    public void actorShouldFailToChangeGameOptions() {
//        WHEN the actor tries to change game options
        var result = room.changeGameOptions(actorId, newGameOptions());
//        THEN he fails due to not being in the room
        var expected = failure(FailedToChangeGameOptions.userNotInTheRoom());
        Assert.assertEquals(expected, result);
    }
}