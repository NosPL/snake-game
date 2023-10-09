package com.noscompany.snake.game.online.playground.test.after.entering;

import com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToPauseGame;
import io.vavr.control.Option;
import org.junit.Assert;
import org.junit.Test;

public class PausingGameTest extends ActorEnteredTheRoomSetup {

    @Test
    public void pausingGameShouldNotReturnFailure() {
//        GIVEN that actor took a seat
        assert isSuccess(playground.takeASeat(actorId, freeSeatNumber()));
//        AND actor is the admin
        assert playground.userIsAdmin(actorId);
//        AND game is started
        isSuccess(playground.startGame(actorId));
//        WHEN the actor tries to pause game
        var result = playground.pauseGame(actorId);
//        THEN he succeeds
        var expected = Option.none();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToPauseGameWithoutTakingASeat() {
//        WHEN the actor tries to pause game
        var result = playground.pauseGame(actorId);
//        THEN he fails due to not taking a seat
        var expected = Option.of(FailedToPauseGame.playerDidNotTakeASeat(actorId));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToPauseGameWithoutBeingAdmin() {
//        GIVEN that someone else is an admin
        someRandomUserTakesASeat();
        adminIsChosen();
//        AND actor took a seat
        assert isSuccess(playground.takeASeat(actorId, freeSeatNumber()));
//        WHEN the actor tries to pause game
        var result = playground.pauseGame(actorId);
//        THEN he fails due to not being an admin
        var expected = Option.of(FailedToPauseGame.playerIsNotAdmin(actorId));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToPauseGameWhenGameIsNotStarted() {
//        GIVEN that actor took a seat
        assert isSuccess(playground.takeASeat(actorId, freeSeatNumber()));
//        AND actor is the admin
        assert playground.userIsAdmin(actorId);
//        WHEN actor tries to pause game
        var result = playground.pauseGame(actorId);
//        THEN he fails because game is not running
        var expected = Option.of(FailedToPauseGame.gameNotStarted(actorId));
        Assert.assertEquals(expected, result);
    }
}