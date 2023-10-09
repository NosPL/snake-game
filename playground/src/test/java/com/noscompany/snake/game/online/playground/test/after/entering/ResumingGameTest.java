package com.noscompany.snake.game.online.playground.test.after.entering;

import com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToResumeGame;
import io.vavr.control.Option;
import org.junit.Assert;
import org.junit.Test;

public class ResumingGameTest extends ActorEnteredTheRoomSetup {

    @Test
    public void resumingGameShouldNotReturnFailure() {
//        GIVEN that actor took a seat
        assert isSuccess(playground.takeASeat(actorId, freeSeatNumber()));
//        AND actor is the admin
        assert playground.userIsAdmin(actorId);
//        AND game is started
        isSuccess(playground.startGame(actorId));
//        WHEN the actor tries to resume game
        var result = playground.resumeGame(actorId);
//        THEN he succeeds
        var expected = Option.none();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToResumeGameWithoutTakingASeat() {
//        WHEN the actor tries to resume game
        var result = playground.resumeGame(actorId);
//        THEN he fails due to not taking a seat
        var expected = Option.of(FailedToResumeGame.playerDidNotTakeASeat(actorId));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToResumingGameWithoutBeingAdmin() {
//        GIVEN that someone else is an admin
        someRandomUserTakesASeat();
        adminIsChosen();
//        AND actor took a seat
        assert isSuccess(playground.takeASeat(actorId, freeSeatNumber()));
//        WHEN the actor tries to resume game
        var result = playground.resumeGame(actorId);
//        THEN he fails due to not being an admin
        var expected = Option.of(FailedToResumeGame.playerIsNotAdmin(actorId));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToResumeGameWhenGameIsNotStarted() {
//        GIVEN that actor took a seat
        assert isSuccess(playground.takeASeat(actorId, freeSeatNumber()));
//        AND actor is the admin
        assert playground.userIsAdmin(actorId);
//        WHEN actor tries to resume game
        var result = playground.resumeGame(actorId);
//        THEN he fails because game is not running
        var expected = Option.of(FailedToResumeGame.gameNotStarted(actorId));
        Assert.assertEquals(expected, result);
    }
}