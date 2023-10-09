package com.noscompany.snake.game.online.playground.test.after.entering;

import com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToCancelGame;
import com.noscompany.snake.game.online.contract.messages.seats.AdminId;
import com.noscompany.snake.game.online.playground.test.commons.PlaygroundTestSetup;
import io.vavr.control.Option;
import org.junit.Assert;
import org.junit.Test;

public class CancellingTest extends PlaygroundTestSetup {

    @Test
    public void cancellingGameShouldNotReturnFailure() {
//        GIVEN that actor took a seat as admin
        playground.playerTookASeat(actorId, anyPlayerNumber(), AdminId.of(actorId));
//        AND game was started
        isSuccess(playground.startGame(actorId));
//        WHEN the actor tries to cancel game
        var result = playground.cancelGame(actorId);
//        THEN he succeeds
        var expected = Option.none();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToCancelGameWithoutBeingAdmin() {
//        WHEN the actor tries to cancel game
        var result = playground.cancelGame(actorId);
//        THEN he fails due to not being an admin
        var expected = Option.of(FailedToCancelGame.playerIsNotAdmin(actorId));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToCancelGameWhenGameIsNotStarted() {
//        GIVEN that actor took a seat as admin
        playground.playerTookASeat(actorId, anyPlayerNumber(), AdminId.of(actorId));
//        WHEN actor tries to cancel game
        var result = playground.cancelGame(actorId);
//        THEN he fails because game is not running
        var expected = Option.of(FailedToCancelGame.gameNotStarted(actorId));
        Assert.assertEquals(expected, result);
    }
}