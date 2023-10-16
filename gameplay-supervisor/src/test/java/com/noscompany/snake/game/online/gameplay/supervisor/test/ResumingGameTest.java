package com.noscompany.snake.game.online.gameplay.supervisor.test;

import com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToResumeGame;
import com.noscompany.snake.game.online.contract.messages.seats.AdminId;
import com.noscompany.snake.game.online.gameplay.supervisor.test.commons.PlaygroundTestSetup;
import io.vavr.control.Option;
import org.junit.Assert;
import org.junit.Test;

public class ResumingGameTest extends PlaygroundTestSetup {

    @Test
    public void resumeGameHappyPath() {
//        GIVEN that actor took a seat as admin
        gameplaySupervisor.playerTookASeat(actorId, anyPlayerNumber(), AdminId.of(actorId));
//        AND game is started
        isSuccess(gameplaySupervisor.startGame(actorId));
//        WHEN the actor tries to resume game
        var result = gameplaySupervisor.resumeGame(actorId);
//        THEN he succeeds
        var expected = Option.none();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToResumingGameWithoutBeingAdmin() {
//        GIVEN that actor took a seat, but not as admin
        gameplaySupervisor.playerTookASeat(actorId, anyPlayerNumber(), AdminId.random());
//        WHEN the actor tries to resume game
        var result = gameplaySupervisor.resumeGame(actorId);
//        THEN he fails due to not being an admin
        var expected = Option.of(FailedToResumeGame.playerIsNotAdmin(actorId));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void actorShouldFailToResumeGameWhenGameIsNotStarted() {
//        GIVEN that actor took a seat as admin
        gameplaySupervisor.playerTookASeat(actorId, anyPlayerNumber(), AdminId.of(actorId));
//        WHEN actor tries to resume game
        var result = gameplaySupervisor.resumeGame(actorId);
//        THEN he fails because game is not running
        var expected = Option.of(FailedToResumeGame.gameNotStarted(actorId));
        Assert.assertEquals(expected, result);
    }
}